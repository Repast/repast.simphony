package repast.simphony.weka;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.data.analysis.AnalysisPluginWizard;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.FileDataSink;
import repast.simphony.data2.FormatType;

/**
 * A wizard for executing Weka on a file outputter's output.
 * 
 * @author Eric Tatara
 * 
 */
public class WekaWizard extends AnalysisPluginWizard {

	public WekaWizard(){
		
	}
	
	public WekaWizard(DataSetRegistry loggingRegistry, boolean showCopyright,
			boolean browseForRHome, String name, String installHome, String defaultLocation,
			String licenseFileName){
		
		super(loggingRegistry, showCopyright, browseForRHome, name, installHome, 
				defaultLocation, licenseFileName);
	}

	private String createCSVFile(String fileName) throws FileNotFoundException,IOException{

		// Just copy the file and rename with .csv extension 

		String csvFile = fileName + ".csv";

		BufferedReader buf  = new BufferedReader(new FileReader(fileName));

		BufferedWriter out = new BufferedWriter(new FileWriter(csvFile));

		String line;
		while((line = buf.readLine()) != null)     // copy data into new file			
			out.write(line+"\n");

		buf.close();
		out.close();

		return csvFile;
	}
	
	@Override
	public String[] getExecutionCommand() {
		/*
		 * The Weka command line strucure consists of the java executable in addition
		 * to the classpath of the weka jar and the spreadsheet class along with
		 * the data file to load, for example:
		 * 
		 * java -cp C:/Program Files/weka/weka.jar weka.gui.explorer.Explorer datafile.csv;
		 */

    List<String> commands = new ArrayList<String>();

    commands.add("java");
    commands.add("-Xmx512M");
    commands.add("-cp");
    commands.add(getExecutableLoc());
    commands.add("weka.gui.explorer.Explorer");

		List<FileDataSink> outputters = fileStep.getChosenOutputters();
		for (int i = 0; i < outputters.size(); i++) {
			String filename = outputters.get(i).getFile().getAbsolutePath();

			if (!filename.endsWith(".csv")){
				try {
					filename = createCSVFile(filename);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

//				JOptionPane.showMessageDialog(null, "Weka requires a .csv filname extension.\nThe file " +
//						outputters.get(i).getFileName() + " has been copied\nto " + filename + " for your " +
//								"convienience.");
			}

			commands.add(prepFileNameFor(filename));

			if (outputters.get(i).getFormat() != FormatType.TABULAR) {
				LOG
				.warn("When invoking Weka, an outputter without a delimited formatter was found. " +
				"Weka can only be invoked on output files with using a delimiter.");
				break;
			}
		}
		
		return commands.toArray(new String[commands.size()]);
	}

	public String getExecutableLoc() {
//    String home = getInstallHome();
//    if (!home.endsWith(File.separator)) home += File.separator;
//    return home + "weka.jar";
		
		return getInstallHome();
	}

	@Override
	public String getCannotRunMessage() {
		// TODO Auto-generated method stub
		return null;
	}
}

