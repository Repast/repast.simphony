/*
 * $Id: ComboBoxAdaptor.java,v 1.5 2006/03/19 18:28:45 Bierhance Exp $
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

/**
 * An implementation of the AbstractAutoCompleteAdaptor that is suitable for JComboBox.
 * 
 * @author Thomas Bierhance
 */
public class ComboBoxAdaptor extends AbstractAutoCompleteAdaptor implements ActionListener {
    
    /** the combobox being adapted */
    private JComboBox comboBox;
    
    /**
     * Creates a new ComobBoxAdaptor for the given combobox.
     * @param comboBox the combobox that should be adapted
     */
    public ComboBoxAdaptor(JComboBox comboBox) {
        this.comboBox = comboBox;
        // mark the entire text when a new item is selected
        comboBox.addActionListener(this);
    }
    
    /**
     * Implementation side effect - do not invoke.
     * @param actionEvent -
     */
    // ActionListener (listening to comboBox)
    public void actionPerformed(ActionEvent actionEvent) {
        markEntireText();
    }
    
    public int getItemCount() {
        return comboBox.getItemCount();
    }
    
    public Object getItem(int index) {
        return comboBox.getItemAt(index);
    }
    
    public void setSelectedItem(Object item) {
        comboBox.setSelectedItem(item);
    }
    
    public Object getSelectedItem() {
        return comboBox.getModel().getSelectedItem();
    }
    
    public JTextComponent getTextComponent() {
        // returning the component of the combobox' editor
        return (JTextComponent) comboBox.getEditor().getEditorComponent();
    }
}