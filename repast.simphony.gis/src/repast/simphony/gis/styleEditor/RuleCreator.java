package repast.simphony.gis.styleEditor;

import static repast.simphony.gis.GeometryUtil.GeometryType.LINE;
import static repast.simphony.gis.GeometryUtil.GeometryType.POINT;

import java.awt.Color;
import java.util.Arrays;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

import repast.simphony.gis.GeometryUtil.GeometryType;

/**
 * Utility class for creating Styles from rules and rule editing.
 * 
 * @author Nick Collier
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
		Rule[] ruleArray = new Rule[rules.size()];
		rules.toArray(ruleArray);
		FeatureTypeStyle fts = fac.createFeatureTypeStyle();
		fts.rules().clear();
		fts.rules().addAll(Arrays.asList(ruleArray));
		Style style = builder.createStyle();		
		style.featureTypeStyles().clear();
		style.featureTypeStyles().addAll(Arrays.asList(new FeatureTypeStyle[]{fts}));

		return style;
	}

	/**
	 * Creates a default else rule with a square mark of the specified
	 * color and a black border
	 *
	 * @param color the color of the mark
	 * @return the created default else rule.
	 */
	public Rule createDefaultRule(Color color, GeometryType type) {
		Rule rule = fac.createRule();
		rule.setIsElseFilter(true);
		rule.setTitle("Default");

		Symbolizer[] syms = new Symbolizer[1];
		if (type == LINE) {
			LineSymbolizer sym = createLineSymbolizer(2, color);
			syms[0] = sym;
		} else if (type == POINT) {
			PointSymbolizer sym = builder.createPointSymbolizer(builder.createGraphic(null,
							builder.createMark("square", color, Color.BLACK, 1), null));
			sym.getGraphic().setSize(builder.literalExpression(6));
			syms[0] = sym;
		} else {
			// assume polygon then
			Symbolizer sym = builder.createPolygonSymbolizer(color, Color.BLACK, 1);
			syms[0] = sym;
		}
		rule.symbolizers().clear();
		rule.symbolizers().addAll(Arrays.asList(syms));
		return rule;
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
		Object att = feature.getAttribute(attributeName);
		Rule rule = fac.createRule();
		rule.setTitle(att.toString());

		Expression attExp = builder.attributeExpression(attributeName);
		Expression lit = filterFactory.literal(att);
		BinaryComparisonOperator filter = filterFactory.equals(lit, attExp);
		rule.setFilter(filter);
		Symbolizer sym = factory.createSymbolizer();
		rule.symbolizers().clear();
		rule.symbolizers().addAll(Arrays.asList(new Symbolizer[]{sym}));
		return rule;
	}

	private LineSymbolizer createLineSymbolizer(int strokeWidth, Color color) {
		String rgb = Integer.toHexString(color.getRGB());
		// trim of the alpha portion
		Expression colorExp = builder.literalExpression("#" + rgb.substring(2, rgb.length()));
		Expression widthExp = builder.literalExpression(strokeWidth);
		Stroke newStroke = fac.createStroke(colorExp, widthExp);
		return fac.createLineSymbolizer(newStroke, null);
	}

	/**
	 * Gets the color of the symbolizer in the specified rule.
	 *
	 * @param rule the rule whose color we want to get
	 * @return the color of the symbolizer in the specified rule.
	 */
	public static Color getColor(Rule rule) {
		Symbolizer sym = rule.getSymbolizers()[0];
		Literal colorExp = null;
		if (sym instanceof PointSymbolizer) {
			colorExp = (Literal) ((PointSymbolizer) sym).getGraphic().getMarks()[0].getFill().getColor();
		} else if (sym instanceof PolygonSymbolizer) {
			colorExp = (Literal) ((PolygonSymbolizer) sym).getFill().getColor();
		} else if (sym instanceof LineSymbolizer) {
			colorExp = (Literal) ((LineSymbolizer) sym).getStroke().getColor();
		}
		if (colorExp != null)
			return Color.decode(colorExp.getValue().toString());

		return null;
	}

	/**
	 * Sets the color of the symbolizer in the specified rule.
	 *
	 * @param rule the rule whose color we want to set
	 */
	public static void setColor(Rule rule, Color color) {
		Symbolizer sym = rule.getSymbolizers()[0];
		LiteralExpressionImpl colorExp = null;
		
		if (sym instanceof PointSymbolizer) {
			colorExp = (LiteralExpressionImpl) ((PointSymbolizer) sym).getGraphic().getMarks()[0].getFill().getColor();
		} else if (sym instanceof PolygonSymbolizer) {
			colorExp = (LiteralExpressionImpl) ((PolygonSymbolizer) sym).getFill().getColor();
		} else if (sym instanceof LineSymbolizer) {
			colorExp = (LiteralExpressionImpl) ((LineSymbolizer) sym).getStroke().getColor();
		}
		if (colorExp != null) {
			String rgb = Integer.toHexString(color.getRGB());
			// trim of the alpha portion
			colorExp.setValue("#" + rgb.substring(2, rgb.length()));
		}
	}
}
