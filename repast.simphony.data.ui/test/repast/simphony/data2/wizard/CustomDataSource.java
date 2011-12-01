/**
 * 
 */
package repast.simphony.data2.wizard;

import repast.simphony.data2.AggregateDataSource;
import repast.simphony.data2.NonAggregateDataSource;

/**
 * @author Nick Collier
 */
public class CustomDataSource implements AggregateDataSource, NonAggregateDataSource {

  @Override
  public String getId() {
    // TODO Auto-generated method stub
    return "custom data source";
  }

  @Override
  public Class<?> getDataType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Class<?> getSourceType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object get(Object obj) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object get(Iterable<?> objs, int size) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void reset() {
    // TODO Auto-generated method stub
    
  }
}
