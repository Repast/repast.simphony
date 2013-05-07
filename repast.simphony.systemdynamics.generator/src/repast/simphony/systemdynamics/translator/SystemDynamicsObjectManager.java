package repast.simphony.systemdynamics.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemDynamicsObjectManager {

	private Map<String, SystemDynamicsObject> objectsMap;


	public SystemDynamicsObjectManager() {
		objectsMap = new HashMap<String, SystemDynamicsObject>();
	}



	public void processRSD() {

	}

	public List<EquationGraphicValidation> validate(Map<String, Equation> equations) {
		// need to check consistency between equations and graphics objects and arrows
		// the equations are always truth -- augment graphics as required
		
		
		System.out.println("######################" + "VALIDATE" + "######################");

		List<EquationGraphicValidation> validations = new ArrayList<EquationGraphicValidation>();

		for (SystemDynamicsObject sdo : objectsMap.values()) {
			EquationGraphicValidation egv = new EquationGraphicValidation(this, sdo, equations);
			egv.validate();
			if (!egv.isValid())
				validations.add(egv);
		}
		System.out.println("######################" + "VALIDATE" + "######################");
		return validations;
	}

	public void createSystemDynamicsObjectForNonGraphic(List<EquationGraphicValidation> validations, Map<String, Equation> equations) {

		// we have located items that appear in equations, but not in graphics
		// we need to create an associated SystemDynamicsObject and populate with
		// equations and arrows. Graphic Objects will need to be create else where???
		
		System.out.println("######################" + "createSystemDynamicsObjectForNonGraphic" + "######################");

		for (EquationGraphicValidation egv : validations) {
			egv.apply();
		}
		System.out.println("######################" + "createSystemDynamicsObjectForNonGraphic" + "######################");

	}

	public List<Equation> findEquationsWithScreenName(String screenNameE, Map<String, Equation> equations) {
		
			String screenName = getScreenName(screenNameE);  // make sure that we translate to screen name if passed LHS
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
					obj.getType().equals(View.RATE) ||
					obj.getType().equals(View.CLOUD) ||
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
	
	  public Iterable<String> screenNames() {
		    return objectsMap.keySet();
		  }

	public SystemDynamicsObject getObjectWithName(String screenNameE) {
		String screenName = getScreenName(screenNameE);
		return objectsMap.get(screenName);
	}

	public String getAssociatedVariableFor(String screenNameE) {
		String screenName = getScreenName(screenNameE);
		SystemDynamicsObject sdo = objectsMap.get(screenName);
		return sdo.getAssociatedVariableName();
	}

	public void addSystemDynamicsObject(String screenNameE) {
		String screenName = getScreenName(screenNameE);
		
		if (!objectsMap.containsKey(screenName)) {
			System.out.println("SDOM: Add "+screenName);
			objectsMap.put(screenName, new SystemDynamicsObject(screenName));
		}
	}

	private void addSystemDynamicsObjectPlaceHolder(String screenNameE) {
		String screenName = getScreenName(screenNameE);
		System.out.println("SDOM: Adding placeholder for: "+screenName);
		objectsMap.put(screenName, new SystemDynamicsObject(screenName));
	}

	public void addGraphicObject(String screenNameE, GraphicObject graphicObject) {
		String screenName = getScreenName(screenNameE);
		if (!objectsMap.containsKey(screenName))
			addSystemDynamicsObjectPlaceHolder(screenName);

		objectsMap.get(screenName).addGraphicObject(graphicObject);
	}

	public void addEquation(String screenNameE, Equation equation) {
		String screenName = getScreenName(screenNameE);
		System.out.println("addEquation: "+screenName+" "+equation.getEquation());
		if (!objectsMap.containsKey(screenName))
			addSystemDynamicsObjectPlaceHolder(screenName);
		objectsMap.get(screenName).addEquation(equation);
	}

	public void addIncomingArrow(String screenNameE, Arrow incomingArrow) {
		String screenName = getScreenName(screenNameE);
		if (!objectsMap.containsKey(screenName))
			addSystemDynamicsObjectPlaceHolder(screenName);
		objectsMap.get(screenName).addIncomingArrow(incomingArrow);
	}

	public void addOutgoingArrow(String screenNameE, Arrow incomingArrow) {
		String screenName = getScreenName(screenNameE);
		if (!objectsMap.containsKey(screenName))
			addSystemDynamicsObjectPlaceHolder(screenName);
		objectsMap.get(screenName).addOutgoingArrow(incomingArrow);
	}

	public List<Equation> getEquations(String screenNameE) {
		String screenName = getScreenName(screenNameE);
		if (!objectsMap.containsKey(screenName))
			addSystemDynamicsObjectPlaceHolder(screenName);

		return objectsMap.get(screenName).getEquations();

	}

	public List<GraphicObject> getGraphicObjects(String screenNameE) {
		String screenName = getScreenName(screenNameE);  // make sure that we translate to screen name if passed LHS
		if (!objectsMap.containsKey(screenName)) 
			addSystemDynamicsObjectPlaceHolder(screenName);
		return objectsMap.get(screenName).getGraphicObjects();
	}

	public List<Arrow> getIncomingArrows(String screenNameE) {
		String screenName = getScreenName(screenNameE);
		if (!objectsMap.containsKey(screenName))
			addSystemDynamicsObjectPlaceHolder(screenName);
		return objectsMap.get(screenName).getIncomingArrows();

	}

	public List<Arrow> getOutgoingArrows(String screenNameE) {
		String screenName = getScreenName(screenNameE);
		if (!objectsMap.containsKey(screenName))
			addSystemDynamicsObjectPlaceHolder(screenName);
		return objectsMap.get(screenName).getOutgoingArrows();
	}

	public void extractStructure(View view) {

		Map<String, GraphicObject> idToGraphicObject = new HashMap<String, GraphicObject>();

		Map<String, String> idToName= new HashMap<String, String>();
		Map<String, String> nameToId = new HashMap<String, String>();
		Map<String, String> nameToType = new HashMap<String, String>();

		Map<String, List<Arrow>> inArrows = new HashMap<String, List<Arrow>> ();
		Map<String, List<Arrow>> outArrows  = new HashMap<String, List<Arrow>> ();

		// need to combine the valve and associated rate into a single SDO
		for (GraphicObject go : view.getGraphicObjects()) {
			idToGraphicObject.put(go.getId(), go);
		}

		// first grab everything but arrows and populate data structures

		for (GraphicObject go : view.getGraphicObjects()) {
			if (go.isArrow())
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
			if (!go.isArrow())
				continue;

			// for this arrow, get ID, type (arrow), and name (null)

			String id = go.getId();
			String type = view.translateCodeToString(go.getType());
			String name = go.getName();

			// should we even care about this? since name is null!

			idToName.put(id, name);
			nameToId.put(name, id);
			nameToType.put(name, type);

			// get the GO at the head of the arrow
			// if pointing to a valve, set GO to its associated rate
			
			// Note that Vensim stores valve arrow information  
			
			go.print();
			
			boolean swap = false;
			
			String arrowType = "Unknown";
			String direction = "Unknown";
			
			if (go.isInfluenceArrow()) 
				arrowType = Arrow.INFLUENCE;
			if (go.isFlowArrow()) 
				arrowType = Arrow.FLOW;
			
			GraphicObject EffectiveToGO = idToGraphicObject.get(go.getTo());
			// if arrow is to a valve, point to rate associated with valve
			if (EffectiveToGO.isValve()) {
				EffectiveToGO = EffectiveToGO.getAssociatedVariable();
			}

			// get the name at the head of the arrow
			String toName = idToName.get(EffectiveToGO.getId());

			// get the GO at the tail of the arrow
			// if pointing to a valve, set GO to its associated rate

			GraphicObject EffectiveFromGO = idToGraphicObject.get(go.getFrom());
			if (EffectiveFromGO.isValve()) {
				if (go.isInFlowArrow()) {
					swap = true;
				} else {
					
				}					
				EffectiveFromGO = EffectiveFromGO.getAssociatedVariable();
			}
			
			System.out.println("swap "+swap);

			// get the name at the head of the arrow
			String fromName = idToName.get(EffectiveFromGO.getId());
			
			
			if (swap) {
				String tmp = toName;
				toName = fromName;
				fromName = tmp;
			}

			// have lists been initialized?
			if (!inArrows.containsKey(toName))
				inArrows.put(toName, new ArrayList<Arrow>());
			if (!outArrows.containsKey(fromName))
				outArrows.put(fromName, new ArrayList<Arrow>());

			// add arrows to in and out lists as needed
			List<Arrow> in = inArrows.get(toName);
			boolean found = hasArrowWithOtherEnd( in, fromName);
			if (!found) {
				Arrow inArrow = new Arrow(fromName, Arrow.IN, arrowType);
				in.add(inArrow);
				System.out.println(inArrow);
			}

			List<Arrow> out = outArrows.get(fromName);
			found = hasArrowWithOtherEnd( out, toName);
			if (!found) {
				Arrow outArrow = new Arrow(toName, Arrow.OUT, arrowType);
				out.add(outArrow);
				System.out.println(outArrow);
			}
		}

		// now pass this information to the SystemDynamicsOjbectManager;
		for (GraphicObject go : view.getGraphicObjects()) {

			String id = go.getId();
			String name = idToName.get(id);
			String type = nameToType.get(name);

			// arrows are processed separately
			if (go.isArrow())
				continue;
System.out.println("Extract Structure add");
			addSystemDynamicsObject(name);
			addGraphicObject(name, go);
			//	    if (go.getAssociatedVariable() != null) {
			//	    	System.out.println("adding associated "+go.getAssociatedVariable().getName());
			//	    	addGraphicObject(go.getAssociatedVariable().getName(), go.getAssociatedVariable());
			//	    }
		}


		// we only want arrows that are variable to variable
		// note that we can have indirect connects through valves
		// valves should be the from in these instances?
		// but handle both

		for (String to : inArrows.keySet()) {
			for (Arrow from : inArrows.get(to)) {

				SystemDynamicsObject sdTo = getObjectWithName(to);
				SystemDynamicsObject sdFrom = getObjectWithName(from.getOtherEnd());

				SystemDynamicsObject effectiveTo = sdTo;
				SystemDynamicsObject effectiveFrom = sdFrom;

				// if this is a purely visual arrow (i.e. to/from cloud, just skip)

//				if (sdTo.getType().equals(View.COMMENT) || sdFrom.getType().equals(View.COMMENT))
//					continue;

				if (sdTo != null && sdFrom != null) {
					// MJB 4/10 try this 
					//		    if (sdTo.getType().equals(View.VALVE)) {
					//			effectiveTo = getObjectWithName(getAssociatedVariableFor(sdTo.getScreenName()));
					//		    }
					//		    
					//		    if (sdFrom.getType().equals(View.VALVE)) {
					//			effectiveFrom = getObjectWithName(getAssociatedVariableFor(sdFrom.getScreenName()));
					//		    }
					//		    
					if ((effectiveTo.getType().equals(View.VARIABLE) || effectiveTo.getType().equals(View.RATE) || effectiveTo.getType().equals(View.CLOUD)) && 
							(effectiveFrom.getType().equals(View.VARIABLE) || effectiveFrom.getType().equals(View.RATE) ||  effectiveFrom.getType().equals(View.CLOUD))) {
						addIncomingArrow(effectiveTo.getScreenName(), from);
//						System.out.println("addIn: "+from);
					} else {
						//			System.out.println("Something not right");
					}
				}
			}
		}

		for (String from : outArrows.keySet()) {
			for (Arrow to : outArrows.get(from)) {
				SystemDynamicsObject sdTo = getObjectWithName(to.getOtherEnd());
				SystemDynamicsObject sdFrom = getObjectWithName(from);

				SystemDynamicsObject effectiveTo = sdTo;
				SystemDynamicsObject effectiveFrom = sdFrom;

				if (sdTo != null && sdFrom != null) {
					// MJB 4/10 try this 
					//		    if (sdTo.getType().equals(View.VALVE)) {
					//			effectiveTo = getObjectWithName(getAssociatedVariableFor(sdTo.getScreenName()));
					//		    }
					//		    
					//		    if (sdFrom.getType().equals(View.VALVE)) {
					//			effectiveFrom = getObjectWithName(getAssociatedVariableFor(sdFrom.getScreenName()));
					//		    }

					if ((effectiveTo.getType().equals(View.VARIABLE) || effectiveTo.getType().equals(View.RATE) || effectiveTo.getType().equals(View.CLOUD)) && 
							(effectiveFrom.getType().equals(View.VARIABLE) || effectiveFrom.getType().equals(View.RATE) ||  effectiveFrom.getType().equals(View.CLOUD))) {
						addOutgoingArrow(effectiveFrom.getScreenName(),to);
//						System.out.println("addOut: "+to);
					} else {
						//			System.out.println("Something not right");
					}
				}
			}
		}
	}
	
	private boolean hasArrowWithOtherEnd(List<Arrow> in, String name) {
		boolean found = false;
		for (Arrow a : in) {
			if (a.getOtherEnd().equals(name))
				found = true;
		}
		return found;
	}
}
