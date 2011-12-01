package repast.simphony.gis.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.text.DecimalFormat;


/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2005/11/21 18:55:17 $
 */
public class DoubleDocument extends PlainDocument {

	private String decPoint;

	public DoubleDocument() {
	  DecimalFormat df = new DecimalFormat();
	  decPoint = String.valueOf(df.getDecimalFormatSymbols().
	          getDecimalSeparator());
	}

	public void insertString(int offs, String string, AttributeSet att)
	        throws BadLocationException {
	  if (string.indexOf(decPoint) != -1 &&
	          getText(0, getLength()).indexOf(decPoint) != -1) {
	    return;
	  }

	  try {
	    if (!string.equals(decPoint)) {
	      Double.parseDouble(string);
	    }
	  } catch (NumberFormatException ex) {
	    return;
	  }

	  super.insertString(offs, string, att);
	}
}

