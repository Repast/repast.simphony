package repast.simphony.agents.designer.model.propertycelleditors;

import org.eclipse.jface.viewers.LabelProvider;

public class EditableComboBoxLabelProvider extends LabelProvider {

	public EditableComboBoxLabelProvider() {
	}

	public String getText(Object element) {
		if (element == null) {
			return "";
		} else if (element instanceof String) {
			return (String) element;
		} else {
			return element.toString();
		}
	}

}