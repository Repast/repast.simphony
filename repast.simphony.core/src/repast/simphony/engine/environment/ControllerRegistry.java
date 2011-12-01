/*CopyrightHere*/
package repast.simphony.engine.environment;

import repast.simphony.parameter.ParameterSetter;
import repast.simphony.util.collections.Tree;

import java.util.Collection;

/**
 * A registry that holds {@link repast.simphony.engine.environment.ControllerAction}s.
 * 
 * @author Jerry Vos
 */
public interface ControllerRegistry extends Cloneable {
    public static final String DEFAULT_MASTER_CONTEXT_ID = "MasterContextID";
    public static final ControllerAction ACTION_TREE_ROOT = new DefaultControllerAction();

    /**
     * Adds an action to those for the specified context, under the specified parent action.
     *
     * @param parent
     *            the action under which this action occurs, if null, this is a child of the root
     * @param action
     *            the action to add
     */
    void addAction(Object contextId, ControllerAction parent, ControllerAction action);

    /**
     * Removes an action. Since the actions are stored as a tree, this has the side-effect of also
     * removing all actions nested under the given action.
     *
     * @param action
     *            the action to remove
     * @return if the action was removed, false if it was not in the registry
     */
    boolean removeAction(Object contextId, ControllerAction action);

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
    void replaceAction(Object contextId, ControllerAction oldAction, ControllerAction newAction);

    /**
     * Returns the root node for the tree of actions that occur for a specified context type.
     *
     * @param contextTypeID
     *            the type id of the context who's action tree to return
     * @return the root node in the tree of actions
     */
    Tree<ControllerAction> getActionTree(Object contextTypeID);

    Tree<Object> getContextIdTree();

    void addContextId(Object parentId, Object newId);

    void removeContextId(Object id);

    void replaceContextId(Object idToReplace, Object newId);

    void setMasterContextId(Object id);

    Object getMasterContextId();

    String getName();

    void setName(String name);

    /**
     * Registers an action so that it can be found later.
     *
     * @param contextID
     * @param id
     * @param action
     */
    void registerAction(Object contextID, String id, ControllerAction action);

    /**
     * Finds a registered action.
     *
     * @param contextID
     *            the context of the action
     * @param actionID
     *            the id of the action
     * @return the found action
     */
    ControllerAction findAction(Object contextID, String actionID);

    public void addParameterSetter(ParameterSetter paramInit);

    public void removeParameterSetter(ParameterSetter paramInit);

    public Collection<ParameterSetter> getParameterSetters();
    
    public ControllerRegistry clone();
}