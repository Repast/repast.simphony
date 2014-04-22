package repast.simphony.ui;

import static repast.simphony.ui.RSGUIConstants.CAMERA_ICON;
import static repast.simphony.ui.RSGUIConstants.DEFAULT_PERSPECTIVE;
import static repast.simphony.ui.RSGUIConstants.LOG_GROUP;
import static repast.simphony.ui.RSGUIConstants.MOVIE_ICON;
import static repast.simphony.ui.RSGUIConstants.PROBE_GROUP;
import static repast.simphony.ui.RSGUIConstants.STATUS_BAR;
import static repast.simphony.ui.RSGUIConstants.STATUS_BAR_VIZ;
import static repast.simphony.ui.RSGUIConstants.TREE_GROUP;
import static repast.simphony.ui.RSGUIConstants.TREE_VIEW;
import static repast.simphony.ui.RSGUIConstants.VISUALIZATION_GROUP;
import static repast.simphony.ui.RSGUIConstants.VIZ_HOME_ICON;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.xml.stream.XMLStreamException;

import repast.simphony.engine.environment.GUIRegistry;
import repast.simphony.engine.environment.GUIRegistryType;
import repast.simphony.parameter.MutableParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersValuesLoader;
import repast.simphony.ui.parameters.ParametersUI;
import repast.simphony.ui.plugin.TickCountFormatter;
import repast.simphony.ui.probe.Probe;
import repast.simphony.ui.probe.ProbeManager;
import repast.simphony.ui.probe.ProbePanelCreator;
import repast.simphony.ui.tree.ScenarioTree;
import repast.simphony.ui.widget.ErrorLog;
import repast.simphony.ui.widget.IconRotator;
import repast.simphony.ui.widget.IconUtils;
import repast.simphony.ui.widget.MovieMakerDialog;
import repast.simphony.ui.widget.SnapshotTaker;
import repast.simphony.ui.widget.VizHomeAction;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.DisplayEvent;
import repast.simphony.visualization.DisplayListener;
import repast.simphony.visualization.IDisplay;
import saf.core.ui.GUIBarManager;
import saf.core.ui.dock.DockableFrame;
import saf.core.ui.dock.DockingManager;
import saf.core.ui.dock.DockingManager.MinimizeLocation;
import saf.core.ui.dock.Perspective;
import saf.core.ui.event.DockableFrameEvent;
import saf.core.ui.event.DockableFrameListener;
import saf.core.ui.util.FileChooserUtilities;
import simphony.util.messages.MessageCenter;

/**
 * Mediator for gui manipulation.
 * 
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/03 15:24:30 $
 */
public class RSGui implements DockableFrameListener, PropertyChangeListener {

  private static MessageCenter msg = MessageCenter.getMessageCenter(RSGui.class);

  private static final String PROBE_KEY = "RSGUI.PROBE_KEY";
  private static final String DISPLAY_ID_PREFIX = "__display.id__";
  private static int display_id = 0;

  private DockingManager dockingManager;
  private DockableFrame treeView;
  private ButtonCoordinator buttonCoordinator = new ButtonCoordinator();
  private JFrame frame;
  private JPanel customPanel;
  private JPanel customPanelContent;

  private boolean running = false;

  private Set<DockableFrame> nonTreeViews = new HashSet<DockableFrame>();
  // this needs to be a linked hashmap to work around a java3D bug in OSX
  // see addVisualization below for more info
  private Map<DockableFrame, IDisplay> displayViewMap = new LinkedHashMap<DockableFrame, IDisplay>();
  private Map<JComponent, JToolBar> toolBarMap = new HashMap<JComponent, JToolBar>();

  // need to track what views are iconfied because viewRestore
  // event restores from floated etc., not just iconified
  // and we want to communicate only on a deiconfiy
  private Set<DockableFrame> iconified = new HashSet<DockableFrame>();
  private List<DockableFrame> parameterViews = new ArrayList<DockableFrame>();
  private TickCountFormatter tickCountFormatter = new DefaultTickCountFormatter();
  private ScenarioTree tree;
  private List<JComponent> compsToDisable = new ArrayList<JComponent>();
  private File paramDir;
  private IconRotator warnRotator;

