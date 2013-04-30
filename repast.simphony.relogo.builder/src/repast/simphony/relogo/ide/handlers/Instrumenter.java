package repast.simphony.relogo.ide.handlers;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.eclipse.jdt.core.IType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/**
 * Creates the necessary methods PLOT classes within an instrumenting package.
 * 
 * @author jozik
 * 
 */
public class Instrumenter {

	private static final String BASE_OBSERVER = "repast.simphony.relogo.BaseObserver";
	private static final String BASE_TURTLE = "repast.simphony.relogo.BaseTurtle";
	private static final String BASE_PATCH = "repast.simphony.relogo.BasePatch";
	private static final String BASE_LINK = "repast.simphony.relogo.BaseLink";

	static private final String[] TURTLE_TURTLE_METHOD_INSTANCE_NAMES = { "turtleHatchTypesMethod",
			"turtlePatchTypesHereMethod", "turtleTypesAtMethod", "tplTypesOnPMethod",
			"tplTypesOnTMethod", "typesOnCMethod", // requires tplTypesOnPMethod
																								// and tplTypesOnTMethod
			"isTypeQMethod", "tplTypesMethod", "tplTypeMethod" };

	static private final String[] TURTLE_PATCH_SIMPLE_METHOD_INSTANCE_NAMES = { "patchGetterField",
			"patchSetterField" };
	static private final String[] TURTLE_PATCH_ACCESSOR_METHOD_INSTANCE_NAMES = {
			"patchGetterGetter", "patchSetterSetter" };

	static private final String[] TURTLE_DIRECTED_LINK_METHOD_INSTANCE_NAMES = {
			"turtleCreateTypeFromMethod", "turtleCreateTypesFromMethodCollection",
			"turtleCreateTypeToMethod", "turtleCreateTypesToMethodCollection",
			"turtleInTypeNeighborQMethod", "turtleInTypeNeighborsMethod", "turtleInTypeFromMethod",
			"turtleMyInTypesMethod", "turtleMyOutTypesMethod", "turtleOutTypeNeighborQMethod",
			"turtleOutTypeNeighborsMethod", "turtleOutTypeToMethod", "turtleTypeNeighborQMethod",
			"turtleTypeNeighborsMethod", "turtleMyTypesMethod", "isTypeQMethod", "tplTypesMethod",
			"tplLinkTypeMethod" };

	static private final String[] TURTLE_UNDIRECTED_LINK_METHOD_INSTANCE_NAMES = {
			"turtleCreateTypeWithMethod", "turtleCreateTypesWithMethodCollection",
			"turtleTypeNeighborQMethod", "turtleTypeNeighborsMethod", "turtleTypeWithMethod",
			"turtleMyTypesMethod", "isTypeQMethod", "tplTypesMethod", "tplLinkTypeMethod" };

	static private final String[] PATCH_TURTLE_METHOD_INSTANCE_NAMES = { "patchSproutTypesMethod",
			"turtlePatchTypesHereMethod", "patchTypesAtMethod", "tplTypesOnPMethod", "tplTypesOnTMethod",
			"typesOnCMethod", "isTypeQMethod", "tplTypesMethod", "tplTypeMethod" };

	static private final String[] PATCH_LINK_METHOD_INSTANCE_NAMES = { "isTypeQMethod",
			"tplTypesMethod", "tplLinkTypeMethod" };

	static private final String[] LINK_TURTLE_METHOD_INSTANCE_NAMES = { "tplTypesOnPMethod",
			"tplTypesOnTMethod", "typesOnCMethod", "isTypeQMethod", "tplTypeMethod",
			"tplTypesMethod" };

	static private final String[] LINK_LINK_METHOD_INSTANCE_NAMES = { "isTypeQMethod",
			"tplTypesMethod", "tplLinkTypeMethod" };
	
	static private final String[] OBSERVER_TURTLE_METHOD_INSTANCE_NAMES = {
		"observerCreateTypesMethod",
		"observerCreateOrderedTypesMethod",
		"isTypeQMethod",
		"observerTypesMethod",
		"observerTypeMethod",
		"observerTypesOnPMethod",
		"observerTypesOnTMethod",
		"typesOnCMethod"
	};
	
	static private final String[] OBSERVER_LINK_METHOD_INSTANCE_NAMES = { 
		"isTypeQMethod",
		"observerTypesMethod",
		"observerLinkTypeMethod"
	};

	private InstrumentingInformation ii;

	static protected STGroup RELOGO_INSTRUMENTING_TEMPLATE_GROUP;
	static protected String RELOGO_BUILDER_INSTRUMENTING = "ReLogoBuilderInstrumenting.stg";

