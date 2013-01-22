/**
 * 
 */
package repast.simphony.statecharts.part;


/**
 * Interface for classes that wish integrate the statechart code into a compilation unit. 
 * 
 * @author Nick Collier
 */
public interface StatechartCodeAdder {
  
  /**
   * Performs the code adding on a compilation unit.
   */
  void run(String packageName, String className, String fqAgentName) throws Exception;
  
 
}
