package repast.simphony.visualization.gui;

import java.awt.Color;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Fill;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.style.Graphic;

import repast.simphony.gis.styleEditor.PreviewLabel;

/**
 * @author Nick Collier
 */
public class StylePreviewer {

  private PreviewLabel label;
  private Style style;
  static FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2();
  static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
  
  public StylePreviewer(PreviewLabel label) {
    this.label = label;
  }

  public Style getStyle() {
    return style;
  }

  public void update(Style style) {
    this.style = style;
    DuplicatingStyleVisitor dsv = new DuplicatingStyleVisitor(styleFactory, filterFactory);
		dsv.visit(style.featureTypeStyles().get(0).rules().get(0));
		Rule rule = (Rule) dsv.getCopy();
    Symbolizer symbolizer = rule.getSymbolizers()[0];
		if (PointSymbolizer.class.isAssignableFrom(symbolizer.getClass())) {
			initPoint((PointSymbolizer)symbolizer);
		}
		if (PolygonSymbolizer.class.isAssignableFrom(symbolizer.getClass())) {
			initPolygon((PolygonSymbolizer)symbolizer);
		}
		if (LineSymbolizer.class.isAssignableFrom(symbolizer.getClass())) {
			initLine((LineSymbolizer)symbolizer);
		}
    label.updatePreview();
  }

  private void initPoint(PointSymbolizer ps) {
    Mark mark = SLD.mark(ps);
		String wkn = SLD.wellKnownName(mark);
		label.setMark(wkn);
		Graphic gr = SLD.graphic(ps);
		Expression ex = gr.getSize();
		
		if (ex instanceof Literal ){	
			Double d = ex.evaluate(null,Double.class);
			
			if (d != null)
			  label.setMarkSize(d);
		}	
		
		Fill fill = mark.getFill();
    if (fill != null){
    	Color fillColor = SLD.color(fill.getColor());
    	label.setFillColor(fillColor);
    	double fillOpacity = SLD.pointOpacity(ps);
    	label.setFillOpacity(fillOpacity);
    }
    else
    	label.setFillColor(null);
    
    setStrokeProperties(SLD.stroke(ps));
  }

  private void initPolygon(PolygonSymbolizer ps) {
    label.setShapeToPolygon();
    Fill fill = SLD.fill(ps);
    if (fill != null){
    	Color fillColor = SLD.color(fill.getColor());
    	label.setFillColor(fillColor);
    	double fillOpacity = SLD.polyFillOpacity(ps);
    	label.setFillOpacity(fillOpacity);
    }
    else
    	label.setFillColor(null);
    
		setStrokeProperties(SLD.stroke(ps));
	}

  private void initLine(LineSymbolizer ls) {
    label.setShapeToLine();
    setStrokeProperties(SLD.stroke(ls));
  }
  
  private void setStrokeProperties(Stroke stroke){
		if (stroke != null){
			label.setOutlineColor(SLD.color(stroke));
			int outlineThickness = (int) SLD.width(stroke);
			label.setOutlineThickness(outlineThickness);
			double outlineOpacity = SLD.opacity(stroke);
			label.setOutlineOpacity(outlineOpacity);
		}
		else
			label.setOutlineColor(null);
  }
}