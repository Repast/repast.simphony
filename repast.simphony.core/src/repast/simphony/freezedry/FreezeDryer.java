package repast.simphony.freezedry;


public interface FreezeDryer<T> {

	public static final String CLASS_MARKER = "_CLASS";
	public static final String ID_MARKER = "_ID";

	FreezeDryedObject freezeDry(String id, T o)
			throws FreezeDryingException;

	T rehydrate(FreezeDryedObject fdo) throws FreezeDryingException;
	
	boolean handles(Class<?> clazz);
}
