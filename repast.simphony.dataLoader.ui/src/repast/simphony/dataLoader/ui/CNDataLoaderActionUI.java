package repast.simphony.dataLoader.ui;

import javax.swing.JPopupMenu;

import repast.simphony.dataLoader.engine.ClassNameDataLoaderAction;
import repast.simphony.ui.plugin.ActionUI;
import repast.simphony.ui.plugin.editor.DefaultEditorDialog;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * ActionUI for working with the class name data loader action.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class CNDataLoaderActionUI implements ActionUI {

  private ClassNameDataLoaderAction action;

  public CNDataLoaderActionUI(ClassNameDataLoaderAction action) {
    this.action = action;
  }

  public Editor getEditor(ScenarioTreeEvent evt) {
    CNDataLoaderActionPanel panel = new CNDataLoaderActionPanel(action, evt.getScenario().getContext().getClasspath());
    return new DefaultEditorDialog(panel);
  }

  public String getLabel() {
    String label = action.getBuilder().getClassName();
    label = label.substring(label.lastIndexOf(".") + 1, label.length());
    return label;
  }

  public JPopupMenu getPopupMenu(ScenarioTreeEvent evt) {
    return null;
  }
}
