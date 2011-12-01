/*CopyrightHere*/
package repast.simphony.data2.wizard;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.pietschy.wizard.InvalidStateException;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.ui.plugin.editor.ScheduleParamsPanel;
import repast.simphony.util.wizard.ModelAwarePanelStep;

/**
 * @author Jerry Vos
 */
public class DataSetScheduleStep extends ModelAwarePanelStep<DataSetWizardModel> {
	private static final long serialVersionUID = 1L;

	public DataSetScheduleStep() {
		super("Schedule Parameters", "Select when the data should be gathered during the simulation.");
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		scheduleParamsPanel1 = new ScheduleParamsPanel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"pref:grow",
			"top:pref:grow"));
		add(scheduleParamsPanel1, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		setComplete(true);
		
		scheduleParamsPanel1.setScheduleParameters(ScheduleParameters.createRepeating(1.0, 1.0,
            ScheduleParameters.LAST_PRIORITY));
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private ScheduleParamsPanel scheduleParamsPanel1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	@Override
	public void prepare() {
		super.prepare();
		
		if (model.getSchedParams() != null) {
			scheduleParamsPanel1.setScheduleParameters(model.getSchedParams());
		}
	}
	
	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();
		
		model.setSchedParams(scheduleParamsPanel1.createScheduleParameters());
	}
}
