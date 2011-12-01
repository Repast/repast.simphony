package repast.simphony.batch.distributed.gridgain;

import repast.simphony.batch.distributed.BytesFromJar;
import repast.simphony.batch.distributed.Directory;
import repast.simphony.batch.distributed.Message;

/**
 * Class containing information for a message.
 *  
 * @version $Revision: 2.0
 * @author Mark Altaweel
 *
 */
public class MessageImpl implements Message {

	private boolean thereIsMessage=false;
	private boolean cleanDirectory=false;
	private boolean placeRSFolder=false;
	private boolean setupJar=false;
	private Directory directory;
	private BytesFromJar bfj;
	
	public boolean isCleanDirectory() {
		return cleanDirectory;
	}
	
	public boolean isThereIsMessage() {
		return thereIsMessage;
	}

	public void setThereIsMessage(boolean thereIsMessage) {
		this.thereIsMessage = thereIsMessage;
	}

	public void setCleanDirectory(boolean cleanDirectory) {
		this.cleanDirectory = cleanDirectory;
	}
	public boolean isPlaceRSFolder() {
		return placeRSFolder;
	}
	public void setPlaceRSFolder(boolean placeRSFolder) {
		this.placeRSFolder = placeRSFolder;
	}
	public Directory getDirectory() {
		return directory;
	}
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}
	public BytesFromJar getBfj() {
		return bfj;
	}
	public void setBfj(BytesFromJar bfj) {
		this.bfj = bfj;
	}
	public boolean isSetupJar() {
		return setupJar;
	}
	public void setSetupJar(boolean setupJar) {
		this.setupJar = setupJar;
	}
}
