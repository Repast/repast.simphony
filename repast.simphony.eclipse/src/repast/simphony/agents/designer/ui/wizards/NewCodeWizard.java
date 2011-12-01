package repast.simphony.agents.designer.ui.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class NewCodeWizard extends Wizard {

	public ArrayList<NewCodeWizardEntry> entryList = new ArrayList<NewCodeWizardEntry>();

	public Map<String, IWizardPage> wizardPageMap = new HashMap<String, IWizardPage>();

	public NewCodeWizardFormPage finalWizardFormPage = null;

	public NewCodeWizardFormPage getFinalWizardFormPage() {
		return finalWizardFormPage;
	}

	public void setFinalWizardFormPage(NewCodeWizardFormPage finalWizardFormPage) {
		this.finalWizardFormPage = finalWizardFormPage;
	}

	public Map<String, IWizardPage> getWizardPageMap() {
		return wizardPageMap;
	}

	public String getResults() {
		return this.finalWizardFormPage.getResults();
	}

	public NewCodeWizard(ArrayList<NewCodeWizardEntry> entryList) {
		this.entryList = entryList;
	}

	/**
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	@Override
	public void addPages() {

		if (this.entryList.size() > 0) {

			for (NewCodeWizardEntry entry : entryList) {
				if (entry instanceof NewCodeWizardRadioButtonEntry) {
					NewCodeWizardRadioButttonPage rbe = new NewCodeWizardRadioButttonPage(
							(NewCodeWizardRadioButtonEntry) entry);
					this.wizardPageMap.put(entry.title, rbe);
					this.addPage(rbe);
				} else if (entry instanceof NewCodeWizardFormEntry) {
					NewCodeWizardFormPage rbe = new NewCodeWizardFormPage(
							(NewCodeWizardFormEntry) entry);
					this.wizardPageMap.put(entry.title, rbe);
					this.addPage(rbe);
				}
			}

		}

	}

	@Override
	public boolean canFinish() {
		if (this.getContainer().getCurrentPage() instanceof NewCodeWizardRadioButttonPage) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean performFinish() {
		return (this.finalWizardFormPage != null);
	}

}
