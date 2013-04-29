package repast.simphony.relogo.ide.handlers;

import java.beans.Introspector;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/**
 * Creates the necessary methods PLOT classes within an instrumenting package. 
 * @author jozik
 *
 */
public class Instrumenter {

	private static final String BASE_OBSERVER = "repast.simphony.relogo.BaseObserver";
	private static final String BASE_TURTLE = "repast.simphony.relogo.BaseTurtle";
	private static final String BASE_PATCH = "repast.simphony.relogo.BasePatch";
	private static final String BASE_LINK = "repast.simphony.relogo.BaseLink";

	static private final String[] TURTLE_TURTLE_METHOD_INSTANCE_NAMES = { "turtleHatchTypesMethod",
			"turtlePatchTypesHereMethod", "turtlePatchTypesAtMethod", "tplTypesOnPMethod",
			"tplTypesOnTMethod", "tplTypesOnCMethod", // requires tplTypesOnPMethod
																								// and tplTypesOnTMethod
			"tplIsTypeQMethod", "tplTypesMethod", "tplTypeMethod" };

	static private final String[] TURTLE_PATCH_SIMPLE_METHOD_INSTANCE_NAMES = { "patchGetterField",
			"patchSetterField" };
	static private final String[] TURTLE_PATCH_ACCESSOR_METHOD_INSTANCE_NAMES = {
			"patchGetterGetter", "patchSetterSetter" };
	
	static private final String[] TURTLE_DIRECTED_LINK_METHOD_INSTANCE_NAMES = {				
		"turtleCreateTypeFromMethod",
		"turtleCreateTypesFromMethod",
		"turtleCreateTypesFromMethodCollection",
		"turtleCreateTypeToMethod",
		"turtleCreateTypesToMethod",
		"turtleCreateTypesToMethodCollection",
		"turtleInTypeNeighborQMethod",
		"turtleInTypeNeighborsMethod",
		"turtleInTypeFromMethod",
		"turtleMyInTypesMethod",
		"turtleMyOutTypesMethod",
		"turtleOutTypeNeighborQMethod",
		"turtleOutTypeNeighborsMethod",
		"turtleOutTypeToMethod",
		"turtleTypeNeighborQMethod",
		"turtleTypeNeighborsMethod",
		"turtleMyTypesMethod",
		"tplIsTypeQMethod",
		"tplTypesMethod",
		"tplLinkTypeMethod"
	};
	
	static private final String[] TURTLE_UNDIRECTED_LINK_METHOD_INSTANCE_NAMES = {
		"turtleCreateTypeFromMethodUndir",
		"turtleCreateTypesFromMethodUndir",
		"turtleCreateTypeToMethodUndir",
		"turtleCreateTypesToMethodUndir",
		"turtleCreateTypeWithMethod",
		"turtleCreateTypesWithMethod",
		"turtleCreateTypesWithMethodCollection",
		"turtleInTypeNeighborQMethodUndir",
		"turtleInTypeNeighborsMethodUndir",
		"turtleInTypeFromMethodUndir",
		"turtleMyInTypesMethodUndir",
		"turtleMyOutTypesMethodUndir",
		"turtleOutTypeNeighborQMethodUndir",
		"turtleOutTypeNeighborsMethodUndir",
		"turtleOutTypeToMethodUndir",
		"turtleTypeNeighborQMethod",
		"turtleTypeNeighborsMethod",
		"turtleTypeWithMethod",
		"turtleMyTypesMethod",
		"tplIsTypeQMethod",
		"tplTypesMethod",
		"tplTypeMethod"
	};

	private InstrumentingInformation ii;
//	private IProgressMonitor monitor;

	static protected STGroup TURTLE_INSTRUMENTING_TEMPLATE_GROUP;
	static protected String RELOGO_BUILDER_TURTLE_INSTRUMENTING = "ReLogoBuilderTurtleInstrumenting.stg";

	// protected StringTemplateGroup modelTemplateGroup;
	// static public String RELOGO_MODEL_TEMPLATE_GROUP_FILE =
	// "/templates/model.stg";
	// static protected StringTemplateGroup contextTemplateGroup;
	// static public String RELOGO_CONTEXT_TEMPLATE_GROUP_FILE =
	// "/templates/context.stg";
	// static protected StringTemplateGroup displayTemplateGroup;
	// static public String RELOGO_DISPLAY_TEMPLATE_GROUP_FILE =
	// "/templates/repast.simphony.action.display_relogoDefault.xml.stg";
	// static protected StringTemplateGroup catchAllTemplateGroup;
	// static public String RELOGO_CATCHALL_TEMPLATE_GROUP_FILE =
	// "/templates/catchAll.stg";

	public Instrumenter(InstrumentingInformation ii) {
		this.ii = ii;
//		this.monitor = monitor;
		initializeTemplates();
	}

	protected void initializeTemplates() {
		if (TURTLE_INSTRUMENTING_TEMPLATE_GROUP == null) {
			TURTLE_INSTRUMENTING_TEMPLATE_GROUP = new STGroupFile(RELOGO_BUILDER_TURTLE_INSTRUMENTING);
		}
	}

