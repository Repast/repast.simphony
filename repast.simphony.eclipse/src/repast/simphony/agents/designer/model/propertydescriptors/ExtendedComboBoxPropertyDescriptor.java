package repast.simphony.agents.designer.model.propertydescriptors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;

public class ExtendedComboBoxPropertyDescriptor extends
		ComboBoxPropertyDescriptor {

	public ExtendedComboBoxPropertyDescriptor(Object id, String displayName,
			String[] labelsArray) {
		super(id, displayName, labelsArray);
	}

	public CellEditor createPropertyEditor(Composite container) {
		CellEditor cellEditor = super.createPropertyEditor(container);
		if (cellEditor.getControl() instanceof CCombo) {
			CCombo combo = (CCombo) cellEditor.getControl();
			combo.setVisibleItemCount(Math.min(15, combo.getItemCount()));
		}
		return cellEditor;
	}

}
