package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Polyline;

import java.awt.Color;
import java.util.ArrayList;

import javax.media.opengl.GL;

import com.vividsolutions.jts.geom.Coordinate;


public class MultiLine extends RenderableShape {

	private double[][] vertices;
	public static final double LINE_HEIGHT_OFFSET = 1000;

//	@Override
//	protected void doRender(DrawContext dc){
//		GL gl = dc.getGL();
//		
//		if (geometry == null) return;
//
//		int numPts = geometry.getCoordinates().length;
//		vertices = new double[numPts][3];
//		Vec4 origin = null;
//		
//		// TODO may be slow to find all the points each time
//		
//		int i = 0;
//		for (Coordinate coord : geometry.getCoordinates()){
//			LatLon latlon = WWUtils.CoordToLatLon(coord);
//			
//			double elevation = dc.getGlobe().getElevation(latlon.getLatitude(), 
//					latlon.getLongitude());	
//
//			Vec4 pt = dc.getGlobe().computePointFromPosition(latlon.getLatitude(), 
//					latlon.getLongitude(), elevation + LINE_HEIGHT_OFFSET);
//		
//			// TODO FIX!!!
//			if (i == 0){
//				origin = pt;
//			}
//		
//			vertices[i] = pt.subtract3(origin).toArray3(new double[3], 0);
//			
//			i++;
//		}
//		
//		// ***** start actual rendering code *****
//		
//		// turn off lighting for lines.
//		gl.glDisable(GL.GL_LIGHTING);
//		gl.glDisable(GL.GL_NORMALIZE);
//		
////		int attrBits = GL.GL_HINT_BIT | GL.GL_CURRENT_BIT | GL.GL_LINE_BIT;
////    gl.glPushAttrib(attrBits);
////		
//		dc.getView().pushReferenceCenter(dc, origin);
//		
//		if (dc.isPickingMode()){
//			gl.glLineWidth(8.0f);
//		}
//		else{
//			gl.glLineWidth(1.0f);
//			
//			// TODO get color
//			dc.getGL().glColor4ub((byte) Color.RED.getRed(), (byte) Color.RED.getGreen(),
//	        (byte) Color.RED.getBlue(), (byte) Color.RED.getAlpha());
//		}
//		gl.glBegin(GL.GL_LINE_STRIP);
//		 for (int n=0; n<numPts; n++){
////			 gl.glVertex3d(vertices[i][0],vertices[i][1],vertices[i][2]);
////			 gl.glNormal3f(1f, 0f, 0f);
//			 gl.glVertex3dv(vertices[n],0);
//		}
//		gl.glEnd();
//
//		dc.getView().popReferenceCenter(dc);
//		
////		 gl.glPopAttrib();
//	}
	
//	private ConformingPolygon polygon;
	private Polyline polyLine;
	private ArrayList<LatLon> points;
	
	public MultiLine(){
		polyLine = new Polyline();
//		polyGon = new ConformingPolygon();
		polyLine.setLineWidth(2.0);
		
//		Color color = new Color(1,1,0,1.0f);
//		polyLine.setColor(color);
//		polyLine.setFilled(true);
//		polyLine.setClosed(true);
		
		polyLine.setFollowTerrain(true);
	}
	
	@Override
	protected void doRender(DrawContext dc){
		if (geometry == null) return;
		
		if (points == null){
			points = new ArrayList<LatLon>();
			
			for (Coordinate coord : geometry.getCoordinates()){
			  points.add(WWUtils.CoordToLatLon(coord));
			}
		}
		
		polyLine.setPositions(points, 0);
		
//		RenderUtils.end(dc);
		polyLine.render(dc);
//		RenderUtils.begin(dc);
	}
	
	public void setColor(Color color){
		polyLine.setColor(color);
	}
}
