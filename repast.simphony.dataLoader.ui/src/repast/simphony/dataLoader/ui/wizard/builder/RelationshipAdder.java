package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.piccolo2d.PCamera;
import org.piccolo2d.PLayer;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PDragSequenceEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.nodes.PPath;

import repast.simphony.ui.plugin.editor.DefaultEditorDialog;

public class RelationshipAdder extends PDragSequenceEventHandler {
	PPath linePath;

	Line2D line;

	Point2D start;

	ContextCreatorCanvas canvas;

	ContextDescriptor descriptor;

	JPopupMenu menu;

	RelationshipDescriptor rd;

	AgentDescriptor sourceDesc;

	AgentDescriptor targetDesc;

	public RelationshipAdder(ContextCreatorCanvas canvas,
			ContextDescriptor descriptor) {
		linePath = new PPath.Double();
		line = new Line2D.Float();
		this.canvas = canvas;
		this.descriptor = descriptor;
		menu = new JPopupMenu();
		for (NetworkDescriptor networkDescriptor : descriptor
				.getNetworkDescriptors()) {
			menu.add(getMenuItem(networkDescriptor));
		}
	}

	private JMenuItem getMenuItem(final NetworkDescriptor descriptor) {
		JMenuItem item = new JMenuItem(descriptor.getName());
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				rd = new RelationshipDescriptor(sourceDesc, targetDesc, 0.0);
				DefaultEditorDialog dialog = new DefaultEditorDialog(
						new RelationshipDescriptorFillerPanel(canvas, rd,
								descriptor));
				dialog.display((JDialog) SwingUtilities
						.getWindowAncestor(canvas));
				canvas.getLayer().removeChild(linePath);
			}
		});
		return item;
	}

	@Override
	public void mouseDragged(PInputEvent arg0) {
		super.drag(arg0);
		if (isDragging()) {
			line.setLine(start, arg0.getPosition());
			linePath.reset();
			linePath.append(line,false);
		}
	}

	@Override
	public void mouseReleased(PInputEvent arg0) {
		PNode picked = arg0.getInputManager().getMouseOver().getPickedNode();
		if (picked.getClientProperties().getAttribute(AgentLayer.AGENT_KEY) == null) {
			if (sourceDesc != null)
				canvas.getLayer().removeChild(linePath);
			return;
		}
		if (isDragging()) {
			menu.show(canvas, (int) arg0.getCanvasPosition().getX(), (int) arg0
					.getCanvasPosition().getY());
		}
		this.setIsDragging(false);
		targetDesc = (AgentDescriptor) arg0.getInputManager().getMouseOver()
				.getPickedNode().getClientProperties().getAttribute(AgentLayer.AGENT_KEY);

	}

	@Override
	public void mousePressed(PInputEvent arg0) {
		sourceDesc = null;
		targetDesc = null;
		if (arg0.getPickedNode() instanceof PLayer
				|| arg0.getPickedNode() instanceof PCamera) {
			setIsDragging(false);
			return;
		}
		sourceDesc = (AgentDescriptor) arg0.getPickedNode().getClientProperties().getAttribute(
				AgentLayer.AGENT_KEY);
		setIsDragging(true);
		start = arg0.getPosition();

		line.setLine(start, start);
		linePath.reset();
		linePath.append(line, false);
		canvas.getLayer().addChild(linePath);
	}

}
