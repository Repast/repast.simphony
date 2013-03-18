package repast.simphony.systemdynamics.support;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogConverter {
    
  
    private Map<String, Map<Integer, Map<Integer, String>>> lookup;
    /*
     * Read SubscriptSpace.csv
     * Read Log file
     * Convert Log subscript to alpha subscripts
     * Format data as csv with one row per variable
     */
    
    public class LogRecord {
	String variable;
	List<String> subscripts;
	double time;
	double value;
	
	public LogRecord(String aLine) {
	    parse(aLine);
	}
	
	public void parse(String aLine) {
	    String[] columns = aLine.split(",");
	    variable = extractVariable(columns[0].trim());
	    subscripts = extractSubscripts(columns[0].trim());
	    time = Double.parseDouble(columns[1].trim());
	    value = Double.parseDouble(columns[2].trim());
	}
	
	public String extractVariable(String variable) {
	    String var = variable;
	    
	    if (!variable.contains("["))
		return var;
	    else
		var = variable.split("\\[")[0];	    
	    
	    return var;
	    
	}
	
	public List<String> extractSubscripts(String variable) {
	    List<String> al = new ArrayList<String>();
	    if (!variable.contains("["))
		return al;
	    String[] var = variable.split("\\[");
	    for (int i = 1; i < var.length; i++) {
		al.add(var[i].replace("]", "").trim());
	    }
	    
	    return al;
	}
	
	public String getVariable() {
	    return variable;
	}
	
	public void setVariable(String variable) {
	    this.variable = variable;
	}
	
	public List<String> getSubscripts() {
	    return subscripts;
	}
	
	public void setSubscripts(List<String> subscripts) {
	    this.subscripts = subscripts;
	}
	
	public double getTime() {
	    return time;
	}
	
	public void setTime(double time) {
	    this.time = time;
	}
	
	public double getValue() {
	    return value;
	}
	
	public void setValue(double value) {
	    this.value = value;
	}
    }
    
    public class SubscriptRecord {
	
	String variable;
	int dimension;
	String subscript;
	int index;
	
	public SubscriptRecord(String aLine) {
	    parse(aLine);
	}
	
	public void parse(String aLine) {
	    String[] columns = aLine.split(",");
	    variable = columns[0].trim();
	    dimension = Integer.parseInt(columns[1].trim());
	    subscript = columns[2].trim();
	    index = Integer.parseInt(columns[3].trim());
	}
	
	public String getVariable() {
	    return variable;
	}
	
	public void setVariable(String variable) {
	    this.variable = variable;
	}
	
	public int getDimension() {
	    return dimension;
	}
	
	public void setDimension(int dimension) {
	    this.dimension = dimension;
	}
	
	public String getSubscript() {
	    return subscript;
	}
	
	public void setSubscript(String subscript) {
	    this.subscript = subscript;
	}
	
	public int getIndex() {
	    return index;
	}
	
	public void setIndex(int index) {
	    this.index = index;
	}
	

    }
    
    public static void main(String[] args) {
	
	LogConverter logConverter = new LogConverter();
	logConverter.convert(args);
    }
    
    public void convert(String[] args) {
	
	List<LogRecord> logRecords = new ArrayList<LogRecord>();
	List<SubscriptRecord> subscriptRecords = new ArrayList<SubscriptRecord>();

	BufferedReader logReader = openForRead(args[0]);
	BufferedReader subscriptReader = openForRead(args[1]);
	BufferedWriter csvWriter = openForWrite(args[2]);
	
	logRecords = loadLog(logReader);
	subscriptRecords = loadSubscript(subscriptReader);
	lookup = createLookup(subscriptRecords);
	mergeAndWrite(logRecords, csvWriter);
	
	
    }
    
    public Map<String, Map<Integer, Map<Integer, String>>> createLookup(List<SubscriptRecord> subscriptRecords) {
	
	Map<String, Map<Integer, Map<Integer, String>>> lookup = new HashMap<String, Map<Integer, Map<Integer, String>>>();
//	String variable;
//	int dimension;
//	String subscript;
//	int index;
	for (SubscriptRecord rec : subscriptRecords) {
	    String array = rec.getVariable();
	    int dimension = rec.getDimension();
	    String subscript = rec.getSubscript();
	    int index = rec.getIndex();
	    if (!lookup.containsKey(array)) {
		lookup.put(array, new HashMap<Integer, Map<Integer, String>>());
	    }
	    Map<Integer, Map<Integer, String>> dimensionMap = lookup.get(array);
	    if (!dimensionMap.containsKey(dimension)) {
		dimensionMap.put(dimension, new HashMap<Integer, String>());
	    }
	    Map<Integer, String> indexMap = dimensionMap.get(dimension);
	    indexMap.put(index, subscript);
	}
	
	return lookup;
    }
    
    public void mergeAndWrite(List<LogRecord> logRecords, BufferedWriter csvWriter) {
	
	Map<String, List<Double>> valueMap = new HashMap<String, List<Double>>();
//	String variable;
//	List<String> subscripts;
//	double time;
//	double value;
	for (LogRecord record : logRecords) {
	    String variable = record.getVariable();
	    List<String> subscripts = record.getSubscripts();
	    double time = record.getTime();
	    double value = record.getValue();
	    
	    String vensimVariable = getVensimVariable(variable, subscripts);
	    if (!valueMap.containsKey(vensimVariable))
		valueMap.put(vensimVariable, new ArrayList<Double>());
	    List<Double> values = valueMap.get(vensimVariable);
	    values.add(value);
	}
	
	try {
	    String columnHeaders = getColumnHeaders(logRecords);
	    int numTimePeriods = columnHeaders.split(",").length - 1;
	    csvWriter.append(columnHeaders);
	    for (String variable : valueMap.keySet()) {
		csvWriter.append(variable);
		List<Double> values = valueMap.get(variable);
		if (values.size() == 1) {
		    Double value = values.get(0);
		    for (int i = 0; i < numTimePeriods; i++)
			csvWriter.append(","+value);
		} else {
		    for (Double value : values) {
			csvWriter.append(","+value);
		    }
		}
		csvWriter.append("\n");
	    }
	    csvWriter.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }
    
    public String getColumnHeaders(List<LogRecord> logRecords) {
	StringBuffer sb = new StringBuffer();
	List<Double> timeValues = new ArrayList<Double>();
	for (LogRecord logRecord : logRecords) {
	    if (!timeValues.contains(logRecord.getTime()))
		timeValues.add(logRecord.getTime());
	}
	
	sb.append("Variable/Time");
	for (Double tv : timeValues) {
	    sb.append(",");
	    sb.append(tv);
	}
	sb.append("\n");
	
	
	return sb.toString();
    }
    
    public String getVensimVariable(String variable, List<String> subscripts) {
	StringBuffer vensimVariable = new StringBuffer();
	if (subscripts.size() == 0)
	    return variable;
	
	vensimVariable.append("\""+variable);
	vensimVariable.append("[");
	int dimension = 0;
	for (String subscript : subscripts) {
	    if (dimension > 0)
		vensimVariable.append(",");
	    vensimVariable.append(getAlphabeticSubscript(variable, dimension, Integer.parseInt(subscript)));
	    
	    dimension++;
	}
	vensimVariable.append("]\"");
	
	
	return vensimVariable.toString();
	
    }
    
    public String getAlphabeticSubscript(String array, int dimension, int index) {
	
	return lookup.get(array).get(dimension).get(index);
	
    }
    
    public List<LogRecord> loadLog(BufferedReader reader) {
	List<LogRecord> al = new ArrayList<LogRecord>();
	String aLine = null;
	try {
	    aLine = reader.readLine();
	    while (aLine != null) {
		al.add(new LogRecord(aLine));
		aLine = reader.readLine();
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}	
	
	return al;
    }
    public List<SubscriptRecord> loadSubscript(BufferedReader reader) {
	List<SubscriptRecord> al = new ArrayList<SubscriptRecord>();
	String aLine = null;
	try {
	    aLine = reader.readLine();
	    aLine = reader.readLine();
	    while (aLine != null) {
		al.add(new SubscriptRecord(aLine));
		aLine = reader.readLine();
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}	
	
	return al;
    }
    
    public static  BufferedWriter openForWrite(String filename) {
	BufferedWriter report = null;

	try {
	    File aFile = new File(filename);

	    report = new BufferedWriter(new OutputStreamWriter(
		    (new FileOutputStream(aFile, false))));
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}     


	return report;
    }
    
    public static BufferedReader openForRead(String filename) {
	
	BufferedReader fileReader = null;

	// open the file for reading
	try {
	    fileReader = new BufferedReader (new FileReader(new File(filename)));
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	return fileReader;
    }

}
