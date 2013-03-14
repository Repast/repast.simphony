package repast.simphony.gis.display;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
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
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.Geometry;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * LegendIconMaker is used to create the legend icons in the style editor
 * panels and map legend panels.  This class has been highly modified from
 * a deprecated Geotools class.
 * 
 * TODO update deprecated methods.
 *
 */
public class LegendIconMaker {

	public static GeometryFactory gFac = new GeometryFactory();
	public static StyleFactory sFac = CommonFactoryFinder.getStyleFactory();
	static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();
	private static StyleBuilder styleBuilder = new StyleBuilder();
//	private static LiteRenderer2 renderer = new LiteRenderer2();
	
	/**
	 * offset for icon, otherwise icons will be connected to others in the
	 * legend
	 */
	public static int offset = 1;

	/**
	 * An icon cache that contains no more than a specified number of lastly
	 * accessed entries
	 */
	private static final int ICON_CACHE_SIZE = 30;

	private static Map iconCache = new LinkedHashMap(16, 0.75f, true) {
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > ICON_CACHE_SIZE;
		}
	};

	private static SimpleFeatureType fFac;

	// Static initialization block
	static {
		AttributeTypeBuilder builder = new AttributeTypeBuilder();
		
		builder.setName("testGeometry");
   	builder.setBinding(Geometry.class);
   	builder.setNillable(true);
		AttributeType testType = builder.buildType();
		
		SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();
		String featureTypeName = "legend";
		ftBuilder.setName(featureTypeName);
		ftBuilder.addBinding(testType);
		
		fFac = ftBuilder.buildFeatureType();
	}


	public static Icon makeLegendIcon(int iconWidth, Color background,
			Rule rule, SimpleFeature sample) {
		return makeLegendIcon(iconWidth, background, rule.getSymbolizers(),
				sample);
	}

	public static Icon makeLegendIcon(int iconWidth, Rule rule, SimpleFeature sample) {
		return makeLegendIcon(iconWidth, new Color(0, 0, 0, 0), rule, sample);
	}

	public static Icon makeLegendIcon(int iconWidth, Color background,
			Symbolizer[] syms, SimpleFeature sample) {
		return makeLegendIcon(iconWidth, iconWidth, background, syms, sample,
				false);
	}

	public static Icon makeLegendIcon(int iconWidth, int iconHeight,
			Color background, Symbolizer[] syms, SimpleFeature sample,
			boolean cacheIcon) {
		IconDescriptor descriptor = new IconDescriptor(iconWidth, iconHeight,
				background, syms, sample);
		Icon icon = (Icon) iconCache.get(descriptor);

		if (icon == null) {
			icon = reallyMakeLegendIcon(iconWidth, iconHeight, background,
					syms, sample);

			if (cacheIcon)
				iconCache.put(descriptor, icon);
		}

		return icon;
	}

	private static PointSymbolizer clone(PointSymbolizer ps, int size) {
		Graphic oldGraphic = ps.getGraphic();
		Graphic graphic = styleBuilder.createGraphic(oldGraphic
				.getExternalGraphics(), oldGraphic.getMarks(), oldGraphic
				.getSymbols(), oldGraphic.getOpacity(), oldGraphic.getSize(),
				oldGraphic.getRotation());

		org.opengis.filter.expression.Expression sizeExp = graphic.getSize();
		if (sizeExp instanceof Literal
				&& ((Literal) sizeExp).getValue() instanceof Number) {
			int val = ((Number) ((Literal) sizeExp).getValue())
					.intValue();
			if (val > size) {
				graphic.setSize(styleBuilder.literalExpression(size - 4));
				graphic.setMarks(oldGraphic.getMarks());
			}
		}

		return styleBuilder.createPointSymbolizer(graphic);
	}

	private static abstract class ChangeTracker {
		private String geomValue;
		public ChangeTracker(String geomValue) {
			this.geomValue = geomValue;
		}
		abstract void restoreValue();
		public String getGeomValue() {return geomValue; }
	}
	private static class PolygonChangeTracker extends ChangeTracker {
		PolygonSymbolizer ps;
		public PolygonChangeTracker(String geomValue, PolygonSymbolizer ps) {
			super(geomValue);
			this.ps = ps;
		}
		public void restoreValue() {
			ps.setGeometryPropertyName(getGeomValue());
		}
	}
	private static class PointChangeTracker extends ChangeTracker {
		PointSymbolizer ps;
		public PointChangeTracker(String geomValue, PointSymbolizer ps) {
			super(geomValue);
			this.ps = ps;
		}
		public void restoreValue() {
			ps.setGeometryPropertyName(getGeomValue());
		}
	}
	private static class LineChangeTracker extends ChangeTracker {
		LineSymbolizer ls;
		public LineChangeTracker(String geomValue, LineSymbolizer ls) {
			super(geomValue);
			this.ls = ls;
		}
		public void restoreValue() {
			ls.setGeometryPropertyName(getGeomValue());
		}
	}
	private static Icon reallyMakeLegendIcon(int iconWidth, int iconHeight,
			Color background, Symbolizer[] symbolizers, SimpleFeature sample) {
		SimpleFeatureCollection fc = FeatureCollections.newCollection();

		Symbolizer[] syms = symbolizers;
		List<ChangeTracker> changes = new ArrayList<ChangeTracker>();
		for (int i = 0; i < symbolizers.length; i++) {
			// syms[i] = styleCloner.clone(syms[i]);
			if (syms[i] instanceof PolygonSymbolizer) {
				PolygonSymbolizer ps = (PolygonSymbolizer) syms[i];
				String gName = ps.getGeometryPropertyName();
				if (gName != null) {
					changes.add(new PolygonChangeTracker(gName,ps));
					ps.setGeometryPropertyName(null);
				}
			}
			if (syms[i] instanceof PointSymbolizer) {
				PointSymbolizer ps = (PointSymbolizer) syms[i];
				String gName = ps.getGeometryPropertyName();
				if (gName != null) {
					changes.add(new PointChangeTracker(gName,ps));
					ps.setGeometryPropertyName(null);
				}
				syms[i] = clone(ps, Math.min(iconWidth, iconHeight));
			}
			if (syms[i] instanceof LineSymbolizer) {
				LineSymbolizer ls = (LineSymbolizer) syms[i];
				String gName = ls.getGeometryPropertyName();
				if (gName != null) {
					changes.add(new LineChangeTracker(gName,ls));
					ls.setGeometryPropertyName(null);
				}
			}
		}
		for (int i = 0; i < syms.length; i++) {
			SimpleFeature feature = null;

			if (syms[i] instanceof PolygonSymbolizer) {
				Number lineWidth = new Integer(0);
				Stroke stroke = ((PolygonSymbolizer) syms[i]).getStroke();

				if ((stroke != null) && (stroke.getWidth() != null)) {
					lineWidth = (Number) stroke.getWidth().evaluate(
							(Object) sample);
				}

				Coordinate[] c = new Coordinate[5];
				double marginForLineWidth = lineWidth.intValue() / 2.0d;
				c[0] = new Coordinate(offset + marginForLineWidth, offset
						+ marginForLineWidth);
				c[1] = new Coordinate(iconWidth - offset - marginForLineWidth,
						offset + marginForLineWidth);
				c[2] = new Coordinate(iconWidth - offset - marginForLineWidth,
						iconHeight - offset - marginForLineWidth);
				c[3] = new Coordinate(offset + marginForLineWidth, iconHeight
						- offset - marginForLineWidth);
				c[4] = new Coordinate(offset + marginForLineWidth, offset
						+ marginForLineWidth);

				com.vividsolutions.jts.geom.LinearRing r = null;

				try {
					r = gFac.createLinearRing(c);
				} catch (com.vividsolutions.jts.geom.TopologyException e) {
					e.printStackTrace();
					System.err.println("Topology Exception in GMLBox");
				}

				Polygon poly = gFac.createPolygon(r, null);
				Object[] attrib = { poly };
				FeatureId id = new FeatureIdImpl("fid-" + new UID().toString().replace(':', '_'));
				
				feature = new SimpleFeatureImpl(attrib, fFac, id, false);
				
			} else if (syms[i] instanceof LineSymbolizer) {
				
				Coordinate[] c = new Coordinate[2];
				c[0] = new Coordinate(offset, offset);

				// c[1] = new Coordinate(offset + (iconWidth * 0.3), offset +
				// (iconWidth * 0.3));
				// c[2] = new Coordinate(offset + (iconWidth * 0.3), offset +
				// (iconWidth * 0.7));
				// c[3] = new Coordinate(offset + (iconWidth * 0.7), offset +
				// (iconWidth * 0.7));
				c[1] = new Coordinate(offset + iconWidth, offset + iconHeight);

				LineString line = gFac.createLineString(c);
				Object[] attrib = { line };
        FeatureId id = new FeatureIdImpl("fid-" + new UID().toString().replace(':', '_'));
				
				feature = new SimpleFeatureImpl(attrib, fFac, id, false);

			} else if (syms[i] instanceof PointSymbolizer) {

				Point p = gFac.createPoint(new Coordinate(offset
						+ (iconWidth / 2.0d), offset + (iconHeight / 2.0d)));
				Object[] attrib = { p };

        FeatureId id = new FeatureIdImpl("fid-" + new UID().toString().replace(':', '_'));
				
				feature = new SimpleFeatureImpl(attrib, fFac, id, false);
			}

			if (feature != null) {
				fc.add(feature);
			}
		}
		
		Rule rule = sFac.createRule();
		rule.symbolizers().addAll(Arrays.asList(syms));
		
		FeatureTypeStyle fts = sFac.createFeatureTypeStyle(new Rule[]{rule});
		Style style = sFac.createStyle();
    style.featureTypeStyles().add(fts);
	
		ImageIcon icon = null;
		BufferedImage image = new BufferedImage(iconWidth, iconHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setBackground(background);
		graphics.setColor(background);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

		// set the output area and graphics
//		renderer.setConcatTransforms(true);
//		renderer.setOutput(graphics, new java.awt.Rectangle(0, 0, image
//				.getWidth(), image.getHeight()));
//		renderer.render(fc, new Envelope(0, iconWidth, 0, iconWidth), style);
		
		icon = new ImageIcon(image);

		//now restore any changes made to the symbolizers
		for (ChangeTracker ct : changes) {
			ct.restoreValue();
		}
		return icon;
	}

	

	private static class IconDescriptor {
		private int iconHeight;

		private int iconWidth;

		private Color background;

		private Symbolizer[] symbolizers;

		private SimpleFeature sample;

		public IconDescriptor(int iconWidth, int iconHeight, Color background,
				Symbolizer[] symbolizers, SimpleFeature sample) {
			this.iconWidth = iconWidth;
			this.iconHeight = iconHeight;
			this.background = background;
			this.symbolizers = symbolizers;
			this.sample = sample;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			if (!(obj instanceof IconDescriptor)) {
				return false;
			}

			IconDescriptor other = (IconDescriptor) obj;

			if ((other.iconWidth != iconWidth)
					|| (other.iconHeight != iconHeight)) {
				return false;
			}

			if (!((background == null && other.background == null) || other.background
					.equals(background))) {
				return false;
			}

			if (((symbolizers == null) && (other.symbolizers != null))
					|| ((symbolizers != null) && (other.symbolizers == null))
					|| (symbolizers.length != other.symbolizers.length)) {
				return false;
			}

			for (int i = 0; i < symbolizers.length; i++) {
				if ((symbolizers[i] == null && other.symbolizers[i] != null)
						|| (symbolizers[i] != null && other.symbolizers[i] == null)) {
					return false;
				}
				if (symbolizers[i] == null && other.symbolizers[i] == null) {
					continue;
				}
				if (!symbolizers[i].equals(other.symbolizers[i])) {
					return false;
				}
			}

			return (((sample == null) && (other.sample == null)) || ((sample != null)
					&& (other.sample != null) && sample.equals(other.sample)));
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return ((((((((17 + symbolizersHashCode()) * 37) + iconWidth) * 37) + iconHeight) * 37) + (background != null ? background
					.hashCode()
					: 0)) * 37)
					+ (sample == null ? 0 : sample.hashCode());
		}

		private int symbolizersHashCode() {
			int hash = 17;
			for (int i = 0; i < symbolizers.length; i++) {
				if (symbolizers[i] == null) {
					continue;
				}
				hash = (hash + symbolizers[i].hashCode()) * 37;
			}
			return hash;
		}
	}
}
