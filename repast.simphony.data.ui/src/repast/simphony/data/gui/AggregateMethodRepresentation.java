package repast.simphony.data.gui;

import repast.simphony.data.logging.gather.DataMapping;
import repast.simphony.data.logging.gather.aggregate.AbstractStatsAggregateMapping;

/**
 * @author Nick Collier
 *         Date: Jul 31, 2007 2:11:34 PM
 */
public class AggregateMethodRepresentation implements MappingSourceRepresentation {

  private AbstractStatsAggregateMapping mapping;
  private String name;


  public AggregateMethodRepresentation(AbstractStatsAggregateMapping mapping, String methodName) {
    this.mapping = mapping;
    this.name = mapping.getStatisticType() + " of " + methodName + "()";
  }


  /**
   * Adds this mapping represented by this to the specified model.
   *
   * @param model      the model to add the mapping to
   * @param columnName the name of the column the mapping maps to
   */
  public void addMapping(String columnName, DataSetWizardModel model) {
    model.getDescriptor().addPrimaryAggregateMapping(columnName, mapping);
  }

  /**
   * Returns true if the mapping source of this representation
   * is equal to that in the specified DataMapping. Otherwise false.
   *
   * @param mapping the DataMapping to compare to
   * @return true if the mapping source of this representation
   *         is equal to that in the specified DataMapping. Otherwise false.
   */
  public boolean equalsMappingSource(DataMapping mapping) {
    return false;
  }

  /**
   * Gets whether or not this mapping representation is editable.
   *
   * @return true if editable otherwise false.
   */
  public boolean isMappingEditable() {
    return false;
  }

  public String toString() {
    return name;
  }
}
