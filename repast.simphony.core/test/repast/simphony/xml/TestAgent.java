package repast.simphony.xml;

/**
 * Agent used to test XML serialization.
 *
 * @author Nick Collier
 */
public class TestAgent {

  private int intVal = 0;
  private String name;

  private ConvertedObj obj;

  public TestAgent() {
  }

  public TestAgent(int val, String name) {
    intVal = val;
    this.name = name;
    obj = new ConvertedObj(val, name);
  }

  public int getIntVal() {
    return intVal;
  }

  public String getName() {
    return name;
  }

  public ConvertedObj getObj() {
    return obj;
  }
}
