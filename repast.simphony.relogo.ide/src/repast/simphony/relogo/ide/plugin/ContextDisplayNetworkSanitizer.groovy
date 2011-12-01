package repast.simphony.relogo.ide.plugin

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IFile 
import org.eclipse.core.resources.IResource 
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor 
import org.eclipse.core.runtime.IStatus 
import org.eclipse.core.runtime.Status 
import org.eclipse.core.runtime.jobs.Job 

import groovy.util.XmlSlurper;
import groovy.util.slurpersupport.GPathResult;
import groovy.xml.StreamingMarkupBuilder 
import groovy.xml.XmlUtil 


class ContextDisplayNetworkSanitizer {
	
	private static String prettyPrint(xml) {
		XmlUtil.serialize(new StreamingMarkupBuilder().bind {
			mkp.declareNamespace("":"")
			mkp.yield xml
		})
//		new StreamingMarkupBuilder().bind { mkp.yield xml }
	}
	
	private static String plainPrettyPrint(xml) {
		XmlUtil.serialize(new StreamingMarkupBuilder().bind {
			mkp.yield xml
		})
//		new StreamingMarkupBuilder().bind { mkp.yield xml }
	}
	
	/*
	 * Following extracted from ReLogoGlobalASTTransformation
	 * Candidate for refactoring into utility class.
	 */
	static class DefDisplayReturner {
		File displayFile
		GPathResult root
	}

	static private final String DEFAULT_RELOGO_DISPLAY_NAME = "ReLogo Default Display"
	
	protected static List<File> getCandidateDisplayFiles(String directoryString){
		List candidateDisplayFiles = []
		new File(directoryString).eachFileMatch(~/repast.simphony.action.display_.+\.xml/){ candidateDisplayFiles << it }
		return candidateDisplayFiles
	}
	
	protected static DefDisplayReturner findDefaultReLogoDisplayFile(String projectPath, String projectName){
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
	
	public static void sanitize(IFile contextIFile, IResource resource){
		// check context.xml for projection with same name as removed resource
			// get name of removed resource
		String fullResourceName = resource.getName();
		int idx = fullResourceName.lastIndexOf('.');
		if (idx > 0){
			String resourceName = fullResourceName.substring(0, idx);
			File contextFile = new File(contextIFile.getLocation().toOSString());
//			contextIFile.
//			InputStream is = contextIFile.getContents()
			def root = new XmlSlurper().parse(contextFile)
			
			// see if any of the networks in the default context are named resourceName
			GPathResult defaultRLSubContext = root.context.find({it.@id.equals("default_observer_context")})
			if (!defaultRLSubContext.isEmpty()){
				GPathResult targetProjection = defaultRLSubContext.projection.find({it.@id.equals(resourceName) && it.@type.equals("network")})
				if (!targetProjection.isEmpty()){
					targetProjection.replaceNode({})
					// generate new context file
					contextFile.write(prettyPrint(root))
					// get default display and remove nodes
					
					DefDisplayReturner result = findDefaultReLogoDisplayFile(resource.getProject().getLocation().toString(), resource.getProject().getName())
					if (result != null){
						File defaultDisplayFile = result.displayFile
						if (defaultDisplayFile.exists()){
							def displayRoot = result.root
							displayRoot.netStyles.entry.find({it.string[0].text().equals(resourceName)}).replaceNode({})
							displayRoot.editedNetStyles.entry.find({it.string[0].text().equals(resourceName)}).replaceNode({})
							// scan through the projections and number the projection Descriptors accordingly
							int projectionCounter = 2
							GPathResult projectionDataEntries = displayRoot.projections.'repast.simphony.scenario.data.ProjectionData'.each{pde ->
								String projectionId = pde.id.text()
								if (!(projectionId.equals("Space2d") 
										|| (projectionId.equals(resourceName)))){
									displayRoot.projectionDescriptors.entry.
									find({it.string.text().equals(projectionId)}).
									'repast.simphony.visualization.engine.DefaultProjectionDescriptor'.proj.@reference = "../../../../projections/repast.simphony.scenario.data.ProjectionData[$projectionCounter]"
									projectionCounter++
								}
							}
							displayRoot.projections.'repast.simphony.scenario.data.ProjectionData'.find({it.id.text().equals(resourceName)}).replaceNode({})
							displayRoot.projectionDescriptors.entry.find({it.string.text().equals(resourceName)}).replaceNode({})
							defaultDisplayFile.write(plainPrettyPrint(displayRoot))
						}
					}
					// refresh workspace
					Job job = new Job("Refresh project") {
					     protected IStatus run(IProgressMonitor monitor) {
					    	 while (ResourcesPlugin.getWorkspace().isTreeLocked()) {
								  try {Thread.sleep(100);}catch (InterruptedException e) 
								{e.printStackTrace();}
								}
					    	 contextIFile.getParent().refreshLocal(IResource.DEPTH_ONE, null);
					           return Status.OK_STATUS;
					        }
					     };
					  job.setPriority(Job.SHORT);
					  job.schedule(); // start as soon as possible
				}
			}
		}
	}
}
