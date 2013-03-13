package repast.simphony.relogo.ide.handlers;

import java.beans.Introspector;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class Instrumenter {
	
	private static final String BASE_OBSERVER = "repast.simphony.relogo.BaseObserver";
	private static final String BASE_TURTLE = "repast.simphony.relogo.BaseTurtle";
	private static final String BASE_PATCH = "repast.simphony.relogo.BasePatch";
	private static final String BASE_LINK = "repast.simphony.relogo.BaseLink";
	
	private InstrumentingInformation ii;
	private IProgressMonitor monitor;
	
	protected STGroup turtleInstumentingTemplateGroup;
	static protected String RELOGO_BUILDER_TURTLE_INSTRUMENTING = "ReLogoBuilderTurtleInstrumenting.stg";
//	protected StringTemplateGroup modelTemplateGroup;
//	static public String RELOGO_MODEL_TEMPLATE_GROUP_FILE = "/templates/model.stg";
//	static protected StringTemplateGroup contextTemplateGroup;
//	static public String RELOGO_CONTEXT_TEMPLATE_GROUP_FILE = "/templates/context.stg";
//	static protected StringTemplateGroup displayTemplateGroup;
//	static public String RELOGO_DISPLAY_TEMPLATE_GROUP_FILE = "/templates/repast.simphony.action.display_relogoDefault.xml.stg";
//	static protected StringTemplateGroup catchAllTemplateGroup;
//	static public String RELOGO_CATCHALL_TEMPLATE_GROUP_FILE = "/templates/catchAll.stg";
	
	public Instrumenter(InstrumentingInformation ii, IProgressMonitor monitor){
		this.ii = ii;
		this.monitor = monitor;
		initializeTemplates();
	}
	
	protected void initializeTemplates(){
		if (turtleInstumentingTemplateGroup == null) {
//			InputStream tiTemplateStream = getClass().getResourceAsStream(
//					RELOGO_BUILDER_TURTLE_INSTRUMENTING);
//			new StringTemplateGroup(RELOGO_BUILDER_TURTLE_INSTRUMENTING);
			// TODO: fix class loading issue here.
			turtleInstumentingTemplateGroup = new STGroupFile(RELOGO_BUILDER_TURTLE_INSTRUMENTING);
//					new InputStreamReader(tiTemplateStream), DefaultTemplateLexer.class);
		}
	}
	
	public void createAllMethods(IType type) throws JavaModelException{
		clear(type);
		if (ITypeUtils.extendsClass(type, BASE_TURTLE, monitor)) {
			fullInstrumentTurtle(type);
		}
		else if (ITypeUtils.extendsClass(type, BASE_PATCH, monitor)){
			fullInstrumentPatch(type);
		}
		else if (ITypeUtils.extendsClass(type, BASE_LINK, monitor)) {
			fullInstrumentLink(type);
		}
		else if (ITypeUtils.extendsClass(type, BASE_OBSERVER, monitor)) {
			fullInstrumentObserver(type);
		}
		
//		IDE.saveAllEditors(resourceRoots, confirm)
	}

	private void fullInstrumentObserver(IType type) {
		// TODO Auto-generated method stub
		
	}

	private void fullInstrumentLink(IType type) {
		// TODO Auto-generated method stub
		
	}

	private void fullInstrumentPatch(IType type) {
		// TODO Auto-generated method stub
		
	}

	private void clear(IType type) throws JavaModelException {
		IMethod[] methods = type.getMethods();
		for (IMethod method : methods){
			method.delete(true, monitor);
		}
	}

	private void fullInstrumentTurtle(IType type) throws JavaModelException {
		// turtle turtle methods
		createTurtleTurtleMethods(type); // TODO: fix this
		// turtle patch methods
		// turtle link methods
		// turtle observer methods
		
	}

	private void createTurtleTurtleMethods(IType type) throws JavaModelException {
		//turtleHatchTypesMethod(fullyQualifiedName,lowerSingular,capSingular,lowerPlural,capPlural)
		
		for (TypeSingularPluralInformation tspi : ii.getTurtleSingularPlurals()){
			String lowerSingular = Introspector.decapitalize(tspi.singular);
			String capSingular = StringUtils.capitalize(tspi.singular);
			String lowerPlural = Introspector.decapitalize(tspi.plural);
			String capPlural = StringUtils.capitalize(tspi.plural);
			String[] instanceNames = {"turtleHatchTypesMethod","turtlePatchTypesHereMethod","turtlePatchTypesAtMethod"};
			for (String instanceName : instanceNames){
				ST st = turtleInstumentingTemplateGroup.getInstanceOf(instanceName);
				
				st.add("fullyQualifiedName", tspi.fullyQualifiedName);
				st.add("lowerSingular", lowerSingular);
				st.add("capSingular", capSingular);
				st.add("lowerPlural", lowerPlural);
				st.add("capPlural", capPlural);
				IMethod method = type.createMethod(st.render(), null, true, monitor);
			}
//			type.
//			icu.ast
			
//			method.
			
		}
		
	}

	
}
