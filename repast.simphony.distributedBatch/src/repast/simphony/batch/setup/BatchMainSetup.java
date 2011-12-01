package repast.simphony.batch.setup;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//mjb suggest imports
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
// mjb suggest end

import org.gridgain.grid.GridFactory;
import org.gridgain.grid.GridNode;

import repast.simphony.batch.data.CreateUserJar;
import repast.simphony.batch.data.XMLInputData;
import repast.simphony.batch.data.XMLSweeper;
import repast.simphony.batch.distributed.BatchQueue;
import repast.simphony.batch.distributed.BatchQueueImpl;
import repast.simphony.batch.distributed.BatchTaskRun;
import repast.simphony.batch.distributed.BytesFromJar;
import repast.simphony.batch.distributed.Directory;
import repast.simphony.batch.distributed.Message;
import repast.simphony.batch.distributed.RSDirectoryWalker;
import repast.simphony.batch.distributed.RepastJob;
import repast.simphony.batch.distributed.RepastJobImpl;
import repast.simphony.batch.distributed.gridgain.GridGainBatchTask;

/**
 * Setup batch process via gridgain and
 * available nodes.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 */
public class BatchMainSetup {
	
	public final static boolean MJB_SUGGEST = false;
	public final static boolean MJB_REQUIRE = true;
    
	/**The BatchTask object used to launch GridGain*/
	protected BatchTaskRun batchTask= new GridGainBatchTask();
	
	/**Input data object created from xml input */
	protected XMLInputData inputData;

	/**Map to store nodes used in transferring and removing files*/
	protected HashMap<String, String[]> inputNodes;

	/**Map stores run input parameters (i.e., setting for a model) that are sent to a node */
	protected Map<Integer,String>fileRunInput = new HashMap<Integer,String>();
	
	/**The container object for the jobs to distribute*/
	BatchQueue<RepastJob>queue=new BatchQueueImpl<RepastJob>();
	
	/**Number of jobs to run*/
	int runCount=0;
	
	/**Map that contains the unique ip nodes based on address*/
	private Map<String,GridNode>uniqueNodes=new HashMap<String,GridNode>();

	/**Message object that contains data to send to remote nodes*/
	private Message message;
	
//	private Directory directory;

	/**Byte code data for a specific project*/
	private BytesFromJar projectBytes;
	
	// mjb suggest start
	private Options options;
	private String launchFile;
	private String sweepFile;
	// mjb suggest end

	/**Name of the project, which is the same as the name of the project on the user workspace*/
	public  static String ProjectName;
	
	/**Jar creating class*/
	private CreateUserJar cuj=new CreateUserJar();
	
	public BatchMainSetup() {
		
	}
	
