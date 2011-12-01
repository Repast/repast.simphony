package repast.simphony.parameter.bsf;

import junit.framework.TestCase;
import org.apache.bsf.BSFException;
import repast.simphony.parameter.Parameters;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BSFTest extends TestCase {

	public void testBSF() throws BSFException, IOException {
		URL url = getClass().getResource("params.bsh");
		File f = new File(url.getFile());
		ScriptRunner runner = new ScriptRunner(f);
		runner.init(null, null);
		Parameters params = runner.getParameters();
		assertEquals(3, params.getValue("int_param"));
		assertEquals("cormac", params.getValue("a_list"));
	}
}