  public RSGui(DockingManager dockingManager, ScenarioTree tree) {
    this.dockingManager = dockingManager;
    this.tree = tree;
    dockingManager.addDockableListener(this);
    treeView = dockingManager.createDockable(TREE_VIEW, new JScrollPane(tree),
        MinimizeLocation.LEFT, DockingManager.MINIMIZE | DockingManager.FLOAT
            | DockingManager.MAXIMIZE);
    treeView.setTitle("Scenario Tree");
    dockingManager.addDockableToGroup(DEFAULT_PERSPECTIVE, TREE_GROUP, treeView);
  }

  public DockableFrame getDockable(String id) {
    return dockingManager.getDockable(id);
  }

  public DockableFrame addLogView(String id, String title, JComponent comp) {
    DockableFrame view = dockingManager.createDockable(id, comp, MinimizeLocation.BOTTOM);
    view.setTitle(title);
    dockingManager.addDockableToGroup(DEFAULT_PERSPECTIVE, LOG_GROUP, view);
    return view;
  }

  /**
   * Resets the layout using a layout file that defines the initial state.
   */
  public void resetLayout(InputStream stream) {
    Perspective perspective = dockingManager.getPerspective(DEFAULT_PERSPECTIVE);
    perspective.reset(stream);
    DockableFrame view = getDockable(ErrorLog.LOG_VIEW);
    if (view != null) {
      view.close();
    }
  }

  /**
   * Saves the current frame layout to the specified file.
   * 
   * @param file
   *          the file to save the layout to
   */
  public void saveLayout(File file) {
    Perspective perspective = dockingManager.getPerspective(DEFAULT_PERSPECTIVE);
    perspective.saveLayout(file);
  }

  private JToolBar createToolBar(JComponent comp) {
    JToolBar vizBar = new JToolBar();
    vizBar.setFloatable(false);
    JButton snapshot = new JButton(SnapshotTaker.createSnapshotAction(comp));
    snapshot.setIcon(CAMERA_ICON);
    snapshot.setText("");
    compsToDisable.add(snapshot);
    snapshot.setToolTipText("Export to an image.");

    JButton movie = new JButton(MovieMakerDialog.getButtonAction(frame, comp));
    compsToDisable.add(movie);
    movie.setIcon(MOVIE_ICON);
    movie.setToolTipText("Export to a movie.");

    vizBar.add(snapshot);
    vizBar.add(movie);
    return vizBar;
  }

  public DockableFrame addVizualization(String name, JComponent component) {
    JPanel p = new JPanel(new BorderLayout());
    JToolBar toolBar = createToolBar(component);
    toolBarMap.put(component, toolBar);
    p.add(toolBar, BorderLayout.NORTH);
    JPanel compPanel = new JPanel(new BorderLayout());
    compPanel.add(component, BorderLayout.CENTER);
    p.add(compPanel, BorderLayout.CENTER);
    String id = DISPLAY_ID_PREFIX + display_id++;
    DockableFrame view = dockingManager.createDockable(id, p, MinimizeLocation.RIGHT);
    view.setTitle(name);
    dockingManager.addDockableToGroup(DEFAULT_PERSPECTIVE, VISUALIZATION_GROUP, view);
    // this works around a Java3D bug in OSX where adding a canvas3D to a tabbed
    // pane
    // which already contains a tab and then immediately selecting that tab
    // causes the
    // canvas3D initialization to catastrophically fail. The line below always
    // keeps the
    // first added display in selected and so avoids the selection caused when
    // adding
    // to the tab.
    if (displayViewMap.keySet().iterator().hasNext())
      displayViewMap.keySet().iterator().next().toFront();
    nonTreeViews.add(view);
    return view;
  }

  /**
   * Adds the specified listener as a listener for view events. View events are
   * such things as view closed, iconified, etc.
   * 
   * @param listener
   *          the listener to add
   */
  public void addViewListener(DockableFrameListener listener) {
    dockingManager.addDockableListener(listener);
  }

