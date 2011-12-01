package repast.simphony.visualization;

/**
 * Interface for classes that manage a display editor's lifecycle.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public interface DisplayEditorLifecycle {

  /**
   * Runs the editor.
   */
  void run();

  /**
   * Stops the Editor and performs any clean up.
   */
  void stop();

}
