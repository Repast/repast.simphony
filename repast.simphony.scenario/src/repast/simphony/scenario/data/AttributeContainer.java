/**
 * 
 */
package repast.simphony.scenario.data;

import java.util.ArrayList;
import java.util.List;


/**
 * Abstract base class for meta data that
 * contains attributes and an id.
 * 
 * @author Nick Collier
 */
public abstract class AttributeContainer {
  
  protected String id;
  protected List<Attribute> attributes = new ArrayList<Attribute>();
  
  public AttributeContainer(String id) {
    this.id = id;
  }
  
  /**
   * Gets the id of this attribute container.
   * 
   * @return the id of this attribute container.
   */
  public String getId() {
    return id;
  }
  
  /**
   * Adds an attribute to this AttributeContainer. 
   * 
   * @param attribute the attribute to add
   */
  public void addAttribute(Attribute attribute) {
    attributes.add(attribute);
  }
  
  /**
   * Gets an iterable over the attributes this contains.
   * 
   * @return an iterable over the attributes this contains.
   */
  public Iterable<Attribute> attributes() {
    return attributes;
  }
  
  /**
   * Gets the number of attributes contained by this container.
   * 
   * @return the number of attributes contained by this container.
   */
  public int getAttributeCount() {
    return attributes.size();
  }

}
