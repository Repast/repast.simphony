package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.piccolo2d.PCanvas;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

import repast.simphony.ui.plugin.editor.DefaultEditorDialog;

public class ProbeListener extends PBasicInputEventHandler {

	@Override
	public void mouseClicked(PInputEvent ev) {
		Object o = ev.getPickedNode().getClientProperties().getAttribute(AgentLayer.AGENT_KEY);
		if (o != null) {
			AgentDescriptorFillerPanel fillerPanel = new AgentDescriptorFillerPanel(null, null, null,
					(AgentDescriptor) o);
			DefaultEditorDialog dialog = new DefaultEditorDialog(
					fillerPanel);
			Window window = SwingUtilities.getWindowAncestor((PCanvas)ev.getComponent());
			
			if (window instanceof JFrame) {
				dialog.display((JFrame) window);
			} else {
				dialog.display((JDialog) window);
			}			
		}
	}

}
