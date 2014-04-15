package repast.simphony.context.space.gis;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;

public class DefaultGeographyFactory implements GeographyFactory  {

	/* (non-Javadoc)
	 * @see repast.simphony.gis.GeographyFactory#createGeography(java.lang.String, repast.simphony.context.Context, repast.simphony.gis.GeographyParameters)
	 */
	public <T> Geography<T> createGeography(String name, Context<T> context,
			GeographyParameters<T> params) {
		String crsCode = params.getCrs();
		ContextGeography<T> geography = null;
		if (crsCode != null) {
			geography = new ContextGeography<T>(name, crsCode);
		} else {
			geography = new ContextGeography<T>(name);
		}
		geography.setAdder(params.getAdder());
		geography.addFromContext(context);
		context.addProjection(geography);
		return geography;
	}
}
