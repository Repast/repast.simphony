package repast.simphony.context.space.gis;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;

public interface GeographyFactory {

	public abstract <T> Geography<T> createGeography(String name,
			Context<T> context, GeographyParameters<T> params);

}