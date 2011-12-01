/**
 * 
 */
package repast.simphony.data2.wizard;

/**
 * Interface for steps that require other classes to notify
 * them of the compeletion status.
 * 
 * @author Nick Collier
 */
public interface CompletableStep {
  
  /**
   * Notifies this CompletableStep that the source is or is not complete.
   * 
   * @param source
   * @param complete
   */
  void complete(Object source, boolean complete);

}
