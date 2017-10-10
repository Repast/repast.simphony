package repast.simphony.gis.legend;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;


/**
 * Renderer for drawing the cells in a Legend tree. This class delegates the
 * actual rendering to one of the specific renderers that draw the particular
 * types of nodes included in a legend tree.
 *
 * @author $Author: howe $
 * @version $Revision: 1.7 $
 * @date Oct 24, 2006
 * 
 * @deprecated 2D piccolo based code is being removed
 */
public class LegendCellRenderer implements TreeCellRenderer {

	private LegendTreeRuleCellRenderer ruleRenderer = new LegendTreeRuleCellRenderer();

	private LegendTreeLayerCellRenderer layerRenderer = new LegendTreeLayerCellRenderer();

	private LegendDefaultCellRenderer defaultLegendRenderer = new LegendDefaultCellRenderer();

	private DefaultTreeCellRenderer rootRenderer = new DefaultTreeCellRenderer();

	/**
	 * Render the Cell in the Legend tree.
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leav, int row,
			boolean hasFocus) {
		rootRenderer.setLeafIcon(null);
		rootRenderer.setClosedIcon(null);
		rootRenderer.setOpenIcon(null);
		TreeCellRenderer tcr = rootRenderer;
		// TreeCellRenderer tcr = null;
		if (value instanceof LegendRuleEntry) {
			tcr = ruleRenderer;
		} else if (value instanceof LegendLayerEntry) {
			tcr = layerRenderer;
		} else if (value instanceof LegendEntry) {
			tcr = defaultLegendRenderer;
		}

		return tcr.getTreeCellRendererComponent(tree, value, sel, expanded,
				leav, row, hasFocus);
	}

}
