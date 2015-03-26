package repast.simphony.ui.plugin.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.pietschy.wizard.PanelWizardStep;

/**
 * Plugin step for wizards that provides some customization.  All plugin wizards
 *   should extend this class to ensure uniformity across UI wizards.
 * 
 * @author Eric Tatara
 *
 */
public abstract class PluginWizardStep extends PanelWizardStep {

	// The standard screen dimensions of the wizard steps content panels
	public static int CONTENT_PANEL_WIDTH = 500;
	public static int CONTENT_PANEL_HEIGHT = 300;
	
	public PluginWizardStep(String name, String description){
		super(name,description);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(CONTENT_PANEL_WIDTH, CONTENT_PANEL_HEIGHT));
		
		add(getContentPanel(), BorderLayout.CENTER);
	}
	
	/**
	 * Subclasses must provide a JPanel with the wizard contents.
	 * 
	 * @return the content panel
	 */
	protected abstract JPanel getContentPanel();
}