package repast.simphony.parameter.xml;

import org.xml.sax.Attributes;

import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.parameter.ParameterFormatException;

/**
 * Interface for classes that create ParameterSetters from
 * xml attribute data.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface ParameterSetterCreator {

	/**
	 * Initializes this ParameterSetterCreator with the specified attributes.
	 * Any following calls to addParameter or createSetter will use this
	 * attributes.
	 *
	 * @param attributes
	 */
	void init(Attributes attributes) throws ParameterFormatException;

	/**
	 * Adds the parameter to the specified creator based on the
	 * attributes added in init.
	 *
	 * @param creator
	 */
	void addParameter(ParametersCreator creator);

	/**
	 * Creates a parameter setter from the attributes added in init.
	 *
	 * @return a parameter setter from the attributes added in init.
	 */
	ParameterSetter createSetter();
}
