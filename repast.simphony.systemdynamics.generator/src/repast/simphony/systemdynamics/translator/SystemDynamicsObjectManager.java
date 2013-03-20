package repast.simphony.systemdynamics.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import repast.simphony.systemdynamics.sdmodel.InfluenceLink;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;

public class SystemDynamicsObjectManager {

    private Map<String, SystemDynamicsObject> objectsMap;


    public SystemDynamicsObjectManager() {
	objectsMap = new HashMap<String, SystemDynamicsObject>();
    }
    

    
    public void processRSD() {
    	
    }
    
    public List<String> validate(Map<String, Equation> equations) {
	// need to check consistency between equations and graphics objects and arrows
	// the equations are always truth -- augment graphics as required
	
	List<String> addedScreenNames = new ArrayList<String>();
	
	for (SystemDynamicsObject sdo : objectsMap.values()) {
	    
	    List<String> incomingArrows = sdo.getIncomingArrows();
	    
	    List<Equation> eqns = sdo.getEquations();
	    for (Equation eqn : eqns) {
		String veqn = eqn.getVensimEquation();
		if (!eqn.isAssignment() || eqn.getVensimEquation().contains("GET XLS LOOKUPS") || 
			eqn.getVensimEquation().contains("GET XLS DATA") ||
			eqn.getVensimEquation().contains("GET XLS CONSTANTS"))
		    continue;
		Set<String> rhsVars = eqn.getRHSVariables();
		Iterator<String> iter = rhsVars.iterator();
		while (iter.hasNext()) {
		    String var = iter.next();
		    String screenName = getScreenName(var);
		    if (!incomingArrows.contains(screenName)) {
			System.out.println("\n\n###Validate###: "+eqn.getVensimEquation());
			System.out.println("###Validate###: "+sdo.getScreenName()+" add "+screenName);
			incomingArrows.add(screenName);
			addedScreenNames.add(screenName);
		    }
		    
		}
	    }
	}
	
	return addedScreenNames;
    }
	
	public void createSystemDynamicsObjectForNonGraphic(List<String> addedScreenNames, Map<String, Equation> equations) {
	
	// we have located items that appear in equations, but not in graphics
	// we need to create an associated SystemDynamicsObject and populate with
	// equations and arrows. Graphic Objects will need to be create else where???
	
	for (String added : addedScreenNames) {
	    addSystemDynamicsObject(added);
	    System.out.println("###AddObject###: "+added);
	    List<Equation> eqns = findEquationsWithScreenName(added, equations);
	    SystemDynamicsObject sdo = getObjectWithName(added);
	    for (Equation eqn : eqns) {
		if (!eqn.isAssignment() || eqn.getVensimEquation().contains("GET XLS LOOKUPS") || 
			eqn.getVensimEquation().contains("GET XLS DATA") ||
			eqn.getVensimEquation().contains("GET XLS CONSTANTS"))
		    continue;
		sdo.addEquation(eqn);
		Set<String> rhsVars = eqn.getRHSVariables();
		Iterator<String> iter = rhsVars.iterator();
		while (iter.hasNext()) {
		    String var = iter.next();
		    String screenName = getScreenName(var);
		    List<String> incomingArrows = sdo.getIncomingArrows();
		    if (!incomingArrows.contains(screenName)) {
			incomingArrows.add(screenName);
			System.out.println("\n\n###AddObject###: "+eqn.getVensimEquation());
			System.out.println("###AddObject###: "+sdo.getScreenName()+" add "+screenName);
			addedScreenNames.add(screenName);
		    }
		    
		}
	    }
	}
  
    }
    
    private List<Equation> findEquationsWithScreenName(String screenName, Map<String, Equation> equations) {
	List<Equation> list = new ArrayList<Equation>();
	for (String lhs : equations.keySet()) {
	    if (getScreenName(lhs).equals(screenName))
		list.add(equations.get(lhs));
	}
	return list;
    }

    public void print() {
	for (SystemDynamicsObject obj : objectsMap.values()) {
	    if (obj.getType().equals(View.VARIABLE) ||
		    obj.getType().equals(View.ARROW))
		obj.print();
	}
    }

