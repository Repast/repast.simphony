package repast.simphony.ws.gis;

import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.gis.visualization.engine.GISDisplayData;
import repast.simphony.gis.visualization.engine.GISDisplayDescriptor;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.NullLayout;
import repast.simphony.visualization.engine.DisplayCreationException;
import repast.simphony.ws.ServerDisplayCreator;

/**
 * Creates 3D GIS displays.  Uses a different type of style registrar than the
 *   other DisplayCreator implementations that handles all of the display layer
 *   types together.
 * 
 * @author Eric Tatara
 */
public class ServerDisplayCreatorGIS implements ServerDisplayCreator {

	protected Context<?> context;
	protected GISDisplayDescriptor descriptor; 
	protected String outgoingAddr;
	
	public ServerDisplayCreatorGIS(String outgoingAddr, Context<?> context, GISDisplayDescriptor descriptor) {
		this.context = context;
		this.descriptor = descriptor;
		this.outgoingAddr = outgoingAddr;
	}

	protected GISDisplayData<?> createDisplayData() {

		// DisplayData contains only the info needed in the display constructor.  
		// All other info is set below from the descriptor in createDisplay().
		GISDisplayData<?> data = new GISDisplayData(context);

		// Add data about projections
		for (ProjectionData pData : descriptor.getProjections()) {
			data.addProjection(pData.getId());
		}

		// Set the initial globe type (flat or sphere)
		data.setViewType(descriptor.getViewType());

		// Add static coverage filename and style
		for (String fileName : descriptor.getStaticCoverageMap().keySet()) {
			data.addStaticCoverage(fileName, descriptor.getStaticCoverageMap().get(fileName));
		}

		// The optional background WWJ globe layers
		Map<String,Boolean> globeLayers = descriptor.getGlobeLayersMap();
		for (String layerName : globeLayers.keySet()) {
			data.addGlobeLayer(layerName, globeLayers.get(layerName));
		}

		data.setLayerOrders(descriptor.getLayerOrders());

		return data;
	}

	public DisplayServerGIS createDisplay(int display_id) throws DisplayCreationException {

		try {
			GISDisplayData<?> data = createDisplayData();
			Layout<?, ?> layout = new NullLayout();

			final DisplayServerGIS display = new DisplayServerGIS(outgoingAddr, data, descriptor, layout, display_id);
	
			DisplayServerGISStyleRegistrar registrar = new DisplayServerGISStyleRegistrar(display);    
			try {
				registrar.registerAllStyles((GISDisplayDescriptor)descriptor, context);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			} 

			// TODO implement relevant parts for WS
			// TODO scheduling is handled by the descriptor schedule paramaters, so not
			//      sure what effect the layout interval has?
			
//			display.setRenderQuality(descriptor.getRenderQuality());
//			display.setBackgroundColor(descriptor.getBackgroundColor());    
//			display.setTrackAgents(descriptor.getTrackAgents());
//			display.setLayoutFrequency(descriptor.getLayoutFrqeuency(), descriptor.getLayoutInterval());

			return display;
		} 
		catch (Exception ex) {
			throw new DisplayCreationException(ex);
		}
	}
}