package repast.simphony.systemdynamics.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SystemDynamicsObjectManager {

  private Map<String, SystemDynamicsObject> objectsMap;

  public SystemDynamicsObjectManager() {
    objectsMap = new HashMap<String, SystemDynamicsObject>();
  }

  public void processRSD() {}
  
  public Iterable<String> screenNames() {
    return objectsMap.keySet();
  }

  public List<String> validate(Map<String, Equation> equations) {
    // need to check consistency between equations and graphics objects and
    // arrows
    // the equations are always truth -- augment graphics as required

    List<String> addedScreenNames = new ArrayList<String>();

    for (SystemDynamicsObject sdo : objectsMap.values()) {

      List<Arrow> incomingArrows = sdo.getIncomingArrows();

      List<Equation> eqns = sdo.getEquations();
      for (Equation eqn : eqns) {

        String veqn = eqn.getVensimEquation();

        if (!eqn.isAssignment() || eqn.getVensimEquation().contains("GET XLS LOOKUPS")
            || eqn.getVensimEquation().contains("GET XLS DATA")
            || eqn.getVensimEquation().contains("GET XLS CONSTANTS"))
          continue;

        Set<String> rhsVars = eqn.getRHSVariables();
        Iterator<String> iter = rhsVars.iterator();

        while (iter.hasNext()) {
          String var = iter.next();
          String screenName = getScreenName(var);
          if (!incomingArrows.contains(screenName)) {
            // System.out.println("\n\n###Validate###: "+eqn.getVensimEquation());
            // System.out.println("###Validate###: "+sdo.getScreenName()+" add "+screenName);
            Arrow arrow = new Arrow(screenName, Arrow.IN, Arrow.INFLUENCE);
            // incomingArrows.add(arrow);
            System.out.println("Would add " + arrow);
            addedScreenNames.add(screenName);
          }

        }
      }
    }

    return addedScreenNames;
  }

  public void createSystemDynamicsObjectForNonGraphic(List<String> addedScreenNames,
      Map<String, Equation> equations) {

    // we have located items that appear in equations, but not in graphics
    // we need to create an associated SystemDynamicsObject and populate with
    // equations and arrows. Graphic Objects will need to be create else
    // where???

    for (String added : addedScreenNames) {
      addSystemDynamicsObject(added);
      System.out.println("###AddObject###: " + added);
      List<Equation> eqns = findEquationsWithScreenName(added, equations);
      SystemDynamicsObject sdo = getObjectWithName(added);
      for (Equation eqn : eqns) {
        if (!eqn.isAssignment() || eqn.getVensimEquation().contains("GET XLS LOOKUPS")
            || eqn.getVensimEquation().contains("GET XLS DATA")
            || eqn.getVensimEquation().contains("GET XLS CONSTANTS"))
          continue;
        sdo.addEquation(eqn);
        Set<String> rhsVars = eqn.getRHSVariables();
        Iterator<String> iter = rhsVars.iterator();
        while (iter.hasNext()) {
          String var = iter.next();
          String screenName = getScreenName(var);
          List<Arrow> incomingArrows = sdo.getIncomingArrows();
          if (!incomingArrows.contains(screenName)) {
            incomingArrows.add(new Arrow(screenName, Arrow.IN, Arrow.INFLUENCE));
            System.out.println("\n\n###AddObject###: " + eqn.getVensimEquation());
            System.out.println("###AddObject###: " + sdo.getScreenName() + " add " + screenName);
            addedScreenNames.add(screenName);
          }

        }
      }
    }

  }

  private List<Equation> findEquationsWithScreenName(String screenName,
      Map<String, Equation> equations) {
    List<Equation> list = new ArrayList<Equation>();
    for (String lhs : equations.keySet()) {
      if (getScreenName(lhs).equals(screenName))
        list.add(equations.get(lhs));
    }
    return list;
  }

  public void print() {
    for (SystemDynamicsObject obj : objectsMap.values()) {
      if (obj.getType().equals(View.VARIABLE) || obj.getType().equals(View.RATE)
          || obj.getType().equals(View.CLOUD) || obj.getType().equals(View.ARROW))
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

    return screenName.replace("memory.", "").replace("lookup.", "").replace("array.", "")
        .replace("\"", "");
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
    System.out.println("SDOM: Add " + screenName);
    if (!objectsMap.containsKey(screenName))
      objectsMap.put(screenName, new SystemDynamicsObject(screenName));
  }

  private void addSystemDynamicsObjectPlaceHolder(String screenName) {
    System.out.println("SDOM: Adding placeholder for: " + screenName);
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

  public void addIncomingArrow(String screenName, Arrow incomingArrow) {
    if (!objectsMap.containsKey(screenName))
      addSystemDynamicsObjectPlaceHolder(screenName);
    objectsMap.get(screenName).addIncomingArrow(incomingArrow);
  }

  public void addOutgoingArrow(String screenName, Arrow incomingArrow) {
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

  public List<Arrow> getIncomingArrows(String screenName) {
    if (!objectsMap.containsKey(screenName))
      addSystemDynamicsObjectPlaceHolder(screenName);
    return objectsMap.get(screenName).getIncomingArrows();

  }

  public List<Arrow> getOutgoingArrows(String screenName) {
    if (!objectsMap.containsKey(screenName))
      addSystemDynamicsObjectPlaceHolder(screenName);
    return objectsMap.get(screenName).getOutgoingArrows();
  }

  public void extractStructure(View view) {

    Map<String, GraphicObject> idToGraphicObject = new HashMap<String, GraphicObject>();

    Map<String, String> idToName = new HashMap<String, String>();
    Map<String, String> nameToId = new HashMap<String, String>();
    Map<String, String> nameToType = new HashMap<String, String>();

    Map<String, List<Arrow>> inArrows = new HashMap<String, List<Arrow>>();
    Map<String, List<Arrow>> outArrows = new HashMap<String, List<Arrow>>();

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

      System.out.println("swap " + swap);

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
      boolean found = hasArrowWithOtherEnd(in, fromName);
      if (!found) {
        Arrow inArrow = new Arrow(fromName, Arrow.IN, arrowType);
        in.add(inArrow);
        System.out.println(inArrow);
      }

      List<Arrow> out = outArrows.get(fromName);
      found = hasArrowWithOtherEnd(out, toName);
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

      addSystemDynamicsObject(name);
      addGraphicObject(name, go);
      // if (go.getAssociatedVariable() != null) {
      // System.out.println("adding associated "+go.getAssociatedVariable().getName());
      // addGraphicObject(go.getAssociatedVariable().getName(),
      // go.getAssociatedVariable());
      // }
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

        // if (sdTo.getType().equals(View.COMMENT) ||
        // sdFrom.getType().equals(View.COMMENT))
        // continue;

        if (sdTo != null && sdFrom != null) {
          // MJB 4/10 try this
          // if (sdTo.getType().equals(View.VALVE)) {
          // effectiveTo =
          // getObjectWithName(getAssociatedVariableFor(sdTo.getScreenName()));
          // }
          //
          // if (sdFrom.getType().equals(View.VALVE)) {
          // effectiveFrom =
          // getObjectWithName(getAssociatedVariableFor(sdFrom.getScreenName()));
          // }
          //
          if ((effectiveTo.getType().equals(View.VARIABLE)
              || effectiveTo.getType().equals(View.RATE) || effectiveTo.getType()
              .equals(View.CLOUD))
              && (effectiveFrom.getType().equals(View.VARIABLE)
                  || effectiveFrom.getType().equals(View.RATE) || effectiveFrom.getType().equals(
                  View.CLOUD))) {
            addIncomingArrow(effectiveTo.getScreenName(), from);
            // System.out.println("addIn: "+from);
          } else {
            // System.out.println("Something not right");
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
          // if (sdTo.getType().equals(View.VALVE)) {
          // effectiveTo =
          // getObjectWithName(getAssociatedVariableFor(sdTo.getScreenName()));
          // }
          //
          // if (sdFrom.getType().equals(View.VALVE)) {
          // effectiveFrom =
          // getObjectWithName(getAssociatedVariableFor(sdFrom.getScreenName()));
          // }

          if ((effectiveTo.getType().equals(View.VARIABLE)
              || effectiveTo.getType().equals(View.RATE) || effectiveTo.getType()
              .equals(View.CLOUD))
              && (effectiveFrom.getType().equals(View.VARIABLE)
                  || effectiveFrom.getType().equals(View.RATE) || effectiveFrom.getType().equals(
                  View.CLOUD))) {
            addOutgoingArrow(effectiveFrom.getScreenName(), to);
            // System.out.println("addOut: "+to);
          } else {
            // System.out.println("Something not right");
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
