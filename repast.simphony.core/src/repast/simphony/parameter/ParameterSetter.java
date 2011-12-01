/*CopyrightHere*/
package repast.simphony.parameter;

public interface ParameterSetter {
	void reset(Parameters params);
	
	boolean atEnd();

	void next(Parameters params);
}
