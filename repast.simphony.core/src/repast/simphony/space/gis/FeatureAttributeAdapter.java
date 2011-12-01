package repast.simphony.space.gis;

import org.geotools.feature.IllegalAttributeException;

/**
 * Adapts a method call or combination of method calls on an object
 * into a feature attribute lookup.
 *
 * @author Nick Collier
 */
public interface FeatureAttributeAdapter<T> {

  /**
   * Gets the name of the attribute.
   *
   * @return the name of the attribute.
   */
  String getAttributeName();

  /**
   * Gets the value of the attribute.
   *
   * @param adaptee the object to get the attribute value from
   *
   * @return the value of the attribute.
   */
  Object getAttribute(T adaptee);

  /**
   * Sets the value of the attribute.
   *
   * @param adaptee the object on which to set the attribute value
   * @param val the attribute value
   *
   * @throws IllegalAttributeException if there is an  error while
   * setting the attribute
   */
  void setAttribute(T adaptee, Object val) throws IllegalAttributeException;

  /**
   * Gets the attribute type.
   *
   * @return the attribute type.
   */
  Class<?> getAttributeType();

}
