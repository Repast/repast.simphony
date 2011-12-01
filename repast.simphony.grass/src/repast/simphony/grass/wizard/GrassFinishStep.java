/**
 * 
 */
package repast.simphony.grass.wizard;

import javax.swing.JLabel;

import repast.simphony.grass.launch.LaunchScript;

import org.pietschy.wizard.PanelWizardStep;

/**
 * This is the final step in the wizard that takes wizard data
 * and launches Grass.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */
public class GrassFinishStep extends PanelWizardStep {
	private static final long serialVersionUID =1L;

	/**
	 * Default constructor for final launch run script step.
	 */
	public GrassFinishStep() {
		super("Finished", "Grass 6.3 setup finished");
		add(new JLabel("To launch Grass 6.3 execution press finish"));
		
		setComplete(true);

	}
	
	/**
	 *Method to launch script file in LaunchScript object.
	 */
	@SuppressWarnings("unchecked")
	public void fireCommand(){
		String command=SearchForRunFile.HOME_DIR_FIELD.getText();
		if(command!=null && command.length()>0) {
			if(command.endsWith("\\")) {
				command=command.substring(0, command.length()-1);
			}
			LaunchScript ls = new LaunchScript(command);
			ls.launch();
		}
	}
}
