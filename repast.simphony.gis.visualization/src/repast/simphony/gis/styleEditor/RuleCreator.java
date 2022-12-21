package repast.simphony.gis.styleEditor;

import java.awt.Color;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Utility class for creating Styles from rules and rule editing.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 * 
 */
public class RuleCreator {

	private StyleFactory fac = CommonFactoryFinder.getStyleFactory();
	private StyleBuilder builder = new StyleBuilder();
	private FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

	/**
	 * Creates a Style from the specified list of Rules.
	 *
	 * @param rules the rules to create the style from
	 * @return the created style
	 */
	public Style createStyle(String attributeName, java.util.List<Rule> rules) {
		FeatureTypeStyle fts = fac.createFeatureTypeStyle();
		fts.rules().clear();
		fts.rules().addAll(rules);
		Style style = builder.createStyle();		
		style.featureTypeStyles().clear();
		style.featureTypeStyles().add(fts);

		return style;
	}

	/**
	 * Creates a rule that matches on the specified attribute
	 * and and the specified features attribute value. The Symoblizer
	 * will use a clone of the specified mark and the specified color.
	 *
	 * @param feature       the feature whose attribute value will be used in the rule.
	 * @param attributeName the name of the attribute
	 * @param factory       the factory to create the symbolizer with
	 * @return the created rule.
	 */
	public Rule createValueRule(SimpleFeature feature, String attributeName, SymbolizerFactory factory) {
		Object att = null; 
		
		if (feature != null)
		  att = feature.getAttribute(attributeName);
		
		Rule rule = fac.createRule();
		rule.getDescription().setTitle(attributeName);

		Expression attExp = builder.attributeExpression(attributeName);
		Expression lit = filterFactory.literal(att);
		BinaryComparisonOperator filter = filterFactory.equals(lit, attExp);
		rule.setFilter(filter);
		Symbolizer sym = factory.createSymbolizer();
		rule.symbolizers().clear();
		rule.symbolizers().add(sym);
		return rule;
	}

	/**
	 * Gets the color of the symbolizer in the specified rule.
	 *
	 * @param rule the rule whose color we want to get
	 * @return the color of the symbolizer in the specified rule.
	 */
	public static Color getColor(Rule rule) {
		Symbolizer sym = rule.getSymbolizers()[0];
		Expression colorExp = null;
		if (sym instanceof PointSymbolizer) {
			Mark mark = (Mark)((PointSymbolizer) sym).getGraphic().graphicalSymbols().get(0);
			colorExp = mark.getFill().getColor();
		} 
		else if (sym instanceof PolygonSymbolizer) {
			colorExp = ((PolygonSymbolizer) sym).getFill().getColor();
		} 
		else if (sym instanceof LineSymbolizer) {
			colorExp = ((LineSymbolizer) sym).getStroke().getColor();
		}
		if (colorExp != null)
			return Color.decode(colorExp.evaluate(null,String.class));

		return null;
	}

	/**
	 * Sets the color of the symbolizer in the specified rule.
	 *
	 * @param rule the rule whose color we want to set
	 */
	public static void setColor(Rule rule, Color color) {
		Symbolizer sym = rule.getSymbolizers()[0];
		Expression colorExp = null;
		
		if (sym instanceof PointSymbolizer) {
			Mark mark = (Mark)((PointSymbolizer) sym).getGraphic().graphicalSymbols().get(0);
			colorExp = mark.getFill().getColor();
		} 
		else if (sym instanceof PolygonSymbolizer) {
			colorExp = ((PolygonSymbolizer) sym).getFill().getColor();
		} 
		else if (sym instanceof LineSymbolizer) {
			colorExp = ((LineSymbolizer) sym).getStroke().getColor();
		}
		if (colorExp != null) {
			String rgb = Integer.toHexString(color.getRGB());
			// trim of the alpha portion
			((LiteralExpressionImpl)colorExp).setValue("#" + rgb.substring(2, rgb.length()));
		}
	}
}