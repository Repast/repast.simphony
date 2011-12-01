package repast.simphony.visualization.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.scenario.data.ProjectionType;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.cgd.CGDLayout;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.visualization2D.Random2DLayout;
import repast.simphony.visualization.visualization2D.layout.CircleLayout2D;
import repast.simphony.visualization.visualization2D.layout.FRLayout2D;
import repast.simphony.visualization.visualization2D.layout.ISOMLayout2D;
import repast.simphony.visualization.visualization2D.layout.KKLayout2D;
import repast.simphony.visualization.visualization3D.layout.FR3DLayout;
import repast.simphony.visualization.visualization3D.layout.GEM3DLayout;
import repast.simphony.visualization.visualization3D.layout.Random3DLayout;
import repast.simphony.visualization.visualization3D.layout.SphericalLayout;
import saf.core.ui.util.IntegerDocument;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 */
public class NetLayoutStep extends PanelWizardStep {
  private static final long serialVersionUID = -1853984603197206862L;

  private DisplayWizardModel model;
  private JComboBox frequencyBox = new JComboBox(new Object[] { IDisplay.LayoutFrequency.ON_NEW,
      IDisplay.LayoutFrequency.AT_UPDATE, IDisplay.LayoutFrequency.AT_INTERVAL });
  private JTextField intervalFld = new JTextField();
  private JComboBox layoutClassNameBox = new JComboBox();
  private JComboBox layoutNetworkBox = new JComboBox();

  private ComboBoxModel model2DLayout = new DefaultComboBoxModel(new String[] {
      Random2DLayout.class.getName(), CircleLayout2D.class.getName(), FRLayout2D.class.getName(),
      KKLayout2D.class.getName(), ISOMLayout2D.class.getName(), CGDLayout.class.getName() });
  private ComboBoxModel model3DLayout = new DefaultComboBoxModel(new String[] {
      GEM3DLayout.class.getName(), FR3DLayout.class.getName(), SphericalLayout.class.getName(),
      Random3DLayout.class.getName() });

  public NetLayoutStep() {
    super(
	"Layout Details",
	"Please select a network layout and the "
	    + "frequency at which the layout will be updated, and the network that determines the layout");
    setLayout(new BorderLayout());
    intervalFld.setDocument(new IntegerDocument());
    intervalFld.setText("1");

    FormLayout layout = new FormLayout("pref:grow",
	"pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref");
    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();

    CellConstraints cc = new CellConstraints();
    builder.addLabel("Network:", cc.xy(1, 1));
    builder.add(layoutNetworkBox, cc.xy(1, 3));
    builder.addLabel("Class Name:", cc.xy(1, 5));
    builder.add(layoutClassNameBox, cc.xy(1, 7));
    builder.addLabel("Frequency:", cc.xy(1, 9));
    builder.add(frequencyBox, cc.xy(1, 11));
    builder.addLabel("Interval:", cc.xy(1, 13));
    builder.add(intervalFld, cc.xy(1, 15));

    frequencyBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
	intervalFld
	    .setEnabled(frequencyBox.getSelectedItem() == IDisplay.LayoutFrequency.AT_INTERVAL);
      }
    });

    add(builder.getPanel(), BorderLayout.CENTER);
    setComplete(true);

  }

  @Override
  public void init(WizardModel wizardModel) {
    this.model = (DisplayWizardModel) wizardModel;
  }

  @Override
  public void applyState() throws InvalidStateException {
    DisplayDescriptor descriptor = model.getDescriptor();
    String layout = layoutClassNameBox.getSelectedItem().toString();
    descriptor.setLayoutClassName(layout);
    descriptor.setLayoutFrequency((IDisplay.LayoutFrequency) frequencyBox.getSelectedItem());
    if (descriptor.getLayoutFrqeuency() == IDisplay.LayoutFrequency.AT_INTERVAL) {
      String val = intervalFld.getText().trim();
      descriptor.setLayoutInterval(Integer.parseInt(val));
    }
    descriptor.setLayoutProjection(layoutNetworkBox.getSelectedItem().toString());
  }

  @Override
  public void prepare() {
    DisplayDescriptor descriptor = model.getDescriptor();
    if (descriptor.getDisplayType() == DisplayDescriptor.DisplayType.THREE_D) {
      layoutClassNameBox.setModel(model3DLayout);
    } else {
      layoutClassNameBox.setModel(model2DLayout);
    }
    frequencyBox.setSelectedItem(descriptor.getLayoutFrqeuency());

    intervalFld.setText(String.valueOf(descriptor.getLayoutInterval()));
    intervalFld.setEnabled(frequencyBox.getSelectedItem() == IDisplay.LayoutFrequency.AT_INTERVAL);
    layoutClassNameBox.setEditable(true);

    String layoutClassName = descriptor.getLayoutClassName();
    if (layoutClassName != null)
      layoutClassNameBox.setSelectedItem(layoutClassName);
    else
      layoutClassNameBox.setSelectedIndex(0);

    Vector<String> netIDs = new Vector<String>();
    for (ProjectionData proj : model.getDescriptor().getProjections()) {
      if (proj.getType() == ProjectionType.NETWORK) {
	netIDs.add(proj.getId());
      }
    }
    layoutNetworkBox.setModel(new DefaultComboBoxModel(netIDs));
    if (descriptor.getLayoutProjection() != null) {
      layoutNetworkBox.setSelectedItem(descriptor.getLayoutProjection());
    }
  }
}
