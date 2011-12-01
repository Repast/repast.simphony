/*CopyrightHere*/
package repast.simphony.freezedry.wizard;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import org.pietschy.wizard.InvalidStateException;

import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.ui.plugin.editor.ScheduleParamsPanel;
import repast.simphony.util.wizard.ModelAwarePanelStep;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Jerry Vos
 */
public class FreezeDryScheduleStep extends ModelAwarePanelStep<FreezeDryWizardModel> {
	private static final long serialVersionUID = 1L;
	
	public FreezeDryScheduleStep() {
		super("Scheduling Parameters", "Please select when to execute the freeze drying");
		initComponents();
	}

	private void updateEnable(Container container, boolean state) {
		for (Component comp : container.getComponents()) {
			if (comp.isEnabled() == state) {
				// quit early if the first component is already in the state
				// this won't always work but should work in the schedule panel
				return;
			}
			if (comp instanceof Container) {
				updateEnable((Container) comp, state);
			}
			comp.setEnabled(state);
		}
	}
	
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		endActionButton = new JRadioButton();
		normalActionButton = new JRadioButton();
		scheduleParamsPanel1 = new ScheduleParamsPanel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"default:grow",
			"default, default, default"));

		//---- endActionButton ----
		endActionButton.setText("Execute when the schedule finishes (when it stops)");
		endActionButton.setSelected(true);
		add(endActionButton, cc.xy(1, 1));

		//---- normalActionButton ----
		normalActionButton.setText("Execute during the schedule's run");
		add(normalActionButton, cc.xy(1, 2));
		add(scheduleParamsPanel1, cc.xy(1, 3));

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(endActionButton);
		buttonGroup1.add(normalActionButton);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		ItemListener guiUpdater = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateEnable(scheduleParamsPanel1, !endActionButton.isSelected());
			}
		};
		endActionButton.addItemListener(guiUpdater);
		normalActionButton.addItemListener(guiUpdater);
		guiUpdater.itemStateChanged(null);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JRadioButton endActionButton;
	private JRadioButton normalActionButton;
	private ScheduleParamsPanel scheduleParamsPanel1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	@Override
	public void prepare() {
		super.prepare();
		setComplete(true);
		
		ScheduleParameters params = model.getScheduleParams();
		
		if (params != null) {
			if (params.getStart() == ScheduleParameters.END) {
				endActionButton.setSelected(true);
			} else {
				endActionButton.setSelected(false);
				scheduleParamsPanel1.setScheduleParameters(params);
			}
			
		}
	}
	
	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();
		
		if (endActionButton.isSelected()) {
			model.setScheduleParams(ScheduleParameters.createAtEnd(ScheduleParameters.FIRST_PRIORITY));
		} else {
			model.setScheduleParams(scheduleParamsPanel1.createScheduleParameters());			
		}
	}
}
