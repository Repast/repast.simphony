package repast.simphony.relogo.ast;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target 

import org.codehaus.groovy.transform.GroovyASTTransformationClass

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.FIELD])
@GroovyASTTransformationClass(["repast.simphony.relogo.ast.DiffusibleASTTransformation"])
public @interface Diffusible {
	String[] value () default [];
}
