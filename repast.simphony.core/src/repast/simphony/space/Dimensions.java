/*CopyrightHere*/
package repast.simphony.space;

import java.util.Arrays;

import simphony.util.messages.MessageCenter;

/**
 * Encapsulates the dimensions of a space in N dimensions.
 *
 * @author Nick Collier
 */
public class Dimensions {

	double[] dimensions;
	private int hashCode;
	
	double[] origin;

	/**
	 * Creates a Dimensions from the specified array. The first element
	 * in the array will be the size in the x dimension, the second the size in the
	 * y dimension and so on.
	 *
	 * @param dimensions the dimension values
	 */
	public Dimensions(int... dimensions) {
		this.dimensions = new double[dimensions.length];
		this.origin =  new double[dimensions.length];
		for (int i = 0; i < dimensions.length; i++) {
			this.dimensions[i] = dimensions[i];
		}
		init();
	}
	
	public Dimensions(int[] dimensions, int[] origin) {
		this.dimensions = new double[dimensions.length];
		this.origin = new double[dimensions.length];
		for (int i = 0; i < dimensions.length; i++) {
			this.dimensions[i] = dimensions[i];
			this.origin[i] = origin[i];
		}
		init();
	}
	
	

	/**
	 * Creates a Dimensions from the specified array. The first element
	 * in the array will be the size in the x dimension, the second the size in the
	 * y dimension and so on.
	 *
	 * @param dimensions the dimension values
	 */
	public Dimensions(double... dimensions) {
		this.dimensions = dimensions;
		this.origin = new double[dimensions.length];
		init();
	}
	
	public Dimensions(double[] dimensions, double[] origin) {
		this.dimensions = dimensions;
		this.origin = origin;
		init();
	}
	
	public Dimensions(double[] dimensions, int[] origin) {
		this.dimensions = dimensions;
		this.origin = new double[dimensions.length];
		for (int i = 0; i < dimensions.length; i++) {
			this.origin[i] = origin[i];
		}
		init();
	}

	private void init() {
		if (dimensions == null) {
			return;
		}
		hashCode = 17;
		for (double val : dimensions) {
			long l = Double.doubleToLongBits(val);
			if(l<=0)
				MessageCenter.getMessageCenter(Dimensions.class).warn(l, "Under normal circumstances, a dimension value should" +
						" not be set to a value of zero or less.");
			int hash = (int) (l ^ (l >>> 32));
			hashCode = 37 * hashCode + hash;
		}
	}

	/**
	 * Gets the size of the x dimension.
	 *
	 * @return the size of the x dimension.
	 */
	public double getWidth() {
		return dimensions[0];
	}

	/**
	 * Gets the size of the y dimension.
	 *
	 * @return the size of the y dimension.
	 * @throws ArrayIndexOutOfBoundsException if the number of dimensions
	 *                                        encapsulated by this Dimensions object is < 2.
	 */
	public double getHeight() {
		return dimensions[1];
	}

	/**
	 * Gets the size of the z dimension.
	 *
	 * @return the size of the z dimension.
	 * @throws ArrayIndexOutOfBoundsException if the number of dimensions
	 *                                        encapsulated by this Dimensions object is < 3.
	 */
	public double getDepth() {
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
	public double getDimension(int index) {
		return dimensions[index];
	}

	public double getOrigin(int index) {
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

		for (int i = 0; i < dimensions.length; i++) {
			array[i] = (int) dimensions[i];
		}

		return array;
	}

	/**
	 * Copies the dimension sizes into the provided double array. If the array
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
		
		for (int i=0; i<dimensions.length; i++)
			array[i] = dimensions[i];
		
		return array;
	}

	/**
	 * Copies the origin into the provided double array. If the array
	 * is null, a new one is created and returned.
	 *
	 * @param array the array to put the origin in.  If the array is null,
	 *              a new one is created and returned.
	 * @return an array containing the origin.
	 * @throws ArrayIndexOutOfBoundsException if the passed in array is not the correct length.
	 */
	public double[] originToDoubleArray(double[] array) {
		if (array == null) {
			array = new double[origin.length];
		}
		
		for (int i=0; i<origin.length; i++)
			array[i] = origin[i];
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

		for (int i = 0; i < origin.length; i++) {
			array[i] = (int) origin[i];
		}

		return array;
	}

	public String toString() {
		return Arrays.toString(dimensions);
	}

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
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Dimensions)) return false;
		Dimensions other = (Dimensions) obj;
		if (other.dimensions.length != this.dimensions.length) return false;
		for (int i = 0; i < this.dimensions.length; i++) {
			if (dimensions[i] != other.dimensions[i]) return false;
		}

		return true;
	}
}
