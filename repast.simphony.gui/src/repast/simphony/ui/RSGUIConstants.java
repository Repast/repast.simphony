package repast.simphony.ui;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import simphony.util.messages.MessageCenter;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/03 18:23:02 $
 */
public abstract class RSGUIConstants {

  private static final MessageCenter LOG = MessageCenter.getMessageCenter(RSGUIConstants.class);
  
  public static final String PARAMETERS_FILE_NAME = "parameters.xml";
  
  public static String GUI_PLUGIN_ID = "repast.simphony.gui";
  public static String FIELD_PROBE_EXT_ID = "field.probe";
  public static String PROBE_LOCATION_PROVIDER_EXT_ID = "probe.location.provider";

  // perspective
  public static String DEFAULT_PERSPECTIVE = "repast.simphony.ui.perspective.default";

  // status bar
  public static String STATUS_BAR = "repast.simphony.ui.statusbar.main";
  public static String STATUS_BAR_VIZ = "repast.simphony.ui.statusbar.viz";
  public static String STATUS_BAR_WARN = "repast.simphony.ui.statusbar.warn";

  // groups
  public static String VISUALIZATION_GROUP = "repast.simphony.ui.view.viz";
  public static String TREE_GROUP = "repast.simphony.ui.view.tree";
  public static String PROBE_GROUP = "repast.simphony.ui.view.probe";
  public static String LOG_GROUP = "repast.simphony.ui.view.log";

  public static String START_ID = "repast.simphony.ui.action.start";
  public static String PAUSE_ID = "repast.simphony.ui.action.pause";
  public static String STEP_ID = "repast.simphony.ui.action.step";
  public static String OPEN_ID = "repast.simphony.ui.action.open";
  public static String SAVE_ID = "repast.simphony.ui.action.save";
  public static String SAVE_AS_ID = "repast.simphony.ui.action.saveas";
  public static String STOP_ID = "repast.simphony.ui.action.stop";
  public static String RESET_ID = "repast.simphony.ui.action.reset";
  public static String INIT_ID = "repast.simphony.ui.action.init";
  public static String SAVE_PARAMS = "repast.simphony.ui.action.saveParams";
  public static String NEW_SCENARIO_ID = "repast.simphony.ui.action.NewScenario";

  public static String RUN_TOOLS = "runTools";

  public static String TREE_VIEW = "tree.view";

  public static String FILE_MENU_ID = "repast.simphony.ui.file_menu";
  public static String RUN_MENU_ID = "repast.simphony.ui.run_menu";
  public static String TOOLS_MENU_ID = "repast.simphony.ui.tools_menu";
  public static String VIEW_MENU_ID = "repast.simphony.ui.view_menu";
  public static String WINDOW_MENU_ID = "repast.simphony.ui.window_menu";

  public static String TICK_GROUP = "repast.simphony.ui.toolbar.tick.group";

  public static String TICK_COUNT_LABEL = "rs.tick.count.label";

  private final static String ICON_FORMAT = ".png";

  public static final String RESET_LAYOUT_ACTION = "repast.simphony.ui.action.ResetLayout";
  public static final String LOAD_LAYOUT_ACTION = "repast.simphony.ui.action.LoadLayout";
  public static final String SAVE_LAYOUT_ACTION = "repast.simphony.ui.action.SaveLayout";
  public static final String SAVE_DEFAULT_LAYOUT_ACTION = "repast.simphony.ui.action.SaveAsDefaultLayout";
  

  public static Icon loadIcon(String name) {
    try {
      return new ImageIcon(RSGUIConstants.class.getClassLoader().getResource(name + ICON_FORMAT));
    } catch (Exception e) {
      LOG.warn("Error loading: " + name + ", it will not be used.");
      return new ImageIcon(new byte[0]);
    }
  }

  public static Icon START_ICON;
  public static Icon PAUSE_ICON;
  public static Icon STOP_ICON;
  public static Icon STEP_ICON;
  public static Icon OPEN_ICON;
  public static Icon SAVE_ICON;
  public static Icon RESET_ICON;
  public static Icon INIT_ICON;
  public static Icon SCENARIO_LEAF_ICON;
  public static Icon SCENARIO_FOLDER_ICON;
  public static Icon SCENARIO_FOLDER_OPEN_ICON;
  public static Icon CAMERA_ICON;
  public static Icon MOVIE_ICON;
  public static Icon VIZ_EDIT_ICON;
  public static Icon VIZ_HOME_ICON;
  public static Icon VIZ_INFO_ICON;
  public static Icon CHART_ICON;
  public static Icon OUTPUTTER_ICON;
  public static Icon DISPLAY_ICON;
  public static Icon DATASET_ICON;
  public static Icon PERSONAL_ICON;
  public static Icon LEAF_ICON;
  public static Icon WARN_ICON;
  public static Icon USER_PANEL_ICON;
  public static Icon DATA_LOADER_ICON;

  static {
    START_ICON = loadIcon("player_play");
    PAUSE_ICON = loadIcon("player_pause");
    STOP_ICON = loadIcon("player_stop");
    STEP_ICON = loadIcon("player_step");
    OPEN_ICON = loadIcon("project_open");
    SAVE_ICON = loadIcon("filesave");
    RESET_ICON = loadIcon("reload");
    INIT_ICON = loadIcon("configure");
    SCENARIO_LEAF_ICON = loadIcon("exec");
    SCENARIO_FOLDER_ICON = loadIcon("folder");
    SCENARIO_FOLDER_OPEN_ICON = loadIcon("folder_open");
    CAMERA_ICON = loadIcon("camera");
    MOVIE_ICON = loadIcon("movie");
    CHART_ICON = loadIcon("chart");
    OUTPUTTER_ICON = loadIcon("database");
    DISPLAY_ICON = loadIcon("display");
    VIZ_INFO_ICON = loadIcon("info_viz");
    DATASET_ICON = loadIcon("kudesigner");
    PERSONAL_ICON = loadIcon("lockstart_session");
    LEAF_ICON = loadIcon("leaf");
    VIZ_EDIT_ICON = loadIcon("edit");
    VIZ_HOME_ICON = loadIcon("home");
    WARN_ICON = loadIcon("info");
    USER_PANEL_ICON = loadIcon("view_sidetree");
    DATA_LOADER_ICON = loadIcon("harddrive");
  }
}
