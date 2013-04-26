package repast.simphony.statecharts.generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tags a statechart generated class with a statecharts uuid.
 *   
 * @author Nick Collier
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface GeneratedFor {
//Fully qualified classname responsible for generated method
  String value();
}
