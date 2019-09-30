package org.eclipse.gmf.tooling.runtime.draw2d.labels;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.label.ILabelDelegate;
import org.eclipse.swt.graphics.Image;

/**
 * A delegate for a simple label.
 */
public class SimpleLabelDelegate extends ILabelDelegate.Stub {

	private final Label myLabel;

	private boolean myIsSelected;

	public SimpleLabelDelegate(final Label label) {
		super();
		myLabel = label;
	}

	@Override
	public void setFocus(final boolean b) {
		// not supported
	}

	@Override
	public void setSelected(final boolean selected) {
		// ui effect not supported
		myIsSelected = selected;
	}

	@Override
	public String getText() {
		return myLabel.getText();
	}

	@Override
	public void setIcon(final Image image, final int index) {
		if (index == 0) {
			myLabel.setIcon(image);
		}
	}

	@Override
	public void setAlignment(final int right) {
		myLabel.setLabelAlignment(right);
	}

	@Override
	public void setText(final String text) {
		myLabel.setText(text);
	}

	@Override
	public void setTextAlignment(final int alignment) {
		myLabel.setTextAlignment(alignment);
	}

	@Override
	public void setIconAlignment(final int alignment) {
		myLabel.setIconAlignment(alignment);
	}

	@Override
	public Rectangle getTextBounds() {
		Rectangle rect = myLabel.getTextBounds().getCopy();
		myLabel.translateToAbsolute(rect);
		return rect;
	}

	@Override
	public void setTextPlacement(final int placement) {
		myLabel.setTextPlacement(placement);
	}

	@Override
	public void setTextStrikeThrough(final boolean strikeThrough) {
		// not supported
	}

	@Override
	public void setTextUnderline(final boolean underline) {
		// not supported
	}

	@Override
	public Image getIcon(final int index) {
		if (index == 0) {
			return myLabel.getIcon();
		}
		return null;
	}

	@Override
	public int getIconAlignment() {
		return myLabel.getIconAlignment();
	}

	@Override
	public int getTextAlignment() {
		return myLabel.getTextAlignment();
	}

	@Override
	public int getTextPlacement() {
		return myLabel.getTextPlacement();
	}

	@Override
	public boolean hasFocus() {
		return myLabel.hasFocus();
	}

	@Override
	public boolean isSelected() {
		return myIsSelected;
	}

	@Override
	public boolean isTextStrikedThrough() {
		return false;
	}

	@Override
	public boolean isTextUnderlined() {
		return false;
	}

}

/* Copyright (c) Avaloq License AG */