package repast.simphony.relogo;

import groovy.lang.ExpandoMetaClass;
import groovy.util.ObservableMap;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunListener;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.ui.RSApplication;
import repast.simphony.visualization.IDisplay;
import simphony.util.messages.MessageCenter;

/*
 * Singleton class containing information relevant to the whole simulation.
 * 
 * - Action references and button states.
 * - Simulation pause state.
 * - Parameter getting and setting.
 * - Tick control.
 * - Current file and directory information.
 */
@SuppressWarnings("unchecked")
public class ReLogoModel implements RunListener{
	
	public static final Color DEFAULT_TURTLE_COLOR = Color.white;
	public static final Double MONITOR_PRIORITY = -100.0;
	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(ReLogoModel.class);


	private static ReLogoModel uniqueInstance = new ReLogoModel();

	private ReLogoModel() {}

	public static ReLogoModel getInstance() {
		return uniqueInstance;
	}
	

	private boolean eMCEnabledGlobally = false;

	private boolean isEMCEnabledGlobally() {
		return eMCEnabledGlobally;
	}

	private void setEMCEnabledGlobally(boolean eMcEnabledGlobally) {
		eMCEnabledGlobally = eMcEnabledGlobally;
	}
	
	// Check to see if ExpandoMetaClass.enableGlobally() was called	
	public void checkEMCEnabledGlobally(){
		if (!isEMCEnabledGlobally()){
			ExpandoMetaClass.enableGlobally();
			setEMCEnabledGlobally(true);
		}
	}
	
	/*//TODO: possibly change the synchronizedSets for OTPL class instrumentation to concurrent sets (CopyOnWriteArraySet) or simply to regular HashSets
	//Set of instrumented user turtle classes
	private Set<Class<? extends Turtle>> instrumentedTurtleClasses = Collections.synchronizedSet(new HashSet<Class<? extends Turtle>>());
	
	public boolean isTurtleClassInstrumented(Class<? extends Turtle> clazz){
		synchronized(instrumentedTurtleClasses){
			return instrumentedTurtleClasses.contains(clazz);
		}
	}
	
	public void setTurtleClassInstrumented(Class<? extends Turtle> clazz){
		synchronized(instrumentedTurtleClasses){
			instrumentedTurtleClasses.add(clazz);
		}
	}
	
	//Set of instrumented user observer classes
	private Set<Class<? extends Observer>> instrumentedObserverClasses = Collections.synchronizedSet(new HashSet<Class<? extends Observer>>());
	
	public boolean isObserverClassInstrumented(Class<? extends Observer> clazz){
		synchronized(instrumentedObserverClasses){
			return instrumentedObserverClasses.contains(clazz);
		}
	}
	
	public void setObserverClassInstrumented(Class<? extends Observer> clazz){
		synchronized(instrumentedObserverClasses){
			instrumentedObserverClasses.add(clazz);
		}
	}
	
	//Set of instrumented user patch classes
	private Set<Class<? extends Patch>> instrumentedPatchClasses = Collections.synchronizedSet(new HashSet<Class<? extends Patch>>());
	
	public boolean isPatchClassInstrumented(Class<? extends Patch> clazz){
		synchronized(instrumentedPatchClasses){
			return instrumentedPatchClasses.contains(clazz);
		}
	}
	
	public void setPatchClassInstrumented(Class<? extends Patch> clazz){
		synchronized(instrumentedPatchClasses){
			instrumentedPatchClasses.add(clazz);
		}
	}
	
	//Set of instrumented user Link classes
	private Set<Class<? extends Link>> instrumentedLinkClasses = Collections.synchronizedSet(new HashSet<Class<? extends Link>>());
	
	public boolean isLinkClassInstrumented(Class<? extends Link> clazz){
		synchronized(instrumentedLinkClasses){
			return instrumentedLinkClasses.contains(clazz);
		}
	}
	
	public void setLinkClassInstrumented(Class<? extends Link> clazz){
		synchronized(instrumentedLinkClasses){
				instrumentedLinkClasses.add(clazz);
		}
	}*/
	
	Map<String, IAction> actions = new ConcurrentHashMap();
	
	public Map getActions() {
		return actions;
	}

	public IAction getAction(String actionName){
		return actions.get(actionName);
	}
	
	public void addAction(String actionName, IAction action){
		actions.put(actionName, action);
	}
	
	public IAction removeAction(String actionName){
		return actions.remove(actionName);
	}
	
//	private ISchedulableAction goAction;
//	
//	public ISchedulableAction getGoAction() {
//		return goAction;
//	}
//
//	public void setGoAction(ISchedulableAction goAction) {
//		this.goAction = goAction;
//	}

