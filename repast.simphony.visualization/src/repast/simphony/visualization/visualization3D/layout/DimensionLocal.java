package repast.simphony.visualization.visualization3D.layout;

import java.awt.Dimension;

public class DimensionLocal extends Dimension {

	int z;
	
	DimensionLocal(int width, int height, int zValue) {
		this.width=width;
		this.height=height;
		this.z=zValue;
	}
	
	public double getZ(){
		return z;
	}
	
	public void setZ(int z) {
		this.z=z;
	}
}
