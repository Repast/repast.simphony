/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * @author Nick Collier
 */
public class AddParameterAction extends AbstractAction {
  
  private AbstractBatchParameterNode parent;
  private ParameterEditorMediator mediator;

  /**
   * @param parent
   */
  public AddParameterAction(AbstractBatchParameterNode parent, ParameterEditorMediator mediator) {
    super("Add Parameter");
    this.parent = parent;
    this.mediator = mediator;
  }
  
  public void setParent(AbstractBatchParameterNode parent) {
    this.parent = parent;
  }

  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    mediator.addParameter(parent);
  }

}
