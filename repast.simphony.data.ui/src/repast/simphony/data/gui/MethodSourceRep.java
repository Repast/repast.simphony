package repast.simphony.data.gui;

import repast.simphony.data.logging.gather.AggregateDataMapping;
import repast.simphony.data.logging.gather.DataMapping;
import repast.simphony.data.logging.gather.MethodMapping;

import java.lang.reflect.Method;

/**
 * @author Nick Collier
 */
class MethodSourceRep implements MappingSourceRepresentation {

	private Method method;
	private String rep;

	public MethodSourceRep(Method method)  {
		this.method = method;
		Class retVal = method.getReturnType();
		String className = retVal.getName();
		if (className.lastIndexOf(".") > -1) {
			className = className.substring(className.lastIndexOf(".") + 1, className.length());
		}
		rep = method.getName() + "(): " + className;
	}

	public String toString() {
		return rep;
	}

	public int compareTo(MethodSourceRep methodRep) {
		return rep.compareTo(methodRep.rep);
	}


  /**
   * Adds this mapping represented by this to the specified model.
   *
   * @param model      the model to add the mapping to
   * @param columnName the name of the column the mapping maps to
   */
  public void addMapping(String columnName, DataSetWizardModel model) {
    model.getDescriptor().addMapping(columnName, new MethodMapping(method));
  }

  public boolean equalsMappingSource(DataMapping mapping) {
		if (mapping instanceof MethodMapping) {
			MethodMapping mm = (MethodMapping) mapping;
			return method.equals(mm.getMethod());
		}
		return false;
	}

	public boolean isMappingEditable() {
		return true;
	}

	public boolean createdMappingsAggregate() {
		return false;
	}
	
	public Method getMethod() {
		return method;
	}


  /**
   * Creates an AggregateDataMapping.
   *
   * @return the created AggregateMapping.
   */
  public AggregateDataMapping createAggregateMapping() {
    return null;
  }

  /**
   * Gets whether or not this represents an aggregate mapping.
   *
   * @return true if this represents an aggregate mapping, otherwise false.
   */
  public boolean isAggregate() {
    return false; 
  }
}
