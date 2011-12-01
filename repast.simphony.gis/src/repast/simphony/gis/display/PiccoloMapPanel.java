package repast.simphony.gis.display;

import edu.umd.cs.piccolo.event.PInputEventListener;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import repast.simphony.gis.tools.MapTool;
import repast.simphony.gis.tools.ToolManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This is a very basic panel that holds a canvas and a toolbar.
 *
 * @author Howe
 */
public class PiccoloMapPanel extends JPanel {
  private static final long serialVersionUID = -3638545744874130251L;

  private PGISCanvas canvas;

  private JToolBar toolBar;

  private MapContext context;

  private ToolManager toolMan = new ToolManager();

  /**
   * Create a new panel with no MapContext. This will create a new
   * DefaultMapContext.
   */
  public PiccoloMapPanel() {
    context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
    init();
  }

  /**
   * Create a new panel based on an existing MapContext.
   *
   * @param context The map context to display
   */
  public PiccoloMapPanel(MapContext context) {
    this.context = context;
    init();
  }

  private void init() {
    canvas = new PGISCanvas(context);
    toolBar = toolMan.getToolBar();
    // toolMan.setTool(new PGISPanTool(context, canvas));
    setLayout(new BorderLayout());
    add(canvas, BorderLayout.CENTER);
    add(toolBar, BorderLayout.NORTH);
  }

  /**
   * Add a new tool to the toolBar to interact with the canvas.
   *
   * @param listener   The PInputListener to add
   * @param properties Action Properties
   */
  public void addTool(final PInputEventListener listener,
                      Map<String, Object> properties) {

    Action action = new AbstractAction() {
      public void actionPerformed(ActionEvent ev) {
        PInputEventListener curListener = canvas.getCurrentEventHandler();
        if (curListener instanceof MapTool) {
          ((MapTool) curListener).deactivate();
        }
        canvas.setEventHandler(listener);
        if (listener instanceof MapTool) {
          ((MapTool) listener).activate(PiccoloMapPanel.this);
        }

      }
    };

    for (Entry<String, Object> propEntry : properties.entrySet()) {
      action.putValue(propEntry.getKey(), propEntry.getValue());
    }

    toolMan.addTool(action);
    Boolean defaultTool = (Boolean) properties.get("DEFAULT");
    if (defaultTool != null && defaultTool) {
      toolMan.setTool(action);
      canvas.setEventHandler(listener);
    }
  }

  public ToolManager getToolManager() {
    return toolMan;
  }

  public void addButton(final MapTool tool, Map<String, Object> properties) {
    Action action = new AbstractAction() {
      public void actionPerformed(ActionEvent ev) {
        tool.activate(PiccoloMapPanel.this);
      }
    };
    for (Entry<String, Object> propEntry : properties.entrySet()) {
      action.putValue(propEntry.getKey(), propEntry.getValue());
    }
    toolBar.add(action);
  }

  // public void addTool(final MapTool tool, Map<String, Object> properties) {
  // Action action = new AbstractAction() {
  // public void actionPerformed(ActionEvent ev) {
  // tool.execute(PiccoloMapPanel.this);
  // }
  // };
  // for (Entry<String, Object> propEntry : properties.entrySet()) {
  // action.putValue(propEntry.getKey(), propEntry.getValue());
  // }
  // toolBar.add(action);
  // }

  public JToolBar getToolBar() {
    return toolBar;
  }

  public PGISCanvas getCanvas() {
    return canvas;
  }
}
