package repast.simphony.batch.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class LongDocument extends PlainDocument {

	public LongDocument() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LongDocument(Content c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	public void insertString(int offs, String string, AttributeSet att) throws BadLocationException {
		try {
			// add 0 so string as +/- then work
			Long.parseLong(string + "0");
		} catch (Exception e) {
			return;
		}
		super.insertString(offs, string, att);
	}

}
