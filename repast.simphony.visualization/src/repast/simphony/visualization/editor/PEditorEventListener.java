package repast.simphony.visualization.editor;

import org.piccolo2d.event.PInputEventListener;

/**
 * Adds start, stop to PInputEventListener interface.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public interface PEditorEventListener extends PInputEventListener {

  /**
   * Initializes this listener. This happens only once whereas start / stop may
   * occur multiple times.
   */
  void init();

  /**
   * Starts the listener. At the very least this should
   * add the listener to whatever PNode it is listening on.
   */
  void start();

  /**
   * Stops the listener. At the very least this should
   * remove the listener to whatever PNode it is listening on.
   */
  void stop();

  /**
   * Cleans up anything created by this listener in init. This happens only once whereas
   * start / stop may occur multiple times.
   */
  void destroy();
}
