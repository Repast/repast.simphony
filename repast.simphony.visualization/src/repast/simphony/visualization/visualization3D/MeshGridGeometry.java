package repast.simphony.visualization.visualization3D;

import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;
import repast.simphony.visualization.visualization3D.style.ValueLayerStyle3D;

import org.jogamp.java3d.Geometry;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.TriangleStripArray;
import java.awt.*;

/**
 * This uses a terrain drawing algorithm described by Mike Jacobs in JDJ
 * "When Mars is Too Big to Download", <p/>
 * http://java.sys-con.com/read/46231.htm <p/> It departs from that slightly
 * though. The algorithm calculates terrain cooridinates / colors using the four
 * corners of a row column. This means that the final column / row doesn't align
 * with our grids because the values of those row / columns are actually
 * displayed as part of the preceeding row column. Consequently we add a final
 * "fake" column and and "fake" row that just duplicates the of the previous row
 * / column.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class MeshGridGeometry implements DataGeometry {

	private Geometry geometry;
	private int rows, cols;
	private int rowsOffset, colsOffset;
	private float[] updateArray;
	private boolean applyNeeded = false;

	public MeshGridGeometry(int xDim, int zDim, int xOffset, int zOffset) {
		this.cols = xDim;
		this.rows = zDim;
		this.colsOffset = xOffset;
		this.rowsOffset = zOffset;
	}

	public Geometry getGeometry(ValueLayerStyle3D style) {
		if (geometry != null)
			return geometry;
		// The number of coordinates is based on the
		// number of horizontal strips (height - 1) times the
		// number of vertices per strip (width * 2) * 3 coords.
		int numCoords = rows * (cols + 1) * 2 * 3;
		float[] coordinates = new float[numCoords];
		float[] colors = new float[numCoords];

		// * 4 for y coordinate plus r,g,b
		updateArray = new float[(rows) * (cols + 1) * 2 * 4];

		// The strip vertices must be ordered
		// like this: NW, SW, NE, SE for each set of four corners
		// of a quad. A convenient way to accomplish this is to
		// organize the landscape in horizontal strips and iterate
		// across the columns calculating two vertices at a time.

		Color color;
		int ci = 0; // coordinate index
		for (int row = 0, n = rows - 1; row < n; row++) {
			for (int col = 0; col < cols; col++) {

				int nextRow = row + 1;

				// get y coord from the style (height map)
				float swY = style.getY(col - colsOffset, row - rowsOffset);
				float nwY = style.getY(col - colsOffset, nextRow - rowsOffset);

				coordinates[ci] = col;
				coordinates[ci + 1] = nwY;
				// - z coordinates because we want to display back into the
				// screen
				coordinates[ci + 2] = -(row + 1);
				coordinates[ci + 3] = col;
				coordinates[ci + 4] = swY;
				// - z coordinates because we want to display back into the
				// screen
				coordinates[ci + 5] = -row;

				color = (Color) style.getPaint(col - colsOffset, nextRow
						- rowsOffset);
				if (color != null) {
					colors[ci] = color.getRed() / 255f;
					colors[ci + 1] = color.getGreen() / 255f;
					colors[ci + 2] = color.getBlue() / 255f;

					color = (Color) style.getPaint(col - colsOffset, row
							- rowsOffset);
					colors[ci + 3] = color.getRed() / 255f;
					colors[ci + 4] = color.getGreen() / 255f;
					colors[ci + 5] = color.getBlue() / 255f;
				} else {
					colors[ci] = style.getRed(col - colsOffset, nextRow
							- rowsOffset) / 255f;
					colors[ci + 1] = style.getGreen(col - colsOffset, nextRow
							- rowsOffset) / 255f;
					colors[ci + 2] = style.getBlue(col - colsOffset, nextRow
							- rowsOffset) / 255f;
					colors[ci + 3] = style.getRed(col - colsOffset, row
							- rowsOffset) / 255f;
					colors[ci + 4] = style.getGreen(col - colsOffset, row
							- rowsOffset) / 255f;
					colors[ci + 5] = style.getBlue(col - colsOffset, row
							- rowsOffset) / 255f;
				}
				ci += 6;
			}

			coordinates[ci] = cols;
			coordinates[ci + 1] = coordinates[ci - 5];
			coordinates[ci + 2] = coordinates[ci - 4];
			coordinates[ci + 3] = cols;
			coordinates[ci + 4] = coordinates[ci - 2];
			// - z coordinates because we want to display back into the screen
			coordinates[ci + 5] = coordinates[ci - 1];

			colors[ci] = colors[ci - 6];
			colors[ci + 1] = colors[ci - 5];
			colors[ci + 2] = colors[ci - 4];
			colors[ci + 3] = colors[ci - 3];
			colors[ci + 4] = colors[ci - 2];
			colors[ci + 5] = colors[ci - 1];

			ci += 6;

		}

		// do the last row
		int row = rows - 1;
		for (int col = 0; col < cols; col++) {
			// get y coord from the style (height map)
			float swY = style.getY(col - colsOffset, row - rowsOffset);
			float nwY = swY;

			coordinates[ci] = col;
			coordinates[ci + 1] = nwY;
			// - z coordinates because we want to display back into the screen
			coordinates[ci + 2] = -(row + 1);
			coordinates[ci + 3] = col;
			coordinates[ci + 4] = swY;
			// - z coordinates because we want to display back into the screen
			coordinates[ci + 5] = -row;

			color = (Color) style.getPaint(col - colsOffset, row - rowsOffset);
			if (color != null) {
				colors[ci] = color.getRed() / 255f;
				colors[ci + 1] = color.getGreen() / 255f;
				colors[ci + 2] = color.getBlue() / 255f;

				color = (Color) style.getPaint(col - colsOffset, row
						- rowsOffset);
				colors[ci + 3] = color.getRed() / 255f;
				colors[ci + 4] = color.getGreen() / 255f;
				colors[ci + 5] = color.getBlue() / 255f;
			} else {
				colors[ci] = style.getRed(col - colsOffset, row - rowsOffset) / 255f;
				colors[ci + 1] = style.getGreen(col - colsOffset, row
						- rowsOffset) / 255f;
				colors[ci + 2] = style.getBlue(col - colsOffset, row
						- rowsOffset) / 255f;
				colors[ci + 3] = style.getRed(col - colsOffset, row
						- rowsOffset) / 255f;
				colors[ci + 4] = style.getGreen(col - colsOffset, row
						- rowsOffset) / 255f;
				colors[ci + 5] = style.getBlue(col - colsOffset, row
						- rowsOffset) / 255f;
			}
			ci += 6;

		}

		// do the final column of the final row
		int col = cols - 1;

		// get y coord from the style (height map)
		float swY = style.getY(col - colsOffset, row - rowsOffset);
		float nwY = swY;

		coordinates[ci] = cols;
		coordinates[ci + 1] = nwY;
		// - z coordinates because we want to display back into the screen
		coordinates[ci + 2] = -(row + 1);
		coordinates[ci + 3] = cols;
		coordinates[ci + 4] = swY;
		// - z coordinates because we want to display back into the screen
		coordinates[ci + 5] = -row;

		color = (Color) style.getPaint(col - colsOffset, row - rowsOffset);
		if (color != null) {
			colors[ci] = color.getRed() / 255f;
			colors[ci + 1] = color.getGreen() / 255f;
			colors[ci + 2] = color.getBlue() / 255f;

			color = (Color) style.getPaint(col - colsOffset, row - rowsOffset);
			colors[ci + 3] = color.getRed() / 255f;
			colors[ci + 4] = color.getGreen() / 255f;
			colors[ci + 5] = color.getBlue() / 255f;
		} else {
			colors[ci] = style.getRed(col - colsOffset, row - rowsOffset) / 255f;
			colors[ci + 1] = style.getGreen(col - colsOffset, row - rowsOffset) / 255f;
			colors[ci + 2] = style.getBlue(col - colsOffset, row - rowsOffset) / 255f;
			colors[ci + 3] = style.getRed(col - colsOffset, row - rowsOffset) / 255f;
			colors[ci + 4] = style.getGreen(col - colsOffset, row - rowsOffset) / 255f;
			colors[ci + 5] = style.getBlue(col - colsOffset, row - rowsOffset) / 255f;
		}

		int[] stripCounts = new int[rows];
		int strips = (cols + 1) * 2;
		for (int strip = 0; strip < rows; strip++) {
			stripCounts[strip] = strips;
		}

		TriangleStripArray tsa = new TriangleStripArray(numCoords / 3,
				TriangleStripArray.BY_REFERENCE
						| TriangleStripArray.COORDINATES
						| TriangleStripArray.COLOR_3, stripCounts);
		tsa.setCoordRefFloat(coordinates);
		tsa.setColorRefFloat(colors);

		GeometryInfo gi = new GeometryInfo(tsa);
		NormalGenerator gen = new NormalGenerator();
		gen.generateNormals(gi);
		geometry = gi.getGeometryArray(true, false, false);
		geometry.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
		geometry.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
		geometry.setCapability(GeometryArray.ALLOW_COUNT_READ);
		geometry.setCapability(GeometryArray.ALLOW_NORMAL_READ);
		geometry.setCapability(GeometryArray.ALLOW_NORMAL_WRITE);
		return this.geometry;
	}

	public synchronized void update(ValueLayerStyle3D style) {
		applyNeeded = true;
		int i = 0;
		Color color;

		// System.out.println("updateArray[0] = " + updateArray[0]);
		for (int row = 0, n = rows - 1; row < n; row++) {
			for (int col = 0; col < cols; col++) {

				int nextRow = row + 1;
				float nwY = style.getY(col - colsOffset, nextRow - rowsOffset);
				float swY = style.getY(col - colsOffset, row - rowsOffset);

				color = (Color) style.getPaint(col - colsOffset, nextRow
						- rowsOffset);

				if (color != null) {
					updateArray[i++] = nwY;
					updateArray[i++] = color.getRed() / 255f;
					updateArray[i++] = color.getGreen() / 255f;
					updateArray[i++] = color.getBlue() / 255f;

					color = (Color) style.getPaint(col - colsOffset, row
							- rowsOffset);

					updateArray[i++] = swY;
					updateArray[i++] = color.getRed() / 255f;
					updateArray[i++] = color.getGreen() / 255f;
					updateArray[i++] = color.getBlue() / 255f;
				} else {
					updateArray[i++] = nwY;
					updateArray[i++] = style.getRed(col - colsOffset, nextRow
							- rowsOffset) / 255f;
					updateArray[i++] = style.getGreen(col - colsOffset, nextRow
							- rowsOffset) / 255f;
					updateArray[i++] = style.getBlue(col - colsOffset, nextRow
							- rowsOffset) / 255f;

					updateArray[i++] = swY;
					updateArray[i++] = style.getRed(col - colsOffset, row
							- rowsOffset) / 255f;
					updateArray[i++] = style.getGreen(col - colsOffset, row
							- rowsOffset) / 255f;
					updateArray[i++] = style.getBlue(col - colsOffset, row
							- rowsOffset) / 255f;
				}
			}

			// do the final "fake" column
			int col = cols - 1;
			int nextRow = row + 1;
			float nwY = style.getY(col - colsOffset, nextRow - rowsOffset);
			float swY = style.getY(col - colsOffset, row - rowsOffset);

			color = (Color) style.getPaint(col - colsOffset, nextRow
					- rowsOffset);

			if (color != null) {
				updateArray[i++] = nwY;
				updateArray[i++] = color.getRed() / 255f;
				updateArray[i++] = color.getGreen() / 255f;
				updateArray[i++] = color.getBlue() / 255f;

				color = (Color) style.getPaint(col - colsOffset, row
						- rowsOffset);

				updateArray[i++] = swY;
				updateArray[i++] = color.getRed() / 255f;
				updateArray[i++] = color.getGreen() / 255f;
				updateArray[i++] = color.getBlue() / 255f;
			} else {
				updateArray[i++] = nwY;
				updateArray[i++] = style.getRed(col - colsOffset, nextRow
						- rowsOffset) / 255f;
				updateArray[i++] = style.getGreen(col - colsOffset, nextRow
						- rowsOffset) / 255f;
				updateArray[i++] = style.getBlue(col - colsOffset, nextRow
						- rowsOffset) / 255f;

				updateArray[i++] = swY;
				updateArray[i++] = style.getRed(col - colsOffset, row
						- rowsOffset) / 255f;
				updateArray[i++] = style.getGreen(col - colsOffset, row
						- rowsOffset) / 255f;
				updateArray[i++] = style.getBlue(col - colsOffset, row
						- rowsOffset) / 255f;
			}
		}

		// do the last "fake" row
		int row = rows - 1;
		for (int col = 0; col < cols; col++) {

			float nwY = style.getY(col - colsOffset, row - rowsOffset);
			float swY = nwY;

			color = (Color) style.getPaint(col - colsOffset, row - rowsOffset);

			if (color != null) {
				updateArray[i++] = nwY;
				updateArray[i++] = color.getRed() / 255f;
				updateArray[i++] = color.getGreen() / 255f;
				updateArray[i++] = color.getBlue() / 255f;

				color = (Color) style.getPaint(col - colsOffset, row
						- rowsOffset);

				updateArray[i++] = swY;
				updateArray[i++] = color.getRed() / 255f;
				updateArray[i++] = color.getGreen() / 255f;
				updateArray[i++] = color.getBlue() / 255f;
			} else {
				updateArray[i++] = nwY;
				updateArray[i++] = style.getRed(col - colsOffset, row
						- rowsOffset) / 255f;
				updateArray[i++] = style.getGreen(col - colsOffset, row
						- rowsOffset) / 255f;
				updateArray[i++] = style.getBlue(col - colsOffset, row
						- rowsOffset) / 255f;

				updateArray[i++] = swY;
				updateArray[i++] = style.getRed(col - colsOffset, row
						- rowsOffset) / 255f;
				updateArray[i++] = style.getGreen(col - colsOffset, row
						- rowsOffset) / 255f;
				updateArray[i++] = style.getBlue(col - colsOffset, row
						- rowsOffset) / 255f;
			}
		}

		// do the final fake column for the fake row
		// do the final "fake" column
		int col = cols - 1;
		float nwY = style.getY(col - colsOffset, row - rowsOffset);
		float swY = nwY;

		color = (Color) style.getPaint(col - colsOffset, row - rowsOffset);

		if (color != null) {
			updateArray[i++] = nwY;
			updateArray[i++] = color.getRed() / 255f;
			updateArray[i++] = color.getGreen() / 255f;
			updateArray[i++] = color.getBlue() / 255f;

			color = (Color) style.getPaint(col - colsOffset, row - rowsOffset);

			updateArray[i++] = swY;
			updateArray[i++] = color.getRed() / 255f;
			updateArray[i++] = color.getGreen() / 255f;
			updateArray[i++] = color.getBlue() / 255f;
		} else {
			updateArray[i++] = nwY;
			updateArray[i++] = style.getRed(col - colsOffset, row - rowsOffset) / 255f;
			updateArray[i++] = style.getGreen(col - colsOffset, row
					- rowsOffset) / 255f;
			updateArray[i++] = style
					.getBlue(col - colsOffset, row - rowsOffset) / 255f;

			updateArray[i++] = swY;
			updateArray[i++] = style.getRed(col - colsOffset, row - rowsOffset) / 255f;
			updateArray[i++] = style.getGreen(col - colsOffset, row
					- rowsOffset) / 255f;
			updateArray[i++] = style
					.getBlue(col - colsOffset, row - rowsOffset) / 255f;
		}

	}

	public synchronized void applyUpdate(Geometry geometry) {
		if (applyNeeded) {
			GeometryArray array = (GeometryArray) geometry;
			float[] coordinates = array.getCoordRefFloat();
			float[] colors = array.getColorRefFloat();

			for (int i = 0, ci = 0; i < updateArray.length; ci += 6) {
				coordinates[ci + 1] = updateArray[i++];
				colors[ci] = updateArray[i++];
				colors[ci + 1] = updateArray[i++];
				colors[ci + 2] = updateArray[i++];

				coordinates[ci + 4] = updateArray[i++];
				colors[ci + 3] = updateArray[i++];
				colors[ci + 4] = updateArray[i++];
				colors[ci + 5] = updateArray[i++];
			}
		}

		applyNeeded = false;

		/*
		 * int ci = 0; // coordinate index for (int row = 0, n = rows - 1; row <
		 * n; row++) { for (int col = 0; col < cols; col++) {
		 * 
		 * int nextRow = row + 1;
		 * 
		 * float swY = style.getY(col, row); float nwY = style.getY(col,
		 * nextRow);
		 * 
		 * // we don't need to set the x and z because those will remain // the
		 * same colors[ci] = style.getRed(col, nextRow) / 255f; coordinates[ci +
		 * 1] = nwY; colors[ci + 1] = style.getGreen(col, nextRow) / 255f;
		 * colors[ci + 2] = style.getBlue(col, nextRow) / 255f;
		 * 
		 * colors[ci + 3] = style.getRed(col, row) / 255f; coordinates[ci + 4] =
		 * swY; colors[ci + 4] = style.getGreen(col, row) / 255f; colors[ci + 5]
		 * = style.getBlue(col, row) / 255f; ci += 6; } }
		 */
	}
}
