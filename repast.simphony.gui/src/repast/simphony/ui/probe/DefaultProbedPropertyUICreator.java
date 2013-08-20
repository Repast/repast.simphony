package repast.simphony.ui.probe;

import java.beans.PropertyDescriptor;

import javax.swing.JComponent;

import com.jgoodies.binding.PresentationModel;

/**
 * Represents a property of a probed object whether is read / read only etc.
 * Also produces a JComponent to display and edit the property.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class DefaultProbedPropertyUICreator implements ProbedPropertyUICreator {

  protected enum Type {
    READ_WRITE, WRITE, READ
  };

  protected String displayName;
  protected String name, getterName, setterName;
  protected Type type;

  protected DefaultProbedPropertyUICreator(PropertyDescriptor desc) {
    name = desc.getName();
    displayName = desc.getDisplayName();
    if (desc.getReadMethod() != null && desc.getWriteMethod() != null) {
      type = Type.READ_WRITE;
    } else if (desc.getWriteMethod() == null) {
      type = Type.READ;
    } else {
      type = Type.WRITE;
    }
    if (desc.getReadMethod() != null){
    	getterName = desc.getReadMethod().getName();
    }
    if (desc.getWriteMethod() != null){
    	setterName = desc.getWriteMethod().getName();
    }
    
  }

  public abstract JComponent getComponent(PresentationModel<Object> model);

  public String getName() {
    return name;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void printValue(PresentationModel<Object> model) {
    System.out.println(name + " = " + model.getValue(name));
  }
}
