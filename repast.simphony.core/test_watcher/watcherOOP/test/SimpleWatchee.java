package watcherOOP.test;

public class SimpleWatchee {
  
  private int val;
  private String str;
  
  public void updateString(String val) {
    str = val;
  }
  
  public void updateInt(int val) {
    this.val = val;
  }

  public int getVal() {
    return val;
  }

  public String getStr() {
    return str;
  }
  
}
