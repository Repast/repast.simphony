package repast.simphony.visualization.editedStyle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.geotools.renderer.lite.Java2DMark;

import repast.simphony.visualization.visualization2D.style.Style2D;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * 
 * @author Eric Tatara
 *
 */
public class EditedStyle2D implements Style2D<Object> {
	EditedStyleData<Object> innerStyle;
//	private BufferedImage image;
	private Image image;
//	String iconFile = null;

	public EditedStyle2D(String userStyleFile) {
		innerStyle = EditedStyleUtils.getStyle(userStyleFile);
		if (innerStyle == null)
			innerStyle = new DefaultEditedStyleData2D<Object>();

		String iconFile = innerStyle.getIconFile2D();
		BufferedImage im = null;
		if (iconFile != null){
			File file = new File(iconFile);
			try {
				im = ImageIO.read(file);

				// flip the image vertically so that it shows up with the
				// correct orientation in the inverted piccolo canvas
				PImage pimage = new PImage(im);
				AffineTransform trans = new AffineTransform();
				trans.setToScale(1, -1);
				pimage.transformBy(trans);
				image = pimage.toImage();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Rectangle2D getBounds(Object object) {
		float size = EditedStyleUtils.getSize(innerStyle, object);

		return new Rectangle2D.Float(0, 0, size, size);
	}

	public PNode getPNode(Object object, PNode node) {
		PPath path;
		float size = EditedStyleUtils.getSize(innerStyle, object);

//		AffineTransform trans = new AffineTransform();
//		trans.setToScale(1, -1);

		// get the shape from an icon
		if (image != null){
//			if (iconFile != null){
//			path = new PPath();
			PImage pimage = new PImage(image);
//			PImage pimage = new PImage(iconFile);
//			pimage.transformBy(trans);
			pimage.setBounds(new Rectangle2D.Float(0, 0, size, size));

			return pimage;
		}

		// get the shape from well-known text
		else{
			String wkt = innerStyle.getShapeWkt();
			Shape shape = Java2DMark.getWellKnownMark(wkt);
			path = new PPath(shape);
//			path.transformBy(trans);
			path.setBounds(new Rectangle2D.Float(0, 0, size, size));

			return path;
		}
	}

	public Paint getPaint(Object object) {  
		return EditedStyleUtils.getColor(innerStyle, object);
	}

	public double getRotation(Object object) {
		// TODO 
		return 0;
	}
	public Stroke getStroke(Object object) {
		// TODO 
		return new BasicStroke(1.0f);
	}
	public Paint getStrokePaint(Object object) {
		// TODO 
		return Color.BLACK;
	}
	public boolean isScaled(Object object) {
		// TODO 
		return true;
	}

	public PText getLabel(Object object) {

		PText label = null;

		label = new PText(EditedStyleUtils.getLabel(innerStyle, object));

		Font font = new Font(innerStyle.getLabelFontFamily(), 
				innerStyle.getLabelFontType(),
				innerStyle.getLabelFontSize());

		if ("".equals(label.getText()))
			return null;

		label.setFont(font);

		double offSet = innerStyle.getLabelOffset();

		double xOffSet = 0;
		double yOffSet = 0;

		double width = getBounds(object).getWidth();
		double height = getBounds(object).getHeight();

		// right
		if ("right".equals(innerStyle.getLabelPosition())){
			xOffSet = width + offSet;
			yOffSet = height/2 + label.getBounds().height/2;		}
		// left
		else if ("left".equals(innerStyle.getLabelPosition())){
			xOffSet = -label.getBounds().width - offSet;
			yOffSet = height/2 + label.getBounds().height/2;
		}
		// top
		else if ("top".equals(innerStyle.getLabelPosition())){
			xOffSet = -(label.getWidth()/2 - width/2);
			yOffSet = height + label.getBounds().height + offSet;
		}
		else{
			xOffSet = -(label.getWidth()/2 - width/2);
			yOffSet = - offSet;
		}
		label.setOffset(xOffSet,yOffSet);

		float colorRGB[] = innerStyle.getLabelColor();

		label.setTextPaint(new Color(colorRGB[0],colorRGB[1],colorRGB[2]));

		return label;
	}
}
