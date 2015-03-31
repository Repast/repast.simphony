package repast.simphony.ui.probe;

import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;

/**
 * Utilities class.
 *
 * @author Michael J. North
 * @version $Revision$ $Date$
 */
public class Utils {

  protected static NumberFormat numberFormat = NumberFormat.getInstance();
  static {
    numberFormat.setMaximumFractionDigits(5);
  }

  public static NumberFormat getNumberFormatInstance() {
    return numberFormat;
  }

  public static void setNumberFormatInstance(NumberFormat newNumberFormat) {
    numberFormat = newNumberFormat;
  }
}