    public static String getScreenName(String lhs) {
	// screen name from equation lhs:

	// if subscripted, remove subscripts
	// remove '"'

	String screenName = lhs.trim();

	if (lhs.contains("[")) {
	    screenName = lhs.split("\\[")[0].trim();
	}

	return screenName.replace("memory.", "").replace("lookup.", "").replace("array.", "").replace("\"", "");
    }

    public List<SystemDynamicsObject> stockVariables() {
	List<SystemDynamicsObject> stocks = new ArrayList<SystemDynamicsObject>();
	for (SystemDynamicsObject obj : objectsMap.values()) {
	    if (obj.getType().equals(View.VARIABLE)) {
		if (obj.getEquations().size() > 0) {
		    if (obj.getEquations().get(0).isStock())
			stocks.add(obj);
		}
	    }
	}

	return stocks;
    }

    public SystemDynamicsObject getObjectWithName(String screenName) {
	return objectsMap.get(screenName);
    }
    
    public String getAssociatedVariableFor(String screenName) {
	SystemDynamicsObject sdo = objectsMap.get(screenName);
	return sdo.getAssociatedVariableName();
    }

    public void addSystemDynamicsObject(String screenName) {
//	System.out.println("SDOM: Add "+screenName);
	if (!objectsMap.containsKey(screenName))
	    objectsMap.put(screenName, new SystemDynamicsObject(screenName));
    }

    private void addSystemDynamicsObjectPlaceHolder(String screenName) {
//	System.out.println("SDOM: Adding placeholder for: "+screenName);
	objectsMap.put(screenName, new SystemDynamicsObject(screenName));
    }

    public void addGraphicObject(String screenName, GraphicObject graphicObject) {
	if (!objectsMap.containsKey(screenName))
	    addSystemDynamicsObjectPlaceHolder(screenName);

	objectsMap.get(screenName).addGraphicObject(graphicObject);
    }

    public void addEquation(String screenName, Equation equation) {
	if (!objectsMap.containsKey(screenName))
	    addSystemDynamicsObjectPlaceHolder(screenName);
	objectsMap.get(screenName).addEquation(equation);
    }

    public void addIncomingArrow(String screenName, String incomingArrow) {
	if (!objectsMap.containsKey(screenName))
	    addSystemDynamicsObjectPlaceHolder(screenName);
	objectsMap.get(screenName).addIncomingArrow(incomingArrow);
    }

    public void addOutgoingArrow(String screenName, String incomingArrow) {
	if (!objectsMap.containsKey(screenName))
	    addSystemDynamicsObjectPlaceHolder(screenName);
	objectsMap.get(screenName).addOutgoingArrow(incomingArrow);
    }

    public List<Equation> getEquations(String screenName) {
	if (!objectsMap.containsKey(screenName))
	    addSystemDynamicsObjectPlaceHolder(screenName);

	return objectsMap.get(screenName).getEquations();

    }

    public List<GraphicObject> getGraphicObjects(String screenName) {
	if (!objectsMap.containsKey(screenName)) 
	    addSystemDynamicsObjectPlaceHolder(screenName);
	return objectsMap.get(screenName).getGraphicObjects();
    }

    public List<String> getIncomingArrows(String screenName) {
	if (!objectsMap.containsKey(screenName))
	    addSystemDynamicsObjectPlaceHolder(screenName);
	return objectsMap.get(screenName).getIncomingArrows();

    }

    public List<String> getOutgoingArrows(String screenName) {
	if (!objectsMap.containsKey(screenName))
	    addSystemDynamicsObjectPlaceHolder(screenName);
	return objectsMap.get(screenName).getOutgoingArrows();
    }
    
