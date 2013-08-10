package repast.simphony.systemdynamics.translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.systemdynamics.support.MutableBoolean;
import repast.simphony.systemdynamics.support.MutableInteger;

public class FunctionManager {

	private Map<String, FunctionDescription> allFunctions;
	private String functionFile;

	public FunctionManager() {
		allFunctions = new HashMap<String, FunctionDescription>();
	}

//	public void load(String file) {
//		functionFile = file;
//		load();
//	}
	
	 public void load(String file) {
		    try {
		    	
		    	System.out.println("OPENING "+file);
		    	System.out.println("LOCATION: "+new File(file).getAbsolutePath());
		    	
		      load(new FileInputStream(file));
		    } catch (IOException ex) {
		      ex.printStackTrace();
		    }
		  }


	public OperationResult validateFunctionReference(Map<String, Equation> equations, Equation equation, MutableInteger pos, MutableBoolean lhs) {
		OperationResult or = new OperationResult();
		List<String> tokens = equation.getTokens();
		String token = tokens.get(pos.value());
		FunctionDescription fd = getDescription(token);
		if (fd != null)
			return or;


		or.setErrorMessage("Function not registered "+token);

		return or;
	}

	public void load(InputStream in) {
		BufferedReader fileReader = null;

		String aLine;
		// open the file for reading
		
			fileReader = new BufferedReader (new InputStreamReader(in));
		

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

	public boolean isFunction(String function) {

		return allFunctions.containsKey(function.replace("sdFunctions.", "").toUpperCase());
	}

	public FunctionDescription getDescription(String function) {

		if (allFunctions.get(function.replace("sdFunctions.", "").toUpperCase()) == null) {
			printFunctions();
			return null;
		}

		return allFunctions.get(function.replace("sdFunctions.", "").toUpperCase());
	}

	private void printFunctions() {
		List<String> funcs = new ArrayList<String>();
		for (String key : allFunctions.keySet())
			funcs.add(key);
		Collections.sort(funcs);
		for (String func : funcs) 
			System.out.println("Function: "+func);
	}

	public boolean canCheckUnitsConsistency(String eqn) {

		boolean canCheck = true;

		// some functions do not allow unit consistency checks
		// if this equation contains one of them, we'll just skip it.
		for (String functionName : allFunctions.keySet()) {
			FunctionDescription fd = allFunctions.get(functionName);
			if ((fd.getReturnUnits().equals("NA") && eqn.contains(functionName)) ||
					(functionName.equalsIgnoreCase("LOOKUP") && eqn.contains(functionName)))
				return false;
		}

		return canCheck;

	}

	private String clean(String s) {
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
