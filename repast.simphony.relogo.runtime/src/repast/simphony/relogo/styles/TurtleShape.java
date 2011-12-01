package repast.simphony.relogo.styles;

import java.util.List;

public class TurtleShape {
	List<TurtleShapeComponent> components;
	boolean rotates;
	
	public TurtleShape(List<TurtleShapeComponent> components, boolean rotates){
		this.components = components;
		this.rotates = rotates;
	}

	/**
	 * @return the components
	 */
	public List<TurtleShapeComponent> getComponents() {
		return components;
	}

	/**
	 * @return the rotates
	 */
	public boolean isRotates() {
		return rotates;
	}

}
