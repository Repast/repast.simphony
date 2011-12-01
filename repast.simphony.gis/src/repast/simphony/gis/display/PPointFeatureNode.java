package repast.simphony.gis.display;

import com.vividsolutions.jts.geom.Coordinate;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PBounds;
import org.geotools.factory.Hints;
import org.geotools.feature.Feature;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.style.Java2DMark;
import org.geotools.styling.*;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.*;
import org.opengis.spatialschema.geometry.DirectPosition;
import org.opengis.spatialschema.geometry.MismatchedDimensionException;
import simphony.util.messages.MessageCenter;

import java.awt.*;
import java.util.Map;

public class PPointFeatureNode extends PScaleFreeMark {
	MessageCenter msg = MessageCenter.getMessageCenter(getClass());

	static Map<String, Shape> pathMap;

	static String X_LOCATION = "x";

	static String Y_LOCATION = "y";

	static String SHAPE = "wkn";

	private final static CoordinateOperationFactory operationFactory;

	static {
		Hints hints = new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);
		operationFactory = org.geotools.referencing.FactoryFinder
				.getCoordinateOperationFactory(hints);
	}

	private static final long serialVersionUID = -5747765333807581166L;

	private Feature feature;

	private FeatureTypeStyle style;

	CoordinateReferenceSystem destinationCrs = DefaultGeographicCRS.WGS84;

	CoordinateReferenceSystem current;

	static MathTransform transform;

	double x, y, rotation;

	Coordinate coord;

	PBounds bounds = new PBounds();

	public PPointFeatureNode(Feature feature, FeatureTypeStyle style) {
		this.feature = feature;
		this.style = style;
		this.addClientProperty(Feature.class, feature);
		CoordinateReferenceSystem src = feature.getFeatureType()
				.getDefaultGeometry().getCoordinateSystem();
		if (src != current) {
			current = src;
			try {
				transform = (MathTransform2D) operationFactory.createOperation(
						src, destinationCrs).getMathTransform();
			} catch (OperationNotFoundException e) {
				msg.error(e.getMessage(), e);
			} catch (FactoryException e) {
				msg.error(e.getMessage(), e);
			}
		}
	}

	public void update() {
		int size = 0;
		Color fill = null;
		double rotation = 0.0;
		for (Rule r : style.getRules()) {
			if (r.getFilter() == null || r.getFilter().contains(feature)) {
				for (Symbolizer sym : r.getSymbolizers()) {
					if (sym instanceof PointSymbolizer) {
						PointSymbolizer ps = (PointSymbolizer) sym;
						Graphic g = ps.getGraphic();
						for (Mark mark : g.getMarks()) {
							size = ((Number) mark.getSize().getValue(feature))
									.intValue();
							if (size == 0) {
								size = 10;
							}
							String wkn = (String) getClientProperty(SHAPE);
							String newWkn = (String) mark.getWellKnownName()
									.getValue(feature);
							if (wkn == null || !newWkn.equals(wkn)) {
								setPathTo(Java2DMark.getWellKnownMark(newWkn));
								addClientProperty(SHAPE, newWkn);
							}
							Object rotObj = mark.getRotation()
									.getValue(feature);
							if (rotObj != null) {
								rotation = ((Number) rotObj).doubleValue();
							}
							if (Double.isNaN(rotation)) {
								rotation = 0.0;
							}
							fill = Color.decode((String) mark.getFill()
									.getColor().getValue(feature));

							Double opacity = (Double) mark.getFill()
									.getOpacity().getValue(feature);
							if (opacity != null)
								animateToTransparency(opacity.floatValue(), 0);
							if (getStrokePaint() != null) {
								setStrokePaint(null);
								setStroke(null);
							}
						}
					} else if (sym instanceof TextSymbolizer) {
						TextSymbolizer tsym = (TextSymbolizer) sym;
						String label = (String) tsym.getLabel().getValue(
								feature);
						addClientProperty("tooltip", label);
					}
				}
			}
		}
		boolean change = false;
		Coordinate coord = feature.getDefaultGeometry().getCoordinate();
		if (this.coord == null || !this.coord.equals(coord)) {
			change = true;
			if (transform != null) {
				try {
					DirectPosition pt = new GeneralDirectPosition(coord.x,
							coord.y);
					pt = transform.transform(pt, null);
					x = pt.getCoordinates()[0];
					y = pt.getCoordinates()[1];
				} catch (MismatchedDimensionException e) {
					msg.error(e.getMessage(), e);
				} catch (TransformException e) {
					msg.error(e.getMessage(), e);
				}
			}
		} else if (this.rotation != rotation) {
			this.rotation = rotation;
			change = true;
		}
		// update finally
		if (getPaint() == null) {
			setPaint(fill);
		} else if (fill != null) {
			animateToColor(fill, 0);
		}
		if (change) {
			PAffineTransform transform = getTransform();
			transform.setToIdentity();
			transform.setOffset(x, y);
			transform.setRotation(rotation);
			if (getClientProperty(SHAPE).equals("arrow"))
				transform.scale(5 * size, 5);
			else
				transform.scale(size, size);
			this.animateToTransform(transform, 0);
		}
	}
}
