package repast.simphony.gis.ui.probe;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import gov.nasa.worldwind.geom.Position;
import repast.simphony.ui.probe.LocationProbe;
import repast.simphony.ui.probe.Utils;
import repast.simphony.visualization.gis3D.CoverageProbeObject;

/**
 * Provides the location of the probe when clicking on GIS coverages.  Unlike
 * the LocationProbe for other projections which looks up the location using the
 * probed object and the projection, the CoverageLocationProbe receives the location
 * from the CoverageProbeObject which in turns receives the probe location from
 * the GIS display location listener.
 * 
 * @author Eric Tatara
 *
 */
public class CoverageLocationProbe implements LocationProbe {

	protected CoverageProbeObject probeObject;
	
	public CoverageLocationProbe(CoverageProbeObject probeObject) {
		this.probeObject = probeObject;
	}
	
	/**
   * Gets the property descriptor for the location property of this probe.
   * 
   * @return
   * @throws IntrospectionException
   */
  public PropertyDescriptor getLocationDescriptor() throws IntrospectionException {
    PropertyDescriptor pd = new PropertyDescriptor("location", this.getClass(), "getLocation", null);
    pd.setDisplayName(probeObject.getLayerName() + " Location");
    return pd;
  }

	public String getLocation() {
		
		// The geolocated position comes from the CoverageProbeObject itself.
		Position position = probeObject.getPosition();
		
		if (position == null) {
			return "N/A";
		}
		else {
			// Provide the lat and lon values
      String val = "";
      boolean needBreak = false;
      double lat = position.getLatitude().getDegrees();
      if ((!Double.isNaN(lat)) && (!Double.isInfinite(lat))) {
        val += Utils.getNumberFormatInstance().format(lat);
        needBreak = true;
      }
      double lon = position.getLongitude().getDegrees();
      if ((!Double.isNaN(lon)) && (!Double.isInfinite(lon))) {
        if (needBreak) {
          val += ", ";
          needBreak = false;
        }
        val += Utils.getNumberFormatInstance().format(lon);
        needBreak = true;
      }
      
      return val;
    }  
  }
}