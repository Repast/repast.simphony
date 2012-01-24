package repast.simphony.ui.sweep;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import repast.simphony.parameter.Parameters;
import repast.simphony.scenario.ScenarioUtils;

public class ParameterSweepPanel extends JPanel implements ActionListener {
	
	private HashMap<String, String> displayInternalNameMap = new HashMap<String, String>();
	private Parameters params;
	List<ParameterSweepInput> paramPanels;
	JTextField numberOfRuns;
	JDialog parentDialog;
	private final static String PROPERTIES_FILE = "sweep.properties";
	private final static String VALUES_PROPERTIES_FILE = "sweepValues.properties";
	private final static String GRIDGAIN = "GRIDGAIN_HOME";
	
	// activate once a replacement for GridGain had been found
	
	private final static boolean GRID_SUPPORT = false;
	
	boolean gridEligible = false;
	
	Properties sweepProps = new Properties();
	Properties sweepValuesProps = new Properties();
	
	String currentSelectedExecutionOption = "Local";
	
	JPanel radioButtonPanel = new JPanel();
	JPanel localExecutionPanel = new JPanel();
	JPanel gridExecutionPanel = new JPanel();
	JPanel executionPanel = new JPanel();

	
	JTextField textField1;
	JTextField textField2;
	JTextField textField3;
	JTextField textField4;
	JTextField textField5;
	
	String tf1Local = ScenarioUtils.getScenarioDir().toString();
	String tf2Local = "";
	String tf3Local = "";
	String tf4Local = "";
	String tf5Local = "";
	
	String tf1Optimized = ScenarioUtils.getScenarioDir().toString();
	String tf2Optimized = "";
	String tf3Optimized = "";
	String tf4Optimized = "";
	String tf5Optimized = "";
	
	String tf1Grid = ScenarioUtils.getScenarioDir().toString();
	String tf2Grid = ""; //  = "..\\repast.simphony.demo.predatorprey";
	String tf3Grid = ""; // = "predator_prey_batch\\batch_params.xml";
	String tf4Grid = ""; // = "..\\repast.simphony.demo.predatorprey\\predator_prey_continuous_batch.rs";
	String tf5Grid = ""; // = "predator_prey_batch\\batch_params.xml";
	
//	String tf1Grid = ScenarioUtils.getScenarioDir().toString();
//	String tf2Grid ="..\\repast.simphony.demo.predatorprey";
//	String tf3Grid ="predator_prey_batch\\batch_params.xml";
//	String tf4Grid ="..\\repast.simphony.demo.predatorprey\\predator_prey_continuous_batch.rs";
//	String tf5Grid ="predator_prey_batch\\batch_params.xml";
	
	String localEntry1Label = "Scenario Directory:";
	String localEntry2Label = "";
	String localEntry3Label = "";
	String localEntry4Label = "";
	String localEntry5Label = "";
	
	String gridEntry1Label = "Scenario Directory:";
	String gridEntry2Label = "Project Directory:";
	String gridEntry3Label = "Batch XML:";
	String gridEntry4Label = "Remote Scenario Directory:";
	String gridEntry5Label = "Remote Batch XML:";
	
	String optimizedEntry1Label = "Scenario Directory:";
	String optimizedEntry2Label = "Run Result Producer:";
	String optimizedEntry3Label = "Advancement Chooser:";
	String optimizedEntry4Label = "";
	String optimizedEntry5Label = "";
	
	boolean retainFiles = false;
	boolean zipNShip = false;
	
	String retainFilesLabel = "Retain Remote Files";
	String zipNShipLabel = "Create Jar";
	
	JRadioButton gridButton;
	JRadioButton localButton;
	JRadioButton optimizedButton;
	JCheckBox retainFilesCheckBox;
	JCheckBox zipNShipCheckBox;
	ParameterSweepSubmitPanel parameterSweepSubmitPanel;
	
	JLabel labelEntry1;
	JLabel labelEntry2;
	JLabel labelEntry3;
	JLabel labelEntry4;
	JLabel labelEntry5;

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	public ParameterSweepPanel(Parameters params, JDialog parent) {

		this.params = params;
		this.parentDialog = parent;
		
		if (System.getenv(GRIDGAIN) != null)
			gridEligible = true;
		
		loadProperties();
		initComponents();
		loadValuesProperties();
	}
	
