package repast.simphony.batch.distributed;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to store a directory structure (e.g., RS folder).
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 *
 */
public class Directory implements FileSystemComponent, Serializable {

/**The name of the directory*/
private String directoryName;

/**The output directory path*/
private String outputDirectoryName;

	public Directory(String directoryName){
		this.directoryName = directoryName;
	}
	
	//Collection of child FileSystemComponents.
	private List<FileSystemComponent> children = new ArrayList<FileSystemComponent>();
	
	
	public String getOutputDirectoryName() {
		return outputDirectoryName;
	}

	public void setOutputDirectoryName(String outputDirectoryName) {
		this.outputDirectoryName = outputDirectoryName;
	}

	/**
	 * Outputs the directory and its children into the parent directory.
	 * @param parentDirecotyr a parent directory file
	 */
	public void output(File parentDirectory) {
		File thisFile = new File(parentDirectory,directoryName);
		//Does not overwrite
		if (!thisFile.exists()){
			thisFile.mkdirs();
			for (FileSystemComponent child : children) {
				child.output(thisFile);
			}
		}
	}
	
	/**
	 * Adds the graphic to the composition.
	 * @param child a file system child component.
	 */
	public void add(FileSystemComponent child) {
		children.add(child);
	}
	
	/**
	 * Removes a child component.
	 * @param a child component.
	 */
	public void remove(FileSystemComponent child) {
		children.remove(child);
	}

}
