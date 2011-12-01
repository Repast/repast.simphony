/*CopyrightHere*/
package repast.simphony.engine.controller;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.util.collections.NaryTree;
import repast.simphony.util.collections.Tree;

import java.util.*;

/**
 * Basic implementation of a {@link repast.simphony.engine.environment.ControllerRegistry}.
 * 
 * @author Jerry Vos
 */
public class DefaultControllerRegistry implements ControllerRegistry {
    // private static final Logger l4jLogger = InternalLogManager
    // .getLogger(SimpleControllerRegistry.class);

    // key: context type id, value is the tree of actions to be executed
    // against contexts of that type
    private Map<Object, Tree<ControllerAction>> contextActionMap;

    // tree of context types
    private Tree<Object> contextTree;

    private Object masterContextId;

    private String name = "";

    private Map<Object, Map<String, ControllerAction>> actionMap = new HashMap<Object, Map<String, ControllerAction>>();

    protected ArrayList<ParameterSetter> paramSetters;


    /**
     * Constructs the SimpleControllerRegistry with no actions.
     */
    public DefaultControllerRegistry() {
        super();

        this.masterContextId = DEFAULT_MASTER_CONTEXT_ID;
        this.contextActionMap = new HashMap<Object, Tree<ControllerAction>>();
        this.contextTree = new NaryTree<Object>(masterContextId);
        this.paramSetters = new ArrayList<ParameterSetter>();
    }
    
    public ControllerRegistry clone(){
           try {
                return (ControllerRegistry) super.clone();
            }
            catch (CloneNotSupportedException e) {
                // This should never happen
                throw new InternalError(e.toString());
            }
        
    }

    /**
     * Adds an action to those for the specified context, under the specified parent action. If the
     * specified parent is null then the parent is assumed to be the root of the tree. Also, if the
     * specified context id has not already been added to the controller, it will be added as a
     * child of the top level context.
     *
     * @param contextId
     *            the id of the context this applies to
     * @param parent
     *            the action under which this action occurs, can be null
     * @param action
     *            the action to add
     */
    public void addAction(final Object contextId, ControllerAction parent, ControllerAction action) {
        if (parent == null) {
            parent = ControllerRegistry.ACTION_TREE_ROOT;
        }

        Tree<ControllerAction> tree = getActionTree(contextId);
        if (tree == null) {
            tree = new NaryTree<ControllerAction>(ControllerRegistry.ACTION_TREE_ROOT);
            contextActionMap.put(contextId, tree);
        }
        tree.addNode(parent, action);
    }

    public Tree<ControllerAction> getActionTree(Object contextId) {
        return contextActionMap.get(contextId);
    }

    /**
     * Removes an action. Since the actions are stored as a tree, this has the side-effect of also
     * removing all actions nested under the given action. If this behavior is not wanted use the
     * {@link #replaceAction(ControllerAction, ControllerAction)} method with a
     * {@link DefaultControllerAction}.<p/>
     *
     * @see DefaultControllerAction
     *
     * @param action the action to remove
     *
     * @return true if the action was removed, false if it was not in the registry
     */
    public boolean removeAction(Object contextId, ControllerAction action) {
        Tree<ControllerAction> tree = getActionTree(contextId);
        if (tree != null) {
            removeActionFromMap(contextId, action);
            return tree.removeNode(action);
        }
        return false;
    }

    private void removeActionFromMap(Object contextID, ControllerAction action) {
        Map<String, ControllerAction> actions = actionMap.get(contextID);
        if (actions != null) {
            for (String key : actions.keySet()) {
                if (actions.get(key).equals(action)) {
                    actions.remove(key);
                    break;
                }
            }
        }
    }

    /**
     * Replaces a given action with the specified action. This will result in an action tree with
     * the old action replaced with the new one. Any actions nested under the old action will now be
     * nested under the new action.<p/>
     *
     * If the new action is a {@link repast.simphony.engine.environment.DefaultControllerAction} this is the same as
     * removing the given action without removing its children.<p/>
     *
     * This will not accept null as an argument, as it would not make sense as either an old action
     * or as the new one that is replacing it. If null is received an
     * {@link java.security.InvalidParameterException} will be thrown.
     *
     * @param oldAction
     *            the action to replace
     * @param newAction
     *            the action with which to replace the old one
     */
    public void replaceAction(Object contextId, ControllerAction oldAction,
                              ControllerAction newAction) {
        getActionTree(contextId).replaceNode(oldAction, newAction);
        Map<String, ControllerAction> actions = actionMap.get(contextId);
        if (actions != null) {
            for (String key : actions.keySet()) {
                if (actions.get(key).equals(oldAction)) {
                    actions.remove(key);
                    actions.put(key, newAction);
                    break;
                }
            }
        }
    }

    public Tree<Object> getContextIdTree() {
        return this.contextTree;
    }

    public void addContextId(Object parentId, Object newId) {
        this.contextTree.addNode(parentId, newId);
    }

    public void removeContextId(Object id) {
        this.contextTree.removeNode(id);
        actionMap.remove(id);
    }

    public void setMasterContextId(Object id) {
        replaceContextId(masterContextId, id);
        Map<String, ControllerAction> actions = actionMap.remove(masterContextId);
        if (actions != null)
            actionMap.put(id, actions);
        this.masterContextId = id;
    }

    public Object getMasterContextId() {
        return masterContextId;
    }

    public void replaceContextId(Object idToReplace, Object newId) {
        this.contextTree.replaceNode(idToReplace, newId);
        Map<String, ControllerAction> actions = actionMap.remove(idToReplace);
        if (actions != null)
            actionMap.put(newId, actions);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void registerAction(Object contextID, String id, ControllerAction action) {
        Map<String, ControllerAction> actions = actionMap.get(contextID);
        if (actions == null) {
            actions = new HashMap<String, ControllerAction>();
            actionMap.put(contextID, actions);
        }
        actions.put(id, action);
    }

    public ControllerAction findAction(Object contextID, String actionID) {
        Map<String, ControllerAction> actions = actionMap.get(contextID);
        if (actions == null)
            return null;

        return actions.get(actionID);
    }

    public void addParameterSetter(ParameterSetter paramInit) {
        paramSetters.add(paramInit);
    }

    public void removeParameterSetter(ParameterSetter paramInit) {
        paramSetters.remove(paramInit);
    }

    public Collection<ParameterSetter> getParameterSetters() {
        return Collections.unmodifiableCollection(paramSetters);
    }
}
