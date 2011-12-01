package repast.simphony.visualization.gis3D.style;

import repast.simphony.visualization.gis3D.Material;

public interface EdgeStyleGIS3D<T> {

	public Material getMaterial(T obj, Material material);

	public boolean isScaled(T object);

	public float edgeRadius(T obj); 
}