	public Instrumenter(InstrumentingInformation ii) {
		this.ii = ii;
		initializeTemplates();
	}

	protected void initializeTemplates() {
		if (RELOGO_INSTRUMENTING_TEMPLATE_GROUP == null) {
			RELOGO_INSTRUMENTING_TEMPLATE_GROUP = new STGroupFile(RELOGO_BUILDER_INSTRUMENTING);
		}
	}

	/*
	 * public void createAllMethods(IType type) throws JavaModelException { //
	 * even though we're initializing looks like this is needed because the type
	 * information is cached somewhere // clear(type);
	 * 
	 * 
	 * if (ITypeUtils.extendsClass(type, BASE_TURTLE, monitor)) {
	 * createAllTurtleMethods(type); } else if (ITypeUtils.extendsClass(type,
	 * BASE_PATCH, monitor)) { fullInstrumentPatch(type); } else if
	 * (ITypeUtils.extendsClass(type, BASE_LINK, monitor)) {
	 * fullInstrumentLink(type); } else if (ITypeUtils.extendsClass(type,
	 * BASE_OBSERVER, monitor)) { fullInstrumentObserver(type); }
	 * 
	 * // IDE.saveAllEditors(resourceRoots, confirm) }
	 */

	// private void clear(IType type) throws JavaModelException {
	//
	// IMethod[] methods = type.getMethods();
	// for (IMethod method : methods) {
	// method.delete(true, monitor);
	// }
	// }

	/**
	 * Creates all the turtle methods based on the instrumenting information and
	 * appends the contents to the string builder.
	 * 
	 * @param sb
	 */
	public void createAllTurtleMethods(StringBuilder sb) {
		// turtle turtle methods
		createTurtleTurtleMethods(sb);
		// turtle patch methods
		createTurtlePatchMethods(sb);
		// turtle link methods
		createTurtleLinkMethods(sb);
		// globals methods
		createGlobalsMethods(sb);
	}

	private void createGlobalsMethods(StringBuilder sb) {
		for (String globalName : ii.getListOfGlobalFieldNames()) {
			String capName = StringUtils.capitalize(globalName);
			ST st = RELOGO_INSTRUMENTING_TEMPLATE_GROUP.getInstanceOf("globalsGetterSetter");
			st.add("name", globalName);
			st.add("capName", capName);
			sb.append(st.render());
			sb.append("\n\n");
		}
	}

	private void createTurtleLinkMethods(StringBuilder sb) {
		// Directed Links
		for (TypeSingularPluralInformation tspi : ii.getDirLinkSingularPlurals()) {
			String lowerSingular = Introspector.decapitalize(tspi.singular);
			String capSingular = StringUtils.capitalize(tspi.singular);
			String lowerPlural = Introspector.decapitalize(tspi.plural);
			String capPlural = StringUtils.capitalize(tspi.plural);

			for (String instanceName : TURTLE_DIRECTED_LINK_METHOD_INSTANCE_NAMES) {
				ST st = RELOGO_INSTRUMENTING_TEMPLATE_GROUP.getInstanceOf(instanceName);

				st.add("fullyQualifiedName", tspi.fullyQualifiedName);
				st.add("lowerSingular", lowerSingular);
				st.add("capSingular", capSingular);
				st.add("lowerPlural", lowerPlural);
				st.add("capPlural", capPlural);
				sb.append(st.render());
				sb.append("\n\n");
			}
		}
		// Undirected Links
		for (TypeSingularPluralInformation tspi : ii.getUndirLinkSingularPlurals()) {
			String lowerSingular = Introspector.decapitalize(tspi.singular);
			String capSingular = StringUtils.capitalize(tspi.singular);
			String lowerPlural = Introspector.decapitalize(tspi.plural);
			String capPlural = StringUtils.capitalize(tspi.plural);

			for (String instanceName : TURTLE_UNDIRECTED_LINK_METHOD_INSTANCE_NAMES) {
				ST st = RELOGO_INSTRUMENTING_TEMPLATE_GROUP.getInstanceOf(instanceName);

				st.add("fullyQualifiedName", tspi.fullyQualifiedName);
				st.add("lowerSingular", lowerSingular);
				st.add("capSingular", capSingular);
				st.add("lowerPlural", lowerPlural);
				st.add("capPlural", capPlural);
				sb.append(st.render());
				sb.append("\n\n");
			}
		}

	}

