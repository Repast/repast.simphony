/**
 * 
 */
package repast.simphony.data2.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import repast.simphony.data2.ConsoleDataSink;
import repast.simphony.data2.DataSink;
import repast.simphony.data2.DataSource;
import repast.simphony.data2.FormatType;
import repast.simphony.data2.Formatter;
import repast.simphony.data2.LineFormatter;
import repast.simphony.data2.TabularFormatter;

/**
 * DataSinkBuilder that can build FileDataSinks.
 * 
 * @author Nick Collier
 */
public class ConsoleDataSinkBuilder implements SinkBuilder {

  private FormatType type;
  private String delimiter;
  private ConsoleDataSink.OutputStream target;
  // linked to preserve order
  private Set<String> sourceIds = new LinkedHashSet<String>();

  public ConsoleDataSinkBuilder(ConsoleDataSink.OutputStream target, String delimiter, FormatType formatType) {
    this.type = formatType;
    this.delimiter = delimiter;
    this.target = target;
  }
 
  /**
   * Adds the specified source id to the list of those to
   * write to the console.
   * 
   * @param sourceId
   */
  public void addSource(String sourceId) {
    sourceIds.add(sourceId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.builder.SinkBuilder#create()
   */
  @Override
  public DataSink create(Collection<? extends DataSource> sources) {
    List<DataSource> selectedSources = new ArrayList<DataSource>();
    for (String id : sourceIds) {
      for (DataSource source : sources) {
        if (source.getId().equals(id)) {
          selectedSources.add(source);
          break;
        }
      }
    }
    
    // if no selected sources, assume that means use them all.
    if (selectedSources.size() == 0) {
      selectedSources.addAll(sources);
    }
    
    Formatter formatter = type == FormatType.TABULAR ? new TabularFormatter(selectedSources, delimiter)
        : new LineFormatter(selectedSources, delimiter);
    return new ConsoleDataSink(target, formatter);
  }
}
