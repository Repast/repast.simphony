package repast.simphony.ui;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.parameter.ConstantSetter;
import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.Schema;
import repast.simphony.ui.probe.Probe;

/**
 * Manages gui parameters such that parameters can be changed in the gui
 * and then reset back to some set of default parameters.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class GUIParametersManager implements ParameterSetter {

  private Parameters params;
  //private List<ParameterSetter> defaultSetters = new ArrayList<ParameterSetter>();
  private Probe probe;
  //private ParameterSetter seedSetter = new RandomSeedSetter();

  public GUIParametersManager(Parameters params, Probe probe) {
    this.probe = probe;
    this.params = params;
    //defaultSetters = createSetters(params);
  }

  /**
   * Returns false.
   *
   * @return false
   */
  public boolean atEnd() {
    return false;
  }

  /**
   * Sets the parameters to the next set of values. In this case,
   * that will be the values from the GUI.
   *
   * @param params
   */
  public void next(Parameters params) {
    // commiting the probe sets the values directly into
    // the params object.
    if (probe != null) probe.commit();
  }

  /**
   * Resets the parameters to the default values passed in on the constructor.
   *
   * @param params
   */
  public void reset(Parameters params) {
    for (String name : params.getSchema().parameterNames()) {
      ParameterSchema schema = params.getSchema().getDetails(name);
      Object defVal = schema.getDefaultValue();
      if (name.equals(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME) && defVal.equals(Parameters.NULL)) {
        int val = (int) System.currentTimeMillis();
        // per JIRA 76 - "Use positive default random seeds"
        if (val < 0) val = Math.abs(val);
        params.setValue(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME, val);
      } else {
        if (!defVal.equals(Parameters.NULL)) {
          params.setValue(name, defVal);
        }
      }
    }
  }

  /**
   * Resets the parameters managed by this GUIParametersManager back to their default
   * values. The default values are whatever values the Parameters object held
   * when it was passed in the constructor.
   */
  public void reset() {
    reset(params);
  }


  /**
   * Creates a list of ParameterSetters from the specified parameters. This
   * does not create a setter for the random seed.
   *
   * @param params
   */
  private List<ParameterSetter> createSetters(Parameters params) {
    List<ParameterSetter> setters = new ArrayList<ParameterSetter>();
    Schema schema = params.getSchema();
    for (String name : schema.parameterNames()) {
      if (!name.equals(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME)) {
        ConstantSetter setter = new ConstantSetter(name, params.getValue(name));
        setters.add(setter);
      }
    }
    return setters;
  }

  /**
   * Gets the parameters object managed by this GUIParametersManager.
   *
   * @return the parameters object managed by this GUIParametersManager.
   */
  public Parameters getParameters() {
    return params;
	}
}
