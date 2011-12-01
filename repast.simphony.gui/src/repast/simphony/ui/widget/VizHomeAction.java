package repast.simphony.ui.widget;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import repast.simphony.visualization.IDisplay;

/**
 * Action that resets a displays home view
 *
 * @author Eric Tatara
 */
public class VizHomeAction extends AbstractAction {

  private IDisplay display;
  

  public VizHomeAction(IDisplay display) {
    super("Home");
    this.display = display;
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed(ActionEvent e) {
    display.resetHomeView();
  }
}