	private void loadValuesProperties() {
		String fn = ScenarioUtils.getScenarioDir().toString() + File.separator + VALUES_PROPERTIES_FILE;
		File props = new File(fn);
		if (props.exists()) {
			try {
				sweepValuesProps.load(new FileInputStream(props));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (ParameterSweepInput psi : paramPanels) {
				numberOfRuns.setText(sweepValuesProps.getProperty("numberOfRuns", "1"));
				String parameterName = psi.getParameter();
				if (!sweepValuesProps.containsKey(parameterName+"_type"))
					createValuesProperty(psi);
				psi.setSelectedType(sweepValuesProps.getProperty(parameterName+"_type"));
				psi.getFrom().setText(sweepValuesProps.getProperty(parameterName+"_from"));
				psi.getTo().setText(sweepValuesProps.getProperty(parameterName+"_to"));
				psi.getStep().setText(sweepValuesProps.getProperty(parameterName+"_step"));
				psi.getList().setText(sweepValuesProps.getProperty(parameterName+"_list"));
				psi.getConstant().setText(sweepValuesProps.getProperty(parameterName+"_constant"));
			}
		} else {
			createValuesProperties();
		}
	}
	
	private void loadProperties() {
		String fn = ScenarioUtils.getScenarioDir().toString() + File.separator + PROPERTIES_FILE;
		File props = new File(fn);
		if (props.exists()) {
			try {
				sweepProps.load(new FileInputStream(props));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tf2Local = sweepProps.getProperty("localTF2");
			tf3Local = sweepProps.getProperty("localTF3");
			tf4Local = sweepProps.getProperty("localTF4");
			tf5Local = sweepProps.getProperty("localTF5");
			
			tf2Optimized = sweepProps.getProperty("optimizedTF2");
			tf3Optimized = sweepProps.getProperty("optimizedTF3");
			tf4Optimized = sweepProps.getProperty("optimizedTF4");
			tf5Optimized = sweepProps.getProperty("optimizedTF5");
			
			tf2Grid = sweepProps.getProperty("gridTF2");
			tf3Grid = sweepProps.getProperty("gridTF3");
			tf4Grid = sweepProps.getProperty("gridTF4");
			tf5Grid = sweepProps.getProperty("gridTF5");
		} else {
			// create an empty properties file
			createProperties();
			storeProperties();
		}
	}
	
	private void createValuesProperties() {
		for (ParameterSweepInput psi : paramPanels) {
			createValuesProperty(psi);
		}
		sweepValuesProps.setProperty("numberOfRuns", "1");
		storeValuesProperties();
	}
	
	
	private void createValuesProperty(ParameterSweepInput psi) {
		
			String parameterName = psi.getParameter();
			sweepValuesProps.setProperty(parameterName+"_type", psi.getSelectedType());
			sweepValuesProps.setProperty(parameterName+"_from", psi.getFrom().getText());
			sweepValuesProps.setProperty(parameterName+"_to", psi.getTo().getText());
			sweepValuesProps.setProperty(parameterName+"_step", psi.getStep().getText());
			sweepValuesProps.setProperty(parameterName+"_list", psi.getList().getText());
			sweepValuesProps.setProperty(parameterName+"_constant", psi.getConstant().getText());

	}
	private void createProperties() {
		sweepProps.setProperty("localTF2", "");
		sweepProps.setProperty("localTF3", "");
		sweepProps.setProperty("localTF4", "");
		sweepProps.setProperty("localTF5", "");
		
		sweepProps.setProperty("optimizedTF2", "");
		sweepProps.setProperty("optimizedTF3", "");
		sweepProps.setProperty("optimizedTF4", "");
		sweepProps.setProperty("optimizedTF5", "");
		
		sweepProps.setProperty("gridTF2", "");
		sweepProps.setProperty("gridTF3", "");
		sweepProps.setProperty("gridTF4", "");
		sweepProps.setProperty("gridTF5", "");
	}
	public void updateValuesProperties(ParameterSweepInput psi) {

		String parameterName = psi.getParameter();
		sweepValuesProps.setProperty(parameterName+"_type", psi.getSelectedType());
		sweepValuesProps.setProperty(parameterName+"_from", psi.getFrom().getText());
		sweepValuesProps.setProperty(parameterName+"_to", psi.getTo().getText());
		sweepValuesProps.setProperty(parameterName+"_step", psi.getStep().getText());
		sweepValuesProps.setProperty(parameterName+"_list", psi.getList().getText());
		sweepValuesProps.setProperty(parameterName+"_constant", psi.getConstant().getText());

	}
	
	public void updateValuesProperties() {
		sweepValuesProps = new Properties();
		sweepValuesProps.setProperty("numberOfRuns", numberOfRuns.getText());
		for (ParameterSweepInput psi : paramPanels) {
			String parameterName = psi.getParameter();
			sweepValuesProps.setProperty(parameterName+"_type", psi.getSelectedType());
			sweepValuesProps.setProperty(parameterName+"_from", psi.getFrom().getText());
			sweepValuesProps.setProperty(parameterName+"_to", psi.getTo().getText());
			sweepValuesProps.setProperty(parameterName+"_step", psi.getStep().getText());
			sweepValuesProps.setProperty(parameterName+"_list", psi.getList().getText());
			sweepValuesProps.setProperty(parameterName+"_constant", psi.getConstant().getText());
		}
	}
	private void updateProperties() {
		sweepProps.setProperty("localTF2", tf2Local);
		sweepProps.setProperty("localTF3", tf3Local);
		sweepProps.setProperty("localTF4", tf4Local);
		sweepProps.setProperty("localTF5", tf5Local);
		
		sweepProps.setProperty("optimizedTF2", tf2Optimized);
		sweepProps.setProperty("optimizedTF3", tf3Optimized);
		sweepProps.setProperty("optimizedTF4", tf4Optimized);
		sweepProps.setProperty("optimizedTF5", tf5Optimized);
		
		sweepProps.setProperty("gridTF2", tf2Grid);
		sweepProps.setProperty("gridTF3", tf3Grid);
		sweepProps.setProperty("gridTF4", tf4Grid);
		sweepProps.setProperty("gridTF5", tf5Grid);
	}
	
	public void saveValuesProperties() {
		updateValuesProperties();
		storeValuesProperties();
	}
	
	private void saveProperties() {
		saveCurrentSettings(currentSelectedExecutionOption);
		updateProperties();
		storeProperties();
	}
	
	private void storeValuesProperties() {
		try {
			String fn = ScenarioUtils.getScenarioDir().toString()+ File.separator + VALUES_PROPERTIES_FILE;
			File props = new File(fn);
			sweepValuesProps.store(new FileOutputStream(props), "Scenario Sweep Values Properties File");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void storeProperties() {
		try {
			String fn = ScenarioUtils.getScenarioDir().toString()+ File.separator + PROPERTIES_FILE;
			File props = new File(fn);
			sweepProps.store(new FileOutputStream(props), "Scenario Properties File");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initComponents() {
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1; // originall 0
		
		setLayout(new GridBagLayout());
		

		//Create and set up the window.

		JPanel all = new JPanel();
		all.setLayout(new GridBagLayout());

		JPanel numberOfRunsPanel = new JPanel();
		numberOfRunsPanel.setLayout(new GridBagLayout());
		numberOfRunsPanel.setBorder(BorderFactory.createTitledBorder("Repeat Count"));
		

		JPanel p1 = new JPanel();
//		p1.setLayout(new BorderLayout());
		p1.setLayout(new GridBagLayout());
		
		c.weighty = 0.0;
		c.gridy = 0;
		c.gridx = 0;
//		c.fill = GridBagConstraints.NONE;
//		p1.add(new JLabel("Repeat Count: "), BorderLayout.LINE_START);
		p1.add(new JLabel("Repeat Count: "), c);
		numberOfRuns = new JTextField("1", 12);
		c.gridx = 1;
//		p1.add(numberOfRuns, BorderLayout.LINE_END);
		p1.add(numberOfRuns, c);
		numberOfRunsPanel.add(p1);
		
		// radio buttons for local and grid execution
		
		c.fill = GridBagConstraints.NONE;
		
		gridButton = new JRadioButton("Grid Execution");
		gridButton.setActionCommand("Grid");
		gridButton.setSelected(false);
		gridButton.addActionListener(this);
		gridButton.setEnabled(gridEligible);
		
		localButton = new JRadioButton("Local Execution");
		localButton.setActionCommand("Local");
		localButton.setSelected(true);
		localButton.addActionListener(this);
		
		if (GRID_SUPPORT) {
		optimizedButton = new JRadioButton("Optimized Execution (Local)");
		} else {
			optimizedButton = new JRadioButton("Optimized Execution");
		}
		optimizedButton.setActionCommand("Optimized");
		optimizedButton.setSelected(false);
		optimizedButton.addActionListener(this);
		
		ButtonGroup executionButtonGroup = new ButtonGroup();
		executionButtonGroup.add(localButton);
		
		// are we currently supporting a grid execution?
		
		if (GRID_SUPPORT) {
			executionButtonGroup.add(gridButton); 
		}
		executionButtonGroup.add(optimizedButton);
		
		c.insets = new Insets(0, 0, 7, 0);
		c.weightx = 1;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		buttonPanel.add(localButton,c);
		c.gridx = 1;
		buttonPanel.add(optimizedButton,c);
		c.gridx = 2;
		if (GRID_SUPPORT) {
			buttonPanel.add(gridButton,c);
		}
		
		//  checkbox for deleting files on remote systems
		
		retainFilesCheckBox = new JCheckBox(retainFilesLabel);
		zipNShipCheckBox = new JCheckBox(zipNShipLabel);
		
		
		JPanel checkPanel = new JPanel();
		GridBagConstraints c2 = new GridBagConstraints();
		checkPanel.setLayout(new GridBagLayout());
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.weightx = 1;
		c2.weighty = 1; // originall 0
		c2.gridx = 0;
		c2.gridy = 0;
		
		checkPanel.add(retainFilesCheckBox,c2);
		c2.gridy = 1;
		checkPanel.add(zipNShipCheckBox,c2);
		

		
		c.gridx = 3;
		if (GRID_SUPPORT) {
			buttonPanel.add(checkPanel,c);
		}
		
		retainFilesCheckBox.setSelected(retainFiles);
		retainFilesCheckBox.setEnabled(false);
		

		zipNShipCheckBox.setSelected(zipNShip);
		zipNShipCheckBox.setEnabled(false);
		
		executionPanel.setLayout(new GridBagLayout());
		String note = " (NOTE: Grid Execution Requires GridGain Installation)";
		if (gridEligible || !GRID_SUPPORT)
			note = "";
		executionPanel.setBorder(BorderFactory.createTitledBorder("Model Execution"+note));
		
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		executionPanel.add(buttonPanel,c);

		gridExecutionPanel.setLayout(new GridBagLayout());
	
		c.fill = GridBagConstraints.HORIZONTAL;
		
		// required data for launch file
		
	
		labelEntry1 = new JLabel(localEntry1Label, JLabel.TRAILING);
		textField1 = new JTextField(tf1Local, 40);
		c.gridx = 0;
		c.gridy = 0;
		gridExecutionPanel.add(labelEntry1,c);
		c.gridx = 1;
		gridExecutionPanel.add(textField1,c);
		
		labelEntry2 = new JLabel(localEntry2Label, JLabel.TRAILING);
		textField2 = new JTextField(tf2Local, 40);
		c.gridx = 0;
		c.gridy = 1;
		gridExecutionPanel.add(labelEntry2,c);
		c.gridx = 1;
		gridExecutionPanel.add(textField2,c);
		
		labelEntry3 = new JLabel(localEntry3Label, JLabel.TRAILING);
		textField3 = new JTextField(tf3Local, 40);
		c.gridx = 0;
		c.gridy = 2;
		gridExecutionPanel.add(labelEntry3,c);
		c.gridx = 1;
		gridExecutionPanel.add(textField3,c);
		
		labelEntry4 = new JLabel(localEntry4Label, JLabel.TRAILING);
		textField4 = new JTextField(tf4Local, 40);
		c.gridx = 0;
		c.gridy = 3;
		if (GRID_SUPPORT) {
			gridExecutionPanel.add(labelEntry4,c);
			c.gridx = 1;
			gridExecutionPanel.add(textField4,c);
		}
		
		labelEntry5 = new JLabel(localEntry5Label, JLabel.TRAILING);
		textField5 = new JTextField(tf5Local, 40);
		c.gridx = 0;
		c.gridy = 4;
		if (GRID_SUPPORT) {
			gridExecutionPanel.add(labelEntry5,c);
			c.gridx = 1;
			gridExecutionPanel.add(textField5,c);
		}
		
		textField1.setEnabled(true);
		textField1.setEditable(true);
		textField2.setEnabled(false);
		textField3.setEnabled(false);
		textField4.setEnabled(false);
		textField5.setEnabled(false);

		c.gridy = 0;
		c.gridx = 0;
		c.ipady = 0;
	
		
		c.gridy = 1;
		executionPanel.add(gridExecutionPanel,c);
		
		// save configuration button
		c.fill = GridBagConstraints.NONE;
		JButton savePropsButton = new JButton("Save Run/Parameter/Execution Configuration");
		savePropsButton.setActionCommand("SaveProps");
		savePropsButton.addActionListener(this);
		c.gridy = 2;
		executionPanel.add(savePropsButton, c);
		
//		
		
		// uncomment for directory widget
//		gridExecutionPanel.add(p3,c);
		
		c.insets = new Insets(0, 0, 0, 0);

		ParameterSweepParameterPanel parameterPanel = new ParameterSweepParameterPanel(params);
		paramPanels = new ArrayList<ParameterSweepInput>(parameterPanel.getEntries());
		List<String> names = new ArrayList<String>();
		List<String> parameterNames = new ArrayList<String>();
		
		for (String name : params.getSchema().parameterNames()) {
			names.add(params.getDisplayName(name));
			parameterNames.add(name);
			displayInternalNameMap.put(params.getDisplayName(name), name);
		}

		JPanel aButtonPanel = new JPanel();
		aButtonPanel.setLayout(new BoxLayout(aButtonPanel, BoxLayout.PAGE_AXIS));

		aButtonPanel.setBorder(BorderFactory.createTitledBorder("Submit For Execution"));
		parameterSweepSubmitPanel = new ParameterSweepSubmitPanel(this, parentDialog);
		aButtonPanel.add(parameterSweepSubmitPanel);
		c.fill = GridBagConstraints.BOTH;
		c.gridx=0;
		c.gridy = 0;
		c.weighty = 0;
		all.add(numberOfRunsPanel,c);
		c.gridy = 1;
		c.weighty = 1;	
		c.fill = GridBagConstraints.BOTH;
		all.add(parameterPanel,c);
		c.weighty = 0;
		c.gridy = 2;
		c.fill = GridBagConstraints.BOTH;
		all.add(executionPanel,c);
		c.weighty = 0;
		c.gridy = 3;
		all.add(aButtonPanel,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		add(all, c);
		
	}
	
	public void saveCurrentSettings(String current) {
		if (current.equals("Local")) {
			tf1Local = textField1.getText();
			tf2Local = textField2.getText();
			tf3Local = textField3.getText();
			tf4Local = textField4.getText();
			tf5Local = textField5.getText();
		} else if (current.equals("Grid")) {
			tf1Grid = textField1.getText();
			tf2Grid = textField2.getText();
			tf3Grid = textField3.getText();
			tf4Grid = textField4.getText();
			tf5Grid = textField5.getText();
			retainFiles = retainFilesCheckBox.isSelected();
			zipNShip = zipNShipCheckBox.isSelected();
		} else if (current.equals("Optimized")) {
			tf1Optimized = textField1.getText();
			tf2Optimized = textField2.getText();
			tf3Optimized = textField3.getText();
			tf4Optimized = textField4.getText();
			tf5Optimized = textField5.getText();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		
		
		
//		GridBagConstraints c = new GridBagConstraints();
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.weightx = 1;
//		c.weighty = 0;
		
		saveCurrentSettings(currentSelectedExecutionOption);
		if (e.getActionCommand().equals("SaveProps")) {
			saveProperties();
			saveValuesProperties();
			return;
		}
		if (e.getActionCommand().equals("Grid")) {
			
			textField1.setEnabled(true);
			textField2.setEnabled(true);
			textField3.setEnabled(true);
			textField4.setEnabled(true);
			textField5.setEnabled(true);

			textField1.setText(tf1Grid);
			textField2.setText(tf2Grid);
			textField3.setText(tf3Grid);
			textField4.setText(tf4Grid);
			textField5.setText(tf5Grid);
			
			labelEntry1.setText(gridEntry1Label);
			labelEntry2.setText(gridEntry2Label);
			labelEntry3.setText(gridEntry3Label);
			labelEntry4.setText(gridEntry4Label);
			labelEntry5.setText(gridEntry5Label);
			
			retainFilesCheckBox.setEnabled(true);
			retainFilesCheckBox.setSelected(retainFiles);
			
			zipNShipCheckBox.setEnabled(true);
			zipNShipCheckBox.setSelected(zipNShip);
			
			parameterSweepSubmitPanel.setButtonText("Submit For Grid Execution");
			
		} else  if (e.getActionCommand().equals("Local")) {
			
			textField1.setText(tf1Local);
			textField2.setText(tf2Local);
			textField3.setText(tf3Local);
			textField4.setText(tf4Local);
			textField5.setText(tf5Local);
			
			labelEntry1.setText(localEntry1Label);
			labelEntry2.setText(localEntry2Label);
			labelEntry3.setText(localEntry3Label);
			labelEntry4.setText(localEntry4Label);
			labelEntry5.setText(localEntry5Label);
			
			textField1.setEnabled(true);
			textField2.setEnabled(false);
			textField3.setEnabled(false);
			textField4.setEnabled(false);
			textField5.setEnabled(false);
			
			retainFilesCheckBox.setSelected(false);
			retainFilesCheckBox.setEnabled(false);
			
			zipNShipCheckBox.setSelected(false);
			zipNShipCheckBox.setEnabled(false);
			

			parameterSweepSubmitPanel.setButtonText("Submit For Local Execution");
		} else if (e.getActionCommand().equals("Optimized")) {
			
			textField1.setText(tf1Optimized);
			textField2.setText(tf2Optimized);
			textField3.setText(tf3Optimized);
			textField4.setText(tf4Optimized);
			textField5.setText(tf5Optimized);
			
			labelEntry1.setText(optimizedEntry1Label);
			labelEntry2.setText(optimizedEntry2Label);
			labelEntry3.setText(optimizedEntry3Label);
			labelEntry4.setText(optimizedEntry4Label);
			labelEntry5.setText(optimizedEntry5Label);
			
			textField1.setEnabled(true);
			textField2.setEnabled(true);
			textField3.setEnabled(true);
			textField4.setEnabled(false);
			textField5.setEnabled(false);
			
			retainFilesCheckBox.setSelected(false);
			retainFilesCheckBox.setEnabled(false);
			
			zipNShipCheckBox.setSelected(false);
			zipNShipCheckBox.setEnabled(false);

			parameterSweepSubmitPanel.setButtonText("Submit For Optimized Execution");
		}
		currentSelectedExecutionOption = e.getActionCommand();
	}

	public HashMap<String, String> getDisplayInternalNameMap() {
		return displayInternalNameMap;
	}

	public void setDisplayInternalNameMap(
			HashMap<String, String> displayInternalNameMap) {
		this.displayInternalNameMap = displayInternalNameMap;
	}

	public Parameters getParams() {
		return params;
	}

	public void setParams(Parameters params) {
		this.params = params;
	}

	public List<ParameterSweepInput> getParamPanels() {
		return paramPanels;
	}

	public void setParamPanels(List<ParameterSweepInput> paramPanels) {
		this.paramPanels = paramPanels;
	}

	public JTextField getNumberOfRuns() {
		return numberOfRuns;
	}

	public void setNumberOfRuns(JTextField numberOfRuns) {
		this.numberOfRuns = numberOfRuns;
	}

	public JPanel getRadioButtonPanel() {
		return radioButtonPanel;
	}

	public void setRadioButtonPanel(JPanel radioButtonPanel) {
		this.radioButtonPanel = radioButtonPanel;
	}

	public JPanel getLocalExecutionPanel() {
		return localExecutionPanel;
	}

	public void setLocalExecutionPanel(JPanel localExecutionPanel) {
		this.localExecutionPanel = localExecutionPanel;
	}

	public JPanel getGridExecutionPanel() {
		return gridExecutionPanel;
	}

	public void setGridExecutionPanel(JPanel gridExecutionPanel) {
		this.gridExecutionPanel = gridExecutionPanel;
	}

	public JPanel getExecutionPanel() {
		return executionPanel;
	}

	public void setExecutionPanel(JPanel executionPanel) {
		this.executionPanel = executionPanel;
	}


	public JRadioButton getGridButton() {
		return gridButton;
	}

	public void setGridButton(JRadioButton gridButton) {
		this.gridButton = gridButton;
	}

	public JRadioButton getLocalButton() {
		return localButton;
	}

	public void setLocalButton(JRadioButton localButton) {
		this.localButton = localButton;
	}

	public JRadioButton getOptimizedButton() {
		return optimizedButton;
	}

	public boolean isRetainFiles() {
		return retainFiles;
	}

	public JCheckBox getRetainFilesCheckBox() {
		return retainFilesCheckBox;
	}

	public JTextField getTextField1() {
		return textField1;
	}

	public void setTextField1(JTextField textField1) {
		this.textField1 = textField1;
	}

	public JTextField getTextField2() {
		return textField2;
	}

	public void setTextField2(JTextField textField2) {
		this.textField2 = textField2;
	}

	public JTextField getTextField3() {
		return textField3;
	}

	public void setTextField3(JTextField textField3) {
		this.textField3 = textField3;
	}

	public JTextField getTextField4() {
		return textField4;
	}

	public void setTextField4(JTextField textField4) {
		this.textField4 = textField4;
	}

	public JTextField getTextField5() {
		return textField5;
	}

	public void setTextField5(JTextField textField5) {
		this.textField5 = textField5;
	}

	public String getTf1Local() {
		return tf1Local;
	}

	public void setTf1Local(String tf1Local) {
		this.tf1Local = tf1Local;
	}

	public String getTf2Local() {
		return tf2Local;
	}

	public void setTf2Local(String tf2Local) {
		this.tf2Local = tf2Local;
	}

	public String getTf3Local() {
		return tf3Local;
	}

	public void setTf3Local(String tf3Local) {
		this.tf3Local = tf3Local;
	}

	public String getTf4Local() {
		return tf4Local;
	}

	public void setTf4Local(String tf4Local) {
		this.tf4Local = tf4Local;
	}

	public String getTf5Local() {
		return tf5Local;
	}

	public void setTf5Local(String tf5Local) {
		this.tf5Local = tf5Local;
	}

	public String getTf1Optimized() {
		return tf1Optimized;
	}

	public void setTf1Optimized(String tf1Optimized) {
		this.tf1Optimized = tf1Optimized;
	}

	public String getTf2Optimized() {
		return tf2Optimized;
	}

	public void setTf2Optimized(String tf2Optimized) {
		this.tf2Optimized = tf2Optimized;
	}

	public String getTf3Optimized() {
		return tf3Optimized;
	}

	public void setTf3Optimized(String tf3Optimized) {
		this.tf3Optimized = tf3Optimized;
	}

	public String getTf4Optimized() {
		return tf4Optimized;
	}

	public void setTf4Optimized(String tf4Optimized) {
		this.tf4Optimized = tf4Optimized;
	}

	public String getTf5Optimized() {
		return tf5Optimized;
	}

	public void setTf5Optimized(String tf5Optimized) {
		this.tf5Optimized = tf5Optimized;
	}

	public String getTf1Grid() {
		return tf1Grid;
	}

	public void setTf1Grid(String tf1Grid) {
		this.tf1Grid = tf1Grid;
	}

	public String getTf2Grid() {
		return tf2Grid;
	}

	public void setTf2Grid(String tf2Grid) {
		this.tf2Grid = tf2Grid;
	}

	public String getTf3Grid() {
		return tf3Grid;
	}

	public void setTf3Grid(String tf3Grid) {
		this.tf3Grid = tf3Grid;
	}

	public String getTf4Grid() {
		return tf4Grid;
	}

	public void setTf4Grid(String tf4Grid) {
		this.tf4Grid = tf4Grid;
	}

	public String getTf5Grid() {
		return tf5Grid;
	}

	public void setTf5Grid(String tf5Grid) {
		this.tf5Grid = tf5Grid;
	}

	public void setRetainFiles(boolean retainFiles) {
		this.retainFiles = retainFiles;
	}

	public boolean isZipNShip() {
		return zipNShip;
	}

	public void setZipNShip(boolean zipNShip) {
		this.zipNShip = zipNShip;
	}
}
