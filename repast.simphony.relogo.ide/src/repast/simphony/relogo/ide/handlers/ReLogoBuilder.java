/**
 * 
 */
package repast.simphony.relogo.ide.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jdt.groovy.model.GroovyCompilationUnit;
import org.eclipse.core.resources.IFile;
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
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;

import relogo.Activator;
import repast.simphony.util.collections.Pair;

/**
 * @author Nick Collier
 * @author jozik
 */
@SuppressWarnings("restriction")
public class ReLogoBuilder extends IncrementalProjectBuilder {

	// public static final String STATECHART_EXTENSION = "rsc";
	public static final String RELOGO_BUILDER = Activator.PLUGIN_ID + ".builder";

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

	private static final String DIRECTED_ANNOTATION = "repast.simphony.relogo.Directed";
	private static final String UNDIRECTED_ANNOTATION = "repast.simphony.relogo.Undirected";

	private static final String BASE_OBSERVER = "repast.simphony.relogo.BaseObserver";
	private static final String BASE_TURTLE = "repast.simphony.relogo.BaseTurtle";
	private static final String BASE_PATCH = "repast.simphony.relogo.BasePatch";
	private static final String BASE_LINK = "repast.simphony.relogo.BaseLink";
	private static final String ABSTRACT_GLOBALS_AND_PANEL = "repast.simphony.relogo.factories.AbstractReLogoGlobalsAndPanelFactory";
	private static final String LIB_TURTLE_ANNOTATION = "repast.simphony.relogo.ast.ExtendsLibTurtle";
	private static final String LIB_PATCH_ANNOTATION = "repast.simphony.relogo.ast.ExtendsLibPatch";
	private static final String LIB_LINK_ANNOTATION = "repast.simphony.relogo.ast.ExtendsLibLink";
	private static final String LIB_OBSERVER_ANNOTATION = "repast.simphony.relogo.ast.ExtendsLibObserver";

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
		delta.accept(new InstrumentationInformationDeltaVisitor(getProject(), monitor));
	}

	private void fullBuild(IProgressMonitor monitor) throws CoreException {
		FullBuildInstrumentationInformationVisitor visitor = new FullBuildInstrumentationInformationVisitor(
				getProject(), monitor);
		getProject().accept(visitor);
//		System.out.println(visitor.iih);
	}

	protected static class FullBuildInstrumentationInformationVisitor implements IResourceVisitor {
		IProgressMonitor monitor;
		IProject project;
		InstrumentingInformationHolder iih = new InstrumentingInformationHolder();

		public FullBuildInstrumentationInformationVisitor(IProject project, IProgressMonitor monitor) {
			this.monitor = monitor;
			this.project = project;
		}

		// private IPackageFragment findIPackageFragmentParent(IResource resource) {
		// Object obj = resource.getAdapter(IJavaElement.class);
		// if (obj != null) {
		// IJavaElement javaElement = (IJavaElement) obj;
		// IJavaElement jElePFrag = javaElement.getParent();
		// if (jElePFrag != null && jElePFrag instanceof IPackageFragment) {
		// return (IPackageFragment) jElePFrag;
		// }
		// }
		// return null;
		// }

		private static class ReLogoResourceResult {
			boolean isInReLogoPackage = false;
			String instrumentingPackageName = "";
		}

		protected String getInstrumentingPackageName(String packageName) {
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

		private ReLogoResourceResult examineResourceReLogo(IResource resource) throws CoreException {
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

		@SuppressWarnings("restriction")
		protected boolean extendsClass(IType type, String clazzName) throws JavaModelException {
			IType[] types = JavaModelUtil.getAllSuperTypes(type, monitor);
			for (IType t : types) {
				if (t.getFullyQualifiedName().equals(clazzName))
					return true;
			}
			return false;
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

					Object obj = resource.getAdapter(IJavaElement.class);
					if (obj != null) {
						IJavaElement javaElement = (IJavaElement) obj;
						if (javaElement.getElementType() == IJavaElement.COMPILATION_UNIT) {
							ICompilationUnit cu = (ICompilationUnit) javaElement;
							IType type = cu.getTypes()[0];
							if (extendsClass(type, BASE_TURTLE)) {
								// find plural
								PluralInformation pi = getPluralInformation(type);
								iih.putTurtlePluralInformation(pi, instrumentingPackageName);
							} else if (extendsClass(type, BASE_PATCH)) {
								// find fields and type info
								List<Pair<String, String>> patchFieldTypes = getPatchFieldTypes(type);
								// put in iih
								iih.putPatchFieldTypes(patchFieldTypes, instrumentingPackageName);

							} else if (extendsClass(type, BASE_LINK)) {
								// find plural
								PluralInformation pi = getPluralInformation(type);

								// find if it's directed or undirected
								boolean isDir = isDir(type);
								if (isDir) {
									iih.putDirLinkPluralInformation(pi, instrumentingPackageName);
								} else {
									iih.putUndirLinkPluralInformation(pi, instrumentingPackageName);
								}
								
								// TODO: look at context.xml (and then display file if
								// necessary)
								// call context checker with type
								checkContextAndDisplayFiles(type);
								

							} else if (extendsClass(type, ABSTRACT_GLOBALS_AND_PANEL)) {
								// TODO: do for UGPFactory
								// if the javaModel is too difficult, might try ast
							}
						}
					}
				}

			}
			return true;
		}
		
		private void checkContextAndDisplayFiles(IType type){
			StringBuilder sb = new StringBuilder();
			sb.append(project.getName());
			sb.append(".rs");
			IFile contextFile = project.getFile(sb.toString());
			ContextAndDisplayUtils.checkToModifyContextFile(projectPath, projectName, className, basePackageName, contextFilePath)
		}

		private List<Pair<String, String>> getPatchFieldTypes(IType type) throws JavaModelException {
			// IJavaProject javaProject =
			List<Pair<String, String>> patchFieldTypes = new ArrayList<Pair<String, String>>();
			while (type != null && !type.getFullyQualifiedName().equals(BASE_PATCH)) {
				boolean isGroovySource = false;
				if (!type.isBinary()) {
					if (type.getCompilationUnit() instanceof GroovyCompilationUnit) {
						isGroovySource = true;
					}
				}
				// if (type.getCorrespondingResource())
				IField[] fields = type.getFields();
				IMethod[] methods = type.getMethods();
				List<String> methodNames = new ArrayList<String>();
				for (IMethod method : methods) {
					if (Flags.isPublic(method.getFlags())) {
						methodNames.add(method.getElementName());
					}
				}
				boolean previousField = false;
				for (IField field : fields) {
					boolean foundField = false;
					int flags = field.getFlags();
//					System.out.println("Field : " + field.getElementName() + " Flags: " + flags
//							+ " toString: " + Flags.toString(flags));
					if (!Flags.isPublic(flags)) {
						if (Flags.isPrivate(flags) && isGroovySource) {
							// package default is seen as private in groovy source
							// so need to check if it's truly private or not
							String source = field.getSource();
							if (source != null) {
								if (!source.trim().startsWith("private ")){
									// This is to check if this is part of a comma separated list of fields
									// since in that case the non-first elements won't show the private modifier
									if (source.trim().equals(field.getElementName())){
										// check to see if the previous field was found
										foundField = previousField;
									}
									else {
										foundField = true;
									}
								}
							}
						} else {
							// look for methods with is/get and set
							String fieldName = field.getElementName();
							String capitalizedFieldName = StringUtils.capitalize(fieldName);
							String isGetter = "is" + capitalizedFieldName;
							String getGetter = "get" + capitalizedFieldName;
							String setSetter = "set" + capitalizedFieldName;
							boolean foundGetter = false, foundSetter = false;
							if (methodNames.contains(isGetter) || methodNames.contains(getGetter)) {
								foundGetter = true;
							}
							if (methodNames.contains(setSetter)) {
								foundSetter = true;
							}
							if (foundGetter && foundSetter) {
								foundField = true;
							}
						}

					} else {
						foundField = true;
					}

					if (foundField) {
						try {
							patchFieldTypes.add(getFieldAndFieldTypePair(field, type));
						} catch (IllegalArgumentException iae) {
							// if IllegalArgumentException is caught, quietly ignore this
							// field
						}
					}
					previousField = foundField;
				}
//				System.out.println("Type: " + type.getElementName());
				type = getSuperType(type);
//				System.out.println("Supertype: " + type.getElementName());

			}
			return patchFieldTypes;
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

		private Pair<String, String> getFieldAndFieldTypePair(IField field, IType type)
				throws IllegalArgumentException, JavaModelException {
			String fieldTypeSignature = field.getTypeSignature();
			return new Pair<String, String>(field.getElementName(), getFullResolvedName(
					fieldTypeSignature, type));
		}

		private PluralInformation getPluralInformation(IType type) throws JavaModelException {
			ICompilationUnit cu = type.getCompilationUnit();
			if (cu == null)
				return new PluralInformation(type);
			if (cu instanceof GroovyCompilationUnit) {
				return getPluralInformationFromSource(type);
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

		private PluralInformation getPluralInformationFromType(IType type) throws JavaModelException {
			PluralInformation pi = new PluralInformation(type);
			for (IAnnotation annot : type.getAnnotations()) {
				// If the annotation is the fully qualified name
				if (annot.getElementName().equals(PLURAL_ANNOTATION)) {
					pi.hasPluralAnnotation = true;
					pi.plural = getValueFromAnnotation(annot);
					return pi;
				}
				String[][] resolve = type.resolveType(annot.getElementName());
				for (String[] res : resolve) {
					if (StringUtils.join(res, '.').equals(PLURAL_ANNOTATION)) {
						pi.hasPluralAnnotation = true;
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

		protected String getValueFromSimplePluralAnnotationLine(String line) {
			Matcher matcher = SIMPLE_PLURAL.matcher(line);
			if (matcher.find()) {
				String result = matcher.group(1);
				matcher = SIMPLE_COMMENTED_PLURAL.matcher(line);
				if (!matcher.find())
					return result;
			}
			return null;
		}

		protected String getValueFromFullPluralAnnotationLine(String line) {

			Matcher matcher = FULL_PLURAL.matcher(line);
			if (matcher.find()) {
				String result = matcher.group(1);
				matcher = FULL_COMMENTED_PLURAL.matcher(line);
				if (!matcher.find())
					return result;
			}
			return null;
		}

		private PluralInformation getPluralInformationFromSource(IType type) throws JavaModelException {
			PluralInformation pi = new PluralInformation(type);
			String source = type.getCompilationUnit().getSource();
			if (source != null) {
				Scanner scanner = new Scanner(source);
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					if (line.contains("@Plural")) {
						String[][] resolve = type.resolveType("Plural");
						for (String[] res : resolve) {
							if (StringUtils.join(res, '.').equals(PLURAL_ANNOTATION)) {
								pi.hasPluralAnnotation = true;
								String plural = getValueFromSimplePluralAnnotationLine(line);
								if (plural != null) {
									pi.plural = plural;
								}
								scanner.close();
								return pi;
							}
						}
					}
					if (line.contains("@repast.simphony.relogo.Plural")) {
						pi.hasPluralAnnotation = true;
						String plural = getValueFromFullPluralAnnotationLine(line);
						if (plural != null) {
							pi.plural = plural;
						}
						scanner.close();
						return pi;
					}
				}
				scanner.close();
			}
			return pi;
		}

	}

	private static class InstrumentationInformationDeltaVisitor implements IResourceDeltaVisitor {
		IProgressMonitor monitor;
		IProject project;

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
			IPath path = delta.getResource().getRawLocation();
			// System.out.println("statechart builder running: " + delta);
			if ((delta.getKind() == IResourceDelta.ADDED) && path != null
					&& path.getFileExtension() != null
					&& (path.getFileExtension().equals("groovy") || path.getFileExtension().equals("java"))) {
				boolean isInReLogoPackage = false;
				for (String segment : path.segments()) {
					if (segment.equalsIgnoreCase("relogo")) {
						isInReLogoPackage = true;
						break;
					}
				}
				if (isInReLogoPackage) {
					System.out.println("Delta Visitor: Found " + delta.getResource().getName());
				}
			}
			return true;
		}
	}

	// protected static String extractInstrumentingPackageName(String[] segments)
	// {
	// StringBuilder sb = new StringBuilder();
	// for (String string : segments) {
	// if (string.equals("relogo")) {
	// break;
	// } else {
	// sb.append(string);
	// sb.append(".");
	// }
	// }
	// return sb.toString();
	// }

	/*
	 * private static class FullBuildVisitor implements IResourceVisitor {
	 * 
	 * IProgressMonitor monitor; IProject project; CodeGenerator generator;
	 * 
	 * public FullBuildVisitor(IProject project, IProgressMonitor monitor) {
	 * this.monitor = monitor; this.project = project; generator = new
	 * CodeGenerator(); }
	 * 
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core
	 * .resources.IResource)
	 * 
	 * @Override public boolean visit(IResource resource) throws CoreException {
	 * IPath path = resource.getRawLocation(); if (path != null &&
	 * path.getFileExtension() != null &&
	 * path.getFileExtension().equals(STATECHART_EXTENSION)) { IPath srcPath =
	 * generator.run(project, path, monitor); if (srcPath != null) { new
	 * SVGExporter().run(path, srcPath, monitor);
	 * project.getFolder(srcPath.lastSegment
	 * ()).refreshLocal(IResource.DEPTH_INFINITE, monitor); } } return true; }
	 * 
	 * }
	 */

	/*
	 * private static class Visitor implements IResourceDeltaVisitor {
	 * 
	 * IProgressMonitor monitor; IProject project;
	 * 
	 * public Visitor(IProject project, IProgressMonitor monitor) { this.monitor =
	 * monitor; this.project = project; }
	 * 
	 * @Override public boolean visit(IResourceDelta delta) throws CoreException {
	 * IPath path = delta.getResource().getRawLocation(); //
	 * System.out.println("statechart builder running: " + delta); if
	 * ((delta.getKind() == IResourceDelta.CHANGED || delta.getKind() ==
	 * IResourceDelta.ADDED) && path != null && path.getFileExtension() != null &&
	 * path.getFileExtension().equals(STATECHART_EXTENSION)) { // create svg file
	 * here IPath srcPath = new CodeGenerator().run(project, path, monitor); if
	 * (srcPath != null) { new SVGExporter().run(path, srcPath, monitor);
	 * project.getFolder
	 * (srcPath.lastSegment()).refreshLocal(IResource.DEPTH_INFINITE, monitor); }
	 * } return true; } }
	 */
}
