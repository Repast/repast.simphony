package repast.simphony.relogo.styles;

import java.awt.Color;
import java.awt.Shape;

public class TurtleShapeComponent {
	Shape shape;
	boolean changingColor;
	Color color;
	
	public Shape getShape() {
		return shape;
	}

	public boolean isChangingColor() {
		return changingColor;
	}

	public Color getColor() {
		return color;
	}

	public TurtleShapeComponent(Shape shape, boolean changingColor, Color color){
		this.shape = shape;
		this.changingColor = changingColor;
		this.color = color;
	}
	
}
