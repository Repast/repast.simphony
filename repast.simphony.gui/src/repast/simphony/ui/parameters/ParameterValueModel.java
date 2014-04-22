/**
 * 
 */
package repast.simphony.ui.parameters;

import repast.simphony.parameter.Parameters;

import com.jgoodies.binding.value.AbstractValueModel;

/**
 * Bingings value model for a single Parameter.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ParameterValueModel extends AbstractValueModel {
  
  private String paramName;
  private Parameters params;
  private boolean isFloat = false;
  
  ParameterValueModel(String paramName, Parameters params) {
    this.paramName = paramName;
    this.params = params;
    Class<?> type = params.getSchema().getDetails(paramName).getType();
    isFloat = type.equals(float.class) || type.equals(Float.class);
  }

  /* (non-Javadoc)
   * @see com.jgoodies.binding.value.ValueModel#getValue()
   */
  @Override
  public Object getValue() {
    return params.getValue(paramName);
  }

  /* (non-Javadoc)
   * @see com.jgoodies.binding.value.ValueModel#setValue(java.lang.Object)
   */
  @Override
  public void setValue(Object val) {
    //System.out.println("setting " + paramName + " to " + val);
    Object oldVal = params.getValue(paramName);
    if (val == null) val = oldVal;
    // this is necessary because number format returns a
    // double from its string parse, and we need to cast that
    // to a float.
    if (Number.class.isAssignableFrom(val.getClass()) && isFloat) {
      val = ((Number)val).floatValue();
    }
    params.setValue(paramName, val);
    fireValueChange(oldVal, val);
  }
}
