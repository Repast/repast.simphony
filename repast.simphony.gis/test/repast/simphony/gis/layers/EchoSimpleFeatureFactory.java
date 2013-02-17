package repast.simphony.gis.layers;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.LenientFeatureFactoryImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;

public class EchoSimpleFeatureFactory extends LenientFeatureFactoryImpl {

	FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
	
	@Override
	 public SimpleFeature createSimpleFeature(Object[] array, SimpleFeatureType type, String id) {
   if( type.isAbstract() ){
       throw new IllegalArgumentException("Cannot create an feature of an abstract FeatureType "+type.getTypeName());
   }
   
   return new EchoSimpleFeatureImpl(array, type, ff.featureId(id), false);
}

}
