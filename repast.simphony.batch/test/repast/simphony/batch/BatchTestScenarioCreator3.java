package repast.simphony.batch;

import java.io.File;

import repast.simphony.dataLoader.engine.ClassNameContextBuilder;
import repast.simphony.scenario.data.ContextData;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BatchTestScenarioCreator3 implements BatchScenarioCreator {

  /**
   * Creates a BatchScenario suitable for running by the BatchRunner.
   * 
   * @return a BatchScenario suitable for running by the BatchRunner.
   */
  public BatchScenario createScenario() {
    BatchScenario scenario = new BatchScenario("Batch Test Context 3");
    try {
      // this prepare must come before the ClassNameDataLoader is created
      // otherwise we would have a duplicate definition of the watchee classes.
      scenario.prepareWatchee("repast.simphony.batch.BatchTestAgent3", "fire", new File("./bin").getAbsolutePath());
      ContextData data = scenario.getContext();
      data.addAgent("repast.simphony.batch.BatchTestAgent3");
      scenario.addDataLoader("Batch Test Context 3", new ClassNameContextBuilder(
          "repast.simphony.batch.BatchTestContextCreator3"));
      scenario.setUserInitializer(new TestModelInitializer());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    }

    return scenario;
  }
}
