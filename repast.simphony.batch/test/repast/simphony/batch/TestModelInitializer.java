package repast.simphony.batch;

import repast.simphony.context.Context;
import repast.simphony.engine.controller.NullAbstractControllerAction;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.scenario.ModelInitializer;
import repast.simphony.scenario.Scenario;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class TestModelInitializer implements ModelInitializer {

  public void initialize(Scenario scen, RunEnvironmentBuilder builder) {
    String id = scen.getContext().getId();
    if (id.equals("Batch Test Context 2")) {
      scen.addMasterControllerAction(new NullAbstractControllerAction() {
        @Override
        public void runInitialize(RunState runState, final Context context, Parameters params) {
          RunEnvironment.getInstance().endAt(BatchTest.END_AT);
          RunEnvironment.getInstance().getCurrentSchedule().schedule(
              ScheduleParameters.createRepeating(1, 1), new IAction() {
                public void execute() {
                  for (Object agent : context) {
                    ((BatchTestAgent2) agent).step();
                  }
                }
              });
        }

        ;
      });
    } else if (id.equals("Batch Test Context 3")) {
      scen.addMasterControllerAction(new NullAbstractControllerAction() {
        @Override
        public void runInitialize(RunState runState, final Context context, Parameters params) {
          RunEnvironment.getInstance().endAt(BatchTest.END_AT);
          RunEnvironment.getInstance().getCurrentSchedule().schedule(
              ScheduleParameters.createOneTime(1, 1), new IAction() {
                public void execute() {
                  for (Object agent : context) {
                    BatchTestAgent3 a = (BatchTestAgent3) agent;
                    if (a.id.equals("0")) {
                      a.start();
                    }
                  }
                }
              });
        }

        ;
      });
    }
  }
}
