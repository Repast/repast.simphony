package repast.simphony.visualization.editor.gis;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapLayerListEvent;
import org.geotools.map.MapLayerListListener;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import repast.simphony.space.gis.FeatureAgentFactoryFinder;
import repast.simphony.space.gis.FeatureAttributeAdapter;
import repast.simphony.space.gis.GISConstants;

/**
 * Decorates selected features with a highlight. This is done
 * by adding a rule to the style for selection.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class SelectionDecorator implements MapLayerListListener {
  private static final String HIGHLIGHT_RULE_NAME = "__high_light__";

  private Set<Object> selected = new HashSet<Object>();
  private List<Layer> layers = new ArrayList<Layer>();
  private StyleBuilder builder = new StyleBuilder();
  FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory( null );
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

  public SelectionDecorator(MapContent context) {
//    context.addMapLayerListListener(this);
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
    for (Layer layer : layers) {
      Style style = layer.getStyle();
      
      Iterator iter = style.featureTypeStyles().toArray(
      		new FeatureTypeStyle[0])[0].rules().iterator();
      while (iter.hasNext()) {
        Rule rule = (Rule) iter.next();
        if (rule.getName() != null && rule.getName().equals(HIGHLIGHT_RULE_NAME)) {
          iter.remove();
        }
      }
      ((FeatureLayer)layer).setStyle(style);
    }
  }

  public void addHighightRules() {
    for (Layer layer : layers) {
      Style style = layer.getStyle();
      SimpleFeatureType type = (SimpleFeatureType)layer.getFeatureSource().getSchema();
      Class geomType = type.getGeometryDescriptor().getType().getBinding();
      if (geomType.equals(org.locationtech.jts.geom.Polygon.class)
              || geomType.equals(MultiPolygon.class)) {
        highlightPolygon(style);
      } else if (geomType.equals(org.locationtech.jts.geom.Point.class)
              || geomType.equals(MultiPoint.class)) {
        highlightPoint(style);
      } else if (geomType.equals(LineString.class)
              || geomType.equals(MultiLineString.class)) {
        highlightLine(style);
      }
      ((FeatureLayer)layer).setStyle(style);
    }
  }

  private BinaryComparisonOperator createFilter() {
    Expression attExp = builder.attributeExpression(GISConstants.SELECTED_ATTRIBUTE_NAME);
    Expression lit =  filterFactory.literal(Boolean.TRUE);
    
    return filterFactory.equals(attExp, lit);
  }

  private void highlightLine(Style style) {
    FeatureTypeStyle fts = style.featureTypeStyles().toArray(
    		new FeatureTypeStyle[0])[0];
    Rule highlightRule = builder.createRule(builder.createLineSymbolizer(
            highlightColor, 3));
    highlightRule.setName(HIGHLIGHT_RULE_NAME);
    highlightRule.setFilter(createFilter());
    fts.rules().add(highlightRule);
  }

  private void highlightPoint(Style style) {
    
  	// TODO update to Geotools 8.6 if GIS editor is renabled.
  	
//    FeatureTypeStyle fts = style.featureTypeStyles().toArray(
//    		new FeatureTypeStyle[0])[0];
//    Mark mark = SLD.pointMark(style);
//    Expression expr = mark.getSize();
//    mark = builder.createMark(mark.getWellKnownName().toString());
//    mark.setSize(expr);
//    mark.setStroke(builder.createStroke(highlightColor, 3));
//    PointSymbolizer ps = builder.createPointSymbolizer(builder
//            .createGraphic(null, mark, null));
//    Rule highlightRule = builder.createRule(ps);
//    highlightRule.setName(HIGHLIGHT_RULE_NAME);
//    highlightRule.setFilter(createFilter());
//    fts.rules().add(highlightRule);
  }

  private void highlightPolygon(Style style) {
    FeatureTypeStyle fts = style.featureTypeStyles().toArray(
    		new FeatureTypeStyle[0])[0];
    Fill fill = builder.createFill(highlightColor, .01);
    Stroke stroke = builder.createStroke(highlightColor, 2, .9);

    PolygonSymbolizer symbol = builder.createPolygonSymbolizer(stroke, fill);
    Rule highlightRule = builder.createRule(symbol);
    highlightRule.setName(HIGHLIGHT_RULE_NAME);
    highlightRule.setFilter(createFilter());
    fts.rules().add(highlightRule);
  }

  public void layerAdded(MapLayerListEvent event) {
    Layer layer = event.getLayer();
    if (layer instanceof FeatureLayer) {
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

	public void layerPreDispose(MapLayerListEvent arg0) {
	}
}
