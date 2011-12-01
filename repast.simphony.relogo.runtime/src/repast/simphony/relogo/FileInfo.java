package repast.simphony.relogo;

import java.io.BufferedReader;
import java.io.File;

public class FileInfo {
	
	public FileInfo(File file){
		this.file = file;
		this.fileReadable = true;
		this.bufferedReader = null;
	}
	
	private BufferedReader bufferedReader;
	private File file;
	private boolean fileReadable;
	
	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}
	public File getFile() {
		return file;
	}
	public boolean isFileReadable() {
		return fileReadable;
	}
	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public void setFileReadable(boolean fileReadable) {
		this.fileReadable = fileReadable;
	}
}
