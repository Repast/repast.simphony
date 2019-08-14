package org.eclipse.gmf.tooling.runtime.directedit.locator;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

public class LabelCellEditorLocator implements CellEditorLocator {

	private Label myLabel;

	public LabelCellEditorLocator(Label label) {
		myLabel = label;
	}

	public Label getLabel() {
		return myLabel;
	}

	public void relocate(CellEditor celleditor) {
		Control text = celleditor.getControl();
		Rectangle rect = getLabel().getTextBounds().getCopy();
		getLabel().translateToAbsolute(rect);
		int avr = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
		rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avr * 2, 0));
		if (!rect.equals(new Rectangle(text.getBounds()))) {
			text.setBounds(rect.x, rect.y, rect.width, rect.height);
		}
	}
}
