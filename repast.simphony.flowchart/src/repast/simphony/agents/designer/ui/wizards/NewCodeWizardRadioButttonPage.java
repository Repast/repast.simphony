package repast.simphony.agents.designer.ui.wizards;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import repast.simphony.agents.designer.ui.AgentBuilderPluginImages;

public class NewCodeWizardRadioButttonPage extends WizardPage implements
		IWizardPage {

	NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntry = null;

	Button buttonList[] = null;

	public NewCodeWizardRadioButttonPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public NewCodeWizardRadioButttonPage(String pageName) {
		super(pageName);
	}

	public NewCodeWizardRadioButttonPage(
			NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntry) {
		super(newCodeWizardRadioButtonEntry.title,
				newCodeWizardRadioButtonEntry.title,
				AgentBuilderPluginImages.DESC_WIZBAN_NewAgent);
		this.setDescription(newCodeWizardRadioButtonEntry.description);
		this.newCodeWizardRadioButtonEntry = newCodeWizardRadioButtonEntry;
	}

	@Override
	public IWizardPage getNextPage() {

		int i = 0;
		for (; i < newCodeWizardRadioButtonEntry.optionList.length; i++) {
			if (buttonList[i].getSelection()) {
				break;
			}
		}
		if ((0 <= i) && (i < newCodeWizardRadioButtonEntry.optionList.length)) {

			NewCodeWizard codeWizard = ((NewCodeWizard) this.getWizard());

			IWizardPage nextPage = codeWizard.getWizardPageMap().get(
					newCodeWizardRadioButtonEntry.nextPageList[i]);
			if (nextPage != null) {
				if (nextPage instanceof NewCodeWizardFormPage) {
					codeWizard
							.setFinalWizardFormPage((NewCodeWizardFormPage) nextPage);
				} else {
					codeWizard.setFinalWizardFormPage(null);
				}
				return nextPage;
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	@Override
	public boolean canFlipToNextPage() {
		return true;
	}

	public void createControl(Composite parent) {

		// create the composite to hold the widgets
		Composite composite = new Composite(parent, SWT.NONE);

		composite.setLayout(new FillLayout(SWT.VERTICAL));

		try {
			if (newCodeWizardRadioButtonEntry.optionList.length > 0) {

				buttonList = new Button[newCodeWizardRadioButtonEntry.optionList.length];
				for (int i = 0; i < newCodeWizardRadioButtonEntry.optionList.length; i++) {

					buttonList[i] = new Button(composite, SWT.RADIO);
					buttonList[i]
							.setText(newCodeWizardRadioButtonEntry.optionList[i]);

				}
				buttonList[0].setSelection(true);

			}
		} catch (Exception e) {
			e.printStackTrace();
			if (this.newCodeWizardRadioButtonEntry == null) {
				System.out
						.println("Error Creating a Wizard Form Page, newCodeWizardFormEntry was null");
			} else {
				System.out
						.println("Error Creating the Wizard Form Page Titled \""
								+ this.newCodeWizardRadioButtonEntry.title + "\"");
			}
		}
		this.setControl(composite);

	}

}
