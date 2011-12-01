/*CopyrightHere*/
package repast.simphony.space.grid;

import java.util.Arrays;

/**
 * Encapsulates the dimensions of a space in N dimensions.
 *
 * @author Nick Collier
 */
public class GridDimensions {

	int[] dimensions;
	private int hashCode;
	int[] origin;

	/**
	 * Creates a Dimensions from the specified array. The first element
	 * in the array will be the size in the x dimension, the second the size in the
	 * y dimension and so on.
	 *
	 * @param dimensions the dimension values
	 */
	public GridDimensions(int... dimensions) {
		this.dimensions = dimensions;
		this.origin =  new int[dimensions.length];
		init();
	}
	
	/**
	 * Creates a Dimensions from the specified array. The first element
	 * in the array will be the size in the x dimension, the second the size in the
	 * y dimension and so on.
	 *
	 * @param dimensions the dimension values
	 * 
	 * @param origin the origin values
	 */
	public GridDimensions(int[] dimensions, int[] origin) {
		this.dimensions = dimensions;
		this.origin = origin;
		init();
	}

	/**
	 * Creates a Dimensions from the specified array. The first element
	 * in the array will be the size in the x dimension, the second the size in the
	 * y dimension and so on.
	 *
	 * @param dimensions the dimension values
	 */
	public GridDimensions(double... dimensions) {
		this.dimensions = new int[dimensions.length];
		this.origin =  new int[dimensions.length];
		for (int i = 0; i < dimensions.length; i++) {
			this.dimensions[i] = (int) Math.rint(dimensions[i]);
		}
		init();
	}
	
	/**
	 * Creates a Dimensions from the specified array. The first element
	 * in the array will be the size in the x dimension, the second the size in the
	 * y dimension and so on.
	 *
	 * @param dimensions the dimension values
	 * 
	 * @param origin the origin values
	 */
	public GridDimensions(double[] dimensions, double[] origin) {
		this.dimensions = new int[dimensions.length];
		this.origin =  new int[dimensions.length];
		for (int i = 0; i < dimensions.length; i++) {
			this.dimensions[i] = (int) Math.rint(dimensions[i]);
			this.origin[i] = (int) Math.rint(origin[i]);
		}
		init();
	}

	private void init() {
		if (dimensions == null) {
			return;
		}
		hashCode = 17;
		for (int val : dimensions) {
			long l = val;
			int hash = (int) (l ^ (l >>> 32));
			hashCode = 37 * hashCode + hash;
		}
	}

	/**
	 * Gets the size of the x dimension.
	 *
	 * @return the size of the x dimension.
	 */
	public int getWidth() {
		return dimensions[0];
	}

	/**
	 * Gets the size of the y dimension.
	 *
	 * @return the size of the y dimension.
	 * @throws ArrayIndexOutOfBoundsException if the number of dimensions
	 *                                        encapsulated by this Dimensions object is < 2.
	 */
	public int getHeight() {
		return dimensions[1];
	}

	/**
	 * Gets the size of the z dimension.
	 *
	 * @return the size of the z dimension.
	 * @throws ArrayIndexOutOfBoundsException if the number of dimensions
	 *                                        encapsulated by this Dimensions object is < 3.
	 */
	public int getDepth() {
		return dimensions[2];
	}

	/**
	 * Gets the dimension size at the specified index. The x coordinate is at index 0, y at 1,
	 * z at 2 and so on.
	 *
	 * @param index the index of the dimension
	 * @return the dimension size at the specified index.
	 * @throws ArrayIndexOutOfBoundsException if the number of dimensions encapsulated
	 *                                        by this Dimensions object is less than specified index.
	 */
	public int getDimension(int index) {
		return dimensions[index];
	}
	
	/**
	 * Gets the origin at the specified index. The x coordinate is at index 0, y at 1,
	 * z at 2 and so on.
	 *
	 * @param index the index of the dimension
	 * @return the origin at the specified index.
	 * @throws ArrayIndexOutOfBoundsException if the number of dimensions encapsulated
	 *                                        by this Dimensions object is less than specified index.
	 */
	public int getOrigin(int index) {
		return origin[index];
	}

	/**
	 * Gets the number of dimensions in this Dimensions.
	 *
	 * @return the number of dimensions in this Dimensions.
	 */
	public int size() {
		return dimensions.length;
	}

	/**
	 * Copies the dimension sizes into the provided int array, casting
	 * to an int if necessary. If the array is null, a new one is created and returned.
	 *
	 * @param array the array to put the dimension sizes in.  If the array is null,
	 *              a new one is created and returned.
	 * @return an array containing the dimension sizes.
	 * @throws ArrayIndexOutOfBoundsException if the passed in array is not the correct length.
	 */
	public int[] toIntArray(int[] array) {
		if (array == null) {
			array = new int[dimensions.length];
		}
		
		for (int i=0; i<dimensions.length; i++)
			array[i] = dimensions[i];
		
		return array;
	}
	
	/**
	 * Copies the origin into the provided int array, casting
	 * to an int if necessary. If the array is null, a new one is created and returned.
	 *
	 * @param array the array to put the origin in.  If the array is null,
	 *              a new one is created and returned.
	 * @return an array containing the origin.
	 * @throws ArrayIndexOutOfBoundsException if the passed in array is not the correct length.
	 */
	public int[] originToIntArray(int[] array) {
		if (array == null) {
			array = new int[origin.length];
		}
		
		for (int i=0; i<origin.length; i++)
			array[i] = origin[i];
		
		return array;
	}

	/**
	 * Copies the dimension sizes into the provied double array. If the array
	 * is null, a new one is created and returned.
	 *
	 * @param array the array to put the dimension sizes in.  If the array is null,
	 *              a new one is created and returned.
	 * @return an array containing the dimension sizes.
	 * @throws ArrayIndexOutOfBoundsException if the passed in array is not the correct length.
	 */
	public double[] toDoubleArray(double[] array) {
		if (array == null) {
			array = new double[dimensions.length];
		}

		for (int i = 0; i < dimensions.length; i++) {
			array[i] = dimensions[i];
		}

		return array;
	}


	@Override
	public String toString() {
		return Arrays.toString(dimensions);
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	/**
	 * Overrides equals. Returns true if the other object is a Dimension whose
	 * dimension values are the same as this one.
	 *
	 * @param obj
	 * @return true if the other object is a Dimension whose
	 *         dimension values are the same as this one.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof GridDimensions)) return false;
		GridDimensions other = (GridDimensions) obj;
		if (other.dimensions.length != this.dimensions.length) return false;
		for (int i = 0; i < this.dimensions.length; i++) {
			if (dimensions[i] != other.dimensions[i]) return false;
		}

		return true;
	}
}
