package repast.simphony.visualization.gis3D.style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
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
				image = createShapeImage(shape, new Dimension(20, 20), color);
			}
			else {
				image = PatternFactory.createPattern(PatternFactory.PATTERN_SQUARE, 
					new Dimension(10, 10), 1.0f, Color.RED);
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
		// Reduce the scale by a factor so that the DefaultEditedStyleData2D 
		//    default size doesnt make the shape too large.
		return EditedStyleUtils.getSize(innerStyle, obj) / 10 ;
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

	public static BufferedImage createShapeImage(Shape shape, Dimension size, Color color){
		double halfWidth = size.width / 2;
		double halfHeight = size.height / 2;
		
		BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Translate the shape to the image center, and scale to image size
		AffineTransform at = new AffineTransform();
		at.translate(halfWidth, halfHeight);
		at.scale(0.8*size.width, 0.8*size.height);
    shape = at.createTransformedShape(shape);
   
	  // Background Color TODO provide as parameters
    Color bgcolor = new Color(0,0,0,0);
    g2.setPaint(bgcolor);
    g2.fillRect(0, 0, size.width, size.height);
		
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(2.0f));
		g2.draw(shape);
		
    g2.setPaint(color); 
    g2.fill(shape);
    
		g2.dispose();
		return image;
	}

}
