package repast.simphony.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import repast.simphony.parameter.MutableParameters;
import repast.simphony.parameter.ParameterUtils;
import repast.simphony.parameter.ParametersParser;
import repast.simphony.parameter.StringConverter;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class AddParameterDialog extends JDialog {
	private JPanel inputPanel;
	private JTextField nameField;
	private JTextField displayNameField;
	private JTextField typeField;
	private JTextField defaultValueField;
	private JTextField converterField;
	private JTextField valuesField;
	private JCheckBox readOnlyCheckBox;
	
	private JPanel buttonBar;
	private JButton cancelButton;
	private JButton okButton;

	private MutableParameters params;
	private boolean parameterAdded;
	public AddParameterDialog(Frame owner) {
	    super(owner);
	    initComponents();
	  }

	  public AddParameterDialog(Dialog owner) {
	    super(owner);
	    initComponents();
	  }
	  private void addListeners() {
		    okButton.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		    	  parameterAdded = applyParameter();
		    	  if (parameterAdded) {
		    		  AddParameterDialog.this.dispose();
		    	  }
		      }
		    });

		    cancelButton.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		    	  parameterAdded = false;
		    	  AddParameterDialog.this.dispose();
		      }
		    });
		  }

  private boolean applyParameter() {
		  boolean requiresConverter = false;
    	  String name = getNameField().getText();
    	  String type = getTypeField().getText();
    	  String defValue = getDefaultValueField().getText();
    	  if (name == null && name.trim().length() == 0 || 
   			  type == null || type.trim().length()== 0 || 
   			  defValue==null || defValue.trim().length()==0) {
			  JOptionPane.showMessageDialog(AddParameterDialog.this, "A required value is missing. Values must be entered for Name, Type and Default Value.", "Missing Required Field", JOptionPane.ERROR_MESSAGE);
			  return false;
   		  
    	  }
    	  name = name.trim();
    	  type = type.trim();
    	  defValue = defValue.trim();
		  if (params.getSchema().contains(name)) {
			  JOptionPane.showMessageDialog(AddParameterDialog.this, "A Parameter named '"+name+"' already exists.  Please choose a different name", "Duplicate Parameter Name", JOptionPane.ERROR_MESSAGE);
			  return false;
		  }
		  
		  Class<?> typeCls = ParametersParser.typeMap.get(type);
		  if (typeCls == null) {
			//if it is not a known "primitive" type, then make sure the specified type is on the classpath
			try {
				typeCls = Class.forName(type);
  			} catch (ClassNotFoundException e) {
  			  JOptionPane.showMessageDialog(AddParameterDialog.this, "Type not found '"+type+"'.  Please enter the fully qualified type, or a primitive type.", "Type Class No Found", JOptionPane.ERROR_MESSAGE);
			  return false;
			}
  			requiresConverter = true;
		  }
		  String converter = getConverterField().getText();
		  if (requiresConverter && (converter == null || converter.trim().length() ==0)) {
  			  JOptionPane.showMessageDialog(AddParameterDialog.this, "A converter is required for '"+type+"'.", "Converter Required", JOptionPane.ERROR_MESSAGE);
			  return false;			  
		  }
		  StringConverter strConv = null;
		  if (converter != null && converter.trim().length() >0) {
			  converter = converter.trim();
				try {
					Class cls = Class.forName(converter);
					strConv = (StringConverter)cls.newInstance();
	  			} catch (Exception e) {
	  			  JOptionPane.showMessageDialog(AddParameterDialog.this, "Converter, '"+converter+"' could not be instantiated.", "Converter Creation Failed", JOptionPane.ERROR_MESSAGE);
				  return false;
				}
			  
		  }
		  boolean readOnly = getReadOnlyCheckbox().isSelected();
		  
		  Object defVal;
		  try {
			  if (strConv != null) defVal = strConv.fromString(defValue);
			  else defVal = ParameterUtils.parseDefaultValue(typeCls, name, defValue)[0];
		  } catch (Exception e) {
  			  JOptionPane.showMessageDialog(AddParameterDialog.this, "Error converting default value '"+defValue+"' to type '"+type+"'", "Default Value Failed", JOptionPane.ERROR_MESSAGE);
			  return false;
		  }
		  
		  String displayName = getDisplayNameField().getText();
		  if (displayName == null || displayName.trim().length() ==0) {
			  displayName = name;
		  } else {
			  displayName = displayName.trim();
		  }
		  
		  List constraints = null;
		  String list = getValuesField().getText();
		  if (list != null && list.trim().length() != 0) {
			  list = list.trim();
			  try {
		          if (strConv == null) {
		            Object[] vals = ParameterUtils.parseDefaultValue(typeCls, name, list);
		            constraints = Arrays.asList(vals);
		          } else {
		            List objs = new ArrayList();
		            Object[] vals = ParameterUtils.parseDefaultValue(String.class, name, list);
		            for (Object val : vals) {
		              objs.add(strConv.fromString(val.toString()));
		            }
		            constraints = objs;
		          }
			  } catch (Exception e) {
	  			  JOptionPane.showMessageDialog(AddParameterDialog.this, "Error converting value list to type '"+type+"'", "Value List Failed", JOptionPane.ERROR_MESSAGE);
				  return false;
			  }

		  }
		  if (constraints != null && !constraints.contains(defVal)) {
  			  JOptionPane.showMessageDialog(AddParameterDialog.this, "Default value not in the Values List", "Default Value Invalid", JOptionPane.ERROR_MESSAGE);
			  return false;  
		  }
		  params.addParameter(name,displayName,typeCls,defVal,readOnly);
		  if (strConv != null) {
			  params.addConvertor(name,strConv);
		  }
		  if (constraints != null) {
			  params.addConstraint(name, constraints);
		  }
		  return true;
	  }
	  private void initComponents() {
		  setTitle("Add Parameter");
		  setModal(true);
		  
		  Container contentPane = getContentPane();
		  contentPane.setLayout(new BorderLayout());

		  contentPane.add(getInputPanel(),BorderLayout.CENTER);
		  contentPane.add(getButtonBar(),BorderLayout.SOUTH);
	  }
	  
	  private JPanel getInputPanel() {
		  if (inputPanel == null) {
			  FormLayout layout = new FormLayout("3dlu, right:pref, 6dlu, pref:grow");
			  DefaultFormBuilder builder = new DefaultFormBuilder(layout);
			  builder.setDefaultDialogBorder();
			  builder.appendSeparator("New Parameter Details");
			  builder.setLeadingColumnOffset(1);
			  builder.nextLine();
			  builder.append("Name:",getNameField());
			  builder.nextLine();
			  builder.append("Display Name:",getDisplayNameField());
			  builder.nextLine();
			  builder.append("Type:",getTypeField());
			  builder.nextLine();
			  builder.append("Default Value:",getDefaultValueField());
			  builder.nextLine();
			  builder.append("Converter:",getConverterField());
			  builder.nextLine();
			  builder.append("Values:",getValuesField());
			  builder.nextLine();
			  builder.append(getReadOnlyCheckbox());
			  
			  inputPanel = builder.getPanel();
		  }
		  return inputPanel;
		  
	  }

	  private JTextField getNameField() {
		  if (nameField == null) {
			  nameField = new JTextField();
			  nameField.setColumns(30);
		  }
		  return nameField;
	  }
	  private JTextField getDisplayNameField() {
		  if (displayNameField == null) {
			  displayNameField = new JTextField();
			  displayNameField.setColumns(30);
		  }
		  return displayNameField;
	  }
	  private JTextField getTypeField() {
		  if (typeField == null) {
			  typeField = new JTextField();
			  typeField.setColumns(30);
		  }
		  return typeField;
	  }
	  private JTextField getDefaultValueField() {
		  if (defaultValueField == null) {
			  defaultValueField = new JTextField();
			  defaultValueField.setColumns(30);
		  }
		  return defaultValueField;
	  }
	  private JTextField getConverterField() {
		  if (converterField == null) {
			  converterField = new JTextField();
			  converterField.setColumns(30);
		  }
		  return converterField;
	  }
	  private JTextField getValuesField() {
		  if (valuesField == null) {
			  valuesField = new JTextField();
			  valuesField.setColumns(30);
		  }
		  return valuesField;
	  }
	  private JCheckBox getReadOnlyCheckbox() {
		  if (readOnlyCheckBox == null) {
			  readOnlyCheckBox = new JCheckBox("Read Only");
		  }
		  return readOnlyCheckBox;
	  }
	  
	  private JPanel getButtonBar() {
		  if (buttonBar == null) {
			  buttonBar = new JPanel();
			    okButton = new JButton();
			    cancelButton = new JButton();
		        buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
		        buttonBar.setLayout(new GridBagLayout());
		        ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 85, 80};
		        ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0, 0.0};

		        //---- okButton ----
		        okButton.setText("OK");
		        buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
		                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                new Insets(0, 0, 0, 5), 0, 0));

		        //---- cancelButton ----
		        cancelButton.setText("Cancel");
		        buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
		                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                new Insets(0, 0, 0, 0), 0, 0));

		  }
		  return buttonBar;
	  }
	  public void init(MutableParameters params) {
		  	parameterAdded = false;
		    addListeners();
		    this.params = params;
	  }
	  public boolean parameterAdded() {
		  return parameterAdded;
	  }
}
