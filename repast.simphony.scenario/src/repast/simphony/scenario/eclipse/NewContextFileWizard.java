package repast.simphony.scenario.eclipse;

import repast.simphony.eclipse.ide.NewRepastXMLFileWizard;

/**
 * New Context XML file wizard.
 * 
 * @author Eric Tatara
 *
 */
public class NewContextFileWizard extends NewRepastXMLFileWizard {
	
	private static final String CATALOG_ENTRY_KEY = "http://repast.org/scenario/context";
	
	public NewContextFileWizard(){
		super(CATALOG_ENTRY_KEY);
	}
	
}