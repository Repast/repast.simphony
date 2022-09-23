package repast.simphony.gis.legend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeModel;

import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapLayerEvent;
import org.geotools.map.MapLayerListener;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;

import repast.simphony.gis.styleEditor.StylePreviewFactory;
import simphony.util.messages.MessageCenter;

/**
 * This is the data model for a legend representing a gis map. It consists of
 * categories and map layer elements.
 *
 * @author $Author: howe $
 * @version $Revision: 1.20 $
 * @date Oct 24, 2006
 * 
 * @deprecated 2D piccolo based code is being removed
 */
public class LegendModel extends DefaultTreeModel {

  private static MessageCenter msg = MessageCenter
          .getMessageCenter(LegendModel.class);

  private static final long serialVersionUID = 6188668067237501278L;

  protected LegendEntry root;

  protected int iconWidth = UIManager.getIcon("Tree.openIcon").getIconWidth();

  protected static final Object DEFAULT_ROOT = "Legend";

  protected Object rootObject;

  protected Map<Layer, LegendLayerEntry> nodeMap = new HashMap<Layer, LegendLayerEntry>();

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

  public void initMapContext(MapContent context) {
    root.removeAllChildren();
    List<Layer> layers = new ArrayList<Layer>();
    for (Layer layer : context.layers()) {
      layers.add(layer);
    }
    Collections.sort(layers, new Comparator<Layer>() {
      public int compare(Layer o1, Layer o2) {
        return o1.getStyle().getDescription().getTitle().compareTo(o2.getStyle().getDescription().getTitle());
      }
    });

    for (Layer layer : layers) {
      LegendEntry entry = createLayerEntry(layer);
      root.add(entry);
    }
  }

  private void addRuleNodes(Style style, LegendLayerEntry layerNode) {
  	for (FeatureTypeStyle fts: style.featureTypeStyles()) {
  		for (Rule rule : fts.rules()) {
  			//      Icon icon = LegendIconMaker.makeLegendIcon(iconWidth, rule, null);
  			// TODO Geotools [minor] - might ned to modify this to use the size.
  			Icon icon = StylePreviewFactory.createIcon(rule);  
  			LegendRuleEntry ruleNode = new LegendRuleEntry(rule.getName(), icon, rule);
  			insertNodeInto(ruleNode, layerNode, layerNode.getChildCount());
  		}
  	}
  }

  /**
   * Add a new gis data layer to the legend. The hierarchy should be defined
   * by the objects specified in the category.
   *
   * @param layer the layer to add
   * @return The node created for the layer.
   */
  public LegendEntry createLayerEntry(Layer layer) {
    LegendLayerEntry entry = new LegendLayerEntry(layer.getStyle().getName(), true, layer);
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
  public void updateLayer(Layer layer) {
    LegendEntry layerEntry = nodeMap.get(layer);
    if (layerEntry != null) {
      while (layerEntry.getChildCount() > 0) {
        this.removeNodeFromParent((LegendEntry) layerEntry.getChildAt(0));
      }
      Style style = layer.getStyle();
      layerEntry.setName(style.getName());
      addRuleNodes(style, (LegendLayerEntry) layerEntry);
    }
  }


  class LayerNodeListener implements MapLayerListener {

    private Layer layer;

    public LayerNodeListener(Layer layer) {
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
