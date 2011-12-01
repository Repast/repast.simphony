package repast.simphony.ui.editor;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import repast.simphony.engine.controller.DefaultSchedulableAction;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.ui.plugin.editor.ScheduleParamsPanel;
import repast.simphony.ui.plugin.editor.UISaver;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/06 22:26:52 $
 */
public class SchedulableActionEditor extends UISaver {

	private DefaultSchedulableAction action;
	private ScheduleParamsPanel spPanel;

	public SchedulableActionEditor(DefaultSchedulableAction action) {
		setLayout(new BorderLayout());
		this.action = action;
    JPanel outerLabelPanel = createTitlePanel(action.getDescriptor().getName());
    this.add(outerLabelPanel, BorderLayout.NORTH);

		ScheduleParameters sp = this.action.getDescriptor().getScheduleParameters();
		spPanel = new ScheduleParamsPanel(sp);
		this.add(spPanel, BorderLayout.CENTER);
	}


  public boolean save() {
		ScheduleParameters sp = spPanel.createScheduleParameters();
		if (sp != null) {
			action.getDescriptor().setScheduleParameters(sp);
			return true;
		}

		return false;
	}

	public boolean cancel() {
		System.out.println("cancel");
		return true;
	}

	public String getDialogTitle() {
		return "Schedulable Action Editor";
	}
}
