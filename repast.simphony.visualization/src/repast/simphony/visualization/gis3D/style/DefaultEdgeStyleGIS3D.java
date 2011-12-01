package repast.simphony.visualization.gis3D.style;

import java.awt.Color;

import repast.simphony.visualization.gis3D.Material;
import repast.simphony.visualization.gis3D.MaterialFactory;

/**
 * 
 * @author Eric Tatara
 *
 * @param <T>
 */
public class DefaultEdgeStyleGIS3D<T> implements EdgeStyleGIS3D<T>  {
	protected float radius = 5.0f;
	 
	public Material getMaterial(T obj, Material material) {
		return MaterialFactory.setMaterialAppearance(material, Color.GREEN);
	}

	public float edgeRadius(T obj){
		return radius;
	}

	public boolean isScaled(T obj) {
		return false;
	}
}