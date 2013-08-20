/**
 * 
 */
package repast.simphony.systemdynamics.handlers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;

/**
 * @author Nick Collier
 */
public class ApplySubscriptsTest {
  
  private List<Subscript> createSubscripts(String... names) {
    List<Subscript> subs = new ArrayList<Subscript>();
    for (String name : names) {
      Subscript sub = SDModelFactory.eINSTANCE.createSubscript();
      sub.setName(name);
      subs.add(sub);
      sub.getElements().add("1");
      sub.getElements().add("2");
    }
    return subs;
  }
  
  private List<Variable> createVariables(VariableType type, String... names) {
    List<Variable> vars = new ArrayList<Variable>();
    for (String name : names) {
      Variable var = SDModelFactory.eINSTANCE.createVariable();
      var.setName(name);
      var.setType(type);
      var.setEquation("");
      vars.add(var);
    }
    return vars;
  }
  
  private void updateEQs(List<Variable> vars, String eq) {
    for (Variable var : vars) {
      var.setEquation(eq);
    }
  }
  
  @Test
  public void testAux() {
    List<Variable> vars = createVariables(VariableType.AUXILIARY, "population");
    SystemModel model = SDModelFactory.eINSTANCE.createSystemModel();
    model.getVariables().addAll(vars);
    model.getVariables().addAll(createVariables(VariableType.CONSTANT, "birth rate", "death rate"));
    
    SubscriptApplier applier = new SubscriptApplier(createSubscripts("age", "species"), vars);
    applier.run();
    
    Variable var = vars.get(0);
    String expected = var.getName() + "[age, species] =";
    assertEquals(expected, var.getEquation());
    
    var.setEquation("birth rate - death rate");
    applier.run();
    
    expected = var.getName() + "[age, species] = birth rate[age, species] - death rate[age, species]";
    assertEquals(expected, var.getEquation());
    
    var.setEquation(var.getName() + "[foo] = birth rate[foo] - death rate[foo]");
    applier.run();
    expected = var.getName() + "[foo, age, species] = birth rate[foo, age, species] - death rate[foo, age, species]";
    assertEquals(expected, var.getEquation());
  }
  
  @Test
  public void testStock() {
    List<Variable> vars = createVariables(VariableType.STOCK, "population");
    SystemModel model = SDModelFactory.eINSTANCE.createSystemModel();
    model.getVariables().addAll(vars);
    model.getVariables().addAll(createVariables(VariableType.CONSTANT, "birth rate", "death rate"));
    
    SubscriptApplier applier = new SubscriptApplier(createSubscripts("age", "species"), vars);
    applier.run();
    
    Variable var = vars.get(0);
    String expected = var.getName() + "[age, species] = INTEG()";
    assertEquals(expected, var.getEquation());
    
    var.setEquation("birth rate - death rate");
    applier.run();
    
    expected = var.getName() + "[age, species] = INTEG(birth rate[age, species] - death rate[age, species])";
    assertEquals(expected, var.getEquation());
  }
  
  @Test
  public void testConstants() {
    List<Variable> vars = createVariables(VariableType.CONSTANT, "birth rate", "death rate");
    SubscriptApplier applier = new SubscriptApplier(createSubscripts("age", "species"), vars);
    applier.run();
    
    for (Variable var : vars) {
      String expected = var.getName() + "[age, species] = 0, 0, 0, 0";
      assertEquals(expected, var.getEquation());
    }
    
    updateEQs(vars, "8");
    applier.run();
    
    for (Variable var : vars) {
      String expected = var.getName() + "[age, species] = 8";
      assertEquals(expected, var.getEquation());
    }
  }
}
