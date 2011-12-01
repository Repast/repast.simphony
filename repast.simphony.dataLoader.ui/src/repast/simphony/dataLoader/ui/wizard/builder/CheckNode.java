package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.Color;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.umd.cs.piccolo.PLayer;

/**
 * Tree node with a selected checkbox like quality.
 * 
 * 
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:24:11 $
 * @author Nick Collier
 */
public abstract class CheckNode extends DefaultMutableTreeNode implements
		Comparable {

	public static final int NO_CHILDREN = 0;

	public static final int ALL_SELECTED = 1;

	public static final int ALL_DESELECTED = 2;

	public static final int MIXED = 3;

	Color color;

	protected boolean isSelected = false;

	protected boolean cascadeSelection = true;

	private State treeState = ALL_SELECTED_STATE;

	public static class State {
		private Color color = Color.WHITE;

		public State(int state) {
			if (state == CheckNode.MIXED)
				color = Color.LIGHT_GRAY;
		}

		public Color getColor() {
			return color;
		}
	}

	private static State NO_CHILDREN_STATE = new State(NO_CHILDREN);

	private static State ALL_SELECTED_STATE = new State(ALL_SELECTED);

	private static State ALL_DESELECTED_STATE = new State(ALL_DESELECTED);

	private static State MIXED_STATE = new State(MIXED);

	PLayer layer;

	public CheckNode(String title, PLayer layer, boolean isSelected) {
		super(title);
		this.layer = layer;
		if (isSelected) {
			setSelected(true);
		}
	}

	public boolean isCascadeSelection() {
		return cascadeSelection;
	}

	public void setCascadeSelection(boolean cascadeSelection) {
		this.cascadeSelection = cascadeSelection;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public State getTreeState() {
		return treeState;
	}

	private void setTreeState() {
		if (getChildCount() == 0) {
			treeState = NO_CHILDREN_STATE;
		} else {

			boolean allSelected = true;
			boolean allDeselected = true;
			for (Enumeration children = children(); children.hasMoreElements();) {
				CheckNode child = (CheckNode) children.nextElement();
				State childTreeState = child.getTreeState();
				if (childTreeState == MIXED_STATE) {
					treeState = MIXED_STATE;
					allSelected = allDeselected = false;
					break;
				} else if (child.isSelected)
					allDeselected = false;
				else if (!child.isSelected)
					allSelected = false;
			}

			treeState = allSelected ? ALL_SELECTED_STATE
					: allDeselected ? ALL_DESELECTED_STATE : MIXED_STATE;

			if (treeState == ALL_SELECTED_STATE || treeState == MIXED_STATE) {
				isSelected = true;
			} else if (treeState == ALL_DESELECTED_STATE) {
				isSelected = false;
			}
		}

		if (getParent() instanceof CheckNode) {
			CheckNode parent = (CheckNode) getParent();

			if (parent != null) {
				parent.setTreeState();
			}
		}
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
		if (cascadeSelection && children != null) {
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				CheckNode node = (CheckNode) iter.next();
				node.setSelected(selected);
			}
		}

		setTreeState();
	}

	public void sort(Comparator comparator) {
		if (children != null) {
			Collections.sort(children, comparator);
			for (int i = 0; i < children.size(); i++) {
				CheckNode node = (CheckNode) children.get(i);
				node.sort(comparator);
			}
		}
	}

	public void sort() {
		if (children != null) {
			Collections.sort(children);
			for (int i = 0; i < children.size(); i++) {
				CheckNode node = (CheckNode) children.get(i);
				node.sort();
			}
		}

	}

	public int compareTo(Object o) {
		return this.toString().compareTo(o.toString());
	}

	public PLayer getLayer() {
		return layer;
	}

	public abstract Icon getIcon();

}
