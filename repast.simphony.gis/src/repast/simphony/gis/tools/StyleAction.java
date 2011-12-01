package repast.simphony.gis.tools;

import org.geotools.map.MapLayer;
import org.geotools.styling.Style;
import org.jdesktop.swingx.util.SwingWorker;
import repast.simphony.gis.legend.LegendAction;
import repast.simphony.gis.legend.LegendEntry;
import repast.simphony.gis.legend.LegendLayerEntry;
import repast.simphony.gis.styleEditor.StyleDialog;

import javax.swing.*;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2007/05/16 22:04:41 $
 */
public class StyleAction implements LegendAction<LegendLayerEntry> {

	private JFrame frame;


	public StyleAction(JFrame frame) {
		this.frame = frame;
	}

	public boolean canProcess(LegendEntry entry) {
		return true;
	}

	public void execute(LegendLayerEntry entry) {
    final MapLayer layer = entry.getLayer();
      final StyleDialog dialog = new StyleDialog(frame);
			dialog.setMapLayer(layer);
			boolean complete = dialog.display();
			if (complete) {
				SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {

					@Override
					protected Object doInBackground() throws Exception {
						Style style = dialog.getStyle();
						layer.setStyle(style);
						return null;
					}

				};
				worker.execute();
			}
		}


}
