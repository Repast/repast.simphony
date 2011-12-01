package repast.simphony.util.bytecode;

/**
 * @author Nick Collier
 */
public interface DataSource {

  /**
   * Gets some data value from the specified object
   *
   * @param obj the object to get the data from
   * @return the data
   */
  Object getData(Object obj);
}
