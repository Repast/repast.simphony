package repast.simphony.relogo.ast;


import groovy.util.slurpersupport.GPathResult;
import groovy.xml.StreamingMarkupBuilder;
import groovy.xml.XmlUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.ClassNode.MapOfLists;
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation



@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
class ReLogoGlobalASTTransformation implements ASTTransformation {

	private final ClassNode baseObserver = ClassHelper.make('repast.simphony.relogo.BaseObserver')
	private final ClassNode baseTurtle = ClassHelper.make('repast.simphony.relogo.BaseTurtle')
	private final ClassNode basePatch =  ClassHelper.make('repast.simphony.relogo.BasePatch')
	private final ClassNode baseLink =  ClassHelper.make('repast.simphony.relogo.BaseLink')
	private final ClassNode abstractGlobalsAndPanel = ClassHelper.make('repast.simphony.relogo.factories.AbstractReLogoGlobalsAndPanelFactory')
	private final ClassNode libTurtleAnnotation = ClassHelper.make('repast.simphony.relogo.ast.ExtendsLibTurtle')
	private final ClassNode libPatchAnnotation = ClassHelper.make('repast.simphony.relogo.ast.ExtendsLibPatch')
	private final ClassNode libLinkAnnotation = ClassHelper.make('repast.simphony.relogo.ast.ExtendsLibLink')
	private final ClassNode libObserverAnnotation = ClassHelper.make('repast.simphony.relogo.ast.ExtendsLibObserver')

	static private final String DEFAULT_RELOGO_DISPLAY_NAME = "ReLogo Default Display"


	private String prettyPrint(xml) {

		XmlUtil.serialize(new StreamingMarkupBuilder().bind {
			mkp.declareNamespace("":"")
			mkp.yield xml
		})
	}

	private String plainPrettyPrint(xml) {
		XmlUtil.serialize(new StreamingMarkupBuilder().bind { mkp.yield xml })
	}

