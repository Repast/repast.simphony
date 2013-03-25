package repast.simphony.gis.styleEditor;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.geotools.renderer.lite.Java2DMark;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.opengis.style.Symbolizer;

/**
 * The icon ("label") that represents a styled mark in the GIS style editors.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 *
 */
public class PreviewLabel extends JLabel {

	private static final int SMALL_MARK_SIZE = 10;
	private GeneralPath polygonShape;
	private Shape shape;
	private String mark = null;
	private Color fillColor = Color.WHITE;
	private double fillOpacity = 1;
	private Color outlineColor = Color.BLACK;
	private double outlineOpacity = 1;
	private double outlineThickness = 1;
	private double markSize = 6;
	private Image image;

	public PreviewLabel() {
		image = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
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

	public void updatePreview() {
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 100, 100);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
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
		this.setIcon(new ImageIcon(image));
	}
	
	public ImageIcon getSmallIcon() {
		return getSmallIcon(null, null);
	}
	
	/**
	 * Provide a small icon for table cells or other locations where just the
	 * small icon is needed. Optionally provide a new fill color.
	 * 
	 * @param newFill the optional new fill color
	 * @return the small icon
	 */
	public ImageIcon getSmallIcon(Color newFill, Color newLine) {
		Image img = new BufferedImage(2*SMALL_MARK_SIZE, 2*SMALL_MARK_SIZE, 
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		Shape s = null;
		Color fill = null;
		Color line = null;
		
		if (mark != null){
			s  = AffineTransform.getScaleInstance(SMALL_MARK_SIZE, SMALL_MARK_SIZE)
					.createTransformedShape(Java2DMark.getWellKnownMark(mark));
	  AffineTransform transform = AffineTransform
			.getTranslateInstance(SMALL_MARK_SIZE, SMALL_MARK_SIZE);
	  s = transform.createTransformedShape(s);
		}
		else
			s = shape;
		
		if (newFill != null)
			fill = newFill;
		else
			fill = fillColor;
		
		if (newLine != null)
			line = newLine;
		else
			line = outlineColor;
		
		if (fill != null){
			g2d.setColor(fill);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(float) fillOpacity));
			g2d.fill(s);
		}
		if (line != null){
			g2d.setColor(line);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(float) outlineOpacity));
			g2d.setStroke(new BasicStroke((float) outlineThickness));
		}
		
		g2d.draw(s);
		
		return new ImageIcon(img);
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		updatePreview();
	}

	public double getFillOpacity() {
		return fillOpacity;
	}

	public void setFillOpacity(double fillOpacity) {
		this.fillOpacity = fillOpacity;
		updatePreview();
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
		updateShape();
		updatePreview();
	}

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

  public void setShapeToLine() {
		GeneralPath path = new GeneralPath();
		path.moveTo(10, 10);
		path.lineTo(90, 90);
		shape = path;
		updatePreview();
		mark = null;
	}

	private void updateShape() {
		shape = AffineTransform.getScaleInstance(markSize, markSize)
				.createTransformedShape(Java2DMark.getWellKnownMark(mark));
		AffineTransform transform = AffineTransform
				.getTranslateInstance(50, 50);
		shape = transform.createTransformedShape(shape);
	}

	public Color getOutlineColor() {
		return outlineColor;
	}

	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
		updatePreview();
	}

	public double getOutlineOpacity() {
		return outlineOpacity;
	}

	public void setOutlineOpacity(double outlineOpacity) {
		this.outlineOpacity = outlineOpacity;
		updatePreview();
	}

	public double getMarkSize() {
		return markSize;
	}

	public void setMarkSize(double markSize) {
		this.markSize = markSize;
		updateShape();
		updatePreview();
	}

	public double getOutlineThickness() {
		return outlineThickness;
	}

	public void setOutlineThickness(double outlineThickness) {
		this.outlineThickness = outlineThickness;
		updatePreview();
	}
	
	/**
	 * Generate an Icon from a Rule.
	 * 
	 * @param label the PreviewLabel from which to get the base icon
	 * @param rule the rule used to format the icon
	 * 
	 * @return the rule-based icon.
	 * 
	 * TODO Geotools this should be changed to be only dependent on the Rule
	 *      and not require a preview label to be more flexible.
	 */
	public static Icon formatPreview(PreviewLabel label, Rule rule){
		Icon icon = null;
		Symbolizer sym = rule.symbolizers().get(0);
			
		// TODO only does color...add mark, stroke, etc.
		
		if (sym instanceof PointSymbolizer){
			PointSymbolizer ps = (PointSymbolizer) sym;	
			Mark mark = (Mark)ps.getGraphic().graphicalSymbols().get(0);
			Color c = Color.decode(mark.getFill().getColor().toString());	
			icon = label.getSmallIcon(c,null);
		}
		else if (sym instanceof LineSymbolizer){
			LineSymbolizer ls = (LineSymbolizer) sym;
			Color c = Color.decode(ls.getStroke().getColor().toString());
			icon = label.getSmallIcon(null, c);
		}
		else if (sym instanceof PolygonSymbolizer){
			PolygonSymbolizer ps = (PolygonSymbolizer) sym;		
			Color c = Color.decode(ps.getFill().getColor().toString());	
			icon = label.getSmallIcon(c,null);
		}
	
		return icon;
	}
}
