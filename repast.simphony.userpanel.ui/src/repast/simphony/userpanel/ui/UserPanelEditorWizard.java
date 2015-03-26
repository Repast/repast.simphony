package repast.simphony.userpanel.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardStep;

import repast.simphony.ui.plugin.editor.PluginWizard;

public class UserPanelEditorWizard {
	protected UserPanelWizardModel model;

	protected Wizard wizard;

	private ArrayList<PanelWizardStep> steps;

	public UserPanelEditorWizard(List<Class<?>> clazzes) {
		this(clazzes, null);
	}

	public UserPanelEditorWizard(List<Class<?>> clazzes,
			UserPanelDescriptor descriptor) {
		Collections.sort(clazzes, new Comparator<Class<?>>() {
			public int compare(Class<?> o1, Class<?> o2) {
				return o1.getSimpleName().compareTo(o2.getSimpleName());
			}
		});

		if (descriptor != null) {
			model = new UserPanelWizardModel(clazzes, descriptor);
		} else {
			model = new UserPanelWizardModel(clazzes);
		}
		buildPath();
		wizard = new PluginWizard(model);
		wizard.setOverviewVisible(false);
		wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
		model.setLastVisible(false);
	}

	private void buildPath() {
		steps = new ArrayList<PanelWizardStep>();
		steps.add(new UserPanelStep());

		for (WizardStep step : steps) {
			model.add(step);
		}
	}

	public void showDialog(Component component, String title) {
		wizard.showInDialog(title, component, true);
	}

	public UserPanelWizardModel getModel() {
		return model;
	}

	public boolean wasCancelled() {
		if (wizard == null) {
			return true;
		} else {
			return wizard.wasCanceled();
		}
	}

	public Wizard getWizard() {
		return wizard;
	}

	public Collection<PanelWizardStep> getSteps() {
		return steps;
	}

	public static void main(String[] args) {
		UserPanelEditorWizard wiz = new UserPanelEditorWizard(
				new ArrayList<Class<?>>());

		wiz.showDialog(null, "");
	}
}
