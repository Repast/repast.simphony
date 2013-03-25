package repast.simphony.systemdynamics.translator;

import repast.simphony.systemdynamics.support.MappedSubscriptManager;
import repast.simphony.systemdynamics.support.NamedSubscriptManager;
import repast.simphony.systemdynamics.support.SubscriptManager;


public class InformationManagers {

	private static InformationManagers instance;

	private ArrayManager arrayManager; // >
	private UnitsManager unitsManager; // >
	private FunctionManager functionManager; // >
	private SystemDynamicsObjectManager systemDynamicsObjectManager; // not static
	private MappedSubscriptManager mappedSubscriptManager; // >
	private NamedSubscriptManager namedSubscriptManager; // >
	private NativeDataTypeManager nativeDataTypeManager; // >
	private SubscriptManager subscriptManager; // empty
	private MacroManager macroManager;


	public static InformationManagers getInstance() {
		return instance;
	}
	
	private InformationManagers() {
		arrayManager = new ArrayManager();
		unitsManager = new UnitsManager();
		functionManager = new FunctionManager();
		systemDynamicsObjectManager = new SystemDynamicsObjectManager();
		mappedSubscriptManager = new MappedSubscriptManager();
		namedSubscriptManager = new NamedSubscriptManager();
		nativeDataTypeManager = new NativeDataTypeManager();
		subscriptManager = new SubscriptManager();
		macroManager = new MacroManager();
	}

	public static void init() {
		instance = new InformationManagers();
	}

	public ArrayManager getArrayManager() {
		return arrayManager;
	}

	public UnitsManager getUnitsManager() {
		return unitsManager;
	}

	public FunctionManager getFunctionManager() {
		return functionManager;
	}

	public SystemDynamicsObjectManager getSystemDynamicsObjectManager() {
		return systemDynamicsObjectManager;
	}

	public MappedSubscriptManager getMappedSubscriptManager() {
		return mappedSubscriptManager;
	}

	public NamedSubscriptManager getNamedSubscriptManager() {
		return namedSubscriptManager;
	}

	public NativeDataTypeManager getNativeDataTypeManager() {
		return nativeDataTypeManager;
	}

	public SubscriptManager getSubscriptManager() {
		return subscriptManager;
	}

	public MacroManager getMacroManager() {
		return macroManager;
	}

}
