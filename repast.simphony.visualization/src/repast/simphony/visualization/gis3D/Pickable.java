package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.render.DrawContext;

/**
 * Pickable objects must implement this interface.
 * 
 * @author Eric Tatara
 *
 */
public interface Pickable {

	public void pickRender(DrawContext dc);
}
