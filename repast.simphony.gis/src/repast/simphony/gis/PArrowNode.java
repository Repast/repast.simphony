package repast.simphony.gis;

import java.awt.BasicStroke;
import java.awt.Stroke;

import edu.umd.cs.piccolo.nodes.PPath;

public class PArrowNode extends PPath {
	private static final long serialVersionUID = 5777336609201157067L;

	Stroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND);

	public PArrowNode(int stemLength) {
		setStroke(stroke);
		moveTo(0, 0);
		lineTo(stemLength, 0);
		lineTo((float) (stemLength * .75), (float) (stemLength * .5));
		moveTo(stemLength, 0);
		lineTo((float) (stemLength * .75), (float) (stemLength * -.5));
	}
}