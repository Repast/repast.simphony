/*
 * $Id: AutoCompleteDocument.java,v 1.2 2006/04/06 18:51:34 Bierhance Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.autocomplete;

import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A document that can be plugged into any JTextComponent to enable automatic completion.
 * It finds and selects matching items using any implementation of the AbstractAutoCompleteAdaptor.
 */
public class AutoCompleteDocument extends PlainDocument {
    
    /** Flag to indicate if adaptor.setSelectedItem has been called.
     * Subsequent calls to remove/insertString should be ignored
     * as they are likely have been caused by the adapted Component that
     * is trying to set the text for the selected component.*/
    boolean selecting=false;
    
    /**
     * true, if only items from the adaptors's list can be entered
     * false, otherwise (selected item might not be in the adaptors's list)
     */
    boolean strictMatching;
    
    /**
     * The adaptor that is used to find and select items.
     */
    AbstractAutoCompleteAdaptor adaptor;
    
    /**
     * Creates a new AutoCompleteDocument for the given AbstractAutoCompleteAdaptor.
     * 
     * 
     * @param strictMatching true, if only items from the adaptor's list should
     * be allowed to be entered
     * @param adaptor The adaptor that will be used to find and select matching
     * items.
     */
    public AutoCompleteDocument(AbstractAutoCompleteAdaptor adaptor, boolean strictMatching) {
        this.adaptor = adaptor;
        this.strictMatching = strictMatching;
        
        // Handle initially selected object
        Object selected = adaptor.getSelectedItem();
        if (selected!=null) setText(selected.toString());
        adaptor.markEntireText();
    }
    
    /**
     * Returns if only items from the adaptor's list should be allowed to be entered.
     * @return if only items from the adaptor's list should be allowed to be entered
     */
    public boolean isStrictMatching() {
        return strictMatching;
    }
    
    public void remove(int offs, int len) throws BadLocationException {
        // return immediately when selecting an item
        if (selecting) return;
        super.remove(offs, len);
        if (!strictMatching) {
            setSelectedItem(getText(0, getLength()));
            adaptor.getTextComponent().setCaretPosition(offs);
        }
    }
    
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // return immediately when selecting an item
        if (selecting) return;
        // insert the string into the document
        super.insertString(offs, str, a);
        // lookup and select a matching item
        Object item = lookupItem(getText(0, getLength()));
        if (item != null) {
            setSelectedItem(item);
        } else {
            if (strictMatching) {
                // keep old item selected if there is no match
                item = adaptor.getSelectedItem();
                // imitate no insert (later on offs will be incremented by
                // str.length(): selection won't move forward)
                offs = offs-str.length();
                // provide feedback to the user that his input has been received but can not be accepted
                UIManager.getLookAndFeel().provideErrorFeedback(adaptor.getTextComponent());
            } else {
                // no item matches => use the current input as selected item
                item=getText(0, getLength());
                setSelectedItem(item);
            }
        }
        setText(item==null?"":item.toString());
        // select the completed part
        adaptor.markText(offs+str.length());
    }
    
    /**
     * Sets the text of this AutoCompleteDocument to the given text.
     * 
     * @param text the text that will be set for this document
     */
    private void setText(String text) {
        try {
            // remove all text and insert the completed string
            super.remove(0, getLength());
            super.insertString(0, text, null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e.toString());
        }
    }
    
    /**
     * Selects the given item using the AbstractAutoCompleteAdaptor.
     * 
     * @param item the item that is to be selected
     */
    private void setSelectedItem(Object item) {
        selecting = true;
        adaptor.setSelectedItem(item);
        selecting = false;
    }
    
    /**
     * Searches for an item that matches the given pattern. The AbstractAutoCompleteAdaptor
     * is used to access the candidate items. The match is not case-sensitive
     * and will only match at the beginning of each item's string representation.
     * 
     * @param pattern the pattern that should be matched
     * @return the first item that matches the pattern or <code>null</code> if no item matches
     */
    private Object lookupItem(String pattern) {
        Object selectedItem = adaptor.getSelectedItem();
        // only search for a different item if the currently selected does not match
        if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), pattern)) {
            return selectedItem;
        } else {
            // iterate over all items
            for (int i=0, n=adaptor.getItemCount(); i < n; i++) {
                Object currentItem = adaptor.getItem(i);
                // current item starts with the pattern?
                if (currentItem != null && startsWithIgnoreCase(currentItem.toString(), pattern)) {
                    return currentItem;
                }
            }
        }
        // no item starts with the pattern => return null
        return null;
    }
    
    /**
     * Returns true if <code>base</code> starts with <code>prefix</code> (ignoring case).
     * @param base the string to be checked
     * @param prefix the prefix to check for
     * @return true if <code>base</code> starts with <code>prefix</code>; false otherwise
     */
    private boolean startsWithIgnoreCase(String base, String prefix) {
        if (base.length() < prefix.length()) return false;
        return base.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}