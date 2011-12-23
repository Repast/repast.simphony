package repast.simphony.relogo.ast

import java.util.List

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.control.CompilationUnit
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.CompilerConfiguration

class MethodFromStringCreator {
	
	private String uniqueId;
	
	public MethodFromStringCreator(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public MethodNode createMethodFromString(String methodName, String methodString){
		List<ASTNode> result
		try {
		result = buildFromString(CompilePhase.SEMANTIC_ANALYSIS,false,methodString)
		}
		catch(Exception e){
		}
		if (result){
			return( result[1].getMethods().find({ it.name.equals(methodName) }))
		}
		return null
	}
	
	private List<ASTNode> buildFromString(CompilePhase phase = CompilePhase.CLASS_GENERATION, boolean statementsOnly = true, String source) {
		if (!source || "" == source.trim()) throw new IllegalArgumentException("A source must be specified")
		return compile(source, phase, statementsOnly);
	}
	
	private List<ASTNode> compile(String script, CompilePhase compilePhase, boolean statementsOnly) {
		def scriptClassName = "script" + uniqueId + System.currentTimeMillis()
		GroovyClassLoader classLoader = new GroovyClassLoader(this.getClass().getClassLoader())
		GroovyCodeSource codeSource = new GroovyCodeSource(script, scriptClassName + ".groovy", "/groovy/script")
		CompilationUnit cu = new CompilationUnit(CompilerConfiguration.DEFAULT, codeSource.codeSource, classLoader)
		cu.addSource(codeSource.getName(), script);
		cu.compile(compilePhase.getPhaseNumber())
		// collect all the ASTNodes into the result, possibly ignoring the script body if desired
		return cu.ast.modules.inject([]) {List acc, ModuleNode node ->
			if (node.statementBlock) acc.add(node.statementBlock)
			node.classes?.each {
				if (!(it.name == scriptClassName && statementsOnly)) {
					acc << it
				}
			}
			acc
		}
	}
}
