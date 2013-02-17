package repast.simphony.space.gis;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.gis.GeographyFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class FeatureAgentTest extends TestCase {

	SimpleFeatureType featureType;
	SimpleFeature feature;

	SimpleFeatureType featureTypeRS;
	SimpleFeature featureAgent;

	protected class Boid{
		private String name;
		
		private Boid (String name){
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		featureType = DataUtilities.createType("buildings", "the_geom:Point,name:String,ADDRESS:String");
		feature = SimpleFeatureBuilder.build(featureType, new Object[] {null, "ABC", "Random Road, 12"}, "building.1");


		Context ctxt = new DefaultContext();
		GeographyParameters geoParams = new GeographyParameters();
		Geography geography = GeographyFactoryFinder.createGeographyFactory(null)
         .createGeography("Geography", ctxt, geoParams);
		GeometryFactory geoFac = new GeometryFactory();
		FeatureAgentFactoryFinder finder = FeatureAgentFactoryFinder.getInstance();

		DefaultFeatureAgentFactory agentFac = finder.getFeatureAgentFactory(Boid.class, Point.class, null);
		
		featureTypeRS = agentFac.getFeatureType();
		
		Boid agent = new Boid("Guy");
		
		Coordinate coord = new Coordinate(41.8, 87.7);
		Point geom = geoFac.createPoint(coord);
		geography.move(agent, geom);
		
		featureAgent = agentFac.getFeature(agent, geography);
	}
	
	public void testGetProperty(){
//		assertEquals("ABC", feature.getProperty("name").getValue());
//    
//		assertEquals("Guy", featureAgent.getProperty("name").getValue());
	}



}
