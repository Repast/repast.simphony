package repast.simphony.parameter.groovy;

import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.engine.environment.RunState;

/**
 * Extendable parameter setter to be used with the groovy
 * based parameter builder. In addition to setting parameter's values
 * this class is also responsible for creating the parameters that it
 * manages. The RunState for the next run will be set prior to each run
 * and implementations can thus use this RunState in their <code>next</code>
 * etc. code.
 *
 * @author Nick Collier
 */
public abstract class CustomParameterSetter implements ParameterSetter {

  protected String name;
  protected Class type;
  protected RunState runState;


  /**
   * Construct a CustomParameterSetter.
   *
   * @param name the name of the parameter this setter will set
   * @param parameterType the type of the parameter this setter will set
   */
  protected CustomParameterSetter(String name, Class parameterType) {
    this.name = name;
    this.type = parameterType;
  }

  /**
   * Initializes this CustomParameterSetter with the RunState for the
   * next run.
   *
   * @param state the RunState for the next run.
   */
  public void init(RunState state) {
    this.runState = state;
  }

  /**
   * Gets the name of the parameter this setter will set.
   *
   * @return the name of the parameter this setter will set.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the class of the parameter this setter will set.
   *
   * @return the class of the parameter this setter will set.
   */
  public Class getParameterType() {
    return type;
  }


  /**
   * Adds the parameters that this class is responsible for to
   * the ParametersCreator factory. For example, a CustomParameterSetter
   * will create the parameter that is directly responsible for setting
   * (the one specified by the name property) and may create a derived
   * parameter that is also set by this CustomParameterSetter.
   *
   * @param creator the factory used to create the parameters that
   * this class manages
   */
  public abstract void addParameters(ParametersCreator creator);
}
