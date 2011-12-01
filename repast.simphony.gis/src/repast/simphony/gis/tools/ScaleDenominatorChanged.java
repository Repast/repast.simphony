package repast.simphony.gis.tools;

import java.io.Serializable;

public class ScaleDenominatorChanged implements Serializable {

  private static final long serialVersionUID = -4838735674751526626L;

  private double scaleDenomintor;

  private ScaleDenominatorChanged() {

  }

  public ScaleDenominatorChanged(double scaleDenomintor) {
    super();
    this.scaleDenomintor = scaleDenomintor;
  }

  public double getScaleDenomintor() {
    return scaleDenomintor;
  }

  public void setScaleDenomintor(double scaleDenomintor) {
    this.scaleDenomintor = scaleDenomintor;
  }

  public String toString() {
    return "Scale Denominator Changed: " + this.scaleDenomintor;
  }
}