  public void addViewsFromRegistry(final GUIRegistry registry, ProbeManager probeManager) {
    List<Pair<GUIRegistryType, List<JComponent>>> comps = new ArrayList<Pair<GUIRegistryType, List<JComponent>>>();
    for (Pair<GUIRegistryType, Collection<JComponent>> pair : registry.getTypesAndComponents()) {
      List<JComponent> compList = new ArrayList<JComponent>(pair.getSecond());
      Collections.sort(compList, new Comparator<JComponent>() {
        public int compare(JComponent o1, JComponent o2) {
          return registry.getName(o1).compareTo(registry.getName(o2));
        }
      });

      comps.add(new Pair<GUIRegistryType, List<JComponent>>(pair.getFirst(), compList));
    }

    Collections.sort(comps, new Comparator<Pair<GUIRegistryType, List<JComponent>>>() {
      public int compare(Pair<GUIRegistryType, List<JComponent>> o1,
          Pair<GUIRegistryType, List<JComponent>> o2) {
        GUIRegistryType type = o1.getFirst();
        // this should work without comparison because the registry types
        // will necessarily be different
        return type == GUIRegistryType.DISPLAY ? -1 : type == GUIRegistryType.CHART ? 0 : 1;
      }
    });

    for (Pair<GUIRegistryType, List<JComponent>> pair : comps) {
      for (JComponent component : pair.getSecond()) {
        DockableFrame view = addVizualization(registry.getName(component), component);
        if (pair.getFirst().equals(GUIRegistryType.DISPLAY)) {

          IDisplay display = registry.getDisplayForComponent(component);
          JToolBar bar = toolBarMap.get(component);
          if (bar != null) {

            JButton homeButton = new JButton(new VizHomeAction(display));
            homeButton.setText(null);
            homeButton.setIcon(VIZ_HOME_ICON);
            homeButton.setToolTipText("Reset View");
            bar.add(homeButton);

            // TODO possibly implement for other displays later.
            // JToggleButton infoButton = new JToggleButton();
            // infoButton.setAction(new VizInfoAction(display));
            // infoButton.setText(null);
            // infoButton.setIcon(VIZ_INFO_ICON);
            // infoButton.setToolTipText("Toggle Info Probe");
            // bar.add(infoButton);

            display.registerToolBar(bar);
            display.addDisplayListener(new DisplayListener() {
              public void receiveInfoMessage(DisplayEvent evt) {
                GUIBarManager barManager = dockingManager.getBarManager();
                String message = evt.getSubject().toString();
                // unfortunately we can't share a "distance" constant
                // across the gis display and this without causing dependency
                // issues
                if (evt.getProperty(DisplayEvent.TYPE).equals("distance"))
                  barManager.setStatusBarText(STATUS_BAR, message);
                else
                  barManager.setStatusBarText(STATUS_BAR_VIZ, message);
              }
            });
          }
          display.addProbeListener(probeManager);
          displayViewMap.put(view, display);
          display.update();
          display.render();
        }
      }
    }
  }

  public void reset() {
    removeNonTreeViews();
    setGUIForModelLoaded();
    iconified.clear();
    displayViewMap.clear();
    for (DockableFrame view : parameterViews) {
      Probe probe = (Probe) view.getClientProperty(PROBE_KEY);
      if (probe != null) probe.update();
    }
    setStatusBarText("");
    toolBarMap.clear();
  }

  private void removeNonTreeViews() {
    // if (treeView.isMinimized()) treeView.restore();
    // treeView.toFront();
    // we need to do the cop y because calling view.close()
    // ends up calling nonTreeViews.remove(view) and the copy
    // avoids the concurrent modification exception
    List<DockableFrame> tmp = new ArrayList<DockableFrame>(nonTreeViews);
    nonTreeViews.clear();
    for (DockableFrame view : tmp) {
      view.close();
      view.getContentPane().removeAll();
    }

    compsToDisable.clear();
  }

  public void removeParameterViews() {
    List<DockableFrame> frames = new ArrayList<DockableFrame>(parameterViews);
    for (DockableFrame view : frames) {
      view.close();
    }
    parameterViews.clear();
  }

