package repast.simphony.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyAnnot {
	String displayName();

	String getMethod() default "";

	String setMethod() default "";

	boolean readOnly() default false;
}
