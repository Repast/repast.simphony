package repast.simphony.relogo.factories;

import repast.simphony.space.continuous.PointTranslator;
import repast.simphony.space.grid.GridPointTranslator;

public class RLWorldDimensions {
	private int minPxcor;
	private int maxPxcor;
	private int minPycor;
	private int maxPycor;
	private GridPointTranslator pgt;
	private PointTranslator pt;

	public GridPointTranslator getPgt() {
		return pgt;
	}

	public PointTranslator getPt() {
		return pt;
	}

	public RLWorldDimensions(int minPxcor, int maxPxcor, int minPycor, int maxPycor){
		this(minPxcor, maxPxcor, minPycor, maxPycor, new repast.simphony.space.continuous.WrapAroundBorders());
	}
	
	public RLWorldDimensions(int minPxcor, int maxPxcor, int minPycor, int maxPycor, PointTranslator pt){
		this.minPxcor = minPxcor;
		this.maxPxcor = maxPxcor;
		this.minPycor = minPycor;
		this.maxPycor = maxPycor;
		this.pt = pt;
		if (pt.getClass() == repast.simphony.space.continuous.WrapAroundBorders.class){
			this.pgt = new repast.simphony.space.grid.WrapAroundBorders();
		}
		else if (pt.getClass() == repast.simphony.space.continuous.BouncyBorders.class){
			this.pgt = new repast.simphony.space.grid.BouncyBorders();
		}
		else if (pt.getClass() == repast.simphony.space.continuous.StickyBorders.class){
			this.pgt = new repast.simphony.space.grid.StickyBorders();
		}
		else {
			throw new RuntimeException("The point translator "+ pt + " is not currently supported.");
		}
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
