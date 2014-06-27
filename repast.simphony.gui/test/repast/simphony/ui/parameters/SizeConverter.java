/**
 * 
 */
package repast.simphony.ui.parameters;

import repast.simphony.parameter.StringConverter;

/**
 * @author Nick Collier
 */
public class SizeConverter implements StringConverter<Size>{
  
  @Override
  public String toString(Size obj) {
    return obj.toString();
  }

  @Override
  public Size fromString(String strRep) {
    if (strRep.equals("BIG")) return Size.BIG;
    else if (strRep.equals("SMALL")) return Size.SMALL;
    return null;
  }

}
