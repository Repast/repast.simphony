package repast.simphony.gis.styleEditor;

import org.geotools.filter.Expression;
import org.geotools.styling.*;
import org.geotools.styling.Stroke;

import java.awt.*;

/**
 * Builds SymbolizerFactories of different types.
 *
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2007/04/18 19:25:53 $
 */
public class SymbolizerFactoryBuilder {

	private static abstract class AbstractSymFactory implements SymbolizerFactory {

		protected StyleFactory fac = StyleFactoryFinder.createStyleFactory();
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
			Mark mark = graphic.getMarks()[0];
			Stroke stroke = fac.createStroke(mark.getStroke().getColor(), mark.getStroke().getWidth());
			String rgb = Integer.toHexString(color.getRGB());
			// trim of the alpha portion
			Fill fill = fac.createFill(builder.literalExpression("#" + rgb.substring(2, rgb.length())));
			Mark newMark = fac.createMark(mark.getWellKnownName(), stroke, fill, mark.getSize(), mark.getRotation());
			Graphic newGraphic = fac.createGraphic(null, new Mark[]{newMark}, null, graphic.getOpacity(),
							graphic.getSize(), graphic.getRotation());
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
