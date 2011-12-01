package repast.simphony.batch.distributed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;

/**
 * Object for storing directory contents. This object is currently needed.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 *
 */
public class Files {

	Map<String,Map<String,Object>> contents = new HashMap<String,Map<String,Object>>();
	
	public Files(){
		
	}

	public void readDirectory(String directory, int r) throws IOException{
		File f = new File(directory);
		Map<String,Object> obMap=null;
		if(contents.containsKey(f.getName())){
			obMap= contents.get(f.getName());
		}
		else {
			obMap=new HashMap<String,Object>();
		}
		if(f.isDirectory()){
			String[] fs=f.list();
			for(int i=0; i < fs.length;i++) {
				File newF = new File(directory+"/"+fs[i]);
				Map<String,Object>reads = new HashMap<String,Object>();
				if(!newF.isDirectory()){
					String fileContent = FileUtils.readFileToString(newF);
					
					FileReader input = new FileReader(newF.getAbsolutePath());
					BufferedReader reader = new BufferedReader(input);
					String name=newF.getName();
					reads.put(name, reader);
					obMap.put(f.getName(),fileContent);
				}
				else{
					if(newF.getName().contains(".svn"))
						continue;
					Map<String,Object> newObs=null;
					if(obMap.containsKey(f.getName()))
						newObs=(Map<String, Object>) obMap.get(f.getName());
					else
						newObs=new HashMap<String,Object>();
					obMap.put(newF.getName(), newObs);
					readDirectory(directory+"/"+newF.getAbsolutePath(),r+1);
				}
			}
		}
		contents.put(f.getName(), obMap);
	}
	
	public void outputDirectory(Map<String,Object> initialMap,String directory,String outputDirectory) throws IOException{
		Object ob1 = initialMap.get(directory);
		if(ob1 instanceof Map) {
			Map objMap = (Map<String,Object>)ob1;
			Iterator<String>ic = objMap.keySet().iterator();
		
			while(ic.hasNext()){
				String key = ic.next();
				Object obj = objMap.get(key);
				if(obj instanceof String){
					String reader = (String)obj;
					createFile(reader,outputDirectory,key);
				}
				else{
					Map<String,Object> newObjs = (Map<String,Object>)obj;
					Iterator<String>id = newObjs.keySet().iterator();
					while(id.hasNext()){
						String s = id.next();
						outputDirectory(newObjs,directory+"/"+s,outputDirectory);
					}
				}
			}
		}
		else{
			String reader = (String)ob1;
			createFile(reader,directory,outputDirectory);
		}
	}
	
	public void createFile(String reader,String outputDirectory,String key) throws IOException{
		File file = new File(outputDirectory+"/"+key);
		FileUtils.writeStringToFile(file, reader);
	}

	/**
	 * Main to test the directory walker for user folders
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		RSDirectoryWalker walker = new RSDirectoryWalker(new File("../repast.simphony.demo.predatorprey/predator_prey_batch"));
		walker.startWalking();
		Directory directory=walker.getDirectory();
		File outputF = new File("/Users/markaltaweel/Desktop/");
		directory.output(outputF);
	}
}
