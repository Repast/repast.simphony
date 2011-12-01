package repast.simphony.ui.newscenario;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NewScenarioFinalPanel extends PanelWizardStep {

	private NewWizardModel model;
	private JLabel specLbl = new JLabel();
	private JLabel scenarioLbl = new JLabel();
	private JLabel modelInitLbl = new JLabel();
	private JLabel modelPluginLbl = new JLabel();

	public NewScenarioFinalPanel() {
		super("Scenario Details", "The details of the scenario are given below.");
		setLayout(new BorderLayout());
		specLbl.setFont(specLbl.getFont().deriveFont(Font.BOLD));
		scenarioLbl.setFont(specLbl.getFont());
		modelInitLbl.setFont(specLbl.getFont());
		modelPluginLbl.setFont(specLbl.getFont());

		FormLayout layout = new FormLayout("3dlu, pref:grow",
						"pref, 3dlu, pref, 5dlu, pref, 3dlu, pref, 5dlu, pref, 3dlu, pref, 5dlu pref, 3dlu, pref");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addLabel("Model Specification:", cc.xyw(1, 1, 2));
		builder.add(specLbl, cc.xy(2, 3));
		builder.addLabel("Model Initializer (Optional):", cc.xyw(1, 5, 2));
		builder.add(modelInitLbl, cc.xy(2, 7));
		builder.addLabel("Use Model Plugin:", cc.xyw(1, 9, 2));
		builder.add(modelPluginLbl, cc.xy(2, 11));
		builder.addLabel("New Scenario Directory:", cc.xyw(1, 13, 2));
		builder.add(scenarioLbl, cc.xy(2, 15));
		this.add(builder.getPanel(), BorderLayout.NORTH);
		setComplete(true);
	}

	public void init(WizardModel model) {
		this.model = (NewWizardModel) model;
	}

	@Override
	public void prepare() {
		super.prepare();
		specLbl.setText(model.getSpecPath().getAbsolutePath());
		scenarioLbl.setText(model.getScenarioPath().getAbsolutePath());
		if (model.getModelInit() != null && model.getModelInit().length() > 0) {
			modelInitLbl.setText(model.getModelInit());
		} else {
			modelInitLbl.setText("<None>");
		}

		modelPluginLbl.setText(model.doUseModelPlugin() ? "Yes" : "No");
	}
}
