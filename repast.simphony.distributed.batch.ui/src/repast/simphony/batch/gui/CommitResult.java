package repast.simphony.batch.gui;
/**
 * 
 */

/**
 * Encapsulates the result of commiting data back to the model,
 * whether it was successful, and error message and so on. 
 * 
 * @author Nick Collier
 */
public class CommitResult {
  
  public static CommitResult SUCCESS = new CommitResult();
  
  private boolean success;
  private String message;
  private int tab;
  
  public CommitResult() {
    this(true, "", -1);
  }

  /**
   * @param success
   * @param message
   * @param tab
   */
  public CommitResult(boolean success, String message, int tab) {
    this.success = success;
    this.message = message;
    this.tab = tab;
  }

  /**
   * @return the success
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @return the tab
   */
  public int getTab() {
    return tab;
  }
}
