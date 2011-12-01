package repast.simphony.ui.probe;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;

/**
 * ProbeableBean that for an objects location in a continuous space.
 * 
 * @author Nick Collier
 */
public class SpaceLocationProbe extends ProbeableBean {

	private Object obj;
	private ContinuousSpace space;

	public SpaceLocationProbe(Object obj, ContinuousSpace space) {
		this.obj = obj;
		this.space = space;
	}

	/**
	 * Gets the objects location as a String.
	 * 
	 * @return the objects location as a String.
	 */
	public String getLocation() {
		NdPoint pt = space.getLocation(obj);
		if (pt == null) {
			return "N/A";
		} else {
			String val = "";
			int dims = pt.dimensionCount();
			int dimsMinusOne = dims - 1;
			for (int i = 0; i < dims; i++) {
				val += Utils.getNumberFormatInstance().format(pt.getCoord(i));
				if (i < dimsMinusOne) {
					val += ", ";
				}
			}
			return val;
		}
	}
}