/**
 * 
 */
package repast.simphony.data2;

import repast.simphony.engine.environment.RunEnvironment;

/**
 * DataSource that will return the current value of a parameter.
 * 
 * @author Nick Collier
 */
public class ParameterDataSource implements AggregateDataSource, NonAggregateDataSource {

  private String name;

  /**
   * Creates a ParametersDataSource that will the value of the 
   * named parameter.
   * 
   * @param name the parameter name
   */
  public ParameterDataSource(String name) {
    this.name = name;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSource#getId()
   */
  @Override
  public String getId() {
    return name;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSource#getDataType()
   */
  @Override
  public Class<?> getDataType() {
    return Object.class;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSource#getSourceType()
   */
  @Override
  public Class<?> getSourceType() {
    return void.class;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.NonAggregateDataSource#get(java.lang.Object)
   */
  @Override
  public Object get(Object obj) {
    return RunEnvironment.getInstance().getParameters().getValue(name);
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.AggregateDataSource#get(java.lang.Iterable, int)
   */
  @Override
  public Object get(Iterable<?> objs, int size) {
    return RunEnvironment.getInstance().getParameters().getValue(name);
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.AggregateDataSource#reset()
   */
  @Override
  public void reset() {}
}
