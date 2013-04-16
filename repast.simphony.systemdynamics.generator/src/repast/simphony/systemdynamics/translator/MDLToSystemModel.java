/**
 * 
 */
package repast.simphony.systemdynamics.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;

import repast.simphony.systemdynamics.sdmodel.InfluenceLink;
import repast.simphony.systemdynamics.sdmodel.Rate;
import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.Stock;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;

/**
 * Creates a SystemModel from an mdl file.
 * 
 * @author Nick Collier
 */
public class MDLToSystemModel {

  private final static Set<String> MODEL_VARS = new HashSet<String>();
  static {
    MODEL_VARS.add(TranslatorConstants.FINAL_TIME);
    MODEL_VARS.add(TranslatorConstants.INITIAL_TIME);
    MODEL_VARS.add(TranslatorConstants.SAVEPER);
    MODEL_VARS.add(TranslatorConstants.TIME_STEP);
  }

  public MDLToSystemModel() {
  }

  public SystemModel run(SystemModel model, Diagram diagram, String mdlFile) {
    Reader reader = new Reader(mdlFile);
    List<String> mdlContents = reader.readMDLFile();
    InformationManagers.getInstance().getFunctionManager()
        .load(getClass().getResourceAsStream("/implementedFunctions.csv"));
    SystemDynamicsObjectManager sdObjectManager = InformationManagers.getInstance()
        .getSystemDynamicsObjectManager();

    // process graphics first since we get screen name information from this
    // portion of the file
    new GraphicsProcessor().processGraphics(sdObjectManager, mdlContents);
    EquationProcessor eqProcessor = new EquationProcessor();
    Map<String, Equation> equations = eqProcessor.processRawEquations(sdObjectManager,
        mdlContents);
    initModel(model, equations);
    
    Map<String, Variable> varMap = new HashMap<String, Variable>();
    List<Rate> rates = new ArrayList<Rate>();
    for (String name : sdObjectManager.screenNames()) {
      if (!MODEL_VARS.contains(name)) {
        List<Equation> eqs = sdObjectManager.getEquations(name);
        Variable var = processEquations(name, eqs, model);
        if (var != null) {
          if (var.getType().equals(VariableType.RATE)) {
            rates.add((Rate)var);
          }
          varMap.put(name, var);
        }
      }
    }

    // create the links
    createLinks(varMap, model, sdObjectManager);
    processRates(rates, varMap, sdObjectManager);
    
    return model;
  }

  private void createLinks(Map<String, Variable> varMap, SystemModel model,
      SystemDynamicsObjectManager objMan) {

    for (String name : varMap.keySet()) {
      List<Arrow> sources = objMan.getIncomingArrows(name);
      Variable target = varMap.get(name);

      for (Arrow source : sources) {
        if (source.getType().equals(Arrow.INFLUENCE)) {
          Variable vSource = varMap.get(source.getOtherEnd());
          InfluenceLink link = SDModelFactory.eINSTANCE.createInfluenceLink();
          link.setFrom(vSource);
          link.setTo(target);
          link.setUuid(EcoreUtil.generateUUID());
          
          model.getLinks().add(link);
        } 
      }
    }
  }

  private void processRates(List<Rate> rates, Map<String, Variable> varMap, SystemDynamicsObjectManager objMan) {
    for (Rate rate : rates) {
      Stock from = null;
      Stock to = null;
      
      for (Arrow arrow : objMan.getIncomingArrows(rate.getName())) {
        if (arrow.getType().equals(Arrow.FLOW)) {
          from = (Stock)varMap.get(arrow.getOtherEnd());
          break;
        }
      }
      
      for (Arrow arrow : objMan.getOutgoingArrows(rate.getName())) {
        if (arrow.getType().equals(Arrow.FLOW)) {
          to = (Stock)varMap.get(arrow.getOtherEnd());
          break;
        }
      }
      rate.setFrom(from);
      rate.setTo(to);
    }
  }

  private Variable processEquations(String name, List<Equation> eqs, SystemModel model) {
    Variable var = null;
    System.out.println("Variable Name: " + name);
    if (eqs.size() > 0) {
      Equation eq = eqs.get(0);
      VariableType type = eq.getVariableType();
      if (type == VariableType.RATE) {
        var = SDModelFactory.eINSTANCE.createRate();
        var.setType(VariableType.RATE);
      } else if (type == VariableType.AUXILIARY) {
        var = SDModelFactory.eINSTANCE.createVariable();
        var.setType(VariableType.AUXILIARY);
      } else if (type == VariableType.CONSTANT) {
        var = SDModelFactory.eINSTANCE.createVariable();
        var.setType(VariableType.CONSTANT);
      } else if (type == VariableType.STOCK) {
        var = SDModelFactory.eINSTANCE.createStock();
        var.setType(VariableType.STOCK);
      } else if (type == VariableType.LOOKUP) {
        var = SDModelFactory.eINSTANCE.createVariable();
        var.setType(VariableType.LOOKUP);
      }

      if (var != null) {
        var.setName(name);
        String comment = eq.getComment() == null ? "" : eq.getComment();
        var.setComment(comment);
        parseEquation(var, eq);
        var.setUnits(eq.getUnits());
        var.setUuid(EcoreUtil.generateUUID());
        model.getVariables().add(var);
      }

    } else if (name.startsWith("CLOUD")) {
      var = SDModelFactory.eINSTANCE.createCloud();
      var.setName(name);
      var.setUuid(EcoreUtil.generateUUID());
      var.setType(VariableType.STOCK);
      model.getVariables().add(var);
    }

    return var;
  }

  private void parseEquation(Variable var, Equation eq) {
    String equation = eq.getEquation().trim();
    String[] sides = equation.split("=");
    if (sides.length == 2) {
      String lhs = sides[0].trim();
      String rhs = sides[1].trim();
      if (var.getType() == VariableType.STOCK && rhs.startsWith("INTEG")) {
        int index = rhs.indexOf("(");
        if (index != -1) {
          rhs = rhs.substring(index + 1);
          if (rhs.endsWith(")"))
            rhs = rhs.substring(0, rhs.length() - 1);
        }

        var.setEquation(rhs.trim());
      } else {
        var.setEquation(rhs.trim());
      }
      var.setLhs(lhs.trim());
    } else {
      var.setEquation(equation.trim());
    }
  }

  private String getRHS(String equation) {
    String[] vals = equation.split("=");
    if (vals.length == 2)
      return vals[1].trim();
    return "";
  }

  private double getValue(String name, Map<String, Equation> equations) {
    Equation eq = equations.get(name);
    if (eq == null)
      throw new IllegalArgumentException("Equation '" + name + "' does not exist");
    String val = getRHS(eq.getEquation());
    try {
      return Double.parseDouble(val);
    } catch (NumberFormatException ex) {
    }
    // assume val is a variable name
    return getValue(val, equations);
  }

  private void initModel(SystemModel model, Map<String, Equation> equations) {
    double val = getValue(TranslatorConstants.FINAL_TIME, equations);
    model.setEndTime(val);

    val = getValue(TranslatorConstants.INITIAL_TIME, equations);
    model.setStartTime(val);

    val = getValue(TranslatorConstants.SAVEPER, equations);
    model.setReportingInterval(val);

    val = getValue(TranslatorConstants.TIME_STEP, equations);
    model.setTimeStep(val);

    model.setUnits(equations.get(TranslatorConstants.TIME_STEP).getUnits().trim());
  }
}
