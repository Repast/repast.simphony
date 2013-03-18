package repast.simphony.systemdynamics.translator;


public class ArrayDeclaration {
    
    private String name;
    private String type;
    private int numDimensions;
    private Integer[] dimensionSize;
    
    public ArrayDeclaration(String name, String type, int numDimensions, Integer[] dimensionSize) {
	this.name = name;
	this.type = type;
	this.numDimensions = numDimensions;
	this.dimensionSize = dimensionSize;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getNumDimensions() {
        return numDimensions;
    }

    public Integer[] getDimensionSize() {
        return dimensionSize;
    }

}
