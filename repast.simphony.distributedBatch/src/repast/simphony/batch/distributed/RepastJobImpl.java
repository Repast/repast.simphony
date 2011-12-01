package repast.simphony.batch.distributed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import repast.simphony.batch.BatchMain;
import repast.simphony.batch.data.XMLInputData;
import repast.simphony.batch.setup.BatchMainSetup;
import simphony.util.messages.MessageCenter;

/**
 * Class implements RepastJob, which distributes a given process.
 * The run() method is called and executes the command line call to repast, which launches
 * the distributed batch process on local or remote nodes.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class RepastJobImpl<T> implements RepastJob<Object>{

	/**The input data for informing distributed run*/
	private XMLInputData data;
	
	/**The directory reference to user.dir*/
	public String dir;
	
	/**Output data contained based on remote and local files created after runs*/
	private Map<String,Object>outputInfo;
	
	/**Run number of this distributed process*/
	int runNumber;
	
	/**The run model parameters used for distribution*/
	private String runParams;
	
	/**The input reference used to make a command line call to repast*/
	private String[] input;

//	private Directory directory;

	/**Byte data for batch jar that is to be transfered over to remote nodes*/
	private BytesFromJar projectBytes;
	
	/**Delay process in order to get output file from remote node*/
	private int delayTime=0;
	
	/**String array arguments to be sent to the BatchMain method in repast.simphony.batch*/
	String[] args;
	
	/**
	 * The constructor for a job that calls distributed batch cleanly each time.
	 * @param data the input data
	 * @param projectBytes byte code data for user project to be blown up on remote nodes
	 * @param runNumber the run number
	 * @param runParams the model run parameters
	 */
	public RepastJobImpl(XMLInputData data,BytesFromJar projectBytes,int runNumber,String runParams){
		this.data=data;
		this.runNumber=runNumber;
		this.runParams=runParams;
		this.projectBytes=projectBytes;
		this.delayTime=data.getDelayTime();
//		this.directory=directory;
//		this.batchDirectory=batchDirectory;
		init();
			
	}
	
	public RepastJobImpl(){
	}
	
	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public int getRunNumber() {
		return runNumber;
	}

	public void setRunNumber(int runNumber) {
		this.runNumber = runNumber;
	}

	/**
	 * Method initializes the batch main method call 
	 * (see repast.simphony.batch.BatchMain.main(String[] args).
	 */
	public void init(){
		
		// ++++++++++++++++++++++++++++++++++++
		boolean mjbSuggest =  BatchMainSetup.MJB_SUGGEST;
		boolean mjbRequire =  BatchMainSetup.MJB_REQUIRE;
		String f = null;
		
		this.input = new String[8];
		String distributedPath=".."+File.separator+"libs"+File.separator+"ext";
		String[] splitPPath = null;
		// ++++++++++++++++++++++++++++++++++++
		if (mjbRequire) {
			f = new File(data.getProjectPath()).getName();
		} else {
			
			splitPPath=data.getProjectPath().split(File.separator);
		}
		input[0]="-params";
		
		// ++++++++++++++++++++++++++++++++++++
		if (mjbSuggest) {
			input[1]=data.getBatchXMLPath();
			
		} else {
			input[1]=data.getProjectPath() + data.getBatchXMLPath();
		}
		input[1]=data.getBatchXMLPath();
		input[2]=data.getMainScenario();
		input[3]=data.getScenario();
		// ++++++++++++++++++++++++++++++++++++
		if (mjbRequire) {
			input[4]=distributedPath+File.separator+f+".jar";
		} else {
			input[4]=distributedPath+File.separator+splitPPath[splitPPath.length-1]+".jar";
		}
		input[5]=data.getRemoteBatchXML();
		input[6]=Integer.toString(this.runNumber);
		input[7]=runParams;
		String[] args={input[0],input[1],input[2],input[3],input[4],input[5],input[6],input[7]};
		this.args=args;
		
		System.out.println("############################# One instance of job ###############################");
		for (String a : args) {
			System.out.println("<"+a+">");
		}
	}
	
	@Override
	public void addResult(Object t1, Object t2) {
		// TODO Auto-generated method stub	
	}

	public XMLInputData getData() {
		return data;
	}

	public void setData(XMLInputData data) {
		this.data = data;
	}

	@Override
	public boolean executeNow() {
		return true;
	}
	
	/**
	 * Method to delete directories (used for remote nodes).
	 * @param dir the directory
	 * @return a boolean if the directory is deleted
	 */
	public static boolean deleteDir(File dir) {
		 if(dir.exists()) {
		      File[] files = dir.listFiles();
		      for(int i=0; i<files.length; i++) {
		         if(files[i].isDirectory()) {
		           deleteDir(files[i]);
		         }
		         else {
		           files[i].delete();
		         }
		      }
		    }
		    return( dir.delete() );
		 }

	@Override
	public void finishProcess() {
		this.dir=System.getProperty("user.dir");
		synchronized(this){
			try {
				this.wait(delayTime);
			} catch (InterruptedException e) {
				MessageCenter.getMessageCenter(RepastJobImpl.class).error("Could not delay runtime" +
						"process.",e);
			}
		} 
		File f = new File(System.getProperty("user.dir"));
		String[] files=f.list();
		
		for(String file: files){
			
			File fi = new File(f.getAbsolutePath()+File.separator+file);
			
			if(this.dir.contains(File.separator+"gridgain-")){
				if(fi.isDirectory())		
					deleteDir(fi);
				else if(file.contains(".xml") || file.contains(".launch") || file.contains(".log") ||
						file.contains(".settings") || file.contains(".properties"))
					fi.delete();
			}
			
			if(file.contains(".") && !file.contains(".xml") &&!file.contains(".launch") && !file.contains(".svn")
					&& !file.contains(".log") && !file.contains(".properties") 
					&& !file.contains(".settings") && !file.contains("plugin_jpf.txt") && !file.contains(".sh")) {
				if(fi==null || !fi.exists()) {
				continue;
				}
				
				byte[] buffer = new byte[(int) fi.length()];
				FileInputStream fis = null;
				
				try {
					fis = new FileInputStream(fi.getAbsolutePath());
				} catch (FileNotFoundException e) {
					MessageCenter.getMessageCenter(RepastJobImpl.class).error("Problems with file input stream" +
						"batch mode", e);
					e.printStackTrace();
				}

				try {
					fis.read(buffer);
					
				} catch (IOException e) {
						MessageCenter.getMessageCenter(RepastJobImpl.class).error("Problems with reading file", e);
						e.printStackTrace();
				}

				if(outputInfo==null)
					outputInfo= new HashMap<String,Object>();
				outputInfo.put(fi.getName(), buffer);
				
				if(this.dir.contains(File.separator+"gridgain-")){
					if(!file.contains(".sh")){
						
						//remote output files are deleted here if user chooses this option
						if(this.data.isRemoveFiles())
							fi.delete();
					}
				}
			}
		}
	}

	@Override
	public BatchRunResults returnResults() {
		BatchRunResults brr = new BatchRunResults();
		brr.setResults(outputInfo);
		return brr;
	}
	
	/**
	 * Create the user jar on the remote node and unjar contents.
	 * @throws IOException
	 */
	public void createJar() throws IOException{
//		batchBytes.writeJar(jar);
		projectBytes.writeJar(".."+File.separator+"libs"+File.separator+"ext"+File.separator+projectBytes.getName());
		String arg ="jar xf .."+File.separator+"libs"+File.separator+"ext"+File.separator+projectBytes.getName();
		
		try {
				Process p=Runtime.getRuntime().exec(arg);
				p.waitFor();
				System.out.println(arg);
			} catch (InterruptedException e3) {
				MessageCenter.getMessageCenter(RepastJobImpl.class).error("Could not unjar jar file", e3);
		}
			
		projectBytes=null;
	}
		
	@Override
	public void run() {
		String dir=System.getProperty("user.dir");
		this.dir=dir;
		
		if(dir.contains(File.separator+"gridgain-")){
			dir=dir.substring(0, dir.length()-3)+"libs"+File.separator+"ext";
			String file1=File.separator+this.data.getName().concat(".jar");
			File f = new File(dir+file1);
			if(!f.exists()){
				try{
					createJar();  //fired if a batch jar needs to be created
				}
				catch(IOException e){
					MessageCenter.getMessageCenter(RepastJobImpl.class).error("Can't create batch jar" +
							"in batch mode", e);
					e.printStackTrace();
				} 
			}
		}
		
		try {
			new BatchMain().main(args);
		} catch (Exception e) {
			MessageCenter.getMessageCenter(RepastJobImpl.class).error("Problems with launching" +
					"batch mode", e);
			e.printStackTrace();
		}	
	}
}
