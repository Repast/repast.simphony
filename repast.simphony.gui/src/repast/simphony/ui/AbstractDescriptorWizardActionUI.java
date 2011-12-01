package repast.simphony.ui;

import repast.simphony.engine.controller.DescriptorControllerAction;
import repast.simphony.engine.schedule.Descriptor;
import repast.simphony.ui.plugin.editor.AbstractWizardActionUI;

/**
 * @author Jerry Vos
 */
public abstract class AbstractDescriptorWizardActionUI<T extends Descriptor, U extends DescriptorControllerAction> extends AbstractWizardActionUI<U> {

	public AbstractDescriptorWizardActionUI(U action) {
		super(action, action.getDescriptor().getName(), action.getDescriptor().getName());
	}
}
