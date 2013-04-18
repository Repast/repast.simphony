package repast.simphony.gis.styleEditor;

import static repast.simphony.gis.util.GeometryUtil.GeometryType.LINE;
import static repast.simphony.gis.util.GeometryUtil.GeometryType.POINT;

import java.awt.Color;

import javax.swing.Icon;

import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.style.Symbolizer;

import repast.simphony.gis.util.GeometryUtil;

/**
 * Factory for creating style preview labels that are used in GUI elements to
 * show how style agents will appear.  Currently the the factory supports 
 * creation of two size: regular and small.   Regular sized previews show how
 * the styles will actually appear in the display with respect to size, while
 * the small version is just a scaled down version that can easily fit in GUI
 * elements like buttons or table cells. 
 * 
 * @author Eric Tatara
 * @author Nick Collier
 *
 */
public class StylePreviewFactory {

	// Default symbolizer properties are used to create basic default symbolizers
	//   for the style editor panels.
	// Default point symbolizer properties
	public static final String DEFAULT_POINT_MARK = "square";
	public static final double DEFAULT_POINT_SIZE = 10;
	public static final double DEFAULT_POINT_ROTATION = 0;
	public static final Color DEFAULT_POINT_FILL = Color.BLUE;
	public static final double DEFAULT_POINT_OPACITY = 1.0;
	public static final double DEFAULT_POINT_STROKE_WIDTH = 1;
	public static final Color DEFAULT_POINT_STROKE_COLOR = Color.BLACK;
	public static final double DEFAULT_POINT_STROKE_OPACITY = 1.0;
	
	// Default line symbolizer properties
	public static final double DEFAULT_LINE_STROKE_WIDTH = 1;
	public static final Color DEFAULT_LINE_STROKE_COLOR = Color.RED;
	public static final double DEFAULT_LINE_STROKE_OPACITY = 1.0;
	
  // Default polygon symbolizer properties
	public static final Color DEFAULT_POLY_FILL = Color.GREEN;
	public static final double DEFAULT_POLY_OPACITY = 1.0;
	public static final double DEFAULT_POLY_STROKE_WIDTH = 1;
	public static final Color DEFAULT_POLY_STROKE_COLOR = Color.BLACK;
	public static final double DEFAULT_POLY_STROKE_OPACITY = 1.0;

	/**
	 * Creates an instance of a PreviewLabel using the Rule properties.
	 * 
	 * @param rule the Rule used to create the PreviewLabel
	 * @return the new PrviewLabel
	 */
	public static PreviewLabel createPreviewLabel(Rule rule){
		PreviewLabel preview = new PreviewLabel();
		Symbolizer sym = rule.symbolizers().get(0);
		
		if (sym instanceof PointSymbolizer){
			PointSymbolizer ps = (PointSymbolizer) sym;	
			Mark mark = (Mark)ps.getGraphic().graphicalSymbols().get(0); 
			
			double size = evaluateExpression(ps.getGraphic().getSize(), DEFAULT_POINT_SIZE, Double.class);	
			String markName = evaluateExpression(mark.getWellKnownName(), DEFAULT_POINT_MARK, String.class);
			double rotation = evaluateExpression(ps.getGraphic().getRotation(), DEFAULT_POINT_ROTATION, Double.class);
			Color fillColor = evaluateExpression(mark.getFill().getColor(), DEFAULT_POINT_FILL, Color.class);	
			double fillOpacity = evaluateExpression(mark.getFill().getOpacity(), DEFAULT_POINT_OPACITY, Double.class);
			Color outlineColor = evaluateExpression(mark.getStroke().getColor(), DEFAULT_POINT_STROKE_COLOR, Color.class);
			double outlineThickness = evaluateExpression(mark.getStroke().getWidth(), DEFAULT_POINT_STROKE_WIDTH, Double.class);
			double outlineOpacity = evaluateExpression(mark.getStroke().getOpacity(), DEFAULT_POINT_STROKE_OPACITY, Double.class);
			
			preview.setMark(markName);
			preview.setMarkSize(size);
			preview.setMarkRotation(rotation);
			preview.setFillColor(fillColor);
			preview.setFillOpacity(fillOpacity);
			preview.setOutlineColor(outlineColor);
			preview.setOutlineThickness(outlineThickness);
			preview.setOutlineOpacity(outlineOpacity);
			
		}
		else if (sym instanceof LineSymbolizer){
			preview.setShapeToLine();
			LineSymbolizer ls = (LineSymbolizer) sym;
			
			Color c = evaluateExpression(ls.getStroke().getColor(), DEFAULT_LINE_STROKE_COLOR, Color.class);
			double width = evaluateExpression(ls.getStroke().getWidth(), DEFAULT_LINE_STROKE_WIDTH, Double.class);
			double opacity = evaluateExpression(ls.getStroke().getOpacity(), DEFAULT_LINE_STROKE_OPACITY, Double.class);
			
			preview.setOutlineColor(c);
			preview.setOutlineThickness(width);
			preview.setOutlineOpacity(opacity);
			
		}
		else if (sym instanceof PolygonSymbolizer){
			preview.setShapeToPolygon();
			PolygonSymbolizer ps = (PolygonSymbolizer) sym;
			
			Color fillColor = evaluateExpression(ps.getFill().getColor(), DEFAULT_POLY_FILL, Color.class);
			double fillOpacity = evaluateExpression(ps.getFill().getOpacity(), DEFAULT_POLY_OPACITY, Double.class);
			Color outlineColor = evaluateExpression(ps.getStroke().getColor(), DEFAULT_POLY_STROKE_COLOR, Color.class);
			double outlineThickness = evaluateExpression(ps.getStroke().getWidth(), DEFAULT_POLY_STROKE_WIDTH, Double.class);
			double outlineOpacity = evaluateExpression(ps.getStroke().getOpacity(), DEFAULT_POLY_STROKE_OPACITY, Double.class);
			
			preview.setFillColor(fillColor);
			preview.setFillOpacity(fillOpacity);
			preview.setOutlineColor(outlineColor);
			preview.setOutlineThickness(outlineThickness);
			preview.setOutlineOpacity(outlineOpacity);
		}
	
		preview.updatePreview();
		return preview;		
	}
	
