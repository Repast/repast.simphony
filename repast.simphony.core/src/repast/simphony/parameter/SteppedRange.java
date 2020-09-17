/**
 * 
 */
package repast.simphony.parameter;

/**
 * Implements a range with step information for a GUI.
 * 
 * @author nick
 */
public class SteppedRange {

	private double min, max;
	private double step;
	private int hashCode;

	/**
	 * Creates a SteppedRange with the specified min, max and step values.
	 * 
	 * @param min
	 * @param max
	 * @param step
	 */
	public SteppedRange(double min, double max, double step) {
		this.min = min;
		this.max = max;
		this.step = step;
		
		long l = Double.doubleToLongBits(this.min);
	    hashCode = 31 * hashCode + (int)(l ^ (l >>> 32));
	    l = Double.doubleToLongBits(this.max);
	    hashCode = 31 * hashCode + (int)(l ^ (l >>> 32));
	    l = Double.doubleToLongBits(this.step);
	    hashCode = 31 * hashCode + (int)(l ^ (l >>> 32));
	}

	/**
	 * Gets whether or not the specified value is within the range ( >= min && <=
	 * max).
	 * 
	 * @param val
	 * @return true if the specified val is within the range, otherwise false.
	 */
	public boolean contains(double val) {
		return val >= min && val <= max;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public double getStep() {
		return step;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SteppedRange)) {
			return false;
		}

		SteppedRange other = (SteppedRange) obj;
		return other.min == min && other.max == max && other.step == step;
	}
	
	@Override
	public int hashCode() {
		return hashCode;
	}

}
