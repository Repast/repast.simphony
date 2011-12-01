/**
 * 
 */
package repast.simphony.data2.builder;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.data2.DataSet;
import repast.simphony.data2.DataSink;
import repast.simphony.data2.MethodDataSource;
import repast.simphony.data2.NonAggregateDataSet;
import repast.simphony.data2.NonAggregateDataSource;
import repast.simphony.data2.TickCountDataSource;

/**
 * Builds datasets from a description of them.
 * 
 * @author Nick Collier
 */
public class NonAggregateDataSetBuilder extends AbstractDataSetBuilder implements DataSetBuilder<NonAggregateDataSource> {

  private Class<?> targetType;
  private List<NonAggregateDataSource> dataSources = new ArrayList<NonAggregateDataSource>();
  /**
   * Creates a NonAggregateDataSetBuilder that will build a dataset with the
   * specified id.
   * 
   * @param id
   *          the id of the dataset to build
   */
  public NonAggregateDataSetBuilder(String id, Class<?> targetType) {
    super(id);
    this.targetType = targetType;
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.builder.DataSetBuilder#isAggrgate()
   */
  @Override
  public boolean isAggregate() {
    return false;
  }
  
  /**
   * Adds the specified data source to this builder. The data source will
   * be source of data for the data set created by this builder.
   * 
   * @param dataSource the data source to add
   */
  public void addDataSource(NonAggregateDataSource dataSource) {
    if (dataSource.getId().equals(TickCountDataSource.ID) && dataSources.size() > 0) {
      dataSources.add(0, dataSource);
    } else {
      dataSources.add(dataSource);
    }
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.builder.DataSetBuilder#create()
   */
  @Override
  public DataSet create() {
    List<DataSink> sinks = new ArrayList<DataSink>();
    for (SinkBuilder sinkBuilder : sinkBuilders) {
      sinks.add(sinkBuilder.create(dataSources));
    }
    return new NonAggregateDataSet(id, dataSources, sinks);
  }

  /**
   * Defines a method data sources to be added to the data set built by this
   * builder. The data source will be named by the id, and call the specified
   * method on objects of the specified type.
   * 
   * @param id
   * @param objType
   * @param methodName
   */
  public void defineMethodDataSource(String id, Class<?> objType, String methodName) {
    if (!targetType.isAssignableFrom(objType))
      throw new IllegalArgumentException("Invalid data source for given dataset target type.");
    dataSources.add(new MethodDataSource(id, objType, methodName));
  }
}
