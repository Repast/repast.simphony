package repast.simphony.ui.probe;

/**
 * @author Nick Collier
*/
public class MutableNumber {

  double val;


  public MutableNumber(double val) {
    this.val = val;
  }

  public double getVal() {
    return val;
  }

  public void setVal(double val) {
    this.val = val;
  }

  public boolean equals(Object obj) {
    if (obj instanceof MutableNumber) {
      return val == ((MutableNumber)obj).val;
    }

    return false;
  }
}
