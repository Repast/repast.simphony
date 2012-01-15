package repast.simphony.relogo.ast;

import groovy.util.logging.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode 
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.VariableScope;
import org.codehaus.groovy.ast.builder.AstBuilder;
import org.codehaus.groovy.ast.expr.ArgumentListExpression 
import org.codehaus.groovy.ast.expr.ArrayExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression 
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression 
import org.codehaus.groovy.ast.expr.VariableExpression 
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement 
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement 
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation 
import org.codehaus.groovy.transform.GroovyASTTransformation 
import org.objectweb.asm.Opcodes;

import repast.simphony.relogo.DiffusiblePatchVariable;

import cern.colt.matrix.DoubleMatrix2D;

@Log
@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
class DiffusibleASTTransformation implements ASTTransformation {
	private final ClassNode diffusiblePatchVariable = ClassHelper.make('repast.simphony.relogo.DiffusiblePatchVariable')

	public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
		
		AnnotationNode annot = (AnnotationNode) nodes[0]
		AnnotatedNode parent = (AnnotatedNode) nodes[1]
		if (parent instanceof FieldNode) {
			FieldNode fieldNode = (FieldNode) parent;
			final ClassNode type = fieldNode.getType();
			final ClassNode owner = fieldNode.getOwner();
			List<FieldNode> fieldNodes = owner.getFields()
			double defaultValue = 0.0
			if (fieldNode.hasInitialExpression()){
				Expression exp1 = fieldNode.getInitialExpression()
				if (exp1 instanceof ConstantExpression){
					ConstantExpression exp2 = (ConstantExpression) exp1;
					def expVal = exp2.getValue()
					if (expVal instanceof Number){
						defaultValue = ((Number)expVal).doubleValue()
					}
				}
			}
			
			List<PropertyNode> propertyNodes = owner.getProperties()
			String originalFieldNodeName = fieldNode.getName()
			owner.renameField(originalFieldNodeName, '__notUsed__' + originalFieldNodeName)
			fieldNodes.remove(fieldNode)
			PropertyNode pn = propertyNodes.find { 
				it.getName().equals(originalFieldNodeName)
			}
			propertyNodes.remove(pn)
			
			owner.addMethod(createPatchVarGetterMethod(originalFieldNodeName, type))
			owner.addMethod(createPatchVarSetterMethod(originalFieldNodeName, type))

			// Add a static 'getDiffusiblePatchVars' method (if it doesn't exist yet):
			String gDPVName = 'getDiffusiblePatchVars'
			MethodNode getDiffusiblePatchVarsMethodNode = owner.getDeclaredMethod(gDPVName, new Parameter[0])
			if (! getDiffusiblePatchVarsMethodNode ) {
				MethodFromStringCreator mfsc = new MethodFromStringCreator(owner.getName())
				String methodString = """
					import repast.simphony.relogo.*
					public static List<DiffusiblePatchVariable> ${gDPVName}(){
						return []
					}
					"""
				getDiffusiblePatchVarsMethodNode = mfsc.createMethodFromString(gDPVName,methodString) 
				owner.addMethod(getDiffusiblePatchVarsMethodNode)
			}
			// Add the originalFieldNodeName to the list returned by 'getDiffusiblePatchVars' 
			DiffusiblePatchVariable dpv = new DiffusiblePatchVariable(originalFieldNodeName,defaultValue)
			BlockStatement block =  (BlockStatement) getDiffusiblePatchVarsMethodNode.getCode();
			List<Statement> stmts = block.getStatements()
			Statement stmt = stmts?.get(0)
			if (stmt && stmt instanceof ReturnStatement){
				Expression expr = ((ReturnStatement)stmt).getExpression()
				if (expr && expr instanceof ListExpression){
					ConstantExpression ceName = new ConstantExpression(dpv.getName());
					ConstantExpression ceValue = new ConstantExpression(dpv.getDefaultValue());
					ArgumentListExpression ale = new ArgumentListExpression(ceName,ceValue);
					((ListExpression)expr).addExpression(new ConstructorCallExpression(diffusiblePatchVariable,ale))
				}
			}
		}
	}
		
	private MethodNode createPatchVarGetterMethod(String fieldName, ClassNode type){
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
		mn.setCode(createPatchVarGetterCode(fieldName))
		return mn
	}
	
	private MethodNode createPatchVarSetterMethod(String fieldName, ClassNode type){
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
		mn.setCode(createPatchVarSetterCode(fieldName))
		return mn
	}
	
	private BlockStatement createPatchVarGetterCode(String fieldName){
		
		VariableExpression rcListExpr = new VariableExpression('rc', ClassHelper.make(List))
		ConstantExpression fieldNameStringExpr = new ConstantExpression(fieldName)
		
		ExpressionStatement es1 = new ExpressionStatement(
		
		new DeclarationExpression(
		rcListExpr,
		new Token(Types.EQUALS, "=", -1, -1),
		new MethodCallExpression(
		new VariableExpression("this"),
		'getRowCol',
		new ArgumentListExpression()
		)
		)
		)
		
		ReturnStatement rs1 = new ReturnStatement(
		new MethodCallExpression(
		new MethodCallExpression(
		new MethodCallExpression(
		new VariableExpression("this"),
		'getMyObserver',
		new ArgumentListExpression()
		),
		'getPatchVarMatrix',
		new ArgumentListExpression(fieldNameStringExpr)
		),
		'getQuick',
		new ArgumentListExpression(
		new MethodCallExpression(
		rcListExpr,
		'get',
		new ConstantExpression(0)
		),
		new MethodCallExpression(
		rcListExpr,
		'get',
		new ConstantExpression(1)
		)
		)
		)
		)
		
		return new BlockStatement([es1,rs1] as Statement[], new VariableScope())
	}
	
	private BlockStatement createPatchVarSetterCode(String fieldName){
		
		VariableExpression rcListExpr = new VariableExpression('rc', ClassHelper.make(List))
		ConstantExpression fieldNameStringExpr = new ConstantExpression(fieldName)
		
		ExpressionStatement es1 = new ExpressionStatement(
		new DeclarationExpression(
		rcListExpr,
		new Token(Types.EQUALS, "=", -1, -1),
		new MethodCallExpression(
		new VariableExpression("this"),
		'getRowCol',
		new ArgumentListExpression()
		)
		)
		)
		
		ExpressionStatement es2 = new ExpressionStatement(
		new MethodCallExpression(
		new MethodCallExpression(
		new MethodCallExpression(
		new VariableExpression("this"),
		'getMyObserver',
		new ArgumentListExpression()
		),
		'getPatchVarMatrix',
		new ArgumentListExpression(fieldNameStringExpr)
		),
		'setQuick',
		new ArgumentListExpression(
		new MethodCallExpression(
		rcListExpr,
		'get',
		new ConstantExpression(0)
		),
		new MethodCallExpression(
		rcListExpr,
		'get',
		new ConstantExpression(1)
		),
		new CastExpression(ClassHelper.make(Double), new VariableExpression('value'))
		)
		)
		)
		
		return new BlockStatement([es1,es2] as Statement[], new VariableScope())
	}	

}