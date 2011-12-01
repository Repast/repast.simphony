package repast.simphony.agents.designer.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import repast.simphony.agents.designer.ui.AgentBuilderPluginImages;

public class NewCodeWizardFormPage extends WizardPage implements IWizardPage {

	public NewCodeWizardFormEntry newCodeWizardFormEntry;

	public Text textList[];

	public String results = "";

	public NewCodeWizardFormPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public NewCodeWizardFormPage(String pageName) {
		super(pageName);
	}

	public NewCodeWizardFormPage(NewCodeWizardFormEntry newCodeWizardFormEntry) {
		super(newCodeWizardFormEntry.title, newCodeWizardFormEntry.title,
				AgentBuilderPluginImages.DESC_WIZBAN_NewAgent);
		this.setDescription(newCodeWizardFormEntry.description);
		this.newCodeWizardFormEntry = newCodeWizardFormEntry;
	}

	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	public void createControl(Composite parent) {

		// create the composite to hold the widgets
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 8;
		composite.setLayout(gridLayout);

		try {
			if (newCodeWizardFormEntry.labelList.length > 0) {
				textList = new Text[newCodeWizardFormEntry.labelList.length];
				for (int i = 0; i < newCodeWizardFormEntry.labelList.length; i++) {

					Label label = new Label(composite, SWT.NULL);
					label.setText(newCodeWizardFormEntry.labelList[i] + ": ");

					textList[i] = new Text(composite, SWT.SINGLE | SWT.BORDER);
					GridData gridData = new GridData(
							GridData.HORIZONTAL_ALIGN_FILL);
					gridData.horizontalSpan = 1;
					textList[i].setLayoutData(gridData);

					if (newCodeWizardFormEntry.defaultValueList.length > i) {
						textList[i]
								.setText("                                                                                   "
										+ newCodeWizardFormEntry.defaultValueList[i]);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (this.newCodeWizardFormEntry == null) {
				System.out
						.println("Error Creating a Wizard Form Page, newCodeWizardFormEntry was null");
			} else {
				System.out
						.println("Error Creating the Wizard Form Page Titled \""
								+ this.newCodeWizardFormEntry.title + "\"");
			}
		}

		this.setControl(composite);

	}

	@Override
	public void dispose() {
		this.results = this.newCodeWizardFormEntry.getResult(textList);
		super.dispose();
	}

	public String getResults() {
		return this.results;
	}
}
