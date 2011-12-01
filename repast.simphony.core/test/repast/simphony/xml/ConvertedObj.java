package repast.simphony.xml;

/**
 * Sample object that uses a converter.
 *
 * @author Nick Collier
 */
public class ConvertedObj {

  private String name;
  private int id;

  public ConvertedObj(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
