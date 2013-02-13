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
import repast.simphony.systemdynamics.sdmodel.SystemModel;
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
  private List<String> varNames = new ArrayList<String>();

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

  /**
   * Runs the subscript applier on the list of variables. Updating those
   * variables' equations if necessary.
   */
  public void run() {
    varNames.clear();
    for (Variable var : variables) {
      String eq = "";
      if (var.getType().equals(VariableType.CONSTANT)) {
        eq = applyToConstant(var);

      } else if (var.getType().equals(VariableType.STOCK)) {
        eq = applyToStock(var);
      } else if (!var.getType().equals(VariableType.LOOKUP)) {
        eq = applyToAuxRate(var);
      }

      if (eq.length() > 0) {
        TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(var);
        if (domain == null) {
          // for testing when there is no domain, just apply directory
          var.setEquation(eq);
        } else {
          Command cmd = domain.createCommand(SetCommand.class, new CommandParameter(var,
              SDModelPackage.Literals.VARIABLE__EQUATION, eq));
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

    SystemModel model = (SystemModel) var.eContainer();
    for (Variable v : model.getVariables()) {
      varNames.add(v.getName());
    }
    return varNames;
  }
  
  private String applyToAuxRate(Variable var) {
    String eq = var.getEquation().trim();
    String name = var.getName();
    
    if (eq.length() == 0) {
      eq = name + "[" + subscriptText + "] =";
    } else {
      
      EquationCreator creator = new EquationCreator(eq);
      List<String> names = getVarNames(var);
      Equation equation = creator.createEquation(names);
      
      for (String vName : names) {
        List<VariableBlock> blocks = equation.getBlocks(vName);
        for (VariableBlock block : blocks) {
          block.addSubscripts(subscripts);
        }
      }
      
      List<VariableBlock> vBlock = equation.getBlocks(name);
      if (vBlock.isEmpty()) {
        // assume equation is just variables without = so add the = to it.
        eq = name + "[" + subscriptText + "] = " + equation.getText();
      } else {
        // just apply the subscripts to the existing equation.
        eq = equation.getText();
      }
    }
    
    return eq;
  }

  private String applyToStock(Variable var) {
    String eq = var.getEquation().trim();
    String name = var.getName();

    if (eq.length() == 0) {
      eq = name + "[" + subscriptText + "] = INTEG()";
    } else {
      EquationCreator creator = new EquationCreator(eq);
      List<String> names = getVarNames(var);
      Equation equation = creator.createEquation(names);
     
      
      for (String vName : names) {
        List<VariableBlock> blocks = equation.getBlocks(vName);
        for (VariableBlock block : blocks) {
          block.addSubscripts(subscripts);
        }
      }
      
      List<VariableBlock> vBlock = equation.getBlocks(name);
      if (vBlock.isEmpty()) {
        // assume equation is just variables so add the equals to it.
        eq = name + "[" + subscriptText + "] = INTEG(" + equation.getText() + ")";
      } else {
        // just apply the subscripts to the existing equation.
        eq = equation.getText();
      }
    }

    return eq;
  }

  private String applyToConstant(Variable var) {
    String eq = var.getEquation().trim();
    String name = var.getName();

    if (eq.length() == 0) {
      eq = name + "[" + subscriptText + "] = " + getConstantInitText();
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
