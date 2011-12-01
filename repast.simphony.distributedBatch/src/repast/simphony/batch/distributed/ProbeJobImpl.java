package repast.simphony.batch.distributed;

import java.io.File;
import java.util.List;

/**
 * Implementation class of the probe that preps a remote node for a job that needs user jars and data,
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 *
 */
public class ProbeJobImpl implements RepastJob<Object>{

	/**The user's project directory*/
	private Directory directory;
	
	/**The list of stored byte arrays to send to remote nodes*/
	private List<BytesFromJar> bytes;

	/**
	 * Default constructor.
	 * @param directory the user folder directory
	 * @param bytes the byte code arrays for the user's project
	 */
	public ProbeJobImpl(Directory directory,List<BytesFromJar>bytes){
		this.directory=directory;
		this.bytes=bytes;
	}
	
	@Override
	public void addResult(Object t1, Object t2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean executeNow() {
		return true;
	}

	@Override
	public void finishProcess() {
		
	}

	@Override
	public BatchRunResults returnResults() {
		return null;
	}

	@Override
	public void run() {
		String dir=System.getProperty("user.dir");
		File rsFile = new File(dir);
		if(directory!=null) {
			directory.output(rsFile);
			System.out.println("directory output");
		}
		
		for(BytesFromJar byts:bytes){
			String name=byts.getName();
			byts.writeJar(dir+"/"+name);
		}
	}
}
