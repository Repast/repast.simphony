package repast.simphony.relogo.ast

import java.beans.Introspector;
import java.text.DateFormat 
import java.text.SimpleDateFormat 
import java.util.List;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassHelper 
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode 
import org.codehaus.groovy.ast.ModuleNode 
import org.codehaus.groovy.ast.VariableScope 
import org.codehaus.groovy.ast.builder.AstBuilder 
import org.codehaus.groovy.ast.expr.ArgumentListExpression 
import org.codehaus.groovy.ast.expr.ConstantExpression 
import org.codehaus.groovy.ast.expr.MethodCallExpression 
import org.codehaus.groovy.ast.expr.VariableExpression 
import org.codehaus.groovy.ast.stmt.BlockStatement 
import org.codehaus.groovy.ast.stmt.ExpressionStatement 
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement 
import org.codehaus.groovy.control.CompilationUnit 
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.CompilerConfiguration 
import org.codehaus.groovy.runtime.MetaClassHelper 
import org.objectweb.asm.Opcodes 

class LinkTypeClassInstrumentor {
	private ClassNode linkClass;
	private boolean directed;
	private List<ClassNode> listOfUserClasses;
	private String singularString;
	private String pluralString;
	private String className;
	private List<ClassNode> listOfUserTurtleClasses;
	private List<ClassNode> listOfUserPatchClasses;
	private List<ClassNode> listOfUserLinkClasses;
	private List<ClassNode> listOfUserObserverClasses;
	private MethodFromStringCreator mfsc
	
	public LinkTypeClassInstrumentor(ClassNode linkClass, boolean directed, List<ClassNode> listOfUserClasses, List<ClassNode> listOfUserTurtleClasses, 
			List<ClassNode> listOfUserPatchClasses, List<ClassNode> listOfUserLinkClasses, List<ClassNode> listOfUserObserverClasses,  
			String singularString, String pluralString){
		this.linkClass = linkClass;
		this.directed = directed;
		this.listOfUserClasses = listOfUserClasses;
		this.listOfUserTurtleClasses = listOfUserTurtleClasses;
		this.listOfUserPatchClasses = listOfUserPatchClasses;
		this.listOfUserLinkClasses = listOfUserLinkClasses;
		this.listOfUserObserverClasses = listOfUserObserverClasses;
		this.singularString = singularString;
		this.pluralString = pluralString;
		this.className = linkClass.getNameWithoutPackage()
		this.mfsc = new MethodFromStringCreator(this.className)
	}
	
	public instrument(){
		
		// Link
		
		
		for (ClassNode cn : listOfUserLinkClasses){
			List<MethodNode> linkMethodList = [
				tplIsTypeQMethod(),
				tplTypesMethod(),
				tplTypeMethod()
				]
			for (MethodNode mn : linkMethodList){
				if (mn){
					addMethod(cn,mn)
				}
			}
		}
		
		
		// Turtles
		
		
		for (ClassNode cn in listOfUserTurtleClasses){
			List<MethodNode> turtleMethodList = []
			if (directed){
				turtleMethodList = [
				turtleCreateTypeFromMethod(),
				turtleCreateTypesFromMethod(),
				turtleCreateTypesFromMethodCollection(),
				turtleCreateTypeToMethod(),
				turtleCreateTypesToMethod(),
				turtleCreateTypesToMethodCollection(),
				turtleCreateTypeWithMethodDir(),
				turtleCreateTypesWithMethodDir(),
				turtleInTypeNeighborQMethod(),
				turtleInTypeNeighborsMethod(),
				turtleInTypeFromMethod(),
				turtleMyInTypesMethod(),
				turtleMyOutTypesMethod(),
				turtleOutTypeNeighborQMethod(),
				turtleOutTypeNeighborsMethod(),
				turtleOutTypeToMethod(),
				turtleTypeNeighborQMethod(),
				turtleTypeNeighborsMethod(),
				turtleTypeWithMethodDir(),
				turtleMyTypesMethod(),
				tplIsTypeQMethod(),
				tplTypesMethod(),
				tplTypeMethod()]
			}
			else {
				turtleMethodList = [
					turtleCreateTypeFromMethodUndir(),
					turtleCreateTypesFromMethodUndir(),
					turtleCreateTypeToMethodUndir(),
					turtleCreateTypesToMethodUndir(),
					turtleCreateTypeWithMethod(),
					turtleCreateTypesWithMethod(),
					turtleCreateTypesWithMethodCollection(),
					turtleInTypeNeighborQMethodUndir(),
					turtleInTypeNeighborsMethodUndir(),
					turtleInTypeFromMethodUndir(),
					turtleMyInTypesMethodUndir(),
					turtleMyOutTypesMethodUndir(),
					turtleOutTypeNeighborQMethodUndir(),
					turtleOutTypeNeighborsMethodUndir(),
					turtleOutTypeToMethodUndir(),
					turtleTypeNeighborQMethod(),
					turtleTypeNeighborsMethod(),
					turtleTypeWithMethod(),
					turtleMyTypesMethod(),
					tplIsTypeQMethod(),
					tplTypesMethod(),
					tplTypeMethod()]
			}
			for (MethodNode mn in turtleMethodList){
				if (mn){
					addMethod(cn,mn)
				}
			}
		}
		
		
		// Observers
		
		
		for (ClassNode cn in listOfUserObserverClasses){
			List<MethodNode> observerMethodList = [
				observerIsTypeQMethod(),
				observerTypesMethod(),
				observerTypeMethod()]
			for (MethodNode mn in observerMethodList){
				if (mn){
					addMethod(cn,mn)
				}
			}
		}
		
		// Patches
		
		
		for (ClassNode cn in listOfUserPatchClasses){
			List<MethodNode> patchMethodList = [
				tplIsTypeQMethod(),
				tplTypesMethod(),
				tplTypeMethod()]
			for (MethodNode mn in patchMethodList){
				if (mn){
					addMethod(cn,mn)
				}
			}
		}
	}
	
