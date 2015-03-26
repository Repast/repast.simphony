package repast.simphony.ui.plugin.editor;

import javax.swing.JComponent;

import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardModel;

/**
 * Wizard for editors.  Overriding the default methods provides some customization.
 * 
 * @author Eric Tatara
 *
 */
public class PluginWizard extends Wizard {
	private static final long serialVersionUID = 2246585500811974788L;
	
	public PluginWizard(WizardModel model) {
		super(model);
	}
	
	@Override
	protected JComponent createTitleComponent(){
		return new TitleComponent(this);
  }	
}