package repast.simphony.agents.designer.ui.actions;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import repast.simphony.agents.designer.editparts.AgentPropertyorStepLabelEditPart;

public class SelectionCountingAction extends SelectionAction {
	
	public final static String ACTION_SELECTION_COUNTING = "selection_counting";
	
	public static List objects = null;
	
	@Override
	protected void handleSelectionChanged() {
		super.handleSelectionChanged();
		objects = getSelectedObjects();
		if (objects == null) {
			count = 0;
			labels = false;
		} else {
			count = objects.size();
			if (count == 1) {
				labels = true;
			} else if (count > 0) {
				labels = true;
				for (Object object : objects) {
					if (!(object instanceof AgentPropertyorStepLabelEditPart)) {
						labels = false;
					}
				}
			} else {
				labels = false;
			}
		}
	}

	public int count = 0;
	public boolean labels = false;

	public int getCount() {
		return count;
	}
	
	public boolean labelsCanMove() {
		return labels;
	}

	public List getCurrentlySelectedObjects() {
		return objects;
	}
	
	public SelectionCountingAction(IWorkbenchPart part) {
		super(part);
		setText("Selection counting");
		setId(ACTION_SELECTION_COUNTING);
	}

	protected boolean calculateEnabled() {
		return true;
	}

	public void run() {
	}

}
