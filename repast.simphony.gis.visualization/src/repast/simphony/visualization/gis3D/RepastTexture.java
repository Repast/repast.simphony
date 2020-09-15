package repast.simphony.visualization.gis3D;

import com.jogamp.opengl.GL;

import com.jogamp.opengl.util.texture.Texture;

import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.LazilyLoadedTexture;

/**
 * RepastTexture overrides the default WorldWind Texture so that it can provide
 *   options for smoothing (anti-aliasing) in cases such as raster layers.
 * 
 * @author Eric Tatara
 *
 */
public class RepastTexture extends LazilyLoadedTexture {

	// Draw smooth will use linear interpolation smoothing on texture pixels.
	//    False will draw the raw image.
	private boolean drawSmooth = false;
	
	public RepastTexture(Object imageSource, boolean useMipMaps) {
		super(imageSource, useMipMaps);
	}
		
	@Override
	protected void setTextureParameters(DrawContext dc, Texture texture){
		
		// Override the WWJ texture parameter so we can control filtering etc.
		//  See https://open.gl/textures for info.

		// Enable the appropriate mip-mapping texture filters if the caller has specified that mip-mapping should be
		// enabled, and the texture itself supports mip-mapping.
		boolean useMipMapFilter = this.useMipMaps && (this.getTextureData().getMipmapData() != null
				|| texture.isUsingAutoMipmapGeneration());

		GL gl = dc.getGL();
		
		int mipmapFilterType = GL.GL_NEAREST_MIPMAP_NEAREST;
		int filterType = GL.GL_NEAREST;
		
		if (drawSmooth) { 
			filterType = GL.GL_LINEAR;
			mipmapFilterType = GL.GL_LINEAR_MIPMAP_LINEAR;
		}
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, useMipMapFilter ? mipmapFilterType : filterType);
		
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, filterType);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);

		if (this.isUseAnisotropy() && useMipMapFilter)
		{
			double maxAnisotropy = dc.getGLRuntimeCapabilities().getMaxTextureAnisotropy();
			if (dc.getGLRuntimeCapabilities().isUseAnisotropicTextureFilter() && maxAnisotropy >= 2.0)
			{
				gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAX_ANISOTROPY_EXT, (float) maxAnisotropy);
			}
		}
	}

	/**
	 * Toggles smooth (anti-aliased) rendering of texture images.
	 * 
	 * @param drawSmooth
	 */
	public void setDrawSmooth(boolean drawSmooth) {
		this.drawSmooth = drawSmooth;
	}	
}
