package repast.simphony.gis.styleEditor;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * The icon ("label") that represents a styled mark in the GIS style editors.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 *
 */
public class PreviewLabel extends JLabel {

	protected static final int SMALL_MARK_SIZE = 10;
	protected static final int PREVIEW_SIZE = 100;
	
	protected GeneralPath polygonShape;
	protected Shape shape;
	protected String mark = null;
	protected Color fillColor = Color.WHITE;
	protected double fillOpacity = 1;
	protected Color outlineColor = Color.BLACK;
	protected double outlineOpacity = 1;
	protected double outlineThickness = 1;
	protected double markSize = 6;
	protected double markRotation = 0;
	protected BufferedImage image;
	
	protected static SimpleMarkFactory markFac = new SimpleMarkFactory();
	
	public PreviewLabel() {
		image = new BufferedImage(PREVIEW_SIZE, PREVIEW_SIZE, BufferedImage.TYPE_4BYTE_ABGR);
		setText("");
		this.setIcon(new ImageIcon(image));
		polygonShape = new GeneralPath();
		polygonShape.moveTo(10, 10);
		polygonShape.lineTo(90, 30);
		polygonShape.lineTo(70, 90);
		polygonShape.lineTo(10, 90);
		polygonShape.closePath();
		shape = polygonShape;
		updatePreview();
	}

	/**
	 * Updates the PreviewLabel icon based on the current values for fill, opacity, etc.
	 * 
	 */
	public void updatePreview() {
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, PREVIEW_SIZE, PREVIEW_SIZE);
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (fillColor != null){
			g2d.setColor(fillColor);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(float) fillOpacity));
			g2d.fill(shape);
		}
		if (outlineColor != null){
			g2d.setColor(outlineColor);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(float) outlineOpacity));
			g2d.setStroke(new BasicStroke((float) outlineThickness));
		}
		
		g2d.draw(shape);
		
	  // Flip the image vertically to match the Geotools render orientation
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -image.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);
		
		this.setIcon(new ImageIcon(image));
	}

	
	/**
	 * Provide a small icon for table cells or other locations where just the
	 * small icon is needed. 
	 * 
	 * @return the small icon
	 */
	public ImageIcon getSmallIcon() {
		BufferedImage img = new BufferedImage(2*SMALL_MARK_SIZE, 2*SMALL_MARK_SIZE, 
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		Shape s = null;
		
		if (mark != null){
			s  = AffineTransform.getScaleInstance(SMALL_MARK_SIZE, SMALL_MARK_SIZE)
					.createTransformedShape(markFac.getMark(mark));
		  
			// Center the shape in the icon image
			AffineTransform transform = AffineTransform
					.getTranslateInstance(SMALL_MARK_SIZE, SMALL_MARK_SIZE);
			s = transform.createTransformedShape(s);
			
		  // apply rotation (convert deg to rad)
			transform = AffineTransform.getRotateInstance(-markRotation*Math.PI/180, 
					SMALL_MARK_SIZE, SMALL_MARK_SIZE);
			s = transform.createTransformedShape(s);
		}
		else
			s = shape;
				
		if (fillColor != null){
			g2d.setColor(fillColor);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(float) fillOpacity));
			g2d.fill(s);
		}
		if (outlineColor != null){
			g2d.setColor(outlineColor);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(float) outlineOpacity));
			g2d.setStroke(new BasicStroke((float) outlineThickness));
		}
		
		g2d.draw(s);
		
		 // Flip the image vertically to match the Geotools render orientation
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -img.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		img = op.filter(img, null);
		
		return new ImageIcon(img);
	}
	
	/**
	 * Set the mark based on the well-known text string (ie "circle").
	 * 
	 * @param mark the well-known text string
	 */
	public void setMark(String mark) {
		this.mark = mark;
		updateShape();
		updatePreview();
	}

	/**
	 * Set the Shape instance to a polygon shape.
	 */
	public void setShapeToPolygon() {
    polygonShape = new GeneralPath();
		polygonShape.moveTo(10, 10);
		polygonShape.lineTo(90, 30);
		polygonShape.lineTo(70, 90);
		polygonShape.lineTo(10, 90);
		polygonShape.closePath();
		shape = polygonShape;
		mark = null;
  }

  /**
   * Set the Shape instance to a line shape.
   */
	public void setShapeToLine() {
		GeneralPath path = new GeneralPath();
		path.moveTo(10, 10);
		path.lineTo(90, 90);
		shape = path;
		updatePreview();
		mark = null;
	}

	public void updateShape() {
		// Create a shape according to the mark type and size
		shape = AffineTransform.getScaleInstance(markSize, markSize)
				.createTransformedShape(markFac.getMark(mark));
		
		// Center the shape in the icon image
		AffineTransform transform = AffineTransform
				.getTranslateInstance(PREVIEW_SIZE/2, PREVIEW_SIZE/2);
		shape = transform.createTransformedShape(shape);
		
		// apply rotation (convert deg to rad)
		transform = AffineTransform.getRotateInstance(-markRotation*Math.PI/180, 
				PREVIEW_SIZE/2, PREVIEW_SIZE/2);
		shape = transform.createTransformedShape(shape);
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		updatePreview();
	}

	public void setFillOpacity(double fillOpacity) {
		this.fillOpacity = fillOpacity;
		updatePreview();
	}

	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
		updatePreview();
	}

	public void setOutlineOpacity(double outlineOpacity) {
		this.outlineOpacity = outlineOpacity;
		updatePreview();
	}

	public void setMarkSize(double markSize) {
		this.markSize = markSize;
		updateShape();
		updatePreview();
	}
	
	public void setMarkRotation(double markRotation) {
		this.markRotation = markRotation;
		updateShape();
		updatePreview();
	}

	public void setOutlineThickness(double outlineThickness) {
		this.outlineThickness = outlineThickness;
		updatePreview();
	}
}