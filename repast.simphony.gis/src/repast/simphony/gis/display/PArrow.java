package repast.simphony.gis.display;

import java.awt.geom.GeneralPath;

import edu.umd.cs.piccolo.nodes.PPath;

public class PArrow extends PPath {

	public PArrow() {
		this.getPathReference().setWindingRule(GeneralPath.WIND_EVEN_ODD);
		moveTo(0f, -5f);
		lineTo(.5f, 0f);
		lineTo(0f, 5f);
		lineTo(0f, .1f);
		lineTo(-.5f, .1f);
		lineTo(-.5f, -.1f);
		lineTo(0f, -.1f);
		lineTo(0f, -.5f);

	}

	public void setLength(double length) {
		float dist = (float) (length / 2);
		this.reset();
		GeneralPath path = new GeneralPath();
		path.moveTo(0f, -3f);
		path.lineTo(5, 0f);
		path.lineTo(0f, 3f);
		path.lineTo(2f, .5f);
		path.lineTo(-dist, .5f);
		path.lineTo(-dist, -.5f);
		path.lineTo(2f, -.5f);
		path.lineTo(0f, -3f);
		setPathTo(path);
	}
}
