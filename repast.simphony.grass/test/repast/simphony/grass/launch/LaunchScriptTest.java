package repast.simphony.grass.launch;

import junit.framework.TestCase;

/**
 * Test class for sending arguments to Grass.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */
public class LaunchScriptTest extends TestCase {

	/**Test command to pass to grass*/
	private String testString;

	/**
	 * Setup method for test.
	 * @param testScript a test script path to the test argument
	 */
	public void setUp(String testScript){
		testString=testScript;
	}
	
	/**
	 * Test script launch (e.g., grass.sh script). 
	 * Other scripts could be launched in addition to grass.sh.
	 */
	@SuppressWarnings("unchecked")
	public void testLaunch(){
		setUp("/Applications/GRASS-6.3.app/Contents/MacOS/grass.sh");
		LaunchScript ls = new LaunchScript(testString);
		ls.launch();
	}
	
	/**
	 * Another test script launch, similar to testLaunch().
	 */
	@SuppressWarnings("unchecked")
	public void testLaunch2(){
		setUp("../repast.simphony.grass/testScripts/grass_env.sh");
		LaunchScript ls = new LaunchScript(testString);
		ls.launch();
		
		ls.setCommandArgument("../repast.simphony.grass/testScripts/rinfo_test.sh");
		ls.launch();
	}
}
