/*
 * Created by JFormDesigner on Thu Sep 06 10:13:44 EDT 2007
 */

package repast.simphony.visualization.editor;

import groovy.lang.MetaClass;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.jscience.physics.amount.Amount;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.grid.Grid;
import repast.simphony.ui.probe.GridLocationProbe;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.ui.probe.SpaceLocationProbe;
import repast.simphony.util.ClassUtilities;
import repast.simphony.util.ContextUtils;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;

/**
 * @author User #2
 * @deprecated 2D piccolo based code is being removed
 */
public class AgentEditor extends JPanel {

  private static final String ADD_ICON = "add_user.png";
  private static final String REMOVE_ICON = "delete_user.png";
  private static final String CLONE_ICON = "add_group.png";

  private static final int EDGE_INDEX = 1;
  private static final int AGENT_INDEX = 0;

  private class AddMenuAction extends AbstractAction {

    private Class clazz;

    public AddMenuAction(Class clazz) {
      super(clazz.getSimpleName());
      this.clazz = clazz;
    }

    public void actionPerformed(ActionEvent e) {
      editor.addAgent(clazz);
    }
  }

  private class SourceTargetRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
      String title = titleMap.get(value);
      return super.getTableCellRendererComponent(table, title == null ? value : title, isSelected, hasFocus, row, column);
    }
  }

  private DisplayEditor editor;
  private int agentTitleIndex = 1;
  private boolean doFireSelection = true;
  private Map<Object, String> titleMap = new HashMap<Object, String>();
  private Map<Object, String> edgeTitleMap = new HashMap<Object, String>();
  private Object selectedObject;
  private List<Class> agentClasses;


  public AgentEditor() {
    initComponents();
    agentList.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) label.setText(titleMap.get(value));
        return label;
      }
    });

    edgeList.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) label.setText(edgeTitleMap.get(value));
        return label;
      }
    });

    addBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource(ADD_ICON)));
    addBtn.setToolTipText("Add a new agent");
    removeBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource(REMOVE_ICON)));
    removeBtn.setToolTipText("Remove selected agent(s)");
    cloneBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CLONE_ICON)));
    cloneBtn.setToolTipText("Clone selected agent");
  }

  public void init(DisplayEditor editor, List<Class> agentClasses, List<RepastEdge> edges, int dividerLoc) {
    this.editor = editor;
    tabbedPane1.setEnabledAt(EDGE_INDEX, edges != null);
    this.agentClasses = new ArrayList<Class>(agentClasses);
    Collections.sort(agentClasses, new Comparator<Class>() {
      public int compare(Class o1, Class o2) {
        return o1.getSimpleName().compareTo(o2.getSimpleName());
      }
    });

    fillLists(edges);
    splitPane1.setDividerLocation(dividerLoc);

    addListeners();
    addBtn.setEnabled(true);
    removeBtn.setEnabled(false);
    cloneBtn.setEnabled(false);
  }

  private void addListeners() {
    agentList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          performAgentSelection();

          removeBtn.setEnabled(agentList.getSelectedIndices().length > 0 &&
                  tabbedPane1.getSelectedIndex() == AGENT_INDEX);
          cloneBtn.setEnabled(agentList.getSelectedIndices().length == 1);
        }
      }
    });

    edgeList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          performEdgeSelection();
          removeBtn.setEnabled(edgeList.getSelectedIndices().length > 0 &&
                  tabbedPane1.getSelectedIndex() == EDGE_INDEX);
        }
      }
    });

    tabbedPane1.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        if (tabbedPane1.getSelectedIndex() == 0) {
          performAgentSelection();
          addBtn.setEnabled(true);
          removeBtn.setEnabled(agentList.getSelectedIndices().length > 0);
          cloneBtn.setEnabled(agentList.getSelectedIndices().length == 1);
        } else {
          performEdgeSelection();
          addBtn.setEnabled(false);
          cloneBtn.setEnabled(false);
          removeBtn.setEnabled(edgeList.getSelectedIndices().length > 0);
        }
      }
    });


    agentProps.addPropertySheetChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        Property prop = (Property) evt.getSource();
        prop.writeToObject(selectedObject);
        String title = probeForTitle(selectedObject);
        if (title != null && !title.equals(titleMap.get(selectedObject))) {
          titleMap.put(selectedObject, title);
          agentList.revalidate();
          agentList.repaint();

          // update any relevant edge titles as well
          DefaultListModel model = (DefaultListModel) edgeList.getModel();
          for (int i = 0; i < model.size(); i++) {
            RepastEdge edge = (RepastEdge) model.get(i);
            if (edge.getSource().equals(selectedObject) || edge.getTarget().equals(selectedObject)) {
              String edgeTitle = createEdgeTitle(edge);
              edgeTitleMap.put(edge, edgeTitle);
            }
          }

          edgeList.revalidate();
          edgeList.repaint();
        }
      }
    });

    addBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        if (agentClasses.size() > 1) {
          JPopupMenu menu = new JPopupMenu();
          for (Class clazz : agentClasses) {
            menu.add(new JMenuItem(new AddMenuAction(clazz)));
          }
          menu.show(addBtn, 0, addBtn.getSize().height);
        } else {
          new AddMenuAction(agentClasses.get(0)).actionPerformed(evt);
        }
      }
    });

    removeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        int tabIndex = tabbedPane1.getSelectedIndex();
        if (tabIndex == AGENT_INDEX) {
          Object[] agents = agentList.getSelectedValues();
          editor.removeAgents(agents);
        } else if (tabIndex == EDGE_INDEX) {
          Object[] objs = edgeList.getSelectedValues();
          RepastEdge[] edges = new RepastEdge[objs.length];
          int i = 0;
          for (Object obj : objs) {
            edges[i++] = (RepastEdge) obj;
          }
          editor.removeEdges(edges);
        }
      }
    });

    cloneBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        // this should never throw NPE because
        // we disable the button if nothing selected.
        Object obj = agentList.getSelectedValues()[0];
        editor.cloneAgent(obj);
      }
    });
  }

  /**
   * Called when a new agent has been added.
   *
   * @param agent  the added agent
   * @param select true if the agent should be selected
   */
  public void agentAdded(Object agent, boolean select) {
    DefaultListModel model = (DefaultListModel) agentList.getModel();
    if (!model.contains(agent)) {
      titleMap.put(agent, createTitle(agent));
      model.addElement(agent);
      if (select) {
        int index = model.size() - 1;
        agentList.setSelectedIndex(index);
        agentList.scrollRectToVisible(agentList.getCellBounds(index, index));
        tabbedPane1.setSelectedIndex(AGENT_INDEX);
      }
    } else if (select) {
      int index = model.indexOf(agent);
      agentList.scrollRectToVisible(agentList.getCellBounds(index, index));
      tabbedPane1.setSelectedIndex(AGENT_INDEX);
      agentList.setSelectedIndex(index);
    }
  }

  /**
   * Called when an agent has been removed.
   *
   * @param agent the removed agent
   */
  public void agentRemoved(Object agent) {
    DefaultListModel model = (DefaultListModel) agentList.getModel();
    model.removeElement(agent);
  }

  public void agentMoved(Object agent) {
    if (selectedObject != null && agent.equals(selectedObject)) updateProperties();
  }

  /**
   * Called when a new edge has been eadded.
   *
   * @param edge   the added edge
   * @param select true if the agent should be selected
   */
  public void edgeAdded(RepastEdge edge, boolean select) {
    tabbedPane1.setEnabledAt(EDGE_INDEX, true);
    doFireSelection = false;
    DefaultListModel model = (DefaultListModel) edgeList.getModel();
    if (!model.contains(edge)) {
      edgeTitleMap.put(edge, createEdgeTitle(edge));
      model.addElement(edge);
      if (select) {
        int index = model.size() - 1;
        edgeList.setSelectedIndex(index);
        edgeList.scrollRectToVisible(edgeList.getCellBounds(index, index));
        tabbedPane1.setSelectedIndex(EDGE_INDEX);
      }
    } else if (select) {
      int index = model.indexOf(edge);
      edgeList.setSelectedIndex(index);
      edgeList.scrollRectToVisible(edgeList.getCellBounds(index, index));
      tabbedPane1.setSelectedIndex(EDGE_INDEX);
    }
    doFireSelection = true;
  }

  public void edgeRemoved(RepastEdge edge) {
    DefaultListModel model = (DefaultListModel) edgeList.getModel();
    model.removeElement(edge);
  }

  /**
   * Informs this AgentEditor that edges have been selected
   * by code external to this AgentEditor.
   *
   * @param edges the selected edges.
   */
  public void edgesSelected(List<Object> edges) {
    doFireSelection = false;
    int[] indices = new int[edges.size()];
    int i = 0;
    for (Object obj : edges) {
      indices[i++] = ((DefaultListModel) edgeList.getModel()).indexOf(obj);
    }
    edgeList.setSelectedIndices(indices);
    if (indices.length > 0) {
      edgeList.scrollRectToVisible(edgeList.getCellBounds(indices[0], indices[0]));
      tabbedPane1.setSelectedIndex(EDGE_INDEX);
    }
    doFireSelection = true;
  }

  /**
   * Informs this AgentEditor that agents have been selected
   * by code external to this AgentEditor.
   *
   * @param agents the selected agents.
   */
  public void agentsSelected(List<Object> agents) {
    doFireSelection = false;
    int[] indices = new int[agents.size()];
    int i = 0;
    for (Object obj : agents) {
      indices[i++] = ((DefaultListModel) agentList.getModel()).indexOf(obj);
    }
    agentList.setSelectedIndices(indices);
    if (indices.length > 0) {
      agentList.scrollRectToVisible(agentList.getCellBounds(indices[0], indices[0]));
      tabbedPane1.setSelectedIndex(AGENT_INDEX);
    }
    doFireSelection = true;
  }

  private void performEdgeSelection() {
    Object[] edges = edgeList.getSelectedValues();
    if (doFireSelection) {
      editor.edgesSelected(edges);
    }

    if (edges.length == 1) selectedObject = edges[0];
    else selectedObject = null;
    updateProperties();
  }

  private void performAgentSelection() {
    Object[] agents = agentList.getSelectedValues();
    if (doFireSelection) {
      editor.agentsSelected(agents);
    }

    if (agents.length == 1) selectedObject = agents[0];
    else selectedObject = null;
    updateProperties();
  }

  private void updateProperties() {
    if (selectedObject == null) agentProps.setProperties(new Property[0]);
    else {
      BeanInfo beanInfo = null;
      try {
        beanInfo = Introspector.getBeanInfo(selectedObject.getClass(), Object.class);
      } catch (IntrospectionException e) {
        e.printStackTrace();
      }

      agentProps.setMode(PropertySheet.VIEW_AS_FLAT_LIST);
      agentProps.setDescriptionVisible(false);

      List<PropertyDescriptor> pds = new ArrayList<PropertyDescriptor>();
      for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
        if (descriptor.getReadMethod() != null && !descriptor.getReadMethod().getReturnType().equals(MetaClass.class))
          pds.add(descriptor);
      }

      agentProps.setProperties(pds.toArray(new PropertyDescriptor[pds.size()]));
      agentProps.getEditorRegistry().registerEditor(Amount.class, new AmountPropertyEditor());

      Property[] properties = agentProps.getProperties();
      for (int i = 0, c = properties.length; i < c; i++) {
        try {
          Property property = properties[i];
          if (selectedObject instanceof RepastEdge && (property.getName().equalsIgnoreCase("source") ||
                  property.getName().equalsIgnoreCase("target"))) {
            agentProps.getRendererRegistry().registerRenderer(property, new SourceTargetRenderer());
          }
          property.readFromObject(selectedObject);

        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      createLocationProperties();
    }
  }

  private void createLocationProperties() {
    // see if we can find the context for it and the projections
    Context context = ContextUtils.getContext(selectedObject);
    try {
      if (context != null) {

        for (Grid grid : (Iterable<Grid>) context.getProjections(Grid.class)) {
          // we need to recreate the bean info each time
          // if we do not the property sheet can distinguish the property
          BeanInfo info = Introspector.getBeanInfo(GridLocationProbe.class, Object.class);
          PropertyDescriptor pd = info.getPropertyDescriptors()[0];
          pd.setDisplayName(grid.getName() + " Location");
          Property prop = new PropertyDescriptorAdapter(pd);
          GridLocationProbe probe = new GridLocationProbe(selectedObject, grid);
          prop.readFromObject(probe);
          agentProps.addProperty(prop);
        }


        for (ContinuousSpace space : (Iterable<ContinuousSpace>) context.getProjections(ContinuousSpace.class)) {
          BeanInfo info = Introspector.getBeanInfo(SpaceLocationProbe.class, Object.class);
          PropertyDescriptor pd = info.getPropertyDescriptors()[0];
          pd.setDisplayName(space.getName() + " Location");
          SpaceLocationProbe probe = new SpaceLocationProbe(selectedObject, space);
          Property prop = new PropertyDescriptorAdapter(pd);
          prop.readFromObject(probe);
          agentProps.addProperty(prop);
        }

      }
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }
  }

  private void fillLists(List<RepastEdge> edges) {
    DefaultListModel model = new DefaultListModel();
    for (Object obj : editor.getContainer()) {
      titleMap.put(obj, createTitle(obj));
      model.addElement(obj);
    }
    agentList.setModel(model);

    model = new DefaultListModel();
    if (edges != null) {
      for (RepastEdge edge : edges) {
        model.addElement(edge);
        edgeTitleMap.put(edge, createEdgeTitle(edge));
      }
    }
    edgeList.setModel(model);
  }

  private String createEdgeTitle(RepastEdge edge) {
    Object source = edge.getSource();
    Object target = edge.getTarget();
    StringBuffer buf = new StringBuffer(titleMap.get(source));
    buf.append(edge.isDirected() ? " -> " : " <-> ");
    buf.append(titleMap.get(target));
    return buf.toString();
  }

  private String probeForTitle(Object obj) {
    Method[] methods = ClassUtilities.findMethods(obj.getClass(), ProbeID.class);
    if (methods.length > 0) {
      Method method = methods[0];
      if (!method.getReturnType().equals(void.class) && method.getParameterTypes().length == 0) {
        try {
          return method.invoke(obj).toString();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }

    return null;

  }

  private String createTitle(Object obj) {
    String title = probeForTitle(obj);
    return title == null ? obj.getClass().getSimpleName() + " - " + agentTitleIndex++ : title;
  }


  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    panel1 = new JPanel();
    addBtn = new JButton();
    removeBtn = new JButton();
    cloneBtn = new JButton();
    splitPane1 = new JSplitPane();
    tabbedPane1 = new JTabbedPane();
    scrollPane1 = new JScrollPane();
    agentList = new JList();
    scrollPane2 = new JScrollPane();
    edgeList = new JList();
    agentProps = new PropertySheetPanel();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setLayout(new FormLayout(
            ColumnSpec.decodeSpecs("default:grow"),
            new RowSpec[]{
                    FormSpecs.DEFAULT_ROWSPEC,
                    FormSpecs.LINE_GAP_ROWSPEC,
                    new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            }));

    //======== panel1 ========
    {
      panel1.setLayout(new FormLayout(
              new ColumnSpec[]{
                      FormSpecs.BUTTON_COLSPEC,
                      FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
                      FormSpecs.BUTTON_COLSPEC,
                      FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
                      FormSpecs.BUTTON_COLSPEC
              },
              RowSpec.decodeSpecs("default")));
      panel1.add(addBtn, cc.xy(1, 1));
      panel1.add(removeBtn, cc.xy(3, 1));
      panel1.add(cloneBtn, cc.xy(5, 1));
    }
    add(panel1, cc.xy(1, 1));

    //======== splitPane1 ========
    {
      splitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);

      //======== tabbedPane1 ========
      {

        //======== scrollPane1 ========
        {
          scrollPane1.setViewportView(agentList);
        }
        tabbedPane1.addTab("Agents", scrollPane1);

        //======== scrollPane2 ========
        {
          scrollPane2.setViewportView(edgeList);
        }
        tabbedPane1.addTab("Links", scrollPane2);
        tabbedPane1.setEnabledAt(1, false);
      }
      splitPane1.setTopComponent(tabbedPane1);
      splitPane1.setBottomComponent(agentProps);
    }
    add(splitPane1, cc.xy(1, 3));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JPanel panel1;
  private JButton addBtn;
  private JButton removeBtn;
  private JButton cloneBtn;
  private JSplitPane splitPane1;
  private JTabbedPane tabbedPane1;
  private JScrollPane scrollPane1;
  private JList agentList;
  private JScrollPane scrollPane2;
  private JList edgeList;
  private PropertySheetPanel agentProps;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}