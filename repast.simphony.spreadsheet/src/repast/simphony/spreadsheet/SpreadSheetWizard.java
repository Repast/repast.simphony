package repast.simphony.spreadsheet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import repast.simphony.data.analysis.AnalysisPluginWizard;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.FileDataSink;
import repast.simphony.data2.FormatType;

/**
 * A wizard for executing a spreadsheet app on a file outputter's output.
 * 
 * @author Eric Tatara
 * 
 */
public class SpreadSheetWizard extends AnalysisPluginWizard {

	public SpreadSheetWizard(){
		
	}
	
	public SpreadSheetWizard(DataSetRegistry loggingRegistry, boolean showCopyright,
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
		String[] command;
		StringBuilder logFileBuilder = new StringBuilder();

		List<FileDataSink> outputters = fileStep.getChosenOutputters();
		for (int i = 0; i < outputters.size(); i++) {
			if (i != 0){
				logFileBuilder.append(" ");
			}

			String filename = outputters.get(i).getFile().getAbsolutePath();

			if (!filename.endsWith(".csv")){
				try {
					filename = createCSVFile(filename);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			logFileBuilder.append(prepFileNameFor(filename));

			if (outputters.get(i).getFormat() != FormatType.TABULAR) {
				LOG
				.warn("When invoking a spreadsheet, an outputter without a delimited formatter was found. " +
				"spreadsheet can only be invoked on output files with using a delimiter.");
				break;
			}
		}

		if (SystemUtils.IS_OS_MAC){
			command = new String[4];
			command[0] = "open";
			command[1] = "-a";
			command[2] = getExecutableLoc();
			command[3] = logFileBuilder.toString();
		}
		else if (SystemUtils.IS_OS_WINDOWS){
			command = new String[2];
			command[0] = getExecutableLoc();
			command[1] = logFileBuilder.toString();
		}
		else {
			command = new String[1];
			command[0] = getExecutableLoc() + " " + logFileBuilder.toString();
		}
		
		return command;
	}

	public String getExecutableLoc() {
		return getInstallHome();
	}

	@Override
	public String getCannotRunMessage() {
		// TODO Auto-generated method stub
		return null;
	}
}