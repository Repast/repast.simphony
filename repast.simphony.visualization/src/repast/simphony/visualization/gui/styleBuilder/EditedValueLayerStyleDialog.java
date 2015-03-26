package repast.simphony.visualization.gui.styleBuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import repast.simphony.ui.plugin.editor.SquareIcon;
import repast.simphony.visualization.editedStyle.DefaultEditedValueLayerStyleData2D;
import repast.simphony.visualization.editedStyle.DefaultEditedValueLayerStyleData3D;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;
import repast.simphony.visualization.editedStyle.EditedValueLayerStyleData;
import repast.simphony.visualization.engine.CartesianDisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XStream11XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * @author Eric Tatara
 */
public class EditedValueLayerStyleDialog extends JDialog {
  private boolean save = false;
  private EditedValueLayerStyleData userStyleData;

  private String valueLayerName;
  private String userStyleName;
  private String displayType;

  public EditedValueLayerStyleDialog(Frame owner) {
    super(owner);
  }

  public EditedValueLayerStyleDialog(Dialog owner) {
    super(owner);
  }

  public void init(String valueLayerName, String userStyleName,
                   CartesianDisplayDescriptor descriptor) {
    this.valueLayerName = valueLayerName;
    this.userStyleName = userStyleName;
    this.displayType = descriptor.getDisplayType();

    userStyleData = EditedStyleUtils.getValueLayerStyle(descriptor.getValueLayerEditedStyleName());

    // TODO Projections: init from viz registry data entries
    // Set objects based on display type 2D/3D
    if (userStyleData == null){
    	if (displayType.equals(DisplayType.TWO_D)) 
    		userStyleData = new DefaultEditedValueLayerStyleData2D();
    	else if (displayType.equals(DisplayType.THREE_D))
    		userStyleData = new DefaultEditedValueLayerStyleData3D();
    }

    initComponents();
    initMyComponents(displayType);
  }

