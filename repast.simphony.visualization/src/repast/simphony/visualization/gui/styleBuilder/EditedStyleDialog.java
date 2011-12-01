package repast.simphony.visualization.gui.styleBuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;
import org.jscience.physics.amount.Amount;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.scenario.ScenarioUtils;
import repast.simphony.ui.widget.SquareIcon;
import repast.simphony.visualization.editedStyle.DefaultEditedStyleData2D;
import repast.simphony.visualization.editedStyle.DefaultEditedStyleData3D;
import repast.simphony.visualization.editedStyle.EditedStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;
import repast.simphony.visualization.engine.DisplayDescriptor;
import saf.core.ui.util.FileChooserUtilities;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XStream11XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * @author Eric Tatara
 */
public class EditedStyleDialog extends JDialog {
  private boolean save = false;
  private EditedStyleData userStyleData;
  private static final Set<Class> pTypes = new HashSet<Class>();
  private List<String> methodList;
  private List<String> labelMethodList;

  private DefaultComboBoxModel sizeModel;
  private DefaultComboBoxModel sizeMinModel;
  private DefaultComboBoxModel sizeMaxModel;
  private DefaultComboBoxModel sizeScaleModel;
  private DefaultComboBoxModel labelModel;
  private DefaultComboBoxModel labelFontFamilyModel;

  private DefaultComboBoxModel shapeModel;

  private DefaultComboBoxModel variableIconRedColorValueModel;
  private DefaultComboBoxModel variableIconGreenColorValueModel;
  private DefaultComboBoxModel variableIconBlueColorValueModel;
  private DefaultComboBoxModel variableIconRedColorMinModel;
  private DefaultComboBoxModel variableIconGreenColorMinModel;
  private DefaultComboBoxModel variableIconBlueColorMinModel;
  private DefaultComboBoxModel variableIconRedColorMaxModel;
  private DefaultComboBoxModel variableIconGreenColorMaxModel;
  private DefaultComboBoxModel variableIconBlueColorMaxModel;
  private DefaultComboBoxModel variableIconRedColorScaleModel;
  private DefaultComboBoxModel variableIconGreenColorScaleModel;
  private DefaultComboBoxModel variableIconBlueColorScaleModel;

  private String agentClassName;
  private String userStyleName;
  //	private DisplayDescriptor descriptor;
  private DisplayDescriptor.DisplayType displayType;

  private PreviewIcon preview;

  static {
    pTypes.add(int.class);
    pTypes.add(double.class);
    pTypes.add(float.class);
    pTypes.add(long.class);
    pTypes.add(byte.class);
    pTypes.add(short.class);
    pTypes.add(Amount.class);
    pTypes.add(java.lang.Object.class);
  }

  public EditedStyleDialog(Frame owner) {
    super(owner);
    methodList = new ArrayList<String>();
    labelMethodList = new ArrayList<String>();
  }

  public EditedStyleDialog(Dialog owner) {
    super(owner);
    methodList = new ArrayList<String>();
    labelMethodList = new ArrayList<String>();
  }

