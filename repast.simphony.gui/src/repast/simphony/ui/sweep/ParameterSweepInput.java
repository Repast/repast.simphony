package repast.simphony.ui.sweep;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.Schema;

public class ParameterSweepInput extends JPanel implements ItemListener {
	
    private JPanel parameterSweepPanel; //a panel that uses CardLayout
    private JComboBox parameterTypeComboBox;
    private JLabel parameterName;
    
    public final static String NUMBER = "Number";
    
    public final static String LIST = "List";
    public final static String CONSTANT = "Constant";
    
    public final static String RANDOM = "Random";
    
    private JTextField from = new JTextField("", 8);
    private JTextField to = new JTextField("", 8);
    private JTextField step = new JTextField("", 8);
    
    private JTextField list = new JTextField("", 24);
    private JTextField constant = new JTextField("", 24);
    
   
    private String parameter;
    private String displayName;
    
    private Parameters params;
    
    public ParameterSweepInput(String parameter, Parameters params) {
    	super();
    	this.parameter = parameter;
    	this.params = params;
    	this.displayName = params.getDisplayName(parameter);
    	addComponentToPane();
    }
    
    public String getSelectedType() {

    	return (String) parameterTypeComboBox.getSelectedItem();
    }
    
    public void setSelectedType(String type) {
    	parameterTypeComboBox.setSelectedItem(type);
    	itemStateChanged(type);
    }
    
    public String[] getValues() {
    	
    	String[] values;
    	
    	if (getSelectedType().equals(NUMBER)) {
    		values = new String[3];
    		values[0] = from.getText();
    		values[1] = to.getText();
    		values[2] = step.getText();
    	} else if (getSelectedType().equals(LIST)) {
       		values = new String[1];
    		values[0] = list.getText();
    	} else if (getSelectedType().equals(CONSTANT)) {
       		values = new String[1];
    		values[0] = constant.getText();
    	} else {
    		return null;
    	}
    	
    	return values;
    	
    }
    
    public void setConstantValue(String value) {
    	
    	constant.setText(value);
//    	if (parameter.equals("randomSeed")) 
//    		parameterTypeComboBox.setSelectedItem(RANDOM);
//	   else 
    	parameterTypeComboBox.setSelectedItem(CONSTANT);
    	
    	// if the other fields are blank, set the initial value to this value
    	
    	if (from.getText() != null && from.getText().length() == 0)
    		from.setText(value);
    	
    	if (list.getText() != null && list.getText().length() == 0)
    		list.setText(value);
    	
    	
    }
    

    public void addComponentToPane() {
    	
    	this.setLayout(new GridBagLayout());
    	
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
//    	setBorder(BorderFactory.createTitledBorder(""));
        //Put the JComboBox in a JPanel
        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        if (parameter.equals("randomSeed")) {
//        	String comboBoxItems[] = { NUMBER , LIST, CONSTANT, RANDOM};
        	parameterTypeComboBox = new JComboBox(new String[] {NUMBER , LIST, CONSTANT, RANDOM});
        	
        // need to differentiate between text and numbers!
        } else {
        	Schema schema = params.getSchema();
        	ParameterSchema details = schema.getDetails(parameter);
			Class type = details.getType();

			String myType = type.getCanonicalName();
			
//			System.out.println("parameter = "+parameter+" myType = "+myType);
//        	String comboBoxItems[] = { NUMBER , LIST, CONSTANT};
			if (myType.equals("String") || myType.equals("boolean") ||
					myType.equals("java.lang.String") || myType.equals("java.lang.Boolean")) {        	
				parameterTypeComboBox = new JComboBox(new String[] {LIST, CONSTANT});
			} else {
				parameterTypeComboBox = new JComboBox(new String[] {NUMBER , LIST, CONSTANT});
			}
        	
        }
//        parameterTypeComboBox = new JComboBox(comboBoxItems);
        parameterTypeComboBox.setEditable(false);
        parameterTypeComboBox.addItemListener(this);
        
        parameterName = new JLabel(displayName+":");
        parameterName.setHorizontalAlignment(JLabel.RIGHT);
//        comboBoxPane.add(parameterName);
        comboBoxPane.add(parameterTypeComboBox);
        
        //Create the "cards".

        JPanel selection1 = new JPanel();
        JPanel selection1Sub1 = new JPanel();
        JPanel selection1Sub2 = new JPanel();
        JPanel selection1Sub3 = new JPanel();
        
        selection1Sub1.add(new JLabel("From:"));
        selection1Sub2.add(new JLabel("To:"));
        selection1Sub3.add(new JLabel("Step:"));
        
        selection1Sub1.add(from);
        selection1Sub2.add(to);
        selection1Sub3.add(step);
        
        selection1.add(selection1Sub1);
        selection1.add(selection1Sub2);
        selection1.add(selection1Sub3);
        
        JPanel selection2 = new JPanel();
        JPanel selection2Sub = new JPanel();
        selection2Sub.add(new JLabel("Blank Separated:"));
        selection2Sub.add(list);
        selection2.add(selection2Sub);
        
        JPanel selection3 = new JPanel();
        JPanel selection3Sub = new JPanel();
        selection3Sub.add(new JLabel("Constant Value:"));
        selection3Sub.add(constant);
        selection3.add(selection3Sub);
        
        JPanel selection4 = new JPanel();
        
        //Create the panel that contains the "cards".
        parameterSweepPanel = new JPanel(new CardLayout());
        parameterSweepPanel.add(selection1, NUMBER);
        parameterSweepPanel.add(selection2, LIST);
        parameterSweepPanel.add(selection3, CONSTANT);
        if (parameter.equals("randomSeed")) {
        parameterSweepPanel.add(selection4, RANDOM);
        }
        
        c.gridx = 0;
        c.gridy = 0;
        add(parameterName, c);
        c.gridx = 1;
        add(comboBoxPane, c);
        c.gridx = 2;
        add(parameterSweepPanel,c );
        
    }
    
    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout)(parameterSweepPanel.getLayout());
        cl.show(parameterSweepPanel, (String)evt.getItem());
    }
    
    public void itemStateChanged(String evt) {
        CardLayout cl = (CardLayout)(parameterSweepPanel.getLayout());
        cl.show(parameterSweepPanel, evt);
    }

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Parameters getParams() {
		return params;
	}

	public JPanel getParameterSweepPanel() {
		return parameterSweepPanel;
	}

	public JComboBox getParameterTypeComboBox() {
		return parameterTypeComboBox;
	}

	public JLabel getParameterName() {
		return parameterName;
	}

	public JTextField getFrom() {
		return from;
	}

	public JTextField getTo() {
		return to;
	}

	public JTextField getStep() {
		return step;
	}

	public JTextField getList() {
		return list;
	}

	public JTextField getConstant() {
		return constant;
	}
    

}
