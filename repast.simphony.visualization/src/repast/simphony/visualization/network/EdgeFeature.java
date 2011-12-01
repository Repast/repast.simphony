package repast.simphony.visualization.network;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;

import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeBuilder;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.type.GeometricAttributeType;
import org.geotools.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

public class EdgeFeature implements Feature {

	private FeatureCollection parent;
	private Geometry geom;
	private FeatureType type;
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
		FeatureTypeBuilder builder = FeatureTypeFactory.newInstance(agentClass.getName()
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

	public FeatureType getFeatureType() {
		
		System.out.println(" ~~ someone tried to get FeatureType: " + type);
		
		return type;
	}

	public String getID() {
		return id;
	}
	
	public Envelope getBounds() {
		 return geom.getEnvelopeInternal();
	}
	
	public void setParent(FeatureCollection parent) {
		this.parent = parent;
	}
	
	public FeatureCollection getParent() {
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