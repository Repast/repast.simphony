/**
 * 
 */
package repast.simphony.statecharts.runtime;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import repast.simphony.ui.table.AgentTableListener;
import saf.core.ui.dock.DockableFrame;
import saf.core.ui.event.DockableFrameAdapter;
import saf.core.ui.event.DockableFrameEvent;
import saf.core.ui.event.DockableFrameListener;

import com.jgoodies.binding.PresentationModel;

/**
 * PPUICreatorFactory for creating the UI probe component for a statechart.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class UICreatorFactory implements PPUICreatorFactory {
	
	@Override
	public void init(RSApplication app) {		
		new WindowRegistry();
	}

	@Override
	public ProbedPropertyUICreator createUICreator(Object obj, FieldPropertyDescriptor fpd)
			throws IllegalAccessException, IllegalArgumentException {
		return new PPUICreator(obj, (DefaultStateChart<?>) fpd.getField().get(obj),
				fpd.getDisplayName());
	}

	public static class WindowRegistry{
		protected Map<StateChart, StateChartSVGDisplayController> windowRegistry;
		protected Map<StateChart, List<StateChartButton>> buttonRegistry;
		
		protected static WindowRegistry instance;
	
		public static WindowRegistry getInstance(){
			return instance;
		}
	
		public WindowRegistry(){
			instance = this;
			
			windowRegistry = new HashMap<StateChart, StateChartSVGDisplayController>();
			buttonRegistry = new HashMap<StateChart, List<StateChartButton>>();
		}
		
		public void addWindow(StateChart statechart, 
				StateChartSVGDisplayController controller){
			
			windowRegistry.put(statechart, controller);
			
			// Highlight any existing buttons
			List<StateChartButton> buttons = buttonRegistry.get(statechart);
			
			if (buttons != null){
				for (StateChartButton button : buttons){
					button.highlight();
				}
			}
		}
		
		public void removeWindow(StateChart statechart){
			windowRegistry.remove(statechart);
			List<StateChartButton> buttons = buttonRegistry.get(statechart);
			
			if (buttons != null){
				for (StateChartButton button : buttons){
					button.unHighlight();
				}
			}
		}
		
		public void addButton(StateChart statechart, StateChartButton button){
			List<StateChartButton> buttons = buttonRegistry.get(statechart);
			
			if (buttons == null){
				buttons = new ArrayList<StateChartButton>();
				buttonRegistry.put(statechart, buttons);
			}
			buttons.add(button);
			button.registerList(buttons);
			
			// If the window is currently open, highlight button
			if (windowRegistry.get(statechart) != null){
				button.highlight();
			}
		}
		
		public StateChartSVGDisplayController getController(StateChart statechart){
			return windowRegistry.get(statechart);
		}
	}
		
	public static class StateChartButton extends JButton implements AgentTableListener{	
		private List<StateChartButton> myList;
		
		public StateChartButton(String label){
			super(label);
		}
		
		public void highlight(){
			this.setBackground(Color.GREEN);
			this.setText("Show");
		}
		
		public void unHighlight(){
			this.setBackground(null);
			this.setText("Display");
		}
		
		public void registerList(List<StateChartButton> list){
			myList = list;
		}
		
		@Override
		public void tableClosed() {
			// When the table is closed (destroyed) remove this button from the registry.
			if (myList != null)
				myList.remove(this);
		}
	}
	
	private static class PPUICreator extends DockableFrameAdapter implements ProbedPropertyUICreator,
			StatechartCloseListener, DockableFrameListener{

		private DefaultStateChart<?> statechart;
		private String name;
		private Object obj;
		private StateChartSVGDisplayController scsdc;

		public PPUICreator(Object obj, DefaultStateChart<?> statechart, String name) {
			this.obj = obj;
			this.statechart = statechart;
			this.name = name;
		}

		@Override
		public String getDisplayName() {
			return name;
		}

		StateChartButton button;

		@Override
		public JComponent getComponent(PresentationModel<Object> model) {
			button = new StateChartButton("Display");
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			panel.add(button);
			
			if (statechart == null) {
				button.setEnabled(false);
			} 
			else {
				WindowRegistry.getInstance().addButton(statechart, button);
				
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						// Just show the window if it exists already
						scsdc = WindowRegistry.getInstance().getController(statechart);
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
						WindowRegistry.getInstance().addWindow(statechart, scsdc);
						scsdc.createAndShowDisplay();
					}
				});
			}
			return panel;
		}

		@Override
		public void statechartClosed() {
			WindowRegistry.getInstance().removeWindow(statechart);
		}

		@Override
		public void dockableClosed(DockableFrameEvent evt) {
			DockableFrame view = evt.getDockable();
			Object closingObject = view.getClientProperty(ProbeManager.PROBED_OBJ_KEY);
			if (closingObject == obj) {
			
				System.out.println("Probe dockable closed.");

				for (Component comp : view.getContentPane().getComponents()){
					if (comp instanceof AgentTableListener){
						((AgentTableListener)comp).tableClosed();

						// TODO figure out why this doesnt work 
						
						System.out.println("\t " + comp.getClass().getName());
					}
				}
			}
		}
	}
}