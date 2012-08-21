/**
 * 
 */
package repast.simphony.batch;

/**
 * Encapsulates info about a Remote ssh reachable host 
 * that simphony models can be run on.
 * 
 * @author Nick Collier
 */
public interface Remote {
  
  /**
   * Gets the name or ip address of the remote host.
   * 
   * @return the name or ip address of the remote host.
   */
  String getHost();
  
  /**
   * Gets the user account used to log into the remote host.
   * 
   * @return the user account used to log into the remote host.
   */
  String getUser();
  
  /**
   * Gets the password used to log into the remote host.
   * 
   * @return the password used to log into the remote host.
   */
  String getPassword();
  
  /**
   * Gets the archive zip file that contains the model and simphony code to run
   * on the remote host.
   * 
   * @return the archive zip file that contains the model and simphony code to run
   * on the remote host.
   */
  String getModelArchive();

}
