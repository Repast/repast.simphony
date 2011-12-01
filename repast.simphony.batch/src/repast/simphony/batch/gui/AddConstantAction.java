/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Action for adding a parameter constant.
 * 
 * @author Nick Collier
 */
public class AddConstantAction extends AbstractAction {
  
  private AbstractBatchParameterNode parent;
  private ParameterEditorMediator mediator;

  /**
   * @param parent
   */
  public AddConstantAction(AbstractBatchParameterNode parent, ParameterEditorMediator mediator) {
    super("Add Constant");
    this.parent = parent;
    this.mediator = mediator;
  }


  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    mediator.addConstant(parent);
  }
}
