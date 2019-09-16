package repast.simphony.visualization.gis3D.style;

import java.awt.Color;

import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.render.SurfacePolyline;
import gov.nasa.worldwind.render.SurfaceShape;
import repast.simphony.visualization.editedStyle.DefaultEditedStyleData3D;
import repast.simphony.visualization.editedStyle.EditedStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;

public class EditedSurfaceShapeStyle implements SurfaceShapeStyle {

	public static String POLYGON = "polygon";
	public static String LINE = "line";
	
	EditedStyleData<?> innerStyle;
	
	public EditedSurfaceShapeStyle (String userStyleFile) {
		innerStyle = EditedStyleUtils.getStyle(userStyleFile);
		if (innerStyle == null)
			innerStyle = new DefaultEditedStyleData3D<Object>();
	}
	
	@Override
	public SurfaceShape getSurfaceShape(Object object, SurfaceShape shape) {
		
		String shapeName = innerStyle.getShapeWkt();

		if (LINE.equalsIgnoreCase(shapeName)){
			shape = new SurfacePolyline();
		}
		
		else if (POLYGON.equalsIgnoreCase(shapeName)){
			shape = new SurfacePolygon();
			ShapeAttributes attrs =  new BasicShapeAttributes();		 
			shape.setAttributes(attrs);
		}
		return shape;
	}

	@Override
	public Color getFillColor(Object obj) {
		if (POLYGON.equalsIgnoreCase(innerStyle.getShapeWkt())){
			return EditedStyleUtils.getColor(innerStyle, obj);
		}
		return null;
	}

	@Override
	public double getFillOpacity(Object obj) {
		if (POLYGON.equalsIgnoreCase(innerStyle.getShapeWkt())){
			return 0.8;
		}
		return 1.0;
	}

	@Override
	public Color getLineColor(Object obj) {
		if (POLYGON.equalsIgnoreCase(innerStyle.getShapeWkt())){
			return Color.BLACK;
		}
		else {
			return EditedStyleUtils.getColor(innerStyle, obj);
		}
	}

	@Override
	public double getLineOpacity(Object obj) {
		return 1.0;
	}

	@Override
	public double getLineWidth(Object obj) {
		
		if (LINE.equalsIgnoreCase(innerStyle.getShapeWkt())){
			return EditedStyleUtils.getSize(innerStyle, obj);
		}
		
		return 1;
	}
}