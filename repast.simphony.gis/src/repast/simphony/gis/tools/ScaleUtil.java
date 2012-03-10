package repast.simphony.gis.tools;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

public class ScaleUtil {
	private static int DPI = 90;

	public static Envelope calcEnvelope(Envelope e,
			CoordinateReferenceSystem crs, double scaleDenom, int width,
			int height) throws FactoryException, TransformException {
		// Find the diagonal size of the canvas
		GeodeticCalculator calculator = new GeodeticCalculator(crs);
		double pixelWidth = width / DPI * 2.54 / 100; // 2.54 = cm/inch,
		// 100= cm/m
		double pixelHeight = height / DPI * 2.54 / 100;

		Unit unit = calculator.getEllipsoid().getAxisUnit();
		if (unit != SI.METER) {
			UnitConverter convert = SI.METER.getConverterTo(unit);
			pixelWidth = convert.convert(pixelWidth);
			pixelHeight = convert.convert(pixelHeight);
		}
		Coordinate center = e.centre();

		double worldWidth = pixelWidth * scaleDenom;
		double worldHeight = pixelHeight * scaleDenom;
		double centerX = center.x;
		double centerY = center.y;
		Envelope newEnvelope = new Envelope();
		calculator.setStartingPosition(new DirectPosition2D(crs, centerX,
				centerY));
		calculator.setDirection(-90, worldWidth / 2);
		Position point = calculator.getDestinationPosition();
		newEnvelope.expandToInclude(point.getPosition().getOrdinate(0), point
				.getPosition().getOrdinate(1));
		calculator.setStartingPosition(new DirectPosition2D(crs, centerX,
				centerY));
		calculator.setDirection(0, worldHeight / 2);
		point = calculator.getDestinationPosition();
		newEnvelope.expandToInclude(point.getPosition().getOrdinate(0), point
				.getPosition().getOrdinate(1));
		calculator.setDestinationPosition(new DirectPosition2D(crs, centerX,
				centerY));
		calculator.setDirection(90, worldWidth / 2);
		point = calculator.getDestinationPosition();
		newEnvelope.expandToInclude(point.getPosition().getOrdinate(0), point
				.getPosition().getOrdinate(1));
		calculator.setDestinationPosition(new DirectPosition2D(crs, centerX,
				centerY));
		calculator.setDirection(180, worldHeight / 2);
		point = calculator.getDestinationPosition();
		newEnvelope.expandToInclude(point.getPosition().getOrdinate(0), point
				.getPosition().getOrdinate(1));
		return newEnvelope;
	}
}
