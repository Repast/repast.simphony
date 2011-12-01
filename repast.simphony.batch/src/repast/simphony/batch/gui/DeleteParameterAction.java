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
public class DeleteParameterAction extends AbstractAction {
  
  private AbstractBatchParameterNode node;
  private ParameterEditorMediator mediator;

  /**
   * @param parent
   */
  public DeleteParameterAction(AbstractBatchParameterNode node, ParameterEditorMediator mediator) {
    super("Delete Parameter");
    this.node = node;
    this.mediator = mediator;
  }
  
  public void setNode(AbstractBatchParameterNode node) {
    this.node = node;
  }

  public void actionPerformed(ActionEvent e) {
    mediator.deleteNode(node);
  }
}
