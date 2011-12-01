package repast.simphony.batch.distributed;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Class to output a text file to a given path.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel.
 *
 */
public class TextFile implements FileSystemComponent {

	/**The file name of the text file*/
	String fileName;
	
	/**The contents of the text file*/
	String contents;
	
	/**
	 * Constructor for writing a text file output.
	 * @param fileName the file name to output.
	 * @param contents the string contents to output.
	 */
	public TextFile(String fileName, String contents){
		this.fileName = fileName;
		this.contents = contents;
	}
	/**
	 * Method to output a directory.
	 * 
	 * @param parentDirectory the parent directory path.
	 */
	public void output(File parentDirectory) {
		File thisFile = new File(parentDirectory,fileName);
		if (!thisFile.exists()){
			try {
				FileUtils.writeStringToFile(thisFile, contents);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
