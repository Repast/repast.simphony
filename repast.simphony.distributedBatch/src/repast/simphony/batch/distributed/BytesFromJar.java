package repast.simphony.batch.distributed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import repast.simphony.batch.data.CreateUserJar;

/**
 * Class that stores user's info from a jar and recreates the jar in different locations. 
 * The class can also create a user jar from a project location.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 *
 */
public class BytesFromJar implements Serializable{

	/**The bytes to store for a user jar*/
	private byte[] bytes;

	/**The name of the user project jar*/
	private String name;
	
	public BytesFromJar(String name){
		this.name=name;
	}
	
	
	public String getName() {
		return name;
	}


	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * Method reads a jar file from a path and stores the data in a byte array.  
	 * @param file the name of the jar file
	 */
	public void readToByteArray(String file){
		try {
			
			bytes = IOUtils.toByteArray(new FileInputStream(file));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method writes the jar file to the path given.
	 * @param pathToWriteTo the string path to write the jar to.
	 */
	public void writeJar(String pathToWriteTo){
		try {
			FileOutputStream s = new FileOutputStream(pathToWriteTo);
			IOUtils.write(bytes, s);
			s.flush();
			s.close();
			bytes=null;
	//		FileOutputStream fos = new FileOutputStream(pathToWriteTo);
	//		fos.write(bytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method creates a jar based on the path of the project
	 * @param path the path of the project
	 */
	public void createJar(String path){
		CreateUserJar cuj = new CreateUserJar();
		try {
			try {
				cuj.createJar(path);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test main to see if the class can read and write jars based on byte arrays
	 * @param args
	 */
	public static void main(String[] args){
		BytesFromJar bfj = new BytesFromJar("repast.simphony.batch.jar");
		String path="../repast.simphony.batch";
		bfj.createJar(path);
		bfj.readToByteArray("../repast.simphony.batch.jar");
		bfj.writeJar("/Users/markaltaweel/Desktop/"+bfj.getName());
	}
}
