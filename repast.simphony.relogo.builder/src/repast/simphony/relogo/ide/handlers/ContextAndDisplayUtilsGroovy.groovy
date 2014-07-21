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

	protected static String checkToModifyContextFile(String contents, String className){
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
				return fileContents;
			}
		}
		return null;
	}

	static public GPathResult checkIfDefaultReLogoDisplay(String contents){
		GPathResult displayRoot = new XmlSlurper().parseText(contents)
		if (displayRoot.name.equals(DEFAULT_RELOGO_DISPLAY_NAME)){
			return displayRoot
		}
		return null
	}


	protected static String getDisplayFileContents(GPathResult displayRoot, String className){
		String fileContents = null;

		// Check to see if network is already there (to avoid accidental duplication)
		//
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

					fileContents = plainPrettyPrint(displayRoot)
				}
			}
		}
		return fileContents
	}


}
