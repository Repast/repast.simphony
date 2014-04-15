package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.piccolo2d.PCanvas;
import org.piccolo2d.PNode;

public class ContextCreatorCanvas extends PCanvas {
	private Map<Class<?>, AgentLayer> agentLayerMap;

	private Map<NetworkDescriptor, NetworkLayer> networkLayerMap;

	private Color[] colors = { Color.RED, Color.BLUE, Color.BLACK, Color.CYAN,
			Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY,
			Color.MAGENTA, Color.ORANGE, Color.PINK, Color.WHITE, Color.YELLOW };

	private int colorIndex = 0;

	private int dash = 5;

	private Map<AgentDescriptor, PNode> nodeMap = new HashMap<AgentDescriptor, PNode>();

	public ContextCreatorCanvas(ContextDescriptor descriptor) {
		super();
		networkLayerMap = new LinkedHashMap<NetworkDescriptor, NetworkLayer>();
		for (NetworkDescriptor network : descriptor.getNetworkDescriptors()) {
			NetworkLayer layer = new NetworkLayer(new BasicStroke(1, BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND, 10.0f, new float[] { dash, 5 }, 0.0f));
			dash = dash + 5;
			networkLayerMap.put(network, layer);
			getCamera().addLayer(layer);
		}
		agentLayerMap = new LinkedHashMap<Class<?>, AgentLayer>();
		for (Class<?> agentClass : descriptor.getAgentClasses()) {
			AgentLayer layer = new AgentLayer(colors[colorIndex], new Ellipse2D.Float(0, 0, 15, 15));
			agentLayerMap.put(agentClass, layer);
			colorIndex++;
			getCamera().addLayer(layer);
		}

		setSize(500, 500);
	}

	public void addAgentDescriptor(int index, AgentDescriptor desc,
			Point2D position) {
		AgentLayer layer = agentLayerMap.get(desc.getAgentClass());
		PNode node = layer.addObject(index, desc, position);
		layer.repaint();
		nodeMap.put(desc, node);
	}

	public AgentLayer getAgentLayer(Class<?> clazz) {
		return agentLayerMap.get(clazz);
	}

	public void addRelationshipDescriptor(NetworkDescriptor nd,
			RelationshipDescriptor desc) {
		NetworkLayer layer = networkLayerMap.get(nd);
		PNode source = nodeMap.get(desc.getSource());
		source.repaint();
		PNode target = nodeMap.get(desc.getTarget());
		target.repaint();
		layer.addRelationship(source, target, desc.getStrength());
		layer.repaint();
	}
	
	public void removeRelationshipDescriptor(NetworkDescriptor networkDesc,
			AgentDescriptor agent) {
		for (RelationshipDescriptor relationDesc : networkDesc.getRelationships()) {
			if (relationDesc.getSource().equals(agent) || relationDesc.getTarget().equals(agent)) {
				removeRelationshipDescriptor(networkDesc, relationDesc);				
			}
		}
	}
	
	public void removeRelationshipDescriptor(NetworkDescriptor desc,
			RelationshipDescriptor relationship) {
		networkLayerMap.get(desc).removeRelationship(nodeMap.get(relationship.getSource()),
				nodeMap.get(relationship.getTarget()));
	}

	public NetworkLayer getNetworkLayer(NetworkDescriptor descriptor) {
		return networkLayerMap.get(descriptor);
	}

	public void removeAgentDescriptor(AgentDescriptor agentDesc) {
		for (NetworkDescriptor networkDesc : networkLayerMap.keySet()) {
			removeRelationshipDescriptor(networkDesc, agentDesc);
		}
		AgentLayer layer = agentLayerMap.get(agentDesc.getAgentClass());
		PNode node = nodeMap.get(agentDesc);
		layer.removeChild(node);
		nodeMap.remove(agentDesc);
		layer.repaint();
	}
}
