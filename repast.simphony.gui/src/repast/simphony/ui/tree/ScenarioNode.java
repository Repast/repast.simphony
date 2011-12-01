package repast.simphony.ui.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/06 22:27:26 $
 */
public class ScenarioNode extends DefaultMutableTreeNode {

	private Object contextID;

  public ScenarioNode(ActionUI uiRep, Object contextID) {
    super(uiRep);
		this.contextID = contextID;
  }

  public ScenarioNode(ActionUI uiRep, boolean allowsChildren, Object contextID) {
    super(uiRep, allowsChildren);
		this.contextID = contextID;
  }

  public String toString() {
    return ((ActionUI)userObject).getLabel();
  }

	public ActionUI getUIRepresentation() {
		return (ActionUI)userObject;
	}

	public Object getContextID() {
		return contextID;
	}

}
