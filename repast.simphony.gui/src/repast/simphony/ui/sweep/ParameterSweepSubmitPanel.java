package repast.simphony.ui.sweep;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import repast.simphony.batch.setup.BatchMainSetup;
import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.Schema;
import repast.simphony.parameter.StringConverter;
import repast.simphony.runtime.RepastBatchMain;
import repast.simphony.scenario.ScenarioUtils;

public class ParameterSweepSubmitPanel extends JPanel implements ActionListener {

	JDialog parentDialog;
	List<ParameterSweepInput> params;
	JTextField numberOfRuns;
	JTextField textField1;
	JTextField textField2;
	JTextField textField3;
	JTextField textField4;
	JTextField textField5;
	Parameters parameters;
	String optimizationFile;
	String sweepFile;
	String launchFile;
	File sweepFileFile;
	File launchFileFile;
	File optimizationFileFile;
	ParameterSweepPanel parameterSweepPanel;
	JButton submit;
	int runNum = 1;



	class ExecRunnable implements Runnable {

		private String command;
		private ArrayList<String> theCommand = new ArrayList<String>();
		private ConsoleWindowDialog dialog;
		ParameterSweepPanel parameterSweepPanel;
		ArrayList<String> args;
		boolean gridExecution;
		boolean separateProcess;

		private final static String CLASSPATH_FILE = "../repast.simphony.distributedBatch/DBClasspath";

		public ExecRunnable(ParameterSweepPanel parameterSweepPanel, String mainClass, 
				String command, ArrayList<String> args, String logfile, boolean gridExecution) {
			
			separateProcess = loadProcessProperties();

			this.gridExecution = gridExecution;

			this.command = command;
			this.parameterSweepPanel = parameterSweepPanel;

			theCommand.add("java");
			theCommand.add("-Xmx600M");
			theCommand.add("-cp");

			if (gridExecution)
				theCommand.add((System.getProperty("java.class.path")+File.pathSeparator+loadClasspath()+
						File.pathSeparator+getModelJar()).replaceAll("\\\\", "/"));
			else
				theCommand.add(System.getProperty("java.class.path").replaceAll("\\\\", "/"));
				
			//		      theCommand.add(File.pathSeparator+loadClasspath()+File.pathSeparator+getModelJar());


			//		      theCommand.add(System.getProperty("java.class.path")+File.pathSeparator+getModelJar()+ 
			//		    		  File.pathSeparator+".."+File.separator+"repast.simphony.distributedBatch"+File.separator+"bin");
					      

			//		      theCommand.add(System.getProperty("java.class.path")+File.pathSeparator+getModelJar());
			//		      theCommand.add(System.getProperty("java.class.path")+File.pathSeparator+
			//				".."+File.separator+"repast.simphony.distributedBatch"+File.separator+"bin");


			theCommand.add(mainClass);
			theCommand.addAll(args);

			this.args = args;

			
		}

		private String getModelJar() {
			// determine the name of the jar file
			String jar = null;

			String projectDir = parameterSweepPanel.getTf2Grid();
			File f = new File(projectDir);
			String project = f.getName();

			jar = ".."+File.separator+project+File.separator+"transferFiles"+File.separator+project+".jar";


			return jar;
		}

		public void setBatchFile(String command, ArrayList<String> args, String logfile) {
			theCommand.clear();
			theCommand.add(command);
			theCommand.addAll(args);
			theCommand.add(">"+logfile);
			theCommand.add("2>&1");
		}

		private String loadClasspath() {

			URL runtimeSource = repast.simphony.runtime.RepastMain.class
			.getProtectionDomain().getCodeSource().getLocation();

			// this path gets us to runtime. Classpath entries in file
			// will always be relative to runtime.
			String path = runtimeSource.getFile().replaceAll("%20", " ")+"../";
			path = path.replaceFirst("/", "");
			
			path = "";


			StringBuffer sb = new StringBuffer();

			try {
				BufferedReader reader = new BufferedReader(new FileReader(CLASSPATH_FILE));

				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(path);
					sb.append(line);
					sb.append(File.pathSeparator);
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return sb.toString().trim().replaceAll("\\\\", "/");

		}


		private String getPropertiesLocation() {
			URL runtimeSource = repast.simphony.runtime.RepastMain.class
			.getProtectionDomain().getCodeSource().getLocation();

			String path = runtimeSource.getFile().replaceAll("%20", " ");
			return path + "../../repast.simphony.gui/process.properties";
		}

		private boolean loadProcessProperties() {
			Properties sweepProps = new Properties();
			File props = new File(getPropertiesLocation());
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
				return Boolean.parseBoolean(sweepProps.getProperty("separateProcess"));

			}
			return false;
		}

