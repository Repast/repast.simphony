package org.eclipse.gmf.tooling.runtime.directedit.locator;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

public class TextCellEditorLocator implements CellEditorLocator {

	private WrappingLabel myWrapLabel;

	public TextCellEditorLocator(WrappingLabel wrapLabel) {
		myWrapLabel = wrapLabel;
	}

	public WrappingLabel getWrapLabel() {
		return myWrapLabel;
	}

	public void relocate(CellEditor celleditor) {
		Control text = celleditor.getControl();
		Rectangle rect = getWrapLabel().getTextBounds().getCopy();
		getWrapLabel().translateToAbsolute(rect);
		if (getWrapLabel().isTextWrapOn() && getWrapLabel().getText().length() > 0) {
			rect.setSize(new Dimension(text.computeSize(rect.width, SWT.DEFAULT)));
		} else {
			int avr = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
			rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avr * 2, 0));
		}
		if (!rect.equals(new Rectangle(text.getBounds()))) {
			text.setBounds(rect.x, rect.y, rect.width, rect.height);
		}
	}
}
