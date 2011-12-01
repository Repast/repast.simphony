package repast.simphony.visualization.editor;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.projection.Projection;
import repast.simphony.visualization.*;
import repast.simphony.visualization.continuous.Continuous2DLayout;
import repast.simphony.visualization.editor.space.Projected3DGrid;
import repast.simphony.visualization.editor.space.Projected3DSpace;
import repast.simphony.visualization.grid.Grid2DLayout;
import repast.simphony.visualization.visualization2D.Display2D;
import repast.simphony.visualization.visualization2D.layout.CircleLayout2D;
import repast.simphony.visualization.visualization2D.style.DefaultEdgeStyle2D;
import repast.simphony.visualization.visualization2D.style.DefaultStyle2D;
import repast.simphony.visualization.visualization3D.Display3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * DisplayEditor for a 3D display.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class DisplayEditor3D implements DisplayEditorLifecycle {

  private static int X = 0;
  private static int Y = 1;
  private static int Z = 2;

  private Display3D display;
  private JPanel panel, editorPanel, displayPanel;
  private Grid grid;
  private java.util.List<Network> nets = new ArrayList<Network>();
  private ContinuousSpace space;
  private java.util.List<DisplayEditorLifecycle> editors = new ArrayList<DisplayEditorLifecycle>();
  private java.util.List<IDisplay> displays = new ArrayList<IDisplay>();
  private java.util.List<Projected3DGrid> projGrids = new ArrayList<Projected3DGrid>();
  private java.util.List<Projected3DSpace> projSpaces = new ArrayList<Projected3DSpace>();
  private static final String XY_TITLE = "X x Y";
  private static final String XZ_TITLE = "X x Z";
  private static final String ZY_TITLE = "Z x Y";
  private JComboBox box;
  private Color[] colors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GREEN,
          Color.LIGHT_GRAY, Color.MAGENTA, Color.GRAY, Color.ORANGE, Color.PINK, Color.RED};

  public DisplayEditor3D(Display3D display, JPanel panel) {
    this.display = display;
    this.panel = panel;

    init();
  }

  private void init() {
    for (Projection proj : display.getInitData().getProjections()) {
      if (proj instanceof Grid) grid = (Grid) proj;
      if (proj instanceof Network) nets.add((Network) proj);
      if (proj instanceof ContinuousSpace) space = (ContinuousSpace) proj;
    }
  }

  /**
   * Runs the editor.
   */
  public void run() {
    display.setPause(false);
    display.update();
    display.render();

    editorPanel = new JPanel(new BorderLayout());
    JPanel parent = (JPanel) panel.getParent();
    parent.remove(panel);

    displayPanel = new JPanel(new CardLayout());
    editorPanel.add(displayPanel, BorderLayout.CENTER);
    JToolBar bar = new JToolBar();
    bar.setLayout(new FlowLayout(FlowLayout.RIGHT));
    bar.setFloatable(false);
    bar.add(createDisplays());
    editorPanel.add(bar, BorderLayout.NORTH);
    parent.add(editorPanel);

    for (DisplayEditorLifecycle editor : editors) {
      editor.run();
    }

    //box.setSelectedIndex(1);

    parent.revalidate();
    parent.repaint();
  }

  private void processGrid(DefaultComboBoxModel model) {
    if (grid.getDimensions().size() < 3) {
      createDisplay(createNonNetData(grid), createLayout(grid), XY_TITLE);
      model.addElement(XY_TITLE);
    } else {
      Projected3DGrid xy = new Projected3DGrid(grid, X, Y);
      projGrids.add(xy);
      createDisplay(createNonNetData(xy), createLayout(xy), XY_TITLE);
      model.addElement(XY_TITLE);

      Projected3DGrid xz = new Projected3DGrid(grid, X, Z);
      projGrids.add(xz);
      createDisplay(createNonNetData(xz), createLayout(xz), XZ_TITLE);
      model.addElement(XZ_TITLE);

      Projected3DGrid zy = new Projected3DGrid(grid, Z, Y);
      createDisplay(createNonNetData(zy), createLayout(zy), ZY_TITLE);
      model.addElement(ZY_TITLE);
      projGrids.add(zy);
    }
  }

  private DisplayData createNonNetData(Projection proj) {
    SyntheticDisplayData data = new SyntheticDisplayData(display.getInitData().getContainer());
    data.addProjection(proj);
    for (Network net : nets) {
      data.addProjection(net);
    }

    return data;
  }

  private Layout createLayout(Grid grid) {
    Layout layout = new Grid2DLayout(grid);
    UnitSizeLayoutProperties props = new UnitSizeLayoutProperties();
    props.setUnitSize(15);
    layout.setLayoutProperties(props);
    return layout;
  }

  private Layout createLayout(Projected3DSpace space) {
    Layout layout = new Continuous2DLayout(space);
    UnitSizeLayoutProperties props = new UnitSizeLayoutProperties();
    props.setUnitSize(15);
    layout.setLayoutProperties(props);
    return layout;
  }

  private void processSpace(DefaultComboBoxModel model) {
    Projected3DSpace xy = new Projected3DSpace(space, X, Y);
    projSpaces.add(xy);
    createDisplay(createNonNetData(xy), createLayout(xy), XY_TITLE);
    model.addElement(XY_TITLE);


    Projected3DSpace xz = new Projected3DSpace(space, X, Z);
    projSpaces.add(xz);
    createDisplay(createNonNetData(xz), createLayout(xz), XZ_TITLE);
    model.addElement(XZ_TITLE);

    Projected3DSpace zy = new Projected3DSpace(space, Z, Y);
    createDisplay(createNonNetData(zy), createLayout(zy), ZY_TITLE);
    model.addElement(ZY_TITLE);
    projSpaces.add(zy);
  }

  // only network in display no grid or space
  private void processNet(DefaultComboBoxModel model) {
    SyntheticDisplayData data = new SyntheticDisplayData(display.getInitData().getContainer());
    for (Network net : nets) {
      data.addProjection(net);
    }

    CircleLayout2D layout = new CircleLayout2D();
    layout.setProjection(nets.get(0));
    Display2D display = createDisplay(data, layout, "2D Network(s)");
    display.setLayoutFrequency(IDisplay.LayoutFrequency.ON_NEW, 0);
    model.addElement("2D Network(s)");

  }

  private Display2D createDisplay(DisplayData data, Layout layout, String title) {
    Display2D display2D = new Display2D(data, layout);
    displays.add(display2D);
    for (Class clazz : display.getRegisteredAgents()) {
      display2D.registerStyle(clazz, new DefaultStyle2D());
    }

    int i = 0;
    for (Network net : nets) {
      DefaultEdgeStyle2D style = new DefaultEdgeStyle2D();
      Color color = getColor(i++);
      style.setPaint(color);
      display2D.registerNetworkStyle(net, style);
    }

    display2D.setLayoutFrequency(IDisplay.LayoutFrequency.AT_UPDATE, 0);
    display2D.init();
    JPanel panel2D = display2D.getPanel();
    JPanel p = new JPanel(new BorderLayout());
    p.add(panel2D, BorderLayout.CENTER);
    this.displayPanel.add(p, title);
    panel2D.setPreferredSize(panel.getPreferredSize());
    editors.add(display2D.createEditor(panel2D));
    display2D.resetHomeView();
    return display2D;
  }

  private Color getColor(int index) {
    if (index > colors.length - 1) index = (index % (colors.length)) - 1;
    return colors[index];
  }

  private JComboBox createDisplays() {
    displayPanel.add(panel, "3D");
    DefaultComboBoxModel model = new DefaultComboBoxModel();
    model.addElement("3D");

    if (grid != null) processGrid(model);
    else if (space != null) processSpace(model);
    else if (nets.size() > 0) processNet(model);

    box = new JComboBox(model);
    box.setPrototypeDisplayValue("XXXXXXXXXXXXXXX");
    box.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        JComboBox src = (JComboBox) evt.getSource();
        String key = src.getSelectedItem().toString();
        ((CardLayout) displayPanel.getLayout()).show(displayPanel, key);
        display.update();
        display.render();
      }
    });

    return box;
  }

  /**
   * Stops the Editor and performs any clean up.
   */
  public void stop() {
    for (DisplayEditorLifecycle editor : editors) {
      editor.stop();
    }

    editors.clear();

    for (Projected3DGrid grid : projGrids) {
      grid.destroy();
    }

    for (Projected3DSpace space : projSpaces) {
      space.destroy();
    }

    for (IDisplay display : displays) {
      display.destroy();
    }
    displays.clear();

    Container parent = editorPanel.getParent();
    parent.remove(editorPanel);
    displayPanel.removeAll();
    parent.add(panel);

    panel.revalidate();
    panel.repaint();

    parent.invalidate();
    parent.repaint();
  }
}
