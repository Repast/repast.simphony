package repast.simphony.parameter.xml;

import org.xml.sax.Attributes;

import repast.simphony.parameter.*;
import repast.simphony.parameter.xml.AbstractNumberSetterCreator.Type;

import java.util.Map;

/**
 * Creator for numeric stepped parameter setters.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NumberSetterCreator extends AbstractNumberSetterCreator {

	public static final String START = "start";
	public static final String END = "end";
	public static final String STEP = "step";
	public static final String NUMBERTYPE = "number_type";

	private Double start;
	private Double end;
	private Double step;

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
    try {
			String startStr = attributes.getValue(START);
			String endStr = attributes.getValue(END);
			String stepStr = attributes.getValue(STEP);
			initType(startStr, endStr, stepStr);
			if (type == Type.FLOAT || type == Type.LONG) {
				startStr = trim(startStr);
				endStr = trim(endStr);
				stepStr = trim(stepStr);
			}

			start = Double.valueOf(startStr);
			end = Double.valueOf(endStr);
			step = Double.valueOf(stepStr);

//			if (type == Type.DOUBLE && isInt(start) && isInt(end) && isInt(step) &&
//              !(isDouble(startStr) && isDouble(endStr) && isDouble(stepStr))) type = Type.INT;
		} catch (NumberFormatException ex) {
			throw new ParameterFormatException("Invalid format for parameter '" + name + "'", ex);
		}
	}

	private void initType(String start, String end, String step) {
		
		String numberType = attributes.getValue(NUMBERTYPE);
		
		if (numberType.equals("long") || numberType.equals("java.lang.Long")) {
			type = Type.LONG;
		} else if (numberType.equals("int") || numberType.equals("java.lang.Integer")) {
			type = Type.INT;
		} else if (numberType.equals("double") || numberType.equals("java.lang.Double")) {
			type = Type.DOUBLE;
		}else if (numberType.equals("float") || numberType.equals("java.lang.Float")) {
			type = Type.FLOAT;
		}else if (numberType.equals("short") || numberType.equals("java.lang.Short")) {
			type = Type.SHORT;
		}else if (numberType.equals("byte") || numberType.equals("java.lang.Byte")) {
			type = Type.BYTE;
		}
		
//		if (isFloat(start) || isFloat(end) || isFloat(step)) {
//			type = Type.FLOAT;
//		} else if (isLong(start) || isLong(end) || isLong(step)) {
//			type = Type.LONG;
//		} else {
//			type = Type.DOUBLE;
//		}
	}

	/**
	 * Adds the parameter to the specified creator based on the
	 * attributes added in init.
	 *
	 * @param creator
	 */
	public void addParameter(ParametersCreator creator) {
		if (type == Type.DOUBLE) {
			creator.addParameter(name, Double.class, start, false);
		} else if (type == Type.INT) {
			creator.addParameter(name, Integer.class, new Integer(start.intValue()), false);
		} else if (type == Type.LONG) {
			creator.addParameter(name, Long.class, new Long(start.longValue()), false);
		} else if (type == Type.SHORT) {
			creator.addParameter(name, Short.class, new Short(start.shortValue()), false);
		} else if (type == Type.BYTE) {
			creator.addParameter(name, Byte.class, new Byte(start.byteValue()), false);
		}else {
			creator.addParameter(name, Float.class, new Float(start.floatValue()), false);
		}
	}

	/**
	 * Creates a parameter setter from the specified attributes
	 *
	 * @return a parameter setter created from the specified attributes
	 */
	public ParameterSetter createSetter() {
		if (type == Type.DOUBLE) {
				return new DoubleSteppedSetter(name, start, end, step);
		} else if (type == Type.INT) {
			return new IntSteppedSetter(name, start.intValue(), end.intValue(), step.intValue());
		} else if (type == Type.LONG) {
			return new LongSteppedSetter(name, start.longValue(), end.longValue(), step.longValue());
		} else if (type == Type.SHORT) {
			return new ShortSteppedSetter(name, start.shortValue(), end.shortValue(), step.shortValue());
		} else if (type == Type.BYTE) {
			return new ByteSteppedSetter(name, start.byteValue(), end.byteValue(), step.byteValue());
		} else {
			return new FloatSteppedSetter(name, start.floatValue(), end.floatValue(), step.floatValue());
		}
	}

}
