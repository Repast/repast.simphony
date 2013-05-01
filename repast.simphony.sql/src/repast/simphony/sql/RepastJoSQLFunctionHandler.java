package repast.simphony.sql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RepastJoSQLFunctionHandler {

	public final static String ERROR_MESSAGE = "Field Not Accessible";

	public static List<String> exclusionList = new ArrayList<String>();
	{
		exclusionList.add("toString");
		exclusionList.add("getMetaClass");
		exclusionList.add("hashCode");
		exclusionList.add("getClass");
	}

	public String propertyNames(Object obj) {

		String results = "";

		for (Method method : obj.getClass().getMethods()) {
			String methodName = method.getName();
			if ((method.getReturnType() != Void.TYPE)
					&& (method.getParameterTypes().length == 0)
					&& (!this.isExcluded(methodName))) {
				if (methodName.startsWith("get")) {
					methodName = methodName.substring(3);
				}
				if (results.length() > 0) {
					results = results + "; " + methodName;
				} else {
					results = methodName;
				}
			}

		}

		return results;

	}

	public String propertyValues(Object obj) {

		String results = "";

		for (Method method : obj.getClass().getMethods()) {
			String methodName = method.getName();
			if ((method.getReturnType() != Void.TYPE)
					&& (method.getParameterTypes().length == 0)
					&& (!this.isExcluded(methodName))) {
				try {
					Object methodResult = method.invoke(obj);
					if (methodName.startsWith("get")) {
						methodName = methodName.substring(3);
					}
					if (results.length() > 0) {
						results = results + "; " + methodName + " = "
								+ methodResult;
					} else {
						results = methodName + " = " + methodResult;
					}
				} catch (Exception e) {
					results = results + " is not accessible";
				}
			}

		}

		return results;

	}

	public boolean isExcluded(String fieldName) {

		for (String excludedName : exclusionList) {
			if (excludedName.endsWith("*")) {
				if (fieldName.startsWith(excludedName.replace("*", ""))) {
					return true;
				}
			} else {
				if (fieldName.equals(excludedName)) {
					return true;
				}
			}
		}

		return false;

	}

}
