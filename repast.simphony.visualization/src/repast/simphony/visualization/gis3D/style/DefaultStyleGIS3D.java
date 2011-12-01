package repast.simphony.visualization.gis3D.style;

import java.awt.Color;

import repast.simphony.visualization.gis3D.GIS3DShapeFactory;
import repast.simphony.visualization.gis3D.Material;
import repast.simphony.visualization.gis3D.MaterialFactory;
import repast.simphony.visualization.gis3D.RenderableShape;

/**
 * 
 * @author tatara
 *
 * @param <T>
 */
public class DefaultStyleGIS3D<T> implements StyleGIS3D<T> {

	public RenderableShape getShape(T obj) {
	 
		return GIS3DShapeFactory.createSphere(10);
	}

	public Material getMaterial(T obj, Material material) {
	
    return MaterialFactory.setMaterialAppearance(material, Color.BLUE);
	}

	public float[] getScale(T obj) {
		return null;
	}

	public boolean isScaled(T object) {
		return true;
	}
}
