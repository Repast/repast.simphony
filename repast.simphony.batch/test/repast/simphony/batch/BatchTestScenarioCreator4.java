package repast.simphony.batch;

import repast.simphony.dataLoader.engine.ClassNameContextBuilder;
import repast.simphony.scenario.data.ContextData;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BatchTestScenarioCreator4 implements BatchScenarioCreator {

  /**
   * Creates a BatchScenario suitable for running by the BatchRunner.
   * 
   * @return a BatchScenario suitable for running by the BatchRunner.
   */
  public BatchScenario createScenario() {
    BatchScenario scenario = new BatchScenario("Batch Test Context 4");
    try {
      ContextData data = scenario.getContext();
      data.addAgent("repast.simphony.batch.BatchTestAgent4");
      scenario.addDataLoader("Batch Test Context 4", new ClassNameContextBuilder(
          "repast.simphony.batch.BatchTestContextCreator4"));
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
