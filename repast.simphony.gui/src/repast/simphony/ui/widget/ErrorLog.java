package repast.simphony.ui.widget;

import repast.simphony.ui.RSGui;
import saf.core.ui.dock.DockableFrame;
import saf.core.ui.msg.MessagePanel;
import saf.core.ui.msg.MessageTableModel;
import simphony.util.messages.MessageEvent;

import javax.swing.*;

/**
 * Encapsulates an error log.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ErrorLog {

  public static final String LOG_VIEW = ErrorLog.class.getName() + ".view";
  
  private MessageTableModel model = new MessageTableModel();

  private RSGui gui;

  public ErrorLog(RSGui gui) {
    this.gui = gui;
  }
  
  /**
   * Hides the error log.
   */
  public void hide() {
    DockableFrame view = gui.getDockable(LOG_VIEW);
    if (view != null) {
      view.close();
    }
  }

  public void show() {
    DockableFrame view = gui.getDockable(LOG_VIEW);
    if (view == null) {
      gui.addLogView(LOG_VIEW, "Error Log", new MessagePanel(model));
    } else {
      gui.setActiveView(view);
    }
  }

  public void addError(final MessageEvent evt) {
    if (SwingUtilities.isEventDispatchThread()) {
      model.addMessageEvent(evt);
    } else {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          model.addMessageEvent(evt);
        }
      });
    }
  }
}
