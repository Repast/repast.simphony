package watcherOOP.test;

public class WatcheeChildC extends WatcheeChildA {
  
  private String cStr;
  
  public void cIncrVal() {
    val++;
  }
  
  public void cIncrValSuper() {
    super.aIncrVal();
  }

  public String getStr() {
    return cStr;
  }

  public void setStr(String cStr) {
    this.cStr = cStr;
  }
}
