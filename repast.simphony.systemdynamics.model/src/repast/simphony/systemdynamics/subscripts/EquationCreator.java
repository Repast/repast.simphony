/**
 * 
 */
package repast.simphony.systemdynamics.subscripts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Divides up an equation into blocks of Variables and everything else. 
 *  
 * 
 * @author Nick Collier
 */
public class EquationCreator {
  
  private String equation;
  
  public EquationCreator(String equation) {
    this.equation = equation;
  }
  
  /**
   * Creates an Equation that contains the specified variables
   * as individual blocks together with all the remaining text.
   * 
   * @param variables 
   * 
   * @return the created equation.
   */
  public Equation createEquation(List<String> variables) {
    VariableFinder finder = new VariableFinder(equation);
    List<VariableBlock> allBlocks = new ArrayList<VariableBlock>();
    for (String var : variables) {
      allBlocks.addAll(finder.findBlock(var));
    }
    
    Collections.sort(allBlocks, new Comparator<VariableBlock>() {
      @Override
      public int compare(VariableBlock b1, VariableBlock b2) {
        return b1.getBlockStart() < b2.getBlockStart() ? -1 : 1;
      }
    });
    
    int lastIndex = 0;
    List<TextBlock> textblocks = new ArrayList<TextBlock>();
    for (VariableBlock block : allBlocks) {
      int start = block.getBlockStart();
      if (start != lastIndex) {
        textblocks.add(new SimpleTextBlock(equation.substring(lastIndex, start)));
      }
      textblocks.add(block);
      lastIndex = block.getBlockEnd() + 1;
    }
    if (lastIndex != equation.length()) {
      textblocks.add(new SimpleTextBlock(equation.substring(lastIndex, equation.length())));
    }
    
    return new Equation(textblocks);
  }
  
  public Equation createEquation(String variable) {
    List<String> list = new ArrayList<String>();
    list.add(variable);
    return createEquation(list);
  }
}
