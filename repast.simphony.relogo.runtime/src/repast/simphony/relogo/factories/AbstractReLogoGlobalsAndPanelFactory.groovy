package repast.simphony.relogo.factories;

import groovy.lang.Closure;
import groovy.swing.SwingBuilder 

import java.awt.Component 
import java.awt.Dimension 
import java.util.List;

import javax.swing.Box 
import javax.swing.JButton;
import javax.swing.JPanel;

import repast.simphony.engine.environment.RunEnvironment;


@SuppressWarnings("unchecked")
public abstract class AbstractReLogoGlobalsAndPanelFactory {
	
	private JPanel panel;
	private boolean hasChild = false;
	private static final String DEFAULT_OBSERVER_NAME = "default_observer";
	
	public void initialize(JPanel panel){
		this.panel = panel;
	}
	
	private void addToParent(JPanel child){
		if (child != null){
			if(hasChild){
				panel.add(Box.createRigidArea(new Dimension(0, 5)));
			}
			else {
				hasChild = true
			}
			panel.add(child);
		}
	}
	
	public void addPanel(Closure cl){
		if (RunEnvironment.instance.isBatch()) return;
		def swing = new SwingBuilder()
		addToParent(swing.panel(alignmentX:Component.LEFT_ALIGNMENT,cl))
//		panel.add(swing.edt(cl))
	}
	
