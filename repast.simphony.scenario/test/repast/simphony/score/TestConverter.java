package repast.simphony.score;

import repast.simphony.parameter.StringConverter;

/**
 * Dummy converter used to test converter creation in 
 * Attributes.
 * 
 * @author Nick Collier
 */
public class TestConverter implements StringConverter<TestAttributeType> {

  /* (non-Javadoc)
   * @see repast.simphony.parameter.StringConverter#fromString(java.lang.String)
   */
  public TestAttributeType fromString(String strRep) {
    return new TestAttributeType(strRep);
  }

  /* (non-Javadoc)
   * @see repast.simphony.parameter.StringConverter#toString(java.lang.Object)
   */
  public String toString(TestAttributeType obj) {
    return obj.getVal();
  }
}
