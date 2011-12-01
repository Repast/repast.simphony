package repast.simphony.visualization.editor;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.visualization.IDisplay;

/**
 * Manages the coordination between DisplayEditors such that
 * edits performed by one are reflected in the other displays.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class EditorNotifier {

  private List<IDisplay> displays = new ArrayList<IDisplay>();
  private List<DisplayEditor2D> editors = new ArrayList<DisplayEditor2D>();

  /**
   * Adds the specified display to the list of displays to updated
   * due to an editor event.
   *
   * @param display the display to add
   */
  public void addDisplay(IDisplay display) {
    displays.add(display);
  }

  /**
   * Adds the specified editor to the list of editors to be updated
   * due to an editor event.
   *
   * @param editor the editor to add
   */
  public void addEditor(DisplayEditor2D editor) {
    editors.add(editor);
  }

  /**
   * Resets the editor manager prior to another simulation run.
   */
  public void reset() {
    displays.clear();
    editors.clear();
  }

  /*
  public void agentAdded(Object agent, Object source) {
    for (DisplayEditor2D editor : editors) {
      editor.agentAdded(new EditorEvent(source, agent));
    }
  }

  public void agentRemoved(Object agent, Object source) {
    for (DisplayEditor2D editor : editors) {
      editor.agentRemoved(new EditorEvent(source, agent));
    }
  }
  */

  /**
   * Invoked when some editor event occurs that should
   * be reflected in other displays. For example,
   * if an agent is added.
   */
  public void editorEventOccurred() {
    for (IDisplay display : displays) {
      display.update();
      display.render();
    }
  }
}
