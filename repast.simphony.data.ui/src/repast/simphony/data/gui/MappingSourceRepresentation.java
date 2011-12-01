package repast.simphony.data.gui;

import repast.simphony.data.logging.gather.DataMapping;

/**
 * Representation of a DataMapping source used to work with a mapping
 * in a JTable.
 *
 * @author Nick Collier
 */
public interface MappingSourceRepresentation {

  /**
   * Adds this mapping represented by this to the specified model.
   *
   * @param model the model to add the mapping to
   * @param columnName the name of the column the mapping maps to
   */
  void addMapping(String columnName, DataSetWizardModel model);


  /**
   * Returns true if the mapping source of this representation
   * is equal to that in the specified DataMapping. Otherwise false.
   *
   * @param mapping the DataMapping to compare to
   *
   * @return true if the mapping source of this representation
   * is equal to that in the specified DataMapping. Otherwise false.
   */
  boolean equalsMappingSource(DataMapping mapping);

  /**
   * Gets whether or not this mapping representation is editable.
   *
   * @return true if editable otherwise false.
   */
  boolean isMappingEditable();
}
