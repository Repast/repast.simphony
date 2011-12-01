/*CopyrightHere*/
package repast.simphony.space.continuous;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;

/**
 * This will place the added object at a random location in the space. Since the space should be
 * adding new objects to itself automatically, this performs a move on those objects to a random
 * location.
 */
public class RandomCartesianAdder<T> implements ContinuousAdder<T> {
	/**
	 * Adds the specified object to the space at a random location.
	 * 
	 * @param space
	 *            the space to add the object to.
	 * @param obj
	 *            the object to add.
	 */
	public void add(ContinuousSpace<T> space, T obj) {
		Dimensions dims = space.getDimensions();
		double[] location = new double[dims.size()];
		findLocation(location, dims);
		while (!space.moveTo(obj, location)) {
			findLocation(location, dims);
		}
	}

	private void findLocation(double[] location, Dimensions dims) {
		double[] origin = dims.originToDoubleArray(null);
		for (int i = 0; i < location.length; i++) {
			try{
				location[i] = RandomHelper.getUniform().nextDoubleFromTo(0, dims.getDimension(i)) - origin[i];
			}
			catch(Exception e){
				
			}
		}
	}
}
