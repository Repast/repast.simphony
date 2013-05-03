package repast.simphony.iReport;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import repast.simphony.data.analysis.AnalysisPluginWizard;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.FileDataSink;
import repast.simphony.data2.FormatType;
import repast.simphony.data2.Formatter;
import repast.simphony.data2.TabularFormatter;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * A wizard for executing iReport on a file outputter's output.
 * 
 * @author Eric Tatara
 * 
 */

public class IReportWizard extends AnalysisPluginWizard {

	private String csvFileName;
	private String csvFiledelimiter;
	private String connectionName;
	private List<String>columnNames;

	public IReportWizard(){

	}

	public IReportWizard(DataSetRegistry loggingRegistry, boolean showCopyright,
			boolean browseForRHome, String name, String installHome, String defaultLocation, 
			String licenseFileName) {

		super(loggingRegistry, showCopyright, browseForRHome, name, installHome, 
				defaultLocation, licenseFileName);
	}
	
	@Override
	public String[] getExecutionCommand() {
		List<String> commands = new ArrayList<String>();

		List<FileDataSink> outputters = fileStep.getChosenOutputters();
		for (int i = 0; i < outputters.size(); i++) {

			csvFileName = prepFileNameFor(outputters.get(i).getFile().getAbsolutePath());

			Formatter formatter = outputters.get(i).getFormatter();
			if (outputters.get(i).getFormat() != FormatType.TABULAR) {
				LOG.warn("When invoking iReort, an outputter without a delimited " +
						"formatter was found. iReport can only be invoked on output files " +
						"with using a delimiter.");
				break;
			}
			csvFiledelimiter = formatter.getDelimiter();

			connectionName = outputters.get(i).getName();
			columnNames = ((TabularFormatter)outputters.get(i).getFormatter()).getColumnNames();
		}

		// TODO The new iReport Designer no longer supports command line arguments 
		//      to set the connection configuration file, so just launch the app.
		//      Users will have to import data set connection files created here.
		//      It might be good in the future to have the Repast iReport wizard
		//      GUI provide two buttons - one to open the iReport designer editor,
		//      and a second to actually generate the report (PDF, HTML, etc.)
		//      programmatically since the jasperreports plugin contains the engine
		//      to actually compile the report.
		//
		//      You can pass the .jrxml report template to iReport Designer via the
		//      command line, but the data source connection must be imported
		//      in the iReport Designer GUI manually.
		
		commands.add(getExecutableLoc());
//		commands.add("-config-file");
//		commands.add(createConfigFile());

		return commands.toArray(new String[commands.size()]);
	}


	private String getExecutableLoc() {
		return getInstallHome();
	}

	/**
	 * Create the iReport Designer data connection configuration file.
	 * @return
	 */
	private String createConfigFile(){
		String filename = csvFileName + ".iReport.xml";

		Element e;
		CDATASection cdata;
		Document doc= new DocumentImpl();

		Element root = doc.createElement("iReportConnectionSet");
		doc.appendChild(root);

		// Define the connection element
		Element con = doc.createElement("iReportConnection");
		con.setAttribute("name", connectionName);
		con.setAttribute("connectionClass", "com.jaspersoft.ireport.designer.connection.JRCSVDataSourceConnection");
		root.appendChild(con);

		// Create the column entries
		int i = 0;
		for (String columnName : columnNames){
			e = doc.createElement("connectionParameter");
			e.setAttribute("name", "COLUMN_"+i);
			cdata = doc.createCDATASection(columnName);
			e.appendChild(cdata);
			con.appendChild(e);
			i++;
		}

		// specify field delimiter
		e = doc.createElement("connectionParameter");
		e.setAttribute("name", "fieldDelimiter");
		cdata = doc.createCDATASection(csvFiledelimiter);
		e.appendChild(cdata);
		con.appendChild(e);

		// Indicate use first row as column headers
		e = doc.createElement("connectionParameter");
		e.setAttribute("name", "useFirstRowAsHeader");
		cdata = doc.createCDATASection("true");
		e.appendChild(cdata);
		con.appendChild(e);

		// location of csv output file
		e = doc.createElement("connectionParameter");
		e.setAttribute("name", "Filename");
		cdata = doc.createCDATASection(csvFileName);
		e.appendChild(cdata);
		con.appendChild(e);

		// record delimiter (newline)
		e = doc.createElement("connectionParameter");
		e.setAttribute("name", "recordDelimiter");
		// Important that the delimiter string is actually "\n" and not a newline
		cdata = doc.createCDATASection("\\n");
		e.appendChild(cdata);
		con.appendChild(e);


		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filename);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		OutputFormat of = new OutputFormat("XML","UTF-8",true);

//		String docType = "-//iReport/DTD iReport Configuration//EN";
//		String schema = "http://ireport.sourceforge.net/dtds/iReportProperties.dtd";
//		of.setDoctype(docType,schema);

		XMLSerializer serializer = new XMLSerializer(fos,of);

		try {
			serializer.asDOMSerializer();
			serializer.serialize(doc.getDocumentElement());
			fos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 

		return filename;
	}

	@Override
	public String getCannotRunMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}

