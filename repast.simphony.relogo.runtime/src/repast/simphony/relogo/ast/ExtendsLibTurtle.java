package repast.simphony.relogo.ast;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ExtendsLibTurtle {
	//Fully qualified classname of the turtle being extended
	String value();
}
