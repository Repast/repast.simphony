package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Point2D;

import org.piccolo2d.PLayer;
import org.piccolo2d.PNode;
import org.piccolo2d.nodes.PPath;

public class AgentLayer extends PLayer {

	Color nodeColor;

	Shape nodeShape;

	public static final String AGENT_KEY = "AGENT";
	public AgentLayer(Color nodeColor, Shape nodeShape) {
		this.nodeColor = nodeColor;
		this.nodeShape = nodeShape;
	}

	public PNode addObject(int index, AgentDescriptor desc, Point2D position) {
		PPath path = new PPath.Double(nodeShape);
		path.setPaint(nodeColor);
		path.setStrokePaint(Color.BLACK);
		path.setBounds(0, 0, 10, 10);
		path.setX(position.getX());
		path.setY(position.getY());
		path.getClientProperties().addAttribute(AGENT_KEY, desc);
		this.addChild(path);
		return path;
	}

	public Color getNodeColor() {
		return nodeColor;
	}
}
