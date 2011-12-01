package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;

import java.awt.Color;

import javax.media.opengl.GL;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Superclass for all model renderable shapes in the 3D GIS display.
 * 
 * @author Eric Tatara
 *
 */
public class RenderableShape implements Renderable, Pickable{
	
	public static final double HEIGHT_OFFSET = 10;
	public static final double ZOOM_SCALE_FACTOR = 5000;
	
	protected boolean initialized = false;
	protected int glListId;
	protected Material material;
  protected Geometry geometry;
  protected float[] scale;
	protected boolean isScaled = false;
	
	/**
	 * The rendering method for picking omits color/material/lighting information.
	 * 
	 * @param dc the DrawContext.
	 */
	public void pickRender(DrawContext dc){
		
		dc.getGL().glMatrixMode(GL.GL_MODELVIEW);
		dc.getGL().glPushMatrix();
						
		this.doRender(dc);
		
		dc.getGL().glMatrixMode(GL.GL_MODELVIEW);
		dc.getGL().glPopMatrix();
	}
	
	/**
	 * Initialize is used by subclasses to create the GenLists for some shapes. 
	 * 
	 * @param dc the DrawContext.
	 */
	protected void initialize(DrawContext dc){}
	
	/**
	 * Implemented by subclasses to do the actual rendering.
	 * 
	 * @param dc the DrawContext.
	 */
	protected void doRender(DrawContext dc){}
	
	/**
	 * Apply the material and call doRender(dc) on the subclass to do the actual
	 * drawing.
	 * 
	 * @param dc the DrawContext.
	 */
	public void render(DrawContext dc) {
		if (!initialized)
			initialize(dc);

		if (material != null)
		  material.apply(dc.getGL(), GL.GL_FRONT);
		
		// TODO hack
		if (this instanceof MultiLine){
			((MultiLine)this).setColor(material.getDiffuseColor());
		}
		else
			RenderUtils.begin(dc);
		
		// enable blend mode to allow for transparency if set.
		dc.getGL().glEnable(GL.GL_BLEND);
		dc.getGL().glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			
		// render the shape as defined by subclass.
		this.doRender(dc);
		
		// reset the blend mode.
		dc.getGL().glDisable(GL.GL_BLEND);
		
		// TODO hack
		if (!(this instanceof MultiLine)){
			RenderUtils.end(dc);
		}
	}	

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	public void setScale(float[] scale) {
		this.scale = scale;
	}
	public void setIsScaled(boolean isScaled) {
		this.isScaled = isScaled;
	}
}