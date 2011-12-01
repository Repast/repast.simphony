package repast.simphony.parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a get or set accessor method as a Parameter.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {

	/**
	 * The proper natural language name for this parameter. For example,
	 * "Simple Agent Count" rather than something like "simpleAgentCount."
	 *
	 * @return proper natural language name for this parameter.
	 */
	String displayName();

	/**
	 * Gets java bean style property name for this parameter. For example,
	 * if this annotates a "getFoo" method, then the property
	 * name is "foo."
	 *
	 * @return java bean style property name for this parameter.
	 */
	String usageName();

  /**
   * Gets the default value of this Parameter.
   *
   * @return the default value of this Parameter.
   */
  String defaultValue() default "";

  /**
   * Gets the fully qualififed name of class used to convert the return value of the annotated
   * method to and from a String representation. The class must implement repast.simphony.parameter.StringConvertor.
   * This is only necessary if the return type is not a String, a primitive value, or one of the
   * Object representatons of a primitive (e.g. an Integer, or Boolean).
   *
   * @return  Gets the convertor used to convert the return value of the annotated
   * method to and from a String representation.
   */
  String converter() default "";

}