	// mjb suggest
//	public void setup(String[] args) {
//		options = new Options();
//		Option help = new Option("help", "print this message");
//
//		Option paramfile = OptionBuilder.withArgName("file")
//		.hasArg()
//		.withDescription("use given parameter sweep file")
//		.create("params");
//
//		Option launchfile = OptionBuilder.withArgName("file")
//		.hasArg()
//		.withDescription("use given launch file")
//		.create("launch");
//
//		Option opt = OptionBuilder.withArgName("file")
//		.hasOptionalArg()
//		.withDescription("use optimizable parameter sweeper with given optimizing properties file")
//		.create("opt");
//
//		Option mode = OptionBuilder
//		.withDescription("specifies if the batch mode is interactive")
//		.create("interactive");
//
//		options.addOption(help);
//		options.addOption(paramfile);
//		options.addOption(launchfile);
//		options.addOption(opt);
//		options.addOption(mode);
//
//		if (args != null && args.length > 0) {
//			CommandLineParser parser = new GnuParser();
//
//			// parse the command line arguments
//			CommandLine line;
//			try {
//				line = parser.parse(options, args);
//				String params = line.getOptionValue("params");
//				if (params != null) {
//					sweepFile = params;
//				}
//				String launch = line.getOptionValue("launch");
//				if (launch != null) {
//					launchFile = launch;
//				}
//
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}
	// mjb suggest end
	
	
	public XMLInputData getInputData() {
		return inputData;
	}

	/**
	 * This methods starts up the grid configuration and will attach to other active nodes.
	 */
	public void initializeSetup() {
		batchTask.initializeTask();
	}
	
	public void setInputData(XMLInputData inputData){
		this.inputData=inputData;
	}
	
	/**
	 * This method reads the information from the user XML launch input file and sets the data object accessible by all nodes.
	 */
	public void determineJobs(){
		XMLInputData xmi = new XMLInputData();
		this.inputData=xmi.setupXMLMapFileData(".."+File.separator+"repast.simphony.distributedBatch"+
				File.separator+"launchData"+File.separator+"XML_Launch_Inputs.xml");
		BatchMainSetup.ProjectName=this.inputData.getName();
	}
	
	// +++++++++++++++++++++++++++++++++++++++++++
	// mjb suggest
	public void determineJobs(String launchFile){
		XMLInputData xmi = new XMLInputData();
		this.inputData=xmi.setupXMLMapFileData(launchFile);
		BatchMainSetup.ProjectName=this.inputData.getName();
	}
	// mjb suggest end

	/**
	 * Method to create and send the distributed jobs to a GridGain Task based on input data (user xml input info.)
	 * information.
	 */
	public void runJobs(){
		
		boolean mjbSuggest =  BatchMainSetup.MJB_SUGGEST;
		
		int runs=this.inputData.getNumberOfRuns();
		
		if (mjbSuggest) {
			runs =  this.fileRunInput.size();
		}
		for(int i=0; i < runs;i++) {
			String step=this.fileRunInput.get(i);
			RepastJobImpl rji = new RepastJobImpl(inputData,projectBytes,i,step);
			queue.addJob(rji);
	//		queue.removeJob(rji);
		}
		batchTask.startTask(queue);
	}
	
	/**
	 * Method to setup the user directory that contains the rs folder.
	 * @return a Directory object that contains the user's directory structure
	 */
	public Directory setupDirectory(){
		String projDirectory=this.inputData.getProjectPath();
		String rsFolder=this.inputData.getScenario();
		RSDirectoryWalker walker = new RSDirectoryWalker(new File(projDirectory+rsFolder));
		walker.startWalking();
		Directory directory=walker.getDirectory();
		
		return directory;
	}
	
	/**
	 * Method to set the parameters from the xml launch data file (see lanunchData folder).
	 * @throws Exception
	 */
	public void setParameters() throws Exception{
		int runs = this.inputData.getNumberOfRuns();
		String file = this.inputData.getBatchXMLPath();
		XMLSweeper xml = new XMLSweeper(file,runs);
		int i=0;
		while(xml.keepRunning(i)) {
			xml.resourceLoader();
			String step=xml.step(i);
			xml.writeData(step);
			this.fileRunInput.put(i, step);
			i++;
		}	
	}
	
	/**
	 * Shut the node down.
	 */
	public void finishJobs(){
		 GridFactory.stop(true);
	}
	
	/**
	 * Method to determine nodes needed for process. Currently, this is only used for manual transfer and removal of files on remote nodes.
	 */
	public void determineNodes(){
		XMLInputData xmi = new XMLInputData();
		this.inputData=xmi.setupXMLMapFileData(".."+File.separator+"repast.simphony.distributedBatch"+
				File.separator+"launchData"+File.separator+"XML_Launch_Inputs.xml");
		List<String[]> nodes=this.inputData.getNodes();
		this.inputNodes= new HashMap<String,String[]>();
		for(String[] node: nodes){
			String[] s = {node[1],node[2]};
			inputNodes.put(node[0], s);
		}
	}
	
	/**
	 * Method to see if there is a message to send to remote nodes and if there is to send the message over.
	 */
	public void checkMessage(){
		if(message.isThereIsMessage()){
			HashSet<GridNode> nodes=(HashSet) batchTask.returnNodeCollection();
			InetAddress addLocal=null;
			try {
				addLocal = InetAddress.getLocalHost();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
			String hostnameLocal=addLocal.getHostName();
			Iterator<GridNode> ic=nodes.iterator();
			while(ic.hasNext()){
				GridNode node = ic.next();
				String address=node.getPhysicalAddress();
				InetAddress addr=null;
				try {
					addr = InetAddress.getByName(address);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				if(addr.getHostName().equals(hostnameLocal))
					continue;
				if(!uniqueNodes.containsKey(address)){
					uniqueNodes.put(address, node);
				}
			}
			batchTask.setMessangingNodes(uniqueNodes);
			batchTask.setupMessanging(message);
		}
	}
	
	/**
	 * Method to create the user batch directory structure to be sent over to the remote nodes.
	 * @return a Directory object that contains the user batch directory structure
	 */
	public Directory setupBatchFile(){
		String[] s = this.inputData.getBatchXMLPath().split(File.separator);
		String pathDir="";
		for(int i=0; i < s.length; i++){
			if(i<s.length-2)
				pathDir+=s[i];
		}
		RSDirectoryWalker walker = new RSDirectoryWalker(new File(this.inputData.getProjectPath()+pathDir));
		walker.startWalking();
		Directory directory=walker.getDirectory();
		
		return directory;
	}
	
	/**
	 * Method to create the batch jar.
	 * @return a batch jar byte array.
	 */
	public BytesFromJar createBatchBytes(){
		BytesFromJar bfj = new BytesFromJar("repast.simphony.batch.jar");
		bfj.readToByteArray(".."+File.separator+"repast.simphony.distributedBatch"+File.separator+"transferFiles"
				+File.separator+"repast.simphony.batch.jar");
		
		return bfj;
	}
	
	/**
	 * Method to create a byte arrage of the user project. The byte array will be transported to the remote nodes.
	 * @return a ByteFromJar class that contains the byte arrays of the user project
	 */
	public BytesFromJar createProjectBytes(){
		
		boolean mjbSuggest =  BatchMainSetup.MJB_REQUIRE;
		String jarFile;
		
		if (mjbSuggest) {
			String path = this.inputData.getProjectPath();
			jarFile=new File(path).getName()+".jar";
		} else {
			jarFile=this.inputData.getName()+".jar";
		}
		BytesFromJar bfj = new BytesFromJar(jarFile);
//		String scenario=this.inputData.getScenario().split("/")[1];
//		bfj.createJar(path,scenario);
		bfj.readToByteArray(".."+File.separator+this.inputData.getName()+File.separator+"transferFiles"+
				File.separator+jarFile);
		
		return bfj;
	}
	
	/**
	 * The main to launch distributed batch processes.
	 * @param args the args input
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		
		boolean mjbSuggest =  BatchMainSetup.MJB_REQUIRE;
		
		BatchMainSetup bms = new BatchMainSetup();
		if (mjbSuggest) {
//			bms.setup(args);
			bms.setSweepFile(args[1]);
			bms.setLaunchFile(args[2]);
			bms.determineJobs(bms.launchFile);
		} else {
			bms.determineJobs();
		}
		if(bms.inputData.isCreateJar()){
			bms.cuj.createJarFromAnt(bms.inputData);
		}
		bms.projectBytes=bms.createProjectBytes();
		bms.initializeSetup();
		bms.setParameters();
		bms.runJobs();
		bms.finishJobs();
	}
	
	public  void execute(String[] args) throws Exception{
		
		boolean mjbSuggest =  BatchMainSetup.MJB_REQUIRE;
		
		System.out.println("0: <"+args[0]+">");
		System.out.println("1: <"+args[1]+">");
		System.out.println("2: <"+args[2]+">");
		
		BatchMainSetup bms = new BatchMainSetup();
		if (mjbSuggest) {
			bms.setSweepFile(args[1]);
			bms.setLaunchFile(args[2]);
//			bms.setup(args);
			bms.determineJobs(bms.launchFile);
		} else {
			bms.determineJobs();
		}
		if(bms.inputData.isCreateJar()){
			bms.cuj.createJarFromAnt(bms.inputData);
		}
		bms.projectBytes=bms.createProjectBytes();
		bms.initializeSetup();
		bms.setParameters();
		bms.runJobs();
		bms.finishJobs();
	}

	public String getLaunchFile() {
		return launchFile;
	}

	public void setLaunchFile(String launchFile) {
		this.launchFile = launchFile;
	}

	public String getSweepFile() {
		return sweepFile;
	}

	public void setSweepFile(String sweepFile) {
		this.sweepFile = sweepFile;
	}
}