  void initFrame(JFrame frame) {
    this.frame = frame;
  }

  @SuppressWarnings("serial")
  private abstract class ProbeAction extends AbstractAction {
    private Probe probe;

    private ProbeAction(String name, Probe probe) {
      super(name);
      this.probe = probe;
    }

    protected void setProbe(Probe probe) {
      this.probe = probe;
    }

    protected Probe getProbe() {
      return probe;
    }
  }

  @SuppressWarnings("serial")
  private class LoadParams extends ProbeAction {

    private Parameters params;

    public LoadParams(Parameters params, Probe probe) {
      super("Load Parameters", probe);
      this.params = params;
    }

    public void actionPerformed(ActionEvent e) {
      File file = FileChooserUtilities.getOpenFile(frame, paramDir);
      if (file != null) {
        try {
          ParametersValuesLoader loader = new ParametersValuesLoader(file);
          loader.loadValues(params);
          getProbe().update();
          paramDir = file.getParentFile();
        } catch (Exception ex) {
          msg.warn("Error loading parameters", ex);
        }
      }
    }
  }

  /*
   * private class SaveParams extends AbstractAction {
   * 
   * private Parameters params;
   * 
   * public SaveParams(Parameters params) { super("Save Parameters");
   * this.params = params; }
   * 
   * public void actionPerformed(ActionEvent e) { File file =
   * FileChooserUtilities.getSaveFile(frame, paramDir); if (file != null) {
   * ParametersWriter writer = new ParametersWriter(); try {
   * writer.writeValuesToFile(params, file); paramDir = file.getParentFile(); }
   * catch (IOException ex) { msg.warn("Error writing parameters", ex); } } } }
   */

  @SuppressWarnings("serial")
  private class DefaultParams extends AbstractAction {

    private Parameters params;

    public DefaultParams(Parameters params) {
      super("Set Default Parameters");
      this.params = params;
    }

    public void actionPerformed(ActionEvent e) {
      DefaultParamsDialog dialog = new DefaultParamsDialog(frame);
      dialog.init(params);
      dialog.pack();
      dialog.setVisible(true);
    }
  }

  @SuppressWarnings("serial")
  private abstract class ModifyParameterAction extends AbstractAction {
    private MutableParameters params;
    private ParametersUI pui;
    private RSApplication rsApp;

    public ModifyParameterAction(MutableParameters params, ParametersUI pui, RSApplication rsApp) {
      this.params = params;
      this.rsApp = rsApp;
      this.pui = pui;
    }

    public void actionPerformed(ActionEvent e) {
      if (showParamModificationDialog(params)) {
          File paramFile = rsApp.saveCurrentParameters();
          try {
            pui.updatePanel(paramFile);
          } catch (Exception ex) {
            msg.error("Error while modifying parameters", ex);
          }
      }
    }

    public abstract boolean showParamModificationDialog(MutableParameters parameters);
  }

  public Probe updateProbePanel(Probe probe, MutableParameters params) {
    JPanel panel = probe.getPanel();
    Container parent = panel.getParent();
    ProbePanelCreator creator = new ProbePanelCreator(params);
    Probe nProbe = creator.getProbe("Simulation Parameters", false);
    parent.removeAll();
    parent.add(nProbe.getPanel(), BorderLayout.CENTER);
    // msg.info("After add new Probe panel to docking frame its parent is: "
    // + nProbe.getPanel().getParent());
    // also reset the PROBE_KEY for the Parameter dockable frame
    for (DockableFrame view : parameterViews) {
      Probe p = (Probe) view.getClientProperty(PROBE_KEY);
      if (p != null && p.equals(probe)) {
        view.putClientProperty(PROBE_KEY, nProbe);
      }
    }
    return nProbe;
  }

  @SuppressWarnings("serial")
  private class AddParameter extends ModifyParameterAction {

    public AddParameter(MutableParameters params, ParametersUI pui, RSApplication rsApp) {
      super(params, pui, rsApp);
    }

    @Override
    public boolean showParamModificationDialog(MutableParameters parameters) {
      AddParameterDialog dialog = new AddParameterDialog(frame);
      dialog.init(parameters);
      dialog.pack();
      dialog.setLocationRelativeTo(frame);
      dialog.setVisible(true);
      return dialog.parameterAdded();
    }

  }

