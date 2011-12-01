package repast.simphony.parameter.xml;

import org.xml.sax.Attributes;

import repast.simphony.parameter.ConstantSetter;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.parameter.*;

/**
 * Creates a constant setter for boolean values.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ConstantBooleanSetterCreator extends AbstractParameterSetterCreator {

	private Boolean value;

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
		value = Boolean.parseBoolean(attributes.getValue(SetterConstants.CONSTANT_VALUE));
	}

	/**
	 * Adds the parameter to the specified creator based on the
	 * attributes added in init.
	 *
	 * @param creator
	 */
	public void addParameter(ParametersCreator creator) {
		creator.addParameter(name, Boolean.class, value, false);
	}

	/**
	 * Creates a parameter setter from the attributes added in init.
	 *
	 * @return a parameter setter from the attributes added in init.
	 */
	public ParameterSetter createSetter() {
		return new ConstantSetter<Boolean>(name, value);
	}

}