		private boolean isSeparateProcess() {
			return separateProcess;
		}


		public void run() {


			boolean separateProcess = isSeparateProcess();

//			Map <String, String> xyz = System.getenv();
//			System.out.println("Environment");
//			for (String v : xyz.keySet()) {
//				System.out.println("<"+v+"> <"+System.getenv(v)+">");
//			}
//			System.out.println("Properties");
//			Properties props = System.getProperties();
//			Enumeration abc = props.propertyNames();
//			while (abc.hasMoreElements()) {
//				String p = (String) abc.nextElement();
//				System.out.println("<"+p+"> <"+props.getProperty(p)+">");
//			}

			if (separateProcess || !gridExecution) {

				try {
					for (String s : theCommand) {
						System.out.print(s);
						System.out.print(" ");
					}
					System.out.println("");

					//					Process p = Runtime.getRuntime().exec(command, null, new File(".."+File.separator+"repast.simphony.distributedBatch"));

					ProcessBuilder pb = new ProcessBuilder(theCommand);
					pb.redirectErrorStream(true);
					//					pb.directory(new File(".."+File.separator+"repast.simphony.demos"));
					pb.directory(new File("."));
					dialog.pack();
					dialog.setVisible(true);
					Process process = pb.start();

					dialog.log(process);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				dialog.pack();
				dialog.setVisible(true);
				dialog.setLogPrintStream();
				
//				directExec();
				
				DirectExecution de = new DirectExecution(args, gridExecution);
				
				Thread thread = new Thread(de);
				thread.start();
				
				dialog.logPrintStream();
				
				
			}
		}

//		public void directExec() {
//
//			String[] a = new String[args.size()];
//			for (int i = 0; i < a.length; i++)
//				a[i] = args.get(i);
//
//			if (gridExecution) {
//
//				BatchMainSetup bms = new BatchMainSetup();
//				try {
//					bms.execute(a);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			} else {
//				RepastBatchMain bm = new RepastBatchMain();
//				bm.run(a);
//				System.out.println("Completed");
//			}
//		}

		public ConsoleWindowDialog getDialog() {
			return dialog;
		}

		public void setDialog(ConsoleWindowDialog dialog) {
			this.dialog = dialog;
		}
	}
	
	class DirectExecution implements Runnable {
		ArrayList<String> args;
		boolean gridExecution;
		public  DirectExecution(ArrayList<String> args, boolean gridExecution) {
			this.args = args;
			this.gridExecution = gridExecution;
		}
		
		public void run() {

			String[] a = new String[args.size()];
			for (int i = 0; i < a.length; i++)
				a[i] = args.get(i);

			if (gridExecution) {

				BatchMainSetup bms = new BatchMainSetup();
				try {
					bms.execute(a);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				RepastBatchMain bm = new RepastBatchMain();
				bm.run(a);
				System.out.println("Completed");
			}
		}
	}

	public ParameterSweepSubmitPanel(ParameterSweepPanel psp, JDialog parent) {
		super();
		parentDialog = parent;
		parameterSweepPanel = psp;
		this.params = psp.getParamPanels();
		this.numberOfRuns = psp.getNumberOfRuns();
		this.textField1 = psp.getTextField1();
		this.parameters = psp.getParams();
		this.textField2 = psp.getTextField2();
		this.textField3 = psp.getTextField3();
		this.textField4 = psp.getTextField4();
		this.textField5 = psp.getTextField5();
		submit = new ParameterSweepSubmitButton();
		submit.addActionListener(this);
		add(submit);
	}

	public void setButtonText(String text) {
		submit.setText(text);
	}