  @SuppressWarnings("serial")
  private class RemoveParameter extends ModifyParameterAction {
    public RemoveParameter(MutableParameters params, ParametersUI pui, RSApplication rsApp) {
      super(params, pui, rsApp);
    }

    @Override
    public boolean showParamModificationDialog(MutableParameters parameters) {
      RemoveParametersDialog dialog = new RemoveParametersDialog(frame);
      dialog.init(parameters);
      dialog.pack();
      dialog.setVisible(true);
      return dialog.parameterRemoved();
    }
  }

  public void addPlaceHolderUserPanel() {
    customPanel = new JPanel(new BorderLayout());
    DockableFrame view = dockingManager.createDockable("__custom.user__", new JScrollPane(
        customPanel), MinimizeLocation.BOTTOM, DockingManager.FLOAT | DockingManager.MINIMIZE
        | DockingManager.MAXIMIZE);
    view.setTitle("User Panel");
    dockingManager.addDockableToGroup(DEFAULT_PERSPECTIVE, TREE_GROUP, view);
    dockingManager.dock(view, treeView);
    treeView.toFront();
  }

  private boolean hasCustomUserPanelDefined = false;

  public void addCustomUserPanel(JPanel panel) {
    customPanelContent = panel;
    customPanel.add(panel);
    hasCustomUserPanelDefined = true;
    customPanel.revalidate();
    customPanel.repaint();
  }

  public boolean hasCustomUserPanelDefined() {
    return hasCustomUserPanelDefined;
  }

  public void removeCustomUserPanel() {
    if (customPanelContent != null) {
      customPanel.remove(customPanelContent);
    }
    customPanel.revalidate();
    customPanel.repaint();
    customPanelContent = null;
    hasCustomUserPanelDefined = false;
  }

  public void addRunOptionsView(RunOptionsModel model) {
    RunOptionsPanel panel = new RunOptionsPanel();
    panel.init(model);

    DockableFrame view = dockingManager.createDockable("__run.options__", panel,
        MinimizeLocation.BOTTOM, DockingManager.FLOAT | DockingManager.MINIMIZE
            | DockingManager.MAXIMIZE);
    view.setTitle("Run Options");
    dockingManager.addDockableToGroup(DEFAULT_PERSPECTIVE, TREE_GROUP, view);
    treeView.toFront();
  }

  public ParametersUI addParameterView(Parameters params, File currentDirectory, RSApplication rsApp) throws FileNotFoundException, 
    XMLStreamException {
    String id = "__gui__parameters__";
    this.paramDir = currentDirectory;
    ParametersUI pui = new ParametersUI(params);
    JPanel pPanel = pui.createPanel(new File(currentDirectory, RSGUIConstants.PARAMETERS_FILE_NAME));
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(pPanel, BorderLayout.CENTER);
   
    JToolBar tbar = new JToolBar();
    panel.add(tbar, BorderLayout.NORTH);
    tbar.setFloatable(false);
    // going forward all Parameters should be Mutable
    // but we need this check for backward compatibility I think
    if (params instanceof MutableParameters) {
      
      AddParameter add = new AddParameter((MutableParameters) params, pui, rsApp);
      RemoveParameter rem = new RemoveParameter((MutableParameters) params, pui, rsApp);

      JButton btn = new JButton(add);
      btn.setIcon(IconUtils.loadIcon("add_exc.gif"));
      btn.setText("");
      btn.setToolTipText("Add Parameter");
      tbar.add(btn);
      
      btn = new JButton(rem);
      btn.setToolTipText("Remove Parameters");
      btn.setText("");
      btn.setIcon(IconUtils.loadIcon("rem_co.gif"));
      tbar.add(btn);
    }
    
    if (tbar.getComponentCount() > 0) {
      tbar.addSeparator();
    }
    JButton btn = new JButton(new DefaultParams(params));
    btn.setText("");
    btn.setIcon(IconUtils.loadIcon("nav_refresh.gif"));
    btn.setToolTipText("Set current parameter values as default parameter values");
    tbar.add(btn);

    DockableFrame view = dockingManager.createDockable(id, panel, MinimizeLocation.BOTTOM,
        DockingManager.FLOAT | DockingManager.MINIMIZE | DockingManager.MAXIMIZE);
    view.setTitle("Parameters");
    dockingManager.addDockableToGroup(DEFAULT_PERSPECTIVE, TREE_GROUP, view);
    treeView.toFront();
    parameterViews.add(view);
    return pui;
  }

