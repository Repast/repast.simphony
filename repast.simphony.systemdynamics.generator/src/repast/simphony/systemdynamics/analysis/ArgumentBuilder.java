package repast.simphony.systemdynamics.analysis;

public class ArgumentBuilder {
    
    public final static int NUM_ITERATIONS = 10;
    
    private double[][] arguments;
    private int numArgs;
    private int indexToVary;

    public ArgumentBuilder() {
    }
    
    public ArgumentBuilder(int numArgs, int indexToVary) {
	this.numArgs = numArgs;
	this.indexToVary = indexToVary;
	arguments = new double[NUM_ITERATIONS][numArgs];
	initializeForIndex(indexToVary);
	print();
    }
    
    public ArgumentBuilder(int numArgs) {
	this.numArgs = numArgs;
	arguments = new double[NUM_ITERATIONS*numArgs][numArgs];
	initializeForAllArrows();
	print();
    }
    
    private void initializeForIndex(int varyingIndex) {
	// this outside loop controls which index receives the iteration values
	
	    for (int i = 0; i < numArgs; i++) {
		for (int j = 0; j < NUM_ITERATIONS; j++) {
		    if (i == varyingIndex) {
			arguments[j][i] = j+1;
		    } else {
			arguments[j][i] = 1.0;
		    }
		}
	    }
    }
    
    private void initializeForAllArrows() {
	// this outside loop controls which index receives the iteration values
	for (int varyingIndex = 0; varyingIndex < numArgs; varyingIndex++) {
	    for (int i = 0; i < numArgs; i++) {
		for (int j = 0; j < NUM_ITERATIONS; j++) {
		    if (i == varyingIndex) {
			arguments[j+varyingIndex*NUM_ITERATIONS][i] = j+1;
		    } else {
			arguments[j+varyingIndex*NUM_ITERATIONS][i] = 1.0;
		    }
		}
	    }
	}
    }
    
    private void print() {
	for (int i = 0; i < arguments.length; i++) {
	    for (int j = 0; j < arguments[i].length; j++) {
		System.out.print(arguments[i][j]+(j+1 == arguments[i].length ? "\n" : "  "));
	    }
	}
    }

    public double[][] getArguments() {
        return arguments;
    }
}
