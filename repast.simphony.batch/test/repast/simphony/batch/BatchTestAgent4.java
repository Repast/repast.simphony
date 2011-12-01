package repast.simphony.batch;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;

import java.util.List;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BatchTestAgent4 {

  private List<Object[]> results;

  public BatchTestAgent4(List<Object[]> results) {
    this.results = results;
  }

  @ScheduledMethod(start = 1, pick = 1)
  public void step() {
    Parameters params = RunEnvironment.getInstance().getParameters();
    if (BatchTest.OPT) {
      Object[] array = new Object[2];
      array[0] = params.getValue("x");
      array[1] = params.getValue("y");
      results.add(array);
    } else {
      Object[] array = new Object[9];
      array[0] = params.getValue("double_const");
      array[1] = params.getValue("long_const");
      array[2] = params.getValue("string_const");
      array[3] = params.getValue("boolean_const");
      array[4] = params.getValue("long_param");
      array[5] = params.getValue("float_param");
      array[6] = params.getValue("string_param");
      array[7] = params.getValue(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME);
      array[8] = RandomHelper.getSeed();
      results.add(array);
    }
  }
}
