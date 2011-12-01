package repast.simphony.relogo.ast

import java.util.List;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode 
import org.codehaus.groovy.ast.VariableScope 
import org.codehaus.groovy.ast.builder.AstBuilder 
import org.codehaus.groovy.ast.expr.ArgumentListExpression 
import org.codehaus.groovy.ast.expr.ConstantExpression 
import org.codehaus.groovy.ast.expr.MethodCallExpression 
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression 
import org.codehaus.groovy.ast.expr.VariableExpression 
import org.codehaus.groovy.ast.stmt.BlockStatement 
import org.codehaus.groovy.ast.stmt.ExpressionStatement 
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement 
import org.codehaus.groovy.runtime.MetaClassHelper 
import org.objectweb.asm.Opcodes 

class GlobalsClassInstrumentor {
	private List<ClassNode> listOfUserClasses;
	private List<String> listOfGlobalFieldNames;
	
	public GlobalsClassInstrumentor(List<ClassNode> listOfUserClasses, List<String> listOfGlobalFieldNames){
		this.listOfUserClasses = listOfUserClasses;
		this.listOfGlobalFieldNames = listOfGlobalFieldNames;
	}
	
	public instrument(){
	
		for (ClassNode ut in listOfUserClasses){
			for (String fieldName in listOfGlobalFieldNames){
				addMethod(ut,createGlobalVarGetterMethod(fieldName,ClassHelper.OBJECT_TYPE))
				addMethod(ut,createGlobalVarSetterMethod(fieldName,ClassHelper.OBJECT_TYPE))
			}
		}
	}
	
	private void addMethod(ClassNode cn, MethodNode mn){
		if (!cn.getMethod(mn.getName(), mn.getParameters())){
			cn.addMethod(mn)
		}
	}
	private MethodNode createGlobalVarGetterMethod(String fieldName, ClassNode type){
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
		mn.setCode(createPanelGlobalVarGetterCode(fieldName))
		return mn
	}
	
	private MethodNode createGlobalVarSetterMethod(String fieldName, ClassNode type){
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
		mn.setCode(createPanelGlobalVarSetterCode(fieldName))
		return mn
	}
	
	private BlockStatement createPanelGlobalVarGetterCode(String fieldName){
		ReturnStatement rs = new ReturnStatement(
		new MethodCallExpression(
		new StaticMethodCallExpression(
		ClassHelper.make('repast.simphony.relogo.ReLogoModel'),
		'getInstance',
		new ArgumentListExpression()
		),
		'getModelParam',
		new ArgumentListExpression(new ConstantExpression(fieldName))
		)
		)
		return new BlockStatement([rs] as Statement[], new VariableScope())
	}
	
	private BlockStatement createPanelGlobalVarSetterCode(String fieldName){
		
		ExpressionStatement es = new ExpressionStatement(
		new MethodCallExpression(
		new StaticMethodCallExpression(
		ClassHelper.make('repast.simphony.relogo.ReLogoModel'),
		'getInstance',
		new ArgumentListExpression()
		),
		'setModelParam',
		new ArgumentListExpression(new ConstantExpression(fieldName),new VariableExpression('value'))
		)
		)
		
		return new BlockStatement([es] as Statement[], new VariableScope())
	}	

}
