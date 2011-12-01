package watcherOOP.test;

public class WatcheeChildB extends WatcheeRoot {
  
  private int iVal;
  
  public void bIncrVal() {
    dVal++;
  }
  
  public void bIncrValSuper() {
    super.incrVal();
  }
}
