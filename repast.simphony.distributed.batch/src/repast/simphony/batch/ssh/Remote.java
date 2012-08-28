/**
 * 
 */
package repast.simphony.batch.ssh;

import repast.simphony.batch.RunningStatus;

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
   * Gets the id (user@host) of this Remote.
   * 
   * @return the id (user@host) of this Remote.
   */
  String getId();
  
  /**
   * Gets the number of model instances to run on this Remote.
   * 
   * @return the number of model instances to run on this Remote.
   */
  int getInstances();
  
  /**
   * Gets the parameter input for this remote.This should be a string
   * containing a series of lines each of which is a parameter combination.
   * 
   * @return the parameter input for this remote.
   */
  String getInput();
  
  /**
   * Sets the parameter input for this remote. This should be a string
   * containing a series of lines each of which is a parameter combination.
   * 
   * @param input
   */
  void setInput(String input);
  
  /**
   * Sets the run status for the specified instance on this Remote.
   * 
   * @param instance the instance id
   * @param status the status
   */
  void setRunStatus(int instance, RunningStatus status);
  
  /**
   * Gets the status of the specified instance.
   * 
   * @param instance
   * @return the status of the specified instance.
   */
  RunningStatus getStatus(int instance);

}
