package repast.simphony.visualization.gis.util;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import gov.nasa.worldwind.geom.Sector;
import junit.framework.TestCase;
import repast.simphony.visualization.gis3D.WWUtils;

public class GISUtilTests extends TestCase {

	@Override
	public void setUp() {

	}

	/**
	 * Convert a WGS84 degree ReferencedEnvelope to Sector
	 */
	public void testEnvelopeToSectorWGS84CRS(){

		double minLon = -87.6204;
		double minLat = 41.8736;
		double maxLon = -87.6177;
		double maxLat = 41.8786;

		// Default WGS84 axis order is lon,lat (EAST_NORTH)
		
		ReferencedEnvelope envelope = new ReferencedEnvelope(
				minLon, maxLon, minLat, maxLat, DefaultGeographicCRS.WGS84);

		Sector sector = WWUtils.envelopeToSector(envelope);
		
		assertEquals(minLat, sector.getMinLatitude().getDegrees());
		assertEquals(maxLat, sector.getMaxLatitude().getDegrees());
		assertEquals(minLon, sector.getMinLongitude().getDegrees());
		assertEquals(maxLon, sector.getMaxLongitude().getDegrees());
	}

	/**
	 * Convert a NAD83 degree ReferencedEnvelope to Sector
	 */
	public void testEnvelopeToSectorDifferentCRS(){

		double minLon = -87.6204001;
		double minLat =  41.8736001;
		double maxLon = -87.6177001;
		double maxLat =  41.8786001;

		double tol      = 0.0000001;  // tolerance
			
		// Create a NAD 1983 CRS, unit = degree, axis order = lat,lon (NORTH_EAST)
		CoordinateReferenceSystem crs = null;
		try {
			crs = CRS.decode("EPSG:4269", false);
		} catch (FactoryException e) {
			e.printStackTrace();
		}
		
		// NAD 1983 axis order is lat,lon (NORTH_EAST)
		ReferencedEnvelope envelope = new ReferencedEnvelope(
				minLat, maxLat, minLon, maxLon, crs);

		Sector sector = WWUtils.envelopeToSector(envelope);

		assertEquals(minLat, sector.getMinLatitude().getDegrees(), tol);
		assertEquals(maxLat, sector.getMaxLatitude().getDegrees(), tol);
		assertEquals(minLon, sector.getMinLongitude().getDegrees(), tol);
		assertEquals(maxLon, sector.getMaxLongitude().getDegrees(), tol);
	}
	
	/**
	 * Convert a NAD27 UTM ReferencedEnvelope to Sector
	 */
	public void testEnvelopeToSectorDifferentCRS_UTM(){

		double minLon = -87.6204001; double minUTMeast = -49514.0;
		double minLat =  41.8736001; double minUTMnorth = 4656766.5;
		double maxLon = -87.6177001; double maxUTMeast = -49246.8;
		double maxLat =  41.8786001; double maxUTMnorth = 4657304.7;

		double tol      = 0.001;  // tolerance
			
		// Create a NAD 27 UTM Zone 17N CRS, unit = meter, axis order = lon,lat (EAST_NORTH)
		CoordinateReferenceSystem crs = null;
		try {
			crs = CRS.decode("EPSG:26717", false);
		} catch (FactoryException e) {
			e.printStackTrace();
		}
		
		// NAD 27 UTM Zone 17N  axis order = lon,lat (EAST_NORTH)
		ReferencedEnvelope envelope = new ReferencedEnvelope(
				minUTMeast, maxUTMeast, minUTMnorth, maxUTMnorth, crs);

		Sector sector = WWUtils.envelopeToSector(envelope);

		assertEquals(minLat, sector.getMinLatitude().getDegrees(), tol);
		assertEquals(maxLat, sector.getMaxLatitude().getDegrees(), tol);
		assertEquals(minLon, sector.getMinLongitude().getDegrees(), tol);
		assertEquals(maxLon, sector.getMaxLongitude().getDegrees(), tol);
	}

}
