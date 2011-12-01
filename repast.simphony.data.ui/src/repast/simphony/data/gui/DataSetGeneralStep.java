/*CopyrightHere*/
package repast.simphony.data.gui;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

import org.pietschy.wizard.InvalidStateException;

import repast.simphony.util.wizard.ModelAwarePanelStep;

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
public class DataSetGeneralStep extends ModelAwarePanelStep<DataSetWizardModel> {
	private static final long serialVersionUID = -2791347649258399230L;
	
	public DataSetGeneralStep() {
		super("General Settings", "Enter general settings for the Data Set.");
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label2 = new JLabel();
		dataSetField = new JTextField();
		label3 = new JLabel();
		agentClassBox = new JComboBox();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- label2 ----
		label2.setText("Data Set ID:");
		add(label2, cc.xy(1, 3));

		//---- dataSetField ----
		dataSetField.setText("Data Set 1");
		add(dataSetField, cc.xy(3, 3));

		//---- label3 ----
		label3.setText("Agent Class:");
		add(label3, cc.xy(1, 5));
		add(agentClassBox, cc.xy(3, 5));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		addCompleteListener(dataSetField);
		addCompleteListener(agentClassBox);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label2;
	private JTextField dataSetField;
	private JLabel label3;
	private JComboBox agentClassBox;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	
	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();
		
		model.setActionName(dataSetField.getText());
		model.setDataSetId(dataSetField.getText());
		model.setAgentClass((Class<?>) agentClassBox.getSelectedItem());
	}
	
	@SuppressWarnings("serial")
	@Override
	public void prepare() {
		super.prepare();

		agentClassBox.setModel(new DefaultComboBoxModel(model.getAgentClasses().toArray()));
		agentClassBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				return super.getListCellRendererComponent(list, ((Class<?>) value).getSimpleName(), index, isSelected, cellHasFocus);
			}
		});
		
		if (model.getDataSetId() != null) {
			dataSetField.setText(model.getDataSetId().toString());
		}
		if (model.getAgentClass() != null) {
			agentClassBox.setSelectedItem(model.getAgentClass());
		}
		updateComplete();
	}
	
	@Override
	protected void updateComplete() {
		setComplete(agentClassBox.getSelectedIndex() >= 0 && !dataSetField.getText().equals(""));
	}
}
