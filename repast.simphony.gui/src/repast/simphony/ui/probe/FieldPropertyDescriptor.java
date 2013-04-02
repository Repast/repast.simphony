/**
 * 
 */
package repast.simphony.ui.probe;

import java.lang.reflect.Field;

import repast.simphony.parameter.StringConverter;

/**
 * Describes a property whose source is field.
 * 
 * @author Nick Collier
 */
public class FieldPropertyDescriptor {
  
  private Field field;
  private String displayName;
  private String name;
  
  private StringConverter<?> converter = null;
  
  public FieldPropertyDescriptor(Field field, String name) {
    this.field = field;
    this.name = name;
  }

  /**
   * Gets the display name for this field property. 
   * 
   * @return the display name for this field property. 
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets the display name for this field property. 
   * 
   * @param displayName the display name for this field property. 
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Gets the field for this field property.
   * 
   * @return the field for this field property.
   */
  public Field getField() {
    return field;
  }

  /**
   * Gets the name of this field property.
   * 
   * @return the name of this field property.
   */
  public String getName() {
    return name;
  }
  
  /**
   * Sets the StringConverter.
   * 
   * @param converter
   */
  public void setStringConverter(StringConverter<?> converter) {
    this.converter = converter;
  }
  
  /**
   * Gets the StringConverter, if any.
   * @return
   */
  public StringConverter<?> getStringConverter() {
    return converter;
  }
  
  
}