  public void setGUIForStarted() {
    running = true;
    buttonCoordinator.setGUIForStarted(dockingManager.getBarManager());
    for (JComponent comp : compsToDisable) {
      comp.setEnabled(false);
    }
  }

  public void setGUIForStepped() {
    buttonCoordinator.setGUIForStepped(dockingManager.getBarManager());
  }

  public void setGUIForPaused() {
    running = false;
    buttonCoordinator.updateTickCountLabel();
    buttonCoordinator.setGUIForPaused(dockingManager.getBarManager());
    for (JComponent comp : compsToDisable) {
      comp.setEnabled(true);
    }
  }

  public void setGUIForStopped() {
    running = false;
    buttonCoordinator.updateTickCountLabel();
    buttonCoordinator.setGUIForStopped(dockingManager.getBarManager());
    tree.setEnabled(true);
    for (JComponent comp : compsToDisable) {
      comp.setEnabled(true);
    }
  }

  public void setGUIForPostSimInit() {
    buttonCoordinator.setGUIForPostSimInit(dockingManager.getBarManager());
    tree.setEnabled(false);
    for (JComponent comp : compsToDisable) {
      comp.setEnabled(true);
    }
  }

  public void setGUIForModelLoaded() {
    buttonCoordinator.setGUIForModelLoaded(dockingManager.getBarManager());
    buttonCoordinator.updateTickCountLabel(tickCountFormatter.getInitialValue());
    tree.setEnabled(true);
  }

