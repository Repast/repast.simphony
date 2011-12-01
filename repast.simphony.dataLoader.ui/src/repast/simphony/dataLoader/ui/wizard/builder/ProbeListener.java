package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import repast.simphony.ui.plugin.editor.DefaultEditorDialog;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

public class ProbeListener extends PBasicInputEventHandler {

	@Override
	public void mouseClicked(PInputEvent ev) {
		Object o = ev.getPickedNode().getClientProperty(AgentLayer.AGENT_KEY);
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
