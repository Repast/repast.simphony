package repast.simphony.relogo.ide.handlers;

import java.beans.Introspector;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.ChunkToken;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.apache.velocity.util.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class Instrumenter {
	
	private static final String BASE_OBSERVER = "repast.simphony.relogo.BaseObserver";
	private static final String BASE_TURTLE = "repast.simphony.relogo.BaseTurtle";
	private static final String BASE_PATCH = "repast.simphony.relogo.BasePatch";
	private static final String BASE_LINK = "repast.simphony.relogo.BaseLink";
	
	private InstrumentingInformation ii;
	private IProgressMonitor monitor;
	
	protected StringTemplateGroup turtleInstumentingTemplateGroup;
	static protected String RELOGO_BUILDER_TURTLE_INSTRUMENTING = "/templates/ReLogoBuilderTurtleInstrumenting.stg";
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
			InputStream tiTemplateStream = getClass().getResourceAsStream(
					RELOGO_BUILDER_TURTLE_INSTRUMENTING);
//			new StringTemplateGroup(RELOGO_BUILDER_TURTLE_INSTRUMENTING);
			// TODO: fix class loading issue here.
			turtleInstumentingTemplateGroup = new StringTemplateGroup(
					new InputStreamReader(tiTemplateStream), DefaultTemplateLexer.class);
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
//		createTurtleTurtleMethods(type); // TODO: fix this
		// turtle patch methods
		// turtle link methods
		// turtle observer methods
		
	}

	private void createTurtleTurtleMethods(IType type) throws JavaModelException {
		//turtleHatchTypesMethod(fullyQualifiedName,lowerSingular,capSingular,lowerPlural,capPlural)
		
		for (TypeSingularPluralInformation tspi : ii.getTurtleSingularPlurals()){
			String lowerSingular = Introspector.decapitalize(tspi.singular);
			String capSingular = StringUtils.capitalizeFirstLetter(tspi.singular);
			String lowerPlural = Introspector.decapitalize(tspi.plural);
			String capPlural = StringUtils.capitalizeFirstLetter(tspi.plural);
			String[] instanceNames = {"turtleHatchTypesMethod"};
			for (String instanceName : instanceNames){
				StringTemplate st = turtleInstumentingTemplateGroup.getInstanceOf(instanceName);
				st.setAttribute("fullyQualifiedName", tspi.fullyQualifiedName);
				st.setAttribute("lowerSingular", lowerSingular);
				st.setAttribute("capSingular", capSingular);
				st.setAttribute("lowerPlural", lowerPlural);
				st.setAttribute("capPlural", capPlural);
				IMethod method = type.createMethod(st.toString(), null, true, monitor);
			}
//			type.
//			icu.ast
			
//			method.
			
		}
		
	}

	
}
