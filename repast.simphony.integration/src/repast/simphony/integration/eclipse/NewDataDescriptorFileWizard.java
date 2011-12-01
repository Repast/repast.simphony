package repast.simphony.integration.eclipse;

import repast.simphony.eclipse.ide.NewRepastXMLFileWizard;



/**
 * New DataDescriptor XML file wizard.
 * 
 * @author Eric Tatara
 *
 */
public class NewDataDescriptorFileWizard extends NewRepastXMLFileWizard {
	
	private static final String CATALOG_ENTRY_KEY = "http://repast.org/integration/datadescriptor";
	
	public NewDataDescriptorFileWizard(){
		super(CATALOG_ENTRY_KEY);
	}
	
}