/**
 * 
 */
package repast.simphony.systemdynamics.support;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 
 * 
 * @author bragen
 *
 */
public class VDM extends Data {
    
    private String file;
    private int timePointer = 0;

    /**
     * @param model
     */
    public VDM(SDModel model) {
	super(model);
	// TODO Auto-generated constructor stub
    }
    
    // copy to model data, all values for this time 
    public void advanceTime() {
	
	Data modelData = model.getData();
	
	// scalar data
	
	for (String var : this.data.keySet()) {
	    List<Double> values = this.dataHistory.get(var);
	    if (timePointer >= values.size())
		modelData.setValue(var, values.get(values.size()-1));
	    else
		modelData.setValue(var, values.get(timePointer));
	}
	
	// array data Processing
	
	for (String array : this.arrays.keySet()) {
	    
	    modelData.registerArray(array);
	    
	    Map<String, List<Double>> map = this.arraysHistory.get(array);
	    for (String subscript : map.keySet()) {
		List<Double> values = map.get(subscript);
	   
	    if (timePointer >= values.size())
		modelData.arraySetValue(array, subscript, values.get(values.size()-1));
	    else
		modelData.arraySetValue(array, subscript, values.get(timePointer));
	    }
	}
	
	timePointer++;
    }
    
    public void loadFromFile() {
	loadFromFile(file);
    }
    
    // load the entire data set for all variables and all times
    public void loadFromFile(String file) {
	BufferedReader fileReader = null;

	String aLine;
	// open the file for reading
	try {
	    fileReader = new BufferedReader (new FileReader(new File(file)));
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	
	try {
	   
	    aLine = fileReader.readLine();
	    while (aLine != null) {
		process(aLine);
		aLine = fileReader.readLine();
	    }
	    fileReader.close();


	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    private void process(String aLine) {
	// aLine is a comma separated value file with the first column a quoted variable name
	
	String[] tokens = split(aLine);
	String variable = tokens[0].replace("\"", "");
	if (ArrayReference.isArrayReference(variable)) {
	    ArrayReference ar = new ArrayReference(variable);
	    this.registerArray(ar.getArrayName());
	    for (int i = 1; i < tokens.length; i++) {
		arraySetValue(ar.getArrayName(), ar.getSubscript(), Double.parseDouble(tokens[i]));
	    }
	    
	    // no [] in subscript specification
	} else {
	    for (int i = 1; i < tokens.length; i++) {
		setValue(variable, Double.parseDouble(tokens[i]));
	    }
	}
    }
    
    private String[] split(String line) {
	
	List<String> al = new ArrayList<String>();
	if (line.startsWith("\"")) {
	    String[] x = line.split("\"");
	    al.add(x[1]);
	    for (String s :  x[2].substring(1).split(","))
		al.add(s);
	} else {
	    return line.split(",");
	}
	
	// substring(position.value(), position.value()+1)
	
	return al.toArray(new String[al.size()]);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }


}
