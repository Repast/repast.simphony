/**
 * 
 */
package repast.simphony.relogo.ide.handlers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.jdt.groovy.model.GroovyCompilationUnit;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ide.IDE;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import repast.simphony.eclipse.util.DirectoryCleaner;
import repast.simphony.relogo.builder.Activator;

/**
 * @author Nick Collier
 * @author jozik
 */
@SuppressWarnings("restriction")
public class ReLogoBuilder extends IncrementalProjectBuilder {

	// public static final String STATECHART_EXTENSION = "rsc";
	public static final String RELOGO_BUILDER = repast.simphony.relogo.builder.Activator.PLUGIN_ID
			+ ".builder";

	private static final String SRC_ROOT_PACKAGE_FRAGMENT = "src";
	private static final String SRCGEN_ROOT_PACKAGE_FRAGMENT = "src-gen";

	private static final String PLURAL_ANNOTATION = "repast.simphony.relogo.Plural";
	private static final Pattern FULL_COMMENTED_PLURAL = Pattern
			.compile("\\s*(//)\\s*@repast\\.simphony\\.relogo\\.Plural\\([\"|'](.+)[\"|']\\)");
	private static final Pattern FULL_PLURAL = Pattern
			.compile("@repast\\.simphony\\.relogo\\.Plural\\([\"|'](.+)[\"|']\\)");
	private static final Pattern SIMPLE_COMMENTED_PLURAL = Pattern
			.compile("\\s*(//)\\s*@Plural\\([\"|'](.+)[\"|']\\)");
	private static final Pattern SIMPLE_PLURAL = Pattern.compile("@Plural\\([\"|'](.+)[\"|']\\)");

	// private static final String DIRECTED_ANNOTATION =
	// "repast.simphony.relogo.Directed";
	private static final String UNDIRECTED_ANNOTATION = "repast.simphony.relogo.Undirected";

	private static final String BASE_OBSERVER = "repast.simphony.relogo.BaseObserver";
	private static final String BASE_TURTLE = "repast.simphony.relogo.BaseTurtle";
	private static final String BASE_PATCH = "repast.simphony.relogo.BasePatch";
	private static final String BASE_LINK = "repast.simphony.relogo.BaseLink";
	private static final String ABSTRACT_GLOBALS_AND_PANEL = "repast.simphony.relogo.factories.AbstractReLogoGlobalsAndPanelFactory";
	private static final String[] TPL = {BASE_TURTLE,BASE_PATCH,BASE_LINK};
	private static final String[] TPLA = {BASE_TURTLE,BASE_PATCH,BASE_LINK,ABSTRACT_GLOBALS_AND_PANEL};
	private static final String LIB_TURTLE_ANNOTATION = "repast.simphony.relogo.ast.ExtendsLibTurtle";
	private static final String LIB_PATCH_ANNOTATION = "repast.simphony.relogo.ast.ExtendsLibPatch";
	private static final String LIB_LINK_ANNOTATION = "repast.simphony.relogo.ast.ExtendsLibLink";
	private static final String LIB_OBSERVER_ANNOTATION = "repast.simphony.relogo.ast.ExtendsLibObserver";

	private static final String[] METHODS_ARRAY = { "addGlobal", "addSlider", "addChooser",
			"addSwitch", "addSliderWL", "addChooserWL", "addSwitchWL", "addInput" };
	private static final List<String> METHODS_LIST = Arrays.asList(METHODS_ARRAY);
	private static final String[] METHODS_ARRAY2 = { "slider", "chooser", "rSwitch", "sliderWL",
			"chooserWL", "rSwitchWL", "input" };
	private static final List<String> METHODS_LIST2 = Arrays.asList(METHODS_ARRAY2);
	private static final String[] BOOLS = { "boolean", "java.lang.Boolean" };
	private static final List<String> BOOLS_LIST = Arrays.asList(BOOLS);

	private static final ClassNode abstractGlobalsAndPanel = ClassHelper
			.make("repast.simphony.relogo.factories.AbstractReLogoGlobalsAndPanelFactory");

	private static final ClassNode PLURAL_CLASSNODE = ClassHelper
			.make("repast.simphony.relogo.Plural");

	protected static STGroup reLogoOTPLTemplateGroup;
	static public String RELOGO_OTPL_CLASSES_TEMPLATE_GROUP_FILE = "/templates/reLogoOTPLclasses.stg";

