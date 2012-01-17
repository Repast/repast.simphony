package repast.simphony.visad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import repast.simphony.data.analysis.AnalysisPluginWizard;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.FileDataSink;
import repast.simphony.data2.FormatType;

/**
 * A wizard for executing VisAD on a file outputter's output.
 * 
 * @author Eric Tatara
 * 
 */
public class VisadWizard extends AnalysisPluginWizard {

  public VisadWizard(){
		
	}
	
	public VisadWizard(DataSetRegistry loggingRegistry, boolean showCopyright,
			boolean browseForRHome, String name, String installHome, String defaultLocation, 
			String licenseFileName) {
		
		super(loggingRegistry, showCopyright, browseForRHome, name, installHome, 
				defaultLocation, licenseFileName);
	}

	private String createVisadFile(String fileName) throws FileNotFoundException,IOException{
		/*
		 * This file passed to VisAD need to have a specific header map added.  The file from
		 * the Repast outputter is copied to a new file with a ".visad" extension.
		 * 
		 * NOTE!!  visad column headers CANNOT contain spaces!  We replace spaces in
		 *         column headers from repast data sets with an underscore
		 * 
		 * The headers map is created as follows:
		 * 
		 *  (CN[0]->(CN[1],CN[2],...CN[n]))
		 *  CN[0],CN[1],CN[2],...CN[n]
		 *  
		 *  where CH[i] is the ith column name in the Repast output file
		 */
		
		ArrayList<String> columnNames = new ArrayList<String>();

		String visadFile = fileName + ".visad";
		
		BufferedReader buf  = new BufferedReader(new FileReader(fileName));

		System.out.println("Created: " + visadFile);
		
		// Grab the column names from the first line and remove string quotes and
		// replace spaces with underscore
		String firstLine = buf.readLine();
		StringTokenizer st = new StringTokenizer(firstLine,","); 

		while (st.hasMoreTokens())
		  columnNames.add(trimQuotes(st.nextToken()).replace(' ', '_'));
		
		StringBuilder headerBuilder = new StringBuilder();
		StringBuilder columnNameBuilder = new StringBuilder();

		headerBuilder.append("(").append(columnNames.get(0)).append("->(");
		columnNameBuilder.append(columnNames.get(0)).append(",");
		
		for (int i=1; i < columnNames.size(); i++){
			headerBuilder.append(columnNames.get(i));
			columnNameBuilder.append(columnNames.get(i));
			if (i < columnNames.size()-1){
				headerBuilder.append(",");
				columnNameBuilder.append(",");
			}
		}
		headerBuilder.append("))");

		BufferedWriter out = new BufferedWriter(new FileWriter(visadFile));
		out.write(headerBuilder.toString()+"\n");  // write visad header map
		out.write(columnNameBuilder.toString()+"\n"); // write visad column name header

		String line;
		while((line = buf.readLine()) != null)     // copy data into new file			
			out.write(line+"\n");

		buf.close();
		out.close();
		
		return visadFile;
	}

	@Override
	public String[] getExecutionCommand() {
		/*
		 * The VisAD command line structure consists of the java executable in addition
		 * to the classpath of the visad jar and the spreadsheet class along with
		 * the data file to load, for example:
		 * 
		 * java -cp C:/Program Files/visad/visad.jar visad.ss.SpreadSheet -file datafile.txt;
		 */

		System.getProperty("java.Home");
		
	  List<String> commands = new ArrayList<String>();
    commands.add("java");
    commands.add("-Xmx400M");
    commands.add("-cp");
    commands.add(getExecutableLoc());
    commands.add("visad.ss.SpreadSheet");
    commands.add("-file");

		List<FileDataSink> outputters = fileStep.getChosenOutputters();
		for (int i = 0; i < outputters.size(); i++) {
			String filename = "";
			try {
				filename = createVisadFile(outputters.get(i).getFile().getAbsolutePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			commands.add(filename);

			if (outputters.get(i).getFormat() != FormatType.TABULAR) {
				LOG
				.warn("When invoking VisAD, an outputter without a delimited formatter was found. " +
				"VisAD can only be invoked on output files with using a delimiter.");
				JOptionPane.showMessageDialog(null,
						"When invoking VisAD, an outputter without a delimited formatter was found. " +
						"VisAD can only be invoked on output files with using a delimiter.");
				break;
			}
		}

    return commands.toArray(new String[commands.size()]);
	}

	private String getExecutableLoc() {
    String home = getInstallHome();
    if (!home.endsWith(File.separator)) home += File.separator;
    return home + "visad.jar";
	}

	public static String trimQuotes(String value){
    if (value == null)
      return value;

    value = value.trim();
    if (value.startsWith("\"") && value.endsWith("\""))
      return value.substring(1, value.length() - 1);
   
    return value;
  }
}