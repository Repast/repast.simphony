package repast.simphony.systemdynamics;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.subscripts.Equation;
import repast.simphony.systemdynamics.subscripts.EquationCreator;
import repast.simphony.systemdynamics.subscripts.VariableBlock;
import repast.simphony.systemdynamics.subscripts.VariableFinder;

public class SubscriptTest {
  
  public static String EQ1 = "births[species] = 10, 20, 30";
  public static String EQ2 = "births[species, age] = 10, 20, 30";
  public static String EQ3 = "births[species = 10, 20, 30";
  public static String EQ4 = "births [species, age ] = 10, 20, 30";
  
  public static String EQ5 = "population[species] = INTEG(births[species] - deaths[species])";
  public static String EQ6 = "population = INTEG(births - deaths) / deaths";
  
  @Test
  public void testSubscriptParsing() {
    Variable var = SDModelFactory.eINSTANCE.createVariable();
    var.setName("births");
    assertEquals(0, var.getSubscripts().size());
    
    var.setEquation(EQ1);
    assertEquals(1, var.getSubscripts().size());
    assertEquals("species", var.getSubscripts().get(0));
    
    var.setEquation(EQ2);
    assertEquals(2, var.getSubscripts().size());
    assertEquals("species", var.getSubscripts().get(0));
    assertEquals("age", var.getSubscripts().get(1));
    
    var.setEquation(EQ3);
    assertEquals(0, var.getSubscripts().size());
    
    var.setEquation(EQ4);
    assertEquals(2, var.getSubscripts().size());
    assertEquals("species", var.getSubscripts().get(0));
    assertEquals("age", var.getSubscripts().get(1));
    
    var.setEquation("");
    assertEquals(0, var.getSubscripts().size());
    
    var.setName("foo");
    var.setEquation(EQ4);
    assertEquals(0, var.getSubscripts().size());
  }
  
  @Test
  public void testVariableFinder() {
    VariableFinder finder = new VariableFinder(EQ4);
    List<VariableBlock> blocks = finder.findBlock("births");
    assertEquals(1, blocks.size());
    VariableBlock block = blocks.get(0);
    List<String> subs = block.getSubscripts();
    assertEquals(2, subs.size());
    assertEquals("species", subs.get(0));
    assertEquals("age", subs.get(1));
    
    assertEquals(0, block.getBlockStart());
    assertEquals(21, block.getBlockEnd());
  }
  
  private Subscript createSubscript(String name) {
    Subscript sub = SDModelFactory.eINSTANCE.createSubscript();
    sub.setName(name);
    return sub;
  }
  
  @Test
  public void testEquation() {
    String[] vars = {"population", "births", "deaths"};
    EquationCreator creator = new EquationCreator(EQ5);
    Equation eq = creator.createEquation(Arrays.asList(vars));
    assertEquals(EQ5, eq.getText());
    // test adding a subscript for the vars.
    
    Subscript[] subs = {createSubscript("age")};
    
    for (String var : vars) {
      List<VariableBlock> blocks = eq.getBlocks(var);
      assertEquals(1, blocks.size());
      blocks.get(0).addSubscripts(Arrays.asList(subs));
    }
    
    String expected = "population[species, age] = INTEG(births[species, age] - deaths[species, age])";
    assertEquals(expected, eq.getText());
  }
  
  @Test
  public void testEquation2() {
    String[] vars = {"population", "births", "deaths"};
    EquationCreator creator = new EquationCreator(EQ6);
    Equation eq = creator.createEquation(Arrays.asList(vars));
    assertEquals(EQ6, eq.getText());
    // test adding a subscript for the vars.
    
    Subscript[] subs = {createSubscript("age"), createSubscript("species")};
    
    for (String var : vars) {
      List<VariableBlock> blocks = eq.getBlocks(var);
      int expected = var.equals("deaths") ? 2 : 1; 
      assertEquals(expected, blocks.size());
      for (VariableBlock block : blocks) 
        block.addSubscripts(Arrays.asList(subs));
    }
    
    String expected = "population[age, species] = INTEG(births[age, species] - deaths[age, species]) / deaths[age, species]";
    assertEquals(expected, eq.getText());
  }
}
