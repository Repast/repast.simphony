/**
 * 
 */
package repast.simphony.scenario;

import repast.simphony.engine.schedule.Descriptor;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author Nick Collier
 */
public class AbstractDescriptor implements Descriptor {

  protected transient ScenarioChangedSupport scs = new ScenarioChangedSupport();

  private String name;

  /**
   * Constructs the descriptor with the specified name.
   * 
   * @param name
   *          the name of the descriptor
   */
  public AbstractDescriptor(String name) {
    this.name = name;
  }

  /**
   * Retrieves the name of this descriptor.
   * 
   * @return the name of this descriptor
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this descriptor.
   * 
   * @param name
   *          the name of this descriptor
   */
  public void setName(String name) {
    if (!name.equals(this.name)) {
      this.name = name;
      scs.fireScenarioChanged(this, "name");
    }
  }

  /**
   * Adds the specified object to listen for scenario change events fired by
   * this descriptor.
   * 
   * @param listener
   *          the listener to add
   */
  public void addScenarioChangedListener(ScenarioChangedListener listener) {
    scs.addListener(listener);
  }
  
  // required to set scs on deserialization
  protected Object readResolve() {
    scs = new ScenarioChangedSupport();
    return this;
  }
}
