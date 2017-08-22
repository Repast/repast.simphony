package repast.simphony.scenario;

import repast.simphony.engine.schedule.Descriptor;

/**
 * Dummy object used to test the converter.
 * 
 * @author Nick Collier
 */
public class PersonDescriptor implements Descriptor {

  private String name;

  public String getName() {
          return name;
  }

  public void setName(String name) {
          this.name = name;
  }
}