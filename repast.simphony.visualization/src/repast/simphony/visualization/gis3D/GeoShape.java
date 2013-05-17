package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.render.Renderable;

import java.awt.Paint;

public class GeoShape {

	public enum FeatureType {POINT, LINE, POLYGON}
	
	private Renderable renderable;
//	private FeatureType type;

	public Renderable getRenderable() {
		return renderable;
	}
	public void setRenderable(Renderable renderable) {
		this.renderable = renderable;
	}

//	public FeatureType getType() {
//		return type;
//	}
//	public void setType(FeatureType type) {
//		this.type = type;
//	}
}