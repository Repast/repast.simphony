package repast.simphony.batch;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;

import repast.simphony.runtime.RepastBatchMain;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BatchTest extends TestCase {

  public static final int NUM_AGENTS = 10;
  public static final int END_AT = 10;
  public static boolean OPT = false;

  public static Set results = new HashSet();
  public static List<Object[]> paramResults = new ArrayList<Object[]>();

  private String paramsPath;

  static {
    BasicConfigurator.configure();
  }

  public void setUp() {
    results.clear();
    paramResults.clear();
    URL url = this.getClass().getResource("test_batch_params.xml");
    File f = new File(url.getFile());
    paramsPath = f.getAbsolutePath();
    BatchTest.OPT = false;
  }

  // tests that we iterate more than one run
  // and that each agent executes correctly during
  // those runs
  public void testSimpleBatch() throws Exception {
    RepastBatchMain.main(new String[] { "-params", paramsPath,
        "repast.simphony.batch.BatchTestScenarioCreator" });
    // loop for each run, for now the number of runs is hard-coded at 2
    for (int i = 1; i < 3; i++) {
      for (int j = 0; j < NUM_AGENTS; j++) {
        for (double k = 1; k <= END_AT; k++) {
          String key = j + ":" + i + ":" + k;
          assertTrue(key, results.remove(key));
        }
      }
    }
    assertEquals(0, results.size());
  }

  // tests that we iterate runs correctly
  // where the stop condition and scheduling is done
  // in a ModelInitializer
  public void testModelInitBatch() throws Exception {
    BatchMain.main(new String[] { "-params", paramsPath,
        "repast.simphony.batch.BatchTestScenarioCreator2" });
    // loop for each run, for now the number of runs is hard-coded at 2
    for (int i = 1; i < 3; i++) {
      for (int j = 0; j < NUM_AGENTS; j++) {
        for (double k = 1; k <= END_AT; k++) {
          String key = j + ":" + i + ":" + k;
          assertTrue(key, results.remove(key));
        }
      }
    }
    assertEquals(0, results.size());
  }

  public void testWatcherBatch() throws Exception {
    BatchMain.main(new String[] { "-params", paramsPath,
        "repast.simphony.batch.BatchTestScenarioCreator3" });
    for (int i = 1; i < 3; i++) {
      for (int j = 0; j < NUM_AGENTS; j++) {
        String key = j + ":" + i + ":" + (j + 1.0);
        assertTrue(key, results.remove(key));
      }
    }
    assertEquals(0, results.size());
  }

  public void testBatchFromFile() throws Exception {
    // expects "." to be the repast.simphony.batch directory
    RepastBatchMain.main(new String[] { "-params", paramsPath, "./test/test_scenario.rs" });
    for (int i = 1; i < 3; i++) {
      for (int j = 0; j < NUM_AGENTS; j++) {
        String key = j + ":" + i + ":" + (j + 1.0);
        assertTrue(key, results.remove(key));
      }
    }
    assertEquals(0, results.size());
  }

  public void testBatchParamSweepXML() throws Exception {
    URL url = this.getClass().getResource("test_batch_params2.xml");
    File f = new File(url.getFile());
    paramsPath = f.getAbsolutePath();
    RepastBatchMain.main(new String[] { "-params", paramsPath,
        "repast.simphony.batch.BatchTestScenarioCreator4" });
    doParamsResultTest();
  }

  public void testBatchParamGroovy() throws Exception {
    // expects "." to be the repast.simphony.batch directory
    File f = new File("./test_scripts/test_batch_params2.groovy");
    paramsPath = f.getAbsolutePath();
    BatchMain.main(new String[] { "-params", paramsPath,
        "repast.simphony.batch.BatchTestScenarioCreator4" });
    doParamsResultTest();
  }

  public void testBatchParamCustomGroovy() throws Exception {
    // expects "." to be the repast.simphony.batch directory
    File f = new File("./test_scripts/test_batch_params3.groovy");
    paramsPath = f.getAbsolutePath();
    BatchMain.main(new String[] { "-params", paramsPath,
        "repast.simphony.batch.BatchTestScenarioCreator4" });
    doParamsResultTest();
  }

  public void testBatchParamSweepBSF() throws Exception {
    // expects "." to be the repast.simphony.batch directory
    File f = new File("./test/repast/simphony/batch/test_batch_params2.bsh");
    paramsPath = f.getAbsolutePath();
    BatchMain.main(new String[] { "-params", paramsPath,
        "repast.simphony.batch.BatchTestScenarioCreator4" });
    doParamsResultTest();
  }

  public void testOptParamSweepXML() throws Exception {
    URL url = this.getClass().getResource("test_opt.properties");
    File f = new File(url.getFile());
    paramsPath = f.getAbsolutePath();
    BatchTest.OPT = true;
    BatchMain.main(new String[] { "-opt", paramsPath,
        "repast.simphony.batch.BatchTestScenarioCreator4" });

    // assertEquals(41, paramResults.size());
    Object[] array = paramResults.get(paramResults.size() - 1);
    assertEquals(array[0], TestRunResultProducer.xCenter);
    assertEquals(array[1], TestRunResultProducer.yCenter);
  }

  public void testOptParamSweepBSH() throws Exception {
    URL url = this.getClass().getResource("test_opt_bsh.properties");
    File f = new File(url.getFile());
    paramsPath = f.getAbsolutePath();
    BatchTest.OPT = true;
    BatchMain.main(new String[] { "-opt", paramsPath,
        "repast.simphony.batch.BatchTestScenarioCreator4" });

    // assertEquals(41, paramResults.size());
    Object[] array = paramResults.get(paramResults.size() - 1);
    assertEquals(array[0], TestRunResultProducer.xCenter);
    assertEquals(array[1], TestRunResultProducer.yCenter);
  }

  private void doParamsResultTest() {
    /*
     * <sweep runs="2"> <parameter name="long_const" type="constant"
     * constant_type="number" value="11L"/> <parameter name="string_const"
     * type="constant" constant_type="string" value="hello cormac"/> <parameter
     * name="boolean_const" type="constant" constant_type="boolean"
     * value="false"/>
     * 
     * <parameter name="long_param" type="number" start="1L" end="3" step="1">
     * <parameter name="float_param" type="number" start=".8f" end="1.0f"
     * step=".1f"> <parameter name="string_param" type="list"
     * value_type="String" values="foo bar"> <parameter name="randomSeed"
     * type="list" value_type="int" values="1 2"/> </parameter> </parameter>
     * </parameter> </sweep>
     */

    // 36 combinations * 2 runs
    assertEquals(72, paramResults.size());
    Object[][] expected = {
        // extra number on the end is to test what RandomHelper.getSeed returns
        { 4.0, 11L, "hello cormac", false, 1L, .8f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 1L, .8f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 1L, .8f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 1L, .8f, "bar", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 1L, .9f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 1L, .9f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 1L, .9f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 1L, .9f, "bar", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 1L, 1.0f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 1L, 1.0f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 1L, 1.0f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 1L, 1.0f, "bar", 2, 2 },

        { 4.0, 11L, "hello cormac", false, 2L, .8f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 2L, .8f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 2L, .8f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 2L, .8f, "bar", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 2L, .9f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 2L, .9f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 2L, .9f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 2L, .9f, "bar", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 2L, 1.0f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 2L, 1.0f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 2L, 1.0f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 2L, 1.0f, "bar", 2, 2 },

        { 4.0, 11L, "hello cormac", false, 3L, .8f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 3L, .8f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 3L, .8f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 3L, .8f, "bar", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 3L, .9f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 3L, .9f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 3L, .9f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 3L, .9f, "bar", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 3L, 1.0f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 3L, 1.0f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 3L, 1.0f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 3L, 1.0f, "bar", 2, 2 },

        // second duplicate set for second run
        { 4.0, 11L, "hello cormac", false, 1L, .8f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 1L, .8f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 1L, .8f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 1L, .8f, "bar", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 1L, .9f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 1L, .9f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 1L, .9f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 1L, .9f, "bar", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 1L, 1.0f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 1L, 1.0f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 1L, 1.0f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 1L, 1.0f, "bar", 2, 2 },

        { 4.0, 11L, "hello cormac", false, 2L, .8f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 2L, .8f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 2L, .8f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 2L, .8f, "bar", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 2L, .9f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 2L, .9f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 2L, .9f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 2L, .9f, "bar", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 2L, 1.0f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 2L, 1.0f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 2L, 1.0f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 2L, 1.0f, "bar", 2, 2 },

        { 4.0, 11L, "hello cormac", false, 3L, .8f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 3L, .8f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 3L, .8f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 3L, .8f, "bar", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 3L, .9f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 3L, .9f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 3L, .9f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 3L, .9f, "bar", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 3L, 1.0f, "foo", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 3L, 1.0f, "foo", 2, 2 },
        { 4.0, 11L, "hello cormac", false, 3L, 1.0f, "bar", 1, 1 },
        { 4.0, 11L, "hello cormac", false, 3L, 1.0f, "bar", 2, 2 },

    };

    for (int i = 0; i < 72; i++) {
      Object[] array = paramResults.get(i);
      Object[] expArray = expected[i];
      assertEquals("double_const", (Double) expArray[0], (Double) array[0]);
      assertEquals("long_const", (Long) expArray[1], (Long) array[1]);
      assertEquals("string_const", (String) expArray[2], (String) array[2]);
      assertEquals("boolean_const", (Boolean) expArray[3], (Boolean) array[3]);
      assertEquals("long_param", (Long) expArray[4], (Long) array[4]);
      assertEquals("float_param", (Float) expArray[5], (Float) array[5], .0001f);
      assertEquals("string_param", (String) expArray[6], (String) array[6]);
      assertEquals("RandomSeed", (Integer) expArray[7], (Integer) array[7]);
      assertEquals("RandomHelper.getSeed", (Integer) expArray[8], (Integer) array[8]);
    }
  }

  public static junit.framework.Test suite() {
    TestSuite suite = new TestSuite(repast.simphony.batch.BatchTest.class);
    // TestSuite suite = new TestSuite();
    // suite.addTest(new WatcherTest("testDblFieldAnnotation"));
    return suite;
  }
}
