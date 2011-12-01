package repast.simphony.batch.gui;

public class ValidationResult {
  
  public static final ValidationResult OK = new ValidationResult("", true);
  
  private boolean isValid = false;
  private String msg = "";

  public ValidationResult(String msg, boolean isValid) {
    this.isValid = isValid;
    this.msg = msg;
  }

  public boolean isValid() {
    return isValid;
  }

  public String getMsg() {
    return msg;
  }
}
