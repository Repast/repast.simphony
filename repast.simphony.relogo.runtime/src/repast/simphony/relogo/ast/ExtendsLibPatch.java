package repast.simphony.relogo.ast;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ExtendsLibPatch {
	//Fully qualified classname of the patch being extended
	String value();

}
