/**
 * 
 */
package repast.simphony.ui.parameters;

import repast.simphony.parameter.Parameters;

/**
 * ParameterBinder for the random seed.
 * 
 * @author Nick Collier
 */
public class RandomSeedParameterBinder extends DefaultParameterBinder {

  public RandomSeedParameterBinder(String name, String displayName) {
    super(name, displayName, int.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.ParameterBinder#fromParameter(repast.simphony.parameter
   * .Parameters)
   */
  @Override
  public void resetToDefault() {
    Object defaultValue = params.getSchema().getDetails(getName()).getDefaultValue();
    int seed = 0;
    if (defaultValue.equals(Parameters.NULL)) {
      seed = (int) System.currentTimeMillis();
      if (seed < 0)
        seed = Math.abs(seed);
    } else {
      seed = ((Integer) defaultValue).intValue();
    }
    field.setText(String.valueOf(seed));
    toParameter();
  }

  public String toXML() {
    return String.format("<parameter name = \"%s\" displayName = \"%s\" type=\"int\" "
        + "defaultValue=\"__NULL__\" isReadOnly=\"false\"  />", name, displayName);
  }
}
