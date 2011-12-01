package repast.simphony.visualization.gis3D;

import java.awt.Color;

/**
 * 
 * @author tatara
 *
 */
public class MaterialFactory {

	public static Material setMaterialAppearance(Material material, Color color){
	
		if (material == null)
			material = new Material(color);
		
		else 
			material.setDiffuseColor(color);
		
		return material;
	}
}