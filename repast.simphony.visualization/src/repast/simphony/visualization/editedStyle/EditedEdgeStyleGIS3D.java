package repast.simphony.visualization.editedStyle;

import repast.simphony.visualization.gis3D.Material;
import repast.simphony.visualization.gis3D.style.EdgeStyleGIS3D;

/**
 * 
 * @author Eric Tatara
 *
 * @param <T>
 */
public class EditedEdgeStyleGIS3D<T> implements EdgeStyleGIS3D<T> {
  
	public EditedEdgeStyleGIS3D (String userStyleFile) {
		
	}

	public float edgeRadius(T obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Material getMaterial(T obj, Material material) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isScaled(T object) {
		// TODO Auto-generated method stub
		return false;
	}

}
