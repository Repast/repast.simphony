/**
 * 
 */
package repast.simphony.systemdynamics.subscripts;

/**
 * Simple block of text.
 * 
 * @author Nick Collier
 */
public class SimpleTextBlock implements TextBlock {
  
  private String text;
  
  public SimpleTextBlock(String text) {
    this.text = text;
  }

  /* (non-Javadoc)
   * @see repast.simphony.systemdynamics.subscripts.TextBlock#getText()
   */
  @Override
  public String getText() {
    return text;
  }

  /* (non-Javadoc)
   * @see repast.simphony.systemdynamics.subscripts.TextBlock#getType()
   */
  @Override
  public Type getType() {
    return Type.SIMPLE;
  }
}