	/*public void createAllMethods(IType type) throws JavaModelException {
	// even though we're initializing looks like this is needed because the type information is cached somewhere
//		clear(type);
		
		
		if (ITypeUtils.extendsClass(type, BASE_TURTLE, monitor)) {
			createAllTurtleMethods(type);
		} else if (ITypeUtils.extendsClass(type, BASE_PATCH, monitor)) {
			fullInstrumentPatch(type);
		} else if (ITypeUtils.extendsClass(type, BASE_LINK, monitor)) {
			fullInstrumentLink(type);
		} else if (ITypeUtils.extendsClass(type, BASE_OBSERVER, monitor)) {
			fullInstrumentObserver(type);
		}

		// IDE.saveAllEditors(resourceRoots, confirm)
	}*/

	private void fullInstrumentObserver(IType type) {
		// TODO Auto-generated method stub

	}

	private void fullInstrumentLink(IType type) {
		// TODO Auto-generated method stub

	}

	private void fullInstrumentPatch(IType type) {
		// TODO Auto-generated method stub

	}


//	private void clear(IType type) throws JavaModelException {
//
//		IMethod[] methods = type.getMethods();
//		for (IMethod method : methods) {
//			method.delete(true, monitor);
//		}
//	}

	public void createAllTurtleMethods(StringBuilder sb) {
		// turtle turtle methods
		createTurtleTurtleMethods(sb);
		// turtle patch methods
		createTurtlePatchMethods(sb);
		// turtle link methods
		createTurtleLinkMethods(sb);
		// turtle observer methods

	}

	private void createTurtleLinkMethods(StringBuilder sb) {
		for (TypeSingularPluralInformation tspi : ii.getDirLinkSingularPlurals()) {
			String lowerSingular = Introspector.decapitalize(tspi.singular);
			String capSingular = StringUtils.capitalize(tspi.singular);
			String lowerPlural = Introspector.decapitalize(tspi.plural);
			String capPlural = StringUtils.capitalize(tspi.plural);

			for (String instanceName : TURTLE_DIRECTED_LINK_METHOD_INSTANCE_NAMES) {
				ST st = TURTLE_INSTRUMENTING_TEMPLATE_GROUP.getInstanceOf(instanceName);

				st.add("fullyQualifiedName", tspi.fullyQualifiedName);
				st.add("lowerSingular", lowerSingular);
				st.add("capSingular", capSingular);
				st.add("lowerPlural", lowerPlural);
				st.add("capPlural", capPlural);
				sb.append(st.render());
				sb.append("\n\n");
			}
//			type.createMethod(sb.toString(), null, true, monitor);
		}
		
	}

	private void createTurtleTurtleMethods(StringBuilder sb) {

		for (TypeSingularPluralInformation tspi : ii.getTurtleSingularPlurals()) {
			String lowerSingular = Introspector.decapitalize(tspi.singular);
			String capSingular = StringUtils.capitalize(tspi.singular);
			String lowerPlural = Introspector.decapitalize(tspi.plural);
			String capPlural = StringUtils.capitalize(tspi.plural);

			for (String instanceName : TURTLE_TURTLE_METHOD_INSTANCE_NAMES) {
				ST st = TURTLE_INSTRUMENTING_TEMPLATE_GROUP.getInstanceOf(instanceName);

				st.add("fullyQualifiedName", tspi.fullyQualifiedName);
				st.add("lowerSingular", lowerSingular);
				st.add("capSingular", capSingular);
				st.add("lowerPlural", lowerPlural);
				st.add("capPlural", capPlural);
				sb.append(st.render());
				sb.append("\n\n");
			}
//			type.createMethod(sb.toString(), null, true, monitor);
		}

	}

	private void createTurtlePatchMethods(StringBuilder sb) {
		
		if (!ii.getPatchFieldTypes().isEmpty()) {
			for (PatchTypeFieldNameFieldTypeInformation patchInfo : ii.getPatchFieldTypes()) {
				// patchType,fieldName,capFieldName,fieldType,patchGetter,patchSetter
				String patchType = patchInfo.patchType;
				String fieldName = patchInfo.fieldName;
				String capFieldName = null;
				if (fieldName.equals("")) {
					capFieldName = "";
				} else {
					capFieldName = MetaClassHelper.capitalize(patchInfo.fieldName);
				}
				String fieldType = patchInfo.fieldType;
				String patchGetter = patchInfo.patchGetter;
				String patchSetter = patchInfo.patchSetter;
				String[] instanceNames = null;
				if (patchGetter.isEmpty()) {
					instanceNames = TURTLE_PATCH_SIMPLE_METHOD_INSTANCE_NAMES;
				} else {
					instanceNames = TURTLE_PATCH_ACCESSOR_METHOD_INSTANCE_NAMES;
				}
				for (String instanceName : instanceNames) {
					ST st = TURTLE_INSTRUMENTING_TEMPLATE_GROUP.getInstanceOf(instanceName);
					st.add("patchType", patchType);
					st.add("fieldName", fieldName);
					st.add("capFieldName", capFieldName);
					st.add("fieldType", fieldType);
					st.add("patchGetter", patchGetter);
					st.add("patchSetter", patchSetter);
					sb.append(st.render());
					sb.append("\n\n");
				}
			}
//			type.createMethod(sb.toString(), null, true, monitor);
		}
	}
}
