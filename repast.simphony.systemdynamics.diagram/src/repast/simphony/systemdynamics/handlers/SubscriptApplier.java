/**
 * 
 */
package repast.simphony.systemdynamics.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;

import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;
import repast.simphony.systemdynamics.subscripts.Equation;
import repast.simphony.systemdynamics.subscripts.EquationCreator;
import repast.simphony.systemdynamics.subscripts.VariableBlock;

/**
 * Applies subscripts to variables equations.
 * 
 * @author Nick Collier
 */
public class SubscriptApplier {

  private String subscriptText;
  private List<Subscript> subscripts = new ArrayList<Subscript>();
  private int subRangeCount = 1;
  private List<Variable> variables;

  public SubscriptApplier(List<Subscript> subscripts, List<Variable> variables) {
    this.variables = new ArrayList<Variable>(variables);
    boolean notFirst = false;
    StringBuilder buf = new StringBuilder();
    for (Subscript sub : subscripts) {
      if (notFirst)
        buf.append(", ");
      buf.append(sub.getName());
      notFirst = true;

      subRangeCount = subRangeCount * sub.getElements().size();
    }
    subscriptText = buf.toString();
    this.subscripts.addAll(subscripts);
  }

  public void run() {

    for (Variable var : variables) {
      String eq = "";
      if (var.getType().equals(VariableType.CONSTANT)) {
        eq = applyToConstant(var);
      }

      if (eq.length() > 0) {
        TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(var);
        
          Command cmd = domain.createCommand(SetCommand.class, new CommandParameter(var,
              SDModelPackage.Literals.VARIABLE__EQUATION, eq));
          domain.getCommandStack().execute(cmd);
        
      }
    }
  }

  private String getConstantInitText() {
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < subRangeCount; ++i) {
      if (i > 0)
        buf.append(", ");
      buf.append("0");
    }
    return buf.toString();
  }

  private String applyToConstant(Variable var) {
    String eq = var.getEquation().trim();
    String name = var.getName();

    if (eq.length() == 0) {
      eq = name + "[" + subscriptText + "]" + " = " + getConstantInitText();
    } else {
      EquationCreator creator = new EquationCreator(eq);
      Equation equation = creator.createEquation(name);
      List<VariableBlock> blocks = equation.getBlocks(name);
      if (blocks.isEmpty()) {
        // equation not empty but variable not referenced
        eq = name + "[" + subscriptText + "]" + " = " + eq;
      } else {
        for (VariableBlock block : blocks) {
          block.addSubscripts(subscripts);
        }
        eq = equation.getText();
      }
    }

    return eq;
  }

}
