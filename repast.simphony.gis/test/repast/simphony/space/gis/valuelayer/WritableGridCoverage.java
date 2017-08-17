package repast.simphony.space.gis.valuelayer;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.media.jai.TiledImage;
import javax.media.jai.iterator.RandomIterFactory;
import javax.media.jai.iterator.WritableRandomIter;
import javax.media.jai.operator.FormatDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.image.ImageWorker;
import org.opengis.coverage.PointOutsideCoverageException;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author Michael Bedward and Murray Ellis
 */
public class WritableGridCoverage {

	private GridCoverage2D gridCov;
	private MathTransform2D worldToGrid = null;
	private double[] dbuf = new double[1];
	private static final int MAX_PENDING_VALUES = 10000;

	private static class PendingValue {
		Point2D.Double pos;
		boolean isGeographic;
		double value;
	}

	private List<PendingValue> pendingValues = new ArrayList<PendingValue>();

	/**
	 * Constructor
	 * @param gridCov the GridCoverage2D object to be wrapped
	 */
	public WritableGridCoverage(GridCoverage2D gridCov) {
		this.gridCov = gridCov;
	}

	public GridCoverage2D getGridCoverage() {
		flushCache(true);
		return gridCov;
	}

	public String getName() {
		return gridCov.getName().toString();
	}

	public int getNumRows() {
		return gridCov.getRenderedImage().getHeight();
	}

	public int getNumCols() {
		return gridCov.getRenderedImage().getWidth();
	}

	public Rectangle2D.Double getBounds() {
		Envelope2D env = gridCov.getEnvelope2D();
		return new Rectangle2D.Double(env.x, env.y, env.width, env.height);
	}

	public double getPixelWidth() {
		return gridCov.getEnvelope2D().getWidth() / getNumCols();
	}

	public double getValueAtPos(Point2D pos) {
		flushCache(true);

		try {
			gridCov.evaluate(pos, dbuf);
		} catch (PointOutsideCoverageException ex) {
			return Double.NaN;
		}

		return dbuf[0];
	}

	public int getValueAtPosAsInt(Point2D pos, int defaultValue) {
		double val = getValueAtPos(pos);
		if (Double.isNaN(val)) {
			return defaultValue;
		} else {
			return (int) Math.round(val);
		}
	}

	public double[] getValuesAtPos(Point2D pos, double[] buf) {
		flushCache(true);

		if (buf == null) {
			buf = new double[gridCov.getNumSampleDimensions()];
		}

		try {
			buf = gridCov.evaluate(pos, buf);
		} catch (PointOutsideCoverageException ex) {
			Arrays.fill(buf, Double.NaN);
		}

		return buf;
	}

	public void saveAsJPEG(File path) {
		try {
			ImageWorker iw = new ImageWorker(gridCov.getRenderedImage());
			if (!iw.isBytes()) {

				iw.setImage(FormatDescriptor.create(gridCov.getRenderedImage(),
						DataBuffer.TYPE_BYTE, null));
			}
			iw.write(path);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void setValueAtPos(Point2D pos, int value) {
		Number n = Integer.valueOf(value);
		doSetValue(pos, n);
	}

	public void setValueAtPos(Point2D pos, float value) {
		Number n = new Float(value);
		doSetValue(pos, n);
	}

	public void setValueAtPos(Point2D pos, double value) {
		Number n = new Double(value);
		doSetValue(pos, n);
	}

	private void doSetValue(Point2D pos, Number value) {
		PendingValue pv = new PendingValue();
		pv.pos = new Point2D.Double(pos.getX(), pos.getY());
		pv.isGeographic = true;
		pv.value = value.doubleValue();
		pendingValues.add(pv);
		flushCache(false);
	}

	private void flushCache(boolean force) {
		if (pendingValues.size() >= MAX_PENDING_VALUES || (force &&
				pendingValues.size() > 0)) {
			if (worldToGrid == null) {
				try {
					worldToGrid =
							gridCov.getGridGeometry().getGridToCRS2D().inverse();
				} catch (NoninvertibleTransformException ex) {
					throw new RuntimeException("Could not create geographic to grid coords transform");
				}
			}

			TiledImage tImg = new TiledImage(gridCov.getRenderedImage(), true);
			WritableRandomIter writeIter =
					RandomIterFactory.createWritable(tImg, null);

			Point2D.Double gridPos = new Point2D.Double();
			for (PendingValue pv : pendingValues) {
				if (pv.isGeographic) {
					try {
						worldToGrid.transform(pv.pos, gridPos);
					} catch (TransformException ex) {
						throw new RuntimeException("Could not transform location [" + pv.pos + "] to grid coords");
					}
				} else {
					gridPos.setLocation(pv.pos);
				}

				writeIter.setSample((int) gridPos.x, (int) gridPos.y,
						0, pv.value);
			}

			pendingValues.clear();

			String name = getName();
			Envelope env = gridCov.getEnvelope();
		}
	}
} 
