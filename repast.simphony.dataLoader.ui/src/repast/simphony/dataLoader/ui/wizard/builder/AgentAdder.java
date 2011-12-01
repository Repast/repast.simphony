package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import repast.simphony.annotate.AgentAnnot;
import repast.simphony.ui.plugin.editor.DefaultEditorDialog;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

public class AgentAdder extends PBasicInputEventHandler {
	ContextDescriptor descriptor;

	JPopupMenu menu;

	Point2D position;

	ContextCreatorCanvas canvas;

	public AgentAdder(ContextCreatorCanvas canvas, ContextDescriptor descriptor) {
		this.descriptor = descriptor;
		this.canvas = canvas;
		menu = new JPopupMenu();
		for (Class<?> clazz : descriptor.getAgentClasses()) {
			JMenuItem item = getMenuItem(clazz);
			menu.add(item);
		}
	}

	/**
	 * @param clazz
	 * @return
	 */
	private JMenuItem getMenuItem(final Class<?> clazz) {
		JMenuItem item = null;
		if (clazz.isAnnotationPresent(AgentAnnot.class)) {
			AgentAnnot desc = (AgentAnnot) clazz.getAnnotation(AgentAnnot.class);
			item = new JMenuItem(desc.displayName());
		} else {
			item = new JMenuItem(clazz.getName());
		}
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				AgentAdder.this.addAgent(clazz);
			}
		});
		return item;
	}

	private void addAgent(Class<?> clazz) {
		AgentDescriptor ad = new AgentDescriptor(clazz);
		DefaultEditorDialog dialog = new DefaultEditorDialog(new AgentDescriptorFillerPanel(position, descriptor, canvas, 
		        ad));
		dialog.display((JDialog) SwingUtilities.getWindowAncestor(canvas));
	}

	@Override
	public void mouseClicked(PInputEvent ev) {
		position = ev.getPosition();
		menu.show((PCanvas) ev.getComponent(), (int) ev.getCanvasPosition()
				.getX(), (int) ev.getCanvasPosition().getY());
	}

}