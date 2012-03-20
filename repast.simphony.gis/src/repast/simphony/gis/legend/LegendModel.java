package repast.simphony.gis.legend;

import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListener;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import repast.simphony.gis.display.LegendIconMaker;
import simphony.util.messages.MessageCenter;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.util.*;

/**
 * This is the data model for a legend representing a gis map. It consists of
 * categories and map layer elements.
 *
 * @author $Author: howe $
 * @version $Revision: 1.20 $
 * @date Oct 24, 2006
 */
public class LegendModel extends DefaultTreeModel {

  private static MessageCenter msg = MessageCenter
          .getMessageCenter(LegendModel.class);

  private static final long serialVersionUID = 6188668067237501278L;

  protected LegendEntry root;

  protected int iconWidth = UIManager.getIcon("Tree.openIcon").getIconWidth();

  protected static final Object DEFAULT_ROOT = "Legend";

  protected Object rootObject;

  protected Map<MapLayer, LegendLayerEntry> nodeMap = new HashMap<MapLayer, LegendLayerEntry>();

  //protected Map<MapLayer, MapLayerListener> wrapperListeners = new HashMap<MapLayer, MapLayerListener>();


  /**
   * Create a new LegendModel using the default root object. This will produce
   * a LegendModel where the root is "Legend".
   */
  public LegendModel() {
    super(new LegendEntry(DEFAULT_ROOT));
    root = (LegendEntry) getRoot();
  }

  public LegendModel(String title ) {
    super(new LegendEntry(title + " " + DEFAULT_ROOT));
    root = (LegendEntry) getRoot();
  }

  public void initMapContext(MapContext context) {
    root.removeAllChildren();
    List<MapLayer> layers = new ArrayList<MapLayer>();
    for (MapLayer layer : context.getLayers()) {
      layers.add(layer);
    }
    Collections.sort(layers, new Comparator<MapLayer>() {
      public int compare(MapLayer o1, MapLayer o2) {
        return o1.getStyle().getTitle().compareTo(o2.getStyle().getTitle());
      }
    });

    for (MapLayer layer : layers) {
      LegendEntry entry = createLayerEntry(layer);
      root.add(entry);
    }
  }

  private void addRuleNodes(Style style, LegendLayerEntry layerNode) {
    for (Rule rule : style.getFeatureTypeStyles()[0].getRules()) {
      Icon icon = LegendIconMaker.makeLegendIcon(iconWidth, rule, null);
      LegendRuleEntry ruleNode = new LegendRuleEntry(rule.getTitle(),
              icon, rule);
      insertNodeInto(ruleNode, layerNode, layerNode.getChildCount());
    }
  }

  /**
   * Add a new gis data layer to the legend. The hierarchy should be defined
   * by the objects specified in the category.
   *
   * @param layer the layer to add
   * @return The node created for the layer.
   */
  public LegendEntry createLayerEntry(MapLayer layer) {
    LegendLayerEntry entry = new LegendLayerEntry(layer.getStyle().getTitle(), true, layer);
    nodeMap.put(layer, entry);
    layer.addMapLayerListener(new LayerNodeListener(layer));
    addRuleNodes(layer.getStyle(), entry);

    return entry;
  }

  /**
   * When a GIS layer changes, for example because the style has been updated,
   * update the legend nodes to reflect the new information.
   *
   * @param layer the layer to update.
   */
  public void updateLayer(MapLayer layer) {
    LegendEntry layerEntry = nodeMap.get(layer);
    if (layerEntry != null) {
      while (layerEntry.getChildCount() > 0) {
        this.removeNodeFromParent((LegendEntry) layerEntry.getChildAt(0));
      }
      Style style = layer.getStyle();
      layerEntry.setName(style.getTitle());
      addRuleNodes(style, (LegendLayerEntry) layerEntry);
    }
  }


  class LayerNodeListener implements MapLayerListener {

    private MapLayer layer;

    public LayerNodeListener(MapLayer layer) {
      this.layer = layer;
    }

    public void layerChanged(MapLayerEvent event) {
      if (event.getReason() == MapLayerEvent.STYLE_CHANGED) {
        updateLayer(layer);
      }
    }

    public void layerHidden(MapLayerEvent event) {}
    public void layerShown(MapLayerEvent event) {}
		public void layerDeselected(MapLayerEvent arg0) {}
		public void layerPreDispose(MapLayerEvent arg0) {}
		public void layerSelected(MapLayerEvent arg0) {}
	}
}
