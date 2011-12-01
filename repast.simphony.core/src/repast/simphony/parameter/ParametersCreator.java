package repast.simphony.parameter;

import org.apache.commons.lang.math.Range;
import simphony.util.messages.MessageCenter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;


/**
 * Creates Parameters objects. Parameters are themselves intended to be
 * immutable w/r to their schema after their creation. This allows for the
 * incremental creation of a parameters object.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ParametersCreator {

  private static MessageCenter msg = MessageCenter.getMessageCenter(ParametersCreator.class);
  private DefaultParameters parameters = new DefaultParameters();

  /**
   * Adds a parameter to this creator. The next call to createParameters() will
   * create a Parameters object that includes the specified parameter.
   *
   * @param name       the name of the parameter
   * @param type       the type of the parameter
   * @param value      the default value of the parameter
   * @param isReadOnly whether or not the parameter value is read only
   */
  public void addParameter(String name, Class type, Object value, boolean isReadOnly) {
    parameters.addParameter(name, type, value, isReadOnly);
  }

  /**
   * Adds a parameter to this creator. The next call to createParameters() will
   * create a Parameters object that includes the specified parameter.
   *
   * @param name        the name of the parameter
   * @param displayName the display name of the parameter
   * @param type        the type of the parameter
   * @param value       the default value of the parameter
   * @param isReadOnly  whether or not the parameter value is read only
   */
  public void addParameter(String name, String displayName, Class type, Object value, boolean isReadOnly) {
    parameters.addParameter(name, displayName, type, value, isReadOnly);
  }
  
  /**
   * Gets whether or not this creator contains the
   * named parameter.
   * 
   * @param name the name of the parameter
   * @return true if this creator contains the
   * named parameter, otherwise false.
   */
  public boolean contains(String name) {
    return parameters.getSchema().contains(name);
  }

  /**
   * Adds all the parameters in the specified Parameters objects to this creator.
   * The next call to createParameters() will create a Parameters object that includes
   * those parameters.
   *
   * @param params the parameters to add to this creator.
   */
  public void addParameters(Parameters params) {
    Schema schema = params.getSchema();
    for (String name : schema.parameterNames()) {
      ParameterSchema details = schema.getDetails(name);
      String displayName = params.getDisplayName(name);
      if (displayName == null) displayName = name;
      Object defaultValue = params.getSchema().getDetails(name).getDefaultValue();
      addParameter(name, displayName, details.getType(), defaultValue, params.isReadOnly(name));
      parameters.setValue(name, params.getValue(name));
      if (details.getConstrainingList() != null) addConstraint(name, details.getConstrainingList());
      if (details.getConstrainingRange() != null) addConstraint(name, details.getConstrainingRange());
      if (details.getConverter() != null) addConvertor(name, details.getConverter());
    }
  }

  /**
   * Constrains the value of the named to parameter to those in the list.
   *
   * @param name             the name of the parameter
   * @param constrainingList a list containing the possible values for
   *                         the named parameter
   */
  public void addConstraint(String name, List constrainingList) {
    parameters.addConstraint(name, constrainingList);
  }

  /**
   * Adds a converter to convert the named parameter back and forth to a String.
   *
   * @param name the name of the parameter
   * @param converter the converter
   */
  public void addConvertor(String name, StringConverter converter) {
    parameters.addConvertor(name, converter);
  }

  /**
   * Constrains the value of the named parameter to those in the
   * specified range.
   *
   * @param name              the name of the parmaeter
   * @param constrainingRange a range of the possible values for the named parameter
   */
  public void addConstraint(String name, Range constrainingRange) {
    parameters.addConstraint(name, constrainingRange);
  }

  /**
   * Create a BoundParameters object containing the currently added parameters as
   * well any annotatated Parameters in the specified class.
   *
   * @param clazz the class
   * @return the created BoundParameters.
   * @throws java.beans.IntrospectionException
   *          if there is an error introspecting the
   *          specified class for Parameter annotations.
   */
  public BoundParameters createBoundParameters(Class clazz) throws IntrospectionException {
    BoundParameters parameters = new BoundParameters(this.parameters);
    BeanInfo info = Introspector.getBeanInfo(clazz, Object.class);
    PropertyDescriptor[] pds = info.getPropertyDescriptors();
    for (PropertyDescriptor pd : pds) {
      Method method = pd.getReadMethod();
      if (method != null) {
        Parameter param = method.getAnnotation(Parameter.class);
        if (param != null) {
          try {
            Class<?> paramClass = method.getReturnType();
            Object val = ParameterUtils.parseDefaultValue(paramClass, param.usageName(), param.defaultValue())[0];
            parameters.addParameter(param.usageName(), param.displayName(), paramClass, val, pd.getReadMethod(),
                    pd.getWriteMethod());
          } catch (ParameterFormatException ex) {
            msg.warn("Unsupported parameter type", ex);
          }
        }
      }
    }
    this.parameters = new DefaultParameters();
    return parameters;
  }

  /**
   * Create a Parameters object contains the added parameters. The
   * list of parameters is cleared.
   *
   * @return the created Parameters.
   */
  public Parameters createParameters() {
    Parameters ret = new DefaultParameters(parameters);
    parameters = new DefaultParameters();
    return ret;
  }

  /**
   * Gets number of currently added parameters.
   *
   * @return number of currently added parameters.
   */
  public int getParameterCount() {
    return parameters.getSchema().size();
	}
}