	public void actionPerformed(ActionEvent e) {

		// generate the "sweep" file
		parameterSweepPanel.saveCurrentSettings(parameterSweepPanel.currentSelectedExecutionOption);
//		parameterSweepPanel.saveValuesProperties();

		Schema schema = parameters.getSchema();

		if (!validateData())
			return;

		try {
			sweepFileFile = getUniqueFilename();
			sweepFile = sweepFileFile.getAbsolutePath();
			BufferedWriter sweeper = new BufferedWriter(new FileWriter(sweepFileFile));
			launchFileFile = getUniqueFilename();
			launchFile = launchFileFile.getAbsolutePath();
			BufferedWriter launcher = new BufferedWriter(new FileWriter(launchFileFile));


			if (parameterSweepPanel.getOptimizedButton().isSelected()) {
				optimizationFileFile = getUniqueFilename();
				optimizationFile = optimizationFileFile.getAbsolutePath();
				BufferedWriter optimizer = new BufferedWriter(new FileWriter(optimizationFileFile));
				StringBuffer optimize = new StringBuffer();
				optimize.append("run_result_producer="+this.textField2.getText()+"\n");
				optimize.append("advancement_chooser="+this.textField3.getText()+"\n");
				optimize.append("parameter_file="+sweepFile+"\n");
			}

			StringBuffer sweep = new StringBuffer();
			sweep.append("<?xml version=\"1.0\"?>");
			if (parameterSweepPanel.getGridButton().isSelected())
				sweep.append("<sweep runs=\""+"1"+"\"> \n");
			else
				sweep.append("<sweep runs=\""+numberOfRuns.getText().trim()+"\"> \n");

			// get the constants first
			for (ParameterSweepInput psi : params) {

				String st = psi.getSelectedType();

				if (!psi.getSelectedType().equals(ParameterSweepInput.CONSTANT) )
					continue;
				ParameterSchema details = schema.getDetails(psi.getParameter());
				Class type = details.getType();

				String myType = type.getCanonicalName();
				
//				if (myType.equals("int") || myType.equals("java.lang.Integer") ||
//						myType.equals("float") || myType.equals("java.lang.Float") ||
//						myType.equals("long") || myType.equals("java.lang.Long") ||
//						myType.equals("short") || myType.equals("java.lang.Short") ||
//						myType.equals("byte") || myType.equals("java.lang.Byte") ||
//						myType.equals("double") || myType.equals("java.lang.Double"))
//					myType = "number";
				
				sweep.append("<parameter name=\""+psi.getParameter()+"\" type=\"constant\" constant_type=\""+
						myType+"\" value=\""+psi.getValues()[0]+"\" />\n");
			}
			int numClose = 0;
			for (ParameterSweepInput psi : params) {


				ParameterSchema details = schema.getDetails(psi.getParameter());
				Class type = details.getType();

				if (psi.getSelectedType().equals(ParameterSweepInput.CONSTANT) ||
						psi.getSelectedType().equals(ParameterSweepInput.RANDOM))
					continue;
				numClose++;
				if (psi.getSelectedType().equals(ParameterSweepInput.LIST)) {
					sweep.append("<parameter name=\""+psi.getParameter()+"\" type=\"list\" value_type=\""+
							type.getCanonicalName()+"\" values=\""+psi.getValues()[0]+"\" >\n");
				} else if (psi.getSelectedType().equals(ParameterSweepInput.NUMBER)) {
					sweep.append("<parameter name=\""+psi.getParameter()+
							"\" type=\"number\""+
							" number_type=\""+type.getCanonicalName()+
							"\"  start=\""+psi.getValues()[0]+
							"\" end=\""+psi.getValues()[1]+
							"\" step=\""+psi.getValues()[2]+
					"\" >\n");
				}

			}
			for (int i = 0; i < numClose; i++)
				sweep.append("</parameter>\n");
			sweep.append("</sweep>\n");

			// generate the launch file

			StringBuffer launch = new StringBuffer();

			launch.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			launch.append("<XMLInputData>\n");
			launch.append("<numberOfRuns>"+numberOfRuns.getText().trim()+"</numberOfRuns>\n");

			String pName=new File(textField2.getText()).getName();

			launch.append("<name>"+pName+"</name>\n");
			launch.append("<mainScenario>" + textField1.getText()+"</mainScenario>\n"); 
			launch.append("<projectPath>"+textField2.getText()+"</projectPath>\n");

			launch.append("<batchXMLPath>"+sweepFile+"</batchXMLPath>\n"); 
			launch.append("<remoteBatchXML>"+sweepFile+"</remoteBatchXML>\n"); 

			launch.append("<scenario>"+textField4.getText()+"</scenario>\n");  
			launch.append("<delayTime>1000</delayTime>\n");
			launch.append("<removeFiles>"+!parameterSweepPanel.getRetainFilesCheckBox().isSelected()+"</removeFiles>\n");
			launch.append("<createJar>"+parameterSweepPanel.isZipNShip()+"</createJar>\n");
			launch.append("</XMLInputData>\n");

			sweeper.append(sweep.toString());
			sweeper.close();

			launcher.append(launch.toString());
			launcher.close();

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			submit();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private File getUniqueFilename() {
		String file = null;
		File tFile = null;

		try {
			tFile = File.createTempFile("sweepGUI", ".xml");

			file = tFile.getName();
			file = tFile.getAbsolutePath();

			System.out.println("TempFile: <"+file+">");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tFile;
	}

	private void submit() throws Exception{

		String command;
		String logFile;
		StringBuffer sbArgs = new StringBuffer();
		boolean grid;
		ArrayList<String> args = new ArrayList<String>();

		if (parameterSweepPanel.getGridButton().isSelected()) {
			grid = true;
			args.add("-params");
			args.add(sweepFile);
			//			args.add("-launch");
			args.add(launchFile); 
		} else if (parameterSweepPanel.getLocalButton().isSelected()) {
			grid = false;
			args.add("-params");
			args.add(sweepFile);
			args.add(parameterSweepPanel.getTextField1().getText()); 
		} else  {
			grid = false;
			args.add("-opt");
			args.add(optimizationFile);
			args.add(parameterSweepPanel.getTextField1().getText()); 
		}

		command = "..\\repast.simphony.distributedBatch\\run"+(grid ? "Grid" : "Local")+".bat";
		logFile = " ..\\repast.simphony.distributedBatch\\run"+(grid ? "Grid" : "Local")+(runNum++)+".log";

		for (String a : args) {
			sbArgs.append(" \"");
			sbArgs.append(a);
			sbArgs.append("\"");
		}


		String execString = command;


		//		ExecRunnable er = new ExecRunnable(execString, sbArgs.toString().replaceAll("\\\\", "\\\\\\\\"), logFile);
		//		ExecRunnable er = new ExecRunnable(parameterSweepPanel, (grid ? "repast.simphony.batch.setup.BatchMainSetup" : 
		//			"repast.simphony.batch.BatchMain"),execString, args, logFile);
		ExecRunnable er = new ExecRunnable(parameterSweepPanel, (grid ? "repast.simphony.batch.setup.BatchMainSetup" : 
		"repast.simphony.runtime.RepastBatchMain"),execString, args, logFile, grid);

		//		er.setBatchFile(execString, args, logFile);

		ConsoleWindowDialog cwd = new ConsoleWindowDialog(parentDialog, "Console Log For Run Submission "+(runNum-1));
		er.setDialog(cwd);
		Thread thread = new Thread(er);
		thread.start();

	}

	public JButton getSubmit() {
		return submit;
	}

	public void setSubmit(JButton submit) {
		this.submit = submit;
	}

	public boolean validateData() {

		//		this.params = psp.getParamPanels();
		//		this.numberOfRuns = psp.getNumberOfRuns();
		//		this.textField1 = psp.getTextField1();
		//		this.parameters = psp.getParams();
		//		this.textField2 = psp.getTextField2();
		//		this.textField3 = psp.getTextField3();
		//		this.textField4 = psp.getTextField4();
		//		this.textField5 = psp.getTextField5();
		//
		boolean requiresConverter = false;
		boolean canRun = true;
		String numOfRuns = numberOfRuns.getText();

		if (numOfRuns == null || numOfRuns.trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "An integer value > 0 must be specified", "Missing Repeat Count Field", JOptionPane.ERROR_MESSAGE);
			return false;

		}


		requiresConverter = true;

		String converter = "repast.simphony.parameter.StringConverterFactory$IntConverter";

		StringConverter strConv = null;
		if (converter != null && converter.trim().length() >0) {
			converter = converter.trim();
			try {
				Class cls = Class.forName(converter);
				strConv = (StringConverter)cls.newInstance();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Converter, '"+converter+"' could not be instantiated.", "Converter Creation Failed", JOptionPane.ERROR_MESSAGE);
				return false;
			}

		}

		String type = "int";
		Object defVal;
		try {
			defVal = strConv.fromString(numOfRuns);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Incorrect Repeat Count value <"+numOfRuns+">", "Repeat Count Failed", JOptionPane.ERROR_MESSAGE);
			canRun = false;
		}


		for (ParameterSweepInput psi : params) {
			ParameterSchema details = parameters.getSchema().getDetails(psi.getParameter());
			Object o = null;
			if (psi.getSelectedType().equals(ParameterSweepInput.RANDOM)) {
				
			} else if (psi.getSelectedType().equals(ParameterSweepInput.CONSTANT)){

				try {
					o = details.getConverter().fromString(psi.getValues()[0]);
				} catch (Exception e) {
					// TODO Auto-generated catch block

					JOptionPane.showMessageDialog(this, "Bad Value for "+psi.getDisplayName()+"<"+psi.getValues()[0]+">", 
							"Incorrect Parameter Value", JOptionPane.ERROR_MESSAGE);
					canRun = false;
				}
			} else if (psi.getSelectedType().equals(ParameterSweepInput.LIST)) {
				String[] lVals = psi.getValues()[0].split(" ");
				for (String v : lVals) {
					try {
						o = details.getConverter().fromString(v);
					} catch (Exception e) {
						// TODO Auto-generated catch block

						JOptionPane.showMessageDialog(this, "Bad Value for "+psi.getDisplayName()+"<"+v+">", 
								"Incorrect Parameter Value", JOptionPane.ERROR_MESSAGE);
						canRun = false;
					}
				}
			} else if (psi.getSelectedType().equals(ParameterSweepInput.NUMBER)) {
				String[] steps = {"From", "To", "Step"};
				for (int i = 0; i < 3; i++) {
					try {
						o = details.getConverter().fromString(psi.getValues()[i]);
					} catch (Exception e) {
						// TODO Auto-generated catch block

						JOptionPane.showMessageDialog(this, "Invalid "+steps[i]+" Value for "+psi.getDisplayName()+"<"+psi.getValues()[i]+">", 
								"Incorrect Parameter Value", JOptionPane.ERROR_MESSAGE);
						canRun = false;
					}
				}
			}
		}

		// execution fields
		boolean missingText = false;

		if (this.textField1 == null || this.textField1.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "Scenario Directory null", 
					"Missing Execution Text", JOptionPane.ERROR_MESSAGE);
			canRun = false;
		}	
		if (parameterSweepPanel.getGridButton().isSelected()) {
			if (this.textField2 == null || this.textField2.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "Project Directory null", 
						"Missing Execution Text", JOptionPane.ERROR_MESSAGE);
				canRun = false;
			}	
			if (this.textField3 == null || this.textField3.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "Batch XML File null", 
						"Missing Execution Text", JOptionPane.ERROR_MESSAGE);
				canRun = false;
			}	
			if (this.textField4 == null || this.textField4.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "Remote Scenario Directory null", 
						"Missing Execution Text", JOptionPane.ERROR_MESSAGE);
				canRun = false;
			}	
			if (this.textField5 == null || this.textField5.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "Remote Batch XML File null", 
						"Missing Execution Text", JOptionPane.ERROR_MESSAGE);
				canRun = false;
			}	
		} else if (parameterSweepPanel.getOptimizedButton().isSelected()) {
			if (this.textField2 == null || this.textField2.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "Run Result Producer null", 
						"Missing Execution Text", JOptionPane.ERROR_MESSAGE);
				canRun = false;
			}	
			if (this.textField3 == null || this.textField3.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "Advancement Chooser null", 
						"Missing Execution Text", JOptionPane.ERROR_MESSAGE);
				canRun = false;
			}	
		} 


		return canRun;

	}

}
