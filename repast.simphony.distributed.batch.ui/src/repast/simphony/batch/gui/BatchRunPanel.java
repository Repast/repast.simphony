package repast.simphony.batch.gui;

/**
 * Interface for panels that appear in the Batch Run UI.
 * 
 * @author Nick Collier
 */
public interface BatchRunPanel {
  
  /**
   * Initializes the panel with the model. 
   * 
   * @param model
   */
  void init(BatchRunConfigBean model);
  
  /**
   * Commits changes to the model.
   * 
   * @param model
   * @return TODO
   */
  CommitResult commit(BatchRunConfigBean model);

}
