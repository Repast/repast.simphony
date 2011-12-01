/*
 * Created by JFormDesigner on Thu Oct 11 16:24:57 EDT 2007
 */

package repast.simphony.visualization.gui;

import static repast.simphony.gis.GeometryUtil.GeometryType.LINE;
import static repast.simphony.gis.GeometryUtil.GeometryType.POINT;
import static repast.simphony.gis.GeometryUtil.GeometryType.POLYGON;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.xml.transform.TransformerException;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.Symbolizer;

import repast.simphony.gis.GeometryUtil;
import repast.simphony.gis.data.DataUtilities;
import repast.simphony.gis.styleEditor.HollowFeature;
import repast.simphony.gis.styleEditor.PreviewLabel;
import repast.simphony.gis.styleEditor.StyleDialog;
import repast.simphony.scenario.data.AgentData;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.space.gis.DefaultFeatureAgentFactory;
import repast.simphony.space.gis.FeatureAgentFactoryFinder;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.gis.DisplayGIS;
import simphony.util.messages.MessageCenter;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author User #2
 */
public class GISStylePanel extends JPanel {

  private static MessageCenter msg = MessageCenter.getMessageCenter(GISStylePanel.class);
  private static final String SLD_NS = "<sld:UserStyle xmlns=\"http://www.opengis.net/sld\"";
  private static final String SLD_NS_2 = " xmlns:sld=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\"";

  public static File lastDirectory;

  static class AgentTypeElement {

    String agentName;
    String agentClassName;
    String styleXML;
    Map<GeometryUtil.GeometryType, Style> styleMap = new HashMap<GeometryUtil.GeometryType, Style>();
    File source;
    Class<? extends Geometry> defaultGeometry;

    public AgentTypeElement(String agentName, String agentClassName, String styleXML) {
      this.agentName = agentName;
      this.agentClassName = agentClassName;
      this.styleXML = styleXML;
    }

    public String toString() {
      return agentName;
    }
  }

  private static Geometry point, line, polygon;

  static {
    GeometryFactory gFactory = new GeometryFactory();
    point = gFactory.createPoint(new Coordinate(0, 0));
    line = gFactory.createLineString(new Coordinate[]{new Coordinate(0, 0), new Coordinate(1, 1)});
    polygon = gFactory.createPolygon(gFactory.createLinearRing(new Coordinate[]{
            new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(0, 0)}),
            null);
  }

  private DisplayDescriptor descriptor;
  private Map<GeometryUtil.GeometryType, Style> defaultsMap = new HashMap<GeometryUtil.GeometryType, Style>();
  private StylePreviewer previewer;


  public GISStylePanel() {
    initComponents();
    initMyComponents();
    agentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    DefaultComboBoxModel model = new DefaultComboBoxModel(
            new Object[]{POINT, LINE, POLYGON});
    geomBox.setModel(model);
    addListeners();

    defaultsMap.put(POINT, getDefaultStyle(POINT));
    defaultsMap.put(LINE, getDefaultStyle(LINE));
    defaultsMap.put(POLYGON, getDefaultStyle(POLYGON));
    previewer = new StylePreviewer(previewLabel1);
  }

