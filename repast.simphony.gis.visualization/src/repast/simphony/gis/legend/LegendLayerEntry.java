package repast.simphony.gis.legend;

import java.awt.Color;

import javax.swing.Icon;

import org.geotools.map.Layer;
import org.geotools.map.MapLayerEvent;
import org.geotools.map.MapLayerListener;

import simphony.util.messages.MessageCenter;

/**
 * Used to keep the information about legend tree root and layer node such as
 * name, expanded icon, collapsed icon, and isSelected when mean isLayerVisible
 * or does user select the checkbox for this layer node
 *
 * @author howe
 * 
 * @deprecated 2D piccolo based code is being removed
 */
public class LegendLayerEntry extends LegendEntry {

	private static final long serialVersionUID = -4542444440166411066L;

	private transient MessageCenter msg = MessageCenter
			.getMessageCenter(LegendModel.class);

	private Layer layer = null;

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
	public LegendLayerEntry(String name, boolean isLayerSelected, Layer layer) {
		super(layer);
		setName(name);
		this.layer = layer;
		setDataVisible(isLayerSelected);
		layer.addMapLayerListener(new MapLayerListener(){

			public void layerChanged(MapLayerEvent event) {
				if(event.getReason() == MapLayerEvent.STYLE_CHANGED){

				}
			}

			public void layerHidden(MapLayerEvent event) {}
			public void layerShown(MapLayerEvent event) {}
			public void layerDeselected(MapLayerEvent arg0) {}
			public void layerPreDispose(MapLayerEvent arg0) {}
			public void layerSelected(MapLayerEvent arg0) {}

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

	public Layer getLayer() {
		return layer;
	}

	public Icon getIcon(boolean expanded) {
		return null;
	}
}
