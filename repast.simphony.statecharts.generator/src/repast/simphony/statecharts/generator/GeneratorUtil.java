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
  
  private static class StateBlock {

    private String csVar, sVar, methodName, sbVar, csbVar, onEnterTypeName, onExitTypeName;

    public StateBlock(int namesId, int counter) {
      csVar = "cs" + counter;
      sVar = "s" + counter;
      methodName = "createCS" + counter;
      sbVar = "ssBuilder" + counter;
      csbVar = "csBuilder" + counter;
      onEnterTypeName = "SC" + namesId + "OnEnterAction" + counter;
      onExitTypeName = "SC" + namesId + "OnExitAction" + counter;
    }
  }
  
  public static class TransitionBlock {
    
    private String methodName, guardType, onTransType,
      tdfType, ctcType, mcType, meType;

    public TransitionBlock(int namesId, int counter) {
      methodName = "createTransition" + counter;
      guardType = "SC" + namesId + "Guard" + counter;
      onTransType = "SC" + namesId + "OnTransition" + counter;
      tdfType = "SC" + namesId + "TriggerDoubleFunction" + counter;
      ctcType = "SC" + namesId + "ConditionTriggerCondition" + counter;
      mcType = "SC" + namesId + "MessageCondition" + counter;
      meType = "SC" + namesId + "MessageEquals" + counter;
    }
  }
  
  private static class Names {
    
    private static int nameCounter = 1;
    
    private int stateCounter = 1;
    private int transCounter = 1;
    private int branchCounter = 1;
    private int id;
    
    private Map<String, StateBlock> stateBlockMap = new HashMap<String, StateBlock>();
    private Map<String, TransitionBlock> transitionMap = new HashMap<String, TransitionBlock>();
    private Map<String, String> branchVarMap = new HashMap<String, String>();
    
    public Names() {
      this.id = nameCounter++;
    }
    
    public StateBlock getStateBlock(String uuid) {
      StateBlock block = stateBlockMap.get(uuid);
      if (block == null) {
        block = new StateBlock(id, stateCounter);
        stateBlockMap.put(uuid, block);
        stateCounter++;
      }
      return block;
    }
    
    public TransitionBlock getTransitionBlock(String uuid) {
      TransitionBlock block = transitionMap.get(uuid);
      if (block == null) {
        block = new TransitionBlock(id, transCounter);
        transitionMap.put(uuid, block);
        transCounter++;
      }
      return block;
    }
    
    public String getBranchVar(String uuid) {
      String var = branchVarMap.get(uuid);
      if (var == null) {
        var = "branch" + branchCounter;
        branchVarMap.put(uuid, var);
        ++branchCounter;
      }
      return var;
    }
    
    public void reset() {
      stateBlockMap.clear();
      transitionMap.clear();
      branchVarMap.clear();
      stateCounter = transCounter = branchCounter = 1;
    }
  }

  private static CodeExpander expander = new CodeExpander();
  private static Map<String, Names> namesMap = new HashMap<String, Names>();
  private static Names curNames = null;

  public static void init(String uuid) {
    Names names = namesMap.get(uuid);
    if (names == null) {
      names = new Names();
      namesMap.put(uuid, names);
    }
    names.reset();
    curNames = names;
  }
  
  public static String getTDFType(String uuid) {
    return getTBlock(uuid).tdfType;
  }
  
  public static String getCTCType(String uuid) {
    return getTBlock(uuid).ctcType;
  }
  
  public static String getMCType(String uuid) {
    return getTBlock(uuid).mcType;
  }
  
  public static String getMEType(String uuid) {
    return getTBlock(uuid).meType;
  }
  
  public static String getGuardType(String uuid) {
    return getTBlock(uuid).guardType;
  }
  
  public static String getOnTransType(String uuid) {
    return getTBlock(uuid).onTransType;
  }
  
  public static String getTransitionMethodName(String uuid) {
    return getTBlock(uuid).methodName;
  }
  
  private static TransitionBlock getTBlock(String uuid) {
    return curNames.getTransitionBlock(uuid);
  }
  
  public static String getBranchVar(String uuid) {
    return curNames.getBranchVar(uuid);
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
    return curNames.getStateBlock(uuid);
  }
  
  public static String getClassNameFor(String type) {
    if (type == null) return "";
    else if (type.equals("int")) return Integer.class.getSimpleName();
    else if (type.equals("long")) return Long.class.getSimpleName();
    else if (type.equals("float")) return Float.class.getSimpleName();
    else if (type.equals("double")) return Double.class.getSimpleName();
    else if (type.equals("boolean")) return Boolean.class.getSimpleName();
    
    return type;
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
  
  public static String parseImports(String code) {
    return expander.parseForImports(code);
  }
  
  public static String expandBody(String body, Boolean addReturn) {
    return expander.expand(body, addReturn);
  }
 
}
