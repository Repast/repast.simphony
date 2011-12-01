/**
 * 
 */
package repast.simphony.batch.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

/**
 * Simple label node in a tree.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class LabelNode extends AbstractBatchParameterNode {
  
  private boolean isConstantLabel;
  
  public LabelNode(String label, boolean isConstantLabel) {
    super(label, true);
    this.isConstantLabel = isConstantLabel;
  }

  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.AbstractBatchParameterNode#getMenuActions()
   */
  @Override
  public List<Action> getMenuActions(ParameterEditorMediator mediator) {
    List<Action> actions = new ArrayList<Action>();
    if (isConstantLabel) actions.add(new AddConstantAction(this, mediator));
    else actions.add(new AddParameterAction(this, mediator));
    return actions;
  }

  @Override
  public boolean isBeanNode() {
    return false;
  }
}
