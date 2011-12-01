package repast.simphony.parameter.xml;

import org.xml.sax.Attributes;

import repast.simphony.parameter.ListTokenizer;
import repast.simphony.parameter.ParameterFormatException;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.parameter.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creator for ListParameterSteppers.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ListSetterCreator extends AbstractParameterSetterCreator {

	// <parameter name="list_val" type="list" value_type="String" values="foo bar baz" />

	public static final String VALUE_TYPE = "value_type";
	public static final String VALUES = "values";

	private Object[] vals;
	private String valueType;
	private Map<String, Class> typeMap = new HashMap<String, Class>();

	public ListSetterCreator() {
		typeMap.put("String", String.class);
		typeMap.put("string", String.class);
		typeMap.put("java.lang.String", String.class);
		
		typeMap.put("int", Integer.class);
		typeMap.put("java.lang.Integer", Integer.class);
		
		typeMap.put("double", Double.class);
		typeMap.put("java.lang.Double", Double.class);
		
		typeMap.put("float", Float.class);
		typeMap.put("java.lang.Float", Float.class);
		
		typeMap.put("long", Long.class);
		typeMap.put("java.lang.Long", Long.class);
		
		typeMap.put("boolean", Boolean.class);
		typeMap.put("java.lang.Boolean", Boolean.class);
		
		typeMap.put("byte", Byte.class);
		typeMap.put("java.lang.Byte", Byte.class);
		
		typeMap.put("short", Short.class);
		typeMap.put("java.lang.Short", Short.class);
	}

	/**
	 * Adds the parameter to the specified creator based on the
	 * attributes added in init.
	 *
	 * @param creator
	 */
	public void addParameter(ParametersCreator creator) {
		creator.addParameter(name, typeMap.get(valueType), vals[0], false);
	}

	/**
	 * Initializes this ParameterSetterCreator with the specified attributes.
	 * Any following calls to addParameter or createSetter will use this
	 * attributes.
	 *
	 * @param attributes
	 */
	@Override
	public void init(Attributes attributes) throws ParameterFormatException {
		super.init(attributes);
    doInit();
  }


	/**
	 * Initializes this ParameterSetterCreator with the specified attributes.
	 * Any following calls to addParameter or createSetter will use this
	 * attributes.
	 *
	 * @param attributes
	 */
	@Override
	public void init(Map<String, String> attributes) throws ParameterFormatException {
		super.init(attributes);
    doInit();
  }

  private void doInit() throws ParameterFormatException {
    valueType = attributes.getValue(VALUE_TYPE);
		String values = attributes.getValue(VALUES);
		
		System.out.println("doInit "+valueType+" "+values);

		if (valueType.equalsIgnoreCase("String")  || valueType.equalsIgnoreCase("java.lang.String") ) {
			 vals = ListTokenizer.parseStringValues(values);
		} else if (valueType.equals("int") || valueType.equalsIgnoreCase("java.lang.Integer")) {
			 vals = ListTokenizer.parseIntValues(values, name);
		} else if (valueType.equals("double") || valueType.equalsIgnoreCase("java.lang.Double")) {
			 vals = ListTokenizer.parseDoubleValues(values, name);
		} else if (valueType.equals("float") || valueType.equalsIgnoreCase("java.lang.Float")) {
			 vals = ListTokenizer.parseFloatValues(values, name);
		} else if (valueType.equals("long") || valueType.equalsIgnoreCase("java.lang.Long")) {
			 vals = ListTokenizer.parseLongValues(values, name);
		} else if (valueType.equals("boolean") || valueType.equalsIgnoreCase("java.lang.Boolean")) {
			 vals = ListTokenizer.parseBooleanValues(values, name);
		} else if (valueType.equals("short") || valueType.equalsIgnoreCase("java.lang.Short")) {
			 vals = ListTokenizer.parseShortValues(values, name);
		} else if (valueType.equals("byte") || valueType.equalsIgnoreCase("java.lang.Byte")) {
			 vals = ListTokenizer.parseByteValues(values, name);
		}else {
			throw new ParameterFormatException("Unsupported list value type '" + valueType + "'");
		}
	}

	/**
	 * Creates a parameter setter from the specified attributes
	 *
	 * @return a parameter setter created from the specified attributes
	 */
	public ParameterSetter createSetter() {
		return createSetter(name, typeMap.get(valueType));
	}

	private <T> ParameterSetter createSetter(String name, Class<T> clazz) {
		return new ListParameterSetter<T>(name, (T[]) vals);
	}

	

	public static void main(String[] args) {
		Pattern p = Pattern.compile("'(?>\\\\.|.)*?'");
		Matcher m1 = p.matcher("'f.k .k\" \"oo' '. bar' 'baz'");
		while (m1.find()) {
			System.out.println(m1.group());
		}
	}

}