  private void initMyComponents() {
    upBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource(StyleStep.UP_ICON)));
    upBtn.setToolTipText("Move the selected agent towards the foreground");
    downBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource(StyleStep.DOWN_ICON)));
    downBtn.setToolTipText("Move the selected agent towards the background");

    addLayerBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource(StyleStep.ADD_ICON)));
    addLayerBtn.setToolTipText("Add a background layer to the display");
    removeLayerBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource(StyleStep.REMOVE_ICON)));
    removeLayerBtn.setToolTipText("Remove the selected background layer from the display");

    editBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource(StyleStep.EDIT_ICON)));
    editBtn.setToolTipText("Edit the style of the selected agent type");

    loadBtn.setToolTipText("Load Style from Styled Layer Descriptor (SLD) file");
    geomBox.setToolTipText("Specifies the geometry of the selected agent class");
    previewLabel1.setToolTipText("This is a sample of how the agent will appear");
  }

  private void addListeners() {
    agentList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          AgentTypeElement element = (AgentTypeElement) agentList.getSelectedValue();

          editBtn.setEnabled(element != null);
          loadBtn.setEnabled(editBtn.isEnabled());
          removeLayerBtn.setEnabled(element != null && element.source != null);
          if (element == null) agentNameFld.setText("");
          else {

            if (element.source == null) {
              agentNameFld.setText(element.agentClassName);
              geomBox.setEnabled(true);

              if (element.styleXML != null) {
                SLDParser parser = new SLDParser(new StyleFactoryImpl(), new StringReader(element.styleXML));
                Style style = parser.readXML()[0];
                setGeometryBox(style);
                element.styleMap.put((GeometryUtil.GeometryType) geomBox.getSelectedItem(), style);
                previewer.update(style);
              } else {
                geomBox.setSelectedItem(POINT);
                previewer.update(getDefaultStyle(POINT));
              }
            } else {
              agentNameFld.setText(element.agentName);
              geomBox.setEnabled(true);
              setGeometryBox(element.defaultGeometry);
              geomBox.setEnabled(false);
              if (element.styleXML != null) {
                SLDParser parser = new SLDParser(new StyleFactoryImpl(), new StringReader(element.styleXML));
                Style style = parser.readXML()[0];
                setGeometryBox(style);
                element.styleMap.put((GeometryUtil.GeometryType) geomBox.getSelectedItem(), style);
                previewer.update(style);
              }

            }
          }
        }
      }
    });

    editBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        showStyleDialog();
      }
    });

    geomBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        AgentTypeElement element = (AgentTypeElement) agentList.getSelectedValue();
        GeometryUtil.GeometryType type = (GeometryUtil.GeometryType) geomBox.getSelectedItem();
        if (element != null) {
          Style style = element.styleMap.get(type);
          if (style == null) {
            style = getDefaultStyle(type);
          }
          previewer.update(style);
        }
      }
    });

    addLayerBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(lastDirectory);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new FileFilter() {
          public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(".shp");
          }

          public String getDescription() {
            return "Shapefile (.shp)";
          }
        });

        if (chooser.showOpenDialog(GISStylePanel.this) == JFileChooser.APPROVE_OPTION) {
          addLayer(chooser.getSelectedFiles());
          lastDirectory = chooser.getCurrentDirectory();
        }
      }
    });

    removeLayerBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        DefaultListModel model = (DefaultListModel) agentList.getModel();
        Object obj = agentList.getSelectedValue();
        int index = model.indexOf(obj);
        model.removeElement(obj);
        agentList.setSelectedIndex(index == model.size() ? index - 1 : index);
      }
    });

    upBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        DefaultListModel model = (DefaultListModel) agentList.getModel();
        Object obj = agentList.getSelectedValue();
        int newIndex = model.indexOf(obj) - 1;
        if (newIndex > -1) {
          model.removeElement(obj);
          model.insertElementAt(obj, newIndex);
          agentList.setSelectedValue(obj, true);
        }
      }
    });

    downBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        DefaultListModel model = (DefaultListModel) agentList.getModel();
        Object obj = agentList.getSelectedValue();
        int newIndex = model.indexOf(obj) + 1;
        if (newIndex < model.size()) {
          model.removeElement(obj);
          model.insertElementAt(obj, newIndex);
          agentList.setSelectedValue(obj, true);
        }
      }
    });

    loadBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(lastDirectory);
        chooser.setFileFilter(new FileFilter() {
          public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(".sld") ||
                    f.getName().endsWith(".xml");
          }

          public String getDescription() {
            return "SLD format style file (.sld, .xml)";
          }
        });

        if (chooser.showOpenDialog(GISStylePanel.this) == JFileChooser.APPROVE_OPTION) {
          addStyle(chooser.getSelectedFile());
          lastDirectory = chooser.getCurrentDirectory();
        }
      }
    });
  }

  private void addStyle(File file) {
    try {
      AgentTypeElement element = (AgentTypeElement) agentList.getSelectedValue();
      SLDParser parser = new SLDParser(new StyleFactoryImpl(), new FileReader(file));
      Style style = parser.readXML()[0];
      element.styleXML = new SLDTransformer().transform(style);
      element.styleMap.put((GeometryUtil.GeometryType) geomBox.getSelectedItem(), style);
      previewer.update(style);
    } catch (FileNotFoundException e) {
      msg.error("Error while reading style from file", e);
    } catch (TransformerException e) {
      msg.error("Error while reading style from file", e);
    }
  }

  private void addLayer(File[] files) {
    for (File file : files) {
      try {
        ShapefileDataStore dataStore = new ShapefileDataStore(file.toURL());
        FeatureSource source = dataStore.getFeatureSource(dataStore.getTypeNames()[0]);
        String name = source.getSchema().getTypeName();
        AgentTypeElement elem = new AgentTypeElement(name + " (SHP)", "", null);
        elem.source = file;
        elem.defaultGeometry = source.getSchema().getDefaultGeometry().getType();
        DefaultListModel model = (DefaultListModel) agentList.getModel();
        model.addElement(elem);
        agentList.setSelectedIndex(model.size() - 1);
      } catch (IOException ex) {
        msg.error("Error while adding external background layer", ex);
      }
    }

  }

  private void setGeometryBox(Style style) {
    Symbolizer symbolizer = style.getFeatureTypeStyles()[0].getRules()[0].getSymbolizers()[0];
    if (PointSymbolizer.class.isAssignableFrom(symbolizer.getClass())) geomBox.setSelectedItem(POINT);
    if (PolygonSymbolizer.class.isAssignableFrom(symbolizer.getClass())) geomBox.setSelectedItem(POLYGON);
    if (LineSymbolizer.class.isAssignableFrom(symbolizer.getClass())) geomBox.setSelectedItem(LINE);
  }

  private void setGeometryBox(Class<? extends Geometry> clazz) {
    if (com.vividsolutions.jts.geom.Point.class.isAssignableFrom(clazz) || MultiPoint.class.isAssignableFrom(clazz))
      geomBox.setSelectedItem(POINT);
    else if (LineString.class.isAssignableFrom(clazz) || MultiLineString.class.isAssignableFrom(clazz))
      geomBox.setSelectedItem(LINE);
    else if (Polygon.class.isAssignableFrom(clazz) || MultiPolygon.class.isAssignableFrom(clazz))
      geomBox.setSelectedItem(POLYGON);
  }

  private Geometry getGeometry() {
    Object geom = geomBox.getSelectedItem();
    if (geom == POINT) return point;
    if (geom == LINE) return line;
    return polygon;
  }

  private Style getDefaultStyle(GeometryUtil.GeometryType geomType) {
    StyleBuilder builder = new StyleBuilder();

    if (geomType == POINT) {
      Mark mark = builder.createMark("square", Color.RED);
      mark.setStroke(builder.createStroke());
      return builder.createStyle(builder.createPointSymbolizer(builder.createGraphic(null,
              mark, null)));
    }

    if (geomType == LINE) {
      return builder.createStyle(builder.createLineSymbolizer(Color.RED, 1));
    }

    return builder.createStyle(builder.createPolygonSymbolizer(Color.RED, Color.BLACK, 1));
  }

  private void showStyleDialog() {
    StyleDialog dialog = new StyleDialog((JDialog) SwingUtilities.getWindowAncestor(this));
    AgentTypeElement element = (AgentTypeElement) agentList.getSelectedValue();
    try {
      Style style = previewer.getStyle();
      MapLayer layer = null;
      if (element.source == null) {
        Class agentClass = Class.forName(element.agentClassName, true, this.getClass().getClassLoader());

        DefaultFeatureAgentFactory fac = FeatureAgentFactoryFinder.getInstance().getFeatureAgentFactory(agentClass, getGeometry().getClass(),
                DefaultGeographicCRS.WGS84);
        FeatureCollection collection = fac.getFeatures();
        collection.add(new HollowFeature(fac.getFeatureType(), agentClass, getGeometry()));
        layer = new DefaultMapLayer(DataUtilities.createFeatureSource(collection), style);
      } else {
        ShapefileDataStore dataStore = new ShapefileDataStore(element.source.toURL());
        FeatureSource source = dataStore.getFeatureSource(dataStore.getTypeNames()[0]);
        layer = new DefaultMapLayer(source, style);
      }
      dialog.setMapLayer(layer);
      if (dialog.display()) {
        style = dialog.getStyle();
        element.styleXML = getSLDStyle(style);
        element.styleMap.put((GeometryUtil.GeometryType) geomBox.getSelectedItem(), style);
        previewer.update(style);
      }
    } catch (ClassNotFoundException e) {
      msg.error("Error creating agent class while invoking style editor", e);
    } catch (Exception ex) {
      msg.error("Error creating style", ex);
    }
  }

  private String getSLDStyle(Style style) throws TransformerException {
    SLDTransformer sldTransformer = new SLDTransformer();
    String xml = sldTransformer.transform(style);
    // fix the style xml
    int index = xml.indexOf(SLD_NS);
    if (index != -1 && xml.indexOf(SLD_NS_2) == -1) {
      StringBuilder builder = new StringBuilder(xml);
      builder.insert(index + SLD_NS.length(), SLD_NS_2);
      xml = builder.toString();
    }
    return xml;

  }


  public void init(ContextData context, DisplayDescriptor descriptor) {
    this.descriptor = descriptor;
    DefaultListModel listModel = new DefaultListModel();

    for (AgentData agent : context.getAgentData(true)) {
        String styleXML = descriptor.getStyleClassName(agent.getClassName());
        listModel.addElement(new AgentTypeElement(agent.getShortName(), agent.getClassName(), styleXML));
    }

    Map<String, String> shpStyles = (Map<String, String>) descriptor.getProperty(DisplayGIS.SHP_FILE_STYLE_PROP);
    if (shpStyles != null) {
      for (Map.Entry<String, String> entry : shpStyles.entrySet()) {
        try {
          File file = new File(entry.getKey());
          ShapefileDataStore dataStore = new ShapefileDataStore(file.toURI().toURL());
          FeatureSource source = dataStore.getFeatureSource(dataStore.getTypeNames()[0]);
          String name = source.getSchema().getTypeName();
          AgentTypeElement elem = new AgentTypeElement(name + " (SHP)", "", null);
          elem.source = file;
          elem.defaultGeometry = source.getSchema().getDefaultGeometry().getType();
          elem.styleXML = entry.getValue();
          listModel.addElement(elem);
        } catch (IOException ex) {
          msg.error("Error while loading external shapefile", ex);
        }
      }

    }
    agentList.setModel(listModel);
    agentList.setSelectedIndex(0);
  }

  public void done() {
    DefaultListModel model = (DefaultListModel) agentList.getModel();
    Map<String, String> shpStyles = new HashMap<String, String>();
    descriptor.setProperty(DisplayGIS.SHP_FILE_STYLE_PROP, shpStyles);
    for (int i = 0; i < model.size(); i++) {
      AgentTypeElement element = (AgentTypeElement) model.elementAt(i);
      if (element.styleXML != null && element.source == null) {
        descriptor.addStyle(element.agentClassName, element.styleXML);
        descriptor.addLayerOrder(element.agentClassName, i);
      } else if (element.styleXML == null && element.source == null) {
        // if style xml == null that means never edited and never
        // click another element in list so just use current default.
        Style style = getDefaultStyle((GeometryUtil.GeometryType) geomBox.getSelectedItem());
        descriptor.addLayerOrder(element.agentClassName, i);
        try {
          descriptor.addStyle(element.agentClassName, getSLDStyle(style));
        } catch (TransformerException e) {
          msg.warn("Error while transforming Style to xml", e);
        }
      } else if (element.source != null) {
        String filePath = element.source.getAbsolutePath();
        shpStyles.put(filePath, element.styleXML == null ? "" : element.styleXML);
        descriptor.addLayerOrder(filePath, i);
      }
    }
  }


  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
    upBtn = new JButton();
    downBtn = new JButton();
    label3 = new JLabel();
    separator1 = compFactory.createSeparator("Layer Order");
    agentNameFld = new JTextField();
    label1 = new JLabel();
    label4 = new JLabel();
    scrollPane1 = new JScrollPane();
    agentList = new JList();
    geomBox = new JComboBox();
    panel1 = new JPanel();
    panel2 = new JPanel();
    previewLabel1 = new PreviewLabel();
    loadBtn = new JButton();
    editBtn = new JButton();
    addLayerBtn = new JButton();
    removeLayerBtn = new JButton();
    label2 = new JLabel();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setLayout(new FormLayout(
            new ColumnSpec[]{
                    new ColumnSpec(Sizes.dluX(35)),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(Sizes.dluX(35)),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(Sizes.DLUX3),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(51), 0.1),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(Sizes.dluX(10)),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(Sizes.dluX(97)),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            },
            new RowSpec[]{
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    new RowSpec(Sizes.dluY(10)),
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    new RowSpec(Sizes.dluY(44))
            }));
    add(upBtn, cc.xy(1, 1));
    add(downBtn, cc.xy(3, 1));

    //---- label3 ----
    label3.setText("Agent Class:");
    add(label3, cc.xywh(9, 1, 4, 1));
    add(separator1, cc.xywh(1, 3, 5, 1));
    add(agentNameFld, cc.xywh(9, 3, 7, 1));

    //---- label1 ----
    label1.setText("Foreground");
    add(label1, cc.xywh(1, 5, 7, 1));

    //---- label4 ----
    label4.setText("Geometry:");
    add(label4, cc.xywh(9, 5, 3, 1));

    //======== scrollPane1 ========
    {
      scrollPane1.setViewportView(agentList);
    }
    add(scrollPane1, cc.xywh(1, 7, 6, 9));
    add(geomBox, cc.xy(9, 7));

    //======== panel1 ========
    {
      panel1.setBorder(new TitledBorder("Preview"));
      panel1.setLayout(null);

      //======== panel2 ========
      {
        panel2.setBackground(Color.white);
        panel2.setLayout(null);

        //---- previewLabel1 ----
        previewLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        previewLabel1.setBackground(Color.white);
        panel2.add(previewLabel1);
        previewLabel1.setBounds(10, 5, 105, 100);

        { // compute preferred size
          Dimension preferredSize = new Dimension();
          for (int i = 0; i < panel2.getComponentCount(); i++) {
            Rectangle bounds = panel2.getComponent(i).getBounds();
            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
          }
          Insets insets = panel2.getInsets();
          preferredSize.width += insets.right;
          preferredSize.height += insets.bottom;
          panel2.setMinimumSize(preferredSize);
          panel2.setPreferredSize(preferredSize);
        }
      }
      panel1.add(panel2);
      panel2.setBounds(8, 22, 127, 108);

      { // compute preferred size
        Dimension preferredSize = new Dimension();
        for (int i = 0; i < panel1.getComponentCount(); i++) {
          Rectangle bounds = panel1.getComponent(i).getBounds();
          preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
          preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
        }
        Insets insets = panel1.getInsets();
        preferredSize.width += insets.right;
        preferredSize.height += insets.bottom;
        panel1.setMinimumSize(preferredSize);
        panel1.setPreferredSize(preferredSize);
      }
    }
    add(panel1, cc.xywh(13, 7, 1, 14));

    //---- loadBtn ----
    loadBtn.setText("Load SLD");
    add(loadBtn, cc.xy(9, 9));

    //---- editBtn ----
    editBtn.setToolTipText("Edit a custom style");
    add(editBtn, cc.xy(9, 11));
    add(addLayerBtn, cc.xy(9, 13));
    add(removeLayerBtn, cc.xy(9, 15));

    //---- label2 ----
    label2.setText("Background");
    add(label2, cc.xywh(1, 17, 7, 1));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JButton upBtn;
  private JButton downBtn;
  private JLabel label3;
  private JComponent separator1;
  private JTextField agentNameFld;
  private JLabel label1;
  private JLabel label4;
  private JScrollPane scrollPane1;
  private JList agentList;
  private JComboBox geomBox;
  private JPanel panel1;
  private JPanel panel2;
  private PreviewLabel previewLabel1;
  private JButton loadBtn;
  private JButton editBtn;
  private JButton addLayerBtn;
  private JButton removeLayerBtn;
  private JLabel label2;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
