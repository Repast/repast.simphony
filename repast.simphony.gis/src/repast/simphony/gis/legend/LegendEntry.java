package repast.simphony.gis.legend;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * This represents a generic entry in the legend. This will be selectable and
 * have children.
 *
 * @author $Author: howe $
 * @version $Revision: 1.6 $
 * @date Oct 24, 2006
 */
public class LegendEntry extends DefaultMutableTreeNode implements
				java.io.Serializable {

	private static final long serialVersionUID = -3990765642560103023L;
	private boolean dataVisible = true;
	private String name = "";

	/**
	 * Default Constructor, this will be an empty entry in the legend.
	 */
	public LegendEntry() {
		super();
	}

	/**
	 * Create the legend entry based on the supplied user object. The name of
	 * the entry will be the value of the user object.
	 *
	 * @param userObject The object on which to base this legend entry.
	 */
	public LegendEntry(Object userObject) {
		super(userObject);
		setName(userObject.toString());
	}

	/**
	 * Set the name of this Legend Entry.
	 *
	 * @param name The name for this legend entry.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the name of this Legend Entry
	 *
	 * @return The name of this legend entry.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the icon for this entry. The generic legend entry has no icon
	 * associated with it.
	 *
	 * @param selected Is this entry selected?
	 * @return The icon, null in the general case.
	 */
	public Icon getIcon(boolean selected) {
		return null;
	}

	/**
	 * Get the background color for this node based on whether the node is
	 * selected or not.
	 *
	 * @param selected Is this entry currently selected?
	 * @return The background color for this entry.
	 */
	public Color getBackground(boolean selected) {
		Color c = null;
		if (selected) {
			c = new Color(204, 204, 255);
		} else {
			c = new Color(255, 255, 255);
		}
		return c;
	}

	/**
	 * Is this entry currently selected?
	 *
	 * @return Whether the entry is selected or not.
	 */
	public boolean isDataVisible() {
		return dataVisible;
	}

	/**
	 * Set the selected status of this entry.
	 *
	 * @param dataVisible
	 */
	public void setDataVisible(boolean dataVisible) {
		setDataVisible(dataVisible, true);
	}

	/**
	 * Set the selected status of this entry and whether or not
	 * the selected status should be propogated to children.
	 *
	 * @param dataVisible
	 * @param propogateChanges whether not the changes should be propogated to
	 * children
	 */
	public void setDataVisible(boolean dataVisible, boolean propogateChanges) {
		this.dataVisible = dataVisible;
		if (propogateChanges) {
			for (int i = 0; i < this.getChildCount(); i++) {
				if (getChildAt(i) instanceof LegendEntry) {
					((LegendEntry) getChildAt(i)).setDataVisible(dataVisible);
				}
			}
		}
	}

	public String toString() {
		return name;
	}
}