	/**
	 * Create an Icon instance from a Style.  Useful for creating JLabel Icons
	 * for GUI components.
	 * 
	 * @param style the Style used to create the Icon
	 * @return the Icon
	 */
	public static Icon createIcon(Style style){
		Rule rule = style.featureTypeStyles().get(0).rules().get(0);
		return createPreviewLabel(rule).getIcon();
	}
	
	/**
	 * Create an Icon instance from a Rule.  Useful for creating JLabel Icons
	 * for GUI components.
	 * 
	 * @param rule the Rule used to create the Icon
	 * @return the Icon
	 */
	public static Icon createIcon(Rule rule){
		return createPreviewLabel(rule).getIcon();
	}
	
	/**
	 * Create an small Icon instance from a Rule.  Useful for creating JLabel Icons
	 * for GUI components that require small icons.
	 * 
	 * @param rule the Rule used to create the Icon
	 * @return the Icon
	 */
	public static Icon createSmallIcon(Rule rule){
		return createPreviewLabel(rule).getSmallIcon();
	}
	
	/**
   * Provide a default style for the style editors.
   * 
   * @param geomType the geometry type for the selected agent class.
   * @return the default style for the geometry type.
   */
  public static Style getDefaultStyle(GeometryUtil.GeometryType geomType) {
    StyleBuilder builder = new StyleBuilder();

    if (geomType == POINT) {
      Mark mark = builder.createMark(DEFAULT_POINT_MARK, 
      		builder.createFill(DEFAULT_POINT_FILL, DEFAULT_POINT_OPACITY),
      		builder.createStroke(DEFAULT_POINT_STROKE_COLOR, DEFAULT_POINT_STROKE_WIDTH, 
      				DEFAULT_POINT_STROKE_OPACITY));
      mark.setStroke(builder.createStroke());
      Graphic gr = builder.createGraphic();   
      gr.graphicalSymbols().clear();
      gr.graphicalSymbols().add(mark);
   
      PointSymbolizer ps = builder.createPointSymbolizer(gr,null);
      ps.getGraphic().setSize(builder.literalExpression(DEFAULT_POINT_SIZE));
      
      return builder.createStyle(ps);
    }

    if (geomType == LINE) {
      return builder.createStyle(builder.createLineSymbolizer(
      		builder.createStroke(DEFAULT_LINE_STROKE_COLOR, DEFAULT_LINE_STROKE_WIDTH, 
  				DEFAULT_LINE_STROKE_OPACITY)));
    }

    return builder.createStyle(builder.createPolygonSymbolizer(
    		builder.createStroke(DEFAULT_POLY_STROKE_COLOR, DEFAULT_POLY_STROKE_WIDTH, 
    				DEFAULT_POLY_STROKE_OPACITY),
    				builder.createFill(DEFAULT_POLY_FILL, DEFAULT_POLY_OPACITY)));
  }
  
	/**
	 * Returns either the evaluated expression if the expression is a Literal,
	 *   or the default value for other types of expression, such as property
	 *   expressions (which are the property name to be evaluated at run time).
	 *   
	 * @param expr the Expression to evaluate
	 * @param defaultValue the default value to use when the expression is not a Literal
	 * @param type the class of the Expression literal
	 * @return the evaluated expression
	 */
	public static <T extends Object> T evaluateExpression(Expression expr, 
			T defaultValue, Class<T> type){
		
		if (expr instanceof Literal) return expr.evaluate(null, type);
		
		else return defaultValue;
	}
}