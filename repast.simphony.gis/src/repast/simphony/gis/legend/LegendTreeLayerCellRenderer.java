/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
/*
 * LegendTreeCellRender.java
 *
 * Created on 03 July 2003, 20:39
 */
package repast.simphony.gis.legend;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer for rendering root and layer Cell in LegendTree
 *
 * @author jianhuij
 */
public class LegendTreeLayerCellRenderer extends javax.swing.JPanel implements
        javax.swing.tree.TreeCellRenderer {

  /**
   * if the note is a layer
   */
  private javax.swing.JCheckBox legendNoteCheckBox;

  private javax.swing.JLabel treeNoteIconJLabel;


  /**
   * Creates a new instance of LegendTreeCellRender
   */
  public LegendTreeLayerCellRenderer() {
    super();
    initComponents();
  }

  /**
   * This method is called from within the constructor to initialize the form.
   */
  private void initComponents() {
    treeNoteIconJLabel = new javax.swing.JLabel();
    legendNoteCheckBox = new javax.swing.JCheckBox();
    legendNoteCheckBox.setBorder(BorderFactory.createEmptyBorder());

    setLayout(new java.awt.BorderLayout());

    treeNoteIconJLabel.setBackground(new java.awt.Color(255, 255, 255));
    treeNoteIconJLabel.setForeground(new java.awt.Color(255, 255, 255));
    treeNoteIconJLabel
            .setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    add(treeNoteIconJLabel, java.awt.BorderLayout.WEST);

    legendNoteCheckBox.setBackground(new java.awt.Color(255, 255, 255));
    legendNoteCheckBox.setSelected(true);
    legendNoteCheckBox.setText("\"Legend\"");
    add(legendNoteCheckBox, java.awt.BorderLayout.CENTER);
  }

  public boolean isLayerSelected() {
    return this.legendNoteCheckBox.isSelected();
  }

  public void setLayerSelected(boolean sel) {
    this.legendNoteCheckBox.setSelected(sel);
  }

  /**
   * set the note name in the checkbox also remove and readd in the center of
   * the boxlayout of the panel to have enough space for the name string,
   * since the component was initialise with the string "Legend" that might
   * shorter than the note's real name
   *
   * @param name DOCUMENT ME!
   */
  public void setText(String name) {
    remove(legendNoteCheckBox);
    legendNoteCheckBox.setText(name);
    add(legendNoteCheckBox, java.awt.BorderLayout.CENTER);
  }

  public String getText() {
    return this.legendNoteCheckBox.getText();
  }

  public java.awt.Component getTreeCellRendererComponent(
          javax.swing.JTree tree, Object value, boolean selected,
          boolean expanded, boolean leaf, int row, boolean hasFocus) {
    // the value object actually is the note object, so we could get its
    // userObject
    // the userObject is designed to be a LegendTreeCellRender Object, so it
    // could be both a parameters keeper
    // and a renderer.
    LegendLayerEntry userObject = (LegendLayerEntry) value;
    setText(userObject.toString());

    // this select means the if the layer is selected whereas the selected
    // as one of the input parameters
    // means if the note is selected in the tree
    setLayerSelected(userObject.isDataVisible());
    setBackground(userObject.getBackground(selected));
    treeNoteIconJLabel.setIcon(userObject.getIcon(expanded));
    Color c;
    if (selected) {
      c = new Color(204, 204, 255);
    } else {
      c = new Color(255, 255, 255);
    }
    legendNoteCheckBox.setBackground(c);

    return this;
	}
}
