/**
 * 
 */
package repast.simphony.statecharts.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.PseudoStateTypes;
import repast.simphony.statecharts.scmodel.State;
import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartFactory;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;

/**
 * Static utility methods used by the code generator.
 * 
 * @author Nick Collier
 */
public class GeneratorUtil {

  private static final State NULL_STATE = StatechartFactory.eINSTANCE.createState();
  static {
    NULL_STATE.setId("NULL");
    NULL_STATE.setUuid("NULL");
  }

  public static class StateBlock {

    private static int counter = 1;

    private String csVar, sVar, methodName, sbVar, csbVar, onEnterTypeName, onExitTypeName;

    public StateBlock() {
      csVar = "cs" + counter;
      sVar = "s" + counter;
      methodName = "createCS" + counter;
      sbVar = "ssBuilder" + counter;
      csbVar = "csBuilder" + counter;
      onEnterTypeName = "OnEnterAction" + counter;
      onExitTypeName = "OnExitAction" + counter;
      ++counter;
    }
  }
  
  public static class TransitionBlock {

    private static int counter = 1;

    private String methodName;

    public TransitionBlock() {
      methodName = "createTransition" + counter;
      ++counter;
    }
  }
  
  private static int bCounter = 1;
  private static Map<String, StateBlock> blockMap = new HashMap<String, StateBlock>();
  private static Map<String, TransitionBlock> transitionMap = new HashMap<String, TransitionBlock>();
  private static Map<String, String> branchVarMap = new HashMap<String, String>();

  public static void init() {
    blockMap.clear();
    branchVarMap.clear();
    transitionMap.clear();
    bCounter = 1;
    StateBlock.counter = 1;
    TransitionBlock.counter = 1;
  }
  
  public static String getTransitionMethodName(String uuid) {
    TransitionBlock block = transitionMap.get(uuid);
    if (block == null) {
      block = new TransitionBlock();
      transitionMap.put(uuid, block);
    }
    return block.methodName;
  }
  
  public static String getBranchVar(String uuid) {
    String var = branchVarMap.get(uuid);
    if (var == null) {
      var = "branch" + bCounter;
      branchVarMap.put(uuid, var);
      ++bCounter;
    }
    return var;
  }

  public static String getCSMethodName(String uuid) {
    return getCSBlock(uuid).methodName;
  }

  public static String getCSVar(String uuid) {
    return getCSBlock(uuid).csVar;
  }

  public static String getSVar(String uuid) {
    return getCSBlock(uuid).sVar;
  }

  public static String getSBVar(String uuid) {
    return getCSBlock(uuid).sbVar;
  }

  public static String getCSBVar(String uuid) {
    return getCSBlock(uuid).csbVar;
  }

  public static String getOnEnterTypeName(String uuid) {
    StateBlock block = getCSBlock(uuid);
    return block.onEnterTypeName;
  }

  public static String getOnExitTypeName(String uuid) {
    StateBlock block = getCSBlock(uuid);
    return block.onExitTypeName;
  }

  private static StateBlock getCSBlock(String uuid) {
    StateBlock block = blockMap.get(uuid);
    if (block == null) {
      block = new StateBlock();
      blockMap.put(uuid, block);
    }
    return block;
  }

  public static String getPackageFromType(String type) {
    int index = type.lastIndexOf(".");
    if (index == -1) return "";
    return type.substring(0, index);
  }
  
  public static String getSimpleClassName(String fqn) {
    int index = fqn.lastIndexOf(".");
    if (index == -1) return fqn;
    return fqn.substring(index + 1, fqn.length());
  }
  
  public static List<EObject> filterForComposites(List<EObject> list) {
    return filter(list, StatechartPackage.Literals.COMPOSITE_STATE);
  }

  public static List<EObject> filterForStates(List<EObject> list) {
    return filter(list, StatechartPackage.Literals.STATE);
  }

  public static List<EObject> filterForHistory(List<EObject> list) {
    return filter(list, StatechartPackage.Literals.HISTORY);
  }

  public static List<EObject> filterForFinal(List<EObject> list) {
    return filter(list, StatechartPackage.Literals.FINAL_STATE);
  }

  public static List<EObject> filterForChoices(List<EObject> list) {
    List<EObject> ret = new ArrayList<EObject>();
    for (EObject obj : list) {
      if (obj.eClass().equals(StatechartPackage.Literals.PSEUDO_STATE)) {
        PseudoState ps = (PseudoState) obj;
        if (ps.getType().equals(PseudoStateTypes.CHOICE))
          ret.add(obj);
      }
    }
    return ret;
  }

  private static StateMachine findStateMachine(EObject obj) {
    EObject container = obj.eContainer();
    while (container != null) {
      if (container.eClass().equals(StatechartPackage.Literals.STATE_MACHINE))
        return (StateMachine) container;
      container = container.eContainer();
    }
    return null;
  }

  public static AbstractState findFirstState(List<EObject> list) {
    for (EObject obj : list) {
      if (obj.eClass().equals(StatechartPackage.Literals.PSEUDO_STATE)) {
        PseudoState ps = (PseudoState) obj;
        if (ps.getType().equals(PseudoStateTypes.ENTRY)) {
          StateMachine root = findStateMachine(ps);
          for (Transition transition : root.getTransitions()) {
            if (transition.getFrom().equals(ps))
              return transition.getTo();
          }
        }
      }
    }
    return NULL_STATE;
  }

  public static AbstractState findInitialState(List<EObject> list) {
    for (EObject obj : list) {
      if (obj.eClass().equals(StatechartPackage.Literals.PSEUDO_STATE)) {
        PseudoState ps = (PseudoState) obj;
        if (ps.getType().equals(PseudoStateTypes.INITIAL)) {
          StateMachine root = findStateMachine(ps);
          for (Transition transition : root.getTransitions()) {
            if (transition.getFrom().equals(ps))
              return transition.getTo();
          }
        }
      }
    }
    return NULL_STATE;
  }

  private static List<EObject> filter(List<EObject> list, EClass clazz) {
    List<EObject> ret = new ArrayList<EObject>();
    for (EObject obj : list) {
      if (obj.eClass().equals(clazz))
        ret.add(obj);
    }
    return ret;
  }
}
