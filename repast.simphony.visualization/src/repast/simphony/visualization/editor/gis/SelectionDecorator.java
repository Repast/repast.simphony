package repast.simphony.visualization.editor.gis;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.*;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.map.event.MapLayerListListener;
import org.geotools.styling.*;
import org.geotools.styling.Stroke;
import repast.simphony.gis.RepastMapLayer;
import repast.simphony.space.gis.FeatureAgentFactoryFinder;
import repast.simphony.space.gis.FeatureAttributeAdapter;
import repast.simphony.space.gis.GISConstants;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Decorates selected features with a highlight. This is done
 * by adding a rule to the style for selection.
 *
 * @author Nick Collier
 */
public class SelectionDecorator implements MapLayerListListener {

  private static final String HIGHLIGHT_RULE_NAME = "__high_light__";

  private Set<Object> selected = new HashSet<Object>();
  private List<MapLayer> layers = new ArrayList<MapLayer>();
  private StyleBuilder builder = new StyleBuilder();
  private FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();
  private Color highlightColor = Color.decode("#00FFFF");

  class AttributeAdapter implements FeatureAttributeAdapter {

    public Object getAttribute(Object adaptee) {
      return selected.contains(adaptee);
    }

    public String getAttributeName() {
      return GISConstants.SELECTED_ATTRIBUTE_NAME;
    }

    public Class getAttributeType() {
      return Boolean.class;
    }

    public void setAttribute(Object adaptee, Object val) throws IllegalAttributeException {
    }
  }

  private FeatureAttributeAdapter adapter = new AttributeAdapter();

  public SelectionDecorator(MapContext context) {
    context.addMapLayerListListener(this);
  }


  /**
   * Sets up the artificial selected attribute for the
   * specified object type.
   *
   * @param objClass the class of the object
   */
  public void initClass(Class objClass) {
    FeatureAgentFactoryFinder.getInstance().addAdapter(objClass, adapter);
  }


  /**
   * Adds the specified object to the set of selected objects.
   *
   * @param obj the object to add
   */
  public void addSelected(Object obj) {
    selected.add(obj);
  }

  /**
   * Clears the set of selected objects.
   */
  public void clearSelected() {
    selected.clear();
  }

  /**
   * Removes the highlight rules from the styles.
   */
  public void removeHighlightRules() {
    for (MapLayer layer : layers) {
      Style style = layer.getStyle();
      Iterator iter = style.getFeatureTypeStyles()[0].rules().iterator();
      while (iter.hasNext()) {
        Rule rule = (Rule) iter.next();
        if (rule.getName() != null && rule.getName().equals(HIGHLIGHT_RULE_NAME)) {
          iter.remove();
        }
      }
      layer.setStyle(style);
    }
  }

  public void addHighightRules() {
    for (MapLayer layer : layers) {
      Style style = layer.getStyle();
      FeatureType type = layer.getFeatureSource().getSchema();
      Class geomType = type.getDefaultGeometry().getType();
      if (geomType.equals(com.vividsolutions.jts.geom.Polygon.class)
              || geomType.equals(MultiPolygon.class)) {
        highlightPolygon(style);
      } else if (geomType.equals(com.vividsolutions.jts.geom.Point.class)
              || geomType.equals(MultiPoint.class)) {
        highlightPoint(style);
      } else if (geomType.equals(LineString.class)
              || geomType.equals(MultiLineString.class)) {
        highlightLine(style);
      }
      layer.setStyle(style);
    }
  }

  private CompareFilter createFilter() {
    Expression attExp = builder.attributeExpression(GISConstants.SELECTED_ATTRIBUTE_NAME);
    Expression lit = filterFactory.createLiteralExpression(Boolean.TRUE);
    CompareFilter filter = filterFactory.createCompareFilter(FilterType.COMPARE_EQUALS);
    filter.addLeftValue(lit);
    filter.addRightValue(attExp);
    return filter;
  }

  private void highlightLine(Style style) {
    FeatureTypeStyle fts = style.getFeatureTypeStyles()[0];
    Rule highlightRule = builder.createRule(builder.createLineSymbolizer(
            highlightColor, 3));
    highlightRule.setName(HIGHLIGHT_RULE_NAME);
    highlightRule.setFilter(createFilter());
    fts.addRule(highlightRule);
  }

  private void highlightPoint(Style style) {
    FeatureTypeStyle fts = style.getFeatureTypeStyles()[0];
    Mark mark = SLD.pointMark(style);
    Expression expr = mark.getSize();
    mark = builder.createMark(mark.getWellKnownName().toString());
    mark.setSize(expr);
    mark.setStroke(builder.createStroke(highlightColor, 3));
    PointSymbolizer ps = builder.createPointSymbolizer(builder
            .createGraphic(null, mark, null));
    Rule highlightRule = builder.createRule(ps);
    highlightRule.setName(HIGHLIGHT_RULE_NAME);
    highlightRule.setFilter(createFilter());
    fts.addRule(highlightRule);
  }

  private void highlightPolygon(Style style) {
    FeatureTypeStyle fts = style.getFeatureTypeStyles()[0];
    Fill fill = builder.createFill(highlightColor, .01);
    Stroke stroke = builder.createStroke(highlightColor, 2, .9);

    PolygonSymbolizer symbol = builder.createPolygonSymbolizer(stroke, fill);
    Rule highlightRule = builder.createRule(symbol);
    highlightRule.setName(HIGHLIGHT_RULE_NAME);
    highlightRule.setFilter(createFilter());
    fts.addRule(highlightRule);
  }

  public void layerAdded(MapLayerListEvent event) {
    MapLayer layer = event.getLayer();
    if (layer instanceof RepastMapLayer) {
      layers.add(layer);
    }
  }

  public void layerChanged(MapLayerListEvent event) {
  }

  public void layerMoved(MapLayerListEvent event) {
  }

  public void layerRemoved(MapLayerListEvent event) {
    layers.remove(event.getLayer());
  }
}
