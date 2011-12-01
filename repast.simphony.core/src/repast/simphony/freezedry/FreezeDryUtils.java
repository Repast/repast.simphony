/*CopyrightHere*/
package repast.simphony.freezedry;


public class FreezeDryUtils {

	public static void addComplex(FreezeDryedRegistry registry, FreezeDryedObject fdo, String name, Object value) throws FreezeDryingException {
		fdo.put(getClassColumn(name), value.getClass().getName());
		fdo.put(getIDColumn(name), registry.getId(value));
	}


	public static String getClassColumn(String name) {
		return AbstractDataSource.SPECIAL_COL_MARKER + name + AbstractDataSource.CLASS_MARKER;
	}

	public static String getIDColumn(String name) {
		return AbstractDataSource.SPECIAL_COL_MARKER + name + AbstractDataSource.CHILD_ID_MARKER;
	}
}
