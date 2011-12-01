package repast.simphony.batch.setup;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.gridgain.grid.GridFactory;
import org.gridgain.grid.GridNode;

import simphony.util.messages.MessageCenter;

/**
 * Class to remove jars and retrieve outputs from remote nodes.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 */
public class RemoveTransferFromRemoteNodes extends LoadFilesToRemoteNodes{

	/**
	 * Method to remove jar files from remote node.
	 * @throws IOException
	 */
	public void removeJarFiles()throws IOException {
		HashSet<GridNode> nodes=(HashSet) batchTask.returnNodeCollection();
		Iterator<GridNode> ic=nodes.iterator();
		InetAddress addLocal = InetAddress.getLocalHost();
		String hostnameLocal=addLocal.getHostName();
		while(ic.hasNext()){
			GridNode node = ic.next();
			String address=node.getPhysicalAddress();
			InetAddress addr=InetAddress.getByName(address);
		    byte[] rawAddr=addr.getAddress();
			Map<String,String> attributes=node.getAttributes();
			InetAddress hostname=InetAddress.getByAddress(rawAddr);
			if(hostname.getHostName().equals(hostnameLocal))
				continue;
			String gridPath=attributes.get("GRIDGAIN_HOME");
			
			FTPClient ftp = new FTPClient();
			   try {
			      String[] usernamePass=inputNodes.get(hostname.getHostName());
			      ftp.connect(hostname);
			      ftp.login(usernamePass[0], usernamePass[1]);
				  int reply = ftp.getReplyCode();
				  if(!FTPReply.isPositiveCompletion(reply)) {
				        ftp.disconnect();
				        System.err.println("FTP server refused connection.");
				        continue;
				  }
			      ftp.login(usernamePass[0], usernamePass[1]); //Login
			      String directory=gridPath+"/libs/ext/";
			      ftp.changeWorkingDirectory(directory); //Change to remote directory
				  FTPFile[] fs = ftp.listFiles();
				  for(FTPFile f:fs){
					  if(f.isDirectory())
						continue;
					  System.out.println(f.getName());
					  ftp.deleteFile(f.getName());
				  }
			      ftp.sendCommand("rm *");
			      ftp.logout();
			      ftp.disconnect();
			   } catch (Exception e) {
				   MessageCenter.getMessageCenter(BatchMainSetup.class).error("Problems with the FTP connection." +
							"A file has not been succesfully transfered", e);
					e.printStackTrace();
			   }

		}
	}
	
	/**
	 * Method to finish the remote connection.
	 */
	public void finishJobs(){
		 GridFactory.stop(true,false);
	}
	
	/**
	 * Method to transfer output files.
	 * @throws IOException
	 */
	public void transferOutputFiles()throws IOException {
		HashSet<GridNode> nodes=(HashSet) batchTask.returnNodeCollection();
		Iterator<GridNode> ic=nodes.iterator();
		InetAddress addLocal = InetAddress.getLocalHost();
		String hostnameLocal=addLocal.getHostName();
		while(ic.hasNext()){
			GridNode node = ic.next();
			String address=node.getPhysicalAddress();
			InetAddress addr=InetAddress.getByName(address);
		    byte[] rawAddr=addr.getAddress();
			Map<String,String> attributes=node.getAttributes();
			InetAddress hostname=InetAddress.getByAddress(rawAddr);
			if(hostname.getHostName().equals(hostnameLocal))
				continue;
			String[] usernamePass=inputNodes.get(hostname.getHostName());
			String gridPath=attributes.get("GRIDGAIN_HOME");
			FTPClient ftp = new FTPClient();
			ftp.connect(hostname);
			ftp.login(usernamePass[0], usernamePass[1]);
			int reply = ftp.getReplyCode();
		      if(!FTPReply.isPositiveCompletion(reply)) {
		        ftp.disconnect();
		        System.err.println("FTP server refused connection.");
		        continue;
		     }
			
			ftp.changeWorkingDirectory(gridPath+"/bin");
			ftp.setFileType(FTPClient.COMPRESSED_TRANSFER_MODE);
			ftp.setRemoteVerificationEnabled(false);
			ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
			FTPFile[] fs = ftp.listFiles();
				for(FTPFile f:fs){
					if(f.isDirectory())
						continue;
					String fileName=f.getName();
					if(!fileName.endsWith(".txt"))
						continue;
					System.out.println(f.getName());
					FileOutputStream out = new FileOutputStream("../repast.simphony.distributedBatch/"+"remoteOutput/"+f.getName());
					try{
						ftp.retrieveFile(fileName,out);
					}
					catch(Exception e){
						continue;
					}
					finally{
						if(out!=null)
							out.close();
					}
				}
			ftp.logout();
			ftp.disconnect();
		}
	}
	
	/**
	 * Main method to allow remote files to be removed and transfered.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		RemoveTransferFromRemoteNodes rfr = new RemoveTransferFromRemoteNodes();
		
		rfr.initializeSetup();
		rfr.determineNodes();
		rfr.removeJarFiles();
		rfr.transferOutputFiles();
		rfr.finishJobs();
	}
}
