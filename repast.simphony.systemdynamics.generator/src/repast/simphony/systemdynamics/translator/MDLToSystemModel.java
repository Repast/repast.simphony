/**
 * 
 */
package repast.simphony.systemdynamics.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;

import repast.simphony.systemdynamics.sdmodel.Cloud;
import repast.simphony.systemdynamics.sdmodel.InfluenceLink;
import repast.simphony.systemdynamics.sdmodel.Rate;
import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.Stock;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;

/**
 * Creates a SystemModel from an mdl file.
 * 
 * @author Nick Collier
 */
public class MDLToSystemModel {
	
	private boolean fatal = false;
	private boolean warnings = false;
	
	String fatalMessages = "";
	List<String> warningMessages = new ArrayList<String>();
	

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
	    return run(model, diagram, mdlContents);
  }
  
  public SystemModel run(SystemModel model, Diagram diagram, List<String> mdlContents) {
//    Reader reader = new Reader(mdlFile);
//    List<String> mdlContents = reader.readMDLFile();
	  InformationManagers.clear();
    InformationManagers.getInstance().getFunctionManager()
        .load(getClass().getResourceAsStream("/implementedFunctions.csv"));
    InformationManagers.getInstance().setSystemModel(model);
    SystemDynamicsObjectManager sdObjectManager = InformationManagers.getInstance()
        .getSystemDynamicsObjectManager();

    // process graphics first since we get screen name information from this
    // portion of the file
    new GraphicsProcessor().processGraphics(sdObjectManager, mdlContents);
    EquationProcessor eqProcessor = new EquationProcessor();
    Map<String, Equation> equations = eqProcessor.processRawEquations(sdObjectManager,
        mdlContents);
    
    Map<String, Equation> fatalErrors = eqProcessor.getFatalErrors(equations);
	boolean errors = fatalErrors.size() > 0;
	if (errors) {
		fatal = true;
		Iterator<String> iter = fatalErrors.keySet().iterator();
		fatalMessages += "+++ "+"Fatal Errors Detected"+" +++";
		while (iter.hasNext()) {
			String lhs = iter.next();

			Equation eqn = fatalErrors.get(lhs);

			fatalMessages += ("\nEquation:"); // "\n"
			fatalMessages += "\n\t"+eqn.getVensimEquation().split("~")[0];

			for (String msg : eqn.getFatalMessages()) {
				fatalMessages += "\n"+msg;
			}
			
			fatalMessages += "\n----------";
		}
		
	}
	
    initModel(model, equations);
    
//    sdObjectManager.print();
    
    Map<String, Variable> varMap = new HashMap<String, Variable>();
    List<Rate> rates = new ArrayList<Rate>();
    for (String names : sdObjectManager.screenNames()) {
    	String name = SystemDynamicsObjectManager.getScreenName(names);
//    	System.out.println("MDL2RSD screenName: "+name);
    	if (!MODEL_VARS.contains(name)) {
    		List<Equation> eqs = sdObjectManager.getEquations(name);
    		if (eqs.size() > 0) {
    			Equation peek = eqs.get(0);
    			if (peek.isDefinesSubscript()) {

    			} else {
    				Variable var = processEquations(name, eqs, model);
    				if (var != null) {
//    					System.out.println("MDL2RSD var type: "+var.getType().toString());
    					if (var.getType().equals(VariableType.RATE)) {
    						rates.add((Rate)var);
    					}
    					varMap.put(name, var);
    				}
    			}
    		} else {
    			if (name.startsWith("CLOUD")) {
    				Variable var = processEquations(name, eqs, model);
    				if (var != null) {
    					System.out.println("MDL2RSD var type: "+var.getType().toString());
    					if (var.getType().equals(VariableType.RATE)) {
    						rates.add((Rate)var);
    					}
    					varMap.put(name, var);
    					//    				Cloud cloud = SDModelFactory.eINSTANCE.createCloud();
    					//    				cloud.setName(name);
    					//    				System.out.println("Created Cloud "+name);
    					//    				varMap.put(name, cloud);
    				}
    			}
    		}
    	}
    }
    
    
    
    // need to create subscripts
    createSubscripts(model, equations);

    // create the links
    createLinks(varMap, model, sdObjectManager);
    processRates(rates, varMap, sdObjectManager);
    
    return model;
  }

  private void createSubscripts(SystemModel model, Map<String, Equation> equations) {
	  for (Equation eqn : equations.values()) {
		  if (eqn.isDefinesSubscript()) {
//			  System.out.println("%%%%%%%%%%%% subscripts");
//			  eqn.printTokensOneLine();
			  Subscript sub = SDModelFactory.eINSTANCE.createSubscript();
			  sub.setName(eqn.getTokens().get(0));
			  
			  for (int ind = 2; ind < eqn.getTokens().size(); ind += 2) {
				  sub.getElements().add(eqn.getTokens().get(ind));
			  }
			  model.getSubscripts().add(sub);
		  }
	  }
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
				  if (arrow.getOtherEnd().startsWith("CLOUD")) {
//					  System.out.println("CLOUD: "+arrow.getOtherEnd());
//					  System.out.println("Incoming from cloud in varMap = "+varMap.containsKey(arrow.getOtherEnd()));
				  }
				  from = (Stock)varMap.get(arrow.getOtherEnd());

				  break;
			  }
		  }

		  for (Arrow arrow : objMan.getOutgoingArrows(rate.getName())) {
			  if (arrow.getType().equals(Arrow.FLOW)) {
				  if (arrow.getOtherEnd().startsWith("CLOUD")) {
//					  System.out.println("CLOUD: "+arrow.getOtherEnd());
//					  System.out.println("Outgoing to cloud in varMap = "+varMap.containsKey(arrow.getOtherEnd()));
				  }
				  to = (Stock)varMap.get(arrow.getOtherEnd());

				  break;
			  }
		  }

