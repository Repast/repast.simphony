/**
 * 
 */
package repast.simphony.statecharts.editor;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

/**
 * This is a package private class in Eclipse's SemanticHighlightingManager class:
 * https://git.eclipse.org/c/gerrit/jdt/eclipse.jdt.ui.git/tree/org.eclipse.jdt.ui/ui/org/eclipse/jdt/internal/ui/javaeditor/SemanticHighlightingManager.java
 * c3b00ce34a7a5ecfff0485e0f5f0ee7d784b7bec
 * 
 * We need this public so we can implement our own SemanticHighlightingPresenter.
 */
public class HighlightedPosition extends Position {

    /** Highlighting of the position */
    private Highlighting fStyle;

    /** Lock object */
    private Object fLock;

    /**
     * Initialize the styled positions with the given offset, length and foreground
     * color.
     *
     * @param offset
     *            The position offset
     * @param length
     *            The position length
     * @param highlighting
     *            The position's highlighting
     * @param lock
     *            The lock object
     */
    public HighlightedPosition(int offset, int length, Highlighting highlighting, Object lock) {
	super(offset, length);
	fStyle = highlighting;
	fLock = lock;
    }

    /**
     * @return Returns a corresponding style range.
     */
    public StyleRange createStyleRange() {
	int len = 0;
	if (fStyle.isEnabled())
	    len = getLength();

	TextAttribute textAttribute = fStyle.getTextAttribute();
	int style = textAttribute.getStyle();
	int fontStyle = style & (SWT.ITALIC | SWT.BOLD | SWT.NORMAL);
	StyleRange styleRange = new StyleRange(getOffset(), len, textAttribute.getForeground(),
		textAttribute.getBackground(), fontStyle);
	styleRange.strikeout = (style & TextAttribute.STRIKETHROUGH) != 0;
	styleRange.underline = (style & TextAttribute.UNDERLINE) != 0;

	return styleRange;
    }

    /**
     * Uses reference equality for the highlighting.
     *
     * @param off
     *            The offset
     * @param len
     *            The length
     * @param highlighting
     *            The highlighting
     * @return <code>true</code> iff the given offset, length and highlighting are
     *         equal to the internal ones.
     */
    public boolean isEqual(int off, int len, Highlighting highlighting) {
	synchronized (fLock) {
	    return !isDeleted() && getOffset() == off && getLength() == len && fStyle == highlighting;
	}
    }

    /**
     * Is this position contained in the given range (inclusive)? Synchronizes on
     * position updater.
     *
     * @param off
     *            The range offset
     * @param len
     *            The range length
     * @return <code>true</code> iff this position is not delete and contained in
     *         the given range.
     */
    public boolean isContained(int off, int len) {
	synchronized (fLock) {
	    return !isDeleted() && off <= getOffset() && off + len >= getOffset() + getLength();
	}
    }

    public void update(int off, int len) {
	synchronized (fLock) {
	    super.setOffset(off);
	    super.setLength(len);
	}
    }

    /*
     * @see org.eclipse.jface.text.Position#setLength(int)
     */
    @Override
    public void setLength(int length) {
	synchronized (fLock) {
	    super.setLength(length);
	}
    }

    /*
     * @see org.eclipse.jface.text.Position#setOffset(int)
     */
    @Override
    public void setOffset(int offset) {
	synchronized (fLock) {
	    super.setOffset(offset);
	}
    }

    /*
     * @see org.eclipse.jface.text.Position#delete()
     */
    @Override
    public void delete() {
	synchronized (fLock) {
	    super.delete();
	}
    }

    /*
     * @see org.eclipse.jface.text.Position#undelete()
     */
    @Override
    public void undelete() {
	synchronized (fLock) {
	    super.undelete();
	}
    }

    /**
     * @return Returns the highlighting.
     */
    public Highlighting getHighlighting() {
	return fStyle;
    }
    
    public void setStyle(Highlighting style) {
	fStyle = style;
    }

}