	private static final String[] TEMPLATE_INSTANCES = { "reLogoTurtle", "reLogoPatch", "reLogoLink",
			"reLogoObserver" };
	private static final String[] DEFAULT_RELOGO_FILENAMES = { "ReLogoTurtle.java",
			"ReLogoPatch.java", "ReLogoLink.java", "ReLogoObserver.java" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int,
	 * java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {
		try {
			if (kind == IncrementalProjectBuilder.FULL_BUILD) {
				fullBuild(monitor);
			} else {
				IResourceDelta delta = getDelta(getProject());
				if (delta == null) {
					fullBuild(monitor);
				} else {
					incrementalBuild(delta, monitor);
				}
			}
		} catch (CoreException ex) {
			throw new CoreException(new Status(IResourceStatus.BUILD_FAILED, Activator.PLUGIN_ID,
					ex.getLocalizedMessage(), ex));
		}

		return null;
	}

	private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor)
			throws CoreException {
		InstrumentationInformationDeltaVisitor iidv = new InstrumentationInformationDeltaVisitor(getProject(), monitor); 
		delta.accept(iidv);
		if (iidv.needsRebuild){
			fullBuild(monitor);
		}
		// look for changes to trigger full build.
	}

	private void fullBuild(IProgressMonitor monitor) throws CoreException {
		IProject project = getProject();
		FullBuildInstrumentationInformationVisitor visitor = new FullBuildInstrumentationInformationVisitor(
				project, monitor);
		project.accept(visitor);
//		System.out.println(visitor.iih);
//		System.out.println(visitor.cih);
		visitor.removeReLogoBuilderFiles();
		visitor.createClassSources();

		getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

	static public ReLogoResourceResult examineResourceReLogo(IResource resource) throws CoreException {
		ReLogoResourceResult rrr = new ReLogoResourceResult();
		Object obj = resource.getAdapter(IJavaElement.class);
		if (obj != null) {
			IJavaElement javaElement = (IJavaElement) obj;
			IPackageFragmentRoot pfr = (IPackageFragmentRoot) javaElement
					.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
			if (pfr != null && pfr.getElementName().equals(SRC_ROOT_PACKAGE_FRAGMENT)) {
				IJavaElement parent = javaElement.getParent();
				if (parent != null && parent instanceof IPackageFragment) {
					IPackageFragment pf = (IPackageFragment) parent;
					String packageName = pf.getElementName();
					String instrumentingPackageName = getInstrumentingPackageName(packageName);
					if (instrumentingPackageName != null) {
						rrr.isInReLogoPackage = true;
						rrr.instrumentingPackageName = instrumentingPackageName;
					}
				}
			}
		}
		return rrr;
	}

	/**
	 * Returns the instrumenting package name associated with the package or null
	 * if the packageName is not a valid package that contains 'relogo' in it or
	 * if it's null.
	 * 
	 * @param packageName
	 * @return
	 */
	static protected String getInstrumentingPackageName(String packageName) {
		if (packageName != null) {
			String[] segments = packageName.split("\\.");
			boolean isFirst = true;
			StringBuilder sb = new StringBuilder();
			for (String string : segments) {
				if (string.equals("relogo")) {
					return sb.toString();
				} else {
					if (!isFirst) {
						sb.append(".");
					}
					isFirst = false;
					sb.append(string);
				}
			}
		}
		return null;
	}

	public static class ReLogoResourceResult {
		public boolean isInReLogoPackage() {
			return isInReLogoPackage;
		}

		public void setInReLogoPackage(boolean isInReLogoPackage) {
			this.isInReLogoPackage = isInReLogoPackage;
		}

		public String getInstrumentingPackageName() {
			return instrumentingPackageName;
		}

		public void setInstrumentingPackageName(String instrumentingPackageName) {
			this.instrumentingPackageName = instrumentingPackageName;
		}

		boolean isInReLogoPackage = false;
		String instrumentingPackageName = "";
	}

	protected static class FullBuildInstrumentationInformationVisitor implements IResourceVisitor {
		IProgressMonitor monitor;
		IProject project;
		InstrumentingInformationHolder iih = new InstrumentingInformationHolder();
		GeneratedFilesInformationHolder cih = new GeneratedFilesInformationHolder();

		public FullBuildInstrumentationInformationVisitor(IProject project, IProgressMonitor monitor) {
			this.monitor = monitor;
			this.project = project;
		}

		/**
		 * Initializes all the existing generated source files. This enables a clean
		 * and rebuild to fix any errors caused by any mistake that would cause the
		 * java model to not parse the source files properly (e.g., errant curly
		 * brace).
		 */
		public void createClassSources() {

			for (String instrumentingPackageName : cih.instrumentingPackageNames) {
				try {
					generateDefaultReLogoFiles(instrumentingPackageName);
				} catch (UnsupportedEncodingException | CoreException e) {
					e.printStackTrace();
				}
			}

			// TODO: do the library extending classes here
		}

		private void generateDefaultReLogoFiles(String instrumentingPackageName)
				throws UnsupportedEncodingException, CoreException {
			String[] packageNames = instrumentingPackageName.split("\\.");
			IFolder srcGenNewFolder = project.getFolder(SRCGEN_ROOT_PACKAGE_FRAGMENT);
			if(!srcGenNewFolder.exists()){
				
				IJavaProject javaProject = JavaCore.create(project);
				IPath srcPath = javaProject.getPath().append(SRCGEN_ROOT_PACKAGE_FRAGMENT + "/");
				srcGenNewFolder.create(true, true, monitor);
	      IClasspathEntry[] entries = javaProject.getRawClasspath();
	      boolean found = false;
	      for (IClasspathEntry entry : entries) {
	        if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE && entry.getPath().equals(srcPath)) {
	          found = true;
	          break;
	        }
	      }

	      if (!found) {
	        IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];
	        System.arraycopy(entries, 0, newEntries, 0, entries.length);
	        IClasspathEntry srcEntry = JavaCore.newSourceEntry(srcPath, null);
	        newEntries[entries.length] = srcEntry;
	        javaProject.setRawClasspath(newEntries, null);
	      }
			}
			for (String subpackage : packageNames) {
				srcGenNewFolder = createFolderResource(srcGenNewFolder, subpackage);
			}
			if (reLogoOTPLTemplateGroup == null) {
				reLogoOTPLTemplateGroup = new STGroupFile(RELOGO_OTPL_CLASSES_TEMPLATE_GROUP_FILE);
			}
			Instrumenter instrumenter = new Instrumenter(
					iih.getInstrumentingInformationFor(instrumentingPackageName));
			for (int i = 0; i < TEMPLATE_INSTANCES.length; i++) {
				ST st = reLogoOTPLTemplateGroup.getInstanceOf(TEMPLATE_INSTANCES[i]);
				st.add("packageName", instrumentingPackageName);
				StringBuilder sb = new StringBuilder();
				String fileName = DEFAULT_RELOGO_FILENAMES[i];
				if (fileName.contains("Turtle")) {
					instrumenter.createAllTurtleMethods(sb);
				} else if (fileName.contains("Patch")) {
					instrumenter.createAllPatchMethods(sb);
				} else if (fileName.contains("Link")) {
					instrumenter.createAllLinkMethods(sb);
				} else { // observer
					instrumenter.createAllObserverMethods(sb);
				}

				st.add("classBody", sb.toString());
				IFile localFile = srcGenNewFolder.getFile(fileName);

				// For creation:
				if (!localFile.exists()){ 
					localFile.create(new ByteArrayInputStream(st.render().getBytes("UTF-8")), true, null);
				}
				else{// It shouldn't exist, but just in case it does
					localFile.setContents(new ByteArrayInputStream(st.render().getBytes("UTF-8")), true,true,null);
				}

				final IFile fileToSave = localFile;
				final IResource[] resources = { fileToSave };
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IDE.saveAllEditors(resources, false);
					}
				});
			}
		}

		/**
		 * Code snippet from Eclipse wiki showing how to create resources for a
		 * project, a folder, and a file.
		 */
		private IFolder createFolderResource(IFolder parentFolder, String name) {
			IFolder folder = parentFolder.getFolder(name);
			// at this point, no resources have been created
			if (!folder.exists()) {
				try {
					folder.create(IResource.NONE, true, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
			return folder;
		}

		/**
		 * Create a new file in the specified folder, containing the specified
		 * contents. Forces a write of the contents to the file system. TODO: delete
		 * if not used when ReLogo2 completed
		 */
		private void createFileResource(IFolder folder, String name, InputStream contents) {
			IFile file = folder.getFile(name);
			// at this point, no resources have been created
			if (!file.exists()) {
				try {
					file.create(contents, IResource.FORCE, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}

		@SuppressWarnings("restriction")
		protected IType getSuperType(IType type) throws JavaModelException {
			String superTypeName = type.getSuperclassName();
			String[][] resolved = type.resolveType(superTypeName);
			if (resolved != null && resolved.length > 0) {
				String fullyQualifiedSuperTypeName = StringUtils.join(resolved[0], '.');

				IType[] types = JavaModelUtil.getAllSuperTypes(type, monitor);
				for (IType t : types) {
					if (t.getFullyQualifiedName().equals(fullyQualifiedSuperTypeName))
						return t;
				}

			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core
		 * .resources.IResource)
		 */
		@Override
		public boolean visit(IResource resource) throws CoreException {
			IPath path = resource.getRawLocation();
			if (path != null && path.getFileExtension() != null
					&& (path.getFileExtension().equals("groovy") || path.getFileExtension().equals("java"))) {
				ReLogoResourceResult rrr = examineResourceReLogo(resource);
				if (rrr.isInReLogoPackage) {

					// Get relevant instrumenting package name
					// This is the package name under which all the relogo
					// classes share instrumentation.

					String instrumentingPackageName = rrr.instrumentingPackageName;
					boolean targetFound = false;
					Object obj = resource.getAdapter(IJavaElement.class);
					if (obj != null) {
						IJavaElement javaElement = (IJavaElement) obj;
						if (javaElement.getElementType() == IJavaElement.COMPILATION_UNIT) {
							ICompilationUnit cu = (ICompilationUnit) javaElement;
							for (IType type : cu.getTypes()) {
								if (ITypeUtils.extendsClass(type, BASE_TURTLE, monitor)) {
									targetFound = true;
									// find plural
									TypeSingularPluralInformation pi = getPluralInformation(type);
									if (pi != null) {
										iih.putTurtlePluralInformation(pi, instrumentingPackageName);
									}

								} else if (ITypeUtils.extendsClass(type, BASE_PATCH, monitor)) {
									targetFound = true;
									// find fields and type info
									List<PatchTypeFieldNameFieldTypeInformation> patchFieldTypes = getPatchFieldTypes(type);
									// put in iih
									iih.putPatchFieldTypes(patchFieldTypes, instrumentingPackageName);

								} else if (ITypeUtils.extendsClass(type, BASE_LINK, monitor)) {
									targetFound = true;
									// find plural
									TypeSingularPluralInformation pi = getPluralInformation(type);
									if (pi != null) {
										// find if it's directed or undirected
										boolean isDir = isDir(type);
										if (isDir) {
											iih.putDirLinkPluralInformation(pi, instrumentingPackageName);
										} else {
											iih.putUndirLinkPluralInformation(pi, instrumentingPackageName);
										}

										// check to see if context and display files need
										// modification
										// and modify if necessary
										checkContextAndDisplayFiles(type);
									}

								} else if (ITypeUtils.extendsClass(type, ABSTRACT_GLOBALS_AND_PANEL, monitor)) {
									targetFound = true;
									List<String> listOfGlobalFields = getGlobalFields(type);
									iih.putGlobalsInfo(listOfGlobalFields, instrumentingPackageName);
								} else if (ITypeUtils.extendsClass(type, BASE_OBSERVER, monitor)) {
									targetFound = true;
								}
							}
							if (targetFound) {
								// If a candidate PLOT or Globals class was found, add
								// the instrumentingPackageName into the cleaning information.
								cih.addInstrumentingPackageName(instrumentingPackageName);
							}
						}
					}
				}

			}
			return true;
		}

		public void removeReLogoBuilderFiles() {

			GeneratedByReLogoBuilderFilter filter = new GeneratedByReLogoBuilderFilter();
			DirectoryCleaner cleaner = new DirectoryCleaner(filter);
			String rootPath = project.getLocation().append(SRCGEN_ROOT_PACKAGE_FRAGMENT).toFile()
					.getAbsolutePath();
			cleaner.run(rootPath);
		}

		private List<String> getGlobalFields(IType type) throws JavaModelException {
			final List<String> globalFields = new ArrayList<String>();
			// get compilation unit
			if (!type.isBinary()) {
				// TODO: do we need a Java version as well?
				// probably not.
				if (type.getCompilationUnit() instanceof GroovyCompilationUnit) {

					GroovyCompilationUnit icu = (GroovyCompilationUnit) type.getCompilationUnit();

					ModuleNode moduleNode = icu.getModuleNode();
					if (moduleNode != null) {
						List<ClassNode> classNodes = moduleNode.getClasses();
						// Usually there will be only one ClassNode per file but just in
						// case there are multiple classes defined in one file
						for (ClassNode classNode : classNodes) {

							if (classNode.isDerivedFrom(abstractGlobalsAndPanel)) {
								MethodNode addPanelComponentsMethod = classNode.getMethod(
										"addGlobalsAndPanelComponents", Parameter.EMPTY_ARRAY);
								if (addPanelComponentsMethod != null) {
									BlockStatement block = (BlockStatement) addPanelComponentsMethod.getCode();
									block.visit(new CodeVisitorSupport() {

										@Override
										public void visitMethodCallExpression(MethodCallExpression mce) {

											String methodString = mce.getMethodAsString();
											// List<String> methodsList = new ArrayList<String>();
											if (METHODS_LIST.contains(methodString)) {
												Expression argumentsExpression = mce.getArguments();
												if (argumentsExpression != null
														&& argumentsExpression instanceof ArgumentListExpression) {
													List arguments = ((ArgumentListExpression) argumentsExpression)
															.getExpressions();
													if (arguments.get(0) instanceof ConstantExpression) {
														ConstantExpression ce = (ConstantExpression) arguments.get(0);
														Object val = ce.getValue();
														if (val instanceof String) {
															globalFields.add((String) val);
														}
													}
												}
											}
										}

										@Override
										public void visitBinaryExpression(BinaryExpression be) {
											Expression re = be.getRightExpression();
											if (re != null && re instanceof MethodCallExpression) {
												MethodCallExpression mce = (MethodCallExpression) re;
												String methodString = mce.getMethodAsString();
												if (METHODS_LIST2.contains(methodString)) {
													Expression argumentsExpression = mce.getArguments();
													if (argumentsExpression != null
															&& argumentsExpression instanceof ArgumentListExpression) {
														List arguments = ((ArgumentListExpression) argumentsExpression)
																.getExpressions();
														if (arguments.get(0) instanceof ConstantExpression) {
															ConstantExpression ce = (ConstantExpression) arguments.get(0);
															Object val = ce.getValue();
															if (val instanceof String) {
																globalFields.add((String) val);
															}
														}
													}
												}
											}
										}
									}); // end of CodeVisitorSupport
								}
							}
						}
					}
				}
			}
			return globalFields;
		}

		private void checkContextAndDisplayFiles(IType type) {

			try {
				ContextAndDisplayUtilsGroovy.checkToModifyContextFile(project, type, monitor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private List<PatchTypeFieldNameFieldTypeInformation> getPatchFieldTypes(IType type)
				throws JavaModelException {

			List<PatchTypeFieldNameFieldTypeInformation> patchFieldTypes = new ArrayList<PatchTypeFieldNameFieldTypeInformation>();
			while (type != null && !type.getFullyQualifiedName().equals(BASE_PATCH)) {
				List<PatchTypeFieldNameFieldTypeInformation> individualPatchFieldTypes = getIndividualPatchFieldTypes(type);
				if (individualPatchFieldTypes != null) {
					patchFieldTypes.addAll(individualPatchFieldTypes);
				}

				// System.out.println("Type: " + type.getElementName());
				type = getSuperType(type);
				// System.out.println("Supertype: " + type.getElementName());

			}
			return patchFieldTypes;
		}

		protected List<PatchTypeFieldNameFieldTypeInformation> getIndividualPatchFieldTypes(IType type)
				throws JavaModelException {
			List<PatchTypeFieldNameFieldTypeInformation> individualPatchFieldTypes = new ArrayList<PatchTypeFieldNameFieldTypeInformation>();
			// First check to see if this type is in a 'relogo' package
			// Skip otherwise.
			IPackageFragment ipf = type.getPackageFragment();
			if (ipf != null) { // if ipf is null, skip this type
				String instrumentingPackageName = getInstrumentingPackageName(ipf.getElementName());
				if (instrumentingPackageName != null) { // if it's null, skip this
																								// type
					// Note: in the unlikely case that there are more than one patches
					// in the 'relogo' package
					// and one inherits from the other, this bit of code prevents double
					// counting
					// of patch properties
					boolean patchAlreadyProcessed = false;
					InstrumentingInformation ii = iih
							.getInstrumentingInformationFor(instrumentingPackageName);
					if (ii != null) {
						String fullyQualifiedName = type.getFullyQualifiedName();
						for (PatchTypeFieldNameFieldTypeInformation pi : ii.getPatchFieldTypes()) {
							if (pi.patchType.equals(fullyQualifiedName)) {
								patchAlreadyProcessed = true;
								break;
							}
						}
					}

					if (!patchAlreadyProcessed) {
						boolean isGroovySource = false;
						if (!type.isBinary()) {
							if (type.getCompilationUnit() instanceof GroovyCompilationUnit) {
								isGroovySource = true;
							}
						}

						IField[] fields = type.getFields();
						IMethod[] methods = type.getMethods();
						// List<String> publicMethodNames = new ArrayList<String>();// may
						// become
						// unnecessary
						List<String> nonPublicMethodNames = new ArrayList<String>();
						// Gather non-public methods
						for (IMethod method : methods) {
							if (!Flags.isPublic(method.getFlags())) {
								nonPublicMethodNames.add(method.getElementName());
							}
						}

						List<PropertyInfo> patchProperties = null;
						try {
							patchProperties = getPatchProperties(type);
						} catch (JavaModelException jme) {
							// ignore
						}
						boolean previousField = false;
						for (IField field : fields) {
							boolean foundField = false;
							boolean needsGetterResolve = false;
							int flags = field.getFlags();

							String fieldName = field.getElementName();
							String capitalizedFieldName = MetaClassHelper.capitalize(fieldName);

							String isGetter = "is" + capitalizedFieldName;
							String getGetter = "get" + capitalizedFieldName;
							String setSetter = "set" + capitalizedFieldName;

							// System.out.println("Field : " + field.getElementName() +
							// " Flags: "
							// + flags
							// + " toString: " + Flags.toString(flags));

							// String getterName = "";
							// String setterName = "";
							if (!Flags.isPublic(flags)) { // potentially cases 1a, 1b

								if (Flags.isPrivate(flags) && isGroovySource) { // potentially
																																// cases 1a &
																																// 1b
									// package default is seen as private in groovy source
									// so need to check if it's truly private or not
									String source = field.getSource();
									if (source != null) {

										// This is to differentiate between a default visibility
										// and an actual private field within groovy source
										if (!source.trim().startsWith("private ")) {
											// This is to check if this is part of a comma separated
											// list
											// of fields
											// since in that case the non-first elements won't show
											// the
											// private modifier
											if (source.trim().equals(field.getElementName())) {
												// check to see if the previous field was found
												foundField = previousField;
											} else {
												foundField = true;
											}
											if (foundField) {
												// check to see that there are no corresponding
												// non-public accessors
												if (nonPublicMethodNames.contains(isGetter)
														|| nonPublicMethodNames.contains(getGetter)
														|| nonPublicMethodNames.contains(setSetter)) { // case
																																						// 1b
													foundField = false;
												} else { // case 1a
													needsGetterResolve = true; // Groovy generated
																											// accessors
												}
											}
										}
									}
								}
							} else { // case 2
								foundField = true;
							}

							if (foundField) {
								try {
									PatchTypeFieldNameFieldTypeInformation patchInfo = getPatchTypeFieldNameFieldTypeInformation(
											field, type);
									if (needsGetterResolve) {
										String localGetterName = null;
										if (BOOLS_LIST.contains(patchInfo.fieldType)) {
											localGetterName = isGetter;
										} else {
											localGetterName = getGetter;
										}
										String localSetterName = setSetter;
										patchInfo.patchGetter = localGetterName;
										patchInfo.patchSetter = localSetterName;
									}
									individualPatchFieldTypes.add(patchInfo);
								} catch (IllegalArgumentException iae) {
									// if IllegalArgumentException is caught, quietly ignore
									// this
									// field
								}
							}
							previousField = foundField;
						}
						if (patchProperties != null) {
							// look for cases 3 and 4
							for (PropertyInfo pi : patchProperties) {
								String setterMethod = pi.writeMethod;
								String getterMethod = pi.readMethod;
								String propertyType = pi.propertyType;
								boolean propertyFound = false;
								for (PatchTypeFieldNameFieldTypeInformation patchInfo : individualPatchFieldTypes) {
									if (patchInfo.patchSetter.equals(setterMethod)) {
										propertyFound = true;
										break;
									}
								}
								if (!propertyFound) {
									PatchTypeFieldNameFieldTypeInformation newPatchInfo = new PatchTypeFieldNameFieldTypeInformation(
											type.getFullyQualifiedName(), "", propertyType, getterMethod, setterMethod);
									individualPatchFieldTypes.add(newPatchInfo);
								}
							}
						}
					}
				}
			}
			return individualPatchFieldTypes;
		}

		/**
		 * Gathers all the properties in this type. Properties have both accessors
		 * conforming to the JavaBean convention and both public.
		 * 
		 * @param type
		 * @return list of property infos
		 * @throws JavaModelException
		 */
		private List<PropertyInfo> getPatchProperties(IType type) throws JavaModelException {
			List<PropertyInfo> properties = new ArrayList<PropertyInfo>();
			IMethod[] methods = type.getMethods();
			List<IMethod> publicMethods = new ArrayList<IMethod>();
			for (IMethod method : methods) {
				if (Flags.isPublic(method.getFlags())) {
					publicMethods.add(method);
				}
			}
			for (IMethod candidateSetterMethod : publicMethods) {
				String candidateSetterMethodName = candidateSetterMethod.getElementName();
				if (candidateSetterMethodName != null && candidateSetterMethodName.startsWith("set")
						&& candidateSetterMethodName.length() > 3) {
					String getterSuffix = candidateSetterMethodName.substring(3);
					for (IMethod candidateGetterMethod : publicMethods) {
						String candidateGetterMethodName = candidateGetterMethod.getElementName();
						if (candidateGetterMethodName != null
								&& candidateGetterMethodName.endsWith(getterSuffix)
								&& (candidateGetterMethodName.equals("is" + getterSuffix) || candidateGetterMethodName
										.equals("get" + getterSuffix))) {
							// Check to see that it's a one parameter method
							if (candidateSetterMethod.getNumberOfParameters() == 1) {
								// Extract the argument type
								String parameterSignature = candidateSetterMethod.getParameterTypes()[0];
								String parameterTypeName = null;
								try {
									parameterTypeName = getFullResolvedName(parameterSignature, type);
								} catch (JavaModelException jme) {
									// ignore
								}
								if (parameterTypeName != null) {
									String returnSignature = candidateGetterMethod.getReturnType();
									String returnTypeName = null;
									try {
										returnTypeName = getFullResolvedName(returnSignature, type);
									} catch (JavaModelException jme) {
										// ignore
									}
									// Check if return type of getter matches the parameter type
									// of setter
									if (returnTypeName != null && returnTypeName.equals(parameterTypeName)) {
										properties.add(new PropertyInfo(candidateGetterMethodName,
												candidateSetterMethodName, returnTypeName));
									}
								}
							}
						}
					}
				}
			}
			return properties;
		}

		/**
		 * Gets full resolved name, including type arguments, from type signatures.
		 * 
		 * @param typeSignature
		 * @param type
		 *          enclosing type
		 * @return
		 * @throws JavaModelException
		 * @throws IllegalArgumentException
		 */
		protected String getFullResolvedName(String typeSignature, IType type)
				throws JavaModelException, IllegalArgumentException {
			// get num arrays
			int numArrays = Signature.getArrayCount(typeSignature);

			// remove arrays
			String nonArraySignature = Signature.getElementType(typeSignature);

			int simpleSignatureKind = Signature.getTypeSignatureKind(nonArraySignature);
			StringBuilder sb = new StringBuilder();
			if (simpleSignatureKind == Signature.BASE_TYPE_SIGNATURE) {
				sb.append(Signature.toString(nonArraySignature));
			} else {
				String[][] resolved = type.resolveType(Signature.toString(nonArraySignature));
				String fieldType = "java.lang.Object";
				if (resolved != null && resolved.length > 0) {
					if (resolved[0][0].isEmpty()) {
						fieldType = resolved[0][1];
					} else {
						fieldType = StringUtils.join(resolved[0], '.');
					}
				}
				sb.append(fieldType);
				String[] typeArgs = Signature.getTypeArguments(nonArraySignature);
				if (typeArgs.length > 0) {
					sb.append("<");
					boolean isFirst = true;
					for (String typeArg : typeArgs) {
						if (!isFirst)
							sb.append(",");
						sb.append(getFullResolvedName(typeArg, type));
						isFirst = false;
					}
					sb.append(">");
				}

			}
			// add removed arrays
			for (int i = 0; i < numArrays; i++) {
				sb.append("[]");
			}

			return sb.toString();

		}

		private PatchTypeFieldNameFieldTypeInformation getPatchTypeFieldNameFieldTypeInformation(
				IField field, IType type, String patchGetterName, String patchSetterName)
				throws IllegalArgumentException, JavaModelException {
			String fieldTypeSignature = field.getTypeSignature();
			return new PatchTypeFieldNameFieldTypeInformation(type.getFullyQualifiedName(),
					field.getElementName(), getFullResolvedName(fieldTypeSignature, type), patchGetterName,
					patchSetterName);
		}

		private PatchTypeFieldNameFieldTypeInformation getPatchTypeFieldNameFieldTypeInformation(
				IField field, IType type) throws IllegalArgumentException, JavaModelException {
			return getPatchTypeFieldNameFieldTypeInformation(field, type, "", "");
		}

		private TypeSingularPluralInformation getPluralInformation(IType type)
				throws JavaModelException {
			ICompilationUnit cu = type.getCompilationUnit();
			if (cu == null)
				return null;
			if (cu instanceof GroovyCompilationUnit) {
				return getPluralInformationFromGroovySource(type);
			}
			return getPluralInformationFromType(type);
		}

		private String getValueFromAnnotation(IAnnotation annot) throws JavaModelException {
			IMemberValuePair[] mvps = annot.getMemberValuePairs();
			for (IMemberValuePair mvp : mvps) {
				if (mvp.getMemberName().equals("value")) {
					return (String) mvp.getValue();
				}
			}
			return "";
		}

		private TypeSingularPluralInformation getPluralInformationFromType(IType type)
				throws JavaModelException {
			TypeSingularPluralInformation pi = new TypeSingularPluralInformation(type);
			for (IAnnotation annot : type.getAnnotations()) {
				// If the annotation is the fully qualified name
				if (annot.getElementName().equals(PLURAL_ANNOTATION)) {
					pi.plural = getValueFromAnnotation(annot);
					return pi;
				}
				String[][] resolve = type.resolveType(annot.getElementName());
				for (String[] res : resolve) {
					if (StringUtils.join(res, '.').equals(PLURAL_ANNOTATION)) {
						pi.plural = getValueFromAnnotation(annot);
						return pi;
					}
				}
			}
			return pi;
		}

		private boolean isDir(IType type) throws JavaModelException {
			for (IAnnotation annot : type.getAnnotations()) {
				// If the annotation is the fully qualified name
				if (annot.getElementName().equals(UNDIRECTED_ANNOTATION)) {
					return false;
				}
				String[][] resolve = type.resolveType(annot.getElementName());
				for (String[] res : resolve) {
					if (StringUtils.join(res, '.').equals(UNDIRECTED_ANNOTATION)) {
						return false;
					}
				}
			}
			// If no annotation exists or if @Undirected doesn't exist -> @Directed
			return true;
		}

		private TypeSingularPluralInformation getPluralInformationFromGroovySource(IType type)
				throws JavaModelException {
			TypeSingularPluralInformation pi = new TypeSingularPluralInformation(type);
			ICompilationUnit icu = type.getCompilationUnit();
			if (icu instanceof GroovyCompilationUnit) {
				GroovyCompilationUnit gcu = (GroovyCompilationUnit) icu;
				ModuleNode moduleNode = gcu.getModuleNode();
				if (moduleNode != null) {
					List<ClassNode> classNodes = moduleNode.getClasses();
					// Usually there will be only one ClassNode per file but just in
					// case there are multiple classes defined in one file
					for (ClassNode classNode : classNodes) {
						List<AnnotationNode> annots = classNode.getAnnotations(PLURAL_CLASSNODE);
						for (AnnotationNode annot : annots) {
							Expression e = annot.getMember("value");
							if (e instanceof ConstantExpression) {
								Object pluralVal = ((ConstantExpression) e).getValue();
								if (pluralVal instanceof String) {
									pi.plural = (String) pluralVal;
								}
							}
						}
					}
				}
			}
			return pi;
		}

	}

	private static class InstrumentationInformationDeltaVisitor implements IResourceDeltaVisitor {
		IProgressMonitor monitor;
		IProject project;
		boolean needsRebuild = false; // TODO: use this

		public InstrumentationInformationDeltaVisitor(IProject project, IProgressMonitor monitor) {
			this.monitor = monitor;
			this.project = project;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core
		 * .resources.IResourceDelta)
		 */
		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			if (resource != null) {
				IPath path = resource.getRawLocation();
				// System.out.println("statechart builder running: " + delta);
				if (path != null && path.getFileExtension() != null
						&& (path.getFileExtension().equals("groovy") || path.getFileExtension().equals("java"))) {

					ReLogoResourceResult rrr = examineResourceReLogo(resource);
					if (rrr.isInReLogoPackage) {

						if (delta.getKind() == IResourceDelta.ADDED) {
							 
							if (doesResourceExtendThese(resource,TPL)){
								needsRebuild = true;
//								System.out.println("Needs Rebuild: Added Delta: Found " + delta.getResource().getName());
							}

						} else if (delta.getKind() == IResourceDelta.REMOVED) {
							// can't easily check extending class so need to fire this always
							needsRebuild = true;
//							System.out.println("Needs Rebuild: Removed Delta: Found " + delta.getResource().getName());
						} else if (delta.getKind() == IResourceDelta.CHANGED) {
							if (doesResourceExtendThese(resource,TPLA)){
								needsRebuild = true;
//								System.out.println("Needs Rebuild: Changed Delta: Found " + delta.getResource().getName());
							}
						}
					}
				}
			}
			return true;
		}

		private boolean doesResourceExtendThese(IResource resource, String[] fullyQualifiedNames) {
			Object obj = resource.getAdapter(IJavaElement.class);
			if (obj != null) {
				IJavaElement javaElement = (IJavaElement) obj;
				if (javaElement.getElementType() == IJavaElement.COMPILATION_UNIT) {
					ICompilationUnit cu = (ICompilationUnit) javaElement;
					IType[] types = null;
					try {
						types = cu.getTypes();
					} catch (JavaModelException e) {
						return false;
					}
					if (types != null) {
						for (IType type : types) {
							for (String fullyQualifiedName : fullyQualifiedNames) {
								if (extendsClass(type, fullyQualifiedName)) {
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}

		private boolean extendsClass(IType type, String fullyQualifiedName) {
			try {
				return ITypeUtils.extendsClass(type, fullyQualifiedName, monitor);
			} catch (JavaModelException e) {
				return false;
			}
		}
	}
}
