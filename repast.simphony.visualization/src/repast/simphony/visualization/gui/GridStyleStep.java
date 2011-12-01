package repast.simphony.visualization.gui;

import static repast.simphony.visualization.decorator.DecoratorConstants.COLOR;
import static repast.simphony.visualization.decorator.DecoratorConstants.SHOW_DECORATOR;
import static repast.simphony.visualization.decorator.DecoratorConstants.UNIT_SIZE;
import static repast.simphony.visualization.grid.GridDecorator.GRID_DECORATOR;

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

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.scenario.data.ProjectionType;
import repast.simphony.ui.widget.SquareIcon;
import repast.simphony.util.wizard.ModelAwarePanelStep;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.UnitSizeLayoutProperties;
import repast.simphony.visualization.VisualizationProperties;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.ProjectionDescriptor;
import saf.core.ui.util.FloatDocument;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 */
public class GridStyleStep extends ModelAwarePanelStep<DisplayWizardModel> {
  private static final long serialVersionUID = 716163719399246726L;

  private JTextField sizeFld = new JTextField();

  private JLabel nameLbl = new JLabel();

  private JCheckBox gridBox = new JCheckBox();

  private JButton colorBtn = new JButton();

  private Color color = null;
  private boolean prepared, applied;

  public GridStyleStep() {
    super("Grid Style", "Please enter the grid style details");
    this.setLayout(new BorderLayout());
    sizeFld.setDocument(new FloatDocument());
    FormLayout layout = new FormLayout("right:pref, 3dlu, 30dlu, pref:grow",
            "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref");
    PanelBuilder builder = new PanelBuilder(layout);
    builder.setDefaultDialogBorder();

    CellConstraints cc = new CellConstraints();

    builder.addSeparator("Grid Parameters", cc.xyw(1, 1, 4));
    builder.addLabel("Grid Name:", cc.xy(1, 3));
    nameLbl.setFont(nameLbl.getFont().deriveFont(Font.BOLD));
    builder.add(nameLbl, cc.xy(3, 3));
    builder.addLabel("Unit size:", cc.xy(1, 5));
    builder.add(sizeFld, cc.xy(3, 5));
    builder.addLabel("Show Grid:", cc.xy(1, 7));
    builder.add(gridBox, cc.xy(3, 7));
    final JLabel gridLabel = builder.addLabel("Grid Color:", cc.xy(1, 9));
    builder.add(colorBtn, cc.xy(3, 9));
    add(builder.getPanel(), BorderLayout.CENTER);

    gridBox.setSelected(true);
    gridBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        colorBtn.setEnabled(gridBox.isSelected());
        gridLabel.setEnabled(gridBox.isSelected());
      }
    });

    colorBtn.setIcon(new SquareIcon(10, 10, color));
    colorBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Color c = JColorChooser.showDialog(GridStyleStep.this,
                "Select Color", color);
        if (c != null) {
          color = c;
          colorBtn.setIcon(new SquareIcon(10, 10, color));
        }
      }
    });

    setComplete(true);
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

  private void prepareDecoratorProps() {
    if (color == null) {
      if (model.getDescriptor().getDisplayType().equals(DisplayDescriptor.DisplayType.TWO_D)) {
        color = Color.BLACK;
      } else {
        color = Color.WHITE;
      }
    }
    DisplayDescriptor descriptor = model.getDescriptor();
    ProjectionDescriptor pd = model.getTypeDescriptor(ProjectionType.GRID);
    // get the grid props if there and set color etc.
    Boolean show = (Boolean) pd.getProperty(GRID_DECORATOR, SHOW_DECORATOR);
    if (show == null) {
      gridBox.setSelected(true);
      pd.setProperty(GRID_DECORATOR, COLOR, color.getRGB());
      pd.setProperty(GRID_DECORATOR, SHOW_DECORATOR, true);
      VisualizationProperties lProps = descriptor.getLayoutProperties();
      Float cellSize = (Float) lProps.getProperty(UnitSizeLayoutProperties.UNIT_SIZE);
      pd.setProperty(GRID_DECORATOR, UNIT_SIZE, cellSize);
    } else {
      gridBox.setSelected(show);
      Integer colorAsInt = (Integer) pd.getProperty(GRID_DECORATOR, COLOR);
      color = new Color(colorAsInt);
      colorBtn.setIcon(new SquareIcon(10, 10, color));
    }
  }

  @Override
  public void prepare() {
    super.prepare();
    String name = model.getTypeDescriptor(ProjectionType.GRID).getProjectionName();
    if (name != null) {
      nameLbl.setText(name);
    }

    // without this guard, the unit size etc.
    // will get reset improperly
    if (!prepared || (prepared && applied)) {
      prepareLayoutProps();
      prepareDecoratorProps();
    }
    prepared = true;
  }

  private void resetLayoutProperties(DisplayDescriptor descriptor) {
    descriptor.setLayoutProperties(new UnitSizeLayoutProperties());
    if (descriptor.getDisplayType() == DisplayDescriptor.DisplayType.THREE_D) {
      sizeFld.setText(".06");
    } else {
      sizeFld.setText("15");
      color = Color.BLACK;
      colorBtn.setIcon(new SquareIcon(10, 10, color));
    }
  }

  @Override
  public void applyState() throws InvalidStateException {
    DisplayDescriptor descriptor = model.getDescriptor();
    // we know we can cast this because we set them in prepare if they are
    // not grid layout props
    UnitSizeLayoutProperties props = (UnitSizeLayoutProperties) descriptor.getLayoutProperties();
    float cellSize = Float.parseFloat(sizeFld.getText());
    props.setUnitSize(cellSize);

    ProjectionDescriptor pd = model.getTypeDescriptor(ProjectionType.GRID);
    pd.setProperty(GRID_DECORATOR, COLOR, color.getRGB());
    pd.setProperty(GRID_DECORATOR, SHOW_DECORATOR,
            gridBox.isSelected());
    pd.setProperty(GRID_DECORATOR, UNIT_SIZE,
            cellSize);

    for (ProjectionData proj : descriptor.getProjections()) {
      if (proj.getType() == ProjectionType.GRID) {
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
