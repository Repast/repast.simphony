package repast.simphony.gis.display;

import com.vividsolutions.jts.geom.Coordinate;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import org.apache.commons.lang.SystemUtils;
import org.geotools.feature.Feature;
import org.geotools.geometry.jts.JTS;
import org.geotools.map.MapContext;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.style.Java2DMark;
import org.geotools.styling.*;
import org.geotools.styling.Stroke;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.spatialschema.geometry.MismatchedDimensionException;
import simphony.util.messages.MessageCenter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointFeature extends PScaleFreeNode {
  MessageCenter msg = MessageCenter.getMessageCenter(getClass());

  private static final long serialVersionUID = -8649781994092211080L;

  Feature feature;

  List<PPath> children = new ArrayList<PPath>();

  Map<Object, PNode> graphicsMap = new HashMap<Object, PNode>();

  FeatureTypeStyle style;

  CoordinateReferenceSystem destinationCrs = DefaultGeographicCRS.WGS84;

  CoordinateReferenceSystem current;

  Coordinate coord;

  double x, y;

  static MathTransform transform;

  private CoordinateReferenceSystem src;

  private MapContext context;

  private boolean hasLiveAttribute = false;

  private boolean drawStroke = !SystemUtils.IS_OS_MAC_OSX;

  public PointFeature(MapContext context, Feature feature,
                      FeatureTypeStyle style) {
    this.feature = feature;
    this.style = style;
    this.context = context;
    this.addAttribute(Feature.class, feature);
    src = feature.getFeatureType().getDefaultGeometry()
            .getCoordinateSystem();
    if (src != current) {
      try {
        transform = CRS.findMathTransform(src, context
                .getCoordinateReferenceSystem(), true);
      } catch (FactoryException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      current = context.getCoordinateReferenceSystem();
    }
    hasLiveAttribute = feature.getAttribute("validForDate") instanceof Boolean;
    update();
  }

  public void setScaleDenominator(double scaleDenominator) {
    if (activeRules.size() == 0) {
      update();
      return;
    }
    for (Rule rule : activeRules) {
      if (rule.getMaxScaleDenominator() > scaleDenominator
              && rule.getMinScaleDenominator() < scaleDenominator) {
        setVisible(true);
      } else {
        setVisible(false);
        return;
      }
    }
  }

  List<Rule> activeRules = new ArrayList<Rule>();

  Map<Mark, PNode> nodeMap = new HashMap<Mark, PNode>();

  public boolean getVisible() {
    boolean visible = super.getVisible();
    if (!hasLiveAttribute)
      return visible;
    else
      return visible
              && ((Boolean) feature.getAttribute("validForDate"))
              .booleanValue();
  }

  public void update() {
    Rule[] rules = style.getRules();
    List<Symbolizer> currentSymbolizers = new ArrayList<Symbolizer>();
    activeRules.clear();
    for (Rule rule : rules) {
      // if (rule.getMaxScaleDenominator() > scaleDenominator
      // && rule.getMinScaleDenominator() < scaleDenominator) {
      activeRules.add(rule);
      setVisible(true);
      // } else {
      // setVisible(false);
      // return;
      // }
      Filter filter = rule.getFilter();

      if (filter == null || filter.evaluate(feature)) {
        for (Symbolizer symbolizer : rule.getSymbolizers()) {
          currentSymbolizers.add(symbolizer);
        }
      }
    }
    for (Symbolizer symbolizer : currentSymbolizers) {
      if (symbolizer instanceof PointSymbolizer) {

        PointSymbolizer ps = (PointSymbolizer) symbolizer;
        Graphic graphics = ps.getGraphic();
        Displacement displacement = graphics.getDisplacement();
        double offsetX = 0.0;
        double offsetY = 0.0;
        if (displacement != null) {
          offsetX = ((Number) graphics.getDisplacement()
                  .getDisplacementX().evaluate(feature))
                  .doubleValue();
          offsetY = ((Number) graphics.getDisplacement()
                  .getDisplacementY().evaluate(feature))
                  .doubleValue();
        }
        Mark[] marks = graphics.getMarks();
        this.removeAllChildren();
        children.clear();
        for (Mark mark : marks) {
          String wkm = (String) mark.getWellKnownName().evaluate(
                  feature);
          PPath child = (PPath) nodeMap.get(mark);
          children.add(child);
          // TODO: don't recreate shape everytime
          if (wkm.equals("arrow")) {
            child = new PArrow();
          } else {
            child = new PPath(Java2DMark
                    .getWellKnownMark((String) mark
                    .getWellKnownName().evaluate(feature)));
          }
          addChild(child);
          nodeMap.put(mark, child);
          Color fill;
          if (mark.getFill() == null) {

          } else {
            fill = Color.decode((String) mark.getFill().getColor()
                    .evaluate(feature));
            float opacity = 1;
            if (mark.getFill().getOpacity() != null) {
              opacity = ((Number) mark.getFill().getOpacity()
                      .evaluate(feature)).floatValue();
            }
            fill = new Color((float) fill.getRed() / 255,
                    (float) fill.getGreen() / 255, (float) fill
                    .getBlue() / 255, opacity);
            child.setPaint(fill);

          }

          Object size = mark.getSize().evaluate(feature);
          if (size instanceof Number) {
            double scale = ((Number) size).doubleValue();
            if (Double.isNaN(scale))
              scale = 10;
            // TODO: this is a one off solution for bwic
            if (wkm.equals("arrow")) {
              scale = scale * 2;
              if (scale < 1) {
                scale = 1;
              }
              ((PArrow) child).setLength(scale);

            } else {
              child.setScale(scale);
            }

          } else {
            child.setScale(10);
          }

          Object rotation = mark.getRotation().evaluate(feature);
          if (rotation instanceof Number) {
            double rot = ((Number) rotation).doubleValue();
            if (Double.isNaN(rot)) {
              child.setRotation(0);
            } else {
              child.setRotation(Math.toRadians(90 - rot));
            }
          }
          child.setOffset(offsetX, offsetY);
          child.setStroke(null);
          // handleStroke
          // todo : handle osX stroke with PFixedStrokePath
            Stroke stroke = drawStroke ? mark.getStroke() : null;
            if (stroke != null) {
              float[] dashPattern = mark.getStroke().getDashArray();
              float width = ((Number) stroke.getWidth().evaluate(
                      feature)).floatValue();
              float opacity = 1;
              if (stroke.getOpacity() != null) {
                opacity = ((Number) stroke.getOpacity().evaluate(
                        feature)).floatValue();
              }

              // int cap = ((Number) stroke.getLineCap()).intValue();
              // int join = ((Number)
              // stroke.getLineJoin()).intValue();
              float phase = ((Number) stroke.getDashOffset()
                      .evaluate(feature)).floatValue();


              java.awt.Stroke pfws = new BasicStroke((int) (width / this.getGlobalScale()),
                      BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
              child.setStroke(pfws);
              Color strokeColor = Color.decode((String) mark
                      .getStroke().getColor().evaluate(feature));

              strokeColor = new Color(
                      (float) strokeColor.getRed() / 255,
                      (float) strokeColor.getGreen() / 255,
                      (float) strokeColor.getBlue() / 255, opacity);
              //child.setStrokePaint(strokeColor);
            } else {
              child.setStrokePaint(null);
            }


        }

      } else if (symbolizer instanceof TextSymbolizer) {
        TextSymbolizer ts = (TextSymbolizer) symbolizer;
        Expression ex = ts.getLabel();
        if (ex == null) {
          return;
        }

        Object o = ts.getLabel().evaluate(feature);
        if (o instanceof String) {
          String label = (String) o;
          if (label != null) {
            addAttribute("tooltip", label);
          }
        }

      }
    }
    boolean change = false;
    Coordinate coord = feature.getDefaultGeometry().getCoordinate();
    if (!context.getCoordinateReferenceSystem().equals(current)) {
      try {
        transform = CRS.findMathTransform(src, context
                .getCoordinateReferenceSystem(), false);
      } catch (FactoryException e) {
        throw new IllegalArgumentException(
                "Unable to transform to new coordinate reference system",
                e);
      }
    }
    if (this.coord == null || !this.coord.equals(coord)) {
      change = true;
      if (transform != null) {
        try {
          coord = JTS.transform(coord, null, transform);
          x = coord.x;
          y = coord.y;
        } catch (MismatchedDimensionException e) {
          msg.error(e.getMessage(), e);
        } catch (TransformException e) {
          msg.error(e.getMessage(), e);
        }
      }
    }
    if (change) {
      this.getTransformReference(true).setOffset(x, y);
    }
  }

  /*
    * @Override public void fullPaint(PPaintContext context) { for (PPath child :
    * children) { child.setStroke(prepareStroke(child.getStroke(), context)); }
    * super.fullPaint(context); }
    */
}
