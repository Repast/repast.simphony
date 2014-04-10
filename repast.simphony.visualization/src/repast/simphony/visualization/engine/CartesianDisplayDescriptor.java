package repast.simphony.visualization.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Display descriptor for Cartesian (2D,3D) displays with value layers.
 * 
 * TODO Projections: also implement network descriptor?
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */

public class CartesianDisplayDescriptor extends BasicDisplayDescriptor implements ValueLayerDescriptor {

	private String valueLayerStyleName;

  private String valueLayerEditedStyleName;

  public List<String> valueLayers = new ArrayList<String>();

  public CartesianDisplayDescriptor(CartesianDisplayDescriptor descriptor) {
    super(descriptor.getName());
    set(descriptor);
  }

  public CartesianDisplayDescriptor(String name) {
    super(name);
  }

  @Override
  public void clearValueLayerNames() {
    if (valueLayers.size() > 0) {
      this.valueLayers.clear();
      scs.fireScenarioChanged(this, "valueLayers");
    }
  }

  @Override
  public int getValueLayerCount() {
    return valueLayers.size();
  }

  @Override
  protected void set(DisplayDescriptor descriptor) {
    super.set(descriptor);

    CartesianDisplayDescriptor cdd = (CartesianDisplayDescriptor)descriptor;
    
    for (String vlName : cdd.getValueLayerNames()) {
      addValueLayerName(vlName);
    }

    setValueLayerStyleName(cdd.getValueLayerStyleName());

    if (cdd.getValueLayerEditedStyleName() != null)
      setValueLayerEditedStyleName(cdd.getValueLayerEditedStyleName());
  }

  /**
   * Adds the named value layer to the list of value layers to display.
   * 
   * @param name
   *          the name of the value layer to display.
   */
  @Override
  public void addValueLayerName(String name) {
    if (!valueLayers.contains(name)) {
      valueLayers.add(name);
      scs.fireScenarioChanged(this, "valueLayer");
    }
  }

  /**
   * Gets a List of all the names of the value layers to display.
   * 
   * @return a List of all the names of the value layers to display.
   */
  @Override
  public Iterable<String> getValueLayerNames() {
    return valueLayers;
  }

  /**
   * Sets the value layer style name.
   * 
   * @param name
   */
  @Override
  public void setValueLayerStyleName(String name) {
    valueLayerStyleName = name;
    scs.fireScenarioChanged(this, "valueLayerStyle");
  }

  /**
   * Gets the name of the value layer style. Will return null if no value layer
   * style has been selected.
   * 
   * @return the name of the value layer style.
   */
  @Override
  public String getValueLayerStyleName() {
    return valueLayerStyleName;
  }

  /**
   * Gets the name of the value layer edited style. Will return null if no value
   * layer style has been selected.
   * 
   * @return the name of the value layer edited style.
   */
  @Override
  public String getValueLayerEditedStyleName() {
    return valueLayerEditedStyleName;
  }

  /**
   * Sets the value layer edited style name.
   * 
   * @param name
   */
  @Override
  public void setValueLayerEditedStyleName(String name) {
    valueLayerEditedStyleName = name;
    scs.fireScenarioChanged(this, "valueLayerEditedStyle");
  }

	@Override
	public DisplayDescriptor makeCopy() {
		return new CartesianDisplayDescriptor(this);
	}
}