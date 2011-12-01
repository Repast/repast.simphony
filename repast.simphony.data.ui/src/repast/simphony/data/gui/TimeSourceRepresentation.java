package repast.simphony.data.gui;

import repast.simphony.data.logging.gather.DataMapping;
import repast.simphony.data.logging.gather.DataMappingFactory;
import repast.simphony.data.logging.gather.DefaultTimeDataMapping;
import repast.simphony.data.logging.gather.TimeDataMapping;

/**
 * @author Nick Collier
 */
public class TimeSourceRepresentation implements MappingSourceRepresentation {

	private DataMapping createMapping() {
		// arg is ignored so we can just pass object.class
		return DataMappingFactory.createTimeDataMapping(Object.class);
	}


  /**
   * Adds this mapping represented by this to the specified model.
   * This will add the mapping as both an aggregate and a non-aggregate.
   * It is up other code to clean up the model.
   *
   * @param model      the model to add the mapping to
   * @param columnName the name of the column the mapping maps to
   */
  public void addMapping(String columnName, DataSetWizardModel model) {
    model.getDescriptor().addMapping(columnName, new DefaultTimeDataMapping());
    model.getDescriptor().addPrimaryAggregateMapping(columnName, new DefaultTimeDataMapping());
  }

  public boolean equalsMappingSource(DataMapping mapping) {
		return mapping instanceof TimeDataMapping;
	}

	public String toString() {
		return "current tick: double";
	}

	public boolean isMappingEditable() {
		return true;
	}
}
