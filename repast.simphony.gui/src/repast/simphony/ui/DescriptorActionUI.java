package repast.simphony.ui;

import javax.swing.JPopupMenu;

import repast.simphony.engine.schedule.Descriptor;
import repast.simphony.ui.plugin.ActionUI;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:27:26 $
 */
public class DescriptorActionUI<T extends Descriptor> implements ActionUI {

	private T descriptor;

	public DescriptorActionUI(T descriptor) {
		this.descriptor = descriptor;
	}

	public String getLabel() {
		return descriptor.getName();
	}

	/**
	 * Returns null indicating the this is not editable, or has nothing interesting to display.
	 *
	 * @param evt
	 * @return null indicating the this is not editable, or has nothing interesting to display
	 */
	public Editor getEditor(ScenarioTreeEvent evt) {
		return null;
	}

	public JPopupMenu getPopupMenu(ScenarioTreeEvent evt) {
		return null;
	}

	/*
	protected List<DataGathererDescriptor<?>> getDataGathererDescriptors(ControllerRegistry reg, Object contextId) {
		Vector<DataGathererDescriptor<?>> descriptors = new Vector<DataGathererDescriptor<?>>();

		for (ControllerAction action : reg.getActionTree(contextId)
						.getChildren(reg.findAction(contextId, ControllerActionConstants.DATA_SET_ROOT))) {

			if (action instanceof DefaultDataGathererDescriptorAction) {
				descriptors.add(((DefaultDataGathererDescriptorAction) action).getDescriptor());
			}
		}

		return descriptors;
	}
	*/
}
