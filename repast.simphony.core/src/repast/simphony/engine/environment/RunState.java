/*CopyrightHere*/
package repast.simphony.engine.environment;

import repast.simphony.context.Context;
import repast.simphony.random.DefaultRandomRegistry;
import repast.simphony.random.RandomHelper;
import repast.simphony.random.RandomRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that contains information about the current (or upcoming) simulation
 * run.
 */
public class RunState {

	private ScheduleRegistry scheduleRegistry;
	
	private RunInfo runInfo;
	
	private GUIRegistry guiRegistry;
	
	private Context masterContext;

	private Map<Object, Object> registry = new HashMap<Object, Object>();
	
	private ControllerRegistry controllerRegistry;
	
	private RandomRegistry randomRegistry;

	public static Context safeMasterContext = null;
	
	public static void setSafeMasterContext(
			Context safeMasterContext) {
		RunState.safeMasterContext = safeMasterContext;
	}

	/**Map to hold the RunState instances for simulations with multiple threads, including multi-threaded and distributed batch runs*/
	private static Map<Thread,RunState> runStates = new HashMap<Thread,RunState>();
	
	public static RunState staticInstance = null;

	
	public static RunState init() {
		return setStaticInstance(new RunState());
	}
	
	public static RunState initDistributed(){
		RunState s = new RunState();
		runStates.put(Thread.currentThread(), s);
		RandomHelper.init();
		return s;
	}
	
	public static RunState init(RunInfo runInfo, ScheduleRegistry scheduleRegistry, GUIRegistry guiRegistry) {
		return setStaticInstance(new RunState(runInfo, scheduleRegistry, guiRegistry));
	}
	
	
	public static RunState getInstance() {
		if(staticInstance==null)
			return runStates.get(Thread.currentThread());
		return staticInstance;
	}

	/**
	 * @param staticInstance the staticInstance to set
	 */
	private static RunState setStaticInstance(RunState staticInstance) {
		RunState.staticInstance = staticInstance;
		RandomHelper.init();
		return staticInstance;
	}

	/**
	 * Constructs this RunState object with the specified run information,
	 * schedule registry, and logging registry objects.
	 * 
	 * @param runInfo
	 *            information on the current run
	 * @param scheduleRegistry
	 *            a place to store scheduling information
	 */
	RunState(RunInfo runInfo, ScheduleRegistry scheduleRegistry, GUIRegistry guiRegistry) {
		super();

		this.runInfo = runInfo;
		this.scheduleRegistry = scheduleRegistry;
		this.guiRegistry = guiRegistry;
		
		this.randomRegistry = new DefaultRandomRegistry();
	}

	/**
	 * Synonymous with RunState(runInfo, new DefaultScheduleRegistry(), new
	 * DefaultBuildingLoggingRegistry(), new DefaultGUIRegistry()).
	 * 
	 * @see #RunState(RunInfo, ScheduleRegistry, GUIRegistry)
	 * @see DefaultScheduleRegistry
	 * @see DefaultGUIRegistry
	 * 
	 * @param runInfo
	 *            information on the current run
	 */
	RunState(RunInfo runInfo) {
		this(runInfo, new DefaultScheduleRegistry(), new DefaultGUIRegistry());
	}

	/**
	 * Synonymous with RunState(null).
	 * 
	 * @see #RunState(RunInfo)
	 * @see DefaultScheduleRegistry
	 * @see DefaultGUIRegistry
	 *
	 */
	RunState() {
		this(null);
	}

	/**
	 * Retrieves the run info.
	 * 
	 * @return the run info
	 */
	public RunInfo getRunInfo() {
		return runInfo;
	}

	/**
	 * Sets the run info.
	 * 
	 * @param runInfo
	 *            the run info
	 */
	public void setRunInfo(RunInfo runInfo) {
		this.runInfo = runInfo;
	}

	/**
	 * Retrieves the schedule registry.
	 * 
	 * @return the schedule registry
	 */
	public ScheduleRegistry getScheduleRegistry() {
		return scheduleRegistry;
	}

	/**
	 * Sets the schedule registry
	 * 
	 * @param scheduleRegistry
	 *            the schedule registry
	 */
	public void setScheduleRegistry(ScheduleRegistry scheduleRegistry) {
		this.scheduleRegistry = scheduleRegistry;
	}

	/**
	 * Retrieves the GUI registry
	 * 
	 * @return the GUI registry
	 */
	public GUIRegistry getGUIRegistry() {
		return guiRegistry;
	}

	/**
	 * Sets the GUI registry
	 * 
	 * @param guiRegistry
	 *            the gui registry
	 */
	public void setGUIRegistry(GUIRegistry guiRegistry) {
		this.guiRegistry = guiRegistry;
	}

	/**
	 * Adds an item to the general registry.
	 *
	 * @param key the items id
	 * @param value the item itself
	 */
	public void addToRegistry(Object key, Object value) {
		registry.put(key, value);
	}

	/**
	 * Removes the item identified by the specified key from the general registry.
	 *
	 * @param key the items id
	 * @return the removed item.
	 */
	public Object removeFromRegistry(Object key) {
		return registry.remove(key);
	}

	/**
	 * Gets the item identified by the specified key from the general registry.
	 *
	 * @param key the items id
	 * @return the item or null if the item is not found.
	 */
	public Object getFromRegistry(Object key) {
		return registry.get(key);
	}

	/**
	 * Sets the master context for the simulation.
	 * 
	 * @param masterContext
	 *            the simulation's master context
	 */
	public void setMasterContext(Context masterContext) {
		this.masterContext = masterContext;
	}

	/**
	 * Retrieves the master context for the simulation.
	 * 
	 * @return the simulation's master context
	 */
	public Context getMasterContext() {
		return masterContext;
	}
	
	public static Context getSafeMasterContext(){
		if (getInstance().getMasterContext() == null){
			return safeMasterContext;
		}
		else {
			safeMasterContext = null;
			return getInstance().getMasterContext();
		}
	}

	/**
	 * Sets the {@link ControllerRegistry} used to setup and teardown the simulation.
	 * 
	 * @param controllerRegistry
	 *            the run's {@link ControllerRegistry}
	 */
	public void setControllerRegistry(ControllerRegistry controllerRegistry) {
		this.controllerRegistry = controllerRegistry;
	}

	/**
	 * Retrieves the {@link ControllerRegistry} used to setup and teardown the simulation.
	 * 
	 * @return the run's {@link ControllerRegistry}
	 */
	public ControllerRegistry getControllerRegistry() {
		return controllerRegistry;
	}
	
	public RandomRegistry getRandomRegistry() {
		return this.randomRegistry;
	}
	
	public void setRandomRegistry(RandomRegistry registry) {
		this.randomRegistry = registry;
		RandomHelper.init();
	}

	
}
