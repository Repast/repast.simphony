package repast.simphony.scenario;


import java.io.File;

import junit.framework.TestCase;

public class ScenarioUtilsTest extends TestCase {

	public void testCovertWindowsFilePath() {
		
		String scenarioPath = "C:\\Users\\Eric\\model.rs";
		String filePath = "C:\\Users\\Eric\\data\\myfile.txt";
		
		File scenarioFile = new File(scenarioPath);
		File myFile = new File(filePath);
		
		ScenarioUtils.setScenarioDir(scenarioFile);
		
		String relativePath = ScenarioUtils.makeRelativePathToProject(myFile.getAbsolutePath());
		
		String expectedRelativePath = "./data/myfile.txt";
		assertEquals(expectedRelativePath, relativePath);
		
	}
	
	public void testCovertUnixFilePath() {
		
		String scenarioPath = "/Home/Users/Eric/model.rs";
		String filePath = "/Home/Users/Eric/data/myfile.txt";
		
		File scenarioFile = new File(scenarioPath);
		File myFile = new File(filePath);
		
		ScenarioUtils.setScenarioDir(scenarioFile);
		
		String relativePath = ScenarioUtils.makeRelativePathToProject(myFile.getAbsolutePath());
		
		String expectedRelativePath = "./data/myfile.txt";
		assertEquals(expectedRelativePath, relativePath);
	}
}
