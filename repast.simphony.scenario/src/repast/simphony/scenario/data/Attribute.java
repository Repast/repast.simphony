package repast.simphony.scenario.data;

import repast.simphony.parameter.StringConverter;

/**
 * Encapsulates a ContextData heirarchy attribute.
 */
public interface Attribute {
  
  /**
   * Gets the name of this attribute.
   * 
   * @return the attribute's name.
   */
  String getDisplayName();

  /**
   * Gets the attribute's value as a String.
   * 
   * @return the attribute's value
   */
  String getValue();
  
  /**
   * Gets this attribute's id.
   * 
   * @return this attribute's id.
   */
  String getId();
  
  /**
   * Gets this attribute's StringConverter.
   * 
   * @return this attribute's StringConverter.
   */
  @SuppressWarnings("rawtypes")
  StringConverter getConverter();
  
  /**
   * Gets the type of this attribute.
   * 
   * @return the type of this attribute.
   */
  Class<?> getType();
}
