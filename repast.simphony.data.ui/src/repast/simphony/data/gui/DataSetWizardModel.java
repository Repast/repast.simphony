/*CopyrightHere*/
package repast.simphony.data.gui;

import org.pietschy.wizard.models.StaticModel;

import repast.simphony.data.engine.DataGathererDescriptor;
import repast.simphony.data.engine.DefaultDataGathererDescriptor;
import repast.simphony.data.logging.gather.AggregateDataMapping;
import repast.simphony.data.logging.gather.DataMapping;
import repast.simphony.data.logging.gather.RunNumberMapping;
import repast.simphony.data.logging.gather.TimeDataMapping;
import repast.simphony.engine.schedule.ScheduleParameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Jerry Vos
 */
@SuppressWarnings("unchecked")
public class DataSetWizardModel extends StaticModel {

  private Collection<Class<?>> agentClasses;

  private DataGathererDescriptor gathererDescriptor;


  public DataSetWizardModel(Collection<Class<?>> agentClasses) {
   this(agentClasses, new DefaultDataGathererDescriptor("A Data Set"));
  }

  public DataSetWizardModel(Collection<Class<?>> agentClasses, DataGathererDescriptor descriptor) {
    this.agentClasses = agentClasses;
    this.gathererDescriptor = descriptor;
  }

  public String getActionName() {
    return gathererDescriptor.getName();
  }

  public void setActionName(String actionName) {
    gathererDescriptor.setName(actionName);
  }

  public Collection<Class<?>> getAgentClasses() {
    return agentClasses;
  }

  public void setAgentClasses(Collection<Class<?>> agentClasses) {
    this.agentClasses = agentClasses;
  }

  public Collection<AggregateDataMapping<?, ?>> getPrimaryAggregateDataMappings() {
    return gathererDescriptor.getPrimaryAggregateDataMappings();
  }

  public Collection<AggregateDataMapping<?, ?>> getAlternatedAggregateDataMappings() {
    return gathererDescriptor.getAlternatedAggregateMappings();
  }

  public void clearMappings() {
    ArrayList<DataMapping> list = new ArrayList<DataMapping>(gathererDescriptor.getDataMappings());
    for (DataMapping mapping : list) {
      gathererDescriptor.removeMapping(mapping);
    }

    ArrayList<AggregateDataMapping> mappings = new ArrayList<AggregateDataMapping>(gathererDescriptor.getPrimaryAggregateDataMappings());
    for (AggregateDataMapping mapping : mappings) {
      gathererDescriptor.removePrimaryAggregateMapping(mapping);
    }

    mappings = new ArrayList<AggregateDataMapping>(gathererDescriptor.getAlternatedAggregateMappings());
    for (AggregateDataMapping mapping : mappings) {
      gathererDescriptor.removeAlternatedAggregateMapping(mapping);
    }
  }

  public void setDataMappings(Map<String, DataMapping> mappings) {
    ArrayList<DataMapping> list = new ArrayList<DataMapping>(gathererDescriptor.getDataMappings());
    for (DataMapping mapping : list) {
      gathererDescriptor.removeMapping(mapping);
    }
    for (String columnName : mappings.keySet()) {
      gathererDescriptor.addMapping(columnName, mappings.get(columnName));
    }
  }

  public void setPrimaryAggregateDataMappings(Map<String, AggregateDataMapping> mappings) {
    ArrayList<AggregateDataMapping> list = new ArrayList<AggregateDataMapping>(gathererDescriptor.getPrimaryAggregateDataMappings());
    for (AggregateDataMapping mapping : list) {
      gathererDescriptor.removePrimaryAggregateMapping(mapping);
    }
    for (String columnName : mappings.keySet()) {
      gathererDescriptor.addPrimaryAggregateMapping(columnName, mappings.get(columnName));
    }
  }

  public void setAlternatedAggregateDataMappings(Map<String, AggregateDataMapping> mappings) {
    ArrayList<AggregateDataMapping> list = new ArrayList<AggregateDataMapping>(gathererDescriptor.getAlternatedAggregateMappings());
    for (AggregateDataMapping mapping : list) {
      gathererDescriptor.removeAlternatedAggregateMapping(mapping);
    }

    for (String columnName : mappings.keySet()) {
      gathererDescriptor.addAlternatedAggregateMapping(columnName, mappings.get(columnName));
    }
  }

  public Object getDataSetId() {
    return gathererDescriptor.getDataSetId();
  }

  public void setDataSetId(Object dataSetId) {
    gathererDescriptor.setDataSetId(dataSetId);
  }

  public Collection<DataMapping> getDataMappings() {
    return gathererDescriptor.getDataMappings();
  }

  public ScheduleParameters getSchedParams() {
    return gathererDescriptor.getScheduleParameters();
  }

  public void setSchedParams(ScheduleParameters schedParams) {
    gathererDescriptor.setScheduleParameters(schedParams);
  }

  public Class<?> getAgentClass() {
    return gathererDescriptor.getAgentClass();
  }

  public void setAgentClass(Class<?> agentClass) {
    gathererDescriptor.setAgentClass(agentClass);
  }

  public void setDescriptor(DataGathererDescriptor descriptor) {
    this.gathererDescriptor = descriptor;
  }

  public DataGathererDescriptor getDescriptor() {
    return gathererDescriptor;
  }

  /**
   * Cleans up the mapping making sure that mappings and aggregate mappings
   * have more than just time and run info in them.
   */
  public void cleanMappings() {
    boolean onlyTimeRun = true;
    List<String> names = new ArrayList<String>();
    for (DataMapping mapping : (Iterable<DataMapping>) gathererDescriptor.getDataMappings()) {
      names.add(gathererDescriptor.getColumnName(mapping));
      if (!(mapping instanceof RunNumberMapping) && !(mapping instanceof TimeDataMapping)) {
        onlyTimeRun = false;
        break;
      }
    }

    if (onlyTimeRun) {
      for (String name : names) {
        gathererDescriptor.removeMapping(name);
      }
    }


    onlyTimeRun = true;
    for (AggregateDataMapping mapping : (Iterable<AggregateDataMapping>) gathererDescriptor.getPrimaryAggregateDataMappings()) {
      if (!(mapping instanceof RunNumberMapping) && !(mapping instanceof TimeDataMapping)) {
        onlyTimeRun = false;
        break;
      }
    }

    if (onlyTimeRun) {
      List<AggregateDataMapping> mappings = new ArrayList<AggregateDataMapping>();
      mappings.addAll(gathererDescriptor.getPrimaryAggregateDataMappings());
      for (AggregateDataMapping mapping : mappings) {
        gathererDescriptor.removePrimaryAggregateMapping(mapping);
      }
    }
  }
}
