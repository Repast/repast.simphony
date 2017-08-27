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
 * @deprecated As of version 2.5.  Use the {@link BasicWWTexture} instead to avoid
 * memory leaks.
 */
@Deprecated
public class BufferedImageTexture extends BasicWWTexture {

	public BufferedImageTexture(BufferedImage imageSource) {
		super(imageSource);
	}
}