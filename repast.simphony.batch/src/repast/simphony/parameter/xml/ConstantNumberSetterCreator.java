package repast.simphony.parameter.xml;

import org.xml.sax.Attributes;

import repast.simphony.parameter.ConstantSetter;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.parameter.ParameterFormatException;

import java.util.Map;

/**
 * Creator for numeric constant setters.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ConstantNumberSetterCreator extends AbstractNumberSetterCreator {

	private Double val;
	private String javaClass;
	
	public ConstantNumberSetterCreator(String javaClass) {
		this.javaClass = javaClass;
		initType();
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
    try {
			String valStr = attributes.getValue(SetterConstants.CONSTANT_VALUE);
//			initType(valStr);
			if (type == ConstantNumberSetterCreator.Type.FLOAT || type == ConstantNumberSetterCreator.Type.LONG) {
				valStr = trim(valStr);
			}

			val = Double.valueOf(valStr);
//      if (type == Type.DOUBLE && isInt(val) && !isDouble(valStr)) type = ConstantNumberSetterCreator.Type.INT;
		} catch (NumberFormatException ex) {
			throw new ParameterFormatException("Invalid format for parameter '" + name + "'", ex);
		}
	}

	private void initType(String val) {
		if (isFloat(val)) {
			type = Type.FLOAT;
		} else if (isLong(val)) {
			type = Type.LONG;
		} else {
			type = Type.DOUBLE;
		}
	}

	/**
	 * Adds the parameter to the specified creator based on the
	 * attributes added in init.
	 *
	 * @param creator
	 */
	public void addParameter(ParametersCreator creator) {
		if (type == Type.DOUBLE) {
			creator.addParameter(name, Double.class, val, false);
		} else if (type == Type.INT) {
			creator.addParameter(name, Integer.class, new Integer(val.intValue()), false);
		} else if (type == Type.LONG) {
			creator.addParameter(name, Long.class, new Long(val.longValue()), false);
		} else if (type == Type.SHORT) {
			creator.addParameter(name, Short.class, new Short(val.shortValue()), false);
		} else if (type == Type.BYTE) {
			creator.addParameter(name, Byte.class, new Byte(val.byteValue()), false);
		} else {
			creator.addParameter(name, Float.class, new Float(val.floatValue()), false);
		}
	}

	/**
	 * Creates a parameter setter from the specified attributes
	 *
	 * @return a parameter setter created from the specified attributes
	 */
	public ParameterSetter createSetter() {
		if (type == ConstantNumberSetterCreator.Type.DOUBLE) {
				return new ConstantSetter<Double>(name, val);
		} else if (type == ConstantNumberSetterCreator.Type.INT) {
			return new ConstantSetter<Integer>(name, val.intValue());
		} else if (type == ConstantNumberSetterCreator.Type.LONG) {
			return new ConstantSetter<Long>(name, val.longValue());
		} else if (type == ConstantNumberSetterCreator.Type.SHORT) {
			return new ConstantSetter<Short>(name, val.shortValue());
		} else if (type == ConstantNumberSetterCreator.Type.BYTE) {
			return new ConstantSetter<Byte>(name, val.byteValue());
		}  else {
			return new ConstantSetter<Float>(name, val.floatValue());
		}
	}
	
	private void initType() {
		if (javaClass.equals("long") || javaClass.equals("java.lang.Long")) {
			type = ConstantNumberSetterCreator.Type.LONG;
		} else if (javaClass.equals("int") || javaClass.equals("java.lang.Integer")) {
			type = ConstantNumberSetterCreator.Type.INT;
		} else if (javaClass.equals("double") || javaClass.equals("java.lang.Double")) {
			type = ConstantNumberSetterCreator.Type.DOUBLE;
		}else if (javaClass.equals("float") || javaClass.equals("java.lang.Float")) {
			type = ConstantNumberSetterCreator.Type.FLOAT;
		}else if (javaClass.equals("short") || javaClass.equals("java.lang.Short")) {
			type = ConstantNumberSetterCreator.Type.SHORT;
		}else if (javaClass.equals("byte") || javaClass.equals("java.lang.Byte")) {
			type = ConstantNumberSetterCreator.Type.BYTE;
		}
	}
}
