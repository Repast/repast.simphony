package repast.simphony.gis.visualization.engine;

import org.apache.poi.ss.formula.functions.T;

import repast.simphony.context.Context;
import repast.simphony.visualization.DefaultDisplayData;

/**
 * Provides GIS Display specific data to the DefaultDisplayData.
 * 
 * @author Eric Tatara
 *
 */
public class GISDisplayData<T> extends DefaultDisplayData<T> {

	protected GISDisplayDescriptor.VIEW_TYPE viewType;
	protected boolean trackAgents;
	
	public GISDisplayData(Context<T> context) {
		super(context);
	}

	public GISDisplayDescriptor.VIEW_TYPE getViewType() {
		return viewType;
	}

	public void setViewType(GISDisplayDescriptor.VIEW_TYPE viewType) {
		this.viewType = viewType;
	}

	public boolean getTrackAgents() {
		return trackAgents;
	}

	public void setTrackAgents(boolean trackAgents) {
		this.trackAgents = trackAgents;
	}
}