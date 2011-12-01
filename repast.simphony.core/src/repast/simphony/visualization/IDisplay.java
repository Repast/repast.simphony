package repast.simphony.visualization;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import repast.simphony.render.Renderer;

/**
 * Interface for displays used in repast simphony. Classes that implement this
 * interface can provide custom displays.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public interface IDisplay extends Renderer {

  /**
   * Enum values for specifying the frequency of laying out a display.
   */
  enum LayoutFrequency {
    ON_NEW, AT_UPDATE, AT_INTERVAL, ON_MOVE
  }

  /**
   * Updates the state of the display to reflect whatever it is that it is
   * displaying.
   */
  void update();

  /**
   * Initializes the display. Called once before the display is made visible.
   */
  void init();

  /**
   * Sets the Layout for the display to use.
   * 
   * @param layout
   *          the layout to use
   */
  void setLayout(Layout<?, ?> layout);

  /**
   * Adds a display listener to this display.
   * 
   * @param listener
   *          the listener to add
   */
  void addDisplayListener(DisplayListener listener);

  /**
   * Sets the frequency of the layout.
   * 
   * @param frequency
   *          the frequency of the layout
   * @param interval
   *          the interval if the frequency is AT_INTERVAL. The interval is in
   *          terms of number of calls to update()
   */
  void setLayoutFrequency(IDisplay.LayoutFrequency frequency, int interval);

  /**
   * Gets a panel that contains the actual gui for visualization.
   * 
   * @return a panel that contains the actual gui for visualization.
   */
  JPanel getPanel();

  /**
   * Registers the specified toolbar with this IDisplay. This IDisplay can then
   * put buttons etc. are on this toolbar.
   * 
   * @param bar
   *          the bar to register
   */
  void registerToolBar(JToolBar bar);

  /**
   * Destroys the display, allowing it to free any resources it may have
   * acquired.
   */
  void destroy();

  /**
   * Notifies this IDisplay that its associated gui widget has been iconified.
   */
  void iconified();

  /**
   * Notifies this IDisplay that its associated gui widget has been deIconified.
   */
  void deIconified();

  /**
   * Notifies this IDisplay that its associated gui widget has been closed.
   */
  void closed();

  /**
   * Adds a probe listener to listen for probe events produced by this IDisplay.
   * 
   * @param listener
   *          the listener to add
   */
  void addProbeListener(ProbeListener listener);

  /**
   * Gets the layout the display uses
   * 
   * @return a layout object
   */
  Layout<?, ?> getLayout();

  /**
   * Creates an DisplayEditor appropriate for editing this display.
   * 
   * @param panel
   *          a JPanel with a BorderLayout to which editor components can be
   *          added
   * @return an DisplayEditor appropriate for editing this display or null if
   *         this display cannot be edited.
   * @deprecated 2D piccolo based code is being removed
   */
  DisplayEditorLifecycle createEditor(JPanel panel);

  /**
   * Resets the home (initial) view of the display
   */
  public void resetHomeView();

  // public void toggleInfoProbe();

}