	public void addReLogoTickCountDisplay(){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createTickDisplay());
	}
	public JPanel reLogoTickCountDisplay(){
		return(ReLogoGlobalsAndPanelComponentsFactory.createTickDisplay());
	}
	
	/**
	 * Adds a button with a label to the user panel.
	 * @param methodName default observer method name
	 * @param elementLabel label
	 * @deprecated use the {@link repast.simphony.relogo.schedule.Setup} annotation on methods instead.
	 */
	@Deprecated
	public void addButtonWL(String methodName, String elementLabel){
		addButtonWL(DEFAULT_OBSERVER_NAME,methodName,elementLabel);
	}
	
	/**
	 * Adds a button with a label to the user panel, specifying an observer.
	 * @param observerID the observer name
	 * @param methodName observer's method name
	 * @param elementLabel label
	 * @deprecated use the {@link repast.simphony.relogo.schedule.Setup} annotation on methods instead.
	 */
	@Deprecated
	public void addButtonWL(String observerID, String methodName, String elementLabel){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createButton(observerID,methodName,elementLabel));
	}
	
	/**
	 * Adds a state change button with a label to the user panel.
	 * State change buttons do not advance the simulation schedule.
	 * @param methodName default observer method name
	 * @param elementLabel label
	 */
   public void addStateChangeButtonWL(String methodName, String elementLabel){
	   addStateChangeButtonWL(DEFAULT_OBSERVER_NAME, methodName, elementLabel);
   }
   
	/**
	* Adds a state change button with a label to the user panel, specifying an observer.
	* State change buttons do not advance the simulation schedule.
	* @param observerID the observer name
	* @param methodName observer's method name
	* @param elementLabel label
	*/
   public void addStateChangeButtonWL(String observerID, String methodName, String elementLabel){
	   addToParent(ReLogoGlobalsAndPanelComponentsFactory.createStateChangeButton(observerID,methodName,elementLabel));
   }
	
	/**
	* Creates a button with a label. To be used with the widget method in Swing Builder.
	* @param methodName default observer method name
	* @param elementLabel label
	* @deprecated use the {@link repast.simphony.relogo.schedule.Setup} annotation on methods instead.
	*/
	@Deprecated
	public JPanel buttonWL(String methodName, String elementLabel){
		return(buttonWL(DEFAULT_OBSERVER_NAME,methodName,elementLabel));
	}
	
	/**
	* Creates a button with a label, specifying an observer. To be used with the widget method in Swing Builder.
	* @param observerID the observer name
	* @param methodName observer's method name
	* @param elementLabel label
	* @deprecated use the {@link repast.simphony.relogo.schedule.Setup} annotation on methods instead.
	*/
	@Deprecated
	public JPanel buttonWL(String observerID, String methodName, String elementLabel){
		return(ReLogoGlobalsAndPanelComponentsFactory.createButton(observerID,methodName,elementLabel));
	}
	
	/**
	* Creates a state change button with a label. To be used with the widget method in Swing Builder.
	* @param methodName default observer method name
	* @param elementLabel label
	*/
	public JPanel stateChangeButtonWL(String methodName, String elementLabel){
		return(stateChangeButtonWL(DEFAULT_OBSERVER_NAME,methodName,elementLabel));
	}
	
	/**
	* Creates a state change button with a label, specifying an observer. To be used with the widget method in Swing Builder.
	* @param observerID the observer name
	* @param methodName observer's method name
	* @param elementLabel label
	*/
	public JPanel stateChangeButtonWL(String observerID, String methodName, String elementLabel){
		return(ReLogoGlobalsAndPanelComponentsFactory.createStateChangeButton(observerID,methodName,elementLabel));
	}
	
	/**
	* Adds a button to the user panel, specifying an observer.
	* @param observerID the observer name
	* @param methodName observer's method name
	* @deprecated use the {@link repast.simphony.relogo.schedule.Setup} annotation on methods instead.  
	*/
	@Deprecated
	public void addButton(String observerID, String methodName){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createButton(observerID,methodName,methodName));
	}
	
	/**
	* Adds a button to the user panel.
	* @param methodName default observer method name
	* @deprecated use the {@link repast.simphony.relogo.schedule.Setup} annotation on methods instead.
	*/
	@Deprecated
	public void addButton(String methodName){
		addButton(DEFAULT_OBSERVER_NAME,methodName);
	}
	
	/**
	* Adds a state change button to the user panel, specifying an observer.
	* @param observerID the observer name
	* @param methodName observer's method name
	*/
	public void addStateChangeButton(String observerID, String methodName){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createStateChangeButton(observerID,methodName,methodName));
	}
	
	/**
	* Adds a state change button to the user panel.
	* @param methodName default observer method name
	*/
	public void addStateChangeButton(String methodName){
		addStateChangeButton(DEFAULT_OBSERVER_NAME,methodName);
	}
	
	/**
	* Creates a button, specifying an observer. To be used with the widget method in Swing Builder.
	* @param observerID the observer name
	* @param methodName observer's method name
	* @deprecated use the {@link repast.simphony.relogo.schedule.Setup} annotation on methods instead.
	*/
	@Deprecated
	public JPanel button(String observerID, String methodName){
		return(ReLogoGlobalsAndPanelComponentsFactory.createButton(observerID,methodName,methodName));
	}
	
	/**
	* Creates a button. To be used with the widget method in Swing Builder.
	* @param methodName default observer method name
	* @deprecated use the {@link repast.simphony.relogo.schedule.Setup} annotation on methods instead.
	*/
	@Deprecated
	public JPanel button(String methodName){
		return(button(DEFAULT_OBSERVER_NAME,methodName));
	}
	
	/**
	* Creates a state change button, specifying an observer. To be used with the widget method in Swing Builder.
	* @param observerID the observer name
	* @param methodName observer's method name
	*/
	public JPanel stateChangeButton(String observerID, String methodName){
		return(ReLogoGlobalsAndPanelComponentsFactory.createStateChangeButton(observerID,methodName,methodName));
	}
	
	/**
	* Creates a state change button. To be used with the widget method in Swing Builder.
	* @param methodName default observer method name
	*/
	public JPanel stateChangeButton(String methodName){
		return(stateChangeButton(DEFAULT_OBSERVER_NAME,methodName));
	}
	
	/**
	* Adds a toggle button to the user panel, specifying an observer.
	* @param observerID the observer name
	* @param methodName observer's method name
	* @deprecated use the {@link repast.simphony.relogo.schedule.Go} annotation on methods instead.
	*/
	@Deprecated
	public void addToggleButton(String observerID, String methodName){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createToggleButton(observerID,methodName,methodName));
	}
	
	/**
	* Adds a toggle button to the user panel.
	* @param methodName default observer method name
	* @deprecated use the {@link repast.simphony.relogo.schedule.Go} annotation on methods instead.
	*/
	@Deprecated
	public void addToggleButton(String methodName){
		addToggleButton(DEFAULT_OBSERVER_NAME,methodName);
	}
	
	/**
	* Creates a toggle button, specifying an observer. To be used with the widget method in Swing Builder.
	* @param observerID the observer name
	* @param methodName observer's method name
	* @deprecated use the {@link repast.simphony.relogo.schedule.Go} annotation on methods instead.
	*/
	@Deprecated
	public JPanel toggleButton(String observerID, String methodName){
		return(ReLogoGlobalsAndPanelComponentsFactory.createToggleButton(observerID,methodName,methodName));
	}
	
	/**
	* Creates a toggle button. To be used with the widget method in Swing Builder.
	* @param methodName default observer method name
	* @deprecated use the {@link repast.simphony.relogo.schedule.Go} annotation on methods instead.
	*/
	@Deprecated
	public JPanel toggleButton(String methodName){
		return(toggleButton(DEFAULT_OBSERVER_NAME,methodName));
	}
	
	/**
	* Adds a toggle button with a label to the user panel, specifying an observer.
	* @param observerID the observer name
	* @param methodName observer's method name
	* @param elementLabel label
	* @deprecated use the {@link repast.simphony.relogo.schedule.Go} annotation on methods instead.
	*/
	@Deprecated
	public void addToggleButtonWL(String observerID, String methodName, String elementLabel){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createToggleButton(observerID,methodName,elementLabel));
	}
	
	/**
	* Adds a toggle button with a label to the user panel.
	* @param methodName default observer method name
	* @param elementLabel label
	* @deprecated use the {@link repast.simphony.relogo.schedule.Go} annotation on methods instead.
	*/
	@Deprecated
	public void addToggleButtonWL(String methodName, String elementLabel){
		addToggleButtonWL(DEFAULT_OBSERVER_NAME,methodName,elementLabel);
	}
	
	/**
	* Creates a toggle button with a label, specifying an observer. To be used with the widget method in Swing Builder.
	* @param observerID the observer name
	* @param methodName observer's method name
	* @param elementLabel label
	* @deprecated use the {@link repast.simphony.relogo.schedule.Go} annotation on methods instead.
	*/
	@Deprecated
	public JPanel toggleButtonWL(String observerID, String methodName, String elementLabel){
		return(ReLogoGlobalsAndPanelComponentsFactory.createToggleButton(observerID,methodName,elementLabel));
	}
	
	/**
	* Creates a toggle button with a label. To be used with the widget method in Swing Builder.
	* @param methodName default observer method name
	* @param elementLabel label
	* @deprecated use the {@link repast.simphony.relogo.schedule.Go} annotation on methods instead.
	*/
	@Deprecated
	public JPanel toggleButtonWL(String methodName, String elementLabel){
		return(toggleButtonWL(DEFAULT_OBSERVER_NAME,methodName,elementLabel));
	}
	
	/**
	 * Adds a slider to the user panel.
	 * @param varName global variable name
	 * @param minVal minimum value
	 * @param increment increment
	 * @param maxVal maximum value
	 * @param val default value
	 */
	public void addSlider(String varName, Number minVal, Number increment, Number maxVal, Number val){
		addSlider(varName,minVal,increment,maxVal,val,null);
	}
	
	/**
	 * Adds a slider to the user panel, including units.
	 * @param varName global variable name
	 * @param minVal minimum value
	 * @param increment increment
	 * @param maxVal maximum value
	 * @param val default value
	 * @param units units label
	 */
	public void addSlider(String varName, Number minVal, Number increment, Number maxVal, Number val, String units){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createSlider(varName,varName,minVal,increment,maxVal,val,units));
	}
	
	/**
	* Creates a slider. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	* @param minVal minimum value
	* @param increment increment
	* @param maxVal maximum value
	* @param val default value
	*/
	public JPanel slider(String varName, Number minVal, Number increment, Number maxVal, Number val){
		return(slider(varName,minVal,increment,maxVal,val,null));
	}
	
	/**
	* Creates a slider, including units. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	* @param minVal minimum value
	* @param increment increment
	* @param maxVal maximum value
	* @param val default value
	* @param units units label
	*/
	public JPanel slider(String varName, Number minVal, Number increment, Number maxVal, Number val, String units){
		return(ReLogoGlobalsAndPanelComponentsFactory.createSlider(varName,varName,minVal,increment,maxVal,val,units));
	}
	
	
	/**
	* Adds a slider to the user panel with a label.
	* @param varName global variable name
	* @param elementLabel label
	* @param minVal minimum value
	* @param increment increment
	* @param maxVal maximum value
	* @param val default value
	*/
	public void addSliderWL(String varName, String elementLabel, Number minVal, Number increment, Number maxVal, Number val){
		addSliderWL(varName, elementLabel, minVal,increment,maxVal,val,null);
	}
	
	/**
	* Adds a slider to the user panel, including units, with a label.
	* @param varName global variable name
	* @param elementLabel label
	* @param minVal minimum value
	* @param increment increment
	* @param maxVal maximum value
	* @param val default value
	* @param units units label
	*/
	public void addSliderWL(String varName, String elementLabel, Number minVal, Number increment, Number maxVal, Number val, String units){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createSlider(varName,elementLabel,minVal,increment,maxVal,val,units));
	}
	
	/**
	* Creates a slider with a label. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	* @param elementLabel label
	* @param minVal minimum value
	* @param increment increment
	* @param maxVal maximum value
	* @param val default value
	*/
	public JPanel sliderWL(String varName, String elementLabel, Number minVal, Number increment, Number maxVal, Number val){
		return(sliderWL(varName, elementLabel, minVal,increment,maxVal,val,null));
	}
	
	/**
	* Creates a slider, including units, with a label. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	* @param elementLabel label
	* @param minVal minimum value
	* @param increment increment
	* @param maxVal maximum value
	* @param val default value
	* @param units units label
	*/
	public JPanel sliderWL(String varName, String elementLabel, Number minVal, Number increment, Number maxVal, Number val, String units){
		return(ReLogoGlobalsAndPanelComponentsFactory.createSlider(varName,elementLabel,minVal,increment,maxVal,val,units));
	}
	

	/**
	 * Adds a chooser to the user panel.
	 * @param varName global variable name
	 * @param choices a list of choices
	 */
	public void addChooser(String varName, List choices){
		if (!choices.isEmpty()){
			addToParent(ReLogoGlobalsAndPanelComponentsFactory.createChooser(varName,varName,choices,0));
		}
		
	}
	
	/**
	* Adds a chooser to the user panel.
	* @param varName global variable name
	* @param choices a list of choices
	* @param index base-0 index of the default choice
	*/
	public void addChooser(String varName, List choices, int index){
			addToParent(ReLogoGlobalsAndPanelComponentsFactory.createChooser(varName,varName, choices, index));
	}
	
	/**
	* Creates a chooser. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	* @param choices a list of choices
	*/
	public JPanel chooser(String varName, List choices){
		if (!choices.isEmpty()){
			return(ReLogoGlobalsAndPanelComponentsFactory.createChooser(varName,varName,choices,0));
		}
		else {
			return new JPanel();
		}
		
	}
	
	/**
	* Creates a chooser. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	* @param choices a list of choices
	* @param index base-0 index of the default choice
	*/
	public JPanel chooser(String varName, List choices, int index){
			return(ReLogoGlobalsAndPanelComponentsFactory.createChooser(varName,varName, choices, index));
	}
	
	/**
	* Adds a chooser to the user panel, with label.
	* @param varName global variable name
	* @param elementLabel label
	* @param choices a list of choices
	*/
	public void addChooserWL(String varName, String elementLabel, List choices){
		if (!choices.isEmpty()){
			addToParent(ReLogoGlobalsAndPanelComponentsFactory.createChooser(varName,elementLabel,choices,0));
		}
		
	}
	
	/**
	* Adds a chooser to the user panel, with label.
	* @param varName global variable name
	* @param elementLabel label
	* @param choices a list of choices
	* @param index base-0 index of the default choice
	*/
	public void addChooserWL(String varName, String elementLabel, List choices, int index){
			addToParent(ReLogoGlobalsAndPanelComponentsFactory.createChooser(varName,elementLabel, choices, index));
	}
	
	/**
	* Creates a chooser with a label. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	* @param elementLabel label
	* @param choices a list of choices
	*/
	public JPanel chooserWL(String varName, String elementLabel, List choices){
		if (!choices.isEmpty()){
			return(ReLogoGlobalsAndPanelComponentsFactory.createChooser(varName,elementLabel,choices,0));
		}
		else {
			return new JPanel();
		}
		
	}
	
	/**
	* Creates a chooser with a label. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	* @param elementLabel label
	* @param choices a list of choices
	* @param index base-0 index of the default choice
	*/
	public JPanel chooserWL(String varName, String elementLabel, List choices, int index){
			return(ReLogoGlobalsAndPanelComponentsFactory.createChooser(varName,elementLabel, choices, index));
	}
	
	/**
	 * Adds a switch to the user panel.
	 * @param varName global variable name
	 */
	public void addSwitch(String varName){
		addSwitch(varName, false);
	}
	
	/**
	 * Adds a switch to the user panel, specifying the default state.
	 * @param varName global variable name
	 * @param selected default state
	 */
	public void addSwitch(String varName, boolean selected){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createSwitch(varName,varName, selected)); 
	}
	
	/**
	* Creates a switch. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	*/
	public JPanel rSwitch(String varName){
		return(rSwitch(varName, false));
	}
	
	/**
	* Creates a switch, specifying the default state. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	* @param selected default state
	*/
	public JPanel rSwitch(String varName, boolean selected){
		return(ReLogoGlobalsAndPanelComponentsFactory.createSwitch(varName,varName, selected)); 
	}
	
	/**
	* Adds a switch to the user panel, with label.
	* @param varName global variable name
	* @param elementLabel label
	*/
	public void addSwitchWL(String varName, String elementLabel){
		addSwitchWL(varName, elementLabel, false);
	}
	
	/**
	* Adds a switch to the user panel, specifying the default state, with label.
	* @param varName global variable name
	* @param elementLabel label
	* @param selected default state
	*/
	public void addSwitchWL(String varName, String elementLabel, boolean selected){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createSwitch(varName,elementLabel, selected)); 
	}
	
	/**
	* Creates a switch with a label. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	* @param elementLabel label
	*/
	public JPanel rSwitchWL(String varName, String elementLabel){
		return(rSwitchWL(varName, elementLabel, false));
	}
	
	/**
	* Creates a switch with a label, specifying the default state. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	* @param elementLabel label
	* @param selected default state
	*/
	public JPanel rSwitchWL(String varName, String elementLabel, boolean selected){
		return(ReLogoGlobalsAndPanelComponentsFactory.createSwitch(varName,elementLabel, selected)); 
	}
	
	/**
	 * Adds an input field with initial value 0 to the user panel.
	 * @param varName global variable name
	 */
	public void addInput(String varName){
		addInput(varName, 0);
	}
	
	/**
	* Adds an input field to the user panel, specifying an initial value.
	* @param varName global variable name
	*/
	public void addInput(String varName, Object value){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createInput(varName, value));
	}
	
	/**
	* Creates an input field with initial value 0. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	*/
	public JPanel input(String varName){
		return(input(varName, 0));
	}
	
	/**
	* Creates an input field, specifying an initial value. To be used with the widget method in Swing Builder.
	* @param varName global variable name
	*/
	public JPanel input(String varName, Object value){
		return(ReLogoGlobalsAndPanelComponentsFactory.createInput(varName, value));
	}
	
	/**
	 * Adds a monitor to the user panel, specifying the observer.
	 * @param observerID the observer name
	 * @param methodName observer's method name
	 * @param interval update interval
	 */
	public void addMonitor(String observerID, String methodName, double interval){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createMonitor(observerID, methodName, methodName,interval));
	}
	
	/**
	* Adds a monitor to the user panel.
	* @param methodName default observer's method name
	* @param interval update interval
	*/
	public void addMonitor(String methodName, double interval){
		addMonitor(DEFAULT_OBSERVER_NAME, methodName, interval);
	}
	
	/**
	* Creates a monitor, specifying the observer. To be used with the widget method in Swing Builder.
	* @param observerID the observer name
	* @param methodName observer's method name
	* @param interval update interval
	*/
	public JPanel monitor(String observerID, String methodName, double interval){
		return(ReLogoGlobalsAndPanelComponentsFactory.createMonitor(observerID, methodName, methodName,interval));
	}
	
	/**
	* Creates a monitor. To be used with the widget method in Swing Builder.
	* @param methodName default observer's method name
	* @param interval update interval
	*/
	public JPanel monitor(String methodName, double interval){
		return(monitor(DEFAULT_OBSERVER_NAME, methodName, interval));
	}
	
	/**
	* Adds a monitor with label to the user panel, specifying the observer.
	* @param observerID the observer name
	* @param methodName observer's method name
	* @param elementLabel label
	* @param interval update interval
	*/
	public void addMonitorWL(String observerID, String methodName, String elementLabel, double interval){
		addToParent(ReLogoGlobalsAndPanelComponentsFactory.createMonitor(observerID, methodName, elementLabel,interval));
	}
	
	/**
	* Adds a monitor with label to the user panel.
	* @param methodName default observer's method name
	* @param elementLabel label
	* @param interval update interval
	*/
	public void addMonitorWL(String methodName, String elementLabel, double interval){
		addMonitorWL(DEFAULT_OBSERVER_NAME, methodName, elementLabel, interval);
	}
	
	/**
	* Creates a monitor with label, specifying the observer. To be used with the widget method in Swing Builder.
	* @param observerID the observer name
	* @param methodName observer's method name
	* @param elementLabel label
	* @param interval update interval
	*/
	public JPanel monitorWL(String observerID, String methodName, String elementLabel, double interval){
		return(ReLogoGlobalsAndPanelComponentsFactory.createMonitor(observerID, methodName, elementLabel,interval));
	}
	
	/**
	* Creates a monitor with label. To be used with the widget method in Swing Builder.
	* @param methodName default observer's method name
	* @param elementLabel label
	* @param interval update interval
	*/
	public JPanel monitorWL(String methodName, String elementLabel, double interval){
		return(monitorWL(DEFAULT_OBSERVER_NAME, methodName, elementLabel, interval));
	}
	
	/**
	 * Creates a global variable with initial value 0.
	 * @param varName global variable name
	 */
	public void addGlobal(String varName){
		addGlobal(varName,0);
	}
	
	/**
	 * Creates a global variable with a default value.
	 * @param varName global variable name
	 * @param value default value
	 */
	public void addGlobal(String varName, Object value){
		ReLogoGlobalsAndPanelComponentsFactory.createModelParam(varName, value);
	}
	
	public abstract void addGlobalsAndPanelComponents();

}
