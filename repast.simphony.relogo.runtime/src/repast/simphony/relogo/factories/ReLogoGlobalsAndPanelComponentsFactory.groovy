package repast.simphony.relogo.factories

import java.text.NumberFormat;
import repast.simphony.relogo.ReLogoModel
import repast.simphony.relogo.Utility
import repast.simphony.essentials.RepastEssentials
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.IScheduleimport repast.simphony.engine.schedule.IActionimport repast.simphony.engine.schedule.ScheduleParametersimport repast.simphony.context.Context
import repast.simphony.engine.environment.RunEnvironmentimport repast.simphony.engine.environment.RunStateimport java.lang.Iterableimport repast.simphony.query.PropertyEqualsimport repast.simphony.query.Queryimport repast.simphony.relogo.Observerimport javax.swing.JLabel;


import javax.swing.SwingUtilities;


import groovy.swing.SwingBuilder
import java.awt.Componentimport javax.swing.BoxLayoutimport java.awt.Colorimport javax.swing.Boximport java.awt.Dimension
import javax.swing.AbstractButton;
import javax.swing.JPanelimport javax.swing.JButtonimport javax.swing.JToggleButtonimport javax.swing.JSliderimport repast.simphony.relogo.BaseObserverimport repast.simphony.relogo.BaseTurtleimport repast.simphony.relogo.BasePatchimport repast.simphony.relogo.BaseLinkimport javax.swing.event.ChangeListener

import org.apache.commons.lang3.math.NumberUtils;

import simphony.util.messages.MessageCenterimport java.awt.BorderLayoutimport java.awt.FlowLayout
import java.awt.Font;
import java.awt.event.ItemEvent;
/**
 * @author jozik
 *
 */
public class ReLogoGlobalsAndPanelComponentsFactory{
	
	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(ReLogoGlobalsAndPanelComponentsFactory);

	private static HashMap bindingMap = [:];
	
	private static JPanel fixMaximumSize(JPanel panel){
		// Set maximum size based on preferred width or maximum width, whichever is greater
		def defSize = panel.getPreferredSize()
		int maxWidth = (int) defSize.getWidth()
		int maxHeight = (int) defSize.getHeight()
		if (defSize.getHeight() < 32){
			panel.setPreferredSize(new Dimension((int) defSize.getWidth(),32))
			maxHeight = 32
		}
		if (defSize.getWidth() < 200){
			maxWidth = 200
		}
		
		panel.setMaximumSize(new Dimension(maxWidth,maxHeight))
		
		return panel
	}
	
	public static JPanel createButton(String observerID, String methodName, String elementLabel, Object... parameters){
		if (isBatch()) return null;
		
		Closure closure = { event ->    
	    	ReLogoModel model = ReLogoModel.getInstance()
	    	if (!model.isPaused()){
	   			Utility.pauseReLogo()
	   		}
	    	Observer observer = Utility.getObserverByID(observerID);
	    	double dTime = RepastEssentials.GetTickCount()
	   		RepastEssentials.ScheduleAction(observer,dTime+1.0,methodName, parameters)
	   		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule()
	   		def action = [execute: { -> Utility.flushFileSinks(); Utility.checkToPause()}]  as IAction
	   		schedule.schedule(ScheduleParameters.createOneTime(dTime+1.0,ScheduleParameters.LAST_PRIORITY), action)
	   		Utility.resumeReLogo()
		}
		SwingBuilder swing = new SwingBuilder()
		def result = swing.panel(alignmentX: Component.LEFT_ALIGNMENT){
				borderLayout()
				button(text: elementLabel, actionPerformed: closure, icon: new RegularButtonIcon(), font: new Font(Font.DIALOG, Font.PLAIN, 13), alignmentX: Component.LEFT_ALIGNMENT)
			}
		return fixMaximumSize(result)
		
	}
	
	public static JPanel createStateChangeButton(String observerID, String methodName, String elementLabel, Object... parameters){
		if (isBatch()) return null;
		
		Closure closure = { event ->
			
			Observer observer = Utility.getObserverByID(observerID);
			observer.invokeMethod(methodName, parameters)
			ReLogoModel.getInstance().updateDisplay()
		}
		SwingBuilder swing = new SwingBuilder()
		def result = swing.panel(alignmentX: Component.LEFT_ALIGNMENT){
				borderLayout()
				button(text: elementLabel, actionPerformed: closure, icon: new RegularButtonIcon(), font: new Font(Font.DIALOG, Font.PLAIN, 13), alignmentX: Component.LEFT_ALIGNMENT)
			}
		return fixMaximumSize(result)
		
	}
	
