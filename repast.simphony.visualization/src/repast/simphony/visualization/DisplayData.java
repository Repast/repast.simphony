package repast.simphony.visualization;

import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.space.projection.Projection;

/**
 * Interface for those classes that encapsulate the data needed to set up
 * a particular display. This includes access to any existing agents to be added to
 * the display as well access to the projections and ValueLayer-s to be displayed.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface DisplayData<T> {

  /**
   * Gets the container that contains the objects we want to display.
   *
   * @return the container that contains the objects we want to display.
   */
  VisualizedObjectContainer<T> getContainer();

  /**
	 * Gets the objects to be initially displayed.
	 *
	 * @return an iterable over the objects to be initially displayed.
	 */
	Iterable<T> objects();

	/**
	 * Gets an iterable over the projections to be displayed.
	 *
	 * @return an iterable over the projections to be displayed.
	 */
	Iterable<Projection> getProjections();

	/**
	 * Gets the number of projections to display.
	 *
	 * @return the number of projections to display.
	 */
	int getProjectionCount();


	/**
	 * Gets an iterable over the ValueLayers to be displayed.
	 *
	 * @return an iterable over the ValueLayers to be displayed.
	 */
	Iterable<ValueLayer> getValueLayers();

	/**
	 * Gets the number of ValueLayers to display.
	 *
	 * @return the number of ValueLayers to display.
	 */
	int getValueLayerCount();

}
