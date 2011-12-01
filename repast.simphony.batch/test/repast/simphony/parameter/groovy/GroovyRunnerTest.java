package repast.simphony.parameter.groovy;

import junit.framework.TestCase;
import repast.simphony.parameter.Parameters;

import java.io.File;
import java.io.IOException;

/**
 * @author Nick Collier
 */
public class GroovyRunnerTest extends TestCase {

  public void testValidGroovyRunner() {
    // assumes "." is repast.simphony.batch
    File file = new File("./test_scripts/params.groovy");
    GroovyRunner runner = new GroovyRunner(file);
    try {
      Parameters params = runner.getParameters();
      assertEquals(new Integer(2), params.getValue("const_1"));
      assertEquals(new Integer(1), params.getValue("num_1"));
      assertEquals(3, runner.getParameterSweeper().getRunCount());
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  public void testInvalidGroovyRunner() {
    // assumes "." is repast.simphony.batch
    File file = new File("./test_scripts/bad_params.groovy");
    GroovyRunner runner = new GroovyRunner(file);
    try {
      runner.getParameters();
      fail("file eval should throw exception");
    } catch (IOException e) {
      assertTrue(true);
    }
  }

  public void testCustomParam() {
    // assumes "." is repast.simphony.batch
    File file = new File("./test_scripts/custom.groovy");
    GroovyRunner runner = new GroovyRunner(file);
    try {
      Parameters params = runner.getParameters();
      assertEquals(new Integer(2), params.getValue("num_1"));
      assertEquals(3, runner.getParameterSweeper().getRunCount());
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }
}
