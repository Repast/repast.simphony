package repast.simphony.visualization.network;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;


import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.Filter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

public class EdgeFeature implements SimpleFeature {

	private SimpleFeatureCollection parent;
	private Geometry geom;
	private SimpleFeatureType type;
	private String id;

	public EdgeFeature(Class<?> agentClass, Geometry geom, CoordinateReferenceSystem coordRefSystem){
		this.geom = geom;

		 // create the id as in geotools DefaultFeature.
    id = "fid-" + (new UID()).toString();
		
		Class geomClass = LineString.class;
		
		List<AttributeType> ats = new ArrayList<AttributeType>();
		GeometricAttributeType geomType = new GeometricAttributeType("the_geom", geomClass,
				true, null, coordRefSystem, Filter.NONE);
		ats.add(geomType);
		SimpleFeatureTypeBuilder builder = FeatureTypeFactory.newInstance(agentClass.getName()
				+ ".FeatureType");
		builder.addTypes(ats.toArray(new AttributeType[ats.size()]));
		builder.setDefaultGeometry(geomType);
		//type = FeatureTypeBuilder.newFeatureType(ats.toArray(new AttributeType[ats.size()]), agentClass.getName()
		//       + ".FeatureType");
		try {
			type = builder.getFeatureType();
		} catch (SchemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Geometry getDefaultGeometry() {
		return geom;
	}

	public SimpleFeatureType getFeatureType() {
		
		System.out.println(" ~~ someone tried to get FeatureType: " + type);
		
		return type;
	}

	public String getID() {
		return id;
	}
	
	public Envelope getBounds() {
		 return geom.getEnvelopeInternal();
	}
	
	public void setParent(SimpleFeatureCollection parent) {
		this.parent = parent;
	}
	
	public SimpleFeatureCollection getParent() {
		return parent;
	}
	
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getAttribute(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] getAttributes(Object[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNumberOfAttributes() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setAttribute(int arg0, Object arg1)
	throws IllegalAttributeException, ArrayIndexOutOfBoundsException {
		// TODO Auto-generated method stub

	}

	public void setAttribute(String arg0, Object arg1)
	throws IllegalAttributeException {
		// TODO Auto-generated method stub

	}

	public void setDefaultGeometry(Geometry arg0)
	throws IllegalAttributeException {
		// TODO Auto-generated method stub

	}
}