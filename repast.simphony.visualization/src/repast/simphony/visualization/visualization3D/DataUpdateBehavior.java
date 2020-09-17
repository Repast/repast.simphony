package repast.simphony.visualization.visualization3D;

import java.util.Iterator;

import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.Geometry;
import org.jogamp.java3d.WakeupOnBehaviorPost;

/**
 * Behavior to update geometry.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class DataUpdateBehavior extends Behavior {

	private WakeupOnBehaviorPost wakeUp;
	private DataGeometryUpdater updater;
	private Geometry geometry;
	//private int index;

	public DataUpdateBehavior(Geometry geometry, DataGeometryUpdater updater, Behavior masterBehavior) {
		this.updater = updater;
		this.geometry = geometry;
		wakeUp = new WakeupOnBehaviorPost(masterBehavior, 0);
	}

	public void initialize() {
		wakeupOn(wakeUp);
	}

	public void processStimulus(Iterator criteria) {
		updater.updateData(geometry);
		wakeupOn(wakeUp);
	}
}
