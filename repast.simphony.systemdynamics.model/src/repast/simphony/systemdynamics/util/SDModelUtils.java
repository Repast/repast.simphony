/**
 * 
 */
package repast.simphony.systemdynamics.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import repast.simphony.systemdynamics.sdmodel.InfluenceLink;
import repast.simphony.systemdynamics.sdmodel.Rate;
import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Stock;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;

/**
 * Utility class for navigating around an SDModel.
 * 
 * @author Nick Collier
 */
public class SDModelUtils {
  
  /**
   * Gets all the units defined in the model that constains the specified EObject.
   * 
   * @param var
   * 
   * @return all the units defined in the model that constains the specified EObject.
   */
  public static List<String> getAllUnits(EObject obj) {
    Set<String> units = new HashSet<String>();
    SystemModel model = null;
    if (obj.eClass().equals(SDModelPackage.Literals.SYSTEM_MODEL)) {
      model = (SystemModel)obj;
    } else {
      model = ((SystemModel)obj.eContainer());
    }
    if (model.getUnits() != null && model.getUnits().trim().length() > 0) units.add(model.getUnits().trim());
    for (Variable var : model.getVariables()) {
      String unit = var.getUnits();
      if (unit != null & unit.trim().length() > 0)
        units.add(unit.trim());
    }
    
    return new ArrayList<String>(units);
  }
  
  /**
   * Gets a list of the variables whose outgoing links have the specified
   * variable as a target.
   * 
   * @param var
   * 
   * @return  a list of the variables whose outgoing links have the specified
   * variable as a target.
   */
  public static List<Variable> getIncomingVariables(Variable var) {
    List<Variable> vars = new ArrayList<Variable>();
    SystemModel model = (SystemModel)var.eContainer();
    for (InfluenceLink link : model.getLinks()) {
      if (link.getTo().equals(var)) vars.add(link.getFrom());
    }
    
    return vars;
  }
  
  /**
   * Gets all the incoming Rates for the specified Stock
   * 
   * @param stock
   * @return all the incoming Rates for the specified Stock
   */
  public static List<Rate> getIncomingRates(Stock stock) {
    List<Rate> rates = new ArrayList<Rate>();
    SystemModel model = (SystemModel)stock.eContainer();
    for (Variable var : model.getVariables()) {
      if (var.getType() == VariableType.RATE) {
        Rate rate = (Rate)var;
        if (rate.getTo().equals(stock)) rates.add(rate);
      }
    }
    
    return rates;
  }
  
  /**
   * Gets all the outgoing Rates for the specified Stock
   * 
   * @param stock
   * @return all the outgoing Rates for the specified Stock
   */
  public static List<Rate> getOutgoingRates(Stock stock) {
    List<Rate> rates = new ArrayList<Rate>();
    SystemModel model = (SystemModel)stock.eContainer();
    for (Variable var : model.getVariables()) {
      if (var.getType() == VariableType.RATE) {
        Rate rate = (Rate)var;
        if (rate.getFrom().equals(stock)) rates.add(rate);
      }
    }
    
    return rates;
  }
  
  /**
   * Gets all the model variable names in the SystemModel that is
   * the parent of the specified Variable.
   * 
   * @param var
   * 
   * @return all the model variable names in the SystemModel that is
   * the parent of the specified Variable.
   */
  public static  List<String> getVarNames(Variable var) {
    List<String> varNames = new ArrayList<String>();
    SystemModel model = (SystemModel) var.eContainer();
    for (Variable v : model.getVariables()) {
      if (!(v.getType().equals(VariableType.LOOKUP) || v.eClass().equals(SDModelFactory.eINSTANCE.getSDModelPackage().getCloud())))
      varNames.add(v.getName());
    }
    return varNames;
  }

}
