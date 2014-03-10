package repast.simphony.space;

import repast.simphony.query.space.projection.Within;
import repast.simphony.space.projection.Projection;

/**
 * A simple geography projection for use with agents with lat lon coordinates.  
 * This interface is intended to define methods that are not tied to a specific 
 * GIS library implementation.  Default Repast subclasses are located in the 
 * repast.simphony.gis package.
 * 
 * @author Eric Tatara
 *
 * @param <T> Object type contained by the geography.
 */
public interface IGeography <T> extends Projection<T> {

	/**
	 * The implementing geography needs to provide a method to evaluate a Within
	 *   spatial predicate that may be used by the watcher mechanism.
	 *   
	 * @param within the Within predicate
	 * @return the evaluation of the predicate
	 */
	public boolean evaluateWithin(Within within);
	
}
