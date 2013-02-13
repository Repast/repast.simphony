/**
 * 
 */
package repast.simphony.systemdynamics.subscripts;

/**
 * A block of text.
 * 
 * @author Nick Collier
 */
public interface TextBlock {
  
  enum Type {VARIABLE, SIMPLE}
  
  /**
   * Gets the text.
   * 
   * @return the text.
   */
  String getText();
  
  /**
   * Gets the type of this TextBlock.
   * 
   * @return the type of this TextBlock;
   */
  Type getType();

}
