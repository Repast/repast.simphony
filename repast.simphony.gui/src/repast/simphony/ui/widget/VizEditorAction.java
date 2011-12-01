package repast.simphony.ui.widget;

import repast.simphony.visualization.DisplayEditorLifecycle;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Actions that runs and stops a DisplayEditor.
 *
 * @author Nick Collier
 */
public class VizEditorAction extends AbstractAction {

  private DisplayEditorLifecycle editor;
  private boolean running = false;

  public VizEditorAction(DisplayEditorLifecycle editor) {
    super("Edit");
    this.editor = editor;
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed(ActionEvent e) {
    if (running) {
      editor.stop();
      running = false;
    } else {
      editor.run();
      running = true;
    }
  }
}
