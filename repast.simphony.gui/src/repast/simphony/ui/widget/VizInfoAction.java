package repast.simphony.ui.widget;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import repast.simphony.visualization.IDisplay;

/**
 * Action that toggles an info probe
 *
 * @author Eric Tatara
 */
public class VizInfoAction extends AbstractAction {

  private IDisplay display;

  public VizInfoAction(IDisplay display) {
    super("Toggle Info Mode");
    this.display = display;
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed(ActionEvent e) {
//    display.toggleInfoProbe();
  }
}
