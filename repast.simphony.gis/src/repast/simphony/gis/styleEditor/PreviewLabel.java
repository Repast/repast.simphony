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

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.geotools.renderer.lite.Java2DMark;

public class PreviewLabel extends JLabel {

	private GeneralPath polygonShape;

	private Shape shape;

	private String mark = null;

	private Color fillColor = Color.WHITE;

	private double fillOpacity = 1;

	private Color outlineColor = Color.BLACK;

	private double outlineOpacity = 1;

	private double outlineThickness = 1;

	private int markSize = 6;

	private Image image;

	public PreviewLabel(int sizeX, int size) {
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
  }

  public void setShapeToLine() {
		GeneralPath path = new GeneralPath();
		path.moveTo(10, 10);
		path.lineTo(90, 90);
		shape = path;
		updatePreview();
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

	public int getMarkSize() {
		return markSize;
	}

	public void setMarkSize(int markSize) {
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
}
