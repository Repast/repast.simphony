package repast.simphony.gis.legend;

import org.geotools.map.MapLayer;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListener;
import simphony.util.messages.MessageCenter;

import javax.swing.*;
import java.awt.*;

/**
 * Used to keep the information about legend tree root and layer node such as
 * name, expanded icon, collapsed icon, and isSelected when mean isLayerVisible
 * or does user select the checkbox for this layer node
 *
 * @author howe
 */
public class LegendLayerEntry extends LegendEntry {

	private static final long serialVersionUID = -4542444440166411066L;

	private transient MessageCenter msg = MessageCenter
			.getMessageCenter(LegendModel.class);

	private MapLayer layer = null;

	/**
	 * Create a new LegendLayer node for a layer.
	 *
	 * @param name
	 *            The name of this layer
	 * @param isLayerSelected
	 *            Is this layer currently selected
	 * @param layer
	 *            The layer upon which the node is based
	 */
	public LegendLayerEntry(String name, boolean isLayerSelected, MapLayer layer) {
		super(layer);
		setName(name);
		this.layer = layer;
		setDataVisible(isLayerSelected);
		layer.addMapLayerListener(new MapLayerListener(){

			public void layerChanged(MapLayerEvent event) {
				if(event.getReason() == MapLayerEvent.STYLE_CHANGED){

				}
			}

			public void layerHidden(MapLayerEvent event) {
				// TODO Auto-generated method stub

			}

			public void layerShown(MapLayerEvent event) {
				// TODO Auto-generated method stub

			}

		});
	}

	public void setDataVisible(boolean dataVisible) {
		layer.setVisible(dataVisible);
		msg.debug("Selected Status Changed for " + getName() + " : "
				+ dataVisible);
	}

	/**
	 * Set the selected status of this entry and whether or not the selected
	 * status should be propogated to children. In this case, propogation does
	 * nothing as this has no children.
	 *
	 * @param dataVisible
	 * @param propogateChanges
	 *            whether not the changes should be propogated to children
	 */
	@Override
	public void setDataVisible(boolean dataVisible, boolean propogateChanges) {
		this.setDataVisible(dataVisible);
	}

	public boolean isDataVisible() {
		return layer.isVisible();
	}

	public Color getBackground(boolean selected) {
		Color c = null;
		if (selected) {
			c = new Color(204, 204, 255);
		} else {
			c = new Color(255, 255, 255);
		}
		return c;
	}

	public MapLayer getLayer() {
		return layer;
	}

	public Icon getIcon(boolean expanded) {
		return null;
	}
}
