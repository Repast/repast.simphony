/**
 * 
 */
package repast.simphony.statecharts.runtime;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import repast.simphony.statecharts.DefaultStateChart;
import repast.simphony.statecharts.StateChart;
import repast.simphony.ui.RSApplication;
import repast.simphony.ui.RSGui;
import repast.simphony.ui.probe.FieldPropertyDescriptor;
import repast.simphony.ui.probe.PPUICreatorFactory;
import repast.simphony.ui.probe.ProbeManager;
import repast.simphony.ui.probe.ProbedPropertyUICreator;
import saf.core.ui.dock.DockableFrame;
import saf.core.ui.event.DockableFrameAdapter;
import saf.core.ui.event.DockableFrameEvent;
import saf.core.ui.event.DockableFrameListener;

import com.jgoodies.binding.PresentationModel;

/**
 * PPUICreatorFactory for creating the UI probe component for a statechart.
 * 
 * @author Nick Collier
 */
public class UICreatorFactory implements PPUICreatorFactory {

	public static Map<StateChart, StateChartSVGDisplayController> windowRegistry;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.ui.probe.PPUICreatorFactory#init(repast.simphony.ui.
	 * RSApplication)
	 */
	@Override
	public void init(RSApplication app) {
		
		windowRegistry = new HashMap<StateChart, StateChartSVGDisplayController>();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.ui.probe.PPUICreatorFactory#createUICreator(java.lang.Object
	 * , repast.simphony.ui.probe.FieldPropertyDescriptor)
	 */
	@Override
	public ProbedPropertyUICreator createUICreator(Object obj, FieldPropertyDescriptor fpd)
			throws IllegalAccessException, IllegalArgumentException {
		return new PPUICreator(obj, (DefaultStateChart<?>) fpd.getField().get(obj),
				fpd.getDisplayName());
	}

	private static class PPUICreator extends DockableFrameAdapter implements ProbedPropertyUICreator,
			StatechartCloseListener, DockableFrameListener {

		private DefaultStateChart<?> statechart;
		private String name;
		private Object obj;
		private StateChartSVGDisplayController scsdc;

		public PPUICreator(Object obj, DefaultStateChart<?> statechart, String name) {
			this.obj = obj;
			this.statechart = statechart;
			this.name = name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see repast.simphony.ui.probe.ProbedPropertyUICreator#getDisplayName()
		 */
		@Override
		public String getDisplayName() {
			return name;
		}

		JButton button;

		@Override
		public JComponent getComponent(PresentationModel<Object> model) {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			
			button = new JButton("Display");
			
			if (statechart == null) {
				button.setEnabled(false);
			} 
			else {
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Object source = e.getSource();
						
						if (source instanceof JButton) {
//							((JButton) source).setEnabled(false);
							
							((JButton) source).setBackground(Color.GREEN);
						}
						
						// Just show the window if it exists already
						scsdc = windowRegistry.get(statechart);
						if (scsdc != null){
							scsdc.focus();
							return;
						}
						
						scsdc = new StateChartSVGDisplayController(obj, statechart);
						scsdc.registerCloseListener(PPUICreator.this);
						RSApplication rsApp = RSApplication.getRSApplicationInstance();
						if (rsApp != null) {
							RSGui rsGui = rsApp.getGui();
							if (rsGui != null) {
								rsGui.addViewListener(PPUICreator.this);
							}
						}
						windowRegistry.put(statechart, scsdc);
						scsdc.createAndShowDisplay();
					}
				});
			}
			panel.add(button);
			return panel;
		}

		@Override
		public void statechartClosed() {
			windowRegistry.remove(statechart);
//			button.setEnabled(true);
			button.setBackground(null);
		}

		@Override
		public void dockableClosing(DockableFrameEvent evt) {
			DockableFrame view = evt.getDockable();
			Object closingObject = view.getClientProperty(ProbeManager.PROBED_OBJ_KEY);
			if (closingObject == obj) {
				if (scsdc != null){
//					windowRegistry.remove(statechart);
					statechartClosed();
					scsdc.closeDisplayWithoutNotification();
				}
			}
		}
	}
}