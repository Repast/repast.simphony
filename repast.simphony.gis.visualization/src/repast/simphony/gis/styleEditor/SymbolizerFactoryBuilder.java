package repast.simphony.gis.styleEditor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;

/**
 * Builds SymbolizerFactories of different types.
 *
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2007/04/18 19:25:53 $
 */
public class SymbolizerFactoryBuilder {

	private static abstract class AbstractSymFactory implements SymbolizerFactory {

		protected StyleFactory fac =  CommonFactoryFinder.getStyleFactory();
		protected StyleBuilder builder = new StyleBuilder();
		protected Color color;

		protected AbstractSymFactory(Color color) {
			this.color = color;
		}
	}

	private static class LineSymbolizerFactory extends AbstractSymFactory {

		private Stroke stroke;

		public LineSymbolizerFactory(Color color, Stroke stroke) {
			super(color);
			this.stroke = stroke;
		}

		public Symbolizer createSymbolizer() {
			String rgb = Integer.toHexString(color.getRGB());
			// trim of the alpha portion
			Expression colorExp = builder.literalExpression("#" + rgb.substring(2, rgb.length()));
			Stroke newStroke = fac.createStroke(colorExp, stroke.getWidth());
			return fac.createLineSymbolizer(newStroke, null);
		}
	}

	private static class PolygonSymbolizerFactory extends AbstractSymFactory {

		public PolygonSymbolizerFactory(Color color) {
			super(color);
		}


		public Symbolizer createSymbolizer() {
			return builder.createPolygonSymbolizer(color, Color.BLACK, 1);
		}
	}

	private static class PointSymbolizerFactory extends AbstractSymFactory {

		private Graphic graphic;

		public PointSymbolizerFactory(Graphic graphic, Color color) {
			super(color);
			this.graphic = graphic;
		}


		public Symbolizer createSymbolizer() {
			Mark mark = (Mark)graphic.graphicalSymbols().get(0); 
			Stroke stroke = fac.createStroke(mark.getStroke().getColor(), mark.getStroke().getWidth());
			String rgb = Integer.toHexString(color.getRGB());
			// trim of the alpha portion
			Fill fill = fac.createFill(builder.literalExpression("#" + rgb.substring(2, rgb.length())));
			Mark newMark = fac.createMark(mark.getWellKnownName(), stroke, fill, graphic.getSize(), 
					graphic.getRotation());
			
			List<GraphicalSymbol> list = new ArrayList<GraphicalSymbol>();
			list.add(newMark);
			
			Graphic newGraphic = fac.graphic(list, graphic.getOpacity(), graphic.getSize(), 
					graphic.getRotation(), graphic.getAnchorPoint(), graphic.getDisplacement());
			
			return fac.createPointSymbolizer(newGraphic, null);
		}
	}

	public static SymbolizerFactory getSymbolizerFactory(Color color, Symbolizer symbolizer) {
		if (symbolizer instanceof LineSymbolizer) {
			return getLineSymbolizerFactory(color, ((LineSymbolizer)symbolizer).getStroke());
		} else if (symbolizer instanceof PointSymbolizer) {
			return getPointSymbolizerFactory(color, ((PointSymbolizer)symbolizer).getGraphic());
		} else {
			return getPolygonSymbolizerFactory(color);
		}
	}

	public static SymbolizerFactory getLineSymbolizerFactory(Color color, Stroke stroke) {
		return new LineSymbolizerFactory(color, stroke);
	}

	public static SymbolizerFactory getPointSymbolizerFactory(Color color, Graphic graphic) {
		return new PointSymbolizerFactory(graphic, color);
	}

	public static SymbolizerFactory getPolygonSymbolizerFactory(Color color) {
		return new PolygonSymbolizerFactory(color);
	}
}
