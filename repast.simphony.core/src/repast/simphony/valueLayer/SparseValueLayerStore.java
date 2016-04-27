package repast.simphony.valueLayer;

import repast.simphony.util.collections.OpenLongToDoubleHashMap;

/**
 * Value layer store backed by a sparse map.
 * 
 * @author Eric Tatara
 * 
 *
 */
public class SparseValueLayerStore implements ValueLayerStore{

	protected double defaultValue;
	protected OpenLongToDoubleHashMap store;
	
	public SparseValueLayerStore(int size, double defaultValue){
		store = new OpenLongToDoubleHashMap(defaultValue); 
		
		this.defaultValue = defaultValue;
	}
	
	@Override
	public double get(long index){
		return store.get(index);
	}
	
	@Override
	public void set(long index, double value){
		if (value == defaultValue) store.remove(index);
		
		else	store.put(index,value);
	}

	@Override
	public int size() {
		return store.size();
	}
}