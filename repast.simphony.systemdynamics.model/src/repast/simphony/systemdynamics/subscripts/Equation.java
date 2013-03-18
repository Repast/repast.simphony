/**
 * 
 */
package repast.simphony.systemdynamics.subscripts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import repast.simphony.systemdynamics.subscripts.TextBlock.Type;

/**
 * Represents a Variable's equation as modifiable object.
 * 
 * @author Nick Collier
 */
public class Equation {
  
  private List<TextBlock> blocks = new ArrayList<TextBlock>();
  
  public Equation(List<TextBlock> blocks) {
    this.blocks.addAll(blocks);
  }
  
  /**
   * Gets the text of the equation including any updates to 
   * the VariableBlocks.
   * 
   * @return the text of the equation including any updates to 
   * the VariableBlocks.
   */
  public String getText() {
    StringBuilder buf = new StringBuilder();
    for (TextBlock block : blocks) {
      buf.append(block.getText());
    }
    return buf.toString();
  }
  
  /**
   * Gets all the VariableBlocks in this equation. Blocks will 
   * be ordered by starting point.
   * 
   * @return all the VariableBlocks in this equation.
   */
  public List<VariableBlock> getBlocks() {
    List<VariableBlock> ret = new ArrayList<VariableBlock>();
    for (TextBlock block : blocks) {
      if (block.getType() == Type.VARIABLE) {
        ret.add((VariableBlock)block);
      }
    }
    
    Collections.sort(ret, new Comparator<VariableBlock>() {
      @Override
      public int compare(VariableBlock b1, VariableBlock b2) {
        return b1.getBlockStart() < b2.getBlockStart() ? -1 : b1.getBlockStart() == b2.getBlockStart() ? 0 : 1;
      }
    });
    return ret;
  }
  
  /**
   * Gets a List of the VariableBlocks for the
   * named variable in this equation.
   * 
   * @param variableName
   * 
   * @return a List of the VariableBlocks for the
   * named variable in this equation.
   */
  public List<VariableBlock> getBlocks(String variableName) {
    List<VariableBlock> ret = new ArrayList<VariableBlock>();
    for (TextBlock block : blocks) {
      if (block.getType() == Type.VARIABLE && ((VariableBlock)block).getName().equals(variableName)) {
        ret.add((VariableBlock)block);
      }
    }
    return ret;
  }

}
