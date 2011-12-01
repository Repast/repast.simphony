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

package repast.simphony.gis.legend;

import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a widget that represents the legend of some gis space. The legend can
 * be broken up into a series of Categories and subCategories as appropriate.
 *
 * @author $Author: collier $
 * @version $Revision: 1.14 $
 */
public class MapLegend extends JPanel {

	private LegendModel model;

	private JTree legendTree;

	private Map<String, LegendAction<LegendEntry>> layerActions = new HashMap<String, LegendAction<LegendEntry>>();


	/**
	 * Create a MapLegend widget based on the supplied MapContext
	 *
	 * @param context the map context
	 */
	public MapLegend(MapContext context, String title) {
    model = new LegendModel(title);
    model.initMapContext(context);
    init();
		setLegendModel(model);
    expandAll(legendTree, new TreePath(model.getRoot()), true);
  }

	public MapLegend() {
    model = new LegendModel();
    init();
    setLegendModel(model);
  }

	private void init() {
		setLayout(new BorderLayout());
		legendTree = new JTree();
    //legendTree.setRootVisible(false);
    legendTree.setCellRenderer(new LegendCellRenderer());
		add(new JScrollPane(legendTree), BorderLayout.CENTER);
		legendTree.addMouseListener(new CheckboxListener());
		legendTree.addMouseListener(new ContextMenuListener());
	}

	/**
	 * Set the model for the legend widget to draw.
	 *
	 * @param model
	 */
	private void setLegendModel(LegendModel model) {
		this.model = model;
		legendTree.setModel(this.model);
	}

  private void expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}

  /**
	 * Add an action that will be offered when the user right clicks on a layer
	 * entry in the legend.
	 *
	 * @param name
	 *            The name of the action to appear in the context specific menu
	 * @param action
	 *            The action to be executed on the layer.
	 */
	public void addLegendAction(String name, LegendAction<LegendEntry> action) {
		layerActions.put(name, action);
	}

	private class CheckboxListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();

			int row = legendTree.getRowForLocation(x, y);
			TreePath path = legendTree.getPathForRow(row);
			if (path != null) {
				DefaultMutableTreeNode comp = (DefaultMutableTreeNode) path
						.getLastPathComponent();
				if (comp instanceof LegendEntry) {
					Rectangle bounds = legendTree.getPathBounds(path);
					LegendEntry info = (LegendEntry) comp;
					// final MapLayer layer = info.getMapLayer();
					if (x < bounds.x + 21) {
						// layer.setVisible(!layer.isVisible());
						info.setDataVisible(!info.isDataVisible());

						// legendTree.revalidate();
						legendTree.repaint();
						// expandAll(legendTree, new TreePath(getRoot()), true);
					}
				}
			}
		}
	}

	private class ContextMenuListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup(e);
			}
		}

		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup(e);
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup(e);
			}
		}

		@SuppressWarnings("serial")
		private void popup(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			int row = legendTree.getRowForLocation(x, y);
			TreePath path = legendTree.getPathForRow(row);
			if (path != null) {

				DefaultMutableTreeNode comp = (DefaultMutableTreeNode) path
						.getLastPathComponent();
				if (comp instanceof LegendEntry) {
					JPopupMenu menu = new JPopupMenu();
					final LegendEntry layerEntry = (LegendEntry) comp;
					for (final Map.Entry<String, LegendAction<LegendEntry>> entry : layerActions
							.entrySet()) {
						if (entry.getValue().canProcess(layerEntry)) {
							Action action = new AbstractAction() {
								public void actionPerformed(ActionEvent ev) {
									entry.getValue().execute(layerEntry);
								}
							};
							action.putValue(Action.NAME, entry.getKey());
							menu.add(action);
						}
					}
					menu.show(legendTree, x, y);
				}
			}
		}
	}

	public MapLayer getSelectedLayer() {
		TreePath path = legendTree.getSelectionPath();
    if (path == null || path.getPath().length == 0) return null;
    DefaultMutableTreeNode comp = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		if (comp instanceof LegendLayerEntry) {
			LegendLayerEntry info = (LegendLayerEntry) comp;
			return info.getLayer();
		}
		return null;
	}
}