package repast.simphony.relogo.ast;

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import repast.simphony.engine.schedule.ScheduledMethod


@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
class GoASTTransformation implements ASTTransformation {

	private static final ClassNode SCHEDULED_METHOD = ClassHelper.make(ScheduledMethod.class);
	private static final String START = "start";
	private static final double START_VALUE = 1d;
	private static final String INTERVAL = "interval";
	private static final double INTERVAL_VALUE = 1d;
	private static final String[] NON_DEFAULT_ANNOTATION_MEMBERS = ["priority", "duration", "shuffle", "pick"]; 

	public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {

		AnnotationNode annot = (AnnotationNode) nodes[0]
		AnnotatedNode parent = (AnnotatedNode) nodes[1]
		if (parent instanceof MethodNode) {
			MethodNode methodNode = (MethodNode) parent;
			AnnotationNode scheduledMethod = new AnnotationNode(SCHEDULED_METHOD);
			if (scheduledMethod != null){
				// start default is START_VALUE
				Expression startExpression = AstUtil.getExpressionForMember(annot, START);
				if (startExpression == null){
					scheduledMethod.setMember(START, new ConstantExpression(START_VALUE));
				}
				else {
					scheduledMethod.setMember(START, startExpression);
				}
				// interval default is INTERVAL_VALUE
				Expression intervalExpression = AstUtil.getExpressionForMember(annot, INTERVAL);
				if (intervalExpression == null){
					scheduledMethod.setMember(INTERVAL, new ConstantExpression(INTERVAL_VALUE));
				}
				else {
					scheduledMethod.setMember(INTERVAL, intervalExpression);
				}
				for (String member : NON_DEFAULT_ANNOTATION_MEMBERS){
					Expression memberExpression = AstUtil.getExpressionForMember(annot, member);
					if (memberExpression != null){
						scheduledMethod.setMember(member, memberExpression);
					}
				}
				methodNode.addAnnotation(scheduledMethod);
			}
		}
	}
	

}
