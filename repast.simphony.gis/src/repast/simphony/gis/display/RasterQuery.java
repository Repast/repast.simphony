package repast.simphony.gis.display;

import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.Envelope2D;

import java.awt.geom.Point2D;

public class RasterQuery extends PBasicInputEventHandler {

	GridCoverage2D grid;

	public RasterQuery(GridCoverage2D grid) {
		this.grid = grid;
	}

	public void mouseClicked(PInputEvent ev) {
		int[] d = new int[1];
		Point2D position = ev.getPosition();
		Envelope2D e = grid.getEnvelope2D();
		double height = e.getHeight();
		double minY = e.getMinY();
		double y = (height - (position.getY() - minY)) + minY;
		position.setLocation(position.getX(), y);
	}
}
