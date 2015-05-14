/**
 * 
 */
package repast.simphony.statecharts.runtime;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

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

	/**
	 * The WindowRegistry keeps track of open statechart displays and buttons to
	 *   coordinate button label and color highlighting.
	 *
	 */
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
		}

		public StateChartSVGDisplayController getController(StateChart statechart){
			return windowRegistry.get(statechart);
		}

		public Map<StateChart, StateChartSVGDisplayController> getWindowRegistry() {
			return windowRegistry;
		}

		public Map<StateChart, List<StateChartButton>> getButtonRegistry() {
			return buttonRegistry;
		}
	}

	public static class StateChartButton extends JButton implements AgentTableListener{	
		private List<StateChartButton> myList;

		public static String SHOW_LABEL = "Show";
		public static String DISPLAY_LABEL = "Display";
		
		// May not be the actual parent container, but is the Component that is 
		//  returned to the GUI.  Need to track this since the returned Component
		//  can be used in tables, and a name property is needed for sorting and filtering.
		private JPanel parentPanel;
		
		public StateChartButton(){
			super(DISPLAY_LABEL);

			setMaximumSize(new Dimension(100,100));
			setMinimumSize(new Dimension(1,1));
		}

		public void highlight(){
			setBackground(Color.GREEN);
			setText(SHOW_LABEL);
			if (parentPanel != null)
				parentPanel.setName(SHOW_LABEL);  // used for sorting in table
		}

		public void unHighlight(){
			setBackground(null);
			setText(DISPLAY_LABEL);
			if (parentPanel != null)
				parentPanel.setName(DISPLAY_LABEL);  // used for sorting in table
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

		public void setParentPanel(JPanel parentPanel) {
			this.parentPanel = parentPanel;
			parentPanel.setName(getText());
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
			
			// Listens for dock close events
			RSApplication rsApp = RSApplication.getRSApplicationInstance();
			if (rsApp != null) {
				RSGui rsGui = rsApp.getGui();
				if (rsGui != null) {
					rsGui.addViewListener(this);
				}
			}
		}

		@Override
		public String getDisplayName() {
			return name;
		}

		StateChartButton button;

		@Override
		public JComponent getComponent(PresentationModel<Object> model) {
			button = new StateChartButton();

			FormLayout layout = new FormLayout(
					"pref:grow,45dlu,pref:grow",  // columns
					"10dlu"); //rows
			PanelBuilder builder = new PanelBuilder(layout);
			CellConstraints cc = new CellConstraints();

			builder.add(button, cc.xy(2, 1));

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

						WindowRegistry.getInstance().addWindow(statechart, scsdc);
						scsdc.createAndShowDisplay();
					}
				});
			}
			
			JPanel panel = builder.getPanel();
			button.setParentPanel(panel);
			
			// If the statechart window is currently open, highlight button
			// Needs to be called after button.setParentPanel(panel);
			if (WindowRegistry.getInstance().getWindowRegistry().get(statechart) != null){
				button.highlight();
			}
			
			return panel;
		}

		@Override
		/**
		 * Occurs when the statechart Frame is closed
		 */
		public void statechartClosed() {
			WindowRegistry.getInstance().removeWindow(statechart);
		}

		@Override
		/**
		 * Occurs when the probe panel is closed
		 */
		public void dockableClosed(DockableFrameEvent evt) {		
			DockableFrame view = evt.getDockable();
			Object closingObject = view.getClientProperty(ProbeManager.PROBED_OBJ_KEY);
			if (closingObject == obj) {				
				// Remove any buttons from the registry
				List<Component> components = getAllComponents(view.getContentPane());
				for (Component comp : components){			
					if (comp instanceof AgentTableListener){
						((AgentTableListener)comp).tableClosed();
					}
				}
			}
			// Remove this from the RSGUI listeners since it should be disposed
			// Need to invoke later otherwise will cause a concurrent modification
			//   exception when the GUI tries to remove this listener
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					RSApplication rsApp = RSApplication.getRSApplicationInstance();
					if (rsApp != null) {
						RSGui rsGui = rsApp.getGui();
						if (rsGui != null) {
							rsGui.removeViewListener(PPUICreator.this);
						}
					}
				}
			});
		}
	}

	/**
	 * Recursively find all Components in the provided container.
	 * 
	 * @param c the container
	 * @return a list of components in the container
	 */
	public static List<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}
}