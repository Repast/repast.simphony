package repast.simphony.visualization.gis3D;

import java.util.Arrays;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.DirectPosition2D;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.cs.AxisDirection;

import gov.nasa.worldwind.geom.Position;
import repast.simphony.ui.probe.Utils;

/**
 * Probe object for coverage layers.  CoverageProbeObject is used as a proxy for
 * the Object returned from a coverage since the probe should reflect a grid value
 * and location, and not the entire coverage itself.
 * 
 * @author Eric Tatara
 * 
 */
public class CoverageProbeObject {
  
	private double[] loc = new double[2];
  private Position position;
  private String layerName;
  private GridCoverage2D coverage;
  
  private int hashCode = 17;

  public CoverageProbeObject(String layerName, Position position, 
  		GridCoverage2D coverage) {
    this.position = position;
    this.layerName = layerName;
    this.coverage = coverage;
    
    loc[0] = position.getLatitude().getDegrees();
    loc[1] = position.getLongitude().getDegrees();
    
    hashCode = 31 * hashCode + coverage.hashCode();
    hashCode = 31 * hashCode + Arrays.hashCode(loc);
  }
  
  public String getLayerName() {
    return layerName;
  }

  public Position getPosition() {
  	return position;
  }

  public double getValue() {
  	
  	double lat = position.getLatitude().getDegrees();
  	double lon = position.getLongitude().getDegrees();
  	
  	DirectPosition directpos = null;
  	
  	// CRS axis order is EAST,NORTH (Lon,Lat)
  	if (coverage.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(0).getDirection() == AxisDirection.EAST) {
  		directpos = new DirectPosition2D(lon,lat);
  	}
  	// CRS axis order is NORTH,EAST (Lat,Lon)
  	else {
  		directpos = new DirectPosition2D(lat,lon);
  	}
  	
		double[] val = null;
  	val = coverage.evaluate(directpos,val);
  	
  	if (val != null)
  		return val[0];  // TODO GIS multi-band coverage
  	
  	return -1;
  }
  
  public int hashCode() {
    return hashCode;
  }
  
  // overrides equals and hashCode so that multiple probes on the
  // same location are considered equal
  public boolean equals(Object obj) {
    if (obj instanceof CoverageProbeObject) {
      CoverageProbeObject other = (CoverageProbeObject)obj;
      return other.coverage.equals(coverage) && Arrays.equals(other.loc, loc);
    }
    return false;
  }
  
  /**
   * Provides a nice string value for the probe panel title
   */
  @Override
  public String toString() {

  	String lat = Utils.getNumberFormatInstance().format(position.getLatitude().getDegrees());
  	String lon = Utils.getNumberFormatInstance().format(position.getLongitude().getDegrees());
  	
  	String title = layerName + " " + lat + " : " + lon;

  	// The ProbePropertyFactory assumes "." is a class path separator and trims 
  	//   eveything before it, so replace the period with another decimal character
  	title = title.replace(".", ",");
  	
  	return title;
  }
}
