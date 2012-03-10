package repast.simphony.gis.display;

import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.feature.FeatureIterator;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListener;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;

import simphony.util.messages.MessageCenter;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PLayer;

public class PDynamicGisLayer extends PLayer implements MapLayerListener,
		PropertyChangeListener {
	MessageCenter center = MessageCenter.getMessageCenter(getClass());

	private static final long serialVersionUID = 5545539438199113098L;

	MapLayer layer;

	MapContext context;

	FeatureTypeStyle fts;

	List<PointFeature> nodes = new ArrayList<PointFeature>();

	public PDynamicGisLayer(MapLayer layer, MapContext context) {
		this.layer = layer;
		this.context = context;
		// getTransformReference(true).scale(1, -1);
		// Envelope e = context.getAreaOfInterest();
		// animateToBounds(e.getMinX(), e.getMaxY(), e.getWidth(),
		// e.getHeight(), 0);
		Style s = layer.getStyle();
		FeatureTypeStyle[] ftss = s.getFeatureTypeStyles();
		for (FeatureTypeStyle fts : ftss) {
			if (fts.getFeatureTypeName().equals(
					layer.getFeatureSource().getSchema().getTypeName())) {
				this.fts = fts;
			}
		}
	}

	public void init() {
		FeatureIterator it;
		try {
			it = layer.getFeatureSource().getFeatures().features();
			while (it.hasNext()) {
				SimpleFeature feature = it.next();
				PointFeature node = new PointFeature(context, feature, layer
						.getStyle().getFeatureTypeStyles()[0]);
				addChild(node);
				nodes.add(node);
				node.update();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		for (PointFeature node : nodes) {
			node.update();
		}
	}

	public void componentHidden(ComponentEvent e) {

	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {

	}

	public void componentShown(ComponentEvent e) {

	}

	public void layerChanged(MapLayerEvent arg0) {
		this.removeAllChildren();
		this.nodes.clear();
		if (arg0.getReason() == MapLayerEvent.DATA_CHANGED) {
			init();
			this.repaint();
		}
		center.debug("Layer changed: "
				+ layer.getFeatureSource().getSchema().getTypeName());
	}

	public void layerHidden(MapLayerEvent arg0) {
		setVisible(false);
	}

	public void layerShown(MapLayerEvent arg0) {
		setVisible(true);
	}

	public void propertyChange(PropertyChangeEvent evt) {

		if (!evt.getPropertyName().equals(PCamera.PROPERTY_VIEW_TRANSFORM)) {
			return;
		}
		PCamera camera = (PCamera) evt.getSource();
		PGISCanvas canvas = (PGISCanvas) camera.getComponent();

		for (int i = 0; i < getChildrenCount(); i++) {
			PointFeature node = (PointFeature) getChild(i);
			// node.setScaleDenominator(canvas.getScaleDenominator());
		}
		// for (Object o : getChildrenReference()) {
		// if (o instanceof PPointFeatureNode) {
		// ((PPointFeatureNode) o).updateLocalBounds(camera.getViewScale());
		// }
		// }
	}
}
