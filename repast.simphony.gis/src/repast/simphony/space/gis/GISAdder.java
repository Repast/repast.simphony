package repast.simphony.space.gis;

import repast.simphony.space.projection.Adder;

/**
 * Interface for classes that wish to add objects to a real, geographic space. Typically,
 * this will call Geography.moveTo and move agents into the geography and associate
 * them with a geometry in some model specific way.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface GISAdder<T> extends Adder<Geography<T>, T> {

}
