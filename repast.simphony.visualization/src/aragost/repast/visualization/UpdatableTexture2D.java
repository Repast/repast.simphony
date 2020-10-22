/*
 * Copyright (C) 2015, 2016, aragost Trifork AG
 * 
 * Contributed to the Repast community by aragost Trifork AG, developed for the 
 * DEPONS project for Aarhus University (www.depons.au.dk).
 * 
 *  Redistribution and use in source and binary forms, with
 *   or without modification, are permitted provided that the following
 *   conditions are met:
 *
 *     Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *
 *     Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *     Neither the name of the Argonne National Laboratory, aragost Trifork AG, nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *   ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *   PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 *   CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *   EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 *   EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package aragost.repast.visualization;

import java.awt.image.BufferedImage;

import com.jogamp.opengl.GL2;
import org.jogamp.vecmath.Point3f;

import saf.v3d.picking.BoundingSphere;
import saf.v3d.render.RenderState;
import saf.v3d.render.Texture2D;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

/**
 * An updated Texture2D that can be used to efficiently update and redraw some
 * specified dirty region.<p>
 * 
 * This class is intended to be used in conjunction with a user created "background agent"
 * and the {@link aragost.repast.visualization.DirtyRegion DirtyRegion} class. This class
 * is used in a custom style when create the VSpatial.<p>
 * 
 * For example, a TrackingDisplayAgent is a pseudo-agent that is used to paint a background
 * that displays the tracks (the previous locations) of other agents. It creates a
 * BufferedImage that is displayed by the UpdatableTexture2D and contains a
 * {@link aragost.repast.visualization.DirtyRegion DirtyRegion} that tracks what parts of 
 * this image are dirty.<p>
 * 
 * <pre>
 * {@code
public class TrackingDisplayAgent {

    private BufferedImage img;
    private DirtyRegion dr;

    public TrackingDisplayAgent() {
        img = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_ARGB);
        dr = new DirtyRegion(2000, 2000, 0, 0);
    }

    public void initialize() {}

    public BufferedImage getImage() {
        return img;
    }

    public void mark(int x, int y) {
        if (x < img.getWidth() && y > 0 && y < img.getHeight()) {
            img.setRGB(x, img.getHeight() - y, Color.RED.getRGB());
            synchronized (dr) {
                dr.markPoint(x, img.getHeight() - y);
            }
        }
    }

    public DirtyRegion getDirtyRegion() {
        return dr;
    }
}
 * }
 * </pre><p>
 * 
 * The mark method is called whenever another agent's location needs to be marked.<p>
 * 
 * The Tracking agent needs to be styled with a custom style that creates a VSpatial that uses
 * the DirtyRegion and {@link  aragost.repast.visualization.UpdatableTexture2D UpdatableTexture2D}.<p>
 *  * <pre>
 * {@code
 * 
 * DefaultStyleOGL2D and create a VImage2D with the UpdatableTexture2D.
 * 
 * public class TrackingStyle extends DefaultStyleOGL2D {

    public VSpatial getVSpatial(Object agent, VSpatial spatial) {
        TrackingDisplayAgent tda = (TrackingDisplayAgent) agent;
        if (spatial == null) {
            UpdatableTexture2D td = new UpdatableTexture2D(tda.getImage(), tda.getDirtyRegion());
            spatial = new VImage2D(td);
        }

        return spatial;
    
    }
  }
 * 
 * }
 * </pre>
 * 
 * 
 * @see aragost.repast.visualization.UpdatableTexture2D UpdatableTexture2D
 * 
 */
public class UpdatableTexture2D extends Texture2D {

  private BufferedImage backingImg;
  private Texture texture;
  private TextureData textureData;
  private BoundingSphere sphere;
  private DirtyRegion dirtyRegion;
  private int listIndex;

  public UpdatableTexture2D(BufferedImage img, DirtyRegion dr) {
    super(img);
    this.dirtyRegion = dr;
    init(img);
  }

  private void init(BufferedImage img) {
    this.backingImg = img;
    sphere = new BoundingSphere(new Point3f(0, 0, 0),
        Math.max(img.getHeight() / 2f, img.getWidth() / 2f));
  }

  public void bind(GL2 gl) {
    if (texture == null) {
      createTexture(gl);
    }
    texture.bind(gl);
  }

  /**
   * Gets the target of this texture.
   * 
   * @param gl
   * @return
   */
  public int getTarget(GL2 gl) {
    if (texture == null) {
      createTexture(gl);
    }
    return texture.getTarget();
  }

  /**
   * Equivalent to gl.glEnable(texture target)
   * 
   * @param gl
   * @return the target that the texture is enabled for
   */
  public int enable(GL2 gl) {
    if (texture == null) {
      createTexture(gl);
    }
    texture.enable(gl);
    return texture.getTarget();
  }

  /**
   * Equivalent to gl.glDisable(texture target)
   * 
   * @param gl
   */
  public void disable(GL2 gl) {
    if (texture == null) {
      createTexture(gl);
    }
    texture.disable(gl);
  }

  /**
   * Gets the BufferedImage drawn on the this Texture2D.
   * 
   * @return the BufferedImage drawn on the this Texture2D.
   */
  public BufferedImage getImage() {
    return backingImg;
  }

  /**
   * Disposes of this texture.
   */
  public void dispose(GL2 gl) {
    texture.destroy(gl);
    texture = null;
  }

  private void createTexture(GL2 gl) {
    textureData = AWTTextureIO.newTextureData(gl.getGLProfile(), backingImg, true);
    texture = AWTTextureIO.newTexture(textureData);

    float width = texture.getWidth() / 2f;
    float height = texture.getHeight() / 2f;

    listIndex = gl.glGenLists(1);
    gl.glNewList(listIndex, GL2.GL_COMPILE);
    gl.glBegin(GL2.GL_QUADS);

    TextureCoords tc = texture.getImageTexCoords();
    gl.glTexCoord2f(tc.left(), tc.bottom());
    gl.glVertex2f(-width, -height);

    gl.glTexCoord2f(tc.right(), tc.bottom());
    gl.glVertex2f(width, -height);

    gl.glTexCoord2f(tc.right(), tc.top());
    gl.glVertex2f(width, height);

    gl.glTexCoord2f(tc.left(), tc.top());
    gl.glVertex2f(-width, height);

    gl.glEnd();
    gl.glEndList();
  }

  public float getWidth() {
    return texture.getWidth();
  }

  public float getHeight() {
    return texture.getHeight();
  }

  public BoundingSphere getBounds() {
    return sphere;
  }

  public void draw(GL2 gl, RenderState rState) {
    synchronized (dirtyRegion) {
      if (dirtyRegion.getWidth() > 0 && dirtyRegion.getHeight() > 0) {
        // System.err.printf("UpdatableTexture2D - updating subimage %d,%d ->
        // %dx%d\n", dirtyRegion.getX(),dirtyRegion.getY(),
        // dirtyRegion.getWidth(), dirtyRegion.getHeight());
        texture.updateSubImage(gl, textureData, 0, dirtyRegion.getX(), dirtyRegion.getY(),
            dirtyRegion.getX(), dirtyRegion.getY(), dirtyRegion.getWidth(),
            dirtyRegion.getHeight());
        dirtyRegion.reset();
      }
    }

    // System.out.println(texture.getTarget() ==
    // GL2.GL_TEXTURE_RECTANGLE_ARB);
    gl.glCallList(listIndex);
  }

}
