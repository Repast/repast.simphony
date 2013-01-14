/**
 * 
 */
package repast.simphony.systemdynamics.util;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.systemdynamics.sdmodel.InfluenceLink;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;

/**
 * Utility class for navigating around an SDModel.
 * 
 * @author Nick Collier
 */
public class SDModelUtils {
  
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

}
