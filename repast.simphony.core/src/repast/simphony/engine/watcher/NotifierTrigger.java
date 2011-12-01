package repast.simphony.engine.watcher;


/**
 * Interface for classes that do the actual trigger of a method
 * on a watcher based on a notification that the watchee has
 * changed.
 *
 * @author Nick Collier
 */
interface NotifierTrigger {

  /**
   * Executes the relevant method on the watcher, passing the watchee
   * and value if necessary.
   *
   * @param watcher the target of the method call
   * @param watchee the watchee that triggered the call
   * @param value   the new field value
   */
  public void execute(Object watcher, Object watchee, Object value);
}
