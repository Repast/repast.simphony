/*CopyrightHere*/
package repast.simphony.data.gui;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import org.apache.commons.collections15.Predicate;
import org.pietschy.wizard.InvalidStateException;

import repast.simphony.util.wizard.ModelAwarePanelStep;
import repast.simphony.util.PredicateFiltered;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author Jerry Vos
 */
public class MethodMappingStep extends ModelAwarePanelStep<DataMappingWizardModel> implements
        PredicateFiltered<Method> {
	private static final long serialVersionUID = -2955047415395323479L;

	public static Set<String> badMethods = new HashSet<String>();
	
	protected DefaultComboBoxModel methodModel;
	
	protected Predicate<Method> methodFilter;
	
	static {
		MethodMappingStep.badMethods.add("getClass");
		MethodMappingStep.badMethods.add("hashCode");
	}
	
	
	public MethodMappingStep() {
		super("Method", "Select the method to call to retrieve the data value from the agent.");
		initComponents();
	}
	
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		methodBox = new JComboBox();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			},
			RowSpec.decodeSpecs("default")));

		//---- label1 ----
		label1.setText("Method Name");
		add(label1, cc.xy(1, 1));
		add(methodBox, cc.xy(3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		methodModel = new DefaultComboBoxModel();
		methodBox.setModel(methodModel);
		methodBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				if (value instanceof Method) {
					super.getListCellRendererComponent(list, ((Method) value).getName(), index,
							isSelected, cellHasFocus);
				}
				return super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
			}
		});
		
		addCompleteListener(methodBox);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	private JComboBox methodBox;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	@Override
	public void prepare() {
		super.prepare();
		
		methodModel.removeAllElements();
		
		Method[] methods = model.getAgentClass().getMethods();
		for (Method method : methods) {
			if (method.getParameterTypes().length == 0
					&& !method.getReturnType().equals(void.class)
					&& !badMethods.contains(method.getName())
					&& (methodFilter == null || methodFilter.evaluate(method))) {
				methodModel.addElement(method);
			}
		}
		
		if (model.getMappingRepresentation() instanceof MethodSourceRep) {
			Method method = ((MethodSourceRep) model.getMappingRepresentation()).getMethod();
			methodBox.setSelectedItem(method);
		}
	}
	
	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();
		
		model.setMappingRepresentation(new MethodSourceRep((Method) methodModel.getSelectedItem()));
	}
	
	@Override
	protected void updateComplete() {
		setComplete(methodBox.getSelectedIndex() >= 0);
	}

	public Predicate<Method> getFilter() {
		return methodFilter;
	}

	public void setFilter(Predicate<Method> filter) {
		this.methodFilter = filter;
	}
}