//		  System.out.println("Rate: from,to "+(from == null ? "NULL" : from.getName())+" "+(to == null ? "NULL" : to.getName()));

		  rate.setFrom(from);
		  rate.setTo(to);
	  }
  }

  private Variable processEquations(String name, List<Equation> eqs, SystemModel model) {
    Variable var = null;
//    System.out.println("Variable Name: " + name);
    if (eqs.size() > 0) {
      Equation eq = eqs.get(0);
      VariableType type = eq.getVariableType();
//      System.out.println("Variable Type: "+type.toString());
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
        parseEquation(var, eqs);
        var.setUnits(eq.getUnits());
        var.setUuid(EcoreUtil.generateUUID());
        model.getVariables().add(var);
      }

    } else if (name.startsWith("CLOUD")) {
      var = SDModelFactory.eINSTANCE.createCloud();
      var.setName(name);
      var.setUuid(EcoreUtil.generateUUID());
      model.getVariables().add(var);
    } else {
    	System.out.println("^^^^^^^^^^^^ No Equation, not cloud: "+name);
    }

    return var;
  }

  private void parseEquation(Variable var, List<Equation> eqns) {
	 
	  for (int eqnNum = 0; eqnNum < eqns.size(); eqnNum++) {
		  Equation eqn = eqns.get(eqnNum);
		  String equation = eqn.getEquation().trim();
//		  System.out.println("parseEquation: "+equation);
		  String[] sides = equation.split("=", 2);
		  if (sides.length == 2) {
			  String lhs = sides[0].trim();
			  String rhs = sides[1].trim();
			  if (var.getType() == VariableType.STOCK && rhs.startsWith("INTEG")) {
//				  System.out.println("parseEquation: found STOCK & INTEG");

				  // this must be done via the parse tree rather than on simple expression matching
				  Node root = eqn.getTreeRoot();
				  Node lhsNode = root.getChild();
				  Node rhsNode = lhsNode.getNext();  // this is pointing to "INTEG"
				  Node arg1 = rhsNode.getChild();  // firsth arg -> the varname
				  Node arg5 = arg1.getNext().getNext().getNext().getNext();  // this is the "equation"
//				  eqn.printTree(arg5);
				  Node arg6 = arg5.getNext(); // this is the 
				  Stock svar = (Stock) var;

				  String arg6Expression = arg6.generateExpression();
				  String arg5Expression = arg5.generateExpression();

				  if (eqnNum == 0) {
					  svar.setInitialValue(arg6Expression);
					  var.setEquation(arg5Expression);  // just rhs
				  } else {
					  String multiEqn = "~~|" + lhs.trim() + "=INTEG("+arg5Expression+","+arg6Expression+")";
					  var.setEquation(var.getEquation() + multiEqn);
				  }

//				  System.out.println("$$$$$$$5 "+arg5Expression);
//				  System.out.println("$$$$$$$6 "+arg6Expression);

			  } else {
				  // var
				  if (eqnNum == 0) {
					  var.setEquation(rhs.trim());
				  } else {
					  String multiEqn = "~~|" + lhs.trim() + "="+rhs.trim();
					  var.setEquation(var.getEquation() + multiEqn);
				  }
			  }
			  if (eqnNum == 0)
				  var.setLhs(lhs.trim());
		  } else {
			  if (eqnNum == 0) {
				  var.setEquation(equation.trim());
			  } else {
//				  System.out.println("NOT EXPECTING THIS! MDLToSystem");
				  var.setEquation(equation.trim());
			  }
		  }
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

public boolean isFatal() {
	return fatal;
}

public void setFatal(boolean fatal) {
	this.fatal = fatal;
}

public boolean isWarnings() {
	return warnings;
}

public void setWarnings(boolean warnings) {
	this.warnings = warnings;
}

public String getFatalMessages() {
	return fatalMessages;
}

public void setFatalMessages(String fatalMessages) {
	this.fatalMessages = fatalMessages;
}

public List<String> getWarningMessages() {
	return warningMessages;
}

public void setWarningMessages(List<String> warningMessages) {
	this.warningMessages = warningMessages;
}
}
