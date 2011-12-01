package repast.simphony.freezedry;


public class PrimitiveArrayFreezeDryer extends AbstractFreezeDryer {
	public static final char ELEMENT_DELIMITER = ',';
	
	public static final String ELEMENTS_KEY = "elements";
	
	protected FieldUtilities util = FieldUtilities.INSTANCE;

	public PrimitiveArrayFreezeDryer(FreezeDryedRegistry registry) {
		super(registry);
	}

	@SuppressWarnings("unchecked")
	public Object rehydrate(FreezeDryedObject fdo) throws FreezeDryingException {
		String arrayType = fdo.getType().getName();
		
		return ArrayValueHandler.readArray(arrayType, (String) fdo.get(ELEMENTS_KEY),
				ELEMENT_DELIMITER);
	}
	
	public FreezeDryedObject freezeDry(String id, Object o)
			throws FreezeDryingException {
		FreezeDryedObject fdo = new FreezeDryedObject(id, o.getClass());

		fdo.put("elements", ArrayValueHandler.writeArray(o, ELEMENT_DELIMITER));

		return fdo;
	}

	protected boolean isType(Class clazz, Class type) {
		return clazz.equals(type);
	}
	
	public boolean handles(Class type) {
		return FieldUtilities.INSTANCE.isPrimitiveArray(type);
		// type.getComponentType()
//		return isType(clazz, double[].class) || isType(clazz, int[].class)
//				|| isType(clazz, float[].class) || isType(clazz, short[].class)
//				|| isType(clazz, boolean[].class) || isType(clazz, long[].class)
//				|| isType(clazz, char[].class);
	}
}
