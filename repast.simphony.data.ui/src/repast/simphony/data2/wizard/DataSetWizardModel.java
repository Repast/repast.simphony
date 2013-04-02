/*CopyrightHere*/
package repast.simphony.data2.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pietschy.wizard.models.DynamicModel;

import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.engine.schedule.ScheduleParameters;

public class DataSetWizardModel extends DynamicModel {

  private List<Class<?>> agentClasses;

  private DataSetDescriptor descriptor;

  public DataSetWizardModel(Collection<Class<?>> agentClasses) {
    this(agentClasses, new DataSetDescriptor("A Data Set"));
  }

  public DataSetWizardModel(Collection<Class<?>> agentClasses, DataSetDescriptor descriptor) {
    this.agentClasses = new ArrayList<Class<?>>(agentClasses);
    Collections.sort(this.agentClasses, new Comparator<Class<?>>() {
      @Override
      public int compare(Class<?> arg0, Class<?> arg1) {
        return arg0.getSimpleName().compareTo(arg1.getSimpleName());
      }
    });
    this.descriptor = descriptor;
  }

  public String getName() {
    return descriptor.getName();
  }

  public void setName(String actionName) {
    descriptor.setName(actionName);
  }

  public Collection<Class<?>> getAgentClasses() {
    return agentClasses;
  }

  public void setAgentClasses(Collection<Class<?>> agentClasses) {
    this.agentClasses = new ArrayList<Class<?>>(agentClasses);
  }

  public ScheduleParameters getSchedParams() {
    return descriptor.getScheduleParameters();
  }
  
  public boolean isScheduleAtEnd() {
    return descriptor.isScheduleAtEnd();
  }

  public void setSchedParams(ScheduleParameters schedParams, boolean atEnd) {
    descriptor.setScheduleParameters(schedParams);
    descriptor.setScheduleAtEnd(atEnd);
  }

  public void setDescriptor(DataSetDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public DataSetDescriptor getDescriptor() {
    return descriptor;
  }

}
