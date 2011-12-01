package repast.simphony.gis.display;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.util.PFixedWidthStroke;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PRescaleNode extends PPath implements PropertyChangeListener {

	private static final long serialVersionUID = 7117359225852162432L;

	int size;

	Point2D location = new Point2D.Float();

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(PCamera.PROPERTY_VIEW_TRANSFORM)) {
			PCamera camera = (PCamera) evt.getSource();
			Point2D newLoc = globalToLocal(location);
			animateToBounds(newLoc.getX(), newLoc.getY(), (1 / camera.getViewScale()) * size,
					(1 / camera.getViewScale()) * size, 0);
		}
	}

	public static void main(String[] args) {
		TestFrame frame = new TestFrame();
		PCanvas canvas = new PCanvas();
		frame.add(canvas, BorderLayout.CENTER);
		canvas.setSize(600, 600);
		PRescaleNode path = new PRescaleNode();
		canvas.getCamera().addPropertyChangeListener(path);
		path.setPathTo(new Rectangle2D.Float(0, 0, 10, 10));
		path.location.setLocation(20, 20);
		path.animateToBounds(20, 20, 10, 10, 0);
		path.setStroke(new PFixedWidthStroke(1));
		path.size = 10;
		canvas.getLayer().addChild(path);
		frame.setVisible(true);
	}

}
