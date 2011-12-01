package repast.simphony.ui.probe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Annotation that marks a particular method as producing a "probe id". This id
 * will be used as the title of a probe. The annotated method must have a return value
 * and take zero parameters.
 * 
 * @author Nick Collier
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProbeID {}