  public void init(Class agentClass, String userStyleName,
                   DisplayDescriptor descriptor) {
    this.agentClassName = agentClass.getCanonicalName();
    this.userStyleName = userStyleName;
    this.displayType = descriptor.getDisplayType();

    userStyleData = EditedStyleUtils.getStyle(descriptor.getEditedStyleName(agentClass.getName()));

    Method[] methods = agentClass.getMethods();
    for (Method method : methods) {
    	if (!method.isSynthetic()){
      if (method.getParameterTypes().length == 0 && (pTypes.contains(method.getReturnType()) ||
              Number.class.isAssignableFrom(method.getReturnType()))) {
        methodList.add(method.getName());
      }

      if (method.getParameterTypes().length == 0 && (pTypes.contains(method.getReturnType()) ||
              Number.class.isAssignableFrom(method.getReturnType()) ||
              method.getReturnType().equals(String.class))) {
        labelMethodList.add(method.getName());
      }
    	}
    }

    methodList.remove("hashCode");
    labelMethodList.remove("hashCode");
    labelMethodList.remove("toString");
    labelMethodList.add("Name");

    // Set objects based on display type 2D/3D
    if (displayType.equals(DisplayDescriptor.DisplayType.TWO_D)) {
      if (userStyleData == null)
        userStyleData = new DefaultEditedStyleData2D();

      shapeModel = new DefaultComboBoxModel(new String[]{
              "circle",
              "square",
              "triangle",
              "cross",
              "star"});

      shapeModel.setSelectedItem(userStyleData.getShapeWkt());
    } else {
      if (userStyleData == null)
        userStyleData = new DefaultEditedStyleData3D();

      shapeModel = new DefaultComboBoxModel(new String[]{
              "sphere",
              "cube",
              "cylinder",
              "cone"});

      shapeModel.setSelectedItem(userStyleData.getShapeWkt());

    }

    sizeModel = new DefaultComboBoxModel();
    sizeMinModel = new DefaultComboBoxModel();
    sizeMaxModel = new DefaultComboBoxModel();
    sizeScaleModel = new DefaultComboBoxModel();
    labelModel = new DefaultComboBoxModel();
    labelFontFamilyModel = new DefaultComboBoxModel();
    variableIconRedColorValueModel = new DefaultComboBoxModel();
    variableIconGreenColorValueModel = new DefaultComboBoxModel();
    variableIconBlueColorValueModel = new DefaultComboBoxModel();
    variableIconRedColorMinModel = new DefaultComboBoxModel();
    variableIconGreenColorMinModel = new DefaultComboBoxModel();
    variableIconBlueColorMinModel = new DefaultComboBoxModel();
    variableIconRedColorMaxModel = new DefaultComboBoxModel();
    variableIconGreenColorMaxModel = new DefaultComboBoxModel();
    variableIconBlueColorMaxModel = new DefaultComboBoxModel();
    variableIconRedColorScaleModel = new DefaultComboBoxModel();
    variableIconGreenColorScaleModel = new DefaultComboBoxModel();
    variableIconBlueColorScaleModel = new DefaultComboBoxModel();

    // Add available methods to appropriate combo box models
    for (String method : methodList) {
      sizeModel.addElement(method);
      sizeMinModel.addElement(method);
      sizeMaxModel.addElement(method);
      sizeScaleModel.addElement(method);

      variableIconRedColorValueModel.addElement(method);
      variableIconGreenColorValueModel.addElement(method);
      variableIconBlueColorValueModel.addElement(method);
//			variableIconRedColorMinModel.addElement(method);
//			variableIconGreenColorMinModel.addElement(method);
//			variableIconBlueColorMinModel.addElement(method);
//			variableIconRedColorMaxModel.addElement(method);
//			variableIconGreenColorMaxModel.addElement(method);
//			variableIconBlueColorMaxModel.addElement(method);
//			variableIconRedColorScaleModel.addElement(method);
//			variableIconGreenColorScaleModel.addElement(method);
//			variableIconBlueColorScaleModel.addElement(method);
    }

    for (String method : labelMethodList)
      labelModel.addElement(method);

    if (userStyleData.getSizeMethodName() != null)
      sizeModel.setSelectedItem(userStyleData.getSizeMethodName());
    else {
      sizeModel.addElement(userStyleData.getSize());
      sizeModel.setSelectedItem(userStyleData.getSize());
    }
    if (userStyleData.getSizeMinMethodName() != null)
      sizeMinModel.setSelectedItem(userStyleData.getSizeMinMethodName());
    else {
      sizeMinModel.addElement(userStyleData.getSizeMin());
      sizeMinModel.setSelectedItem(userStyleData.getSizeMin());
    }
    if (userStyleData.getSizeMaxMethodName() != null)
      sizeMaxModel.setSelectedItem(userStyleData.getSizeMaxMethodName());
    else {
      sizeMaxModel.addElement(userStyleData.getSizeMax());
      sizeMaxModel.setSelectedItem(userStyleData.getSizeMax());
    }
    if (userStyleData.getSizeScaleMethodName() != null)
      sizeScaleModel.setSelectedItem(userStyleData.getSizeScaleMethodName());
    else {
      sizeScaleModel.addElement(userStyleData.getSizeScale());
      sizeScaleModel.setSelectedItem(userStyleData.getSizeScale());
    }
    if (userStyleData.getLabelMethod() != null) {

      if ("toString".equals(userStyleData.getLabelMethod()))
        labelModel.setSelectedItem("Name");
      else
        labelModel.setSelectedItem(userStyleData.getLabelMethod());
    } else {
      labelModel.addElement(userStyleData.getLabel());
      labelModel.setSelectedItem(userStyleData.getLabel());
    }


    if (userStyleData.getRedMethod() != null)
      variableIconRedColorValueModel.setSelectedItem(userStyleData.getRedMethod());
    else {
      variableIconRedColorValueModel.addElement(userStyleData.getColor()[0]);
      variableIconRedColorValueModel.setSelectedItem(userStyleData.getColor()[0]);
    }
    if (userStyleData.getGreenMethod() != null)
      variableIconGreenColorValueModel.setSelectedItem(userStyleData.getGreenMethod());
    else {
      variableIconGreenColorValueModel.addElement(userStyleData.getColor()[1]);
      variableIconGreenColorValueModel.setSelectedItem(userStyleData.getColor()[1]);
    }
    if (userStyleData.getBlueMethod() != null)
      variableIconBlueColorValueModel.setSelectedItem(userStyleData.getBlueMethod());
    else {
      variableIconBlueColorValueModel.addElement(userStyleData.getColor()[2]);
      variableIconBlueColorValueModel.setSelectedItem(userStyleData.getColor()[2]);
    }


    variableIconRedColorMinModel.addElement(userStyleData.getColorMin()[0]);
    variableIconRedColorMinModel.setSelectedItem(userStyleData.getColorMin()[0]);
    variableIconGreenColorMinModel.addElement(userStyleData.getColorMin()[1]);
    variableIconGreenColorMinModel.setSelectedItem(userStyleData.getColorMin()[1]);
    variableIconBlueColorMinModel.addElement(userStyleData.getColorMin()[2]);
    variableIconBlueColorMinModel.setSelectedItem(userStyleData.getColorMin()[2]);

    variableIconRedColorMaxModel.addElement(userStyleData.getColorMax()[0]);
    variableIconRedColorMaxModel.setSelectedItem(userStyleData.getColorMax()[0]);
    variableIconGreenColorMaxModel.addElement(userStyleData.getColorMax()[1]);
    variableIconGreenColorMaxModel.setSelectedItem(userStyleData.getColorMax()[1]);
    variableIconBlueColorMaxModel.addElement(userStyleData.getColorMax()[2]);
    variableIconBlueColorMaxModel.setSelectedItem(userStyleData.getColorMax()[2]);

    variableIconRedColorScaleModel.addElement(userStyleData.getColorScale()[0]);
    variableIconRedColorScaleModel.setSelectedItem(userStyleData.getColorScale()[0]);
    variableIconGreenColorScaleModel.addElement(userStyleData.getColorScale()[1]);
    variableIconGreenColorScaleModel.setSelectedItem(userStyleData.getColorScale()[1]);
    variableIconBlueColorScaleModel.addElement(userStyleData.getColorScale()[2]);
    variableIconBlueColorScaleModel.setSelectedItem(userStyleData.getColorScale()[2]);

    // Label font
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    String fontList[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getAvailableFontFamilyNames();

    for (int i = 0; i < fontList.length; i++)
      labelFontFamilyModel.addElement(fontList[i]);

    if (labelFontFamilyModel.getIndexOf(userStyleData.getLabelFontFamily()) != -1)
      labelFontFamilyModel.setSelectedItem(userStyleData.getLabelFontFamily());

    initComponents();
    initMyComponents(displayType);
  }

  public void initMyComponents(DisplayDescriptor.DisplayType displayType) {
    CellConstraints cc = new CellConstraints();

    shapeComboBox.setModel(shapeModel);
    sizeComboBox.setModel(sizeModel);
    sizeMinComboBox.setModel(sizeMinModel);
    sizeMaxComboBox.setModel(sizeMaxModel);
    sizeScaleComboBox.setModel(sizeScaleModel);
    labelComboBox.setModel(labelModel);
    labelOffsetTextField.setText((Float.toString(userStyleData.getLabelOffset())));
    labelFontFamilyComboBox.setModel(labelFontFamilyModel);

    labelPositionComboBox.getModel().setSelectedItem(userStyleData.getLabelPosition());

//		DefaultComboBoxModel model = ((DefaultComboBoxModel)(labelFontSizeComboBox.getModel()));
//		if (model.getIndexOf(userStyleData.getLabelFontSize()) != -1)
//		model.setSelectedItem(userStyleData.getLabelFontSize());
//		else{
//		model.addElement(userStyleData.getLabelFontSize());
//		model.setSelectedItem(userStyleData.getLabelFontSize());
//		}

    redValueComboBox.setModel(variableIconRedColorValueModel);
    greenValueComboBox.setModel(variableIconGreenColorValueModel);
    blueValueComboBox.setModel(variableIconBlueColorValueModel);
    redMinComboBox.setModel(variableIconRedColorMinModel);
    greenMinComboBox.setModel(variableIconGreenColorMinModel);
    blueMinComboBox.setModel(variableIconBlueColorMinModel);
    redMaxComboBox.setModel(variableIconRedColorMaxModel);
    greenMaxComboBox.setModel(variableIconGreenColorMaxModel);
    blueMaxComboBox.setModel(variableIconBlueColorMaxModel);
    redScaleComboBox.setModel(variableIconRedColorScaleModel);
    greenScaleComboBox.setModel(variableIconGreenColorScaleModel);
    blueScaleComboBox.setModel(variableIconBlueColorScaleModel);

    if (displayType.equals(DisplayDescriptor.DisplayType.TWO_D)) {
      this.setTitle("2D Shape Editor");
      preview = new PreviewIcon2D();
      previewPanel.add((PreviewIcon2D) preview, cc.xy(1, 1));

      iconButton.setText("Select Icon File");
      iconButton.setFont(iconButton.getFont().deriveFont(Font.PLAIN));
      clearFileButton.setText("Clear Icon File");

      if (userStyleData.getIconFile2D() != null) {
        disableColorButtons();
        iconButton.setFont(iconButton.getFont().deriveFont(Font.BOLD));
        iconButton.setText("Icon Set");
      }

      textureButton.setVisible(false);
      clearTextureButton.setVisible(false);
    } else {
      this.setTitle("3D Shape Editor");
      preview = new PreviewIcon3D();

      iconButton.setText("Select Model File");
      iconButton.setFont(iconButton.getFont().deriveFont(Font.PLAIN));
      clearFileButton.setText("Clear Model File");

      if (userStyleData.getModelFile3D() != null) {
        disableColorButtons();
        iconButton.setFont(iconButton.getFont().deriveFont(Font.BOLD));
        iconButton.setText("Model Set");
      }

      if (userStyleData.getTextureFile3D() != null) {
        disableColorButtons();
        textureButton.setFont(textureButton.getFont().deriveFont(Font.BOLD));
        textureButton.setText("Texture Set");
      }
    }

    if (userStyleData.getIconFile2D() != null){
    	File iconFile = new File(userStyleData.getIconFile2D());
    	preview.setIconFile(iconFile);
    	shapeComboBox.setEnabled(false);
      iconColorbutton.setEnabled(false);
    }
    preview.setMark((userStyleData).getShapeWkt());
    preview.setMarkSize(userStyleData.getSize());
    float[] col = userStyleData.getColor();
    preview.setFillColor(new Color(col[0], col[1], col[2]));

    float labelColor[] = userStyleData.getLabelColor();
    Color labelPaint = new Color(labelColor[0], labelColor[1], labelColor[2]);
    fontColorButton.setIcon(new SquareIcon(14,14,labelPaint));
    preview.setEditorFontColor(labelPaint);

    preview.setEditorFont(new Font(userStyleData.getLabelFontFamily(),
            userStyleData.getLabelFontType(), userStyleData.getLabelFontSize()));

    float iconColor[] = userStyleData.getColor();
    Color iconPaint = new Color(iconColor[0], iconColor[1], iconColor[2]);
    iconColorbutton.setIcon(new SquareIcon(14,14,iconPaint));

    labelPrecisionComboBox.getModel().setSelectedItem(Integer
            .toString(userStyleData.getLabelPrecision()));
  }

  public void writeStyleData() {
    XStream xstream = new XStream(new XppDriver(new XStream11XmlFriendlyReplacer())) {
      protected boolean useXStream11XmlFriendlyMapper() {
        return true;
      }
    };

    File file = null;
    try {
      File dir = new File(EditedStyleUtils.getStyleDirName());

      if (!dir.exists())
        dir.mkdir();

      if (userStyleName != null)
        file = new File(dir, userStyleName);

      else {
        int cnt = 0;
        userStyleName = agentClassName + ".style_" + cnt + ".xml";
        file = new File(dir, userStyleName);
        while (file.exists()) {
          userStyleName = agentClassName + ".style_" + cnt + ".xml";
          file = new File(dir, userStyleName);
          cnt++;
        }
      }

      FileWriter fw = new FileWriter(file);

      xstream.toXML(userStyleData, fw);
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean doSave() {
    return save;
  }

  private void cancelButtonActionPerformed(ActionEvent e) {
    dispose();
  }

  private void okButtonActionPerformed(ActionEvent e) {
    save = true;
    writeStyleData();
    dispose();
  }

  private void shapeComboBoxActionPerformed(ActionEvent e) {
    userStyleData.setShapeWkT((String) shapeComboBox.getSelectedItem());

    preview.setMark((String) shapeComboBox.getSelectedItem());
  }

  private void sizeComboBoxActionPerformed(ActionEvent e) {
    Object selection = sizeComboBox.getSelectedItem();

    if (selection instanceof Number) {
      userStyleData.setSize((Float) selection);
      preview.setMarkSize((Float) selection);
      userStyleData.setSizeMethodName(null);
    } else if (isUserTypedNumber(selection)) {
      userStyleData.setSize(new Float((String) selection));
      preview.setMarkSize(new Float((String) selection));
      userStyleData.setSizeMethodName(null);
    } else
      userStyleData.setSizeMethodName((String) selection);
  }

  private void sizeMinComboBoxActionPerformed(ActionEvent e) {
    Object selection = sizeMinComboBox.getSelectedItem();

    if (selection instanceof Number) {
      userStyleData.setSizeMin((Float) selection);
      userStyleData.setSizeMinMethodName(null);
    } else if (isUserTypedNumber(selection)) {
      userStyleData.setSizeMin(new Float((String) selection));
      userStyleData.setSizeMinMethodName(null);
    } else
      userStyleData.setSizeMinMethodName((String) selection);
  }

  private void sizeMaxComboBoxActionPerformed(ActionEvent e) {
    Object selection = sizeMaxComboBox.getSelectedItem();

    if (selection instanceof Number) {
      userStyleData.setSizeMax((Float) selection);
      userStyleData.setSizeMaxMethodName(null);
    } else if (isUserTypedNumber(selection)) {
      userStyleData.setSizeMax(new Float((String) selection));
      userStyleData.setSizeMaxMethodName(null);
    } else
      userStyleData.setSizeMaxMethodName((String) selection);
  }

  private void sizeScaleComboBoxActionPerformed(ActionEvent e) {
    Object selection = sizeScaleComboBox.getSelectedItem();

    if (selection instanceof Number) {
      userStyleData.setSizeScale((Float) selection);
      userStyleData.setSizeScaleMethodName(null);
    } else if (isUserTypedNumber(selection)) {
      userStyleData.setSizeScale(new Float((String) selection));
      userStyleData.setSizeScaleMethodName(null);
    } else
      userStyleData.setSizeScaleMethodName((String) selection);
  }

  private void labelComboBoxActionPerformed(ActionEvent e) {
    String selection = (String) labelComboBox.getSelectedItem();

    if ("Name".equals(selection))
      userStyleData.setLabelMethod("toString");

    else if (labelMethodList.contains(selection))
      userStyleData.setLabelMethod(selection);

    else {
      userStyleData.setLabel(selection);
      userStyleData.setLabelMethod(null);
    }
  }

  private void labelPositionComboBoxActionPerformed(ActionEvent e) {
    userStyleData.setLabelPosition((String) labelPositionComboBox.getSelectedItem());
  }

  private void labelOffsetTextFieldActionPerformed(ActionEvent e) {
    userStyleData.setLabelOffset(new Float(labelOffsetTextField.getText()));
  }

  private void labelFontFamilyComboBoxActionPerformed(ActionEvent e) {
    String family = (String) labelFontFamilyComboBox.getSelectedItem();
    userStyleData.setLabelFontFamily(family);

    int type = labelFontStyleComboBox.getSelectedIndex();
    int size = new Integer((String) labelFontSizeComboBox.getSelectedItem());
    Font font = new Font(family, type, size);
    preview.setEditorFont(font);
  }

  private void labelFontSizeComboBoxActionPerformed(ActionEvent e) {
    String size = (String) labelFontSizeComboBox.getSelectedItem();
    userStyleData.setLabelFontSize(new Integer(size));

    int type = labelFontStyleComboBox.getSelectedIndex();
    String family = (String) labelFontFamilyComboBox.getSelectedItem();

    Font font = new Font(family, type, new Integer(size));
    preview.setEditorFont(font);
  }

  private void labelFontStyleComboBoxActionPerformed(ActionEvent e) {
    int type = labelFontStyleComboBox.getSelectedIndex();
    userStyleData.setLabelFontType(type);

    int size = new Integer((String) labelFontSizeComboBox.getSelectedItem());
    String family = (String) labelFontFamilyComboBox.getSelectedItem();

    Font font = new Font(family, type, size);
    preview.setEditorFont(font);
  }

  private void fontColorButtonActionPerformed(ActionEvent e) {
    float labelColor[] = userStyleData.getLabelColor();
    Color labelPaint = new Color(labelColor[0], labelColor[1], labelColor[2]);

    Color color = JColorChooser.showDialog(EditedStyleDialog.this,
            "Choose a Font Color", labelPaint);
    if (color != null) {
      userStyleData.setLabelColor(color.getRGBColorComponents(null));
      fontColorButton.setIcon(new SquareIcon(14,14,color));
      preview.setEditorFontColor(color);
    }
  }

  private void iconColorbuttonActionPerformed(ActionEvent e) {
    float iconColor[] = userStyleData.getColor();
    Color iconPaint = new Color(iconColor[0], iconColor[1], iconColor[2]);

    Color color = JColorChooser.showDialog(EditedStyleDialog.this,
            "Choose an Icon Color", iconPaint);
    if (color != null) {
      float col[] = color.getRGBColorComponents(null);
      userStyleData.setColor(col);
      userStyleData.setRedMethod(null);
      userStyleData.setGreenMethod(null);
      userStyleData.setBlueMethod(null);
      variableIconRedColorValueModel.addElement(col[0]);
      variableIconGreenColorValueModel.addElement(col[1]);
      variableIconBlueColorValueModel.addElement(col[2]);
      variableIconRedColorValueModel.setSelectedItem(col[0]);
      variableIconGreenColorValueModel.setSelectedItem(col[1]);
      variableIconBlueColorValueModel.setSelectedItem(col[2]);

      iconColorbutton.setIcon(new SquareIcon(14,14,color));
      preview.setFillColor(color);
    }
  }

  private void redValueComboBoxActionPerformed(ActionEvent e) {
    Object selection = redValueComboBox.getSelectedItem();

    if (selection instanceof Number) {

    } else if (isUserTypedNumber(selection)) {

    } else
      userStyleData.setRedMethod((String) selection);
  }

  private void greenValueComboBoxActionPerformed(ActionEvent e) {
    Object selection = greenValueComboBox.getSelectedItem();

    if (selection instanceof Number) {

    } else if (isUserTypedNumber(selection)) {

    } else
      userStyleData.setGreenMethod((String) selection);
  }

  private void blueValueComboBoxActionPerformed(ActionEvent e) {
    Object selection = blueValueComboBox.getSelectedItem();

    if (selection instanceof Number) {

    } else if (isUserTypedNumber(selection)) {

    } else
      userStyleData.setBlueMethod((String) selection);
  }

  private void redMinComboBoxActionPerformed(ActionEvent e) {
    float[] c = userStyleData.getColorMin();
    c[0] = (Float) redMinComboBox.getSelectedItem();
    userStyleData.setColorMin(c);
  }

  private void greenMinComboBoxActionPerformed(ActionEvent e) {
    float[] c = userStyleData.getColorMin();
    c[1] = (Float) greenMinComboBox.getSelectedItem();
    userStyleData.setColorMin(c);
  }

  private void blueMinComboBoxActionPerformed(ActionEvent e) {
    float[] c = userStyleData.getColorMin();
    c[2] = (Float) blueMinComboBox.getSelectedItem();
    userStyleData.setColorMin(c);
  }

  private void redMaxComboBoxActionPerformed(ActionEvent e) {
    float[] c = userStyleData.getColorMax();
    c[0] = (Float) redMaxComboBox.getSelectedItem();
    userStyleData.setColorMax(c);
  }

  private void greenMaxComboBoxActionPerformed(ActionEvent e) {
    float[] c = userStyleData.getColorMax();
    c[1] = (Float) greenMaxComboBox.getSelectedItem();
    userStyleData.setColorMax(c);
  }

  private void blueMaxComboBoxActionPerformed(ActionEvent e) {
    float[] c = userStyleData.getColorMax();
    c[2] = (Float) blueMaxComboBox.getSelectedItem();
    userStyleData.setColorMax(c);
  }

  private void redScaleComboBoxActionPerformed(ActionEvent e) {
    float[] c = userStyleData.getColorScale();
    c[0] = (Float) redScaleComboBox.getSelectedItem();
    userStyleData.setColorScale(c);
  }

  private void greenScaleComboBoxActionPerformed(ActionEvent e) {
    float[] c = userStyleData.getColorScale();
    c[1] = (Float) greenScaleComboBox.getSelectedItem();
    userStyleData.setColorScale(c);
  }

  private void blueScaleComboBoxActionPerformed(ActionEvent e) {
    float[] c = userStyleData.getColorScale();
    c[2] = (Float) blueScaleComboBox.getSelectedItem();
    userStyleData.setColorScale(c);
  }

  private void iconButtonActionPerformed(ActionEvent e) {
    File currentFile;
    
    String projetRoot = ScenarioUtils.getScenarioDir().getParentFile().getAbsolutePath(); 
    
    if (userStyleData.getIconFile2D() != null)
      currentFile = new File(userStyleData.getIconFile2D());

    else
      currentFile = new File(projetRoot);

    if (!currentFile.exists())
      currentFile = currentFile.getParentFile();

    shapeComboBox.setEnabled(false);
    iconColorbutton.setEnabled(false);
    File chosenFile;

    if (displayType.equals(DisplayDescriptor.DisplayType.TWO_D)) {
      chosenFile = FileChooserUtilities.getFile(this, "Model File", "Select", currentFile,
              new IconFile2DFilter());

      if (chosenFile != null) {
        iconButton.setFont(iconButton.getFont().deriveFont(Font.BOLD));
        userStyleData.setIconFile2D(makeRelativePath(chosenFile.getAbsolutePath()));
        iconButton.setText("Icon Set");
        preview.setIconFile(chosenFile);
        disableColorButtons();
      }
    } else {
      chosenFile = FileChooserUtilities.getFile(this, "Model File", "Select", currentFile,
              new ModelFile3DFilter());

      if (chosenFile != null) {
        iconButton.setFont(iconButton.getFont().deriveFont(Font.BOLD));
        userStyleData.setModelFile3D(makeRelativePath(chosenFile.getAbsolutePath()));
        iconButton.setText("Model Set");
      }
    }
  }
  
  /**
   * Checks if the selected path contains the project root and if so, remove
   * the project root from the path, making it a relative path.
   * 
   * @param fileName the full path to the file
   * @return the relative path to the project root
   */
  private String makeRelativePath(String fileName){
  	String path;
  	String projectRoot = ScenarioUtils.getScenarioDir().getParentFile().getAbsolutePath();  
  	
  	if (fileName.startsWith(projectRoot))
  		path = StringUtils.substringAfter(fileName, projectRoot);
  	
  	else{ 
  		path = fileName;
  		
  		//TODO warn user about icons external to project.
  		//TODO offer to copy icon into user project?
  	
  	}
    // force the file separator to "/"
  	path = StringUtils.replace(path, "\\", "/");
  	
  	// strip leading file separators if any
  	if (path.charAt(0) == '/')
  		path = StringUtils.substringAfter(path, String.valueOf(path.charAt(0)));
  	
  	return path;
  
  }

  private void clearFileButtonActionPerformed(ActionEvent e) {
    iconButton.setFont(iconButton.getFont().deriveFont(Font.PLAIN));

    if (displayType.equals(DisplayDescriptor.DisplayType.TWO_D)) {
      userStyleData.setIconFile2D(null);
      iconButton.setText("Select Icon File");
      preview.setIconFile(null);
      enableColorButtons();
    } else {
      userStyleData.setModelFile3D(null);
      iconButton.setText("Select Model File");
    }
    shapeComboBox.setEnabled(true);
    iconColorbutton.setEnabled(true);
  }

  private void textureButtonActionPerformed(ActionEvent e) {
    File currentFile;
    if (userStyleData.getTextureFile3D() != null)
      currentFile = new File(userStyleData.getTextureFile3D());

    else
      currentFile = new File("/");

    if (!currentFile.exists())
      currentFile = currentFile.getParentFile();

    File chosenFile = FileChooserUtilities.getFile(this, "Model File", "Select", currentFile,
            new IconFile2DFilter());

    if (chosenFile != null) {
      userStyleData.setTextureFile3D(chosenFile.getAbsolutePath());
      textureButton.setFont(textureButton.getFont().deriveFont(Font.BOLD));
      textureButton.setText("Texture Set");
      disableColorButtons();
    }
  }

  private void clearTextureButtonActionPerformed(ActionEvent e) {
    userStyleData.setTextureFile3D(null);
    textureButton.setFont(textureButton.getFont().deriveFont(Font.PLAIN));
    ;
    textureButton.setText("Select Texture");
    enableColorButtons();
  }

  private void disableColorButtons() {
    redMaxComboBox.setEnabled(false);
    redMinComboBox.setEnabled(false);
    redScaleComboBox.setEnabled(false);
    redValueComboBox.setEnabled(false);
    greenMaxComboBox.setEnabled(false);
    greenMinComboBox.setEnabled(false);
    greenScaleComboBox.setEnabled(false);
    greenValueComboBox.setEnabled(false);
    blueMaxComboBox.setEnabled(false);
    blueMinComboBox.setEnabled(false);
    blueScaleComboBox.setEnabled(false);
    blueValueComboBox.setEnabled(false);
  }

  private void enableColorButtons() {
    redMaxComboBox.setEnabled(true);
    redMinComboBox.setEnabled(true);
    redScaleComboBox.setEnabled(true);
    redValueComboBox.setEnabled(true);
    greenMaxComboBox.setEnabled(true);
    greenMinComboBox.setEnabled(true);
    greenScaleComboBox.setEnabled(true);
    greenValueComboBox.setEnabled(true);
    blueMaxComboBox.setEnabled(true);
    blueMinComboBox.setEnabled(true);
    blueScaleComboBox.setEnabled(true);
    blueValueComboBox.setEnabled(true);
  }

  private void labelPrecisionComboBoxActionPerformed(ActionEvent e) {
    String precision = (String) labelPrecisionComboBox.getSelectedItem();
    userStyleData.setLabelPrecision(new Integer(precision));
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    dialogPane = new JPanel();
    buttonBar = new JPanel();
    okButton = new JButton();
    cancelButton = new JButton();
    panel1 = new JPanel();
    shapePanel = new JPanel();
    shapeComboBox = new JComboBox();
    iconColorbutton = new JButton();
    iconButton = new JButton();
    clearFileButton = new JButton();
    textureButton = new JButton();
    clearTextureButton = new JButton();
    previewPanel = new JPanel();
    panel3 = new JPanel();
    label1 = new JLabel();
    label2 = new JLabel();
    label3 = new JLabel();
    label4 = new JLabel();
    sizeComboBox = new JComboBox();
    sizeMinComboBox = new JComboBox();
    sizeMaxComboBox = new JComboBox();
    sizeScaleComboBox = new JComboBox();
    panel4 = new JPanel();
    label5 = new JLabel();
    label6 = new JLabel();
    label7 = new JLabel();
    label19 = new JLabel();
    labelComboBox = new JComboBox();
    labelPositionComboBox = new JComboBox();
    labelOffsetTextField = new JTextField();
    labelPrecisionComboBox = new JComboBox();
    panel2 = new JPanel();
    label8 = new JLabel();
    label9 = new JLabel();
    label10 = new JLabel();
    label11 = new JLabel();
    labelFontFamilyComboBox = new JComboBox();
    labelFontSizeComboBox = new JComboBox();
    labelFontStyleComboBox = new JComboBox();
    fontColorButton = new JButton();
    panel6 = new JPanel();
    label15 = new JLabel();
    label16 = new JLabel();
    label17 = new JLabel();
    label18 = new JLabel();
    label12 = new JLabel();
    redValueComboBox = new JComboBox();
    redMinComboBox = new JComboBox();
    redMaxComboBox = new JComboBox();
    redScaleComboBox = new JComboBox();
    label13 = new JLabel();
    greenValueComboBox = new JComboBox();
    greenMinComboBox = new JComboBox();
    greenMaxComboBox = new JComboBox();
    greenScaleComboBox = new JComboBox();
    label14 = new JLabel();
    blueValueComboBox = new JComboBox();
    blueMinComboBox = new JComboBox();
    blueMaxComboBox = new JComboBox();
    blueScaleComboBox = new JComboBox();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setModal(true);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== dialogPane ========
    {
      dialogPane.setBorder(Borders.DIALOG_BORDER);
      dialogPane.setLayout(new BorderLayout());

      //======== buttonBar ========
      {
        buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
        buttonBar.setLayout(new FormLayout(
                new ColumnSpec[]{
                        FormFactory.GLUE_COLSPEC,
                        FormFactory.BUTTON_COLSPEC,
                        FormFactory.RELATED_GAP_COLSPEC,
                        FormFactory.BUTTON_COLSPEC
                },
                RowSpec.decodeSpecs("pref")));

        //---- okButton ----
        okButton.setText("OK");
        okButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            okButtonActionPerformed(e);
          }
        });
        buttonBar.add(okButton, cc.xy(2, 1));

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            cancelButtonActionPerformed(e);
          }
        });
        buttonBar.add(cancelButton, cc.xy(4, 1));
      }
      dialogPane.add(buttonBar, BorderLayout.SOUTH);

      //======== panel1 ========
      {
        panel1.setLayout(new FormLayout(
                new ColumnSpec[]{
                        new ColumnSpec("left:max(default;166dlu):grow"),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(Sizes.dluX(133)),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC
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
                        FormFactory.DEFAULT_ROWSPEC
                }));

        //======== shapePanel ========
        {
          shapePanel.setBorder(new TitledBorder("Icon Shape and Color"));
          shapePanel.setLayout(new FormLayout(
                  new ColumnSpec[]{
                          new ColumnSpec(Sizes.dluX(79)),
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC
                  },
                  new RowSpec[]{
                          FormFactory.DEFAULT_ROWSPEC,
                          FormFactory.LINE_GAP_ROWSPEC,
                          FormFactory.DEFAULT_ROWSPEC,
                          FormFactory.LINE_GAP_ROWSPEC,
                          FormFactory.DEFAULT_ROWSPEC,
                          FormFactory.LINE_GAP_ROWSPEC,
                          new RowSpec(Sizes.dluY(17))
                  }));

          //---- shapeComboBox ----
          shapeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              shapeComboBoxActionPerformed(e);
            }
          });
          shapePanel.add(shapeComboBox, cc.xy(1, 1));

          //---- iconColorbutton ----
          iconColorbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              iconColorbuttonActionPerformed(e);
            }
          });
          shapePanel.add(iconColorbutton, cc.xy(3, 1));

          //---- iconButton ----
          iconButton.setText("text");
          iconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              iconButtonActionPerformed(e);
            }
          });
          shapePanel.add(iconButton, cc.xy(1, 5));

          //---- clearFileButton ----
          clearFileButton.setText("text");
          clearFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              clearFileButtonActionPerformed(e);
            }
          });
          shapePanel.add(clearFileButton, cc.xy(3, 5));

          //---- textureButton ----
          textureButton.setText("Select Texture");
          textureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              textureButtonActionPerformed(e);
            }
          });
          shapePanel.add(textureButton, cc.xy(1, 7));

          //---- clearTextureButton ----
          clearTextureButton.setText("Clear Texture");
          clearTextureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              clearTextureButtonActionPerformed(e);
            }
          });
          shapePanel.add(clearTextureButton, cc.xy(3, 7));
        }
        panel1.add(shapePanel, cc.xywh(1, 1, 2, 1));

        //======== previewPanel ========
        {
          previewPanel.setBorder(new TitledBorder("Icon Preview"));
          previewPanel.setLayout(new FormLayout(
                  "101dlu:grow",
                  "top:57dlu:grow"));
        }
        panel1.add(previewPanel, cc.xywh(5, 1, 3, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //======== panel3 ========
        {
          panel3.setBorder(new TitledBorder("Icon Size"));
          panel3.setLayout(new FormLayout(
                  new ColumnSpec[]{
                          new ColumnSpec("max(pref;66dlu)"),
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC
                  },
                  new RowSpec[]{
                          FormFactory.DEFAULT_ROWSPEC,
                          FormFactory.LINE_GAP_ROWSPEC,
                          FormFactory.DEFAULT_ROWSPEC
                  }));

          //---- label1 ----
          label1.setText("Value");
          panel3.add(label1, cc.xy(1, 1));

          //---- label2 ----
          label2.setText("Minimum");
          panel3.add(label2, cc.xy(5, 1));

          //---- label3 ----
          label3.setText("Maximum");
          panel3.add(label3, cc.xy(9, 1));

          //---- label4 ----
          label4.setText("Scaling");
          panel3.add(label4, cc.xy(13, 1));

          //---- sizeComboBox ----
          sizeComboBox.setEditable(true);
          sizeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              sizeComboBoxActionPerformed(e);
            }
          });
          panel3.add(sizeComboBox, cc.xy(1, 3));

          //---- sizeMinComboBox ----
          sizeMinComboBox.setEditable(true);
          sizeMinComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              sizeMinComboBoxActionPerformed(e);
            }
          });
          panel3.add(sizeMinComboBox, cc.xy(5, 3));

          //---- sizeMaxComboBox ----
          sizeMaxComboBox.setEditable(true);
          sizeMaxComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              sizeMaxComboBoxActionPerformed(e);
            }
          });
          panel3.add(sizeMaxComboBox, cc.xy(9, 3));

          //---- sizeScaleComboBox ----
          sizeScaleComboBox.setEditable(true);
          sizeScaleComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              sizeScaleComboBoxActionPerformed(e);
            }
          });
          panel3.add(sizeScaleComboBox, cc.xy(13, 3));
        }
        panel1.add(panel3, cc.xywh(1, 3, 7, 1));

        //======== panel4 ========
        {
          panel4.setBorder(new TitledBorder("Icon Label"));
          panel4.setLayout(new FormLayout(
                  new ColumnSpec[]{
                          new ColumnSpec("max(pref;67dlu)"),
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          new ColumnSpec(Sizes.dluX(33)),
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC
                  },
                  new RowSpec[]{
                          FormFactory.DEFAULT_ROWSPEC,
                          FormFactory.LINE_GAP_ROWSPEC,
                          FormFactory.DEFAULT_ROWSPEC
                  }));

          //---- label5 ----
          label5.setText("Value");
          panel4.add(label5, cc.xy(1, 1));

          //---- label6 ----
          label6.setText("Position");
          panel4.add(label6, cc.xy(5, 1));

          //---- label7 ----
          label7.setText("Offset");
          panel4.add(label7, cc.xy(9, 1));

          //---- label19 ----
          label19.setText("Precision");
          panel4.add(label19, cc.xy(13, 1));

          //---- labelComboBox ----
          labelComboBox.setEditable(true);
          labelComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              labelComboBoxActionPerformed(e);
            }
          });
          panel4.add(labelComboBox, cc.xy(1, 3));

          //---- labelPositionComboBox ----
          labelPositionComboBox.setEditable(true);
          labelPositionComboBox.setModel(new DefaultComboBoxModel(new String[]{
                  "bottom",
                  "top",
                  "left",
                  "right"
          }));
          labelPositionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              labelPositionComboBoxActionPerformed(e);
            }
          });
          panel4.add(labelPositionComboBox, cc.xy(5, 3));

          //---- labelOffsetTextField ----
          labelOffsetTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              labelOffsetTextFieldActionPerformed(e);
            }
          });
          panel4.add(labelOffsetTextField, cc.xy(9, 3));

          //---- labelPrecisionComboBox ----
          labelPrecisionComboBox.setEditable(true);
          labelPrecisionComboBox.setModel(new DefaultComboBoxModel(new String[]{
                  "1",
                  "2",
                  "3",
                  "4",
                  "5",
                  "6",
                  "7",
                  "8",
                  "9",
                  "10",
                  "11",
                  "12"
          }));
          labelPrecisionComboBox.setSelectedIndex(2);
          labelPrecisionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              labelPrecisionComboBoxActionPerformed(e);
            }
          });
          panel4.add(labelPrecisionComboBox, cc.xy(13, 3));
        }
        panel1.add(panel4, cc.xywh(1, 5, 7, 1));

        //======== panel2 ========
        {
          panel2.setBorder(new TitledBorder("Icon Label Font"));
          panel2.setLayout(new FormLayout(
                  new ColumnSpec[]{
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC
                  },
                  new RowSpec[]{
                          FormFactory.DEFAULT_ROWSPEC,
                          FormFactory.LINE_GAP_ROWSPEC,
                          FormFactory.DEFAULT_ROWSPEC
                  }));

          //---- label8 ----
          label8.setText("Font");
          panel2.add(label8, cc.xy(1, 1));

          //---- label9 ----
          label9.setText("Font Size");
          panel2.add(label9, cc.xy(5, 1));

          //---- label10 ----
          label10.setText("Font Style");
          panel2.add(label10, cc.xy(9, 1));

          //---- label11 ----
          label11.setText("Font Color");
          panel2.add(label11, cc.xy(13, 1));

          //---- labelFontFamilyComboBox ----
          labelFontFamilyComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              labelFontFamilyComboBoxActionPerformed(e);
            }
          });
          panel2.add(labelFontFamilyComboBox, cc.xy(1, 3));

          //---- labelFontSizeComboBox ----
          labelFontSizeComboBox.setModel(new DefaultComboBoxModel(new String[]{
                  "8",
                  "10",
                  "11",
                  "12",
                  "14",
                  "16",
                  "18",
                  "20",
                  "24",
                  "30",
                  "36",
                  "40",
                  "48",
                  "60",
                  "72 "
          }));
          labelFontSizeComboBox.setEditable(true);
          labelFontSizeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              labelFontSizeComboBoxActionPerformed(e);
            }
          });
          panel2.add(labelFontSizeComboBox, cc.xy(5, 3));

          //---- labelFontStyleComboBox ----
          labelFontStyleComboBox.setModel(new DefaultComboBoxModel(new String[]{
                  "Plain",
                  "Bold",
                  "Italic"
          }));
          labelFontStyleComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              labelFontStyleComboBoxActionPerformed(e);
            }
          });
          panel2.add(labelFontStyleComboBox, cc.xy(9, 3));

          //---- fontColorButton ----
          fontColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              fontColorButtonActionPerformed(e);
            }
          });
          panel2.add(fontColorButton, cc.xy(13, 3));
        }
        panel1.add(panel2, cc.xywh(1, 7, 7, 1));

        //======== panel6 ========
        {
          panel6.setBorder(new TitledBorder("Variable Icon Color"));
          panel6.setLayout(new FormLayout(
                  new ColumnSpec[]{
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC,
                          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                          FormFactory.DEFAULT_COLSPEC
                  },
                  new RowSpec[]{
                          FormFactory.DEFAULT_ROWSPEC,
                          FormFactory.LINE_GAP_ROWSPEC,
                          FormFactory.DEFAULT_ROWSPEC,
                          FormFactory.LINE_GAP_ROWSPEC,
                          FormFactory.DEFAULT_ROWSPEC,
                          FormFactory.LINE_GAP_ROWSPEC,
                          FormFactory.DEFAULT_ROWSPEC
                  }));

          //---- label15 ----
          label15.setText("Value");
          panel6.add(label15, cc.xy(3, 1));

          //---- label16 ----
          label16.setText("Minimum");
          panel6.add(label16, cc.xy(7, 1));

          //---- label17 ----
          label17.setText("Maximum");
          panel6.add(label17, cc.xy(11, 1));

          //---- label18 ----
          label18.setText("Scaling");
          panel6.add(label18, cc.xy(15, 1));

          //---- label12 ----
          label12.setText("Red");
          panel6.add(label12, cc.xy(1, 3));

          //---- redValueComboBox ----
          redValueComboBox.setEditable(true);
          redValueComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              redValueComboBoxActionPerformed(e);
            }
          });
          panel6.add(redValueComboBox, cc.xy(3, 3));

          //---- redMinComboBox ----
          redMinComboBox.setEditable(true);
          redMinComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              redMinComboBoxActionPerformed(e);
            }
          });
          panel6.add(redMinComboBox, cc.xy(7, 3));

          //---- redMaxComboBox ----
          redMaxComboBox.setEditable(true);
          redMaxComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              redMaxComboBoxActionPerformed(e);
            }
          });
          panel6.add(redMaxComboBox, cc.xy(11, 3));

          //---- redScaleComboBox ----
          redScaleComboBox.setEditable(true);
          redScaleComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              redScaleComboBoxActionPerformed(e);
            }
          });
          panel6.add(redScaleComboBox, cc.xy(15, 3));

          //---- label13 ----
          label13.setText("Green");
          panel6.add(label13, cc.xy(1, 5));

          //---- greenValueComboBox ----
          greenValueComboBox.setEditable(true);
          greenValueComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              greenValueComboBoxActionPerformed(e);
            }
          });
          panel6.add(greenValueComboBox, cc.xy(3, 5));

          //---- greenMinComboBox ----
          greenMinComboBox.setEditable(true);
          greenMinComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              greenMinComboBoxActionPerformed(e);
            }
          });
          panel6.add(greenMinComboBox, cc.xy(7, 5));

          //---- greenMaxComboBox ----
          greenMaxComboBox.setEditable(true);
          greenMaxComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              greenMaxComboBoxActionPerformed(e);
            }
          });
          panel6.add(greenMaxComboBox, cc.xy(11, 5));

          //---- greenScaleComboBox ----
          greenScaleComboBox.setEditable(true);
          greenScaleComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              greenScaleComboBoxActionPerformed(e);
            }
          });
          panel6.add(greenScaleComboBox, cc.xy(15, 5));

          //---- label14 ----
          label14.setText("Blue");
          panel6.add(label14, cc.xy(1, 7));

          //---- blueValueComboBox ----
          blueValueComboBox.setEditable(true);
          blueValueComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              blueValueComboBoxActionPerformed(e);
            }
          });
          panel6.add(blueValueComboBox, cc.xy(3, 7));

          //---- blueMinComboBox ----
          blueMinComboBox.setEditable(true);
          blueMinComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              blueMinComboBoxActionPerformed(e);
            }
          });
          panel6.add(blueMinComboBox, cc.xy(7, 7));

          //---- blueMaxComboBox ----
          blueMaxComboBox.setEditable(true);
          blueMaxComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              blueMaxComboBoxActionPerformed(e);
            }
          });
          panel6.add(blueMaxComboBox, cc.xy(11, 7));

          //---- blueScaleComboBox ----
          blueScaleComboBox.setEditable(true);
          blueScaleComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              blueScaleComboBoxActionPerformed(e);
            }
          });
          panel6.add(blueScaleComboBox, cc.xy(15, 7));
        }
        panel1.add(panel6, cc.xywh(1, 9, 7, 1));
      }
      dialogPane.add(panel1, BorderLayout.CENTER);
    }
    contentPane.add(dialogPane, BorderLayout.CENTER);
    setSize(520, 595);
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  //	JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JPanel dialogPane;
  private JPanel buttonBar;
  private JButton okButton;
  private JButton cancelButton;
  private JPanel panel1;
  private JPanel shapePanel;
  private JComboBox shapeComboBox;
  private JButton iconColorbutton;
  private JButton iconButton;
  private JButton clearFileButton;
  private JButton textureButton;
  private JButton clearTextureButton;
  private JPanel previewPanel;
  private JPanel panel3;
  private JLabel label1;
  private JLabel label2;
  private JLabel label3;
  private JLabel label4;
  private JComboBox sizeComboBox;
  private JComboBox sizeMinComboBox;
  private JComboBox sizeMaxComboBox;
  private JComboBox sizeScaleComboBox;
  private JPanel panel4;
  private JLabel label5;
  private JLabel label6;
  private JLabel label7;
  private JLabel label19;
  private JComboBox labelComboBox;
  private JComboBox labelPositionComboBox;
  private JTextField labelOffsetTextField;
  private JComboBox labelPrecisionComboBox;
  private JPanel panel2;
  private JLabel label8;
  private JLabel label9;
  private JLabel label10;
  private JLabel label11;
  private JComboBox labelFontFamilyComboBox;
  private JComboBox labelFontSizeComboBox;
  private JComboBox labelFontStyleComboBox;
  private JButton fontColorButton;
  private JPanel panel6;
  private JLabel label15;
  private JLabel label16;
  private JLabel label17;
  private JLabel label18;
  private JLabel label12;
  private JComboBox redValueComboBox;
  private JComboBox redMinComboBox;
  private JComboBox redMaxComboBox;
  private JComboBox redScaleComboBox;
  private JLabel label13;
  private JComboBox greenValueComboBox;
  private JComboBox greenMinComboBox;
  private JComboBox greenMaxComboBox;
  private JComboBox greenScaleComboBox;
  private JLabel label14;
  private JComboBox blueValueComboBox;
  private JComboBox blueMinComboBox;
  private JComboBox blueMaxComboBox;
  private JComboBox blueScaleComboBox;
//	JFormDesigner - End of variables declaration  //GEN-END:variables

  public String getUserStyleName() {
    return userStyleName;
  }

  private boolean isUserTypedNumber(Object obj) {
    String validChars = "0123456789";
    boolean isNumber = true;

    if (obj instanceof String) {

      String s = (String) obj;

      char c = s.charAt(0);

      if (validChars.indexOf(c) == -1)
        return false;

      else
        return true;
    }
    return false;
  }
}
