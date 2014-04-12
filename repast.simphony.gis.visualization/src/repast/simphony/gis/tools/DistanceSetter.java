package repast.simphony.gis.tools;

import javax.measure.unit.Unit;


public interface DistanceSetter {

	public void setDistance(double distance, Unit units);

	public void clearDistance();

}