	public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
		//		log("in the UserPanelGlobalsASTTransformation visit method")
		List listOfGlobalFieldNames = [];
		//should be a list of maps <ClassNode, <List<String>>>
		Map mapOfTurtleTypesAndSingularPlural = [:];
		Map mapOfDirLinkTypesAndSingularPlural = [:];
		Map mapOfUndirLinkTypesAndSingularPlural = [:];
		Map mapOfPatchTypesAndFieldNames = [:];
		// Package for instrumenting: See SIM-468
		String instrumentingPackageName = "";
		ModuleNode moduleNode = sourceUnit.getAST()
		if (moduleNode){
			def classNodes = moduleNode.getClasses()
			// Usually there will be only one ClassNode per file but just in case there are multiple classes defined in one file
			for (ClassNode classNode in classNodes){
				// Checking to see if the class node is in a 'relogo' package or sub-package
				if (isInReLogoPackage(classNode)){
					instrumentingPackageName = extractInstrumentingPackageName(classNode)
					//					log("classNode $classNode is in the relogo package")

					// Globals and Panel elements
					if (classNode.isDerivedFrom(abstractGlobalsAndPanel)){
						MethodNode addPanelComponentsMethod = classNode.getMethod('addGlobalsAndPanelComponents', Parameter.EMPTY_ARRAY)
						if (addPanelComponentsMethod){
							BlockStatement block = (BlockStatement) addPanelComponentsMethod.getCode()
							for (Statement stmt in block.getStatements()){
								Expression expr = stmt.getExpression()
								if (expr && expr instanceof MethodCallExpression){

									MethodCallExpression mce = (MethodCallExpression) expr
									String methodString = mce.getMethodAsString()
									def methodsList = [
										'addGlobal',
										'addSlider',
										'addChooser',
										'addSwitch',
										'addSliderWL',
										'addChooserWL',
										'addSwitchWL',
										'addInput'
									]
									if (methodsList.contains(methodString)){
										Expression argumentsExpression = mce.getArguments()
										if (argumentsExpression && argumentsExpression instanceof ArgumentListExpression){
											List arguments = ((ArgumentListExpression)argumentsExpression).getExpressions()
											if (arguments.get(0) instanceof ConstantExpression){
												ConstantExpression ce = (ConstantExpression) arguments.get(0)
												def val = ce.getValue()
												if (val instanceof String){
													listOfGlobalFieldNames.add((String) val)
												}
											}
										}
									}
								} else if (expr && expr instanceof BinaryExpression ){
									BinaryExpression be = (BinaryExpression) expr
									Expression re = be.getRightExpression()
									if (re && re instanceof MethodCallExpression){
										MethodCallExpression mce = (MethodCallExpression) re
										String methodString = mce.getMethodAsString()
										def methodsList = [
											'slider',
											'chooser',
											'rSwitch',
											'sliderWL',
											'chooserWL',
											'rSwitchWL',
											'input'
										]
										if (methodsList.contains(methodString)){
											Expression argumentsExpression = mce.getArguments()
											if (argumentsExpression && argumentsExpression instanceof ArgumentListExpression){
												List arguments = ((ArgumentListExpression)argumentsExpression).getExpressions()
												if (arguments.get(0) instanceof ConstantExpression){
													ConstantExpression ce = (ConstantExpression) arguments.get(0)
													def val = ce.getValue()
													if (val instanceof String){
														listOfGlobalFieldNames.add((String) val)
													}
												}
											}
										}
									}
								}
							}
						}
					}

					// Turtle
					if (classNode.isDerivedFrom(baseTurtle)){
						//log("classNode $classNode is derived from baseTurtle")
						String typeName = classNode.getNameWithoutPackage();
						String typeNamePlural;

						List<AnnotationNode> annList = classNode.getAnnotations(ClassHelper.make(repast.simphony.relogo.Plural))
						boolean pluralFormFound = false;
						if (annList){
							AnnotationNode ann = annList.get(0);
							Expression pluralExpression = ann.getMember('value')
							if (pluralExpression instanceof ConstantExpression){
								def pluralVal = ((ConstantExpression)pluralExpression).getValue()
								if (pluralVal instanceof String){
									typeNamePlural = (String) pluralVal
									pluralFormFound = true
								}
							}
						}
						if(!pluralFormFound) { // append 's' to typeName to create plural name
							typeNamePlural = typeName + 's';
						}
						mapOfTurtleTypesAndSingularPlural.put(classNode, [typeName, typeNamePlural])
						for (a in mapOfTurtleTypesAndSingularPlural.keySet()){
							//log(" a $a is ${mapOfTurtleTypesAndSingularPlural.get(a)}")
						}
					}
					/*
					 * Annotations and defaults for link classes:
					 * look for @Plural for plural form, if not present, add an 's' to pluralize
					 * look for @Directed or @Undirected for directionality, if not present, assume directed
					 */
					if (classNode.isDerivedFrom(baseLink)){
						String className = classNode.getNameWithoutPackage()
						String typeName = classNode.getNameWithoutPackage();
						String typeNamePlural;

						List<AnnotationNode> annList = classNode.getAnnotations(ClassHelper.make(repast.simphony.relogo.Plural))
						boolean pluralFormFound = false;
						if (annList){
							AnnotationNode ann = annList.get(0);
							Expression pluralExpression = ann.getMember('value')
							if (pluralExpression instanceof ConstantExpression){
								def pluralVal = ((ConstantExpression)pluralExpression).getValue()
								if (pluralVal instanceof String){
									typeNamePlural = (String) pluralVal
									pluralFormFound = true
								}
							}
						}
						if(!pluralFormFound) { // append 's' to typeName to create plural name
							typeNamePlural = typeName + 's';
						}

						List<AnnotationNode> dirAnnList = classNode.getAnnotations(ClassHelper.make(repast.simphony.relogo.Directed))
						List<AnnotationNode> undirAnnList = classNode.getAnnotations(ClassHelper.make(repast.simphony.relogo.Undirected))

						if (dirAnnList){
							mapOfDirLinkTypesAndSingularPlural.put(classNode, [typeName, typeNamePlural])
						}
						else if (undirAnnList){
							mapOfUndirLinkTypesAndSingularPlural.put(classNode, [typeName, typeNamePlural])
						}
						else {
							mapOfDirLinkTypesAndSingularPlural.put(classNode, [typeName, typeNamePlural])
						}
						//log("starting logging")
						//						def dir = System.getProperty("groovy.target.directory")
						//						def props = System.getProperties()
						//						props.keySet().each{
						//							log("the $it is ${props[it]}")
						//						}

						String packageName = classNode.getPackageName();
						// package shouldn't be null since isInReLogoPackage would have prevented entrance to this logical branch
						int lastIndex = packageName.lastIndexOf('.');
						String basePackageName = lastIndex > 0 ? packageName.substring(0,lastIndex) : ""
						String stylePackagePath = lastIndex > 0 ? packageName.substring(0,lastIndex) + '/style' : 'style';
						stylePackagePath = stylePackagePath.replace('.', '/')

						checkContextFile(className, basePackageName, stylePackagePath)
						
					}

					// Patch fields
					if (classNode.isDerivedFrom(basePatch)){
						ClassNode tempClassNode = classNode
						while (!tempClassNode.equals(basePatch)){
							List<FieldNode> publicFieldsAndProperties = [];
							try{
								tempClassNode.fields.each {p ->
									if (!p.isPublic()){
										if (tempClassNode.getProperty(p.name)){
											publicFieldsAndProperties.add(p)
										}
									}
									else {
										publicFieldsAndProperties.add(p)
									}
								}						
								mapOfPatchTypesAndFieldNames.put(tempClassNode, publicFieldsAndProperties)
								// Collect inherited properties as well
								tempClassNode = tempClassNode.getSuperClass()
							}
							catch(Exception e){
								break;
							}
						}
					}
				}
			}
		}

