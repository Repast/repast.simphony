package repast.simphony.gis.styleEditor;

import org.geotools.filter.Expression;
import org.geotools.map.MapLayer;
import org.geotools.styling.*;
import org.geotools.styling.Stroke;
import simphony.util.ThreadUtilities;

import java.awt.*;

public class StyleSaver extends UISaver {
	StyleBuilder builder;

	RuleEditPanel panel;

	public StyleSaver() {
		init();
	}

	MapLayer layer;

	public StyleSaver(MapLayer layer) {
		this.layer = layer;
		panel = new RuleEditPanel(layer);
		add(panel, BorderLayout.CENTER);
		init();
	}

	private void init() {
		builder = new StyleBuilder();
	}

	@Override
	public String getDialogTitle() {
		return "Edit Style";
	}

	@Override
	public boolean save() {
		final Style style = layer.getStyle();
		Rule rule = style.getFeatureTypeStyles()[0].getRules()[0];
		Rule highlightRule = null;

		if (style.getFeatureTypeStyles()[0].getRules().length > 1) {
			for (Rule tmpRule : style.getFeatureTypeStyles()[0].getRules()) {
				if (tmpRule.getName().equals("highlight")) {
					highlightRule = tmpRule;
				}
			}
		}
		Symbolizer symbolizer = rule.getSymbolizers()[0];
		if (symbolizer instanceof PolygonSymbolizer) {
			PolygonSymbolizer polygonSymbol = (PolygonSymbolizer) symbolizer;
			Fill fill = builder.createFill(panel.getFillColor(), panel
					.getFillTransparency());
			polygonSymbol.setFill(fill);
			Stroke stroke = builder.createStroke(panel.getStrokeColor(), panel
					.getStrokeWidth(), panel.getStrokeTransparency());
			polygonSymbol.setStroke(stroke);
			rule.setSymbolizers(new Symbolizer[] { polygonSymbol });
			if (highlightRule != null) {
				style.getFeatureTypeStyles()[0].setRules(new Rule[] { rule,
						highlightRule });
			} else {
				style.getFeatureTypeStyles()[0].setRules(new Rule[] { rule });
			}
		} else if (symbolizer instanceof PointSymbolizer) {
			PointSymbolizer pointSymbol = (PointSymbolizer) symbolizer;
			Mark mark = builder.createMark(panel.getMark());
			Fill fill = builder.createFill(panel.getFillColor(), panel
					.getFillTransparency());
			mark.setFill(fill);
			Stroke stroke = builder.createStroke(panel.getStrokeColor(), panel
					.getStrokeWidth(), panel.getStrokeTransparency());
			mark.setStroke(stroke);
			Expression sizeExpression = builder.getFilterFactory()
					.createLiteralExpression(panel.getMarkSize());
			mark.setSize(sizeExpression);
			Graphic graphic = builder.createGraphic(null, mark, null);
			pointSymbol.setGraphic(graphic);
			rule.setSymbolizers(new Symbolizer[] { pointSymbol });
			if (highlightRule != null) {
				style.getFeatureTypeStyles()[0].setRules(new Rule[] { rule,
						highlightRule });
			} else {
				style.getFeatureTypeStyles()[0].setRules(new Rule[] { rule });
			}
		}
		ThreadUtilities.runInEventThread(new Runnable() {
			public void run() {
				layer.setStyle(style);
			}
		});
		layer.setStyle(style);
		return true;
	}

	@Override
	public boolean cancel() {
		return true;
	}

}
