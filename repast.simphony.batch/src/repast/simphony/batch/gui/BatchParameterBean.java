package repast.simphony.batch.gui;

import java.beans.PropertyChangeListener;
import java.util.StringTokenizer;

import repast.simphony.parameter.ParameterFormatException;
import repast.simphony.parameter.ParameterTypeFactory;
import repast.simphony.parameter.Parameters;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

public class BatchParameterBean {

  private String name, type, listItems, start, end, step, value;
  private ParameterType pType;
  private boolean isConstant = false;

  protected ExtendedPropertyChangeSupport pcs = new ExtendedPropertyChangeSupport(this);

  public BatchParameterBean(boolean isConstant) {
    this();
    this.isConstant = isConstant;
  }

  public BatchParameterBean() {
    name = type = start = end = step = value = listItems = "";
    pType = ParameterType.CONSTANT;
  }

  public ValidationResult validate(Parameters params) {
    if (name == null || name.trim().length() == 0) {
      String msg = "Invalid name value.";
      return new ValidationResult(msg, false);
    } else if (pType == ParameterType.CONSTANT && value.trim().length() == 0) {
      String msg = "Invalid constant value.";
      return new ValidationResult(msg, false);
    } else if (pType == ParameterType.NUMERIC_RANGE) {
      if (badStrings(start, end, step)) {
        String msg = "Invalid start, end, or step value.";
        return new ValidationResult(msg, false);
      }
      // strings are ok, make sure that can reach end from
      // start with step.
      try {
        double start = Double.valueOf(this.start);
        double end = Double.valueOf(this.end);
        double step = Double.valueOf(this.step);

        if (end > start && step <= 0) {
          String msg = "End is not reachable from start using step.";
          return new ValidationResult(msg, false);
        } else if (end < start && step >= 0) {
          String msg = "End is not reachable from start using step.";
          return new ValidationResult(msg, false);
        }

      } catch (NumberFormatException ex) {
        String msg = "Invalid start, end, or step value.";
        return new ValidationResult(msg, false);
      }
    } else if (pType == ParameterType.LIST) {
      Class<?> clazz = params.getSchema().getDetails(name).getType();
      repast.simphony.parameter.ParameterType type = ParameterTypeFactory.instance()
          .getParameterType(clazz);
      if (type != null) {
        String val = "";
        try {
          for (StringTokenizer tok = new StringTokenizer(listItems, System
              .getProperty("line.separator")); tok.hasMoreTokens();) {
            val = tok.nextToken();
            type.getValue(val);

          }
        } catch (ParameterFormatException ex) {
          String msg = "Invalid list value '" + val + "'.";
          return new ValidationResult(msg, false);
        }
      }
    }

    return ValidationResult.OK;
  }

  private boolean badStrings(String... str) {
    for (String s : str) {
      if (s == null || s.trim().length() == 0)
        return true;
    }
    return false;
  }

  public String toString() {
    return String.format(
        "Name: %s, Type: %s, pType: %s, start: %s, end: %s, step: %s, value: %s, listItems: %s",
        name, type, pType, start, end, step, value, listItems);
  }

  public String getName() {
    return name;
  }

  /**
   * Gets whether this bean is permanently of the constant type.
   * 
   * @return whether this bean is permanently of the constant type.
   */
  public boolean isConstant() {
    return isConstant;
  }

  public void setName(String name) {
    String old = this.name;
    if (!old.equals(name)) {
      this.name = name;
      pcs.firePropertyChange("name", old, this.name);
    }
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    String old = this.type;
    if (!old.equals(type)) {
      this.type = type;
      pcs.firePropertyChange("type", old, this.type);
    }
  }

  public ParameterType getParameterType() {
    return pType;
  }

  public void setParameterType(ParameterType pType) {
    ParameterType old = this.pType;
    if (!old.equals(pType)) {
      this.pType = pType;
      pcs.firePropertyChange("pType", old, this.pType);
    }
  }

  public String getListItems() {
    return listItems;
  }

  public void setListItems(String items) {
    String old = this.listItems;
    if (!old.equals(items)) {
      this.listItems = items;
      pcs.firePropertyChange("listItems", old, this.listItems);
    }
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    String old = this.start;
    if (!old.equals(start)) {
      this.start = start;
      pcs.firePropertyChange("start", old, this.start);
    }
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    String old = this.end;
    if (!old.equals(end)) {
      this.end = end;
      pcs.firePropertyChange("end", old, this.end);
    }
  }

  public String getStep() {
    return step;
  }

  public void setStep(String step) {
    String old = this.step;
    if (!old.equals(step)) {
      this.step = step;
      pcs.firePropertyChange("step", old, this.step);
    }
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    String old = this.value;
    if (!old.equals(value)) {
      this.value = value;
      pcs.firePropertyChange("value", old, this.value);
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener x) {
    pcs.addPropertyChangeListener(x);
  }

  public void removePropertyChangeListener(PropertyChangeListener x) {
    pcs.removePropertyChangeListener(x);
  }
}