    public void extractStructure(View view) {
	
	    Map<String, String> idToName= new HashMap<String, String>();
	    Map<String, String> nameToId = new HashMap<String, String>();
	    Map<String, String> nameToType = new HashMap<String, String>();

	    Map<String, List<String>> inArrows = new HashMap<String, List<String>> ();
	    Map<String, List<String>> outArrows  = new HashMap<String, List<String>> ();

	// first grab everything but arrows and populate data structures

	for (GraphicObject go : view.getGraphicObjects()) {
	    if (go.getType().equals(GraphicObject.ARROW))
		continue;

	    String id = go.getId();
	    String type = view.translateCodeToString(go.getType());
	    String name = go.getName();

	    idToName.put(id, name);
	    nameToId.put(name, id);
	    nameToType.put(name, type);
	}

	// now extract arrows
	for (GraphicObject go : view.getGraphicObjects()) {
	    if (!go.getType().equals(GraphicObject.ARROW))
		continue;


	    String id = go.getId();
	    String type = view.translateCodeToString(go.getType());
	    String name = go.getName();

	    idToName.put(id, name);
	    nameToId.put(name, id);
	    nameToType.put(name, type);

	    String to = idToName.get(go.getTo());
	    String from = idToName.get(go.getFrom());

	    if (!inArrows.containsKey(to))
		inArrows.put(to, new ArrayList<String>());
	    if (!outArrows.containsKey(from))
		outArrows.put(from, new ArrayList<String>());

	    List<String> in = inArrows.get(to);
	    if (!in.contains(from))
		in.add(from);

	    List<String> out = outArrows.get(from);
	    if (!out.contains(to))
		out.add(to);
	}

	// now pass this information to the SystemDynamicsOjbectManager;
	for (GraphicObject go : view.getGraphicObjects()) {

	    String id = go.getId();
	    String name = idToName.get(id);
	    String type = nameToType.get(name);

	    if (type.equals(View.ARROW))
		continue;

	    addSystemDynamicsObject(name);
	    addGraphicObject(name, go);
	}


	// we only want arrows that are variable to variable
	// note that we can have indirect connects through valves
	// valves should be the from in these instances?
	// but handle both

	for (String to : inArrows.keySet()) {
	    for (String from : inArrows.get(to)) {

		SystemDynamicsObject sdTo = getObjectWithName(to);
		SystemDynamicsObject sdFrom = getObjectWithName(from);
		
		SystemDynamicsObject effectiveTo = sdTo;
		SystemDynamicsObject effectiveFrom = sdFrom;
		
		// if this is a purely visual arrow (i.e. to/from cloud, just skip)
		
		if (sdTo.getType().equals(View.COMMENT) || sdFrom.getType().equals(View.COMMENT))
		    continue;

		if (sdTo != null && sdFrom != null) {
		    if (sdTo.getType().equals(View.VALVE)) {
			effectiveTo = getObjectWithName(getAssociatedVariableFor(sdTo.getScreenName()));
		    }
		    
		    if (sdFrom.getType().equals(View.VALVE)) {
			effectiveFrom = getObjectWithName(getAssociatedVariableFor(sdFrom.getScreenName()));
		    }
		    
		    if (effectiveTo.getType().equals(View.VARIABLE) && effectiveFrom.getType().equals(View.VARIABLE)) {
			addIncomingArrow(effectiveTo.getScreenName(), effectiveFrom.getScreenName());
		    } else {
//			System.out.println("Something not right");
		    }
		}
	    }
	}

	for (String from : outArrows.keySet()) {
	    for (String to : outArrows.get(from)) {
		SystemDynamicsObject sdTo = getObjectWithName(to);
		SystemDynamicsObject sdFrom = getObjectWithName(from);
		
		SystemDynamicsObject effectiveTo = sdTo;
		SystemDynamicsObject effectiveFrom = sdFrom;

		if (sdTo != null && sdFrom != null) {
		    if (sdTo.getType().equals(View.VALVE)) {
			effectiveTo = getObjectWithName(getAssociatedVariableFor(sdTo.getScreenName()));
		    }
		    
		    if (sdFrom.getType().equals(View.VALVE)) {
			effectiveFrom = getObjectWithName(getAssociatedVariableFor(sdFrom.getScreenName()));
		    }
		    
		    if (effectiveTo.getType().equals(View.VARIABLE) && effectiveFrom.getType().equals(View.VARIABLE)) {
			addOutgoingArrow(effectiveFrom.getScreenName(), effectiveTo.getScreenName());
		    } else {
//			System.out.println("Something not right");
		    }
		}
	    }
	}
    }
}
