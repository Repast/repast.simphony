package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.render.BasicWWTexture;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.util.Logging;

import java.awt.image.BufferedImage;

import javax.media.opengl.GL;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

/**
 * BufferedImageTexture is a non-cached BasicWWTexture implementation that is 
 *   useful for styling PlaceMarks with often changing appearance, eg for 
 *   agents that may update their appearance every tick.
 *     
 *   The non-cached version of BasicWWTexture just returns a stored jogl Texture
 *   each time rather than storing it in the GPU texture cache.  For cases where
 *   the style updates the texture anyway, this does not impact rendering 
 *   performance but has a huge benefit in reducing memory storage of textures. 
 *   
 *   This implementation only handles BufferedImage as the image source.
 *   
 * @author Eric Tatara
 *
 */
public class BufferedImageTexture extends BasicWWTexture {

	/*
	 * The jogl texture is stored here rather than fetched from a texture cache.
	 */
	protected Texture texture;
	
	/**
	 * 
	 * @param imageSource must be a BufferedImage.
	 */
	public BufferedImageTexture(BufferedImage imageSource) {
		super(imageSource);
	}
	
	@Override
	/**
	 * This overrides BasicWWTexture so that the texture is just returned and not
	 *   fetched from a texture cache.
	 */
	 protected Texture getTextureFromCache(DrawContext dc){
      return texture;
   }

	@Override
	/**
	 * This overrides BasicWWTexture so that a jogl texture is created from a
	 *   BufferedImage source and the texture is not put into a cache.
	 */
	protected Texture initializeTexture(DrawContext dc, Object imageSource){
		if (dc == null){
			String message = Logging.getMessage("nullValue.DrawContextIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}

		if (this.textureInitializationFailed)
			return null;

		boolean haveMipMapData = false;
		GL gl = dc.getGL();

		if (imageSource instanceof BufferedImage){
			try{
				TextureData td = AWTTextureIO.newTextureData(gl.getGLProfile(), (BufferedImage) imageSource,
						false);
				texture = TextureIO.newTexture(td);
//				haveMipMapData = td.getMipmapData() != null;
			}
			catch (Exception e)
			{
				String msg = Logging.getMessage("generic.IOExceptionDuringTextureInitialization");
				Logging.logger().log(java.util.logging.Level.SEVERE, msg, e);
				this.textureInitializationFailed = true;
				return null;
			}
		}
		else
		{
			Logging.logger().log(java.util.logging.Level.SEVERE, "generic.UnrecognizedImageSourceType",
					imageSource.getClass().getName());
			this.textureInitializationFailed = true;
			return null;
		}

		if (texture == null) // In case JOGL TextureIO returned null
		{
			Logging.logger().log(java.util.logging.Level.SEVERE, "generic.TextureUnreadable",
					imageSource instanceof String ? imageSource : imageSource.getClass().getName());
			this.textureInitializationFailed = true;
			return null;
		}

		texture.bind(gl);

		// Enable the appropriate mip-mapping texture filters if the caller has specified that mip-mapping should be
		// enabled, and the texture itself supports mip-mapping.
		boolean useMipMapFilter = false;
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
				useMipMapFilter ? GL.GL_LINEAR_MIPMAP_LINEAR : GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);

		if (this.isUseAnisotropy() && useMipMapFilter){
			double maxAnisotropy = dc.getGLRuntimeCapabilities().getMaxTextureAnisotropy();
			if (dc.getGLRuntimeCapabilities().isUseAnisotropicTextureFilter() && maxAnisotropy >= 2.0)
			{
				gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAX_ANISOTROPY_EXT, (float) maxAnisotropy);
			}
		}

		this.width = texture.getWidth();
		this.height = texture.getHeight();
		this.texCoords = texture.getImageTexCoords();

		return texture;
	}
}