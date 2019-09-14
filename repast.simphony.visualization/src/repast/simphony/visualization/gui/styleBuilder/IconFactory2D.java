package repast.simphony.visualization.gui.styleBuilder;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * Simple 2D icon factory based adapted from the Geotools WellKnownMarkFactory.
 * 
 * TODO Projections: replace this with ability to use SVG.
 *  
 * @author Eric Tatara
 *
 */
public class IconFactory2D {

	private static Shape cross;
	private static Shape star;
	private static Shape triangle;
	private static Shape arrow;
	private static Shape X;
	private static Shape square;

	public static Set<String> Shape_List = new HashSet<String>(Arrays.asList(
		"circle",
		"cross",
		"star",
		"square",
		"triangle",
		"X",
		"arrow"));
	
	static {
		GeneralPath crossPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		crossPath.moveTo(0.5f, 0.125f);
		crossPath.lineTo(0.125f, 0.125f);
		crossPath.lineTo(0.125f, 0.5f);
		crossPath.lineTo(-0.125f, 0.5f);
		crossPath.lineTo(-0.125f, 0.125f);
		crossPath.lineTo(-0.5f, 0.125f);
		crossPath.lineTo(-0.5f, -0.125f);
		crossPath.lineTo(-0.125f, -0.125f);
		crossPath.lineTo(-0.125f, -0.5f);
		crossPath.lineTo(0.125f, -0.5f);
		crossPath.lineTo(0.125f, -0.125f);
		crossPath.lineTo(0.5f, -0.125f);
		crossPath.lineTo(0.5f, 0.125f);

		cross = crossPath;

		AffineTransform at = new AffineTransform();
		at.rotate(Math.PI / 4.0);
		X = crossPath.createTransformedShape(at);

		GeneralPath starPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		starPath.moveTo(0.191f, 0.0f);
		starPath.lineTo(0.25f, 0.344f);
		starPath.lineTo(0.0f, 0.588f);
		starPath.lineTo(0.346f, 0.638f);
		starPath.lineTo(0.5f, 0.951f);
		starPath.lineTo(0.654f, 0.638f);
		starPath.lineTo(1.0f, 0.588f); // max = 7.887
				starPath.lineTo(0.75f, 0.344f);
		starPath.lineTo(0.89f, 0f);
		starPath.lineTo(0.5f, 0.162f);
		starPath.lineTo(0.191f, 0.0f);
		at = new AffineTransform();
		at.translate(-.5, -.5);
		starPath.transform(at);

		star = starPath;

		GeneralPath trianglePath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		trianglePath.moveTo(0f, 1f);
		trianglePath.lineTo(0.866f, -.5f);
		trianglePath.lineTo(-0.866f, -.5f);
		trianglePath.lineTo(0f, 1f);
		at = new AffineTransform();
		at.translate(0, -.25);
		at.scale(.5, .5);
		trianglePath.transform(at);

		triangle = trianglePath;

		GeneralPath arrowPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		arrowPath.moveTo(0f, -.5f);
		arrowPath.lineTo(.5f, 0f);
		arrowPath.lineTo(0f, .5f);
		arrowPath.lineTo(0f, .1f);
		arrowPath.lineTo(-.5f, .1f);
		arrowPath.lineTo(-.5f, -.1f);
		arrowPath.lineTo(0f, -.1f);
		arrowPath.lineTo(0f, -.5f);

		arrow = arrowPath;

		GeneralPath hatchPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		hatchPath.moveTo(.55f,.57f);
		hatchPath.lineTo(.52f,.57f);
		hatchPath.lineTo(-.57f,-.52f);
		hatchPath.lineTo(-.57f,-.57f);
		hatchPath.lineTo(-.52f, -.57f);
		hatchPath.lineTo(.57f, .52f);
		hatchPath.lineTo(.57f,.57f);

		hatchPath.moveTo(.57f,-.49f);
		hatchPath.lineTo(.49f, -.57f);
		hatchPath.lineTo(.57f,-.57f);
		hatchPath.lineTo(.57f,-.49f);

		hatchPath.moveTo(-.57f,.5f);
		hatchPath.lineTo(-.5f, .57f);
		hatchPath.lineTo(-.57f,.57f);
		hatchPath.lineTo(-.57f,.5f);

		square = new Rectangle2D.Double(-.5, -.5, 1., 1.);
	}

	public static Shape getShape(String symbol) {

		// cannot handle a null url
		if(symbol == null)
			return null;

		if (symbol.equalsIgnoreCase("cross")) {
			return cross;
		}

		if (symbol.equalsIgnoreCase("circle")) {
			return new java.awt.geom.Ellipse2D.Double(-.5, -.5, 1., 1.);
		}

		if (symbol.equalsIgnoreCase("triangle")) {
			return triangle;
		}

		if (symbol.equalsIgnoreCase("X")) {
			return X;
		}

		if (symbol.equalsIgnoreCase("star")) {
			return star;
		}

		if (symbol.equalsIgnoreCase("arrow")) {
			return arrow;
		}


		if (symbol.equalsIgnoreCase("square")) {
			return square;
		}

		return null;
	}
}