	private void addMethod(ClassNode cn, MethodNode mn){
		if (!cn.getMethod(mn.getName(), mn.getParameters())){
			cn.addMethod(mn)
		}
	}
	
	protected MethodNode turtleCreateTypeFromMethod(){
		String methodName = 'create' + capitalize(singularString) + 'From'
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(Turtle t, Closure closure = null){
				Link link = this.getMyObserver().getNetwork('${className}').addEdge(t,this)
				if (closure){
					this.ask(link,closure)
				}
				return link;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleCreateTypeFromMethodUndir(){
		String methodName = 'create' + capitalize(singularString) + 'From'
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(Turtle t, Closure closure = null){
				System.err.println('Attention: The directed link method $methodName was called on the undirected link type ${capitalize(singularString)}.')
				return null;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleCreateTypesFromMethod(){
		String methodName = 'create' + capitalize(pluralString) + 'From'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(AgentSet a, Closure closure = null){
				AgentSet links = new AgentSet()
				for(Turtle t : a){
					links.add(this.getMyObserver().getNetwork('${className}').addEdge(t,this))
				}
				if (closure){
					this.ask(links,closure)
				}
				return links;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleCreateTypesFromMethodCollection(){
		String methodName = 'create' + capitalize(pluralString) + 'From'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(Collection a, Closure closure = null){
				AgentSet links = new AgentSet()
				for(Turtle t : a){
					links.add(this.getMyObserver().getNetwork('${className}').addEdge(t,this))
				}
				if (closure){
					this.ask(links,closure)
				}
				return links;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleCreateTypesFromMethodUndir(){
		String methodName = 'create' + capitalize(pluralString) + 'From'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(Collection a, Closure closure = null){
				System.err.println('Attention: The directed link method $methodName was called on the undirected link type ${capitalize(singularString)}.')
				return null;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleCreateTypeToMethod(){
		String methodName = 'create' + capitalize(singularString) + 'To'
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(Turtle t, Closure closure = null){
				Link link = this.getMyObserver().getNetwork('${className}').addEdge(this,t)
				if (closure){
					this.ask(link,closure)
				}
				return link;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleCreateTypeToMethodUndir(){
		String methodName = 'create' + capitalize(singularString) + 'To'
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(Turtle t, Closure closure = null){
				System.err.println('Attention: The directed link method $methodName was called on the undirected link type ${capitalize(singularString)}.')
				return null;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleCreateTypesToMethod(){
		String methodName = 'create' + capitalize(pluralString) + 'To'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(AgentSet a, Closure closure = null){
				AgentSet links = new AgentSet()
				for(Turtle t : a){
					links.add(this.getMyObserver().getNetwork('${className}').addEdge(this,t))
				}
				if (closure){
					this.ask(links,closure)
				}
				return links;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleCreateTypesToMethodCollection(){
		String methodName = 'create' + capitalize(pluralString) + 'To'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(Collection a, Closure closure = null){
				AgentSet links = new AgentSet()
				for(Turtle t : a){
					links.add(this.getMyObserver().getNetwork('${className}').addEdge(this,t))
				}
				if (closure){
					this.ask(links,closure)
				}
				return links;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleCreateTypesToMethodUndir(){
		String methodName = 'create' + capitalize(pluralString) + 'To'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(Collection a, Closure closure = null){
				System.err.println('Attention: The directed link method $methodName was called on the undirected link type ${capitalize(singularString)}.')
				return null;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleCreateTypeWithMethod(){
		String methodName = 'create' + capitalize(singularString) + 'With'
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(Turtle t, Closure closure = null){
				Link link = this.getMyObserver().getNetwork('${className}').addEdge(this,t)
				if (closure){
					this.ask(link,closure)
				}
				return link;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}

	protected MethodNode turtleCreateTypeWithMethodDir(){
		String methodName = 'create' + capitalize(singularString) + 'With'
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(Turtle t, Closure closure = null){
				System.err.println('Attention: The undirected link method $methodName was called on the directed link type ${capitalize(singularString)}.')
				return null;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}

	protected MethodNode turtleCreateTypesWithMethod(){
		String methodName = 'create' + capitalize(pluralString) + 'With'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(AgentSet a, Closure closure = null){
				AgentSet links = new AgentSet()
				for(Turtle t : a){
					links.add(this.getMyObserver().getNetwork('${className}').addEdge(this,t))
				}
				if (closure){
					this.ask(links,closure)
				}
				return links;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleCreateTypesWithMethodCollection(){
		String methodName = 'create' + capitalize(pluralString) + 'With'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(Collection a, Closure closure = null){
				AgentSet links = new AgentSet()
				for(Turtle t : a){
					links.add(this.getMyObserver().getNetwork('${className}').addEdge(this,t))
				}
				if (closure){
					this.ask(links,closure)
				}
				return links;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}

	protected MethodNode turtleCreateTypesWithMethodDir(){
		String methodName = 'create' + capitalize(pluralString) + 'With'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(Collection a, Closure closure = null){
					System.err.println('Attention: The undirected link method $methodName was called on the directed link type ${capitalize(singularString)}.')
					return null;
			}
			"""
		return createMethodFromString(methodName,methodString)
	}

	protected MethodNode turtleInTypeNeighborQMethod(){
		String methodName = 'in' + capitalize(singularString) + 'NeighborQ'
		String methodString = """
			import repast.simphony.relogo.*
			public boolean ${methodName}(Turtle t){
				return this.getMyObserver().getNetwork('${className}').isPredecessor(t, this)
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleInTypeNeighborQMethodUndir(){
		String methodName = 'in' + capitalize(singularString) + 'NeighborQ'
		String methodString = """
			import repast.simphony.relogo.*
			public boolean ${methodName}(Turtle t){
				System.err.println('Attention: The directed link method $methodName was called on the undirected link type ${capitalize(singularString)}.')
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleInTypeNeighborsMethod(){
		String methodName = 'in' + capitalize(singularString) + 'Neighbors'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(){
				return new AgentSet(this.getMyObserver().getNetwork('${className}').getPredecessors(this).iterator().toList())
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleInTypeNeighborsMethodUndir(){
		String methodName = 'in' + capitalize(singularString) + 'Neighbors'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(){
				System.err.println('Attention: The directed link method $methodName was called on the undirected link type ${capitalize(singularString)}.')
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleInTypeFromMethod(){
		String methodName = 'in' + capitalize(singularString) + 'From'
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(Turtle t){
				return this.getMyObserver().getNetwork('${className}').getEdge(t,this)
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleInTypeFromMethodUndir(){
		String methodName = 'in' + capitalize(singularString) + 'From'
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(Turtle t){
				System.err.println('Attention: The directed link method $methodName was called on the undirected link type ${capitalize(singularString)}.')
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleMyInTypesMethod(){
		String methodName = 'myIn' + capitalize(pluralString)
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(){
				return new AgentSet(this.getMyObserver().getNetwork('${className}').getInEdges(this).iterator().toList())
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleMyInTypesMethodUndir(){
		String methodName = 'myIn' + capitalize(pluralString)
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(){
				System.err.println('Attention: The directed link method $methodName was called on the undirected link type ${capitalize(singularString)}.')
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleMyOutTypesMethod(){
		String methodName = 'myOut' + capitalize(pluralString)
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(){
				return new AgentSet(this.getMyObserver().getNetwork('${className}').getOutEdges(this).iterator().toList())
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleMyOutTypesMethodUndir(){
		String methodName = 'myOut' + capitalize(pluralString)
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(){
				System.err.println('Attention: The directed link method $methodName was called on the undirected link type ${capitalize(singularString)}.')
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleOutTypeNeighborQMethod(){
		String methodName = 'out' + capitalize(singularString) + 'NeighborQ'
		String methodString = """
			import repast.simphony.relogo.*
			public boolean ${methodName}(Turtle t){
				return this.getMyObserver().getNetwork('${className}').isPredecessor(this, t)
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleOutTypeNeighborQMethodUndir(){
		String methodName = 'out' + capitalize(singularString) + 'NeighborQ'
		String methodString = """
			import repast.simphony.relogo.*
			public boolean ${methodName}(Turtle t){
				System.err.println('Attention: The directed link method $methodName was called on the undirected link type ${capitalize(singularString)}.')
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleOutTypeNeighborsMethod(){
		String methodName = 'out' + capitalize(singularString) + 'Neighbors'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(){
				return new AgentSet(this.getMyObserver().getNetwork('${className}').getSuccessors(this).iterator().toList())
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleOutTypeNeighborsMethodUndir(){
		String methodName = 'out' + capitalize(singularString) + 'Neighbors'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(){
				System.err.println('Attention: The directed link method $methodName was called on the undirected link type ${capitalize(singularString)}.')
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleOutTypeToMethod(){
		String methodName = 'out' + capitalize(singularString) + 'To'
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(Turtle t){
				return this.getMyObserver().getNetwork('${className}').getEdge(this,t)
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleOutTypeToMethodUndir(){
		String methodName = 'out' + capitalize(singularString) + 'To'
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(Turtle t){
				System.err.println('Attention: The directed link method $methodName was called on the undirected link type ${capitalize(singularString)}.')
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleTypeNeighborQMethod(){
		String methodName = Introspector.decapitalize(singularString) + 'NeighborQ'
		String methodString = """
			import repast.simphony.relogo.*
			public boolean ${methodName}(Turtle t){
				return this.getMyObserver().getNetwork('${className}').isAdjacent(this, t)
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleTypeNeighborsMethod(){
		String methodName = Introspector.decapitalize(singularString) + 'Neighbors'
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(){
				return new AgentSet(this.getMyObserver().getNetwork('${className}').getAdjacent(this).iterator().toList())
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleTypeWithMethod(){
		String methodName = Introspector.decapitalize(singularString) + 'With'
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(Turtle t){
				return this.getMyObserver().getNetwork('${className}').getEdge(this,t)
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleTypeWithMethodDir(){
		String methodName = Introspector.decapitalize(singularString) + 'With'
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(Turtle t){
					System.err.println('Attention: The undirected link method $methodName was called on the directed link type ${capitalize(singularString)}.')
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleMyTypesMethod(){
		String methodName = 'my' + capitalize(pluralString)
		String methodString = """
			import repast.simphony.relogo.*
			public AgentSet ${methodName}(){
				return new AgentSet(this.getMyObserver().getNetwork('${className}').getEdges(this).iterator().toList())
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode tplIsTypeQMethod(){
		String methodName = 'is' + capitalize(singularString) + 'Q'
		String methodString = """
					import repast.simphony.relogo.*
					public boolean ${methodName}(Object o){
						Class clazz = getMyObserver().getLinkFactory().getLinkTypeClass('$singularString')
						if (o != null){
							return (clazz.isAssignableFrom(o.getClass()))
						}
						else {
							return false
						}
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode observerIsTypeQMethod(){
		String methodName = 'is' + capitalize(singularString) + 'Q'
		String methodString = """
					import repast.simphony.relogo.*
					public boolean ${methodName}(Object o){
						Class clazz = getLinkFactory().getLinkTypeClass('$singularString')
						if (o != null){
							return (clazz.isAssignableFrom(o.getClass()))
						}
						else {
							return false
						}
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode tplTypesMethod(){
		String methodName = Introspector.decapitalize(pluralString)
		String methodString = """
					import repast.simphony.relogo.*
					public AgentSet ${methodName}(){
						Class clazz = getMyObserver().getLinkFactory().getLinkTypeClass('$singularString')
						return (Utility.getAgentSetOfClass(clazz,this.getMyObserver()))
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode tplTypeMethod(){
		String methodName = Introspector.decapitalize(singularString)
		String methodString = """
			import repast.simphony.relogo.*
			public Link ${methodName}(int oneEnd, int otherEnd){
				return (this.getMyObserver().getNetwork('${className}').getEdge(turtle(oneEnd),turtle(otherEnd)))
			}
			"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode observerTypesMethod(){
		String methodName = Introspector.decapitalize(pluralString)
		String methodString = """
					import repast.simphony.relogo.*
					public AgentSet ${methodName}(){
						Class clazz = getLinkFactory().getLinkTypeClass('$singularString')
						return (Utility.getAgentSetOfClass(clazz,this))
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode observerTypeMethod(){
		String methodName = Introspector.decapitalize(singularString)
		String methodString = """
					import repast.simphony.relogo.*
					public Link ${methodName}(int oneEnd, int otherEnd){
						return (this.getNetwork('${className}').getEdge(turtle(oneEnd),turtle(otherEnd)))
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode createMethodFromString(String methodName, String methodString){
		return (mfsc.createMethodFromString(methodName, methodString))
	}
	
	//Utility for capitalizing the first character of a string 
	private String capitalize(String s){
		String result = ''
		if (s){
			if (s.size() > 1){
				result = s[0].toUpperCase() + s[1..-1]
			}
			else {
				result = s.toUpperCase()
			}
		}
		return result
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
		mn.setCode(createGlobalVarGetterCode(fieldName))
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
		mn.setCode(createGlobalVarSetterCode(fieldName))
		return mn
	}
	
	private BlockStatement createGlobalVarGetterCode(String fieldName){
		
		ReturnStatement rs = new ReturnStatement(
		new MethodCallExpression(
		new MethodCallExpression(
		new VariableExpression("this"),
		'getMyObserver',
		new ArgumentListExpression()
		),
		'get' + MetaClassHelper.capitalize(fieldName),
		new ArgumentListExpression()
		)
		)
		
		return new BlockStatement([rs] as Statement[], new VariableScope())
	}
	
	private BlockStatement createGlobalVarSetterCode(String fieldName){
		
		ExpressionStatement es = new ExpressionStatement(
		new MethodCallExpression(
		new MethodCallExpression(
		new VariableExpression("this"),
		'getMyObserver',
		new ArgumentListExpression()
		),
		'set' + MetaClassHelper.capitalize(fieldName),
		new ArgumentListExpression(new VariableExpression('value'))
		)
		)
		
		return new BlockStatement([es] as Statement[], new VariableScope())
	}
	
	private ClassNode extractClassNodeFromConstantExpression(ConstantExpression ce, ClassNode ownerClassNode){
		def valueOfConst = ce.getValue()
		if (valueOfConst instanceof String){
			String stringOfConst = (String) valueOfConst
			// possibly check to see if fully qualified name or not and, if not, add current package to name
			def tokenized = stringOfConst.tokenize('.')
			if (tokenized.size() > 1){
				//Assume fully qualified name
				return ClassHelper.make(stringOfConst)
			}
			else {
				//Add current package name to class name
				String packageString = ownerClassNode.getPackageName()
				if (packageString){
					return ClassHelper.make(packageString + '.' + stringOfConst)
				}
			}
		}
		return null
	}
	
	/*private MethodNode createSetterMethod(ClassNode owner, String fieldName, ClassNode type){
	 def ast = new AstBuilder().buildFromSpec {
	 method('set' + MetaClassHelper.capitalize(fieldName), Opcodes.ACC_PUBLIC, Void.TYPE) {
	 parameters {
	 parameter 'value': type.getTypeClass() 
	 }
	 exceptions {}
	 block {}
	 }
	 }
	 MethodNode mn = ast[0]
	 def result = new AstBuilder().buildFromCode {
	 List rc = getRowCol();
	 getMyObserver().getPatchVarMatrix(fieldName).setQuick(rc[0],rc[1], value as double)
	 }
	 mn.setCode(result[0])
	 return mn
	 }
	 private MethodNode createGetterMethod(ClassNode owner, String fieldName, ClassNode type){
	 def ast = new AstBuilder().buildFromSpec {
	 method('get' + MetaClassHelper.capitalize(fieldName), Opcodes.ACC_PUBLIC, type.getTypeClass()) {
	 parameters {}
	 exceptions {}
	 block {}
	 }
	 }
	 MethodNode mn = ast[0]
	 def result = new AstBuilder().buildFromCode {
	 List rc = getRowCol()
	 BaseObserver obs = getMyObserver();
	 DoubleMatrix2D mat = getPatchVarMatrix(fieldName);
	 def result = mat.getQuick(rc[0], rc[1]);
	 return result;
	 }
	 mn.setCode(result[0])
	 return mn
	 }*/

}
