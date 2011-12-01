package repast.simphony.gis;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class ShapefileDescriptor {

	private transient ShapefileDataStore ds;

	private URL shapefileUrl;

	private boolean memoryMapped = false;

	private URI namespace = null;

	public ShapefileDescriptor(URL url) {
		super();
		shapefileUrl = url;
	}

	public ShapefileDescriptor(URL url, boolean mapped, URI namespace) {
		super();
		shapefileUrl = url;
		memoryMapped = mapped;
		this.namespace = namespace;
	}

	public ShapefileDescriptor() {

	}

	public ShapefileDescriptor(URL shapefile, boolean memoryMapped) {
		this.shapefileUrl = shapefile;
		this.memoryMapped = memoryMapped;
	}

	public boolean isMemoryMapped() {
		return memoryMapped;
	}

	public void setMemoryMapped(boolean memoryMapped) {
		this.memoryMapped = memoryMapped;
	}

	public URL getShapefileUrl() {
		return shapefileUrl;
	}

	public void setShapefileUrl(URL shapefileUrl) {
		this.shapefileUrl = shapefileUrl;
	}

	private ShapefileDataStore getDS() {
		if (ds == null) {
			try {
				ds = new ShapefileDataStore(shapefileUrl);
			} catch (MalformedURLException mue) {
				mue.printStackTrace();
			}
		}

		return ds;
	}

	public FeatureCollection getFeatures() throws IOException {
		return getDS().getFeatureSource().getFeatures();
	}

	public FeatureType getFeatureType() throws IOException {
		return getDS().getFeatureSource().getSchema();
	}

}
