/**
 * 
 */
package repast.simphony.data2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * DataSet for ordinary non-aggregate data sources. One row of data will be
 * recorded for each element in the recorded dataset.
 * 
 * @author Nick Collier
 * 
 * @param T
 *          the type that is this data set will collect data from
 */
public class NonAggregateDataSet extends AbstractDataSet {

  private List<NonAggregateDataSource> sources = new ArrayList<NonAggregateDataSource>();
  private Class<?> sourceType;

  /**
   * Creates a NonAggregateDataSet with the specified sources and sinks.
   * 
   * @param sources
   * @param sinks
   */
  public NonAggregateDataSet(String id, Collection<NonAggregateDataSource> sources,
      Collection<? extends DataSink> sinks) {
    super(id);
    this.sources.addAll(sources);
    this.sinks.addAll(sinks);
    for (DataSource source: sources) {
      // void.class is a flag that the tick data source etc.
      // use to indicate that they don't operate on passed in objects.
      if (!source.getSourceType().equals(void.class)) {
        sourceType = source.getSourceType();
        break;
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.AbstractDataSet#getSourceIds()
   */
  @Override
  protected List<String> getSourceIds() {
    List<String> ids = new ArrayList<String>();
    for (NonAggregateDataSource source : sources) {
      ids.add(source.getId());
    }
    return ids;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSet#record(java.util.Collection)
   */
  // lots of looping here but intention is that we
  // only get the data once, and then send it to multiple
  // sinks.
  @Override
  public void record(Map<Class<?>, SizedIterable<?>> objMap) {
    SizedIterable<?> objs = objMap.get(sourceType);
    for (Object obj : objs) {
      for (DataSink sink : sinks) {
        sink.rowStarted();
      }

      for (NonAggregateDataSource source : sources) {
        String id = source.getId();
        Object val = source.get(obj);
        for (DataSink sink : sinks) {
          sink.append(id, val);
        }
      }

      for (DataSink sink : sinks) {
        sink.rowEnded();
      }
    }
    
    for (DataSink sink : sinks) {
      sink.recordEnded();
    }
  }

  @Override
  public Iterable<Class<?>> getSourceTypes() {
    List<Class<?>> list = new ArrayList<Class<?>>();
    list.add(sourceType);
    return list;
  }
}
