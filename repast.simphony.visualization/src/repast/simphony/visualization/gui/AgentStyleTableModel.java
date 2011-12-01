package repast.simphony.visualization.gui;

import repast.simphony.ui.editor.ClassListItem;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.engine.DisplayDescriptor;

/**
 * Table model for agent styles.
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class AgentStyleTableModel extends StyleTableModel {

	public AgentStyleTableModel() {
		COLUMN_NAMES = new String[]{"Agent Class", "Style Class"};
	}

	public void loadStyles(DisplayDescriptor descriptor) {
		for (Pair<ClassListItem, ClassListItem> pair: items) {
			descriptor.addStyle(pair.getFirst().getFullName(), pair.getSecond().getFullName());
		}
	}
}
