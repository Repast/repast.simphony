/*CopyrightHere*/
package repast.simphony.data.gui;

import repast.simphony.data.logging.gather.DataMapping;
import repast.simphony.data.logging.gather.RunNumberMapping;

/**
 * A simple representation of a RunNumber representation.
 * 
 * @author Jerry Vos
 */
public class RunNumberRepresentation implements MappingSourceRepresentation {


  /**
   * Adds this mapping represented by this to the specified model.
   * This will add the mapping as both an aggregate and a non-aggregate.
   * It is up other code to clean up the model.
   *
   * @param model the model to add the mapping to
   * @param columnName the name of the column the mapping maps to
   */
  public void addMapping(String columnName, DataSetWizardModel model) {
    model.getDescriptor().addMapping(columnName, new RunNumberMapping());
    model.getDescriptor().addPrimaryAggregateMapping(columnName, new RunNumberMapping());
  }

  public boolean equalsMappingSource(DataMapping mapping) {
		return mapping instanceof RunNumberMapping;
	}

	public String toString() {
		return "current run number: int";
	}

	public boolean isMappingEditable() {
		return true;
	}

	public boolean createdMappingsAggregate() {
		return false;
	}
}
