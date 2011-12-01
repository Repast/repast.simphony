package repast.simphony.batch.distributed.gridgain;

import java.io.File;
import java.io.Serializable;
import java.util.UUID;

import org.gridgain.grid.GridMessageListener;

import repast.simphony.batch.distributed.BytesFromJar;
import repast.simphony.batch.distributed.Directory;
import repast.simphony.batch.distributed.Message;

/**
 * Object listener that does something from a Message object.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel.
 *
 */
public class GridMessage implements GridMessageListener {


	public GridMessage(){

	}
	
	@Override
	public void onMessage(UUID arg0, Serializable arg1) {
		System.out.println("message received");
		if(arg1 instanceof MessageImpl){
			MessageImpl m = (MessageImpl)arg1;
			if(m.isCleanDirectory()){
				cleanDirectory();
			}
			if(m.isPlaceRSFolder()){
				placeRSFolder(m);
			}
			if(m.isSetupJar()){
				setupJar(m);
			}
		}
		
	}
	
	/**
	 * Setup a jar on a remote node.
	 * @param m the message.
	 */
	public void setupJar(Message m){
		String dir=System.getProperty("user.dir");
		if(dir.endsWith("/gridgain-2.1.1/bin")){
			BytesFromJar bfj=((MessageImpl)m).getBfj();
//			bfj.writeJar(dir+"/"+bfj.getName());
		}
	}
	
	/**
	 * Method to place an RS folder on remote node.
	 * @param m the message.
	 */
	public void placeRSFolder(MessageImpl m){
		String dir=System.getProperty("user.dir");
		if(dir.endsWith("/gridgain-2.1.1/bin")){
			Directory d = m.getDirectory();
			d.output(new File(d.getOutputDirectoryName()));
			System.out.println("directory output");
		}
	}
	
	public void cleanDirectory(){
		String dir=System.getProperty("user.dir");
		if(dir.endsWith("/gridgain-2.1.1/bin")){
			File f = new File(dir);
			File[] files=f.listFiles();
			for(File ff:files){
				if(!ff.getName().endsWith(".sh") || !ff.getName().endsWith(".bat") || !f.getName().endsWith("velocity.log")||
						!f.getName().endsWith("readme.txt")){
					ff.delete();
				}
			}
		}
		
	}

}
