/**
 * 
 */
package repast.simphony.data2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * DataSet implementation that records data from aggregate DataSources.
 * 
 * @author Nick Collier
 */
public class AggregateDataSet extends AbstractDataSet {

  private List<AggregateDataSource> sources = new ArrayList<AggregateDataSource>();
  private List<Class<?>> targetTypes = new ArrayList<Class<?>>();

  /**
   * Creates an AggregateDataSet with the specified sources and sinks.
   * 
   * @param sources
   * @param sinks
   */
  public AggregateDataSet(String id, Collection<AggregateDataSource> sources,
      Collection<? extends DataSink> sinks) {
    super(id);
    this.sources.addAll(sources);
    this.sinks.addAll(sinks);
    for (DataSource source : sources) {
      targetTypes.add(source.getSourceType());
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
    for (AggregateDataSource source : sources) {
      ids.add(source.getId());
    }
    return ids;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSet#getTargetTypes()
   */
  @Override
  public Iterable<Class<?>> getSourceTypes() {
    return targetTypes;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSet#record(java.util.Collection)
   */
  @Override
  // lots of looping here but intention is that we
  // only get the data once, and then send it to multiple
  // sinks.
  public void record(Map<Class<?>, SizedIterable<?>> objMap) {
    for (DataSink sink : sinks) {
      sink.rowStarted();
    }

    for (AggregateDataSource source : sources) {
      source.reset();
    }

    for (AggregateDataSource source : sources) {
      String id = source.getId();
      SizedIterable<?> objs = objMap.get(source.getSourceType());
      Object val = source.get(objs, objs.size());
      for (DataSink sink : sinks) {
        sink.append(id, val);
      }
    }

    for (DataSink sink : sinks) {
      sink.rowEnded();
      sink.recordEnded();
    }
  }
}
