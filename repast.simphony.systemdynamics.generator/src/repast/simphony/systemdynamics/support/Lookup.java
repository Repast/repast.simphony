package repast.simphony.systemdynamics.support;

public class Lookup {
	
	private String name;
	private double[] xVals;
	private double[] yVals;
	
	private double minX, minXY, maxX, maxXY;
	
	public Lookup(String name, double minX, double minXY, double maxX, double maxXY,  double...ds ) {
		this(name, ds);
		this.minX = minX;
		this.minXY = minXY;
		this.maxX = maxX;
		this.maxXY = maxXY;
	}
	
	public Lookup(String name, double...ds ) {
		this.name = name;
		
		int numVals = ds.length / 2;
		
		xVals = new double[numVals];
		yVals = new double[numVals];
		
		for (int i = 0; i < numVals; i++) {
			xVals[i] = ds[i];
			yVals[i] = ds[i+numVals];
		}
	}
	
	public Lookup(String name, double[] xvals, double[] yvals ) {
		this.name = name;
		
		
		xVals = new double[xvals.length];
		yVals = new double[xvals.length];
		
		for (int i = 0; i < xvals.length; i++) {
			xVals[i] = xvals[i];
			yVals[i] = yvals[i];
		}
	}
	
	public Double getValue(double x) {
		
		Double yVal = null;
		// out of range
		if (x < xVals[0]) {
			System.out.println("WARNING: Below Out of range access to lookup: "+name+" "+x);
			return yVals[0]; 
		}
		
		if (x > xVals[xVals.length-1]) {
			System.out.println("WARNING: Above Out of range access to lookup: "+name+" "+x);
			return yVals[yVals.length-1]; 
		}
		
		int pos = 0;
		for (int i = 0; i < xVals.length; i++) {
			if (xVals[i] == x) {
				return yVals[i];
			} else if (x < xVals[i]){
				pos = i-1;
				break;
			}
		}

		if (xVals[pos] == xVals[pos+1])
			return null;
		 yVal =  yVals[pos]*(x - xVals[pos+1])/(xVals[pos] - xVals[pos+1]) + yVals[pos+1]*(x - xVals[pos])/(xVals[pos+1] - xVals[pos]);
		 
		return yVal;
	}

	public double[][] getAsArray() {
	    double[][] look = new double[2][];
	    look[0] = xVals;
	    look[1] = yVals;
	    
	    return look;
	}
}
