package repast.simphony.scenario;

import junit.framework.TestCase;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ScenarioCreatorTest extends TestCase {

	private ScenarioCreator creator;
	private File modelPath = new File("./plugins/repast.simphony.score.runtime/test/source/model.score");
	private File scenarioPath = new File("./plugins/repast.simphony.score.runtime/test/target.rs");

	static {
		try {
			Properties props = new Properties();
			props.put("resource.loader", "class");
			props.put("class.resource.loader.description", "Velocity Classpath Resource Loader");
			props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			props.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
			Velocity.init(props);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUp() {
		creator = new ScenarioCreator(modelPath, scenarioPath, "foo.bar.ModelInit", true);
		for (File file : scenarioPath.listFiles()) {
			file.delete();
		}
	}

	public void testCreator() {
		try {
			creator.createScenario();
			assertTrue(new File(scenarioPath, "model.score").exists());
			assertTrue(new File(scenarioPath, "scenario.xml").exists());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	public void testContext() throws IOException {
		//SContext context = ContextPersist.load("c:/src/repast.simphony/plugins/repast.simphony.test.models/subcontext_test.rs/model.score");
		//System.out.println("context = " + context);
	}
}
