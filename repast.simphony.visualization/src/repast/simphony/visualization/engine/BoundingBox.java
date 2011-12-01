package repast.simphony.visualization.engine;

import java.awt.geom.Rectangle2D;

public class BoundingBox {
	double minX, minY, minZ, maxX, maxY, maxZ;

	public BoundingBox() {

	}

	public BoundingBox(double minX, double minY, double minZ, double width,
			double height, double depth) {
		setFrame(minX, minY, minZ, width, height, depth);
	}

	public double getWidth() {
		return maxX - minX;
	}

	public double getHieght() {
		return maxY - minY;
	}

	public double getDepth() {
		return maxZ - minZ;
	}

	public void setWidth(double width) {
		maxX = minX + width;
	}

	public void setHeight(double height) {
		maxY = minY + height;
	}

	public void setDepth(double depth) {
		maxZ = minZ + depth;
	}

	public void setFrame(double minX, double minY, double minZ, double width,
			double height, double depth) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		maxX = minX + width;
		maxY = minY + height;
		maxZ = minZ + depth;
	}

	public Rectangle2D getRectangle() {
		return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public double getMaxZ() {
		return maxZ;
	}

	public void setMaxZ(double maxZ) {
		this.maxZ = maxZ;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMinZ() {
		return minZ;
	}

	public void setMinZ(double minZ) {
		this.minZ = minZ;
	}

}
