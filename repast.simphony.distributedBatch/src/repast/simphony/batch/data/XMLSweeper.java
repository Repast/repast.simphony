package repast.simphony.batch.data;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.velocity.app.Velocity;
import org.xml.sax.SAXException;

import repast.simphony.batch.setup.BatchMainSetup;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.ParameterTreeSweeper;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersParser;
import repast.simphony.parameter.xml.XMLSweeperProducer;
import simphony.util.messages.MessageCenter;

/**
 * Class used to setup the parameter inputs used in distributed batch runs.
 * @version $Revision: 2.0
 * @author Mark Altaweel
 */
public class XMLSweeper {

	/**The sweeper producer used in batch*/
	private XMLSweeperProducer producer;
	
	/**The file location of a batch file*/
	private String fileLocation;
	private File fileLocationFile;
	
	/**The ParameterTreeSweeper used in batch*/
	private ParameterTreeSweeper sweep;
	
	/**The local file reference for a batch file*/
	private File localFile;
	
	/**
	 * XML setup class for parameters used in batch runs.
	 * @param fileLocation the initial file location
	 * @throws IOException 
	 */
	public XMLSweeper(String fileLocation,int count) throws IOException{
		try {
			this.fileLocation=fileLocation;
			localFile=new File(fileLocation);
			this.producer = new XMLSweeperProducer(new File(fileLocation).toURL());
			producer.getParameterSweeper().setRunCount(count);
		} catch (MalformedURLException e) {
			MessageCenter.getMessageCenter(XMLSweeper.class).error("Problems with creating XML Sweeper Producer" +
					"for batch sweeping.", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the resources to load for creating xml.
	 * @throws Exception
	 */
	public void resourceLoader() throws Exception{
		Properties props = new Properties();
	    props.put("resource.loader", "class");
	    props.put("class.resource.loader.description", "Velocity Classpath Resource Loader");
	    props.put("class.resource.loader.class",
		  "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	    Velocity.init(props);
	}
	
	/**
	 * Method to step into the parameters and step it for the next set of parameters to set for a given run.
	 * @param i the run number
	 * @return a String with parameters to set
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public String step(int i) throws IOException, ParserConfigurationException, SAXException{
		
		// ++++++++++++++++++++++++++++++++++++++
		boolean mjbSuggest = BatchMainSetup.MJB_SUGGEST;
		
		Parameters params=null;
		String stepData=null;
		if(i==0) {
			ParameterTreeSweeper sweeper = producer.getParameterSweeper();
			this.sweep=sweeper;
			params=producer.getParameters();
			this.fileLocation="./"+localFile.getName();
			this.fileLocation= getUniqueFilename().getAbsolutePath();
			// ++++++++++++++++++++++++++++++++++++++
			if (mjbSuggest)  {
				sweep.next(params);
			}
		}
		else{
			// ++++++++++++++++++++++++++++++++++++++
			if (!mjbSuggest) {
				ParametersParser p = new ParametersParser(new File(fileLocation));
				params=p.getParameters();
				sweep.next(params);
			} else {
				params=producer.getParameters();
				sweep.next(params);
			}
		}
		
		ParamWriter pw = new ParamWriter();
		// ++++++++++++++++++++++++++++++++++++++
		if (mjbSuggest) {
			fileLocationFile = getUniqueFilename();
			fileLocation = fileLocationFile.getAbsolutePath();
		}
		pw.writeValuesToFile(params, new File(fileLocation));
	  //pw.writeSpecificationToFile(params,new File(fileLocation));
		URL f = new File(fileLocation).toURL();
		File ff = new File(fileLocation);
		int size=(int)ff.length();
		stepData=this.producer.getParser().readFile(f,size);
		StringBuilder sb = new StringBuilder();
		for(int t=0; t < stepData.length(); t++){
			char s = stepData.charAt(t);
			if(s=='\r' || s=='\n' || s=='\t')
				continue;
		//	if(t==0)
		//		sb.append("'");
			sb.append(s);
		}
//		String ns=sb.toString()+"'";
		return sb.toString();
	}
	
	/**
	 * Method to check if there are more runs for sweeping.
	 * @return a true or false response based on if there are more runs.
	 * @throws IOException
	 */
	public boolean keepRunning(int i) throws IOException {
		
		// ++++++++++++++++++++++++++++++++++++++
		boolean mjbSuggest =  BatchMainSetup.MJB_SUGGEST;

	    ParameterSetter setter = producer.getParameterSweeper().getRootParameterSetter();
	    ParameterTreeSweeper sweeper = producer.getParameterSweeper();
	    
		// ++++++++++++++++++++++++++++++++++++++
		if (mjbSuggest) {
			return !sweeper.atEnd();
		} else {
		Collection<ParameterSetter> children = sweeper.getChildren(setter);
		for (ParameterSetter set : children) {
			if (!set.atEnd()) {
		        return true;
		     }
		 }
		 if(i<producer.getParameterSweeper().getRunCount())
			 return true;
		 return false;
		}
	  }
	
	
	/**
	 * Method writes the string input parameters to the parser.
	 * @param data
	 * @throws IOException
	 */
	public void writeData(String data) throws IOException{
		this.producer.parser.inputFile(data);
	}
	
	/**
	 * Example main method to test the XMLSweeper using predatorprey.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		XMLSweeper xml = new XMLSweeper("../repast.simphony.demo.predatorprey/predator_prey_batch/batch_params.xml",3);
		for(int i=0; i < 3; i++) {
			xml.resourceLoader();
			String step=xml.step(i);
			xml.writeData(step);
		}
	}
	
	// ++++++++++++++++++++++++++++++++++++++
	private File getUniqueFilename() {
		String file = null;
		File tFile = null;
		try {
			tFile = File.createTempFile("sweepDB", ".xml");
			
			file = tFile.getName();
			file = tFile.getAbsolutePath();
			
			tFile.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tFile;
	}
}
