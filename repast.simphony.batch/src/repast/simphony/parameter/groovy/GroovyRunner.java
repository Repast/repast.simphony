package repast.simphony.parameter.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import repast.simphony.context.Context;
import repast.simphony.engine.controller.NullAbstractControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.ParameterTreeSweeper;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.SweeperProducer;

import java.io.File;
import java.io.IOException;

/**
 * Runs a groovy file to setup a parameter sweep.
 *
 * @author Nick Collier
 */
public class GroovyRunner implements SweeperProducer {

  private File script;
  private ParameterBuilder builder;

  public GroovyRunner(File script) {
    this.script = script;
  }

  /**
   * Gets the ParameterTreeSweeper produced by this SweeperProducer.
   *
   * @return the ParameterTreeSweeper produced by this SweeperProducer.
   */
  public ParameterTreeSweeper getParameterSweeper() throws IOException {
    if (builder == null) run();
    return builder.getSweeper();
  }

  private void run() throws IOException {
    try {
      builder = new ParameterBuilder();
      Binding binding = new Binding();
      binding.setVariable("builder", builder);
      GroovyShell shell = new GroovyShell(binding);
      shell.evaluate(script);
    } catch (Exception ex) {
      IOException exception = new IOException(ex.getMessage());
      exception.initCause(ex);
      throw exception;
    }
  }

  /**
   * Gets the Parameters produced by this SweeperProducer.
   *
   * @return the ParameterTreeSweeper produced by this SweeperProducer.
   */
  public Parameters getParameters() throws IOException {
    if (builder == null) run();
    return builder.getParameters();
  }

  /**
   * Initializes the producer with the controller registry and
   * master context id. An action will be added to the registry. This
   * action sets the RunState on any CustomParameterSetters found in
   * the sweeper.
   *
   * @param registry
   * @param masterContextId
   */
  public void init(ControllerRegistry registry, Object masterContextId) {
    if (registry != null) {
      registry.addAction(masterContextId, null, new NullAbstractControllerAction() {

        @Override
        public void runInitialize(RunState runState, Context context, Parameters runParams) {

          try {
            ParameterTreeSweeper sweeper = getParameterSweeper();
            initCustomParameters(runState, sweeper, sweeper.getRootParameterSetter());

          } catch (IOException e) {
            // this should be ok, because file should
            // have been parsed by now.
            e.printStackTrace();
          }
        }

        private void initCustomParameters(RunState state, ParameterTreeSweeper sweeper, ParameterSetter setter) {
          if (setter instanceof CustomParameterSetter) ((CustomParameterSetter) setter).init(state);
          for (ParameterSetter child : sweeper.getChildren(setter)) {
            initCustomParameters(state, sweeper, child);
          }
        }
      });
    }

  }
}
