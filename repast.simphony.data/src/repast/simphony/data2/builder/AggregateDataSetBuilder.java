/**
 * 
 */
package repast.simphony.data2.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.reflect.FastMethod;
import repast.simphony.data2.AggregateDSCreator;
import repast.simphony.data2.AggregateDataSet;
import repast.simphony.data2.AggregateDataSource;
import repast.simphony.data2.AggregateOp;
import repast.simphony.data2.CountDataSource;
import repast.simphony.data2.DataSet;
import repast.simphony.data2.DataSink;
import repast.simphony.data2.MethodDataSource;

/**
 * Builds datasets from a description of them.
 * 
 * @author Nick Collier
 */
public class AggregateDataSetBuilder extends AbstractDataSetBuilder implements DataSetBuilder<AggregateDataSource> {
  
  private List<AggregateDataSource> dataSources = new ArrayList<AggregateDataSource>();
  private Map<FastMethod, AggregateDSCreator> fmBuilderMap = new HashMap<FastMethod, AggregateDSCreator>();
  

  /**
   * Creates a NonAggregateDataSetBuilder that will build a dataset with the
   * specified id.
   * 
   * @param id
   *          the id of the dataset to build
   */
  public AggregateDataSetBuilder(String id) {
    super(id);
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.builder.DataSetBuilder#isAggrgate()
   */
  @Override
  public boolean isAggregate() {
    return true;
  }
  
  /**
   * Adds the specified data source to this builder. The data source will
   * be source of data for the data set created by this builder.
   * 
   * @param dataSource the data source to add
   */
  public void addDataSource(AggregateDataSource dataSource) {
    dataSources.add(dataSource);
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
    return new AggregateDataSet(id, dataSources, sinks);
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
  public void defineMethodDataSource(String id, AggregateOp aggType, Class<?> objType, String methodName) {
    MethodDataSource mds = new MethodDataSource(id, objType, methodName);
    AggregateDSCreator creator = fmBuilderMap.get(mds.getMethod());
    if (creator == null) {
      creator = new AggregateDSCreator(mds);
      fmBuilderMap.put(mds.getMethod(), creator);
    }
    
    dataSources.add(creator.createDataSource(id, aggType));
  }
  
  /**
   * Defines a CountDataSource with the specified id that will return the
   * count of objects of the specified type.
   * 
   * @param id
   * @param objType
   */
  public void defineCountDataSource(String id, Class<?> objType) {
    dataSources.add(new CountDataSource(id, objType));
  }
}
