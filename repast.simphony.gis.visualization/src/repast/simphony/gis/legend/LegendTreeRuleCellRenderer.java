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
 * LegendTreeStyleElementCellRenderer.java
 *
 * Created on 07 July 2003, 21:29
 */
package repast.simphony.gis.legend;

import javax.swing.tree.DefaultTreeCellRenderer;


/**
 * Rule Cell Renderer, the userObject got from a rule node will generate an icon
 * for being set in the renderer as the rule icon
 *
 * @deprecated 2D piccolo based code is being removed
 */
public class LegendTreeRuleCellRenderer
    extends javax.swing.tree.DefaultTreeCellRenderer {
    public java.awt.Component getTreeCellRendererComponent(
        javax.swing.JTree tree, Object value, boolean selected,
        boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) super.getTreeCellRendererComponent(tree,
                value, selected, expanded, leaf, row, hasFocus);
        LegendRuleEntry entry = (LegendRuleEntry)value;
        renderer.setName(entry.toString());
        renderer.setIcon(entry.getIcon());

        return renderer;
    }
}
