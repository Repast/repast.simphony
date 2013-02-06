package repast.simphony.ui.probe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an accessor type method as a Property that should show up when an
 * instance of the class containing it is probed.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ProbeProperty {

  /**
   * The proper natural language name for this proeprty. For example,
   * "Simple Agent Count" rather than something like "simpleAgentCount."
   * 
   * @return proper natural language name for this property.
   */
  String displayName() default "";

  /**
   * Gets java bean style property name for this property. For example, if this
   * annotates a "getFoo" method, then the property name is "foo."
   * 
   * @return java bean style property name for this property.
   */
  String usageName();

  /**
   * Gets the fully qualififed name of class used to convert the return value of
   * the annotated method to and from a String representation. The class must
   * implement repast.simphony.parameter.StringConvertor. This is only necessary
   * if the return type is not a String, a primitive value, or one of the Object
   * representatons of a primitive (e.g. an Integer, or Boolean).
   * 
   * @return Gets the convertor used to convert the return value of the
   *         annotated method to and from a String representation.
   */
  String converter() default "";

}
