/**
 * 
 */
package repast.simphony.batch.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

/**
 * Tree node that contains a batch parameter.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class BatchParameterNode extends AbstractBatchParameterNode {
  
  public BatchParameterNode(BatchParameterBean bean, boolean allowsChildren) {
    super(bean, allowsChildren);
  }

  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.AbstractBatchParameterNode#getMenuActions()
   */
  @Override
  public List<Action> getMenuActions(ParameterEditorMediator mediator) {
    List<Action> actions = new ArrayList<Action>();
    actions.add(new AddParameterAction(this, mediator));
    actions.add(new DeleteParameterAction(this, mediator));
    if (getChildCount() > 0) {
      actions.add(new DeleteParameterTreeAction(this, mediator));
    }
    
    return actions;
  }
  
  public String toString() {
    return ((BatchParameterBean)userObject).getName();
  }

  @Override
  public boolean isBeanNode() {
    return true;
  }
  
  

}