  private void initMyComponents(String displayType) {
  
  	// set initial data from userStyleData
  	float[] c = userStyleData.getColor();
  	RedTextField.setText(Float.toString(c[0]));
  	GreenTextField.setText(Float.toString(c[1]));
  	BlueTextField.setText(Float.toString(c[2]));
  	
    iconColorbutton.setIcon(new SquareIcon(15,15,new Color(c[0], c[1], c[2])));

  	boolean[] cval = userStyleData.getColorValue();
  	RedCheckBox.setSelected(cval[0]);
  	GreenCheckBox.setSelected(cval[1]);
  	BlueCheckBox.setSelected(cval[2]);
  	
  	c = userStyleData.getColorMin();
  	RedMinTextField.setText(Float.toString(c[0]));
  	GreenMinTextField.setText(Float.toString(c[1]));
  	BlueMinTextField.setText(Float.toString(c[2]));
  	
  	c = userStyleData.getColorMax();
  	RedMaxTextField.setText(Float.toString(c[0]));
  	GreenMaxTextField.setText(Float.toString(c[1]));
  	BlueMaxTextField.setText(Float.toString(c[2]));

  	c = userStyleData.getColorScale();
  	RedScaleTextField.setText(Float.toString(c[0]));
  	GreenScaleTextField.setText(Float.toString(c[1]));
  	BlueScaleTextField.setText(Float.toString(c[2]));
  	
  	CellTextField.setText(Float.toString(userStyleData.getCellSize()));

    // TODO Projections: init from viz registry data entries
  	if (displayType.equals(DisplayType.TWO_D)) {
  		this.setTitle("2D Value Layer Editor");

  		HeightTextField.setEnabled(false);
  		HeightCheckBox.setEnabled(false);
  		HeightMinTextField.setEnabled(false);
  		HeightMaxTextField.setEnabled(false);
  		HeightScaleTextField.setEnabled(false);
  	} 
  	else {
  		this.setTitle("3D Shape Editor");

  		HeightTextField.setText(Float.toString(userStyleData.getY()));
  		HeightCheckBox.setSelected(userStyleData.isYValue());
  		HeightMinTextField.setText(Float.toString(userStyleData.getYMin()));
  		HeightMaxTextField.setText(Float.toString(userStyleData.getYMax()));
  		HeightScaleTextField.setText(Float.toString(userStyleData.getYScale()));
  	}
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
        userStyleName = valueLayerName + ".style_" + cnt + ".xml";
        file = new File(dir, userStyleName);
        while (file.exists()) {
          userStyleName = valueLayerName + ".style_" + cnt + ".xml";
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
    
  	// Apply the values from text fields here instead of using action listeners,
  	//  because the listener won't fire unless the user hits return in each
  	//  text field.
  	
    float[] c = userStyleData.getColor();
  	c[0] = Float.valueOf(RedTextField.getText());
  	c[1] = Float.valueOf(GreenTextField.getText());
  	c[2] = Float.valueOf(BlueTextField.getText());
  	
  	float[] minc = userStyleData.getColorMin();
  	minc[0] = Float.valueOf(RedMinTextField.getText());
  	minc[1] = Float.valueOf(GreenMinTextField.getText());
  	minc[2] = Float.valueOf(BlueMinTextField.getText());
  	
  	float[] maxc = userStyleData.getColorMax();
  	maxc[0] = Float.valueOf(RedMaxTextField.getText());
  	maxc[1] = Float.valueOf(GreenMaxTextField.getText());
  	maxc[2] = Float.valueOf(BlueMaxTextField.getText());
  	
  	float[] scalec = userStyleData.getColorScale();
  	scalec[0] = Float.valueOf(RedScaleTextField.getText());
  	scalec[1] = Float.valueOf(GreenScaleTextField.getText());
  	scalec[2] = Float.valueOf(BlueScaleTextField.getText());
  	
  	// Check if the constant color values are valid (0 - 1)
  	if (validateColorRange(c)){
  		save = true;
  		
  		userStyleData.setColor(c);
  		userStyleData.setColorMin(minc);
  		userStyleData.setColorMax(maxc);
  		userStyleData.setColorScale(scalec);
  		
  		userStyleData.setCellSize(Float.valueOf(CellTextField.getText()));

  	// TODO Projections: init from viz registry data entries
  		if (displayType.equals(DisplayType.THREE_D)){
  			userStyleData.setY(Float.valueOf(HeightTextField.getText()));
  			userStyleData.setYMin(Float.valueOf(HeightMinTextField.getText()));
  			userStyleData.setYMax(Float.valueOf(HeightMaxTextField.getText()));
  			userStyleData.setYScale(Float.valueOf(HeightScaleTextField.getText()));
  		}
  		
      writeStyleData();
      dispose();
    }
  }

  private void iconColorbuttonActionPerformed(ActionEvent e) {
    float iconColor[] = userStyleData.getColor();
    Color iconPaint = new Color(iconColor[0], iconColor[1], iconColor[2]);

    Color color = JColorChooser.showDialog(EditedValueLayerStyleDialog.this,
            "Choose an Icon Color", iconPaint);
    if (color != null) {
      float col[] = color.getRGBColorComponents(null);
      userStyleData.setColor(col);
      
      iconColorbutton.setIcon(new SquareIcon(15,15,color));
      
      RedTextField.setText(Float.toString(col[0]));
      GreenTextField.setText(Float.toString(col[1]));
      BlueTextField.setText(Float.toString(col[2]));
    }
  }

  private boolean validateColorRange(float[] c){
  	
  	for (int i=0; i<c.length; i++){
  		if (c[i] < 0 || c[i] > 1){
  			JOptionPane.showMessageDialog(null,
  	  	    "The valid range for color values is 0 - 1",
  	  	    "Color value out of bounds",
  	  	    JOptionPane.WARNING_MESSAGE);
  	  	
  	  	return false;
  		}
  	}
  	return true;
  }
  
  private void RedCheckBoxActionPerformed(ActionEvent e) {
  	 AbstractButton abstractButton = (AbstractButton) e.getSource();
     boolean selected = abstractButton.getModel().isSelected();
     
     boolean[] cval = userStyleData.getColorValue();
     cval[0] = selected;
     userStyleData.setColorValue(cval);
  }

  private void GreenCheckBoxActionPerformed(ActionEvent e) {
  	AbstractButton abstractButton = (AbstractButton) e.getSource();
    boolean selected = abstractButton.getModel().isSelected();
    
    boolean[] cval = userStyleData.getColorValue();
    cval[1] = selected;
    userStyleData.setColorValue(cval);
  }

  private void BlueCheckBoxActionPerformed(ActionEvent e) {
  	AbstractButton abstractButton = (AbstractButton) e.getSource();
    boolean selected = abstractButton.getModel().isSelected();
    
    boolean[] cval = userStyleData.getColorValue();
    cval[2] = selected;
    userStyleData.setColorValue(cval);
  }

  private void HeightCheckBoxActionPerformed(ActionEvent e) {
  	AbstractButton abstractButton = (AbstractButton) e.getSource();
    boolean selected = abstractButton.getModel().isSelected();
    
    userStyleData.setYValue(selected);
  }

  public String getUserStyleName() {
    return userStyleName;
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    dialogPane = new JPanel();
    buttonBar = new JPanel();
    okButton = new JButton();
    cancelButton = new JButton();
    panel1 = new JPanel();
    panel6 = new JPanel();
    label1 = new JLabel();
    iconColorbutton = new JButton();
    label15 = new JLabel();
    label2 = new JLabel();
    label16 = new JLabel();
    label17 = new JLabel();
    label18 = new JLabel();
    label12 = new JLabel();
    RedTextField = new JTextField();
    RedCheckBox = new JCheckBox();
    RedMinTextField = new JTextField();
    RedMaxTextField = new JTextField();
    RedScaleTextField = new JTextField();
    label13 = new JLabel();
    GreenTextField = new JTextField();
    GreenCheckBox = new JCheckBox();
    GreenMinTextField = new JTextField();
    GreenMaxTextField = new JTextField();
    GreenScaleTextField = new JTextField();
    label14 = new JLabel();
    BlueTextField = new JTextField();
    BlueCheckBox = new JCheckBox();
    BlueMinTextField = new JTextField();
    BlueMaxTextField = new JTextField();
    BlueScaleTextField = new JTextField();
    separator1 = new JSeparator();
    label3 = new JLabel();
    HeightTextField = new JTextField();
    HeightCheckBox = new JCheckBox();
    HeightMinTextField = new JTextField();
    HeightMaxTextField = new JTextField();
    HeightScaleTextField = new JTextField();
    separator2 = new JSeparator();
    label4 = new JLabel();
    CellTextField = new JTextField();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setModal(true);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== dialogPane ========
    {
    	dialogPane.setBorder(Borders.DIALOG);
    	dialogPane.setLayout(new BorderLayout());

    	//======== buttonBar ========
    	{
    		buttonBar.setBorder(Borders.BUTTON_BAR_PAD);
    		buttonBar.setLayout(new FormLayout(
    			new ColumnSpec[] {
    				FormSpecs.GLUE_COLSPEC,
    				FormSpecs.BUTTON_COLSPEC,
    				FormSpecs.RELATED_GAP_COLSPEC,
    				FormSpecs.BUTTON_COLSPEC
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
    			new ColumnSpec[] {
    				ColumnSpec.decode("left:max(default;166dlu):grow"),
    				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    				FormSpecs.DEFAULT_COLSPEC,
    				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    				new ColumnSpec(Sizes.dluX(133)),
    				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    				FormSpecs.DEFAULT_COLSPEC
    			},
    			RowSpec.decodeSpecs("default")));

    		//======== panel6 ========
    		{
    			panel6.setBorder(new TitledBorder("Value Layer Properties"));
    			panel6.setLayout(new FormLayout(
    				new ColumnSpec[] {
    					FormSpecs.DEFAULT_COLSPEC,
    					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    					FormSpecs.DEFAULT_COLSPEC,
    					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    					FormSpecs.DEFAULT_COLSPEC,
    					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    					FormSpecs.DEFAULT_COLSPEC,
    					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    					FormSpecs.DEFAULT_COLSPEC,
    					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    					FormSpecs.DEFAULT_COLSPEC,
    					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    					FormSpecs.DEFAULT_COLSPEC,
    					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    					FormSpecs.DEFAULT_COLSPEC,
    					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    					FormSpecs.DEFAULT_COLSPEC,
    					FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    					FormSpecs.DEFAULT_COLSPEC
    				},
    				new RowSpec[] {
    					FormSpecs.DEFAULT_ROWSPEC,
    					FormSpecs.LINE_GAP_ROWSPEC,
    					FormSpecs.DEFAULT_ROWSPEC,
    					FormSpecs.LINE_GAP_ROWSPEC,
    					FormSpecs.DEFAULT_ROWSPEC,
    					FormSpecs.LINE_GAP_ROWSPEC,
    					FormSpecs.DEFAULT_ROWSPEC,
    					FormSpecs.LINE_GAP_ROWSPEC,
    					FormSpecs.DEFAULT_ROWSPEC,
    					FormSpecs.LINE_GAP_ROWSPEC,
    					FormSpecs.DEFAULT_ROWSPEC,
    					FormSpecs.LINE_GAP_ROWSPEC,
    					FormSpecs.DEFAULT_ROWSPEC,
    					FormSpecs.LINE_GAP_ROWSPEC,
    					FormSpecs.DEFAULT_ROWSPEC,
    					FormSpecs.LINE_GAP_ROWSPEC,
    					FormSpecs.DEFAULT_ROWSPEC
    				}));

    			//---- label1 ----
    			label1.setText("Base Color");
    			panel6.add(label1, cc.xy(1, 1));

    			//---- iconColorbutton ----
    			iconColorbutton.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    					iconColorbuttonActionPerformed(e);
    				}
    			});
    			panel6.add(iconColorbutton, cc.xy(3, 1));

    			//---- label15 ----
    			label15.setText("Value");
    			panel6.add(label15, cc.xy(3, 3));

    			//---- label2 ----
    			label2.setText("Use Value Layer Data");
    			panel6.add(label2, cc.xy(7, 3));

    			//---- label16 ----
    			label16.setText("Minimum");
    			panel6.add(label16, cc.xy(11, 3));

    			//---- label17 ----
    			label17.setText("Maximum");
    			panel6.add(label17, cc.xy(15, 3));

    			//---- label18 ----
    			label18.setText("Scaling");
    			panel6.add(label18, cc.xy(19, 3));

    			//---- label12 ----
    			label12.setText("Red");
    			panel6.add(label12, cc.xy(1, 5));
    			panel6.add(RedTextField, cc.xy(3, 5));

    			//---- RedCheckBox ----
    			RedCheckBox.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    					RedCheckBoxActionPerformed(e);
    				}
    			});
    			panel6.add(RedCheckBox, cc.xy(7, 5));
    			panel6.add(RedMinTextField, cc.xy(11, 5));
    			panel6.add(RedMaxTextField, cc.xy(15, 5));
    			panel6.add(RedScaleTextField, cc.xy(19, 5));

