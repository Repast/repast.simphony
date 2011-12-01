package repast.simphony.scenario.data;

import repast.simphony.parameter.StringConverter;

class DefaultAttribute implements Attribute {
  
  String name;
  String value;
  String id;
  Class<?> type;
  StringConverter converter;
  
  /* (non-Javadoc)
   * @see repast.simphony.score.Attribute#getConverter()
   */
  public StringConverter getConverter() {
    return converter;
  }
  /* (non-Javadoc)
   * @see repast.simphony.score.Attribute#getDefaultValue()
   */
  public String getValue() {
    return value;
  }
  /* (non-Javadoc)
   * @see repast.simphony.score.Attribute#getDisplayName()
   */
  public String getDisplayName() {
    return name;
  }
  /* (non-Javadoc)
   * @see repast.simphony.score.Attribute#getId()
   */
  public String getId() {
    return id;
  }
  /* (non-Javadoc)
   * @see repast.simphony.score.Attribute#getType()
   */
  public Class<?> getType() {
    return type;
  }
}