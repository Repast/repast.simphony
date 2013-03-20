/**
 * 
 */
package repast.simphony.systemdynamics.subscripts;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.systemdynamics.sdmodel.Subscript;

/**
 * A block of text that represents a variable name plus optional 
 * subscripts.
 * 
 * @author Nick Collier
 */
public class VariableBlock implements TextBlock {
  
  private int start;
  private int subEnd = -1;
  private String name;
  private List<String> subscripts = new ArrayList<String>();
  
  
  public VariableBlock(String name, int start) {
    this.start = start;
    this.name = name;
  }
  
  /**
   * Gets the name of the variable associated with this block.
   * 
   * @return the name of the variable associated with this block.
   */
  public String getName() {
    return name;
  }
  
  /**
   * Gets the current text of this VariableBlock.
   * 
   * @return the current text of this VariableBlock.
   */
  public String getText() {
    if (subscripts.size() == 0) return name;
    StringBuilder buf = new StringBuilder(name);
    buf.append("[");
    boolean first = true;
    for (String sub : subscripts) {
      if (!first) buf.append(", ");
      buf.append(sub);
      first = false;
    }
    buf.append("]");
    return buf.toString();
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.systemdynamics.subscripts.TextBlock#getType()
   */
  @Override
  public Type getType() {
    return Type.VARIABLE;
  }

  /**
   * Gets whether or not the Variable is subscripted.
   * 
   * @return true if the variable has subscripts, otherwise false.
   */
  public boolean isSubscripted() {
    return subEnd != -1;
  }
  
  /**
   * Gets the start index of this block with respect to the equation
   * that it is part of.
   * 
   * @return the start index of this block with respect to the equation
   * that it is part of.
   */
  public int getBlockStart() {
    return start;
  }
  
  /**
   * Gets the end index of this block with respect to the equation
   * that it is part of. If the variable has subscript, this will be the
   * index of the "]". Otherwise, its the index of the last character of
   * the variable name.
   * 
   * @return the end index of this block with respect to the equation
   * that it is part of.
   */
  public int getBlockEnd() {
    if (subEnd != -1) return subEnd + 1;
    return start + name.length() - 1;
  }
  
  void setSubEnd(int subEnd) {
    this.subEnd = subEnd;
  }
  
  /**
   * Adds the named subscript to this VariableBlock.
   * 
   * @param subscript
   */
  public void addSubscript(String subscript) {
    if (!subscripts.contains(subscript)) subscripts.add(subscript);
  }
  
  /**
   * Adds the named subscript to this VariableBlock. The 
   * subscript will be added before the subscript at the
   * specified pos, if there is one.
   * 
   * @param subscript
   * @param pos
   */
  public void addSubscript(String subscript, int pos) {
    if (subEnd == -1) subscripts.add(subscript);
    else {
      int sstart = start + name.length();
      int i;
      for (i = 0; i < subscripts.size(); ++i) {
        if (sstart + subscripts.get(i).length() + 1 > pos) {
          break;
        }
        sstart += subscripts.get(i).length() + 1;
      }
      subscripts.add(i, subscript);
    }
  }
  
  /**
   * Adds the list of subscripts to this VariableBlock.
   * 
   * @param subs
   */
  public void addSubscripts(List<Subscript> subs) {
    for (Subscript sub : subs) {
      if (!subscripts.contains(sub.getName())) {
        subscripts.add(sub.getName());
      }
    }
  }
  
  
  /**
   * Gets the subscripts associated with this VariableBlock's variable 
   * in its equation string.
   * 
   * @return the subscripts associated with this VariableBlock's variable 
   * in its equation string.
   */
  public List<String> getSubscripts() {
    return subscripts;
  }
}
