package org.eclipse.gmf.tooling.runtime.draw2d.labels;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.ImageUtilities;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * Rotates a String by 90° counterclockwise.
 */
public class VerticalLabel extends ImageFigure {

	private String myText;

	private Font myImageFont;

	private Color myForegroundColor;

	private Color myBackgroundColor;

	/** {@inheritDoc} */
	@Override
	public void addNotify() {
		updateImage();
		super.addNotify();
	}

	/** {@inheritDoc} */
	@Override
	public void removeNotify() {
		if (super.getImage() != null) {
			super.getImage().dispose();
		}
		super.removeNotify();
	}

	/** {@inheritDoc} */
	@Override
	public Image getImage() {
		boolean update = false;
		if (myImageFont != getFont()) {
			myImageFont = getFont();
			update = true;
		}
		if (needsUpdate(myForegroundColor, getForegroundColor())) {
			myForegroundColor = getForegroundColor();
			update = true;
		}
		if (needsUpdate(myBackgroundColor, getBackgroundColor())) {
			myBackgroundColor = getBackgroundColor();
			update = true;
		}

		if (update) {
			return updateImage();
		}
		return super.getImage();
	}

	/**
	 * Updates the image with the string provided.
	 * 
	 * @param text
	 *          to display
	 */
	public void setText(String text) {
		if (!safeEquals(text, myText)) {
			myText = text;
			updateImage();
		}
	}

	public String getText() {
		return myText;
	}

	/**
	 * Redraws / creates the image of the rotated String.
	 * 
	 * @return image created
	 */
	private Image updateImage() {
		if (super.getImage() != null) {
			super.getImage().dispose();
		}
		if (getText() == null) {
			return null;
		}
		if (getFont() == null) {
			return null;
		}
		String safeText = getText();
		if (safeText.length() == 0) {
			safeText = " "; //#393882, IAE from ImageUtilities for empty string passed 
		}
		Image image = ImageUtilities.createRotatedImageOfString(safeText, getFont(), getForegroundColor(), getBackgroundColor());
		super.setImage(image);
		return image;
	}

	private static final boolean needsUpdate(Color cachedColor, Color actualColor) {
		if (cachedColor == null && actualColor == null) {
			return false;
		}
		return cachedColor == null || !cachedColor.equals(actualColor);
	}

	private static boolean safeEquals(String a, String b) {
		return a == null ? b == null : a.equals(b);
	}
}
