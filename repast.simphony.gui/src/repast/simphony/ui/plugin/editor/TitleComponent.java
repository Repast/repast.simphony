package repast.simphony.ui.plugin.editor;

import java.awt.Color;

import org.pietschy.wizard.DefaultTitleComponent;
import org.pietschy.wizard.Wizard;

/**
 * Customized title component for wizard panels.
 * 
 * @author Eric Tatara
 * @author Nick Collier
 *
 */
public class TitleComponent extends DefaultTitleComponent {
	private static final long serialVersionUID = -8294170672636152155L;
	
	public static Color TITLE_COLOR = new Color(173,216,230);  // light blue

	public TitleComponent(Wizard wizard) {
		super(wizard);
		
		setGradientBackground(true);
		setFadeColor(TITLE_COLOR);
	}

	public void setTitle(String text) {
		super.title.setText(text);
	}

	public void setSummary(String summary) {
		super.summary.setText(summary);
	}
}
