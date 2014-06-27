package repast.simphony.context.space.gis;

import java.util.Map;

public class GeographyFactoryFinder {

	public static GeographyFactory createGeographyFactory(
			Map<String, Object> hints) {
		return new DefaultGeographyFactory();
	}
}
