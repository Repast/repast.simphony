package repast.simphony.statecharts.editor;

import org.eclipse.jface.text.TextAttribute;

/**
 * This is a package private class in Eclipse's SemanticHighlightingManager class:
 * https://git.eclipse.org/c/gerrit/jdt/eclipse.jdt.ui.git/tree/org.eclipse.jdt.ui/ui/org/eclipse/jdt/internal/ui/javaeditor/SemanticHighlightingManager.java
 * c3b00ce34a7a5ecfff0485e0f5f0ee7d784b7bec
 * 
 * We need this public so we can implement our own SemanticHighlightingPresenter.
 */

public class Highlighting {

    /** Text attribute */
    private TextAttribute fTextAttribute;
    /** Enabled state */
    private boolean fIsEnabled;

    /**
     * Initialize with the given text attribute.
     * 
     * @param textAttribute
     *            The text attribute
     * @param isEnabled
     *            the enabled state
     */
    public Highlighting(TextAttribute textAttribute, boolean isEnabled) {
	setTextAttribute(textAttribute);
	setEnabled(isEnabled);
    }

    /**
     * @return Returns the text attribute.
     */
    public TextAttribute getTextAttribute() {
	return fTextAttribute;
    }

    /**
     * @param textAttribute
     *            The background to set.
     */
    public void setTextAttribute(TextAttribute textAttribute) {
	fTextAttribute = textAttribute;
    }

    /**
     * @return the enabled state
     */
    public boolean isEnabled() {
	return fIsEnabled;
    }

    /**
     * @param isEnabled
     *            the new enabled state
     */
    public void setEnabled(boolean isEnabled) {
	fIsEnabled = isEnabled;
    }

}