	private boolean paused = true;
	
	private int activeButtons;
	
	public int getActiveButtons() {
		return activeButtons;
	}

	public void setActiveButtons(int activeButtons) {
		this.activeButtons = activeButtons;
	}
	
	public void incrementActiveButtons(){
		this.activeButtons++;
	}
	
	public void decrementActiveButtons(){
		this.activeButtons--;
	}

	public boolean isPaused() {
		return paused;
	}

	private void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	private double ticks = 0.0;
	
	PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public void setTicks(double ticks) {
        pcs.firePropertyChange("ticks", this.ticks, this.ticks = ticks);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    
	public double getTicks() {
		return ticks;
	}

//	public void setTicks(double ticks) {
//		this.ticks = ticks;
//	}

//	Map testMap = new ObservableMap();
//	Map modelParams = Collections.synchronizedMap(new ObservableMap());
	
	//
	Map monitorsMap = new ObservableMap(new ConcurrentHashMap());
	
	public Object getMonitor(String paramName){
		return monitorsMap.get(paramName);
	}
	
	public void setMonitor(String paramName, Object value){
		if (value != null){
			monitorsMap.put(paramName, value);
		}
	}

	Map modelParams = new ObservableMap(new ConcurrentHashMap());
	
	public Map getModelParams() {
		return modelParams;
	}

	public Object getModelParam(String paramName){
		return modelParams.get(paramName);
	}
	
	public void setModelParam(String paramName, Object value){
		if (value != null){
			modelParams.put(paramName, value);
		}
	}
	
	
	
/*	public int getBirthThreshold(){
		return (Integer)getParamValue("birthThreshold");
	}
	
	public double getGrassEnergy(){
		return (Double)getParamValue("grassEnergy");
	}
	public int getGrassGrowRate(){
		return (Integer)getParamValue("grassGrowRate");
	}

	public int getNumber(){
		return (Integer)getParamValue("number");
	}
	
	public Object getParamValue(String paramName){
		Parameters p = RunEnvironment.getInstance().getParameters();
		Object result =  p.getValue(paramName);
		return result;
	}
	
	public double getWeedEnergy(){
	 	 return (Double)getParamValue("weedEnergy");
	}

	public int getWeedsGrowRate(){
		return (Integer)getParamValue("weedsGrowRate");
	}
	*/
	
	
	private File currentDirectory;
	
	private FileInfo currentFileInfo;
	
	private ArrayList<FileInfo> fileInfoList = new ArrayList<FileInfo>();
	
	private long lastTimer;
	
	public long getLastTimer() {
		return lastTimer;
	}

	public void setLastTimer(long lastTimer) {
		this.lastTimer = lastTimer;
	}	
	
	public void resetInstance(){
		currentDirectory = null;
		currentFileInfo = null;
		fileInfoList = new ArrayList<FileInfo>();
	}
	
	public File getCurrentDirectory() {
		return currentDirectory;
	}
	public FileInfo getCurrentFileInfo(){
		return this.currentFileInfo;
	}
	
	public ArrayList<FileInfo> getFileInfoList() {
		return fileInfoList;
	}
	
	public void setCurrentDirectory(File currentDirectory) {
		this.currentDirectory = currentDirectory;
	}
	
	public void setCurrentFileInfo(FileInfo currentFileInfo) {
		this.currentFileInfo = currentFileInfo;
	}
	
	public void setFileInfoList(ArrayList<FileInfo> fileInfoList) {
		this.fileInfoList = fileInfoList;
	}
	
	private IDisplay defaultDisplay;

	public IDisplay getDefaultDisplay() {
		return defaultDisplay;
	}

	public void setDefaultDisplay(IDisplay defaultDisplay) {
		this.defaultDisplay = defaultDisplay;
	}
	
	public void updateDisplay(){
		if (defaultDisplay != null){
			if (isPaused()){
				defaultDisplay.update();
				defaultDisplay.render();
			}
		}
	}

	@Override
	public void stopped() {
		setPaused(true);
		setTicks(0);
		// Remove user panel if stopped
		if (!RunEnvironment.getInstance().isBatch()){
			RSApplication rsApp = RSApplication.getRSApplicationInstance();
			if (rsApp != null){
				if(rsApp.hasCustomUserPanelDefined()){
					rsApp.removeCustomUserPanel();
				}
			}
		}
		
	}

	@Override
	public void paused() {
		setPaused(true);
		
	}

	@Override
	public void started() {
		setPaused(false);
		
	}

	@Override
	public void restarted() {
		setPaused(false);
		
	}
	

}
