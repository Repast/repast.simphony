package repast.simphony.space.gis;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import repast.simphony.random.RandomHelper;
import simphony.util.messages.MessageCenter;

public class RandomGISAdder<T> implements GISAdder<T> {

	Geometry geometry;

	WKTReader reader;

	Envelope bb;

	public RandomGISAdder(Geometry geom) {
		this.geometry = geom;
		bb = geometry.getEnvelopeInternal();
		reader = new WKTReader();
	}

	public void add(Geography<T> destination, T object) {
		double x = RandomHelper.getUniform().nextDoubleFromTo(bb.getMinX(),
				bb.getMaxX());
		double y = RandomHelper.getUniform().nextDoubleFromTo(bb.getMinY(),
				bb.getMaxY());
		Geometry geom;
		try {
			geom = reader.read("POINT(" + x + " " + y + ")");
			while (!geometry.contains(geom)) {
				x = RandomHelper.getUniform().nextDoubleFromTo(bb.getMinX(),
						bb.getMaxX());
				y = RandomHelper.getUniform().nextDoubleFromTo(bb.getMinY(),
						bb.getMaxY());
				geom = reader.read("POINT(" + x + " " + y + ")");
			}
			destination.move(object, geom);
		} catch (ParseException e) {
			MessageCenter.getMessageCenter(getClass()).warn(
					"Unable to create Geometry from wkt", e);
		}
	}

}
