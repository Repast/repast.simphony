package repast.simphony.batch.distributed;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Stack;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

/**
 * Class that allows you to walk through a directory and capture the data files in that directory.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 *
 */
public class RSDirectoryWalker extends DirectoryWalker {

	/**The directory to walk over*/
	Directory directory;

	/**The top level start directory*/
	File startDirectory;
	
	public Directory getDirectory() {
		return directory;
	}

	/**
	 * Main constructor
	 * 
	 * @param startDirectory the top level start directory.
	 */
	public RSDirectoryWalker(File startDirectory) {	
		super(FileFilterUtils.makeSVNAware(null), -1);
		this.startDirectory = startDirectory;
		
	}

	/**
	 * Method to start from the top level directory and search for files in that directory.
	 */
	public void startWalking() {
		Stack<Directory> newS = new Stack();
		// newS.add(directory);
		try {
			this.walk(startDirectory, newS);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	protected boolean handleDirectory(File directory, int depth,Collection results) {
		Directory direc = new Directory(directory.getName());
		if (results.size() != 0) {
			Directory parent = (Directory) ((Stack) results).peek();

			parent.add(direc);
		}
		((Stack) results).add(direc);

		return true;
	}

	@Override
	protected void handleDirectoryEnd(File directory, int depth,
			Collection results) {
		
		Directory poppedDir = (Directory)((Stack) results).pop();
		if (results.size() == 0){
			this.directory = poppedDir;
		}
	}

	@Override
	protected void handleFile(File file, int depth, Collection results) {
		Directory parent = (Directory) ((Stack) results).peek();
		try {
			parent.add(new TextFile(file.getName(), FileUtils
					.readFileToString(file)));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
