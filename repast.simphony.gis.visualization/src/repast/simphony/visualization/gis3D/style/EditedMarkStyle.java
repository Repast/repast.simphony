package repast.simphony.visualization.gis3D.style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.render.BasicWWTexture;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PatternFactory;
import gov.nasa.worldwind.render.WWTexture;
import repast.simphony.visualization.editedStyle.DefaultEditedStyleData3D;
import repast.simphony.visualization.editedStyle.EditedStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;
import repast.simphony.visualization.gis3D.PlaceMark;
import repast.simphony.visualization.gui.styleBuilder.IconFactory2D;

public class EditedMarkStyle implements MarkStyle{

	EditedStyleData<?> innerStyle;
	protected BasicWWTexture texture;
	private static Color defaultBackColor = new Color(0f, 0f, 0f, 0f);

	public EditedMarkStyle (String userStyleFile) {
		innerStyle = EditedStyleUtils.getStyle(userStyleFile);
		if (innerStyle == null)
			innerStyle = new DefaultEditedStyleData3D<Object>();

	}

	@Override
	public WWTexture getTexture(Object object, WWTexture texture) {
		
		// TODO check that the texture is only created once
		// TODO dynamic colors
		
		if (texture != null)
			return texture;
		
		else {
		// TODO check for icon file
			
			String shapeName = innerStyle.getShapeWkt();
			Color color = EditedStyleUtils.getColor(innerStyle, object);
			Shape shape = IconFactory2D.getShape(shapeName);

			BufferedImage image = null;
			if (shape != null) {
				createShapeImage(shape, new Dimension(10, 10), 0.7f,  color, defaultBackColor);
			}
			else {
				image = PatternFactory.createPattern(PatternFactory.PATTERN_SQUARE, 
					new Dimension(10, 10), 0.7f,  Color.RED);
			}
			return new BasicWWTexture(image);	
		}
		
	}

	@Override
	public PlaceMark getPlaceMark(Object object, PlaceMark mark) {
		if (mark == null)
			mark = new PlaceMark();
		
		mark.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
		mark.setLineEnabled(false);
		
		return mark;
	}

	@Override
	public Offset getIconOffset(Object obj) {
		return Offset.CENTER;
	}

	@Override
	public double getElevation(Object obj) {
		return 0;
	}

	@Override
	public double getScale(Object obj) {
		return EditedStyleUtils.getSize(innerStyle, obj);
	}

	@Override
	public double getHeading(Object obj) {
		return 0;
	}

	@Override
	public String getLabel(Object obj) {
		return EditedStyleUtils.getLabel(innerStyle, obj);
	}

	@Override
	public Color getLabelColor(Object obj) {
		float[] c = innerStyle.getLabelColor();
		
		return new Color(c[0],c[1],c[2]);
	}

	@Override
	public Font getLabelFont(Object obj) {
		return new Font(innerStyle.getLabelFontFamily(), innerStyle.getLabelFontType(), 
				innerStyle.getLabelFontSize());
	}

	@Override
	public Offset getLabelOffset(Object obj) {
		return Offset.BOTTOM_CENTER;
	}

	@Override
	public double getLineWidth(Object obj) {
		return 1.0;
	}

	@Override
	public Material getLineMaterial(Object obj, Material lineMaterial) {
		if (lineMaterial == null){
			lineMaterial = new Material(Color.BLACK);
		}
		return lineMaterial;
	}

	public static BufferedImage createShapeImage(Shape shape, Dimension size, float scale, Color lineColor, Color backColor){
		int halfWidth = size.width / 2;
		int halfHeight = size.height / 2;
		int dim = (int)(size.width * scale);
		BufferedImage image = new BufferedImage(size.width,  size.height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Background
		g2.setPaint(backColor);
		g2.fillRect(0, 0, size.width, size.height);
		if (scale <= 0)
			return image;

		// Pattern
		g2.setPaint(lineColor);
		g2.setStroke(new BasicStroke(dim));
		 
		// TODO check that the shape is centered correctly.
		int x = halfWidth - dim / 2;
    int y = halfHeight - dim / 2;
		
    g2.translate(-x, -y);
    
    g2.draw(shape);
		g2.dispose();
		return image;
	}

}
