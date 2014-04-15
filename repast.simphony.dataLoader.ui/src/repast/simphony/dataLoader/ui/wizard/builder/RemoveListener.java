package repast.simphony.dataLoader.ui.wizard.builder;

import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

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
				
		Object o = ev.getPickedNode().getClientProperties().getAttribute(AgentLayer.AGENT_KEY);
		if (o != null) {
			canvas.removeAgentDescriptor((AgentDescriptor) o);
			contextDescriptor.removeAgentDescriptor((AgentDescriptor) o);
		}
	}

	public void setCanvas(ContextCreatorCanvas canvas) {
		this.canvas = canvas;
	}

}
