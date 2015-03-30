package repast.simphony.visualization.gui;

import javax.swing.JPanel;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.ui.plugin.editor.PluginWizardStep;
import repast.simphony.ui.plugin.editor.ScheduleParamsPanel;
import repast.simphony.visualization.engine.DisplayDescriptor;

/**
 * @author Nick Collier
 */
public class ScheduleStep extends PluginWizardStep {
	private static final long serialVersionUID = 3551311107446307848L;
	
	private DisplayWizardModel model;
	private ScheduleParamsPanel spPanel;

	public ScheduleStep() {
		super("Schedule Details", "Please enter the update schedule start time, priority, frequency and interval " +
						"for this display");
		
		setComplete(true);
	}

	@Override
	protected JPanel getContentPanel(){
		spPanel = new ScheduleParamsPanel(ScheduleParameters.createRepeating(1, 1, ScheduleParameters.LAST_PRIORITY),
						false);

		return spPanel;
	}

	@Override
	public void init(WizardModel wizardModel) {
		this.model = (DisplayWizardModel)wizardModel;
	}

	@Override
	public void applyState() throws InvalidStateException {
		ScheduleParameters params = spPanel.createScheduleParameters();
		DisplayDescriptor descriptor = model.getDescriptor();
		descriptor.setScheduleParameters(params);
	}

	@Override
	public void prepare() {
		DisplayDescriptor descriptor = model.getDescriptor();
		ScheduleParameters sp = descriptor.getScheduleParameters();
		spPanel.setScheduleParameters(sp);
	}
}
