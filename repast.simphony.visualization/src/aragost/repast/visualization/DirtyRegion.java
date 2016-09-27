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

/**
 * Tracks which part of a region is "dirty" and needs to be redrawn. Points are marked
 * as dirty using the {@link #markPoint(int, int) markPoint} method.<p>
 * 
 * This class is intended to be used in conjunction with a user created "background agent"
 * and the {@link  aragost.repast.visualization.UpdatableTexture2D UpdatableTexture2D} class.
 * For example, a TrackingDisplayAgent is a pseudo-agent that is used to paint a background
 * that displays the tracks (the previous locations) of other agents. It creates a
 * BufferedImage that is displayed by the {@link  aragost.repast.visualization.UpdatableTexture2D 
 * UpdatableTexture2D} and contains a DirtyRegion that tracks what parts of this image are dirty.<p>
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
public class DirtyRegion {

  private int minX;
  private int minY;
  private int maxX;
  private int maxY;

  public DirtyRegion(int x, int y, int width, int height) {
    this.minX = x;
    this.minY = y;
    this.maxY = width;
    this.maxY = height;
  }

  public void reset() {
    minX = Integer.MAX_VALUE;
    minY = Integer.MAX_VALUE;
    maxX = 0;
    maxY = 0;
  }

  public int getX() {
    return minX;
  }

  public int getY() {
    return minY;
  }

  public int getWidth() {
    return maxX - minX;
  }

  public int getHeight() {
    return maxY - minY;
  }

  /**
   * Marks the specified point as dirty and
   * needing to be redrawn
   * 
   * @param x the x coordinate of the dirty point
   * @param y the y coordinate of the dirty point
   */
  public void markPoint(int x, int y) {
    if (x < this.minX) {
      this.minX = x;
    }
    if (x > this.maxX) {
      this.maxX = x;
    }
    if (y < this.minY) {
      this.minY = y;
    }
    if (y > this.maxY) {
      this.maxY = y;
    }
  }

}
