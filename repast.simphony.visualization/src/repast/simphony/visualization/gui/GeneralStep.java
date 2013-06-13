package repast.simphony.visualization.gui;

import static repast.simphony.visualization.engine.DisplayDescriptor.DisplayType.GIS;
import static repast.simphony.visualization.engine.DisplayDescriptor.DisplayType.GIS3D;
import static repast.simphony.visualization.engine.DisplayDescriptor.DisplayType.THREE_D;
import static repast.simphony.visualization.engine.DisplayDescriptor.DisplayType.TWO_D;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.scenario.data.ProjectionType;
import repast.simphony.ui.widget.ListSelector;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.ProjectionDescriptor;
import repast.simphony.visualization.engine.ProjectionDescriptorFactory;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class GeneralStep extends PanelWizardStep {

  class DisplayItem {
    String displayName;
    ProjectionData proj;

    public DisplayItem(String displayName, ProjectionData proj) {
      this.proj = proj;
      this.displayName = displayName;
    }

    public boolean equals(Object obj) {
      if (obj instanceof DisplayItem) {
        DisplayItem other = (DisplayItem) obj;
        if (proj == null) {
          if (other.proj == null)
            return other.displayName.equals(displayName);
          return false;
        } else {
          if (other.proj != null)
            return proj.equals(other.proj);
          return false;
        }
      }
      return false;
    }

    public String toString() {
      return displayName;
    }

    public ProjectionData getProjectionData() {
      return proj;
    }
  }

  private DisplayWizardModel model;
  private JTextField nameFld = new JTextField();
  private JComboBox typeBox = new JComboBox();
  private ListSelector<DisplayItem> selector;

  public GeneralStep() {
    super("Display Details", "Please enter the name and type of the display as well the "
        + "projections the display should visualize");
    this.setLayout(new BorderLayout());
    FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow",
        "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref");
    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();

    CellConstraints cc = new CellConstraints();
    builder.addSeparator("General", cc.xyw(1, 1, 3));
    builder.addLabel("Name:", cc.xy(1, 3));
    builder.add(nameFld, cc.xy(3, 3));
    builder.addLabel("Type:", cc.xy(1, 5));
    builder.add(typeBox, cc.xy(3, 5));

    java.util.List<DisplayItem> list = new ArrayList<DisplayItem>();
    selector = new ListSelector<DisplayItem>(list, list, false);
    builder.addSeparator("Projections and Value Layers", cc.xyw(1, 7, 3));
    builder.add(selector.getPanel(), cc.xyw(1, 9, 3));

    add(builder.getPanel(), BorderLayout.CENTER);
    selector.addActionListeners(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setComplete(selector.getSelectedItems().size() > 0 && nameFld.getText().length() > 0);
      }
    });
  }

  private boolean doValidate() {
    int geoCount = 0; // # geography projections
    int netCount = 0; // # network projections
    int cartCount = 0; // # cartesian (grid,continuous) projections
    int valueCount = 0; // # value layers

    for (DisplayItem item : selector.getSelectedItems()) {

      if (item.getProjectionData().getType() == ProjectionType.VALUE_LAYER)
        valueCount++;
      else if (item.getProjectionData().getType() == ProjectionType.GEOGRAPHY)
        geoCount++;
      else if (item.getProjectionData().getType() == ProjectionType.NETWORK)
        netCount++;
      else
        cartCount++;
    }

    DisplayDescriptor.DisplayType displayType = (DisplayDescriptor.DisplayType) typeBox
        .getSelectedItem();

    // Validate GIS / Geography projection displays
    // Check if a geography projection is used with a non-GIS display type
    if (geoCount > 0
        && (displayType == DisplayDescriptor.DisplayType.TWO_D || displayType == DisplayDescriptor.DisplayType.THREE_D)) {
      JOptionPane.showMessageDialog(selector,
          "Geography projections can only be used in a GIS display", "Display Error",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    // Check if GIS displays contain a geography projection
    else if (displayType == DisplayDescriptor.DisplayType.GIS
        || displayType == DisplayDescriptor.DisplayType.GIS3D) {

      if (geoCount != 1) {
        JOptionPane.showMessageDialog(selector, "A GIS display must contain a single Geography.",
            "Display Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }

      else if (geoCount <= 1 && (cartCount > 0 || valueCount > 0)) {
        JOptionPane.showMessageDialog(selector,
            "GIS displays may only contain Geography and Network projections.", "Display Error",
            JOptionPane.ERROR_MESSAGE);
        return false;
      }

      else if (geoCount == 1 && netCount > 0) {
        JOptionPane.showMessageDialog(selector,
            "Note that network edges are styled as agents in GIS displays.", "Display Info",
            JOptionPane.INFORMATION_MESSAGE);
        return true;
      }
    }

    // If the display contains value layers...
    if (valueCount > 0) {

      // Check if there is at least one layout projection
      if (cartCount == 0) {
        JOptionPane
            .showMessageDialog(
                selector,
                "Displays containing Value Layers must also contain at least one Grid or Continuous projection.",
                "Display Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }

      // Check if there is no more than one value layer
      if (valueCount > 1) {
        JOptionPane.showMessageDialog(selector, "Displays may only contain one Value Layer.",
            "Display Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }

    }
    return true;
  }

  @Override
  public void init(WizardModel wizardModel) {
    this.model = (DisplayWizardModel) wizardModel;
  }

  @Override
  public void prepare() {
    super.prepare();

    ComboBoxModel cmbModel;
    if (model.contextContainsOnlyProjectionType(ProjectionType.GEOGRAPHY)) {
      cmbModel = new DefaultComboBoxModel(new DisplayDescriptor.DisplayType[] { GIS, GIS3D });
    } else if (model.contextContainsProjectionType(ProjectionType.GEOGRAPHY)) {
      cmbModel = new DefaultComboBoxModel(new DisplayDescriptor.DisplayType[] { TWO_D, THREE_D,
          GIS, GIS3D });
    } else {
      cmbModel = new DefaultComboBoxModel(new DisplayDescriptor.DisplayType[] { TWO_D, THREE_D });
    }
    typeBox.setModel(cmbModel);

    DisplayDescriptor descriptor = model.getDescriptor();
    nameFld.setText(descriptor.getName());

    DisplayDescriptor.DisplayType displayType = descriptor.getDisplayType();
    typeBox.setSelectedItem(displayType);

    java.util.List<DisplayItem> source = new ArrayList<DisplayItem>();

    for (ProjectionData proj : model.getContext().projections()) {
      DisplayItem item = null;
      item = new DisplayItem(proj.getId(), proj);
      source.add(item);
    }

    java.util.List<DisplayItem> target = new ArrayList<DisplayItem>();
    for (ProjectionData proj : descriptor.getProjections()) {
      DisplayItem item = null;
      item = new DisplayItem(proj.getId(), proj);
      source.remove(item);
      target.add(item);
    }

    selector.setLists(source, target);
    setComplete(target.size() > 0);
  }

  @Override
  public void applyState() throws InvalidStateException {
    super.applyState();
    if (!doValidate()) {
      throw new InvalidStateException();
    }

    DisplayDescriptor descriptor = model.getDescriptor();
    descriptor.setName(nameFld.getText().trim());
    DisplayDescriptor.DisplayType curType = descriptor.getDisplayType();
    DisplayDescriptor.DisplayType newType = (DisplayDescriptor.DisplayType) typeBox
        .getSelectedItem();
    if (curType != newType) {
      descriptor.setDisplayType(newType, true);
    }
    descriptor.clearProjections();
    descriptor.clearValueLayerNames();
    descriptor.clearProjectionDescriptors();

    for (DisplayItem item : selector.getSelectedItems()) {
      if (item.proj != null) {
        if (item.getProjectionData().getType() == ProjectionType.VALUE_LAYER) {
          descriptor.addValueLayerName(item.displayName);
        } else {
          ProjectionDescriptor pd = ProjectionDescriptorFactory.createDescriptor(item.proj);
          descriptor.addProjection(item.proj, pd);
        }
      }
    }
  }
}
