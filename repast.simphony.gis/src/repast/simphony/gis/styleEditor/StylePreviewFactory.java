package repast.simphony.gis.styleEditor;

import java.awt.Color;

import javax.swing.Icon;

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.opengis.style.Symbolizer;

/**
 * Factory for creating style preview labels that are used in GUI elements to
 * show how style agents will appear.  Currently the the factory supports 
 * creation of two size: regular and small.   Regular sized previews show how
 * the styles will actually appear in the display with respect to size, while
 * the small version is just a scaled down version that can easily fit in GUI
 * elements like buttons or table cells. 
 * 
 * @author Eric Tatara
 *
 */
public class StylePreviewFactory {

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
				
			double size = ps.getGraphic().getSize().evaluate(null,Double.class);
			double rotation = ps.getGraphic().getRotation().evaluate(null,Double.class);
			Color fillColor = Color.decode(mark.getFill().getColor().evaluate(null, String.class));	
			double fillOpacity = mark.getFill().getOpacity().evaluate(null, Double.class);
			Color outlineColor = Color.decode(mark.getStroke().getColor().evaluate(null,String.class));
			double outlineThickness = mark.getStroke().getWidth().evaluate(null,Double.class);
			double outlineOpacity = mark.getStroke().getOpacity().evaluate(null,Double.class);
			
			preview.setMark(mark.getWellKnownName().evaluate(null,String.class));
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
			
			Color c = Color.decode(ls.getStroke().getColor().evaluate(null,String.class));
			double width = ls.getStroke().getWidth().evaluate(null,Double.class);
			double opacity = ls.getStroke().getOpacity().evaluate(null,Double.class);
			
			preview.setOutlineColor(c);
			preview.setOutlineThickness(width);
			preview.setOutlineOpacity(opacity);
			
		}
		else if (sym instanceof PolygonSymbolizer){
			preview.setShapeToPolygon();
			PolygonSymbolizer ps = (PolygonSymbolizer) sym;
			
			Color fillColor = Color.decode(ps.getFill().getColor().evaluate(null,String.class));
			double fillOpacity = ps.getFill().getOpacity().evaluate(null,Double.class);
			Color outlineColor = Color.decode(ps.getStroke().getColor().evaluate(null,String.class));
			double outlineThickness = ps.getStroke().getWidth().evaluate(null,Double.class);
			double outlineOpacity = ps.getStroke().getOpacity().evaluate(null,Double.class);
			
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
	
}
