/**
 * 
 */
package repast.simphony.systemdynamics.handlers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.junit.Test;

import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;

/**
 * @author Nick Collier
 */
public class ApplySubscriptsTest {
  
  static {
    GMFEditingDomainFactory.getInstance().createEditingDomain();
  }
  
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
  
  private List<Variable> createConstants(String... names) {
    List<Variable> vars = new ArrayList<Variable>();
    for (String name : names) {
      Variable var = SDModelFactory.eINSTANCE.createVariable();
      var.setName(name);
      var.setType(VariableType.CONSTANT);
      var.setEquation("");
      vars.add(var);
    }
    return vars;
  }
  
  @Test
  public void testConstants() {
    List<Variable> vars = createConstants("birth rate", "death rate");
    SubscriptApplier applier = new SubscriptApplier(createSubscripts("age", "species"), vars);
    applier.run();
    
    for (Variable var : vars) {
      String expected = var.getName() + "[age, species] = 0, 0, 0, 0";
      assertEquals(expected, var.getEquation());
    }
  }

}
