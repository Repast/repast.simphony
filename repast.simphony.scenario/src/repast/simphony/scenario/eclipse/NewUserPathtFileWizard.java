package repast.simphony.scenario.eclipse;

import repast.simphony.eclipse.ide.NewRepastXMLFileWizard;

/**
 * New Context XML file wizard.
 * 
 * @author Eric Tatara
 *
 */
public class NewUserPathtFileWizard extends NewRepastXMLFileWizard {
	
	private static final String CATALOG_ENTRY_KEY = "http://repast.org/scenario/user_path";
	
	public NewUserPathtFileWizard(){
		super(CATALOG_ENTRY_KEY);
	}
	
}