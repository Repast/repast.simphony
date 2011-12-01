package repast.simphony.parameter;

/**
 * Test object for Conversion.
 * @author Nick Collier
 */
public class Name {

  private String first, last;

  public Name(String first, String last) {
    this.first = first;
    this.last = last;
  }

  public String getFirst() {
    return first;
  }

  public String getLast() {
    return last;
  }

  public boolean equals(Object obj) {
    if (obj instanceof Name) {
      Name other = (Name) obj;
      return other.first.equals(this.first) && other.last.equals(this.last);
    }
    return false;
  }
}