	public static JPanel createMonitor(String observerID, String closureName, String elementLabel, double interval){
		if (isBatch()) return null;

		ReLogoModel model = ReLogoModel.getInstance();
		model.monitorsMap[closureName] = ""
		//TODO: possibly change the start time to 0.0
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule()
   		def action = [execute: { -> ReLogoModel.getInstance().setMonitor(closureName, Utility.getObserverByID(observerID)."$closureName"())}]  as IAction
   		schedule.schedule(ScheduleParameters.createRepeating(1.0, interval, ReLogoModel.MONITOR_PRIORITY), action)
		NumberFormat nf = NumberFormat.getNumberInstance();
		SwingBuilder swing = new SwingBuilder()
		Closure converterClosure = {a ->
			if (a != null){
				if (a instanceof Number){
					"${nf.format(a as Number)}"
				}
				else {
					a
				}
			}
			else {
				'N/A'
			}
		}
		JPanel result = swing.panel(alignmentX:Component.LEFT_ALIGNMENT) {
			boxLayout(axis: BoxLayout.PAGE_AXIS)
			panel(alignmentX: Component.LEFT_ALIGNMENT/*,border: lineBorder(color: Color.CYAN)*/){
				boxLayout(axis: BoxLayout.LINE_AXIS)
				label(text: elementLabel)
				hglue()
			}
			panel(alignmentX: Component.LEFT_ALIGNMENT/*, border: lineBorder(color: Color.BLUE)*/){
				borderLayout()
				label(text: bind(source: model.monitorsMap, sourceProperty: closureName, converter: converterClosure))
			}
		}
		return fixMaximumSize(result)
	}
	
	private static boolean isBatch(){
		return RunEnvironment.instance.isBatch();
	}
	
