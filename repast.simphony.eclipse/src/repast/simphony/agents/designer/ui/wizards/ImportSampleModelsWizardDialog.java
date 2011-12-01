package repast.simphony.agents.designer.ui.wizards;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.wizards.datatransfer.WizardProjectsImportPage;

public class ImportSampleModelsWizardDialog extends WizardDialog {

	public ImportSampleModelsWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
	}
	
	
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		WizardProjectsImportPage page = (WizardProjectsImportPage)getWizard().getPage("wizardExternalProjectsPage");
		page.getProjectsList().setCheckedElements(new Object[]{});
		page.setPageComplete(false);
		return contents;
	}

}