	private void createTurtleTurtleMethods(StringBuilder sb) {
		createClassTurtleMethods(sb,TURTLE_TURTLE_METHOD_INSTANCE_NAMES);
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
					ST st = RELOGO_INSTRUMENTING_TEMPLATE_GROUP.getInstanceOf(instanceName);
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
			// type.createMethod(sb.toString(), null, true, monitor);
		}
	}

	/**
	 * Creates all the patch methods based on the instrumenting information and
	 * appends the contents to the string builder.
	 * 
	 * @param sb
	 */
	public void createAllPatchMethods(StringBuilder sb) {
		// patch turtle methods
		createPatchTurtleMethods(sb);

		// patch link methods
		createPatchLinkMethods(sb);

		// globals methods
		createGlobalsMethods(sb);

	}

	private void createPatchTurtleMethods(StringBuilder sb) {
		createClassTurtleMethods(sb,PATCH_TURTLE_METHOD_INSTANCE_NAMES);
	}

	private void createPatchLinkMethods(StringBuilder sb) {
		createClassAllLinkMethods(sb,PATCH_LINK_METHOD_INSTANCE_NAMES);
	}

	/**
	 * Creates all the link methods based on the instrumenting information and
	 * appends the contents to the string builder.
	 * 
	 * @param sb
	 */
	public void createAllLinkMethods(StringBuilder sb) {
		// link turtle methods
		createLinkTurtleMethods(sb);

		// link link methods
		createLinkLinkMethods(sb);

		// globals methods
		createGlobalsMethods(sb);

	}

	private void createLinkTurtleMethods(StringBuilder sb) {
		createClassTurtleMethods(sb,LINK_TURTLE_METHOD_INSTANCE_NAMES);
	}
	
	private void createClassTurtleMethods(StringBuilder sb, String[] methodInstanceNames){
		for (TypeSingularPluralInformation tspi : ii.getTurtleSingularPlurals()) {
			String lowerSingular = Introspector.decapitalize(tspi.singular);
			String capSingular = StringUtils.capitalize(tspi.singular);
			String lowerPlural = Introspector.decapitalize(tspi.plural);
			String capPlural = StringUtils.capitalize(tspi.plural);

			for (String instanceName : methodInstanceNames) {
				ST st = RELOGO_INSTRUMENTING_TEMPLATE_GROUP.getInstanceOf(instanceName);

				st.add("fullyQualifiedName", tspi.fullyQualifiedName);
				st.add("lowerSingular", lowerSingular);
				st.add("capSingular", capSingular);
				st.add("lowerPlural", lowerPlural);
				st.add("capPlural", capPlural);
				sb.append(st.render());
				sb.append("\n\n");
			}
		}
	}

	private void createLinkLinkMethods(StringBuilder sb) {
		createClassAllLinkMethods(sb,LINK_LINK_METHOD_INSTANCE_NAMES);
	}
	
	private void createClassAllLinkMethods(StringBuilder sb, String[] methodInstanceNames){
		List<TypeSingularPluralInformation> allLinks = new ArrayList<>();
		allLinks.addAll(ii.getDirLinkSingularPlurals());
		allLinks.addAll(ii.getUndirLinkSingularPlurals());
		for (TypeSingularPluralInformation tspi : allLinks) {
			String lowerSingular = Introspector.decapitalize(tspi.singular);
			String capSingular = StringUtils.capitalize(tspi.singular);
			String lowerPlural = Introspector.decapitalize(tspi.plural);
			String capPlural = StringUtils.capitalize(tspi.plural);

			for (String instanceName : methodInstanceNames) {
				ST st = RELOGO_INSTRUMENTING_TEMPLATE_GROUP.getInstanceOf(instanceName);

				st.add("fullyQualifiedName", tspi.fullyQualifiedName);
				st.add("lowerSingular", lowerSingular);
				st.add("capSingular", capSingular);
				st.add("lowerPlural", lowerPlural);
				st.add("capPlural", capPlural);
				sb.append(st.render());
				sb.append("\n\n");
			}
		}
	}

	public void createAllObserverMethods(StringBuilder sb) {
		// observer turtle methods
		createObserverTurtleMethods(sb);

		// observer link methods
		createObserverLinkMethods(sb);

		// globals methods
		createGlobalsMethods(sb);

	}

	private void createObserverTurtleMethods(StringBuilder sb) {
		createClassTurtleMethods(sb,OBSERVER_TURTLE_METHOD_INSTANCE_NAMES);
	}

	private void createObserverLinkMethods(StringBuilder sb) {
		createClassAllLinkMethods(sb,OBSERVER_LINK_METHOD_INSTANCE_NAMES);
	}

}
