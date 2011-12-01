package repast.simphony.visualization;

import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.space.projection.Projection;

import java.util.List;
import java.util.ArrayList;

/**
 * Abstract implementation of DisplayData.
 *
 * @author Nick Collier
 */
public abstract class AbstractDisplayData<T> implements DisplayData<T> {

  protected List<Projection> projs = new ArrayList<Projection>();
  protected List<ValueLayer> valueLayers = new ArrayList<ValueLayer>();

  /**
	 * Gets an iterable over the projections to be displayed.
   *
   * @return an iterable over the projections to be displayed.
   */
  public Iterable<Projection> getProjections() {
    return projs;
  }

  /**
	 * Gets an iterable over the ValueLayers to be displayed.
   *
   * @return an iterable over the ValueLayers to be displayed.
   */
  public Iterable<ValueLayer> getValueLayers() {
    return valueLayers;
  }

  /**
	 * Gets the number of projections to display.
   *
   * @return the number of projections to display.
   */
  public int getProjectionCount() {
    return projs.size();
  }

  /**
	 * Gets the number of ValueLayers to display.
   *
   * @return the number of ValueLayers to display.
   */
  public int getValueLayerCount() {
    return valueLayers.size();
  }
}
