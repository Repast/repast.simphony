package repast.simphony.relogo.styles;

import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.umd.cs.piccolo.nodes.PPath;

public class PPathBuilder {
	
	static HashMap<String, List<Shape>> shapeMap = new HashMap<String, List<Shape>>();
	
	public static void init(){
		
		int[] xPoints = {0, 15, 15, 0, 4, 4};
		int[] yPoints = {0, 7, 8, 15, 8, 7};
		Shape shape = new Polygon(xPoints, yPoints, 6);
		
		int[] xP2 = {10, 12, 12, 10};
		int[] yP2 = {1, 1, 14, 14};
		Shape s2 = new Polygon(xP2, yP2, 4);
		
		List list = new ArrayList();
		list.add(shape);
		list.add(s2);
		
		shapeMap.put("first image", list);
		
	}
	
	public static PPath getPPathFromString(String ppathString){
		List<Shape> list = shapeMap.get(ppathString);
		PPath path = new PPath();
		for (Shape shape : list){
			path.addChild(new PPath(shape));
		}
		
		return path;
	}

}
