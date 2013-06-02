package repast.simphony.relogo.ide.wizards;

import repast.simphony.relogo.ide.handlers.ReLogoBuilder;

public class NewReLogoEntityWizardUtils {
	private static final String RELOGOTURTLE = "ReLogoTurtle";
	private static final String RELOGOPATCH = "ReLogoPatch";
	private static final String RELOGOLINK = "ReLogoLink";
	private static final String RELOGOOBSERVER = "ReLogoObserver";
	
	public enum ReLogoEntityType {
		RLT(RELOGOTURTLE),RLP(RELOGOPATCH),RLL(RELOGOLINK),RLO(RELOGOOBSERVER);
		private String className;
		private ReLogoEntityType(String className){
			this.className = className;
		}
		public String getClassName(){
			return className;
		}
	}
	
	/**
	 * Checks the package name in the new ReLogo entity wizard and sets the super class
	 * accordingly. Returns true if the package name is correctly a relogo package.
	 * @param page
	 * @return 
	 */
	protected static boolean setReLogoSuperClass(org.eclipse.jdt.ui.wizards.NewClassWizardPage page, ReLogoEntityType reLogoEntityType){
	// check to see if in ReLogo package
			String packageName = page.getPackageText();
			String instrumentingPackageName = ReLogoBuilder.getInstrumentingPackageName(packageName);

			if (instrumentingPackageName != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(instrumentingPackageName);
				if (!instrumentingPackageName.isEmpty()) {
					sb.append(".");
				}
				sb.append(reLogoEntityType.getClassName());
				page.setSuperClass(sb.toString(), false);
				return true;
			} else {
				page.setSuperClass("", false);
				return false;
			}
	}
}
