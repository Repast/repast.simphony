package repast.simphony.ws;

import java.awt.Color;

/**
 * Stores display properties like agent shape and color for each agent in a
 *   display server instance.
 */
public class DisplayProperties <T> {

	// TODO - implement a comparable and/or clone function that will take an existing
	//        properties and a new properties and return the difference that will
	//        be used to only send property changes to the display.
	
	public T agent;
	public int id;
	public String shape;						// A well-known text (e.g. circle) representation of the shape
	public String icon;   					// Icon file name
	public Color color = Color.RED; // Fill color
	public Color borderColor = Color.BLACK;
	public String borderStroke;
	public String borderThickness;
	public String label;
	public String fillOpacity;   // handle via Color alpha?
	public double rotation;
	public double size = 1;

	public DisplayProperties(int id, T agent) {
		this.agent = agent;
		this.id = id;
	}
}
