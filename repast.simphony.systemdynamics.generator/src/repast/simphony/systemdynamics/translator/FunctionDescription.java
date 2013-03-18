package repast.simphony.systemdynamics.translator;

public class FunctionDescription {
    
    private String externalName;
    private String internalName;
    private boolean supported = false;
    private String javaObject;
    private boolean requiresName = false;
    private boolean requiresValue = false;
    private boolean requiresTime = false;
    private boolean requiresTimeStep = false;
    private String returnType;
    private String returnUnits;
    private boolean suppliesInitialValue = false;
    private int numArgs;
    private String[] argUnits;
    
    public FunctionDescription(String description) {
	parse(description);
    }
    
    private void parse(String description) {
	String[] fields = description.split(",");
	setExternalName(fields[0]);
	setInternalName(fields[1]);
	setSupported(fields[2].equals("Y"));
	setJavaObject(fields[3]);
	setRequiresName(fields[4].equals("Y"));
	setRequiresValue(fields[5].equals("Y"));
	setRequiresTime(fields[6].equals("Y"));
	setRequiresTimeStep(fields[7].equals("Y"));
	setReturnType(fields[8]);
	setReturnUnits(fields[9]);
	setSuppliesInitialValue(fields[10].equals("Y"));
	setNumArgs(Integer.parseInt(fields[11]));
	argUnits = new String[getNumArgs()];
	int pos = 0;
	for (int i = 12; i < 12 + getNumArgs(); i++) {
	    argUnits[pos++] = fields[i];
	}
    }
    
//    public String getAlternateReturnUnit(int funcReturnIndex) {
//	
//    }
    
    
    public String getExternalName() {
        return externalName;
    }
    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }
    public String getInternalName() {
        return internalName;
    }
    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }
    public boolean isSupported() {
        return supported;
    }
    public void setSupported(boolean supported) {
        this.supported = supported;
    }
    public String getJavaObject() {
        return javaObject;
    }
    public void setJavaObject(String javaObject) {
        this.javaObject = javaObject;
    }
    public boolean isRequiresName() {
        return requiresName;
    }
    public void setRequiresName(boolean requiresName) {
        this.requiresName = requiresName;
    }
    public boolean isRequiresValue() {
        return requiresValue;
    }
    public void setRequiresValue(boolean requiresValue) {
        this.requiresValue = requiresValue;
    }
    public boolean isRequiresTime() {
        return requiresTime;
    }
    public void setRequiresTime(boolean requiresTime) {
        this.requiresTime = requiresTime;
    }
    public boolean isRequiresTimeStep() {
        return requiresTimeStep;
    }
    public void setRequiresTimeStep(boolean requiresTimeStep) {
        this.requiresTimeStep = requiresTimeStep;
    }
    public String getReturnType() {
        return returnType;
    }
    
    public String getAlternateReturnUnits() {
	StringBuffer sb = new StringBuffer();
	int definedReturnIndex = Integer.parseInt(returnUnits.replace("arg", ""))-1;
	for (int i = 0; i < numArgs; i++) {
	    if (i == definedReturnIndex)
		continue;
	    	if (argUnits[i].equals(argUnits[definedReturnIndex])) {
	    	    sb.append(",");
	    	    sb.append("arg"+(i+1));
	    	}
	}
	
	return sb.toString();
    }
    
    public boolean requiresUnitExpression() {
	
	if (returnUnits.contains("/") || returnUnits.contains("*")) {
	    return true;
	} else {
	    return false;
	}
    }
    
    public static String getFunctionReturn(String func) {
	String funcAndReturn = func.replace("sdFunctions.", "");
	if (funcAndReturn.contains("<")) {
	    return funcAndReturn.split("<")[1].split(">")[0];
	} else {
	    return "";
	}
    }
    
    public static int getFunctionReturnIndex(String loadedFunctionName) {
	String funcReturn = getFunctionReturn(loadedFunctionName);
	if (funcReturn.contains("/") || funcReturn.contains("*")) {
	    return -1;
	}
	if (funcReturn.startsWith("arg")) {
	    return Integer.parseInt(funcReturn.replace("arg", ""))-1;
	}
	return -2;
    }
    
    public static String getFunctionName(String loadedFunctionName) {
	String funcAndReturn = loadedFunctionName.replace("sdFunctions.", "");
	if (funcAndReturn.contains("<")) {
	    return funcAndReturn.split("<")[0];
	} else {
	    return funcAndReturn;
	}
    }
    
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    public String getReturnUnits() {
        return returnUnits /* +getAlternateReturnUnits() */;
    }
    public void setReturnUnits(String returnUnits) {
        this.returnUnits = returnUnits;
    }
    public int getNumArgs() {
        return numArgs;
    }
    
    public int getNumArgsAll() {
	int n = numArgs;
	if (isRequiresName()) n++;
	if (isRequiresValue()) n++;
	if (isRequiresTime()) n++;
	if (isRequiresTimeStep()) n++;
        return n;
    }
    
    public void setNumArgs(int numArgs) {
        this.numArgs = numArgs;
    }
    public String[] getArgUnits() {
        return argUnits;
    }
    public void setArgUnits(String[] argUnits) {
        this.argUnits = argUnits;
    }

    public boolean isSuppliesInitialValue() {
        return suppliesInitialValue;
    }

    public void setSuppliesInitialValue(boolean suppliesInitialValue) {
        this.suppliesInitialValue = suppliesInitialValue;
    }
}
