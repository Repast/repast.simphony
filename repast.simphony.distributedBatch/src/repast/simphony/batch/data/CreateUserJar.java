package repast.simphony.batch.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Level;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import simphony.util.messages.MessageCenter;


/**
 * Jar creation class.
 * @version $Revision: 2.0
 * @author Mark Altaweel
 */
public class CreateUserJar {
	
	  /**String array for storing the location information for a particular path*/
	  private String[] location;

	  /**
	   * Method to create a jar for the path to a folder.
	   * @param path the path to a folder such as the user's project folder.
	   * @throws IOException
	   * @throws InterruptedException 
	   */
	public void createJar(String path) throws IOException, InterruptedException {
		  location = path.split(File.separator);
		  String n = location[location.length-1];
		  File newF = new File(path);
		  File newF2 = new File(path+File.separator+"transferFiles");
		  if(!newF.exists() || !newF2.exists()){
			 MessageCenter.getMessageCenter(CreateUserJar.class).fireMessageEvent(Level.INFO,
					 "Can't find jar file or transferFolder in user project path",new FileNotFoundException(),"File Not Found");
			  return;
		  }
		  String arg = "jar cf "+path+File.separator+"transferFiles"+File.separator+
		  	n+".jar"+" "+path+ " *";
		  
		  System.out.println(arg);
	//	  String arg2 ="jar -xf ./transferFiles/"+location[location.length-1]+".jar"; 	  
		  Process p=Runtime.getRuntime().exec(arg);
		  p.waitFor();  
	  }
	
	/**
	 * Method to create a user's jar file for distributed batch processes
	 * using an ant script (in repast.simphony.distributedBatch/build.xml).
	 * 
	 * @param data the xml user input from XML_Launch_Input.xml
	 */
	public void createJarFromAnt(XMLInputData data){
		//Original Code
//		String[] projectPath=data.getProjectPath().split(File.separator);
		//Original Code End

		
		File f = new File(data.getProjectPath());
		String path = f.getParent();
		
		String dir = System.getProperty("user.dir");

		URL runtimeSource = repast.simphony.runtime.RepastMain.class
		.getProtectionDomain().getCodeSource().getLocation();

		String pathB = runtimeSource.getFile().replaceAll("%20", " ");
		
		pathB = pathB+".."+File.separator+".."+File.separator+"repast.simphony.distributedBatch"+File.separator;

		//reference the ant file
		File buildFile = new File(pathB+"build.xml");
		Project p = new Project();
		p.setUserProperty("ant.file", buildFile.getAbsolutePath());		
		
		//set the log display and info. stream
		DefaultLogger consoleLogger = new DefaultLogger();
		consoleLogger.setErrorPrintStream(System.err);
		consoleLogger.setOutputPrintStream(System.out);
		consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
		p.addBuildListener(consoleLogger);
		
//		//Original Code
//		String limit="";
//		if(projectPath.length>1)
//			limit=projectPath[projectPath.length-2];
//		else
//			limit=projectPath[projectPath.length-1];
//		
//		String path=data.getProjectPath().split(limit)[0].concat(limit);
//		//Original Code End
		
		
		//set the properties (workspace and project)
		p.setProperty("env.WORKSPACE", path);
		p.setProperty("env.PROJECT", data.getName());
		p.setProperty("env.NAME", data.getName());
		
		try {
			p.fireBuildStarted();
			p.init();
			ProjectHelper helper = ProjectHelper.getProjectHelper();
			p.addReference("ant.projectHelper", helper);
			helper.parse(p, buildFile);
			p.executeTarget(p.getDefaultTarget());
			p.fireBuildFinished(null);
		} catch (BuildException e) {
			p.fireBuildFinished(e);
		}
	}
}
