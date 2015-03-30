package repast.simphony.ui.newscenario;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.pietschy.wizard.WizardModel;

import repast.simphony.ui.plugin.editor.PluginWizardStep;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ModelInitPanel extends PluginWizardStep {

	private JTextField initFld;
	private JCheckBox chkPlugin;
	private NewWizardModel model;

	public ModelInitPanel() {
		super("Optional Model Initializer & Model Plugin", "Specify the fully qualified name of a Model Initializer class " +
						"that implements the ModelInitializer interface, and whether or not the scenario uses a model plugin file. " +
						"Note that these are optional.");
		
		setComplete(true);
	}

	@Override
	protected JPanel getContentPanel(){
		initFld = new JTextField();
		chkPlugin = new JCheckBox();
		
		FormLayout layout = new FormLayout(
				"pref, 4dlu, pref:grow", 
				"pref, 4dlu, pref, 4dlu, pref");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addLabel("Model Initializer Class Name:", cc.xyw(1, 1, 3));
		builder.add(initFld, cc.xyw(1, 3, 3));
		builder.addLabel("Use Model Plugin: ", cc.xy(1, 5));
		builder.add(chkPlugin, cc.xy(3, 5));
		return builder.getPanel();
	}

	@Override
	public void init(WizardModel model) {
		this.model = (NewWizardModel) model;
	}

	@Override
	public void prepare() {
		super.prepare();
		if (model.getModelInit() != null) initFld.setToolTipText(model.getModelInit());
		chkPlugin.setSelected(model.doUseModelPlugin());
	}

	@Override
	public void applyState() throws org.pietschy.wizard.InvalidStateException {
		super.applyState();
		model.setModelInit(initFld.getText().trim());
		model.setUseModelPlugin(chkPlugin.isSelected());
	}
}
