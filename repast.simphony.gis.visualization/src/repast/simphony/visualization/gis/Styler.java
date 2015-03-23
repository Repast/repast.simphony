package repast.simphony.visualization.gis;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;

/**
 * Produces styles for agent layers in a gis display.
 *
 * @author Nick Collier
 * @author Eric Tatara
 * 
 */
public class Styler {

  private Map<String, Style> styleMap = new HashMap<String, Style>();

  /**
   * Registers a style for the specified layerName.
   *
   * @param layerName the name of the layer
   * @param style          the style
   */
  public void registerStyle(String layerName, Style style) {
    styleMap.put(layerName, style);
  }

  /**
   * Gets a style for the specified layerName. If no style has been registered
   * then a default one will be returned.
   *
   * @param layerName the name of the layer
   * @param geomType  the agents geometry type
   * @return a style for the agent
   */
  public Style getStyle(String layerName, Class<? extends Geometry> geomType) {
    Style style = styleMap.get(layerName);
    return style == null ? getDefaultStyle(geomType) : style;
  }

  /**
   * Gets a style for the specified layerName. If no
   * style has been registered then return null.
   *
   * @param layerName the name of the layer
   * @return a style for the agent or null if no style has been registered.
   */
  public Style getStyle(String layerName) {
    return styleMap.get(layerName);
  }

  private Style getDefaultStyle(Class geomType) {
    StyleBuilder builder = new StyleBuilder();

    if (geomType.isAssignableFrom(Point.class) || geomType.isAssignableFrom(MultiPoint.class)) {
      Mark mark = builder.createMark("square", Color.RED);
      mark.setStroke(builder.createStroke(Color.BLACK, 1.0));
      Graphic graphic = builder.createGraphic(null, mark, null);
      return builder.createStyle(builder.createPointSymbolizer(graphic));
    }

    if (geomType.isAssignableFrom(LineString.class) || geomType.isAssignableFrom(MultiLineString.class)) {
      return builder.createStyle(builder.createLineSymbolizer(Color.BLACK, 1));
    }

    return builder.createStyle(builder.createPolygonSymbolizer(Color.RED, Color.BLACK, 1));
  }
}
