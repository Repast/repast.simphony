package repast.simphony.systemdynamics;

import static org.junit.Assert.*;

import org.junit.Test;

import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.Variable;

public class SubscriptTest {
  
  public static String EQ1 = "births[species] = 10, 20, 30";
  public static String EQ2 = "births[species, age] = 10, 20, 30";
  public static String EQ3 = "births[species = 10, 20, 30";
  public static String EQ4 = "births [species, age ] = 10, 20, 30";
  
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

}
