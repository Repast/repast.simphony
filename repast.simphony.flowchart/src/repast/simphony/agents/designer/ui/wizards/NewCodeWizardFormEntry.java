package repast.simphony.agents.designer.ui.wizards;

import org.eclipse.swt.widgets.Text;

public class NewCodeWizardFormEntry extends NewCodeWizardEntry {
	
	public String labelList[] = null;
	public String variableList[] = null;
	public String defaultValueList[] = null;
	public String template = null;
	
	public NewCodeWizardFormEntry(String title, String description, String labelList[], String variableList[], String defaultValueList[], String template) {
		super(title, description);
		this.labelList = labelList;
		this.variableList = variableList;
		this.defaultValueList = defaultValueList;
		this.template = template;
	}
	
	public String getResult(Text textList[]) {
		
		String results = template; 
		for (int i = 0; i < variableList.length; i++) {
			results = results.replace(variableList[i], textList[i].getText().trim());
		}
		return results;
		
	}

}
