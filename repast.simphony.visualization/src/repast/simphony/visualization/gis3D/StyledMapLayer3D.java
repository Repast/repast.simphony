package repast.simphony.visualization.gis3D;

import java.util.Map;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.render.Renderable;
import repast.simphony.visualization.gis3D.style.DefaultStyleGIS3D;
import repast.simphony.visualization.gis3D.style.StyleGIS3D;

/**
 * 
 * @author Eric Tatara
 *
 */
public class StyledMapLayer3D extends AbstractDisplayLayerGIS3D {

	StyleGIS3D style;

	public StyledMapLayer3D(String name, StyleGIS3D style, WorldWindowGLCanvas wwglCanvas) {

		if (style == null) 
			this.style = new DefaultStyleGIS3D();
		else
			this.style = style;
		
		init(name, wwglCanvas);
	}
	

	protected void applyUpdatesToNode(RenderableShape node) {
		Object o = shapeToObjectMap.get(node);
		
		node.setMaterial(style.getMaterial(o, node.getMaterial()));
	}

	protected RenderableShape createVisualItem(Object o) {
		RenderableShape renderable = style.getShape(o);

		renderable.setGeometry(geography.getGeometry(o));
		renderable.setIsScaled(style.isScaled(o));
		renderable.setScale(style.getScale(o));

		visualItemMap.put(o, renderable);

		return renderable;
	}
	
	public void setStyle(StyleGIS3D style) {
		this.style = style;
	}


	public Map<Renderable, Object> getShapeToObjectMap() {
		return this.shapeToObjectMap;
	}
}