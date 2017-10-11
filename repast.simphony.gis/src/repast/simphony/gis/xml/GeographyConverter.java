package repast.simphony.gis.xml;

import java.io.File;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

import repast.simphony.context.Context;
import repast.simphony.context.space.gis.ContextGeography;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.scenario.ScenarioUtils;
import repast.simphony.space.gis.GISAdder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.WritableGridCoverage2D;
import repast.simphony.util.collections.Pair;
import repast.simphony.xml.AbstractConverter;
import repast.simphony.xml.Keys;

/**
 * XStream converter for ContextGeographies, the default gis space type
 * in simphony.
 *
 * @author Nick Collier
 */
public class GeographyConverter extends AbstractConverter {

	public GeographyConverter() {
//		fieldsToOmit.add(new Pair(GridCoverage2D.class, "serializedImage"));
	}
	
  public boolean canConvert(Class aClass) {
    return aClass.equals(ContextGeography.class);
  }

  public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mContext) {
    Geography<?> geog = (Geography<?>) o;
    writeString("name", geog.getName(), writer);
    writeString("adder", geog.getAdder().getClass().getName(), writer);
    writeString("crs", geog.getCRS().toWKT(), writer);
    writeString("geom_count", String.valueOf(geog.size()), writer);
    for (Object obj : geog.getAllObjects()) {
      Geometry geom = geog.getGeometry(obj);
      Pair<Object,String> p = new Pair<Object,String>(obj, geom.toText());
      writeObject("geom_entry", p, writer, mContext);
    }
    
    // Coverage layers are serialized to GeoTiff and the file path is stored in
    //   the XML file.
    writeString("coverage_count", String.valueOf(geog.getCoverageNames().size()), writer);
  
//    String dir = xmlFile.getParent();
    
    for (String coverageName : geog.getCoverageNames()) {
    	
    	GridCoverage2D cov = geog.getCoverage(coverageName);
    	
    	try {
    		String fileName = xmlFile.getParent() + File.separator + coverageName + ".tiff";
//    		fileName = ScenarioUtils.makeRelativePathToProject(fileName);
    		
    		File file = new File(fileName);
				GeoTiffWriter coverageWriter = new GeoTiffWriter(file);
				
				// Write the Coverage to a GeoTiff file
				coverageWriter.write(cov, null);
				
				// Use the filename only - not the full path
				Pair<String,String> pair = new Pair<String,String>(coverageName, file.getName());
				writeObject("coverage", pair, writer, mContext);
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    }
  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext umContext) {
    try {
      Context context = (Context) umContext.get(Keys.CONTEXT);
      String name = readNextString(reader);
      String adder = readNextString(reader);
      Class adderClass = Class.forName(adder);
      GeographyParameters params = new GeographyParameters((GISAdder) adderClass.newInstance());
      String sCrs = readNextString(reader);
      CoordinateReferenceSystem crs = CRS.parseWKT(sCrs);

      Geography geog = GeographyFactoryFinder.createGeographyFactory(null).createGeography(name, context, params);
      geog.setCRS(crs);

      WKTReader wktReader = new WKTReader();
      int geomCount = Integer.valueOf(readNextString(reader));
      for (int i = 0; i < geomCount; i++) {
        Pair pair = (Pair) readNextObject(geog, reader, umContext);
        Geometry geom = wktReader.read(pair.getSecond().toString());
        if (!context.contains(pair.getFirst())) context.add(pair.getFirst());
        geog.move(pair.getFirst(), geom);
      }

      int coverageCount = Integer.valueOf(readNextString(reader));
      for (int i = 0; i < coverageCount; i++) {
      	Pair<String,String> pair =  (Pair)readNextObject(geog, reader, umContext);
      
      	String coverageName = pair.getFirst();
      	String fileName = pair.getSecond();
      	
      	fileName = xmlFile.getParent() + File.separator + fileName;
      	fileName = ScenarioUtils.makeRelativePathToProject(fileName);
      	
    		try {
    			GeoTiffReader gtreader = new GeoTiffReader(fileName);
    			GridCoverage2D coverage = gtreader.read(null);
    			
    			geog.addCoverage(coverageName, new WritableGridCoverage2D(coverage));
    			
    		} catch (DataSourceException e1) {
    			e1.printStackTrace();
    		}
    	
      }
      return geog;
    } catch (Exception ex) {
      throw new ConversionException("Error deserializing Geography", ex);
    }

  }
}