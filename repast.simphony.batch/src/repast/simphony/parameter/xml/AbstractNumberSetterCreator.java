package repast.simphony.parameter.xml;

import java.util.regex.Pattern;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class AbstractNumberSetterCreator extends AbstractParameterSetterCreator {

  protected Type type;

  protected String trim(String val) {
		String end = val.substring(val.length() - 1, val.length());
		if (end.equalsIgnoreCase("f") || end.equalsIgnoreCase("l")) return val.substring(0, val.length() - 1);
		return val;
	}

  protected boolean isDouble(String val) {
    return Pattern.matches("\\-?\\d*\\.\\d+", val);
  }

  protected boolean isLong(String val) {
		String end = val.substring(val.length() - 1, val.length());
		return end.equalsIgnoreCase("l");
	}

	protected boolean isFloat(String val) {
		String end = val.substring(val.length() - 1, val.length());
		return end.equalsIgnoreCase("f");
	}

	protected boolean isInt(Double value) {
		return  Math.rint(value) == value;
	}

	protected enum Type {DOUBLE, FLOAT, LONG, INT, SHORT, BYTE}
}
