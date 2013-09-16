/**
 * 
 */
package repast.simphony.batch.ssh;

import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.tuple.Pair;

import repast.simphony.batch.RunningStatus;

/**
 * @author Nick Collier
 */
public interface Session {
  
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
   * Gets the file that contains parameter input for this Session.
   * 
   * @return the parameter input for this remote.
   */
  String getInput();
  
  /**
   * Sets the file that contains the parameter input for this Session.
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

  /**
   * Create a callable that returns with the model run by this
   * Session is finished.
   * 
   * @return a callable that returns with the model run by this
   * Session is finished.
   */
   Callable<Void> createDonePoller(long frequency);

  /**
   * Copies the completion status from the host and directory where the model was run
   * to the specified directory.
   * 
   * @param outDirectory
   * @throws StatusException
   */
   void copyCompletionStatus(String outDirectory) throws StatusException;

  /**
   * Finds the model output of that is the result of running this Session and returns that
   * those files. In the case of remote output the output may be copied to local temporary location.
   * The patterns used to identify output is specified in the filePatterns parameters.
   * 
   * @param filePatterns the first is the output file name, the second is the pattern
   * @return the location of the output in a list of MatchedFiles. Each MatchedFiles object
   * holds one or more files for a specific match.
   * 
   * @throws StatusException 
   */
   List<MatchedFiles> findOutput(List<Pair<String, String>> filePatterns) throws StatusException;

  /**
   * Retrieves the run completion status (e.g. FAILURE) and
   * sets it for this Session.
   * 
   * @throws StatusException 
   */
   void retrieveRunCompletionStatus() throws StatusException;

  /**
   * Runs the model for this Session.
   *
   * @throws SessionException
   */
   void runModel() throws SessionException;

  /**
   * Initializes the model for running. This configures the model archive
   * for this Session and peforms any other archive related work.
   * 
   * @param directory the directory to copy the model archive into
   * 
   * @throws ModelArchiveConfiguratorException 
   * @throws SessionException 
   */
   void initModelArchive(Configuration config, String directory) throws ModelArchiveConfiguratorException,
      SessionException;

}
