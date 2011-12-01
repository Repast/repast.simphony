package repast.simphony.visualization.gis;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.FeatureSource;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Produces styles for agent layers in a gis display.
 *
 * @author Nick Collier
 */
public class Styler {

  private Map<Object, Style> styleMap = new HashMap<Object, Style>();

  /**
   * Registers a style for the specified agent.
   *
   * @param agentClassName the name of the agent
   * @param style          the style
   */
  public void registerStyle(String agentClassName, Style style) {
    styleMap.put(agentClassName, style);
  }

  /**
   * Registers a style for a feature source
   *
   * @param source the feature source
   * @param style  the style
   */
  public void registerStyle(FeatureSource source, Style style) {
    if (style == null) {
      style = getDefaultStyle(source.getSchema().getDefaultGeometry().getType());
    }
    styleMap.put(source, style);
  }


  /**
   * Gets a style for the specified agent. If no style has been registered
   * then a default one will be returned.
   *
   * @param className the name of the agent
   * @param geomType  the agents geometry type
   * @return a style for the agent
   */
  public Style getStyle(String className, Class<? extends Geometry> geomType) {
    Style style = styleMap.get(className);
    return style == null ? getDefaultStyle(geomType) : style;
  }

  /**
   * Gets a style for the specified agent. If no
   * style has been registered then return null.
   *
   * @param className the name of the agent
   * @return a style for the agent or null if no style has been registered.
   */
  public Style getStyle(String className) {
    return styleMap.get(className);
  }

  /**
   * Gets a style for the specified feature source.
   *
   * @param source the feature source
   * @return a style for the feature source
   */
  public Style getStyle(FeatureSource source) {
    return styleMap.get(source);
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
