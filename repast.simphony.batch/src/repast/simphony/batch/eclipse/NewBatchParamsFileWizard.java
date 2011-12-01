package repast.simphony.batch.eclipse;

import repast.simphony.eclipse.ide.NewRepastXMLFileWizard;

/**
 * New Batch parameters XML file wizard.
 * 
 * @author Eric Tatara
 *
 */
public class NewBatchParamsFileWizard extends NewRepastXMLFileWizard {
	
	private static final String CATALOG_ENTRY_KEY = "http://repast.org/batch/params";
	
	public NewBatchParamsFileWizard(){
		super(CATALOG_ENTRY_KEY);
	}
	
}