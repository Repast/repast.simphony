/**
 * 
 */
package repast.simphony.systemdynamics.support;

/**
 * @author bragen
 *
 */
public class TimeSeriesInstance {
    
    private double[] time;
    private double[] values;
    
    
    
    public TimeSeriesInstance(double[] time, double[] values) {
	this.time = time;
	this.values = values;
    }
    
    public double[] valuesForIndex(int index) {
	double[] someValues = new double[time.length];
	int pos = 0;
	int start = index * time.length;
	int end = start + time.length;
	for (int i = start; i < end ; i++)
	    someValues[pos++] = values[i];
	
	return someValues;
    }

    public double[] getTime() {
        return time;
    }

}
