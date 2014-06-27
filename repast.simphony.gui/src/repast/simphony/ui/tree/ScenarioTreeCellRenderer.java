package repast.simphony.ui.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import repast.simphony.ui.RSGUIConstants;
import repast.simphony.ui.plugin.ActionUI;

/**
 * Sets the appearance of nodes and leafs in the Scenario Tree.
 * 
 * @author Eric Tatara
 */

public class ScenarioTreeCellRenderer extends DefaultTreeCellRenderer {

	public ScenarioTreeCellRenderer(){
		super();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean isLeaf, int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, selected, expanded, 
				isLeaf, row, hasFocus);

		Object obj = ((DefaultMutableTreeNode) value).getUserObject();

		boolean treeIsEnabled = tree.isEnabled();

		if (obj instanceof ActionUI){
			if(((ActionUI)obj).getLabel().equals("Charts"))
				if (treeIsEnabled)
					setIcon(RSGUIConstants.CHART_ICON);
				else
					setDisabledIcon(RSGUIConstants.CHART_ICON);
			else if(((ActionUI)obj).getLabel().equals("Text Sinks"))
				if (treeIsEnabled)
					setIcon(RSGUIConstants.OUTPUTTER_ICON);
				else
					setDisabledIcon(RSGUIConstants.OUTPUTTER_ICON);
			else if(((ActionUI)obj).getLabel().equals("Displays"))
				if (treeIsEnabled)
					setIcon(RSGUIConstants.DISPLAY_ICON);
				else
					setDisabledIcon(RSGUIConstants.DISPLAY_ICON);
			else if(((ActionUI)obj).getLabel().equals("Data Sets"))
				if (treeIsEnabled)
					setIcon(RSGUIConstants.DATASET_ICON);
				else
					setDisabledIcon(RSGUIConstants.DATASET_ICON);
			else if(((ActionUI)obj).getLabel().equals("User Specified Actions"))
				if (treeIsEnabled)
					setIcon(RSGUIConstants.PERSONAL_ICON);
				else
					setDisabledIcon(RSGUIConstants.PERSONAL_ICON);
			else if(((ActionUI)obj).getLabel().equals("Data Loaders"))
				if (treeIsEnabled)
					setIcon(RSGUIConstants.DATA_LOADER_ICON);
				else
					setDisabledIcon(RSGUIConstants.DATA_LOADER_ICON);
			else if(((ActionUI)obj).getLabel().equals("User Panel"))
				if (treeIsEnabled)
					setIcon(RSGUIConstants.USER_PANEL_ICON);
				else
					setDisabledIcon(RSGUIConstants.USER_PANEL_ICON);
			else if (isLeaf)
				if (treeIsEnabled)
					setIcon(RSGUIConstants.LEAF_ICON);
				else
					setDisabledIcon(RSGUIConstants.LEAF_ICON);
			else if (treeIsEnabled)
				if (expanded)
					setIcon(RSGUIConstants.SCENARIO_FOLDER_OPEN_ICON);
				else
					setIcon(RSGUIConstants.SCENARIO_FOLDER_ICON);
			else 
				if (expanded)
					setDisabledIcon(RSGUIConstants.SCENARIO_FOLDER_OPEN_ICON);
				else
					setDisabledIcon(RSGUIConstants.SCENARIO_FOLDER_ICON);
			
		}
		return this;
	}
}