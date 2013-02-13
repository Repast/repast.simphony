/**
 * 
 */
package repast.simphony.systemdynamics.subscripts;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds the variable blocks in an equation string.
 * 
 * @author Nick Collier
 */
public class VariableFinder {

  private String equation;

  public VariableFinder(String equation) {
    this.equation = equation;
  }

  public List<VariableBlock> findBlock(String variableName) {
    List<VariableBlock> list = new ArrayList<VariableBlock>();
    int index = equation.indexOf(variableName);
    while (index != -1) {
      VariableBlock block = createBlock(index, variableName);
      list.add(block);
      index = equation.indexOf(variableName, block.getBlockEnd());
    }
    
    return list;
  }

  private VariableBlock createBlock(int start, String name) {
    int subEnd = -1;
    int index = start + name.length();
    VariableBlock block = new VariableBlock(name, start);
    // swallow whitespace
    while (index < equation.length()) {
      if (!Character.isWhitespace(equation.charAt(index)))
        break;
      ++index;
    }

    if (index < equation.length() && equation.charAt(index) == '[') {
      ++index;
      StringBuilder buf = new StringBuilder();
      while (index < equation.length()) {
        char c = equation.charAt(index);
        if (c == ']') {
          subEnd = index - 1;
          break;
        }
        buf.append(c);
        ++index;
      }

      if (subEnd != -1) {
        
        String[] subs = buf.toString().split(",");
        block.setSubEnd(subEnd);
        for (String sub : subs) {
          block.addSubscript(sub.trim());
        }
      }
    }
    
    return block;
  }
}
