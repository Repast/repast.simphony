/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Action for deleting a parameter.
 * 
 * @author Nick Collier
 */
public class DeleteParameterTreeAction extends AbstractAction {
  
  private AbstractBatchParameterNode node;
  private ParameterEditorMediator mediator;

  /**
   * @param parent
   */
  public DeleteParameterTreeAction(AbstractBatchParameterNode node, ParameterEditorMediator mediator) {
    super("Delete Parameter & Children");
    this.node = node;
    this.mediator = mediator;
  }

  public void actionPerformed(ActionEvent e) {
    mediator.deleteNodes(node);
  }
}
