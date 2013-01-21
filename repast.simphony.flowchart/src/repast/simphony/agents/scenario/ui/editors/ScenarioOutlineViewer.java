package repast.simphony.agents.scenario.ui.editors;

import org.eclipse.wst.sse.core.internal.ltk.modelhandler.IDocumentTypeHandler;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.ui.internal.registry.AdapterFactoryProviderForXML;

/**
 * Outline viewer for scenario files.
 * 
 * @author Eric Tatara
 *
 * TODO currently this is just the default editor for XML files, but needs
 *      to be customized so that repast xml files are handled diffrerently,
 *      such as using proper xsd, change icons, etc.
 */
public class ScenarioOutlineViewer extends AdapterFactoryProviderForXML {

	@Override
	public boolean isFor(IDocumentTypeHandler contentTypeDescription) {
	// TODO Auto-generated method stub
		return super.isFor(contentTypeDescription);
	}
	
	@Override
	public void addAdapterFactories(IStructuredModel structuredModel) {
		// TODO Auto-generated method stub
		super.addAdapterFactories(structuredModel);
	}
	
	@Override
	protected void addContentBasedFactories(IStructuredModel structuredModel) {
		// TODO Auto-generated method stub
		super.addContentBasedFactories(structuredModel);
	}
}
