package repast.simphony.relogo.ast

import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode 
import org.codehaus.groovy.ast.VariableScope 
import org.codehaus.groovy.ast.builder.AstBuilder 
import org.codehaus.groovy.ast.expr.ArgumentListExpression 
import org.codehaus.groovy.ast.expr.MethodCallExpression 
import org.codehaus.groovy.ast.expr.VariableExpression 
import org.codehaus.groovy.ast.stmt.BlockStatement 
import org.codehaus.groovy.ast.stmt.ExpressionStatement 
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement 
import org.codehaus.groovy.runtime.MetaClassHelper 
import org.objectweb.asm.Opcodes 

class PatchTypeClassInstrumentor {
	private List<ClassNode> listOfUserTurtleClasses;
	private List<FieldNode> listOfPublicFieldsAndProperties;
	
	public PatchTypeClassInstrumentor(List<ClassNode> listOfUserTurtleClasses, List<FieldNode> listOfPublicFieldsAndProperties){
		this.listOfUserTurtleClasses = listOfUserTurtleClasses;
		this.listOfPublicFieldsAndProperties = listOfPublicFieldsAndProperties;
	}
	
	public instrument(){
	
		for (ClassNode cn : listOfUserTurtleClasses ){
			for (FieldNode fn in listOfPublicFieldsAndProperties){
				if (fn){
					addGetterAndSetterMethods(cn,fn)
				}
			}
		}
	}
	
	private void addGetterAndSetterMethods(ClassNode cn, FieldNode fn){
		addMethodSafe(cn, createTurtleVarGetterMethod(fn.getName(),fn.getType()))
		addMethodSafe(cn, createTurtleVarSetterMethod(fn.getName(),fn.getType()))
	}
	
	private void addMethodSafe(ClassNode cn, MethodNode mn){
		if (!cn.getMethod(mn.getName(), mn.getParameters())){
			cn.addMethod(mn)
		}
	}
	
	private MethodNode createTurtleVarGetterMethod(String fieldName, ClassNode type){
		//create method node
		//		MethodNode n = new MethodNode('get'+ MetaClassHelper.capitalize(fieldName), Opcodes.ACC_PUBLIC, returnType, parameters, exceptions, code)
		def ast = (new AstBuilder()).buildFromSpec {
			method('get' + MetaClassHelper.capitalize(fieldName), Opcodes.ACC_PUBLIC, type.getTypeClass()) {
				parameters {}
				exceptions {}
				block {}
			}
		}
		
		MethodNode mn = ast[0]
		mn.setCode(createTurtleVarGetterCode(fieldName))
		return mn
	}
	
	private MethodNode createTurtleVarSetterMethod(String fieldName, ClassNode type){
		def ast = (new AstBuilder()).buildFromSpec {
			method('set' + MetaClassHelper.capitalize(fieldName), Opcodes.ACC_PUBLIC, Void.TYPE) {
				parameters {
					parameter 'value': type.getTypeClass() 
				}
				exceptions {}
				block {}
			}
		}
		MethodNode mn = ast[0]
		mn.setCode(createTurtleVarSetterCode(fieldName))
		return mn
	}
	
	private BlockStatement createTurtleVarGetterCode(String fieldName){
		
		ReturnStatement rs = new ReturnStatement(
			new MethodCallExpression(
				new MethodCallExpression(
					new VariableExpression("this"),
					'patchHere',
					new ArgumentListExpression()
					),
				'get' + MetaClassHelper.capitalize(fieldName),
				new ArgumentListExpression()
			)
		)
		
		return new BlockStatement([rs] as Statement[], new VariableScope())
	}
	
	private BlockStatement createTurtleVarSetterCode(String fieldName){
		
		ExpressionStatement es = new ExpressionStatement(
			new MethodCallExpression(
				new MethodCallExpression(
					new VariableExpression("this"),
					'patchHere',
					new ArgumentListExpression()
					),
				'set' + MetaClassHelper.capitalize(fieldName),
				new ArgumentListExpression(new VariableExpression('value'))
			)
		)
		
		return new BlockStatement([es] as Statement[], new VariableScope())
	}
	

}
