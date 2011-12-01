package repast.simphony.batch.distributed;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Object used to hold batch distributed results for individual runs if needed.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 */
@SuppressWarnings("serial")
public class BatchRunResults<T> implements Serializable{

	/**Simulation run number*/
	private Map<Integer,Integer> setRunNumber;
	
	/**Map for holding simulation result*/
	private Map<File,Object> results = new HashMap<File,Object>();

	/**
	 * Data relative to a simulation run.
	 * @param number the simulation run number.
	 */
	public void putBatchRunResults(int number){
		if(setRunNumber==null)
			setRunNumber=new HashMap<Integer,Integer>();
		setRunNumber.put(number, number);
	}
	
	public BatchRunResults(){
		
	}

	public Map<Integer,Integer> getSetRunNumber() {
		return setRunNumber;
	}

	public void setSetRunNumber(final Map<Integer,Integer> setRunNumber) {
		this.setRunNumber = setRunNumber;
	}

	public void setResults(Map<File,Object> results) {
		this.results = results;
	}

	public Map<File,Object> getResults() {
		return results;
	}
	
	/**
	 * Add data to results of simulation runs.
	 * @param t1 the key
	 * @param t2 the value
	 */
	public void add(File t1, Object t2){
		results.put(t1,t2);
	}
}
