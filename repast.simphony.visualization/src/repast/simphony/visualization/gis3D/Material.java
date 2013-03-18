package repast.simphony.visualization.gis3D;

import java.awt.Color;

import javax.media.opengl.GL2;

/**
 * Material is used to color RenderableShapes based on the supplied diffuse 
 * color and the default lighting info.
 * 
 * @author Eric Tatara
 *
 */
public class Material{
	
	private Color ambient;
	private Color diffuse;
	private Color specular;
	private Color emission;
	private float shininess;
	
	/**
	 * 
	 * @param ambient the ambient color.
	 * @param diffuse the diffuse color.
	 * @param specular the specular color.
	 * @param emission the emission color. 
	 * @param shininess the shininess factor.
	 */
	public Material(Color ambient, Color diffuse, Color specular, Color emission, 
			float shininess){     
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.emission = emission;
		this.shininess = shininess;
	}

	public Material(){
		setDefaultColors();
	}
	
	public Material(Color color){
		this.diffuse = color;
		
		setDefaultColors();
	}

	private void setDefaultColors(){
		ambient = new Color(0.2f, 0.2f, 0.01f, 0.0f);
		specular = new Color(0.75f, 0.75f, 0.55f, 0.0f);
		emission = new Color(0.0f, 0.0f, 0.0f, 0.0f);
		shininess = 20f;
	}
	
	/**
	 * Apply the material to the GL.
	 * 
	 * @param gl the GL.
	 * @param face the face.
	 */
	public void apply(GL2 gl, int face)	{
		float[] rgba = new float[4];

		gl.glMaterialfv(face, GL2.GL_AMBIENT, ambient.getRGBComponents(rgba), 0);
		gl.glMaterialfv(face, GL2.GL_DIFFUSE, diffuse.getRGBComponents(rgba), 0);
		gl.glMaterialfv(face, GL2.GL_SPECULAR, specular.getRGBComponents(rgba), 0);
		gl.glMaterialfv(face, GL2.GL_EMISSION, emission.getRGBComponents(rgba), 0);
		
		gl.glMaterialf (face, GL2.GL_SHININESS,shininess);
	}

	public Color getDiffuseColor() {
		return diffuse;
	}

	public void setDiffuseColor(Color diffuse) {
		this.diffuse = diffuse;
	}
}