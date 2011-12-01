package repast.simphony.data2;

public class ObjectB extends ObjectA {

  private int val = 3;
  private String id;

  public ObjectB() {
  }

  public ObjectB(String id) {
    this.id = id;
  }

  public void setInt(int val) {
    this.val = val;
  }

  public int getInt() {
    return val;
  }

  public String object() {
    return "hello";
  }

  public String getId() {
    return id;
  }

}
