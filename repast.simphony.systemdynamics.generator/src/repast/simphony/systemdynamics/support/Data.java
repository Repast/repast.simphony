package repast.simphony.systemdynamics.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {

    protected Map<String, Double> data = new HashMap<String, Double>();
    protected Map<String, List<Double>> dataHistory = new HashMap<String, List<Double>>();
    protected double currentTime;
    protected SDModel model;

    protected Map<String, Map<String, Double>> arrays = new HashMap<String, Map<String, Double>>();
    protected Map<String, Map<String, List<Double>>> arraysHistory = new HashMap<String, Map<String, List<Double>>>();
    protected Map<String, List<String>> subscripts = new HashMap<String, List<String>>();

    public Data(SDModel model) {
	this.model = model;
    }

    // Array support
    public void registerArray(String arrayName) {
	if (!arrays.containsKey(arrayName)) {
	    arrays.put(arrayName, new HashMap<String, Double>());
	    arraysHistory.put(arrayName, new HashMap<String, List<Double>>());
	}
    }

    // Subscript support
    public void registerSubscript(String subscriptName, String... strings) {
	List<String> values = new ArrayList<String>();
	for (String val : strings)
	    values.add(val);
	subscripts.put(subscriptName, values);
    }

    public List<SubscriptCombination> getSubscriptValueCombinations(
	    String... strings) {
	// given a list of subscripts return all combinations of values
	List<List<String>> bySubscriptValue = new ArrayList<List<String>>();

	List<SubscriptCombination> values = new ArrayList<SubscriptCombination>();
	List<String> order = new ArrayList<String>();

	// generate list of lists of values
	for (String subscriptName : strings) {
	    bySubscriptValue
		    .add(extractSubscripts(getSubscriptValues(subscriptName)));
	    order.add(subscriptName);
	}

	int numPermutations = 1;
	for (int i = 0; i < bySubscriptValue.size(); i++) {
	    numPermutations *= bySubscriptValue.get(i).size();
	}
	// create all permutations
	SubscriptCombination[] combinations = new SubscriptCombination[numPermutations];

	// initialize the empty buffers
	for (int i = 0; i < numPermutations; i++)
	    combinations[i] = new SubscriptCombination(order);

	// for each of the subscripts
	for (int subscriptNumber = 0; subscriptNumber < strings.length; subscriptNumber++) {

	    // determine the number of times that the same value should be
	    // outputed
	    int numPerValue = 1;
	    for (int i = subscriptNumber + 1; i < bySubscriptValue.size(); i++) {
		numPerValue *= bySubscriptValue.get(i).size();
	    }
	    int row = 0;
	    while (row < numPermutations) {
		for (String value : bySubscriptValue.get(subscriptNumber)) {

		    for (int i = 0; i < numPerValue; i++) {
			combinations[row].addSubscriptValue(
				strings[subscriptNumber], value);
			row++;
		    }
		}
	    }
	}

	for (SubscriptCombination combo : combinations) {
	    values.add(combo);
	}

	return values;
    }

    private List<String> extractSubscripts(List<String> subscriptList) {
	List<String> al = new ArrayList<String>();
	for (String token : subscriptList)
	    if (!token.equals("[") && !token.equals(",") && !token.equals("]"))
		al.add(token);
	return al;
    }

    public List<String> getSubscriptValues(String subscriptName) {
	List<String> al = new ArrayList<String>();

	// if not a named subscript, just return name as value
	if (subscripts.get(subscriptName) == null) {
	    al.add(subscriptName);
	} else {

	    for (String v : subscripts.get(subscriptName)) {
		if (isSubscript(v))
		    al.addAll(getSubscriptValues(v));
		else
		    al.add(v);
	    }
	}
	return al;
    }

    public String concatAsSubscript(List<String> list) {
	StringBuffer sb = new StringBuffer();
	for (String s : list) {
	    if (sb.length() > 0)
		sb.append(",");
	    sb.append(s);
	}
	return sb.toString();
    }

    public String concatAsSubscript(String... list) {
	StringBuffer sb = new StringBuffer();
	for (String s : list) {
	    if (sb.length() > 0)
		sb.append(",");
	    sb.append(s);
	}
	return sb.toString();
    }

    public boolean isSubscript(String subscriptName) {
	if (subscripts.containsKey(subscriptName))
	    return true;
	else
	    return false;
    }

    public boolean hasData(String var) {
	return data.containsKey(var);
    }

    public double valueOf(String var) {
	if (data.containsKey(var)) {
	    return data.get(var);
	}

	// this is part of a method call that has yet to be initialized
	else {
	    //  model.message.println("Uninitialized variable: "+var);
	    return 0.0;
	}
    }

    public List<Double> getDataHistoryFor(String variable) {
	return dataHistory.get(variable);
    }

    public double valueOf(String var, int time) {
	// some variables do not have a history
	List<Double> history = dataHistory.get(var);
	if (history == null) {
	    model.message.println("Uninitialized variable: " + var + " time = "
		    + time);
	    return -99999.;
	} else if (history.size() == 1) {
	    return history.get(0);
	} else if (history.size() > time) {
	    return history.get(time);
	} else {
	    model.message.println("Reaching past data: " + var + " time = " + time);
	    return -99999.;
	}
    }

    public double arrayValueOf(String array, String subscript) {
	if (getArrays().get(array) == null) {
	    // may not necessarily be bad
	    model.message.println("Premature access to undefined array " + array
		    + " with sub " + subscript);
	    if (currentTime > 0 || array.equals("Concentration"))
		model.message.println("Time > 0");
	    return -999.0;
	}
	if (getArrays().get(array).get(subscript) == null) {
	    // may not necessarily be bad
	    model.message.println("Premature access to uninitialized array "
		    + array + " with sub " + subscript);
	    if (currentTime > 0 || array.equals("Concentration"))
		model.message.println("Time > 0");
	    return -999.0;
	}
	return getArrays().get(array).get(subscript);
    }

    public List<String> getModelVariables() {
	List<String> variables = new ArrayList<String>();
	for (String var : data.keySet())
	    if (!var.equalsIgnoreCase("time"))
		variables.add(var);

	Collections.sort(variables);

	return variables;
    }

    public void setValue(String var, double value) {

	if (Double.isNaN(value))
	    model.message.println("Nan Error: " + var);

	data.put(var, value);
	if (!dataHistory.containsKey(var))
	    dataHistory.put(var, new ArrayList<Double>());
	List<Double> history = dataHistory.get(var);
	history.add(value);
    }

    public void extendScalarHistory(String var) {
	setValue(var, valueOf(var));
    }

    public void extendArrayHistory(String array, String subscript) {
	arraySetValue(array, subscript, arrayValueOf(array, subscript));
    }

    public void arraySetValue(String arrayName, String subscript, double value) {

	if (Double.isNaN(value))
	    model.message.println("Nan Error: " + arrayName + "[" + subscript+ "]");

	arrays.get(arrayName).put(subscript, value);

	if (!arraysHistory.containsKey(arrayName))
	    arraysHistory.put(arrayName, new HashMap<String, List<Double>>());
	if (!arraysHistory.get(arrayName).containsKey(subscript))
	    arraysHistory.get(arrayName)
		    .put(subscript, new ArrayList<Double>());
	List<Double> history = arraysHistory.get(arrayName).get(subscript);
	history.add(value);

    }

    public double getINITIALTIME() {
	if (data.containsKey("INITIALTIME")) {
	    return data.get("INITIALTIME");
	} else if (data.containsKey("INITIAL_TIME")) {
	    return data.get("INITIAL_TIME");
	} else if (data.containsKey("INITIAL TIME")) {
	    return data.get("INITIAL TIME");
	} else {
	    return -999.0;
	}
    }

    public double getFINALTIME() {
	if (data.containsKey("FINALTIME")) {
	    return data.get("FINALTIME");
	} else if (data.containsKey("FINAL_TIME")) {
	    return data.get("FINAL_TIME");
	} else {
	    return data.get("FINAL TIME");
	}
    }

    public double getTIMESTEP() {
	if (data.containsKey("TIMESTEP")) {
	    return data.get("TIMESTEP");
	} else if (data.containsKey("TIME_STEP")) {
	    return data.get("TIME_STEP");
	} else {
	    if (!data.containsKey("TIME STEP")) {
		model.message.println("TIME STEP: WTF!");
		return 0.125;
	    }
	    return data.get("TIME STEP");
	}
    }

    public double getCurrentTime() {
	return currentTime;
    }

    public void setCurrentTime(double currentTime) {
	currentTime = currentTime;
    }

    public Map<String, Double> getData() {
	return data;
    }

    public Map<String, List<Double>> getDataHistory() {
	return dataHistory;
    }

    public SDModel getModel() {
	return model;
    }

    public Map<String, Map<String, Double>> getArrays() {
	return arrays;
    }

    public void setArrays(Map<String, Map<String, Double>> arrays) {
	this.arrays = arrays;
    }

    public Map<String, List<String>> getSubscripts() {
	return subscripts;
    }

    public void setSubscripts(Map<String, List<String>> subscripts) {
	this.subscripts = subscripts;
    }
    


    public int getELMCOUNT(String subscript) {
	return subscripts.get(subscript).size();
    }

    public Map<String, Map<String, List<Double>>> getArraysHistory() {
        return arraysHistory;
    }

}
