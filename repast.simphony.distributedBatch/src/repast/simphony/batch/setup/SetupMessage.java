package repast.simphony.batch.setup;

import java.io.File;

import repast.simphony.batch.data.XMLInputData;
import repast.simphony.batch.distributed.BytesFromJar;
import repast.simphony.batch.distributed.Directory;
import repast.simphony.batch.distributed.RSDirectoryWalker;
import repast.simphony.batch.distributed.gridgain.MessageImpl;

/**
 * Class to handle messages for communication between nodes.
 * 
 * @author Mark Altaweel
 * @version $Revision: 2.0
 *
 */
public class SetupMessage {

	private MessageImpl message;
	private XMLInputData data;
	private boolean thereIsMessage=false;

	/**
	 * The input data constructor that uses xml input.
	 * @param data an input data object of xml data
	 */
	public SetupMessage(XMLInputData data){
		this.data=data;
		this.message=new MessageImpl();
		readData();
	}
	
	

	public boolean isThereIsMessage() {
		return thereIsMessage;
	}



	public void setThereIsMessage(boolean thereIsMessage) {
		this.thereIsMessage = thereIsMessage;
	}


	/**
	 * Method applies message of clearing a remote directly from previous runs,
	 * setting up jars, and placing the rs folder.
	 */
	public void readData(){
		
		cleanDirectroy();
		message.setThereIsMessage(true);
		
		setupJarOuput();
		message.setThereIsMessage(true);
		
		
		setupRSFolder();
		message.setThereIsMessage(true);
			
	}
	
	public MessageImpl getMessage() {
		return message;
	}


	public void setMessage(MessageImpl message) {
		this.message = message;
	}


	public XMLInputData getData() {
		return data;
	}


	public void setData(XMLInputData data) {
		this.data = data;
	}
	
	public void setupCleanup(){
		message.setCleanDirectory(true);
	}

	/**
	 * Setup a jar output file based on data given to the message from xml
	 */
	public void setupJarOuput(){
		message.setSetupJar(true);
		String path = data.getProjectPath();
		String[] pathA=path.split("/");
		String jarFile=pathA[pathA.length-1]+".jar";
		BytesFromJar bfj = new BytesFromJar(jarFile+".jar");
		bfj.createJar(path);
		bfj.readToByteArray("../repast.simphony.distributedBatch/transferFiles/"+jarFile);
		message.setBfj(bfj);
	}

	/**
	 * The repast .rs folder setup method based on input from data file.
	 */
	public void setupRSFolder(){
		message.setPlaceRSFolder(true);
		String path=data.getProjectPath();
		String scenario=data.getScenario();
		RSDirectoryWalker walker = new RSDirectoryWalker(new File(path+scenario));
		walker.startWalking();
		Directory directory=walker.getDirectory();
		String outputDirectoryName="../bin";
		directory.setOutputDirectoryName(outputDirectoryName);
		message.setDirectory(directory);
		
	}
	
	/**
	 * Send message to cleanup the main directory with data.
	 */
	public void cleanDirectroy(){
		message.setCleanDirectory(true);
	}
}
