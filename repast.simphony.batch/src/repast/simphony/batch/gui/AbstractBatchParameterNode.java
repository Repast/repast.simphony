/**
 * 
 */
package repast.simphony.batch.gui;

import java.util.List;

import javax.swing.Action;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Abstract tree node in a batch parameter tree.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public abstract class AbstractBatchParameterNode extends DefaultMutableTreeNode {
  
  /**
   * @param userObject
   * @param allowsChildren
   */
  public AbstractBatchParameterNode(Object userObject, boolean allowsChildren) {
    super(userObject, allowsChildren);
  }

  /**
   * @param userObject
   */
  public AbstractBatchParameterNode(Object userObject) {
    super(userObject);
  }

  /**
   * Gets the list of menu actions applicable to this node.
   * 
   * @return the list of menu actions applicable to this node.
   */
  public abstract List<Action> getMenuActions(ParameterEditorMediator mediator);
  
  public abstract boolean isBeanNode();
  

}
