package repast.simphony.systemdynamics.support;

import java.util.List;

public class SDModel {
    
    
    public static String OUTPUT_DIRECTORY = "output/";
    public static String DATA_DIRECTORY = "data/";
    
    protected Data data;
    protected SDFunctionsWithXLSColt sdFunctions;
//    protected RunnerWithProperties runner;
    protected String name;
    protected Message message = null;
    protected ResultsReporter results = null;
    
    protected TimeSeriesData timeSeriesData = new TimeSeriesData();

    protected double currentTime;
    
    protected static boolean dumpMemory = false;
    
   
    public SDModel(/* RunnerWithProperties runner */ String name, String[] args) {
//	this.runner = runner;
//	this.name = runner.getName();
	this.name = name;
	
	data = new Data(this);
	
//	// use SDFunctions(this) or SDFunctionsWithXLS(this)
//	sdFunctions = new SDFunctionsWithXLSColt(this);
//	// use new IOJVM(data) or new IOJS(data)
//	io = new IOJVM(data);
//	results = new ResultsReporterJava();
    }

    public double getINITIALTIME() {
        return data.getINITIALTIME();
    }

    public double getFINALTIME() {
        return data.getFINALTIME();
    }

    public double getTIMESTEP() {
        return data.getTIMESTEP();
    }
    
    public void setValue(String var, double v) {
        data.setValue(var, v);
    }
    
    public double valueOf(String var) {
	double d = data.valueOf(var);
        return data.valueOf(var);
    }

    public double valueOf(String var, int time) {
        return data.valueOf(var, time);
    }
    
    public double arrayValueOf(String array, String subscript) {
	return data.arrayValueOf(array, subscript);
    }
    
    public void arraySetValue(String array, String subscript, double value) {
	registerArray(array);
	
	data.arraySetValue(array, subscript, value);
    }
    
    public int[] newIntArray(int len, int ... values) {
	int[] array = new int[len];
	for (int i = 0; i < len; i++) 
	    array[i] = values[i];
	return array;
    }
    
    public double[] newDoubleArray(int len, double ... values) {
	double[] array = new double[len];
	for (int i = 0; i < len; i++) 
	    array[i] = values[i];
	return array;
    }
    
    public String intToString(int i) {
	return Integer.toString(i);
    }
    
    public String doubleToString(double d) {
	return Double.toString(d);
    }

    public String stringConcat(String ...strings) {
	StringBuffer sb = new StringBuffer();
	for (String s : strings)
	    sb.append(s);
	return sb.toString();
	
    }

    
    public void execute() {

	Initializer.initialize();
	
	    oneTime();
	    int tick = 0;
	    for (double time = getINITIALTIME(); time <= getFINALTIME(); time += getTIMESTEP()) {
	         data.setCurrentTime(time);
	         currentTime = time;
	         Synchronizer.synchronize(currentTime, getTIMESTEP());
	         repeated(time, getTIMESTEP());
	         reportTimeStep(time);
	         
	         if (dumpMemory) {
	             // dump memory at the end of each time tick
	             String filename = "_memoryDump_"+(tick++)+".csv";
	             results.writeReport(RunnerConstants.OUTPUT_DIRECTORY+name+filename, data);
	         }
	    }
	    results.writeReport(RunnerConstants.OUTPUT_DIRECTORY+name+"_sdReport.csv", data);
	}
    
    protected void oneTime() {}
    
    protected void oneTime(double time, double timeStep) {}
    
    protected void repeated(double time, double timeStep) {}
    
    protected void reportTimeStep(double time) {}
    
    
    // Subscript and Array Passthroughs
	// Array support
	public void registerArray(String arrayName) {
	    data.registerArray(arrayName);
	}
	
	
	// Subscript support
	public void registerSubscript(String subscriptName, String ...strings ) {
	    data.registerSubscript(subscriptName, strings);
	}
	
	public List<SubscriptCombination> getSubscriptValueCombinations(String ...strings) {
	    return data.getSubscriptValueCombinations(strings);
	}
	
	public List<SubscriptCombination> getSubscriptValueCombinations(String string) {
	    return data.getSubscriptValueCombinations(string);
	}
	
	public List<String> getSubscriptValues(String subscriptName) {
	    return data.getSubscriptValues(subscriptName);
	}
	
	public String concatAsSubscript(List<String> list) {
	    return data.concatAsSubscript(list);
	}
	
	public String concatAsSubscript(String ... list) {
	    return data.concatAsSubscript(list);
	}
	
	public boolean isSubscript(String subscriptName) {
	    return data.isSubscript(subscriptName);
	}
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Data getData() {
        return data;
    }

    public SDFunctions getSdFunctions() {
        return sdFunctions;
    }

//    public RunnerWithProperties getRunner() {
//        return runner;
//    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }

    public TimeSeriesData getTimeSeriesData() {
        return timeSeriesData;
    }

    public void setTimeSeriesData(TimeSeriesData timeSeriesData) {
        this.timeSeriesData = timeSeriesData;
    }
}
