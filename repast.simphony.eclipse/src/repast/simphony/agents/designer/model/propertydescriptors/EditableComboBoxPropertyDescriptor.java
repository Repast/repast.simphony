package repast.simphony.agents.designer.model.propertydescriptors;

import java.util.ArrayList;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import repast.simphony.agents.designer.model.propertycelleditors.EditableComboBoxCellEditor;
import repast.simphony.agents.designer.model.propertycelleditors.EditableComboBoxLabelProvider;
import repast.simphony.agents.designer.ui.wizards.NewCodeWizardEntry;

public class EditableComboBoxPropertyDescriptor extends PropertyDescriptor {

	public final static String CHOOSE_USING_TYPE_SELECTOR = "Choose Using Type Selector...";

	public final static String EDIT_USING_TEXT_BOX_DIALOG = "Edit Using Text Box Dialog...";

	protected EditableComboBoxCellEditor editor;

	private String[] listItems;

	protected Composite parent;

	public ArrayList<NewCodeWizardEntry> entryList = null;

	public EditableComboBoxPropertyDescriptor(Object identifier,
			String visibleName, String[] newItemsList,
			ArrayList<NewCodeWizardEntry> entryList) {
		super(identifier, visibleName);
		this.listItems = newItemsList;
		this.entryList = entryList;
	}

	public CellEditor createPropertyEditor(Composite newParent) {
		this.parent = newParent;
		this.editor = new EditableComboBoxCellEditor(this.parent,
				this.listItems, SWT.DROP_DOWN, false, this.entryList);
		if (this.getValidator() != null)
			this.editor.setValidator(getValidator());
		return this.editor;
	}

	public void doSetValue(Object newText) {
		this.editor.doSetValue(newText);
	}

	public ILabelProvider getLabelProvider() {
		if (this.isLabelProviderSet()) {
			return super.getLabelProvider();
		} else {
			return new EditableComboBoxLabelProvider();
		}
	}

	public Composite getParent() {
		return this.parent;
	}

}