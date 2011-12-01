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
	
	//BreedsASTTransformation
	// Observer methods
	public void testObserverCreateBreedsMethod(){
		MethodNode actualMethod = bf.observerCreateBreedsMethod(pluralString)

		String methodName = 'createRabbits'
		String methodString = """
						import repast.simphony.relogo.*
			public void ${methodName}(int number, Closure closure = null){
				this.crt(number,closure,'${pluralString}')
			}
			"""
		astTestUtility(methodName,methodString,actualMethod)
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
	
	// Turtle methods
	public void testTurtleHatchBreedsMethod(){
		MethodNode actualMethod = bf.turtleHatchBreedsMethod(pluralString)
		
		String methodName = 'hatchRabbits'
		String methodString = """
						import repast.simphony.relogo.*
					public void ${methodName}(int number, Closure closure = null){
						this.hatch(number,closure,'${pluralString}')
					}
					"""
		astTestUtility(methodName,methodString,actualMethod)
		
	}
	
	public void testPatchSproutBreedsMethod(){
		MethodNode actualMethod = bf.patchSproutBreedsMethod(pluralString)
		
		String methodName = 'sproutRabbits'
		String methodString = """
						import repast.simphony.relogo.*
					public void ${methodName}(int number, Closure closure = null){
						this.sprout(number,closure,'${pluralString}')
					}
					"""
		astTestUtility(methodName,methodString,actualMethod)
		
	}
	
	public void testTurtlePatchBreedsHereMethod(){
		MethodNode actualMethod = bf.turtlePatchBreedsHereMethod(pluralString)
		
		String methodName = 'rabbitsHere'
		String methodString = """
						import repast.simphony.relogo.*
					public AgentSet ${methodName}(){
						return (new AgentSet(this.turtlesHere().findAll{it.breed.equals('rabbits')}))
					}
					"""
		astTestUtility(methodName,methodString,actualMethod)
	}
	
	public void testTurtlePatchBreedsAtMethod(){
		MethodNode actualMethod = bf.turtlePatchBreedsAtMethod(pluralString)
		
		String methodName = 'rabbitsAt'
		String methodString = """
						import repast.simphony.relogo.*
					public AgentSet ${methodName}(double dx, double dy){
						return (new AgentSet(this.turtlesAt(dx,dy).findAll{it.breed.equals('rabbits')}))
					}
					"""
		astTestUtility(methodName,methodString,actualMethod)
	}
	
	public void testTurtlePatchBreedsOnPMethod(){
		MethodNode actualMethod = bf.turtlePatchBreedsOnPMethod(pluralString)
		
		String methodName = 'rabbitsOn'
		String methodString = """
						import repast.simphony.relogo.*
					public AgentSet ${methodName}(Patch p){
						return (new AgentSet(this.turtlesOn(p).findAll{it.breed.equals('rabbits')}))
					}
					"""
		astTestUtility(methodName,methodString,actualMethod)
	}
	
	public void testTurtlePatchBreedsOnTMethod(){
		MethodNode actualMethod = bf.turtlePatchBreedsOnTMethod(pluralString)
		
		String methodName = 'rabbitsOn'
		String methodString = """
						import repast.simphony.relogo.*
					public AgentSet ${methodName}(Turtle t){
						return (new AgentSet(this.turtlesOn(t).findAll{it.breed.equals('rabbits')}))
					}
					"""
		astTestUtility(methodName,methodString,actualMethod)
	}
	
	public void testTurtlePatchBreedsOnAMethod(){
		MethodNode actualMethod = bf.turtlePatchBreedsOnAMethod(pluralString)
		
		String methodName = 'rabbitsOn'
		String methodString = """
						import repast.simphony.relogo.*
					public AgentSet ${methodName}(AgentSet a){
						return (new AgentSet(this.turtlesOn(a).findAll{it.breed.equals('rabbits')}))
					}
					"""
		astTestUtility(methodName,methodString,actualMethod)
	}
	
	// method to add to OTPL
	public void testIsBreedQMethod(){
		MethodNode actualMethod = bf.isBreedQMethod(pluralString, singularString)
		
		String methodName = 'isRabbitQ'
		String methodString = """
						import repast.simphony.relogo.*
					public boolean ${methodName}(Object o){
						if (Utility.isTurtleQ(o)){
							return (o.breed.equals('rabbits'))
						}
						return false
					}
					"""
		astTestUtility(methodName,methodString,actualMethod)
	}
	
	// methods for TPL
	public void testTPLBreedsMethod(){
		MethodNode actualMethod = bf.tplBreedsMethod(pluralString)
		
		String methodName = 'rabbits'
		String methodString = """
						import repast.simphony.relogo.*
					public AgentSet ${methodName}(){
						return (Utility.getAgentSetOfBreed('rabbits',this.getMyObserver()))
					}
					"""
		astTestUtility(methodName,methodString,actualMethod)
	}
	public void testTPLBreedMethod(){
		MethodNode actualMethod = bf.tplBreedMethod(pluralString, singularString)
		
		String methodName = 'rabbit'
		String methodString = """
						import repast.simphony.relogo.*
					public Turtle ${methodName}(int num){
						Turtle turtle = Utility.turtle(num,this.getMyObserver())
						if (turtle.breed.equals('rabbits')) {
							return turtle
						}
						return null
					}
					"""
		astTestUtility(methodName,methodString,actualMethod)
	}
	
	// methods for observer
	public void testObserverBreedsMethod(){
		MethodNode actualMethod = bf.observerBreedsMethod(pluralString)
		
		String methodName = 'rabbits'
		String methodString = """
						import repast.simphony.relogo.*
					public AgentSet ${methodName}(){
						return (Utility.getAgentSetOfBreed('rabbits',this))
					}
					"""
		astTestUtility(methodName,methodString,actualMethod)
	}
	public void testObserverBreedMethod(){
		MethodNode actualMethod = bf.observerBreedMethod(pluralString, singularString)
		
		String methodName = 'rabbit'
		String methodString = """
						import repast.simphony.relogo.*
					public Turtle ${methodName}(int num){
						Turtle turtle = Utility.turtle(num,this)
						if (turtle.breed.equals('rabbits')) {
							return turtle
						}
						return null
					}
					"""
		astTestUtility(methodName,methodString,actualMethod)
	}
}
