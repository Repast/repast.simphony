package repast.simphony.valueLayer;

import java.util.Arrays;

/**
 * Value layer store implementation backed by a dense array.
 * 
 * @author Eric Tatatara
 *
 */
public class DenseValueLayerStore implements ValueLayerStore{

	// TODO bounds checks
	
	protected double[] array;
	
	public DenseValueLayerStore(int size){
		array = new double[size];
	}
	
	public DenseValueLayerStore(int size, double defaultValue){
		array = new double[size];
		Arrays.fill(array, defaultValue);
	}
	
	@Override
	public double get(long index){
		return array[(int)index];
	}
	
	@Override
	public void set(long index, double value){
		array[(int)index] = value;
	}

	@Override
	public int size() {
		return array.length;
	}
}