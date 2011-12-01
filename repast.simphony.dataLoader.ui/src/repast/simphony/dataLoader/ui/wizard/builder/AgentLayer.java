package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

public class AgentLayer extends PLayer {

	Color nodeColor;

	Shape nodeShape;

	public static final String AGENT_KEY = "AGENT";
	public AgentLayer(Color nodeColor, Shape nodeShape) {
		this.nodeColor = nodeColor;
		this.nodeShape = nodeShape;
	}

	public PNode addObject(int index, AgentDescriptor desc, Point2D position) {
		PPath path = new PPath(nodeShape);
		path.setPaint(nodeColor);
		path.setStrokePaint(Color.BLACK);
		path.setBounds(0, 0, 10, 10);
		path.setX(position.getX());
		path.setY(position.getY());
		path.addClientProperty(AGENT_KEY, desc);
		this.addChild(path);
		return path;
	}

	public Color getNodeColor() {
		return nodeColor;
	}
}
