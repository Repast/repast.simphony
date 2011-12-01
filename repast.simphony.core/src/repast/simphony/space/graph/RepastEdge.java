package repast.simphony.space.graph;

public class RepastEdge<T> {

	protected T source;
	protected T target;
	protected boolean directed;
	private double weight = 1;
	
	protected RepastEdge(){
	}

	public RepastEdge(T source, T target, boolean directed) {
		this(source, target, directed, 1);
	}

	public RepastEdge(T source, T target, boolean directed, double weight) {
		this.source = source;
		this.target = target;
		this.directed = directed;
		this.weight = weight;
	}

	/**
	 * Gets the weight of this edge.
	 * 
	 * @return the weight of this edge.
	 */
	public double getWeight() {
		return weight;
	}

	protected void setDirected(boolean directed) {
		this.directed = directed;
	}

	/**
	 * Get the source of this edge.
	 * 
	 * @return the source of this edge.
	 */
	public T getSource() {
		return source;
	}

	/**
	 * Gets the target of this edge.
	 * 
	 * @return the target of this edge.
	 */
	public T getTarget() {
		return target;
	}

	/**
	 * @return true if this edge is directed, otherwise false.
	 */
	public boolean isDirected() {
		return directed;
	}


  /**
   * Sets the weight of this edge.
   *
   * @param weight the weight edge
   */
	public void setWeight(double weight) {
		this.weight = weight;
	}
}
