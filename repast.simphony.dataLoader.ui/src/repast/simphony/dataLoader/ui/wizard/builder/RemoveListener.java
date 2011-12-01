package repast.simphony.dataLoader.ui.wizard.builder;

import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

public class RemoveListener extends PBasicInputEventHandler {

	private ContextDescriptor contextDescriptor;
	private ContextCreatorCanvas canvas;
	
	public RemoveListener(ContextDescriptor contextDescriptor, ContextCreatorCanvas canvas) {
		super();
		this.contextDescriptor = contextDescriptor;
		this.canvas = canvas;
	}

	@Override
	public void mouseClicked(PInputEvent ev) {
		Object o = ev.getPickedNode().getClientProperty(AgentLayer.AGENT_KEY);
		if (o != null) {
			canvas.removeAgentDescriptor((AgentDescriptor) o);
			contextDescriptor.removeAgentDescriptor((AgentDescriptor) o);
		}
	}

	public void setCanvas(ContextCreatorCanvas canvas) {
		this.canvas = canvas;
	}

}
