package repast.simphony.relogo.ide.wizards;


public class NewRelogoProjectWizard extends NetlogoImportWizard {

	public static final String NULL_RELOGO_SOURCE_FILE = "/templates/null.nlogo";
	
	@Override
	public void addPages() {
	
		pageOne = new NetlogoWizardPageOne(true);
		addPage(pageOne);
		pageTwo = new NetlogoWizardPageTwo(pageOne);
		addPage(pageTwo);
	
  }
}