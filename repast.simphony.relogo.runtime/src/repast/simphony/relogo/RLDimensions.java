package repast.simphony.relogo;

public class RLDimensions {

	private int minPxcor;
	private int maxPxcor;
	private int minPycor;
	private int maxPycor;

	public RLDimensions(int minPxcor, int maxPxcor, int minPycor, int maxPycor){
		this.minPxcor = minPxcor;
		this.maxPxcor = maxPxcor;
		this.minPycor = minPycor;
		this.maxPycor = maxPycor;
	}
	
	public int getMinPxcor() {
		return minPxcor;
	}

	public int getMaxPxcor() {
		return maxPxcor;
	}

	public int getMinPycor() {
		return minPycor;
	}

	public int getMaxPycor() {
		return maxPycor;
	}
	
	public int getXdim(){
		return (-minPxcor + maxPxcor + 1);
	}
	
	public int getYdim(){
		return (-minPycor + maxPycor + 1);
	}
	
	public int[] getDims(){
		int[] dims = {getXdim(), getYdim()};
		return dims;
	}
	
	public int[] getOrigin(){
		int[] origin = {-minPxcor, -minPycor};
		return origin;
	}
	
	public double[] getDDims(){
		double[] dDims = {(double) getXdim(),(double)  getYdim()};
		return dDims;
	}
	
	public double[] getDOrigin(){
		int[] origin = getOrigin();
		double[] dOrigin = {((double) origin[0]) + 0.5, ((double) origin[1]) + 0.5};
		return dOrigin;
	}
	
}
