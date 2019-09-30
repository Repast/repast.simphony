package org.eclipse.gmf.tooling.runtime.directedit.locator;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.tooling.runtime.draw2d.labels.VerticalLabel;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

/**
 * A cell editor locator for rotated images.
 */
public class VerticalLabelCellEditorLocator implements CellEditorLocator {

	private final VerticalLabel myVerticalLabel;

	public VerticalLabelCellEditorLocator(final VerticalLabel verticalLabel) {
		myVerticalLabel = verticalLabel;
	}

	/** {@inheritDoc} */
	public void relocate(final CellEditor celleditor) {
		Control text = celleditor.getControl();
		Rectangle rect = myVerticalLabel.getBounds().getCopy();
		myVerticalLabel.translateToAbsolute(rect);
		int avr = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
		Dimension textSize = new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avr * 2, 0);
		rect.x = rect.x + 3;
		rect.y = rect.y + rect.height / 2 - textSize.height / 2;
		rect.setSize(textSize);
		if (!rect.equals(new Rectangle(text.getBounds()))) {
			text.setBounds(rect.x, rect.y, rect.width, rect.height);
		}
	}
}