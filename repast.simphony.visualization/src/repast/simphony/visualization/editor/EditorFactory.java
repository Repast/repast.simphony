/**
 *
 * @author Nick Collier
 * Date: Sep 5, 2007 9:47:52 AM
 */
package repast.simphony.visualization.editor;

import edu.umd.cs.piccolo.PCanvas;
import repast.simphony.gis.display.PGISCanvas;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.projection.Projection;
import repast.simphony.visualization.DisplayEditorLifecycle;
import static repast.simphony.visualization.editor.DisplayEditor.Mode.ADD_EDGE;
import static repast.simphony.visualization.editor.DisplayEditor.Mode.MOVE;
import repast.simphony.visualization.editor.gis.*;
import repast.simphony.visualization.gis.DisplayGIS;
import repast.simphony.visualization.visualization2D.Display2D;
import repast.simphony.visualization.visualization3D.Display3D;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton factory for creating display editors.
 * @deprecated 2D piccolo based code is being removed
 */
public class EditorFactory {

  private static EditorFactory ourInstance = new EditorFactory();

  public static EditorFactory getInstance() {
    return ourInstance;
  }

  private EditorNotifier notifier = new EditorNotifier();

  private EditorFactory() {
  }

  /**
   * Resets this EditorFactory prior to a simulation run.
   */
  public void reset() {
    notifier.reset();
  }

  /**
   * Creates an editor for editing the specified 3D display using
   * the specified panel. This does not edit in the 3D display
   * directly but rather creates 2D projection(s) of the 3D
   * and allows the user to edit those displays.
   *
   * @param display the display to create the editor for
   * @param panel   a border laid out panel to which any editor components can be added
   * @return an editor for editing a 3D display
   */
  public DisplayEditorLifecycle create3DEditor(Display3D display, JPanel panel) {
    notifier.addDisplay(display);
    return new DisplayEditor3D(display, panel);
  }

  /**
   * Creates an editor for editing a GIS display.
   *
   * @param display the display to create editor for
   * @param canvas  the canvas displayed by the display
   * @param panel   a border laid out panel to which any editor components can be added
   * @return the created editor.
   */
  public DisplayEditorLifecycle createGISEditor(DisplayGIS display, PGISCanvas canvas, JPanel panel) {
    notifier.addDisplay(display);
    DisplayEditorGIS editor = new DisplayEditorGIS(display, panel, notifier);
    GISSelectionHandler selectionHandler = new GISSelectionHandler(display, canvas);
    selectionHandler.addObjectSelectionListener(editor);
    editor.addModalListener(DisplayEditor.Mode.SELECT, selectionHandler);
    editor.addModalListener(MOVE, new GISMoveHandler(canvas, display, notifier));
    editor.initAddListener(new GISAddListener(display,
            new GISAddHandler(canvas, editor)));

    List<Network> nets = new ArrayList<Network>();
    for (Projection proj : (Iterable<Projection>) display.getInitData().getProjections()) {
      if (proj instanceof Network) nets.add((Network) proj);
    }

    if (nets.size() > 0)
      editor.addModalListener(ADD_EDGE, new GISNetAddEdgeHandler(editor, canvas, display));


    return editor;
  }

  /**
   * Creates an editor for editing a 2D display using the specified data, canvas,
   * and panel.
   *
   * @param display the display to create the editor for
   * @param canvas  the canvas displayed by the 2D display for which this is to be an editor
   * @param panel   a border laid out panel to which any editor components can be added
   * @return an editor for editing a 2D display
   */
  public DisplayEditorLifecycle create2DEditor(Display2D display, PCanvas canvas, JPanel panel) {
    DisplayEditor2D editor = new DisplayEditor2D(display, panel, notifier);
    notifier.addDisplay(display);
    GridLocationToolTip ttip = new GridLocationToolTip(canvas.getCamera());
    editor.addModelessListener(ttip);
    SelectionHandler selectionHandler = new SelectionHandler(canvas);
    selectionHandler.addNodeSelectionListener(editor);
    editor.addModalListener(DisplayEditor.Mode.SELECT, selectionHandler);

    Grid grid = null;
    List<Network> nets = new ArrayList<Network>();
    ContinuousSpace space = null;
    for (Projection proj : (Iterable<Projection>) display.getInitData().getProjections()) {
      if (proj instanceof Grid) grid = (Grid) proj;
      if (proj instanceof Network) nets.add((Network) proj);
      if (proj instanceof ContinuousSpace) space = (ContinuousSpace) proj;
    }

    if (grid != null) {
      editor.addModalListener(MOVE, new GridMoveHandler(canvas, display, grid, nets, notifier));
      editor.initAddListener(new GridAddListener(grid, new GridAddHandler(canvas, editor, grid)));
      if (nets.size() > 0) {
        editor.addModalListener(ADD_EDGE, new NetAddEdgeHandler(editor, canvas, display, notifier));
      }
    } else if (space != null) {
      ContSpaceMoveHandler moveHandler = new ContSpaceMoveHandler(canvas, display, space, nets, notifier);
      editor.initAddListener(new SpaceAddListener(space, new SpaceAddHandler(canvas, display, editor, space)));
      editor.addModalListener(MOVE, moveHandler);
      if (nets.size() > 0) {
        editor.addModalListener(ADD_EDGE, new NetAddEdgeHandler(editor, canvas, display, notifier));
      }
    } else if (nets.size() > 0) {
      // just a net and nothing else
      editor.addModalListener(MOVE, new NetMoveHandler(canvas, display, nets));
      editor.addModalListener(ADD_EDGE, new NetAddEdgeHandler(editor, canvas, display, notifier));
      editor.initAddListener(new NetAddListener(canvas, editor, display));
    }

    return editor;
  }
}
