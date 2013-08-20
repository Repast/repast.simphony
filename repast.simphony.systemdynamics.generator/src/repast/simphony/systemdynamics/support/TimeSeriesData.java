/**
 * 
 */
package repast.simphony.systemdynamics.support;




import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.systemdynamics.translator.ArrayReferenceNative;

/**
 * @author bragen
 *
 */
public class TimeSeriesData {
    
    // this class provides support for time series data for which time is
    // not explicitly applied as a reference
    
    public boolean nativeDataTypes = true;
    
    public static final int INTERPOLATE = 0;
    public static final int LOOK_FORWARD = 1;
    public static final int HOLD_BACKWORD = -1;
    
    private HashMap<String, Map<String, Map<Double, Double>>> timeSeriesData = 
	new HashMap<String, Map<String, Map<Double, Double>>>();
    
    private int betweenTimeMode = INTERPOLATE;
    
    public void addTimeSeries(Data data, String arrayOrScalar, String subscript, double[] time, double[] values, String btwnMode) {
	this.addTimeSeries(data, arrayOrScalar, subscript, time, values);
	if (btwnMode.equals(":INTERPOLATE:"))
	    betweenTimeMode = INTERPOLATE;
	else if (btwnMode.equals(":LOOK FORWARD:"))
	    betweenTimeMode = LOOK_FORWARD;
	else if (btwnMode.equals(":HOLD BACKWARD:"))
	    betweenTimeMode = HOLD_BACKWORD;
    }
    
    public void addTimeSeries(Data data, String arrayOrScalar, String subscript, double[] time, double[] values) {
	
	// note that scalars can also be assigned values in this manner
	// treat them as an array with empty subscript
	
	
	
	if (!timeSeriesData.containsKey(arrayOrScalar))
	    timeSeriesData.put(arrayOrScalar, new HashMap<String, Map<Double, Double>>());
	Map<String, Map<Double, Double>> timeSeries = timeSeriesData.get(arrayOrScalar);
	
	if (!timeSeries.containsKey(subscript))
	    timeSeries.put(subscript, new HashMap<Double, Double>());
	
	Map<Double, Double> series = timeSeries.get(subscript);
	
	for (int i = 0; i < time.length; i++) {
	    series.put(time[i], values[i]);
	}
	
	// set initial values
	if (subscript.equals("")) {
//	    data.setValue(arrayOrScalar, values[0]);
	} else {
	    data.registerArray(arrayOrScalar);
//	    data.arraySetValue(arrayOrScalar, subscript, values[0]);
	}
	    
    }
    
    public void advanceTime(Data data, double time) {
	// note that scalars can also be assigned values in this manner
	// treat them as an array with empty subscript

	// if there is a data point for this time value, set it, 
	// otherwise need to interpolate!
	
	for (String arrayOrScalar : timeSeriesData.keySet()) {
	    Map<String, Map<Double, Double>> timeSeries = timeSeriesData.get(arrayOrScalar);
	    for (String subscript : timeSeries.keySet()) {
		Map<Double, Double> series = timeSeries.get(subscript);
		if (series.containsKey(time)) {
		    if (subscript.equals("")) {
			data.setValue(arrayOrScalar, series.get(time));
		    } else {
			data.arraySetValue(arrayOrScalar, subscript, series.get(time));
		    }
		} else {
		    if (subscript.equals("")) {
//			data.extendScalarHistory(arrayOrScalar);
			data.setValue(arrayOrScalar, getDataBetweenTimes(arrayOrScalar, time, betweenTimeMode));
		    } else {
//			data.extendArrayHistory(arrayOrScalar, subscript);
			if (!nativeDataTypes)
			    data.arraySetValue(arrayOrScalar, subscript, getDataBetweenTimes(arrayOrScalar+"["+subscript+"]", time, betweenTimeMode));
			else
			    data.arraySetValue(arrayOrScalar, subscript, getDataBetweenTimes(arrayOrScalar+subscript, time, betweenTimeMode));
		    }
		}
	    }
	}

    }
    
    public double getDataForTime(String name, String subs, double time) {

	Map<Double, Double> map = timeSeriesData.get(name).get(subs);
	return map.get(time);
    }
    
    public List<Double> getTimesFor(String name, String subs) {
	List<Double> al = new ArrayList<Double>();
	Map<Double, Double> map = timeSeriesData.get(name).get(subs);
	for (Double time : map.keySet())
	    al.add(time);
	Collections.sort(al);
	return al;
    }
    
    public double getDataBetweenTimes(String tsDataName, double time, double mode) {
	double result = 0.0;
	String name = null;
	String subs = null;
	if (!nativeDataTypes) {
	    ArrayReference ar = new ArrayReference(tsDataName);
	    name = ar.getArrayName();
	    subs = ar.getSubscriptsAsMethodParameters().replace("\"", ""); // no " in this instance
	} else {
	    ArrayReferenceNative ar = new ArrayReferenceNative(tsDataName);
	    name = ar.getArrayName();
	    subs = ar.getSubscriptsAsMethodParameters().replace("\"", ""); // no " in this instance
	    
	}
	List<Double> times = getTimesFor(name, subs);
	
//	if (times.contains(time))
//	    return getDataForTime(name, subs, time);
	if (mode == -1.0) {
	    return getDataForTime(name, subs, getIndexPrior(times, time));
	} else if (mode == 1.0) {
	    return getDataForTime(name, subs, getIndexAfter(times, time));
	} else { // mode = 0
	    int prior = getIndexPrior(times, time);
	    if (prior == times.size()-1)
		return getDataForTime(name, subs, times.get(prior));
	    double v1 = getDataForTime(name, subs, times.get(prior));
	    double v2 = getDataForTime(name, subs, times.get(prior+1));
	    double timeStep = times.get(prior+1) - times.get(prior);
	    double deltaT = time - times.get(prior);
	    
	    result = v1 + (v2-v1) * deltaT/timeStep;
	    return result;
	}
	
	
    }
    
    private int getIndexPrior(List<Double> times, double time) {
	int index = 0;
	for (index = 0; index < times.size()-1; index++) {
	    if (times.get(index) >= time) {
		return index == 0 ? 0 : index-1;
	    }
	}
	return times.size()-1;
    }
    
    private int getIndexAfter(List<Double> times, double time) {
	return getIndexPrior(times, time) + 1;
    }
    
    public boolean hasTimeSeriesFor(String name) {

	// this may be an array referene
	String n = "";
	
	if (ArrayReference.isArrayReference(name)) {
	    n = new ArrayReference(name).getArrayName();
	} else {
	    n = name;
	}

	if (timeSeriesData.containsKey(n))
	    return true;
	else
	    return false;
    }

    public boolean isNativeDataTypes() {
        return nativeDataTypes;
    }

    public void setNativeDataTypes(boolean nativeDataTypes) {
        this.nativeDataTypes = nativeDataTypes;
    }
    

}
