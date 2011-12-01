package repast.simphony.batch.distributed;

import java.io.File;

/**
 * Interface for output a file structure to a given directory
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 *
 */
public interface FileSystemComponent {
	
	/**
	 * Method to output a file to a given parent directory.
	 * 
	 * @param parentDirectory the file parent directory.
	 */
	public void output(File parentDirectory);
}
