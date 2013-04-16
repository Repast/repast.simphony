package repast.simphony.systemdynamics.translator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.junit.Before;
import org.junit.Test;

import repast.simphony.systemdynamics.sdmodel.InfluenceLink;
import repast.simphony.systemdynamics.sdmodel.Rate;
import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;

public class MDLToSystemModelTests {
  
  private static final String EPIDEMIC_MDL = "./test_data/EPIDEMIC.MDL";
  private static final String WFINV_MDL = "./test_data/WFINV.MDL";
  private static final String ARMS_MDL = "./test_data/arms4.mdl";
  
  private SystemModel model;
  private Diagram diagram;
  
  private static final String[][] EPI_EXPECTED = {
    // name, type, equation, lhs, units, comment, subscript
    {"rate that people contact other people", "constant", "5", "rate that people contact other people", "1/day", "", ""},
    {"Contacts between infected and unaffected", "auxiliary", "rate of potential infectious contacts  * Fraction of population infected", 
      "Contacts between infected and unaffected", "Person/day", "", ""},
      {"total population",  "auxiliary", "Healthy + Infected", "total population", "Person", "", ""},
    {"Infected", "stock", "getting sick, initial infected", "Infected", "Person", "", ""},
    {"fraction infected from contact", "constant", "0.1", "fraction infected from contact", "Dmnl", "", ""},
    {"Fraction of population infected", "auxiliary", "Infected / total population", "Fraction of population infected", "Dmnl", "", ""},
    {"getting sick", "rate", "Contacts between infected and unaffected  * fraction infected from contact", "getting sick", "Person/day", "", ""},
    {"initial infected", "constant", "10", "initial infected", "Person", "", ""},
    {"initial susceptible", "constant", "1000000.0", "initial susceptible", "Person", "", ""},
    {"rate of potential infectious contacts", "auxiliary", "Healthy  * rate that people contact other people", "rate of potential infectious contacts", "Person/day", "", ""},
    {"Healthy", "stock", "- getting sick, initial susceptible", "Healthy", "Person", "", ""},
  };
  
  private static final String[][] EPI_LINKS = {
    {"initial susceptible", "Healthy"},
    {"Healthy", "total population"},
    {"Healthy", "rate of potential infectious contacts"},
    {"rate that people contact other people", "rate of potential infectious contacts"},
    {"rate of potential infectious contacts", "Contacts between infected and unaffected"},
    {"Contacts between infected and unaffected", "getting sick"},
    {"fraction infected from contact", "getting sick"},
    {"total population", "Fraction of population infected"},
    {"Fraction of population infected", "Contacts between infected and unaffected"},
    {"Infected", "Fraction of population infected"},
    {"Infected", "total population"},
    {"initial infected", "Infected"},
  };
  
  @Before
  public void setup() {
    model = SDModelFactory.eINSTANCE.createSystemModel();
    diagram = NotationFactory.eINSTANCE.createDiagram();
    if (diagram != null) {
      diagram.setName("test.rsd");
      diagram.setElement(model);
    }
    
  }
  
  @Test
  public void simpleTest() {
    MDLToSystemModel trans = new MDLToSystemModel();
    trans.run(model, diagram, EPIDEMIC_MDL);
    assertEquals(50, model.getEndTime(), 0);
    assertEquals(0, model.getStartTime(), 0);
    assertEquals(1, model.getReportingInterval(), 0);
    assertEquals(1, model.getTimeStep(), 0);
    assertEquals("day", model.getUnits());
    
    Map<String, Variable> varMap = new HashMap<String, Variable>();
    for (Variable var : model.getVariables()) {
      System.out.println(var.getName());
      varMap.put(var.getName(), var);
    }
    
    assertEquals(EPI_EXPECTED.length, varMap.size());
    for (String[] exp : EPI_EXPECTED) {
      testVar(exp, varMap);
    }
    
    List<InfluenceLink> links = new ArrayList<InfluenceLink>(model.getLinks());
    for (String[] link : EPI_LINKS) {
      assertTrue(link[0] + " -> " + link[1], findLink(link[0], link[1], links));
    }
    assertEquals(0, links.size());
    
    // check the rate to and from
    Variable var = varMap.get("getting sick");
    assertNotNull(var);
    assertEquals("Healthy", ((Rate)var).getFrom().getName());
    assertEquals("Infected", ((Rate)var).getTo().getName());
  }
  
  private boolean findLink(String source, String target, List<InfluenceLink> links) {
    for (InfluenceLink link : links) {
      if (link.getFrom().getName().equals(source) && link.getTo().getName().equals(target)) {
        links.remove(link);
        return true;
      }
    }
    return false;
  }
  
  private VariableType convertType(String type) {
    if (type.equals("constant")) return VariableType.CONSTANT;
    if (type.equals("stock")) return VariableType.STOCK;
    if (type.equals("auxiliary")) return VariableType.AUXILIARY;
    if (type.equals("rate")) return VariableType.RATE;
    if (type.equals("lookup")) return VariableType.LOOKUP;
    return null;
    
  }

  private void testVar(String[] exp, Map<String, Variable> varMap) {
    Variable var = varMap.get(exp[0]);
    assertNotNull(exp[0], var);
    // name, type, equation, lhs, units, comment, subscript
    assertEquals(exp[0], var.getName());
    assertEquals(convertType(exp[1]), var.getType());
    assertEquals(var.getName(), exp[2], var.getEquation());
    assertEquals(exp[3], var.getLhs());
    assertEquals(exp[4], var.getUnits());
    assertEquals(exp[5], var.getComment());
    testSubscripts(exp[6], var.getSubscripts());
  }
  
  private void testSubscripts(String subString, List<String> subscripts) {
    String[] subs = subString.split(",");
    if (subs.length == 1 && subs[0].equals("")) subs = new String[0];
    assertEquals(subs.length, subscripts.size());
    for (String sub : subs) {
      assertTrue(subscripts.remove(sub.trim()));
    }
    assertEquals(0, subscripts.size());
  }
  
  private Variable findVar(String name) {
    for (Variable var : model.getVariables()) {
      if (var.getName().equals(name)) return var;
    }
    return null;
  }
  
  @Test
  public void testCloud() {
    MDLToSystemModel trans = new MDLToSystemModel();
    trans.run(model, diagram, WFINV_MDL);
    Rate production = (Rate)findVar("production");
    assertNotNull(production);
    assertNotNull(production.getFrom());
    assertEquals(SDModelPackage.Literals.CLOUD, production.getFrom().eClass());
  }
  
  @Test
  public void testSubscripts() {
    MDLToSystemModel trans = new MDLToSystemModel();
    trans.run(model, diagram, ARMS_MDL);
    List<Subscript> subscripts = model.getSubscripts();
    assertEquals(1, subscripts.size());
    Subscript sub = subscripts.get(0);
    assertEquals("region", sub.getName());
    Set<String> expected = new HashSet<String>();
    expected.add("east");
    expected.add("west");
    
    for (String val : sub.getElements()) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());
    
    
  }

}
