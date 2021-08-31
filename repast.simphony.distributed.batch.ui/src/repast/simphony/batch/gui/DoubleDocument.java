package repast.simphony.batch.gui;

import java.text.DecimalFormat;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class DoubleDocument extends PlainDocument {

	private String decPoint;

	public DoubleDocument() {
		DecimalFormat df = new DecimalFormat();
		decPoint = String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator());
	}

	public void insertString(int offs, String string, AttributeSet att) throws BadLocationException {
		if (string.indexOf(decPoint) != -1 && getText(0, getLength()).indexOf(decPoint) != -1) {
			return;
		}

		try {
			if (!(string.equals(decPoint) || string.equals("-"))) {
				Double.parseDouble(string);
			}
		} catch (NumberFormatException ex) {
			return;
		}

		super.insertString(offs, string, att);
	}

}
