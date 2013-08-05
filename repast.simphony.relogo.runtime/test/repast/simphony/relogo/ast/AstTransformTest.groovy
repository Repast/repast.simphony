package repast.simphony.relogo.ast;
import org.codehaus.groovy.ast.expr.ArgumentListExpression 
import org.codehaus.groovy.ast.expr.ConstantExpression 
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode 
import org.codehaus.groovy.ast.MethodNode 
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.VariableScope 
import org.codehaus.groovy.ast.stmt.BlockStatement 
import org.codehaus.groovy.ast.stmt.ExpressionStatement 
import org.codehaus.groovy.control.CompilePhase;
import org.objectweb.asm.Opcodes;
import junit.framework.Assert;

import org.codehaus.groovy.ast.builder.*

import groovy.util.GroovyTestCase;

class AstTransformTest extends GroovyTestCase {
	
	private AstBuilder factory
	String pluralString = 'rabbits'
	String singularString = 'rabbit'
	
	protected void setUp() {
		super.setUp()
		factory = new AstBuilder()
	}
	
	private void astTestUtility(String methodName, String methodString, MethodNode actualMethod){
		def expected = factory.buildFromString(CompilePhase.SEMANTIC_ANALYSIS,false,methodString)
		def expectedMethodNode = expected[1].getMethods().find({ it.name.equals(methodName) })
		AstAssert.assertSyntaxTree([expectedMethodNode],[actualMethod] )
	}
	
	
	
	public void testReLogoClasses(){
		String methodName = 'rabbitsHere'
		String methodString = """
						import repast.simphony.relogo.*
						public AgentSet ${methodName}(){
							return (new AgentSet(this.turtlesHere().findAll{it.breed.equals('rabbits')}))
						}
						"""

			def expected = factory.buildFromString(CompilePhase.SEMANTIC_ANALYSIS,false,methodString)
			def expectedMethodNode = expected[1].getMethods().find({ it.name.equals(methodName) })



		assertTrue(true)
	}
	
	
	public void testOne(){
		
		String methodName = 'harouxHere'
		String methodString = """
				import repast.simphony.relogo.*
				public AgentSet ${methodName}(){
					return (new AgentSet(this.turtlesHere().findAll{it.breed?.equals('${pluralString}')}))
				}
				"""
		def expected = factory.buildFromString(CompilePhase.SEMANTIC_ANALYSIS,false,methodString)
		def expectedMethodNode = expected[1].getMethods().find({ it.name.equals(methodName) })
		def a = 1
		assertTrue(true)
	}
	
	
}
