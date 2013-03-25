package repast.simphony.systemdynamics.translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionManager {
    
    private static Map<String, FunctionDescription> allFunctions = new HashMap<String, FunctionDescription>();;
    private static String functionFile;
    
    public static void load(String file) {
	functionFile = file;
	load();
    }
    
    public static OperationResult validateFunctionRerference(String token) {
    	OperationResult or = new OperationResult();
    	FunctionDescription fd = getDescription(token);
    	if (fd != null)
    		return or;
    	
    	
    	or.setErrorMessage("Function not registered "+token);
    	
    	return or;
    }
    
    private static void load() {
	BufferedReader fileReader = null;

	String aLine;
	// open the file for reading
	try {
	    fileReader = new BufferedReader (new FileReader(new File(functionFile)));
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	try {
	    aLine = fileReader.readLine(); // header record
	    aLine = fileReader.readLine();
	    while (aLine != null) {
		
		// force all functions to upper case
		
		FunctionDescription fd = new FunctionDescription(aLine);
		allFunctions.put(fd.getExternalName().toUpperCase(), fd);
		allFunctions.put(clean(fd.getExternalName().toUpperCase()), fd);
		allFunctions.put(fd.getInternalName().toUpperCase(), fd);
		aLine = fileReader.readLine();
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}	
    }
    
    public static boolean isFunction(String function) {
    	
	return allFunctions.containsKey(function.replace("sdFunctions.", "").toUpperCase());
    }
    
    public static FunctionDescription getDescription(String function) {
	
	if (allFunctions.get(function.replace("sdFunctions.", "").toUpperCase()) == null) {
	    printFunctions();
	    return null;
	}
	
	return allFunctions.get(function.replace("sdFunctions.", "").toUpperCase());
    }
    
    private static void printFunctions() {
	List<String> funcs = new ArrayList<String>();
	for (String key : allFunctions.keySet())
	    funcs.add(key);
	Collections.sort(funcs);
	for (String func : funcs) 
	    System.out.println("Function: "+func);
    }
    
    public static boolean canCheckUnitsConsistency(String eqn) {
	
	boolean canCheck = true;
	
	// some functions do not allow unit consistency checks
	// if this equation contains one of them, we'll just skip it.
	for (String functionName : allFunctions.keySet()) {
	    FunctionDescription fd = allFunctions.get(functionName);
	    if (fd.getReturnUnits().equals("NA") && eqn.contains(functionName))
		return false;
	}

	return canCheck;
	
    }
    
	private static String clean(String s) {
		return s.replace(" ", "")
			.replace(":", "_")
			.replace("\t", "")
			.replace("&", "_")
			.replace("-", "_")
			.replace("\"", "")
			.replace("(", "_")
			.replace(")", "_")
			.replace("'", "");
	}

}