		// Find all the user classes
		List<ClassNode> moduleClassList = moduleNode?.getClasses()
		if (moduleClassList){
			ClassNode owner = moduleClassList.get(0)
			CompileUnit cu = owner.getCompileUnit()
			List<ClassNode> listOfClasses = cu.getClasses()
			List<ClassNode> reLogoPackageListOfClasses = listOfClasses.findAll{ ClassNode thisCN ->
				isInReLogoPackage(thisCN) && extractInstrumentingPackageName(thisCN).equals(instrumentingPackageName)
			}
			List<ClassNode> listOfUserClasses = reLogoPackageListOfClasses.findAll { ClassNode cn ->
				baseTurtle.equals(cn.getSuperClass()) || basePatch.equals(cn.getSuperClass()) || baseLink.equals(cn.getSuperClass()) || baseObserver.equals(cn.getSuperClass()) ||
						!cn.getAnnotations(libTurtleAnnotation).isEmpty() || !cn.getAnnotations(libPatchAnnotation).isEmpty() || !cn.getAnnotations(libLinkAnnotation).isEmpty() || !cn.getAnnotations(libObserverAnnotation).isEmpty()
			}
			List<ClassNode> listOfUserTurtleClasses = reLogoPackageListOfClasses.findAll { ClassNode cn ->
				baseTurtle.equals(cn.getSuperClass()) || !cn.getAnnotations(libTurtleAnnotation).isEmpty()
			}
			List<ClassNode> listOfUserPatchClasses = reLogoPackageListOfClasses.findAll { ClassNode cn ->
				basePatch.equals(cn.getSuperClass()) || !cn.getAnnotations(libPatchAnnotation).isEmpty()
			}
			List<ClassNode> listOfUserLinkClasses = reLogoPackageListOfClasses.findAll { ClassNode cn ->
				baseLink.equals(cn.getSuperClass()) || !cn.getAnnotations(libLinkAnnotation).isEmpty()
			}
			List<ClassNode> listOfUserObserverClasses = reLogoPackageListOfClasses.findAll { ClassNode cn ->
				baseObserver.equals(cn.getSuperClass()) || !cn.getAnnotations(libObserverAnnotation).isEmpty()
			}


			// For the globals
			if (listOfGlobalFieldNames){
				GlobalsClassInstrumentor gci = new GlobalsClassInstrumentor(listOfUserClasses,listOfGlobalFieldNames);
				gci.instrument();
			}

			// For the turtle types
			if (mapOfTurtleTypesAndSingularPlural){
				for (ClassNode turtleType : mapOfTurtleTypesAndSingularPlural.keySet()){
					List list = mapOfTurtleTypesAndSingularPlural.get(turtleType)
					String singularString = list[0];
					String pluralString = list[1];
					TurtleTypeClassInstrumentor ttci = new TurtleTypeClassInstrumentor(turtleType, listOfUserClasses, listOfUserTurtleClasses, listOfUserPatchClasses, listOfUserLinkClasses, listOfUserObserverClasses, singularString, pluralString)
					ttci.instrument();
				}
			}

			// For the link types
			if (mapOfDirLinkTypesAndSingularPlural || mapOfUndirLinkTypesAndSingularPlural){

				for (ClassNode linkType : mapOfDirLinkTypesAndSingularPlural.keySet()){
					List list = mapOfDirLinkTypesAndSingularPlural.get(linkType)
					String singularString = list[0];
					String pluralString = list[1];
					LinkTypeClassInstrumentor ltci = new LinkTypeClassInstrumentor(linkType, true, listOfUserClasses, listOfUserTurtleClasses, listOfUserPatchClasses, listOfUserLinkClasses, listOfUserObserverClasses, singularString, pluralString)
					ltci.instrument();
				}
				for (ClassNode linkType : mapOfUndirLinkTypesAndSingularPlural.keySet()){
					List list = mapOfUndirLinkTypesAndSingularPlural.get(linkType)
					String singularString = list[0];
					String pluralString = list[1];
					LinkTypeClassInstrumentor ltci = new LinkTypeClassInstrumentor(linkType, false, listOfUserClasses, listOfUserTurtleClasses, listOfUserPatchClasses, listOfUserLinkClasses, listOfUserObserverClasses, singularString, pluralString)
					ltci.instrument();
				}
			}


			// For the patch variables
			if (mapOfPatchTypesAndFieldNames){
				for (ClassNode patchType : mapOfPatchTypesAndFieldNames.keySet()){
					PatchTypeClassInstrumentor ptci = new PatchTypeClassInstrumentor(listOfUserTurtleClasses, mapOfPatchTypesAndFieldNames.get(patchType));
					ptci.instrument();
				}
			}
		}
	}
	
	protected void checkContextFile(String className, String basePackageName, String stylePackagePath){
		// Fixed SIM-483 by obtaining the URI (not the URL) of the resource
		URI resource = this.getClass().getResource("/$stylePackagePath")?.toURI()
		if (resource){
			//log("packageName is: $stylePackagePath")
			//log("URL is: $resource")

			String fullLinkClassPath = resource.getPath()
			int projectPathIndex = fullLinkClassPath.indexOf("/bin/$stylePackagePath")
			String projectPath = fullLinkClassPath.substring(0, projectPathIndex)
			int projectNameIndex = projectPath.lastIndexOf('/')
			String projectName = projectPath.substring(projectNameIndex)
			String sep = File.separator
			String contextFilePath = projectPath + sep + "${projectName}.rs" + sep +"context.xml"
			checkToModifyContextFile(projectPath, projectName, className, basePackageName, contextFilePath)
			
		}
	}

	protected void checkToModifyContextFile(String projectPath, String projectName, String className, String basePackageName, String contextFilePath){
		File contextFile = new File(contextFilePath)
		if (contextFile.exists()){
			def root = new XmlSlurper().parse(contextFile)
			GPathResult listOfDefaultReLogoContexts = root.context.findAll({it.@id.equals("default_observer_context")})
			// If the default_observer_context exists
			if (listOfDefaultReLogoContexts.size() == 1){
				GPathResult defaultReLogoContext = listOfDefaultReLogoContexts[0]
				GPathResult classNameNetworks = defaultReLogoContext.projection.findAll {
					it.@type.equals("network") && it.@id.equals(className)
				}
				// If no networks corresponding to the className exist
				if (classNameNetworks.isEmpty()){
					// Add the network entry to the 
					defaultReLogoContext.leftShift{
						projection(id:className, type:'network')
					}
	
					contextFile.write(prettyPrint(root))
	
					//Add to the display's projection info
					modifyDisplayFile(projectPath, projectName, className, basePackageName)
				}
			}
		}
	}
	
	protected void modifyDisplayFile(String projectPath, String projectName, String className, String basePackageName){

		DefDisplayReturner result = findDefaultReLogoDisplayFile(projectPath, projectName)
		if (result != null && result.displayFile.exists()){
			
			GPathResult displayRoot = result.root
			// Check to see if network is already there (to avoid accidental duplication)
			GPathResult classNameInDisplay = displayRoot.netStyles.entry.string.findAll{
				it.text().equals(className)
			}
			if (classNameInDisplay.isEmpty()){
			
				displayRoot.netStyles.leftShift{
					entry {
						string(className)
						string(basePackageName + ".style.LinkStyle")
					}
				}
	
				displayRoot.editedNetStyles.leftShift{
					entry {
						string(className)
						'null'()
					}
				}
	
				displayRoot.projections.leftShift{
					'repast.simphony.scenario.data.ProjectionData' {
						id(className)
						attributes()
						type('NETWORK')
					}
				}
	
				// The modifications made to displayRoot are not observable until the xml is generated, hence the '+ 1'
				int numberOfProjections = 1 + displayRoot.projections.children().size()
				displayRoot.projectionDescriptors.leftShift{
					entry{
						string(className)
						'repast.simphony.visualization.engine.DefaultProjectionDescriptor'{
							proj(reference:"../../../../projections/repast.simphony.scenario.data.ProjectionData[$numberOfProjections]")
							props()
						}
					}
				}
				result.displayFile.write(plainPrettyPrint(displayRoot))
			}
		}
	}

	static class DefDisplayReturner {
		File displayFile
		GPathResult root
	}

	protected DefDisplayReturner findDefaultReLogoDisplayFile(String projectPath, String projectName){
		// look within project.rs folder and find ReLogo Default Display
		String sep = File.separator
		String directoryString = "${projectPath}${sep}${projectName}.rs"
		List candidateDisplayFiles = getCandidateDisplayFiles(directoryString)
		for (File file in candidateDisplayFiles){
			if (file.exists()){
				GPathResult displayRoot = new XmlSlurper().parse(file)
				if (displayRoot.name.equals(DEFAULT_RELOGO_DISPLAY_NAME)){
					return new DefDisplayReturner(displayFile:file,root:displayRoot)
				}
			}
		}
		return null
	}

	protected List<File> getCandidateDisplayFiles(String directoryString){
		List candidateDisplayFiles = []
		new File(directoryString).eachFileMatch(~/repast.simphony.action.display_.+\.xml/){ candidateDisplayFiles << it }
		return candidateDisplayFiles
	}

	private boolean isInReLogoPackage(ClassNode cn){
		String packageName = cn.getPackageName();
		if (packageName == null){
			return false;
		}
		else{
			Collection col = packageName.tokenize('.');
			for (String string : col){
				if (string.equals('relogo')){
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Extracts the instrumenting package name for a ClassNode.
	 * 
	 * E.g.,
	 * package name = "one.two.relogo.three"
	 * instumenting package name = "one.two.relogo"
	 * 
	 * If a ClassNode is not in a relogo package, returns an empty string.
	 *  
	 * @param cn
	 * @return instrumenting package name
	 */
	private String extractInstrumentingPackageName(ClassNode cn){
		if(isInReLogoPackage(cn)){
			String packageName = cn.getPackageName();
			Collection col = packageName.tokenize('.');
			StringBuilder sb = new StringBuilder()
			for (String string : col){
				if (string.equals('relogo')){
					sb.append('relogo')
					break;
				}
				else {
					sb.append(string)
					sb.append('.')
				}
			}
			return sb.toString()
		}
		else {
			return "";
		}
	}

	//TODO: make the getReLogoPackage(ClassNode cn) and isInThisReLogoPackage(ClassNode cn,String package) methods
	// see https://bugbytes.dis.anl.gov:8443/browse/SIM-468
}