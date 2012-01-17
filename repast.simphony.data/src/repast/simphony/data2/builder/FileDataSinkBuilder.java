/**
 * 
 */
package repast.simphony.data2.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import repast.simphony.data2.DataSink;
import repast.simphony.data2.DataSource;
import repast.simphony.data2.FileDataSink;
import repast.simphony.data2.FormatType;
import repast.simphony.data2.Formatter;
import repast.simphony.data2.LineFormatter;
import repast.simphony.data2.TabularFormatter;
import repast.simphony.data2.util.DataUtilities;

/**
 * DataSinkBuilder that can build FileDataSinks.
 * 
 * @author Nick Collier
 */
public class FileDataSinkBuilder implements SinkBuilder {

  private FormatType type;
  private String delimiter;
  private String name;
  private FileNameFormatter fnameFormatter;
  // linked to preserve order
  private Set<String> sourceIds = new LinkedHashSet<String>();

  public FileDataSinkBuilder(String name, FileNameFormatter fnameFormatter, String delimiter, FormatType formatType) {
    this.type = formatType;
    this.delimiter = delimiter;
    this.fnameFormatter = fnameFormatter;
    this.name = name;
  }
  
  /**
   * Adds the specified source id to the list of those to
   * write to the file.
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
    
    String filename = fnameFormatter.getFilename();
    DataUtilities.renameFileIfExists(filename);
    
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
    return new FileDataSink(name, new File(filename), formatter);
  }
}
