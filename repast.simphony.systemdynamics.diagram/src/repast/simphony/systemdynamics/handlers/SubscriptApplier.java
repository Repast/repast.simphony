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
import repast.simphony.systemdynamics.util.SDModelUtils;

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
  private List<String> varNames = new ArrayList<String>();

  public SubscriptApplier(List<Subscript> subscripts, List<Variable> variables) {
	  System.out.println("SubscriptApplier constructor");
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

  /**
   * Runs the subscript applier on the list of variables. Updating those
   * variables' equations if necessary.
   */
  public void run() {
	  System.out.println("SubscriptApplier run");
    varNames.clear();
    for (Variable var : variables) {
      String[] vals = {"", ""};
      if (var.getType().equals(VariableType.CONSTANT)) {
        vals = applyToConstant(var);

      } else if (var.getType().equals(VariableType.STOCK)) {
        vals = applyToStock(var);
      } else if (!var.getType().equals(VariableType.LOOKUP)) {
        vals = applyToAuxRate(var);
      }
      
      if (vals[0].length() > 0) {
        TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(var);
        if (domain == null) {
          // for testing when there is no domain, just apply directory
          var.setLhs(vals[0]);
        } else {
          Command cmd = domain.createCommand(SetCommand.class, new CommandParameter(var,
              SDModelPackage.Literals.VARIABLE__LHS, vals[0]));
          domain.getCommandStack().execute(cmd);
        }
      }

      if (vals[1].length() > 0) {
        TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(var);
        if (domain == null) {
          // for testing when there is no domain, just apply directory
          var.setEquation(vals[1]);
        } else {
          Command cmd = domain.createCommand(SetCommand.class, new CommandParameter(var,
              SDModelPackage.Literals.VARIABLE__EQUATION, vals[1]));
          domain.getCommandStack().execute(cmd);
        }

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

  private List<String> getVarNames(Variable var) {
    if (varNames.size() > 0)
      return varNames;

    varNames.addAll(SDModelUtils.getVarNames(var));
    return varNames;
  }

  private String[] applyToAuxRate(Variable var) {
	  System.out.println("SubscriptApplier applyToAuxRate");
    String[] retVal = new String[2];
    String eq = var.getEquation().trim();
    String lhs = var.getLhs();
    String name = var.getName();
    
    if (lhs == null || lhs.length() == 0 || lhs.equals(name)) {
      retVal[0] = name + "[" + subscriptText + "]";
    }

    if (eq.length() > 0) {

      EquationCreator creator = new EquationCreator(eq);
      List<String> names = getVarNames(var);
      Equation equation = creator.createEquation(names);

      for (VariableBlock block : equation.getBlocks()) {
        block.addSubscripts(subscripts);
      }
      eq = equation.getText();
    }
    
    retVal[1] = eq;

    return retVal;
  }

  private String[] applyToStock(Variable var) {
	  System.out.println("SubscriptApplier applyToStock");
    String[] retVal = new String[2];
    String eq = var.getEquation().trim();
    String lhs = var.getLhs();
    String name = var.getName();
    
    if (lhs == null || lhs.length() == 0 || lhs.equals(name)) {
      retVal[0] = name + "[" + subscriptText + "]";
    }

    if (eq.length() > 0) {
      EquationCreator creator = new EquationCreator(eq);
      List<String> names = getVarNames(var);
      Equation equation = creator.createEquation(names);

      for (VariableBlock block : equation.getBlocks()) {
        block.addSubscripts(subscripts);
      }
      eq = equation.getText();
    }
    retVal[1] = eq;

    return retVal;
  }

  private String[] applyToConstant(Variable var) {
	  System.out.println("SubscriptApplier applyToConstant");
    String[] retVal = new String[2];
    String eq = var.getEquation().trim();
    String lhs = var.getLhs();
    String name = var.getName();
    
    if (lhs == null || lhs.length() == 0 || lhs.equals(name)) {
      retVal[0] = name + "[" + subscriptText + "]";
    }

    if (eq.length() == 0) {
      eq = getConstantInitText();
    } else {
      
      EquationCreator creator = new EquationCreator(eq);
      Equation equation = creator.createEquation(name);
      List<VariableBlock> blocks = equation.getBlocks(name);
      if (!blocks.isEmpty()) {
        for (VariableBlock block : blocks) {
          block.addSubscripts(subscripts);
        }
        eq = equation.getText();
      }
    }
    retVal[1] = eq;

    return retVal;
  }

}
