package repast.simphony.relogo.ast;

import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.ConstantExpression


public class AstUtil {
	
	public static Expression getExpressionForMember(AnnotationNode annot, String memberName){
		Expression intervalExpression = annot.getMember(memberName);
		if (intervalExpression instanceof ConstantExpression){
			return (ConstantExpression) intervalExpression;
		}
		return null;
	}

}