    			//---- label13 ----
    			label13.setText("Green");
    			panel6.add(label13, cc.xy(1, 7));
    			panel6.add(GreenTextField, cc.xy(3, 7));

    			//---- GreenCheckBox ----
    			GreenCheckBox.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    					GreenCheckBoxActionPerformed(e);
    				}
    			});
    			panel6.add(GreenCheckBox, cc.xy(7, 7));
    			panel6.add(GreenMinTextField, cc.xy(11, 7));
    			panel6.add(GreenMaxTextField, cc.xy(15, 7));
    			panel6.add(GreenScaleTextField, cc.xy(19, 7));

    			//---- label14 ----
    			label14.setText("Blue");
    			panel6.add(label14, cc.xy(1, 9));
    			panel6.add(BlueTextField, cc.xy(3, 9));

    			//---- BlueCheckBox ----
    			BlueCheckBox.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    					BlueCheckBoxActionPerformed(e);
    				}
    			});
    			panel6.add(BlueCheckBox, cc.xy(7, 9));
    			panel6.add(BlueMinTextField, cc.xy(11, 9));
    			panel6.add(BlueMaxTextField, cc.xy(15, 9));
    			panel6.add(BlueScaleTextField, cc.xy(19, 9));
    			panel6.add(separator1, cc.xywh(1, 11, 19, 1));

    			//---- label3 ----
    			label3.setText("Height");
    			panel6.add(label3, cc.xy(1, 13));
    			panel6.add(HeightTextField, cc.xy(3, 13));

    			//---- HeightCheckBox ----
    			HeightCheckBox.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    					HeightCheckBoxActionPerformed(e);
    				}
    			});
    			panel6.add(HeightCheckBox, cc.xy(7, 13));
    			panel6.add(HeightMinTextField, cc.xy(11, 13));
    			panel6.add(HeightMaxTextField, cc.xy(15, 13));
    			panel6.add(HeightScaleTextField, cc.xy(19, 13));
    			panel6.add(separator2, cc.xywh(1, 15, 19, 1));

    			//---- label4 ----
    			label4.setText("Cell size");
    			panel6.add(label4, cc.xy(1, 17));
    			panel6.add(CellTextField, cc.xy(3, 17));
    		}
    		panel1.add(panel6, cc.xywh(1, 1, 5, 1));
    	}
    	dialogPane.add(panel1, BorderLayout.CENTER);
    }
    contentPane.add(dialogPane, BorderLayout.CENTER);
    setSize(540, 335);
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
  private JPanel panel6;
  private JLabel label1;
  private JButton iconColorbutton;
  private JLabel label15;
  private JLabel label2;
  private JLabel label16;
  private JLabel label17;
  private JLabel label18;
  private JLabel label12;
  private JTextField RedTextField;
  private JCheckBox RedCheckBox;
  private JTextField RedMinTextField;
  private JTextField RedMaxTextField;
  private JTextField RedScaleTextField;
  private JLabel label13;
  private JTextField GreenTextField;
  private JCheckBox GreenCheckBox;
  private JTextField GreenMinTextField;
  private JTextField GreenMaxTextField;
  private JTextField GreenScaleTextField;
  private JLabel label14;
  private JTextField BlueTextField;
  private JCheckBox BlueCheckBox;
  private JTextField BlueMinTextField;
  private JTextField BlueMaxTextField;
  private JTextField BlueScaleTextField;
  private JSeparator separator1;
  private JLabel label3;
  private JTextField HeightTextField;
  private JCheckBox HeightCheckBox;
  private JTextField HeightMinTextField;
  private JTextField HeightMaxTextField;
  private JTextField HeightScaleTextField;
  private JSeparator separator2;
  private JLabel label4;
  private JTextField CellTextField;
//	JFormDesigner - End of variables declaration  //GEN-END:variables

}
