/**
 * 
 */
package repast.simphony.statecharts.sheets;

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * @author Nick Collier
 */
public class DoubleToStringConverter implements IConverter {

  /* (non-Javadoc)
   * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
   */
  @Override
  public Object getFromType() {
    return Double.class;
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
   */
  @Override
  public Object getToType() {
    return String.class;
  }

  /* (non-Javadoc)
   * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
   */
  @Override
  public Object convert(Object fromObject) {
    return fromObject.toString();
    
  }
}