	public static JPanel createToggleButton(String observerID, String methodName, String elementLabel, Object... parameters){
		if (isBatch()) return null;
		
		String actionName = observerID + "_" + methodName
		createModelParam(actionName,false)
		def modelParams = ReLogoModel.getInstance().getModelParams()
		BindableBoolean shouldContinue = new BindableBoolean();
		

//		ReLogoModel tempModel = ReLogoModel.getInstance();
//		IAction tempAction =  tempModel.removeAction(actionName);
//		if (tempAction != null){
//			RunEnvironment.getInstance().getCurrentSchedule().removeAction(tempAction);
//			shouldContinue.booleanValue = false
//		}
		
		Closure closure = {e -> 
	    	ReLogoModel model = ReLogoModel.getInstance()
	    	Observer observer = Utility.getObserverByID(observerID);
	    	double dTime = RepastEssentials.GetTickCount()
	    	ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule()
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				
				model.setModelParam(actionName,false)
				shouldContinue.booleanValue = false
				def pauseFlag = model.isPaused()
				
				if (!pauseFlag){
					Utility.pauseReLogo()
				}
				
			    // new way of doing it
			    def executeClosure = {action -> schedule.removeAction(action)}.curry(model.removeAction(actionName))
			  	def remAction = [execute: executeClosure] as IAction
			  	schedule.schedule(ScheduleParameters.createOneTime(dTime+0.5),remAction)
	
			  	model.decrementActiveButtons()
				Utility.flushFileSinks()
				if (!pauseFlag && model.getActiveButtons() > 0){
			   		Utility.resumeReLogo()
			   	}
			}
			else {
				model.setModelParam(actionName,true)
				if (!model.paused){
			   		Utility.pauseReLogo()
			   	}
				
				shouldContinue.booleanValue = true
				ISchedulableAction newAction = schedule.schedule(ScheduleParameters.createRepeating(dTime+1.0, 1.0), new StopEnabledCallBackAction(shouldContinue,observer,methodName,parameters));
		      	
		      	model.addAction(actionName,newAction)
		      	model.incrementActiveButtons()
		      	
		      	Utility.resumeReLogo() 
			}
		}
		SwingBuilder swing = new SwingBuilder()
		if (bindingMap.get(actionName) != null){
			def oldBinding = bindingMap.remove(actionName)
			oldBinding.unbind()
		}
		JPanel panel = swing.panel(alignmentX:Component.LEFT_ALIGNMENT){
			borderLayout()
			toggleButton(selected: bind(source: modelParams, sourceProperty: actionName, id:'binding'), icon: new ToggleButtonIcon(), text: elementLabel, font: new Font(Font.DIALOG, Font.PLAIN, 13), itemStateChanged: closure, alignmentX: Component.LEFT_ALIGNMENT)
			bean(shouldContinue, booleanValue: bind(target: modelParams, targetProperty: actionName))
		}
		bindingMap.put(actionName, swing.binding)
		return fixMaximumSize(panel)
	}
	
	private static int multiplier(List list){
		List tempList = new ArrayList(list)
		int multiplier = 1
		while(true){
			tempList = tempList*.multiply(multiplier)
			if (tempList.every({ (it as BigDecimal).remainder(1 as BigDecimal) == 0 })){
				break;
			}
			multiplier*=10
		}
		return multiplier
	}
	
	public static JPanel createSlider(String varName, String elementLabel, Number minVal, Number increment, Number maxVal, Number val, String units){
		createModelParam(varName,val)
		if (isBatch()) return null;
		
		int multiplier = multiplier([minVal, increment, maxVal])
		
		msgCenter.debug("creating a slider for the model parameter $varName with value $val")
		def modelParams = ReLogoModel.getInstance().getModelParams()
		SwingBuilder swing = new SwingBuilder()
		def result = swing.panel(alignmentX:Component.LEFT_ALIGNMENT) {
			
        	boxLayout(axis:BoxLayout.PAGE_AXIS)
        	panel(alignmentX: Component.LEFT_ALIGNMENT/*,border: lineBorder(color: Color.CYAN)*/){
        		boxLayout(axis: BoxLayout.LINE_AXIS)
        		label(text: elementLabel)
        		widget(Box.createRigidArea(new Dimension(20,0)))
        		hglue()
        		label(text: bind(source: modelParams, sourceProperty: varName, converter: {"${it as Number} " + (units ? units : '')}))
        	}
        	panel(alignmentX: Component.LEFT_ALIGNMENT /*, border: lineBorder(color: Color.BLUE)*/){
        		def sliderRef = slider(snapToTicks: true, value: bind(source: modelParams, sourceProperty: varName, converter: {it*multiplier as int} ), minimum: (minVal * multiplier) as int , maximum: (maxVal * multiplier) as int, minorTickSpacing: (increment * multiplier) as int, extent: 0)
        		sliderRef.addChangeListener([stateChanged: {e-> modelParams[varName] = (multiplier == 1 ? e.source.value : e.source.value/multiplier); ReLogoModel.getInstance().updateDisplay()}] as ChangeListener)
        		sliderRef.getModel().setValue((val * multiplier) as int)
        	}
        }
		

		return fixMaximumSize(result)
	}
	
	public static JPanel createSwitch(def varName, String elementLabel, boolean selected){
		
		createModelParam(varName, selected)
		
		if (isBatch()) return null;
		msgCenter.debug("creating the model parameter $varName with value ${selected}")
		def modelParams = ReLogoModel.getInstance().getModelParams()
		SwingBuilder swing = new SwingBuilder()
		JPanel result = swing.panel(alignmentX:Component.LEFT_ALIGNMENT) {
			//lineBorder(color: Color.CYAN, parent: true)
			borderLayout()
			checkBox(constraints: BorderLayout.WEST, text: elementLabel, selected: bind(source: modelParams, sourceProperty: varName), itemStateChanged: {e -> modelParams[varName] = e.source.selected; ReLogoModel.getInstance().updateDisplay()})
		}
		return fixMaximumSize(result)
	}
	
	public static JPanel createChooser(String varName, String elementLabel, List choices, int index){
		createModelParam(varName, choices[index])
		
		if (isBatch()) return null;
		
		msgCenter.debug("creating the model parameter $varName with value ${choices[index]}")
		def modelParams = ReLogoModel.getInstance().getModelParams()
		SwingBuilder swing = new SwingBuilder()
		JPanel result = swing.panel(alignmentX:Component.LEFT_ALIGNMENT) {
			boxLayout(axis:BoxLayout.PAGE_AXIS)
			panel(alignmentX: Component.LEFT_ALIGNMENT/*,border: lineBorder(color: Color.CYAN)*/){
        		boxLayout(axis: BoxLayout.LINE_AXIS)
        		label(text: elementLabel)
        		hglue()
        	}
			panel(alignmentX: Component.LEFT_ALIGNMENT/*, border: lineBorder(color: Color.BLUE)*/){
				borderLayout()
				comboBox(items: choices, selectedIndex: index, selectedItem: bind(source: modelParams, sourceProperty: varName), actionPerformed: {e -> modelParams[varName] = e.source.selectedItem; ReLogoModel.getInstance().updateDisplay()})
        	}
			
		}
		
		return fixMaximumSize(result)
	
	}
	
	public static JPanel createInput(String varName, def value){
		createModelParam(varName, value)
		
		if (isBatch()) return null;
		
		msgCenter.debug("creating the model parameter defined by the input $varName with value ${value}")
		def modelParams = ReLogoModel.getInstance().getModelParams()
		SwingBuilder swing = new SwingBuilder()
		JPanel result = swing.panel(alignmentX:Component.LEFT_ALIGNMENT) {
			boxLayout(axis:BoxLayout.PAGE_AXIS)
			panel(alignmentX: Component.LEFT_ALIGNMENT/*,border: lineBorder(color: Color.CYAN)*/){
        		boxLayout(axis: BoxLayout.LINE_AXIS)
        		label(text: varName)
        		hglue()
        	}
			panel(alignmentX: Component.LEFT_ALIGNMENT/*, border: lineBorder(color: Color.BLUE)*/){
				borderLayout()
				textField(columns: 10, text: bind(source: modelParams, sourceProperty: varName), actionPerformed: {e ->
						String t = e.source.text
						if (NumberUtils.isNumber(t)){
						  modelParams[varName] = NumberUtils.createNumber(t)
						}
						else {
						  modelParams[varName] = t
						}
						ReLogoModel.getInstance().updateDisplay()
					})
        	}
		}
		return fixMaximumSize(result)
	}
	
	public static JPanel createTickDisplay(){
		if (isBatch()) return null;
		ReLogoModel model = ReLogoModel.getInstance()
		model.setTicks(0.0d);
		SwingBuilder swing = new SwingBuilder()
		def result = swing.panel(alignmentX:Component.LEFT_ALIGNMENT) {
			boxLayout(axis: BoxLayout.LINE_AXIS)
			label(text: 'ReLogo Ticks')
			hglue()
			label(text: bind(source: model, sourceProperty: 'ticks'))
		}
		return fixMaximumSize(result)
	}
	
	public static void createModelParam(String varName, def defaultValue){
		ReLogoModel.getInstance().setModelParam(varName,defaultValue)
		 /*BaseObserver.metaClass."get${Utility.capitalize(varName)}" = { ->
		 	return (ReLogoModel.getInstance().getModelParam(varName))
		 }
		 BaseObserver.metaClass."set${Utility.capitalize(varName)}" = { def value ->
		 	ReLogoModel.getInstance().setModelParam(varName,value)
		 }
		 BaseTurtle.metaClass."get${Utility.capitalize(varName)}" = { ->
		 	return (ReLogoModel.getInstance().getModelParam(varName))
		 }
		 BaseTurtle.metaClass."set${Utility.capitalize(varName)}" = { def value ->
		 	ReLogoModel.getInstance().setModelParam(varName,value)
		 }
		 BasePatch.metaClass."get${Utility.capitalize(varName)}" = { ->
		 	return (ReLogoModel.getInstance().getModelParam(varName))
		 }
		 BasePatch.metaClass."set${Utility.capitalize(varName)}" = { def value ->
		 	ReLogoModel.getInstance().setModelParam(varName,value)
		 }
		 BaseLink.metaClass."get${Utility.capitalize(varName)}" = { ->
		 	return (ReLogoModel.getInstance().getModelParam(varName))
		 }
		 BaseLink.metaClass."set${Utility.capitalize(varName)}" = { def value ->
		 	ReLogoModel.getInstance().setModelParam(varName,value)
		 }*/
	 }
	
}
