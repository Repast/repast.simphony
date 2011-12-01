package repast.simphony.batch.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import simphony.util.messages.MessageCenter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * File that converts XML input into a Java Object with xml data
 * @version $Revision: 2.0
 * @author Mark Altaweel
 */
public class XMLInputData {
	
	/**Number of batch runs*/
	private int numberOfRuns;
	
	/**Name of the batch run*/
	private String name;
	
	/**The path to the local rs folder*/
	private String mainScenario;
	
	/**The path to the local project*/
	private String projectPath;
	
	/**The params setting for batch*/
	private String params;
	
	/**The path to the local batch file that stems from the project path*/
	private String batchXMLPath;
	
	/**Path to the remote scenario folder (i.e., the r.s. folder)*/
	private String scenario;
	
	/**Path to the remote batch xml file*/
	private String remoteBatchXML;
	
	/**Length of the delay time in milliseconds*/
	private int delayTime;
	
	/**Boolean to remove or not remove output files on remote nodes*/
	private boolean removeFiles;
	
	/**Boolean flag to create user jar at the start of each set of simulation runs*/
	private boolean createJar;
	
	/**Nodes to use in the distributed job. 
	 * This is not needed for input, but is an option for manual file transfer*/
	public List<String[]> nodes;
	
	/*
	 * Default constructor
	 */
	public XMLInputData(){
		
	}
	
	
	
	public boolean isRemoveFiles() {
		return removeFiles;
	}



	public void setRemoveFiles(boolean removeFiles) {
		this.removeFiles = removeFiles;
	}

    

	public boolean isCreateJar() {
		return createJar;
	}



	public void setCreateJar(boolean createJar) {
		this.createJar = createJar;
	}



	public int getDelayTime() {
		return delayTime;
	}


	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public List<String[]> getNodes() {
		return nodes;
	}

	public void setNodes(List<String[]> nodes) {
		this.nodes = nodes;
	}

	public String getRemoteBatchXML() {
		return remoteBatchXML;
	}

	public void setRemoteBatchXML(String remoteBatchXML) {
		this.remoteBatchXML = remoteBatchXML;
	}
	
	public String getMainScenario() {
		return mainScenario;
	}



	public void setMainScenario(String mainScenario) {
		this.mainScenario = mainScenario;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParams() {
		return params;
	}

	public int getNumberOfRuns() {
		return numberOfRuns;
	}

	public void setNumberOfRuns(int numberOfRuns) {
		this.numberOfRuns = numberOfRuns;
	}

	public String getBatchXMLPath() {
		return batchXMLPath;
	}

	public void setBatchXMLPath(String batchXMLPath) {
		this.batchXMLPath = batchXMLPath;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}
	
	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	/**
	 * Method to create a jar for the given project
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void createJar() throws IOException, InterruptedException{
		CreateUserJar cj = new CreateUserJar();
		cj.createJar(projectPath);
	}
	
	/**
	 * Method to read the input data from an xml file.
	 * @param file the xml file
	 * @return an XMLInputData object that is parameterized based on the xml file.
	 */
	public XMLInputData setupXMLMapFileData(String file){
		XStream xstream = new XStream(new DomDriver());
		xstream.setClassLoader(XMLInputData.class.getClassLoader());
		xstream.alias("XMLInputData",XMLInputData.class);
		xstream.aliasType("XMLInputData", XMLInputData.class);
		XMLInputData xml=null;
		try {
			xml=(XMLInputData)xstream.fromXML(new FileReader(file));
			
		} catch (Exception e) {
			MessageCenter.getMessageCenter(XMLInputData.class).error("Problems with reading" +
					"input XML file for Scenario", e);
			e.printStackTrace();	

		}
		return xml;
	}

	/**
	 * The output writing method used to create the xml.
	 * @param xml the xml to write
	 * @param filename the file name
	 * @return a File written
	 */
	public File output(String xml,String filename){
		FileWriter fw= null;
		PrintWriter pw= null;
		
	    File file=null;
		try
		{
			file = new File(filename);
			fw= new FileWriter( file );
			pw=  new PrintWriter( fw );
			char[]seq=new char[]{'"','1','.','0','"'};
			String seqStr="";
			for(int i=0; i < seq.length; i++){
				seqStr+=seq[i];
			}
			
			char[]seqUTF=new char[]{'"','U','T','F','-','8','"'};
			String seqUTFStr="";
			for(int i=0; i < seqUTF.length; i++){
				seqUTFStr+=seqUTF[i];
			}
			pw.println("<?xml version="+seqStr+" encoding="+seqUTFStr+"?>");
			pw.print(xml);
			pw.close();
		}
		catch(IOException e){
			MessageCenter.getMessageCenter(XMLInputData.class).error("Problems with creating" +
					"XML output", e);
		}
		return file;
	}
	
	/**
	 * Main method that creates an xml launch file to be used for distributed input. Predatorprey was used
	 * to format the xml file.
	 * @param args
	 */
	public static void main(String[] args){
		XMLInputData xid = new XMLInputData();
		
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("XMLInputData",repast.simphony.batch.data.XMLInputData.class);

		String batchXMLPath="/predator_prey_batch/batch_params.xml";
		
		String scenario="./predator_prey_continuous_batch.rs";
		
		
		List<String[]> nodes = new ArrayList<String[]>();
		String[] value={"nsit-dhcp-250-228.uchicago.edu","xxxxx","xxxxx"};
		nodes.add(value);
		
		xid.setBatchXMLPath(batchXMLPath);
		xid.setScenario(scenario);
		xid.setName("Repast Distributed Launch Settings");
		xid.setNumberOfRuns(5);
		xid.setNodes(nodes);
		xid.setProjectPath("/Applications/RepastSimphony-1.2.0/workspace/repast.simphony.demo.predatorprey");
		String mainScenario=xid.getProjectPath()+"/predator_prey_continuous_batch.rs";
		xid.setMainScenario(mainScenario);
		
		xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
		xstream.aliasType("XMLInputData", XMLInputData.class);
		
		String xml=xstream.toXML(xid);
		String fileName= "XML_Launch_Inputs.xml";
		String outputDirectory= "../repast.simphony.distributedBatch/launchData/";
		String filename= outputDirectory+fileName;
		
		xid.output(xml,filename);
	}
}
