package repast.simphony.scenario;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Provides access to the scenario directory.
 * 
 * @author Eric Tatara
 *
 */
public class ScenarioUtils {

	private static File scenarioDir;

	public static File getScenarioDir() {
		return scenarioDir;
	}

	public static void setScenarioDir(File dir) {
		scenarioDir = dir;
	}
	
  /**
   * Checks if the selected path contains the project root and if so, remove
   * the project root from the path, making it a relative path using UNIX
   * style forward slash file separators.
   * 
   * @param fileName the full path to the file
   * @return the relative path to the project root
   */
  
  public static String makeRelativePathToProject(String filePath){
  
  	File file = null;
  	File scenarioDir = ScenarioUtils.getScenarioDir();

  	// Trim the actual .rs folder so we're left with the model root folder
  	String modelFolder = scenarioDir.getAbsolutePath().replace(scenarioDir.getName(), "");

  	if (filePath.contains(modelFolder)){
  		filePath = filePath.replace(modelFolder, "./");	
  		file = new File (filePath);
  	}  

  	// Use Apache commons io lib to force serialized filename to unix
  	filePath = FilenameUtils.separatorsToUnix(file.getPath());
  	
  	return filePath;  
  }
}
