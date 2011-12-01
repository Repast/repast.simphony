package repast.simphony.relogo;

import repast.simphony.space.graph.RepastEdge;

public class TrackingEdge<T> extends RepastEdge<T> {
	
	private double color;

	public double getColor() {
		return color;
	}

	public void setColor(double color) {
		this.color = color;
	}
	
	private int size = 1;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public TrackingEdge(T source, T target, boolean directed) {
		super(source, target, directed);
	}

	public TrackingEdge(T source, T target, boolean directed, double weight) {
		super(source, target, directed, weight);
	}
	
	
}
