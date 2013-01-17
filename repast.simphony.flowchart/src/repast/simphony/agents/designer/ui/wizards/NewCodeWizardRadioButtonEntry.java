package repast.simphony.agents.designer.ui.wizards;

public class NewCodeWizardRadioButtonEntry extends NewCodeWizardEntry {
	
	public String optionList[] = null;
	public String nextPageList[] = null;
	
	public NewCodeWizardRadioButtonEntry(String title, String description, String optionList[], String nextPageList[]) {
		super(title, description);
		this.optionList = optionList;
		this.nextPageList = nextPageList;
		if (this.nextPageList == null) this.nextPageList = optionList;
	}

}
