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
		
		// TODO Geotools
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
		this.setIcon(new ImageIcon(image));
	}

	
	/**
	 * Provide a small icon for table cells or other locations where just the
	 * small icon is needed. 
	 * 
	 * 
	 * @return the small icon
	 */
	public ImageIcon getSmallIcon() {
		Image img = new BufferedImage(2*SMALL_MARK_SIZE, 2*SMALL_MARK_SIZE, 
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		
		//TODO Geotools
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		Shape s = null;
		
		if (mark != null){
			s  = AffineTransform.getScaleInstance(SMALL_MARK_SIZE, SMALL_MARK_SIZE)
					.createTransformedShape(Java2DMark.getWellKnownMark(mark));
			AffineTransform transform = AffineTransform
					.getTranslateInstance(SMALL_MARK_SIZE, SMALL_MARK_SIZE);
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
	 * @param rule the rule used to format the icon
	 * 
	 * @return the rule-based icon.
	 * 
	 */	
	public static Icon createIcon(Rule rule){
		PreviewLabel preview = new PreviewLabel();
		Icon icon = null;
		Symbolizer sym = rule.symbolizers().get(0);
		
		if (sym instanceof PointSymbolizer){
			PointSymbolizer ps = (PointSymbolizer) sym;	
			Mark mark = (Mark)ps.getGraphic().graphicalSymbols().get(0);
				
			double size = Double.valueOf(ps.getGraphic().getSize().toString());
			Color fillColor = Color.decode(mark.getFill().getColor().toString());	
			double fillOpacity = Double.valueOf(ps.getGraphic().getOpacity().toString());
			Color outlineColor = Color.decode(mark.getStroke().getColor().toString());
			double outlineThickness = Double.valueOf(mark.getStroke().getWidth().toString());
			double outlineOpacity = Double.valueOf(mark.getStroke().getOpacity().toString());
			
			preview.setMark(mark.getWellKnownName().toString());
			preview.setMarkSize(size);
			preview.setFillColor(fillColor);
			preview.setFillOpacity(fillOpacity);
			preview.setOutlineColor(outlineColor);
			preview.setOutlineThickness(outlineThickness);
			preview.setOutlineOpacity(outlineOpacity);
			
		}
		else if (sym instanceof LineSymbolizer){
			preview.setShapeToLine();
			LineSymbolizer ls = (LineSymbolizer) sym;
			
			Color c = Color.decode(ls.getStroke().getColor().toString());
			double width = Double.valueOf(ls.getStroke().getWidth().toString());
			double opacity = Double.valueOf(ls.getStroke().getOpacity().toString());
			
			preview.setOutlineColor(c);
			preview.setOutlineThickness(width);
			preview.setOutlineOpacity(opacity);
			
		}
		else if (sym instanceof PolygonSymbolizer){
			preview.setShapeToPolygon();
			PolygonSymbolizer ps = (PolygonSymbolizer) sym;
			
			Color fillColor = Color.decode(ps.getFill().getColor().toString());
			double fillOpacity = Double.valueOf(ps.getFill().getOpacity().toString());
			Color outlineColor = Color.decode(ps.getStroke().getColor().toString());
			double outlineThickness = Double.valueOf(ps.getStroke().getWidth().toString());
			double outlineOpacity = Double.valueOf(ps.getStroke().getOpacity().toString());
			
			preview.setFillColor(fillColor);
			preview.setFillOpacity(fillOpacity);
			preview.setOutlineColor(outlineColor);
			preview.setOutlineThickness(outlineThickness);
			preview.setOutlineOpacity(outlineOpacity);

		}
	
		preview.updatePreview();
		icon = preview.getSmallIcon();		
		
		return icon;
	}
	
}
