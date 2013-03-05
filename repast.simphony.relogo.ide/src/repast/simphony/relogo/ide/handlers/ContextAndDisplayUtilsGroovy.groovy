package repast.simphony.relogo.ide.handlers

import groovy.util.slurpersupport.GPathResult
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

import java.util.regex.Pattern

import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IFolder
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IResourceVisitor
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.IPath
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.jdt.core.IType

class ContextAndDisplayUtilsGroovy {

	static private final String DEFAULT_RELOGO_DISPLAY_NAME = "ReLogo Default Display"
	static private final String DEFAULT_OBSERVER_CONTEXT_NAME = "default_observer_context"
	private static final Pattern DISPLAY_PATTERN = Pattern.compile("repast\\.simphony\\.action\\.display_.+\\.xml");

	private static String plainPrettyPrint(xml) {
		XmlUtil.serialize(new StreamingMarkupBuilder().bind { mkp.yield xml })
	}

	private static String prettyPrint(xml) {
		XmlUtil.serialize(new StreamingMarkupBuilder().bind {
			mkp.declareNamespace("":"")
			mkp.yield xml
		})
	}

	protected static void checkToModifyContextFile(IProject project, IType type, IProgressMonitor monitor){
		StringBuilder sb = new StringBuilder();
		sb.append(project.getName());
		sb.append(".rs");
		sb.append(File.separator);
		sb.append("context.xml");
		IFile contextFile = project.getFile(sb.toString());
		String className = type.getElementName();
		try{
			String contents = ContextAndDisplayUtils.getStringContentsFromIFile(contextFile)
			if (contents != null){
				def root = new XmlSlurper().parseText(contents)
				GPathResult listOfDefaultReLogoContexts = root.context.findAll({it.@id.equals(DEFAULT_OBSERVER_CONTEXT_NAME)})
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

						String fileContents = prettyPrint(root)
						InputStream source = new ByteArrayInputStream(fileContents.getBytes());
						contextFile.setContents(source, true, true, monitor)
						contextFile.refreshLocal(0, monitor);
						source.close();

						//Add to the display's projection info
					 modifyDisplayFile(project, type, monitor)
					}
				}
			}
		}
		catch (CoreException ce){
		}
	}

/*	protected static void checkToModifyContextFile(String projectPath, String projectName, String className, String basePackageName, String contextFilePath){
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
*/
	private static class DefaultDisplayFinder implements IResourceVisitor {

		protected boolean foundDefaultDisplay = false;
		protected IResource defaultDisplayResource;

		@Override
		public boolean visit(IResource resource) throws CoreException {
			if (foundDefaultDisplay) return false;
			IPath path = resource.getRawLocation();
			if (path != null && path.getFileExtension() != null
			&& path.getFileExtension().equals("xml")) {
				String resourceName = resource.getName();
				if (resourceName != null){
					if (DISPLAY_PATTERN.matcher(resourceName).find()){
						defaultDisplayResource = resource;
						foundDefaultDisplay = true;
					}
				}
			}
			return true;
		}

	}

	protected static DefDisplayReturner findDefaultReLogoDisplayFile(IResource rsFolder){
		if (rsFolder == null){
			return null;
		}
		DefaultDisplayFinder ddf = new DefaultDisplayFinder();
		rsFolder.accept(ddf);
		if (ddf.foundDefaultDisplay){
			IResource resource = ddf.defaultDisplayResource;
			if (resource instanceof IFile){
				try{
					String contents = ContextAndDisplayUtils.getStringContentsFromIFile((IFile)resource)
					if (contents != null){
						GPathResult displayRoot = new XmlSlurper().parseText(contents)
						if (displayRoot.name.equals(DEFAULT_RELOGO_DISPLAY_NAME)){
							return new DefDisplayReturner(displayFile:resource,root:displayRoot)
						}
					}
				}
				catch (Exception ignore){
				}
			}
		}
		return null
	}

	/*protected List<File> getCandidateDisplayFiles(String directoryString){
		List candidateDisplayFiles = []
		new File(directoryString).eachFileMatch(~/repast.simphony.action.display_.+\.xml/){ candidateDisplayFiles << it }
		return candidateDisplayFiles
	}*/

	static class DefDisplayReturner {
		IResource displayFile
		GPathResult root
	}

	//
	protected static void modifyDisplayFile(IProject project, IType iType, IProgressMonitor monitor){

		StringBuilder sb = new StringBuilder();
		sb.append(project.getName());
		sb.append(".rs");
		IFolder rsFolder = project.getFolder(sb.toString());
		DefDisplayReturner result = findDefaultReLogoDisplayFile(rsFolder)
		if (result != null && result.displayFile.exists()){
			String className = iType.getElementName();
			GPathResult displayRoot = result.root
			// Check to see if network is already there (to avoid accidental duplication)

			GPathResult classNameInDisplay = displayRoot.netStyles.entry.string.findAll{
				it.text().equals(className)
			}
			if (classNameInDisplay.isEmpty()){
				GPathResult styleClassNameElement = displayRoot.netStyles.entry.string.find{
					it.text().trim().endsWith(".style.LinkStyle")
				}
				if (styleClassNameElement != null){
					String styleClassName = styleClassNameElement.text();
					if (styleClassName != null && !styleClassName.isEmpty()){
						displayRoot.netStyles.leftShift{
							entry {
								string(className)
								string(styleClassName)
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

						String fileContents = plainPrettyPrint(displayRoot)
						InputStream source = new ByteArrayInputStream(fileContents.getBytes());
						if (result.displayFile instanceof IFile){
							IFile dFile = (IFile)result.displayFile;
							dFile.setContents(source, true, true, monitor)
							dFile.refreshLocal(0, monitor);
							source.close();
						}
					}
				}
			}
		}
	}

	/*protected void modifyDisplayFile(String projectPath, String projectName, String className, String basePackageName){


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
	}*/

}
