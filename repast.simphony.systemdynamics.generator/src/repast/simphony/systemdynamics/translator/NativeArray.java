package repast.simphony.systemdynamics.translator;

public class NativeArray {
    
    private String name;
    private int numDimensions;
    private String[] subscriptName;
    
    public NativeArray(String name) {
	this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumDimensions() {
        return numDimensions;
    }

    public void setNumDimensions(int numDimensions) {
        this.numDimensions = numDimensions;
        subscriptName = new String[numDimensions];
    }

    public String[] getDimensionNames() {
        return subscriptName;
    }
    
    public void setDimensionName(int index, String name) {
	subscriptName[index] = name;
    }
    
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append(name);
    	sb.append("[");
    	for (int i = 0; i < numDimensions; i++) {
    		if (i > 0)
    			sb.append(",");
    		sb.append(subscriptName[i]);
    	}
    	sb.append("]");
    	
    	return sb.toString();
    }

}
