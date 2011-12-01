package repast.simphony.batch.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.gridgain.grid.GridNode;

import simphony.util.messages.MessageCenter;
import sun.net.ftp.FtpClient;

/**
 * Class to load files on remote nodes.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 */
public class LoadFilesToRemoteNodes extends BatchMainSetup{

	/**
	 * Method to transfer jar files (from transferFiles folder in project folder) to remote nodes.
	 * @throws IOException
	 */
	public void transferJarFiles()throws IOException {
		HashSet<GridNode> nodes=(HashSet) batchTask.returnNodeCollection();
		String directory ="../repast.simphony.distributedBatch/transferFiles/";
		InetAddress addLocal = InetAddress.getLocalHost();
		String hostnameLocal=addLocal.getHostName();
		File dir = new File(directory);
		String[] children = dir.list();
		Iterator<GridNode> ic=nodes.iterator();
		while(ic.hasNext()){
			GridNode node = ic.next();
			String address=node.getPhysicalAddress();
			InetAddress addr=InetAddress.getByName(address);
			if(addr.getHostName().equals(hostnameLocal))
				continue;
		    byte[] rawAddr=addr.getAddress();
			Map<String,String> attributes=node.getAttributes();
			InetAddress hostname=InetAddress.getByAddress(rawAddr);
			String gridPath=attributes.get("GRIDGAIN_HOME");
	
			FtpClient ftp = new FtpClient();
			ftp.setConnectTimeout(3000);
			   try {
			      ftp.openServer(hostname.getHostName()); //Connect to FTP server
			      String[] usernamePass=inputNodes.get(hostname.getHostName());
			      ftp.login(usernamePass[0], usernamePass[1]); //Login
			      ftp.binary(); //Set to binary mode transfer
			      ftp.cd(gridPath+"/libs/ext/"); //Change to remote directory
			      for(int i=0; i < children.length;i++) {
			    	  	if(children[i].equals(".svn"))
			    	  		continue;
			    	  	File file = new File(directory+children[i]);
			    	  	System.out.println(children[i]);
			      		OutputStream out = ftp.put(file.getName()); //Start upload
			      		InputStream in = new FileInputStream(file);
			      		byte c[] = new byte[4096];
			      		int read = 0;
			      		while ((read = in.read(c)) != -1 ) {
			      			out.write(c, 0, read);
			      		} //Upload finished
			      		in.close();
			      		out.close();
			      }
			      ftp.closeServer(); //Close connection
			   } catch (Exception e) {
				   MessageCenter.getMessageCenter(BatchMainSetup.class).error("Problems with the FTP connection." +
							"A file has not been succesfully transfered", e);
					e.printStackTrace();
			   }

		}
	}
	
	/**
	 * Method creates a project jar of the project the user wants to distribute.
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void createProjectJar() throws IOException, InterruptedException{
		this.inputData.createJar();
	}
	
	/**
	 * Main argument for launching the transfer process to remote nodes.
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException{
		LoadFilesToRemoteNodes lfr = new LoadFilesToRemoteNodes();
		lfr.initializeSetup();
		lfr.determineNodes();
		lfr.createProjectJar();
		lfr.transferJarFiles();
		lfr.finishJobs();
	}
}
