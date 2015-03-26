package repast.simphony.visualization.gui;

import static repast.simphony.visualization.continuous.ContinuousDecorator.CONTINUOUS_DECORATOR;
import static repast.simphony.visualization.decorator.DecoratorConstants.COLOR;
import static repast.simphony.visualization.decorator.DecoratorConstants.SHOW_DECORATOR;
import static repast.simphony.visualization.decorator.DecoratorConstants.UNIT_SIZE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.ui.plugin.editor.SquareIcon;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.UnitSizeLayoutProperties;
import repast.simphony.visualization.VisualizationProperties;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;
import repast.simphony.visualization.engine.ProjectionDescriptor;
import saf.core.ui.util.FloatDocument;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 */
public class ContinuousStyleStep extends PanelWizardStep {
  private static final long serialVersionUID = 7952844028233688422L;

  private DisplayWizardModel model;
  private JTextField sizeFld = new JTextField();
  private JLabel nameLbl = new JLabel();
  private JCheckBox boundingBox = new JCheckBox();
  private JButton colorBtn = new JButton();
  private Color color = Color.WHITE;
  private boolean prepared, applied;


  public ContinuousStyleStep() {
    super("Continuous Space Style", "Please enter the continuous space details");
    this.setLayout(new BorderLayout());
    sizeFld.setDocument(new FloatDocument());
    FormLayout layout = new FormLayout("right:pref, 3dlu, pref, pref:grow",
            "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref");
    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();

    CellConstraints cc = new CellConstraints();
    builder.addLabel("Space Name:", cc.xy(1, 1));
    nameLbl.setFont(nameLbl.getFont().deriveFont(Font.BOLD));
    builder.add(nameLbl, cc.xyw(3, 1, 2));
    builder.addLabel("Unit size:", cc.xy(1, 3));
    builder.add(sizeFld, cc.xyw(3, 3, 2));
    builder.addLabel("Show Bounding Box:", cc.xy(1, 5));
    builder.add(boundingBox, cc.xyw(3, 5, 2));
    final JLabel boxColorLabel = builder.addLabel("Bounding Box:", cc.xy(1, 7));
    builder.add(colorBtn, cc.xy(3, 7));
    add(builder.getPanel(), BorderLayout.CENTER);

    boundingBox.setSelected(true);
    boundingBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        colorBtn.setEnabled(boundingBox.isSelected());
        boxColorLabel.setEnabled(boundingBox.isSelected());
      }
    });

    colorBtn.setIcon(new SquareIcon(10, 10, color));
    colorBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Color c = JColorChooser.showDialog(ContinuousStyleStep.this, "Select Color", color);
        if (c != null) {
          color = c;
          colorBtn.setIcon(new SquareIcon(10, 10, color));
        }
      }
    });

    setComplete(true);
  }

  @Override
  public void init(WizardModel wizardModel) {
    this.model = (DisplayWizardModel) wizardModel;
  }

  private void prepareLayoutProps() {
    DisplayDescriptor descriptor = model.getDescriptor();
    VisualizationProperties props = descriptor.getLayoutProperties();
    if (props != null) {
      if (props.getProperty(UnitSizeLayoutProperties.UNIT_SIZE_LAYOUT_PROPERTIES_ID) == null) {
        // clear the properties as its the wrong one for now.
        resetLayoutProperties(descriptor);
      } else {
        UnitSizeLayoutProperties hints = (UnitSizeLayoutProperties) props;
        sizeFld.setText(String.valueOf(hints.getUnitSize()));
      }
    } else {
      resetLayoutProperties(descriptor);
    }
  }

  private void resetLayoutProperties(DisplayDescriptor descriptor) {
    descriptor.setLayoutProperties(new UnitSizeLayoutProperties());
    // TODO Projections: instead of using DisplayType, get the properties from the descriptor
    if (descriptor.getDisplayType().equals(DisplayType.THREE_D)) {
      sizeFld.setText(".06");
    } else {
      sizeFld.setText("15");
      color = Color.BLACK;
      colorBtn.setIcon(new SquareIcon(10, 10, color));
    }
  }

  @Override
  public void prepare() {
    String name = model.getTypeDescriptor(ProjectionData.CONTINUOUS_SPACE_TYPE).getProjectionName();
    if (name != null) {
      nameLbl.setText(name);
    }
    if (!prepared || (prepared && applied)) {
      prepareLayoutProps();
      prepareDecoratorProps();
    }
    prepared = true;
  }

  private void prepareDecoratorProps() {
    DisplayDescriptor descriptor = model.getDescriptor();
    ProjectionDescriptor pd = model.getTypeDescriptor(ProjectionData.CONTINUOUS_SPACE_TYPE);
    // get the grid  if there and set color etc.
    Boolean show = (Boolean) pd.getProperty(CONTINUOUS_DECORATOR, SHOW_DECORATOR);
    if (show == null) {
      boundingBox.setSelected(true);
      pd.setProperty(CONTINUOUS_DECORATOR, COLOR, color.getRGB());
      pd.setProperty(CONTINUOUS_DECORATOR, SHOW_DECORATOR, true);
      VisualizationProperties lProps = descriptor.getLayoutProperties();
      Float cellSize = (Float) lProps.getProperty(UnitSizeLayoutProperties.UNIT_SIZE);
      pd.setProperty(CONTINUOUS_DECORATOR, UNIT_SIZE, cellSize);
    } else {
      boundingBox.setSelected(show);
      Integer colorAsInt = (Integer) pd.getProperty(CONTINUOUS_DECORATOR, COLOR);
      color = new Color(colorAsInt);
      colorBtn.setIcon(new SquareIcon(10, 10, color));
    }
  }


  @Override
  public void applyState() throws InvalidStateException {
    DisplayDescriptor descriptor = model.getDescriptor();
    // we know we can cast this because we set them in prepare if they are not continuous layout props
    UnitSizeLayoutProperties hints = (UnitSizeLayoutProperties) descriptor.getLayoutProperties();
    float unitSize = Float.parseFloat(sizeFld.getText());
    hints.setUnitSize(unitSize);

    ProjectionDescriptor pd = model.getTypeDescriptor(ProjectionData.CONTINUOUS_SPACE_TYPE);
    pd.setProperty(CONTINUOUS_DECORATOR, COLOR, color.getRGB());
    pd.setProperty(CONTINUOUS_DECORATOR, SHOW_DECORATOR, boundingBox.isSelected());
    pd.setProperty(CONTINUOUS_DECORATOR, UNIT_SIZE, unitSize);

    for (ProjectionData proj : descriptor.getProjections()) {
      if (proj.getType().equals(ProjectionData.CONTINUOUS_SPACE_TYPE)) {
        descriptor.setLayoutProjection(proj.getId());
        descriptor.setLayoutFrequency(IDisplay.LayoutFrequency.ON_MOVE);
        // we need to set a fake class name here so that the
        // wizard path condition won't show the network layout
        // step. This layout will never be used because the
        // implicit layout of the continuous projection descriptor
        // will override that.
        descriptor.setLayoutClassName("foo.bar.baz");
      }
    }
    applied = true;
  }
}
