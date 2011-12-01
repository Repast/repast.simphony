package repast.simphony.visualization.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.engine.DisplayDescriptor;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class NameOnlyGeneralStep extends PanelWizardStep {

  private DisplayWizardModel model;
  private JTextField nameFld = new JTextField();
  private JLabel typeLbl = new JLabel();
  private JList projections = new JList(new DefaultListModel());

  public NameOnlyGeneralStep() {
    super("Display Details", "Enter the name of the display");
    this.setLayout(new BorderLayout());
    FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow",
	"pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, 60dlu");
    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();
    typeLbl.setFont(typeLbl.getFont().deriveFont(Font.BOLD));

    CellConstraints cc = new CellConstraints();
    builder.addSeparator("General", cc.xyw(1, 1, 3));
    builder.addLabel("Name:", cc.xy(1, 3));
    builder.add(nameFld, cc.xy(3, 3));
    builder.addLabel("Type:", cc.xy(1, 5));
    builder.add(typeLbl, cc.xy(3, 5));

    builder.addSeparator("Projections", cc.xyw(1, 7, 3));
    builder.add(new JScrollPane(projections), cc.xyw(1, 9, 3));

    add(builder.getPanel(), BorderLayout.CENTER);
  }

  @Override
  public void init(WizardModel wizardModel) {
    this.model = (DisplayWizardModel) wizardModel;
  }

  @Override
  public void prepare() {
    super.prepare();
    DisplayDescriptor descriptor = model.getDescriptor();
    nameFld.setText(descriptor.getName());
    if (descriptor.getDisplayType().equals(DisplayDescriptor.DisplayType.TWO_D))
      typeLbl.setText("2D");

    else if (descriptor.getDisplayType().equals(DisplayDescriptor.DisplayType.THREE_D))
      typeLbl.setText("3D");

    else if (descriptor.getDisplayType().equals(DisplayDescriptor.DisplayType.GIS))
      typeLbl.setText("GIS");
    else
      typeLbl.setText("GIS 3D");

    DefaultListModel model = (DefaultListModel) projections.getModel();
    model.clear();
    for (ProjectionData data : descriptor.getProjections()) {
      model.addElement(data.getId());
    }
  }

  @Override
  public void applyState() throws InvalidStateException {
    super.applyState();
    model.getDescriptor().setName(nameFld.getText().trim());
  }
}
