package repast.simphony.ui.editor;

import repast.simphony.engine.controller.DefaultSchedulableAction;
import repast.simphony.engine.schedule.SchedulableDescriptor;
import repast.simphony.ui.DescriptorActionUI;
import repast.simphony.ui.plugin.editor.DefaultEditorDialog;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.plugin.editor.UISaver;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:27:26 $
 */
public class SchedulableActionActionUI extends DescriptorActionUI<SchedulableDescriptor> {

	private DefaultSchedulableAction action;

	public SchedulableActionActionUI(DefaultSchedulableAction action) {
		super(action.getDescriptor());
		this.action = action;
	}

	public Editor getEditor(ScenarioTreeEvent evt) {
		UISaver saver = new SchedulableActionEditor(action);
		return new DefaultEditorDialog(saver);
	}
}