  public void init(final ErrorLog log) {
    GUIBarManager barManager = dockingManager.getBarManager();
    buttonCoordinator.init(barManager);
    JPanel warnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
    barManager.setStatusBarComponent(RSGUIConstants.STATUS_BAR_WARN, warnPanel);
    JButton warnButton = new JButton(RSGUIConstants.WARN_ICON);
    Dimension size = new JTextField().getPreferredSize();
    warnButton.setPreferredSize(new Dimension(22, size.height));
    warnButton.setMaximumSize(warnButton.getPreferredSize());
    warnButton.setMinimumSize(warnButton.getPreferredSize());
    warnButton.setToolTipText("Click to clear warning and view error log");
    warnButton.setEnabled(false);
    warnRotator = new IconRotator(warnButton, (ImageIcon) warnButton.getIcon());
    warnButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        warnRotator.setEnabled(false);
        log.show();
      }
    });
    warnPanel.add(warnButton);

    // remove the View menu and make it part of the windows menu
    // saf.core.ui.view.WindowMenuId

    JMenu viewMenu = barManager.getMenu("saf.core.ui.view.WindowMenuId");
    barManager.getMenuBar().remove(viewMenu);
    JMenu windowMenu = barManager.getMenu(RSGUIConstants.WINDOW_MENU_ID);
    windowMenu.insert(viewMenu, 0);
  }

  void warn() {
    if (warnRotator != null)
      warnRotator.setEnabled(true);
  }

  /**
   * Gets the current TickCountFormatter that is used to format the text in the
   * tick label.
   * 
   * @return the current TickCountFormatter that is used to format the text in
   *         the tick label.
   */
  public TickCountFormatter getTickCountFormatter() {
    return tickCountFormatter;
  }

  /**
   * Sets the current TickCountFormatter that is used to format the text in the
   * tick label.
   * 
   * @param formatter
   *          the formatter to use
   */
  public void setTickCountFormatter(TickCountFormatter formatter) {
    this.tickCountFormatter = formatter;
  }

  public void updateTickCountLabel(double newTick) {
    buttonCoordinator.updateTickCountLabel(tickCountFormatter.format(newTick));
  }

  /**
   * Sets the status bar to the specified text..
   * 
   * @param text
   *          the new status bar text
   */
  public void setStatusBarText(String text) {
    GUIBarManager barManager = dockingManager.getBarManager();
    barManager.setStatusBarTextColor(STATUS_BAR, Color.BLACK);
    barManager.setStatusBarText(STATUS_BAR, text);
  }

  /**
   * Sets the status bar to the specified text and text color.
   * 
   * @param color
   *          the new color
   * @param text
   *          the new text
   */
  public void setStatusBarText(Color color, String text) {
    GUIBarManager barManager = dockingManager.getBarManager();
    barManager.setStatusBarTextColor(STATUS_BAR, color);
    barManager.setStatusBarText(STATUS_BAR, text);
  }

  /**
   * Sets the status bar to the specified text and text color.
   * 
   * @param font
   *          the new font
   * @param color
   *          the new color
   * @param text
   *          the new text
   */
  public void setStatusBarText(Font font, Color color, String text) {
    GUIBarManager barManager = dockingManager.getBarManager();
    barManager.setStatusBarFont(STATUS_BAR, font);
    barManager.setStatusBarTextColor(STATUS_BAR, color);
    barManager.setStatusBarText(STATUS_BAR, text);
  }

  public void setTitle(String title) {
    if (frame == null) {
      frame = (JFrame) SwingUtilities.getWindowAncestor(treeView.getContentPane());
    }
    frame.setTitle(title);
  }

  /**
   * Gets the main application frame.
   * 
   * @return the main application frame.
   */
  public JFrame getFrame() {
    if (frame == null) {
      frame = (JFrame) SwingUtilities.getWindowAncestor(treeView.getContentPane());
    }
    return frame;
  }

  // view listener implementation

  public void dockableClosed(DockableFrameEvent viewEvent) {
    DockableFrame frame = viewEvent.getDockable();
    IDisplay display = displayViewMap.get(frame);
    if (display != null)
      display.closed();
    nonTreeViews.remove(frame);
    parameterViews.remove(frame);
  }

  public void dockableFloated(DockableFrameEvent viewEvent) {
  }

  public void dockableMaximized(DockableFrameEvent viewEvent) {
  }

  public void dockableMinimized(DockableFrameEvent viewEvent) {
    DockableFrame view = viewEvent.getDockable();
    IDisplay display = displayViewMap.get(view);
    if (display != null) {
      iconified.add(view);
      display.iconified();
    }
  }

  public void dockableRestored(DockableFrameEvent viewEvent) {
    DockableFrame view = viewEvent.getDockable();
    if (iconified.contains(view)) {
      IDisplay display = displayViewMap.get(view);
      display.deIconified();
      iconified.remove(view);
    }
  }

  public void setActiveView(DockableFrame dockable) {
    dockable.toFront();
  }

  public DockableFrame addProbeView(String id, String title, JPanel panel) {
    // on OSX java 7, the scrollpane has a white background and
    // this looks odd when any of the widgets have a gray background
    JScrollPane pane = new JScrollPane(panel);
    pane.getViewport().setBackground(panel.getBackground());
    DockableFrame view = dockingManager.createDockable(id, pane,
        MinimizeLocation.BOTTOM);
    view.setTitle(title);
    dockingManager.addDockableToGroup(DEFAULT_PERSPECTIVE, PROBE_GROUP, view);
    nonTreeViews.add(view);
    return view;
  }

  public void showError(String title, String message) {
    JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Called when probed properties are updated.
   */

  public void propertyChange(PropertyChangeEvent evt) {
    if (!running) {
      for (IDisplay display : displayViewMap.values()) {
        display.update();
        display.render();
      }
    }
  }

  public void dockableClosing(DockableFrameEvent arg0) {
  }

  public void dockableFloating(DockableFrameEvent arg0) {
  }

  public void dockableRestoring(DockableFrameEvent arg0) {
  }

  public void dockableMaximizing(DockableFrameEvent evt) {
  }

  public void dockableMinimizing(DockableFrameEvent evt) {
  }
}
