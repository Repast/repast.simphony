/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
/*
 * LegendNoteIconMaker.java
 *
 * Created on 05 July 2003, 22:05
 */
package repast.simphony.gis.display;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.filter.Expression;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.LiteralExpression;
import org.geotools.renderer.lite.LiteRenderer2;
import org.geotools.resources.TestData;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleFactoryFinder;
import org.geotools.styling.builder.StyleBuilder;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.geometry.Geometry;
import org.opengis.style.FeatureTypeStyle;
import org.opengis.style.Fill;
import org.opengis.style.Style;
import org.opengis.style.StyleFactory;
import org.opengis.style.Symbolizer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

/**
 * DOCUMENT ME!
 * 
 * @author jianhuij
 */
public class LegendIconMaker {
	/**
	 * The logger for the Legend module.
	 */
	private static final Logger LOGGER = Logger
			.getLogger("org.geotools.renderer");

	/**
	 * for create artificial geometry feature for making legend icon since the
	 * icon somehow has somekind of shape such as line, polygon etc. then other
	 * code will apply fill and stroke to make an image
	 */
	public static GeometryFactory gFac = new GeometryFactory();

	/**
	 * if the rule already has defined legendGraphic the stylefactory could
	 * create symbolizer to contain it
	 */
	public static StyleFactory sFac = StyleFactoryFinder.createStyleFactory();

	/**
	 * offset for icon, otherwise icons will be connected to others in the
	 * legend
	 */
	public static int offset = 1;

	/**
	 * the current renderer object
	 */
	private static LiteRenderer2 renderer = new LiteRenderer2();

	private static StyleBuilder styleBuilder = new StyleBuilder();

	// private static StyleCloner styleCloner = new StyleCloner(styleBuilder
	// .getStyleFactory());

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
		builder.setName("geometry:text");
   	builder.setBinding(Geometry.class);
   	builder.setNillable(true);
		AttributeType type1 = builder.buildType();
		
		builder.setName("label");
   	builder.setBinding(String.class);
   	builder.setNillable(true);
		AttributeType type2 = builder.buildType();
		
		AttributeType[] attribs = {type1, type2 };

		builder.setName("testGeometry");
   	builder.setBinding(Geometry.class);
   	builder.setNillable(true);
		AttributeType testType = builder.buildType();
		try {
			fFac = FeatureTypeFactory
					.newFeatureType(
							new AttributeType[] {testType}, "legend");
		} catch (SchemaException se) {
			throw new RuntimeException(se);
		}
	}

	private static int missCounter = 0;

	private LegendIconMaker() {
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
		if (sizeExp instanceof LiteralExpression
				&& ((LiteralExpression) sizeExp).getLiteral() instanceof Number) {
			int val = ((Number) ((LiteralExpression) sizeExp).getLiteral())
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

				try {
					feature = fFac.create(attrib);
				} catch (IllegalAttributeException ife) {
					throw new RuntimeException(ife);
				}

				LOGGER.fine("feature = " + feature);
			} else if (syms[i] instanceof LineSymbolizer) {
				LOGGER.fine("building line");

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

				try {
					feature = fFac.create(attrib);
				} catch (IllegalAttributeException ife) {
					throw new RuntimeException(ife);
				}

				LOGGER.fine("feature = " + feature);
			} else if (syms[i] instanceof PointSymbolizer) {
				LOGGER.fine("building point");

				Point p = gFac.createPoint(new Coordinate(offset
						+ (iconWidth / 2.0d), offset + (iconHeight / 2.0d)));
				Object[] attrib = { p };

				try {
					feature = fFac.create(attrib);
				} catch (IllegalAttributeException ife) {
					throw new RuntimeException(ife);
				}

				LOGGER.fine("feature = " + feature);
			}

			if (feature != null) {
				fc.add(feature);
			}
		}

		FeatureTypeStyle fts = styleBuilder.createFeatureTypeStyle("",
				styleBuilder.createRule(syms));
		fts.setFeatureTypeName(fc.features().next().getFeatureType()
				.getTypeName());

		Style s = styleBuilder.createStyle();
		s.addFeatureTypeStyle(fts);
		Symbolizer symbolizer = s.getFeatureTypeStyles()[0].getRules()[0]
				.getSymbolizers()[0];

		ImageIcon icon = null;
		BufferedImage image = new BufferedImage(iconWidth, iconHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setBackground(background);
		graphics.setColor(background);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

		// set the output area and graphics
		renderer.setConcatTransforms(true);
		renderer.setOutput(graphics, new java.awt.Rectangle(0, 0, image
				.getWidth(), image.getHeight()));
		renderer.render(fc, new Envelope(0, iconWidth, 0, iconWidth), s);
		icon = new ImageIcon(image);

		//now restore any changes made to the symbolizers
		for (ChangeTracker ct : changes) {
			ct.restoreValue();
		}
		return icon;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		try {
			HashMap params1 = new HashMap();
			params1.put("url", TestData.getResource(LegendIconMaker.class,
					"lakes.shp"));

			DataStore data = DataStoreFinder.getDataStore(params1);
			int width = UIManager.getIcon("Tree.openIcon").getIconWidth();
			Expression fcolor1 = FilterFactoryFinder.createFilterFactory()
					.createLiteralExpression(Color.PINK.getRGB() + "");
			Expression fcolor2 = FilterFactoryFinder.createFilterFactory()
					.createLiteralExpression(Color.BLACK.getRGB() + "");
			Expression lineWidth = FilterFactoryFinder.createFilterFactory()
					.createLiteralExpression(3.0);
			Fill fill = StyleFactoryFinder.createStyleFactory().createFill(
					fcolor1);
			Stroke stroke = StyleFactoryFinder.createStyleFactory()
					.createStroke(fcolor2, lineWidth);
			PolygonSymbolizer polySymbolizer = StyleFactoryFinder
					.createStyleFactory().createPolygonSymbolizer(stroke, fill,
							"testGeometry");
			JLabel iconJLabel = new JLabel("Legend Icon Example");

			String typeName = data.getTypeNames()[0];
			FeatureSource shape = data.getFeatureSource(typeName);

			iconJLabel.setIcon(LegendIconMaker.makeLegendIcon(width, new Color(
					0, 0, 0, 0), new Symbolizer[] { polySymbolizer },
					(SimpleFeature) shape.getFeatures().collection().toArray()[2]));

			JFrame f = new JFrame();
			f.getContentPane().setBackground(new Color(204, 204, 255));
			f.getContentPane().add(iconJLabel);
			f.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
