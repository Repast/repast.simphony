/**
 * 
 */
package repast.simphony.data2.builder;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

/**
 * DataSinkBuilder that can build FileDataSinks.
 * 
 * @author Nick Collier
 */
public class FileDataSinkBuilder implements SinkBuilder {

  private FormatType type;
  private String delimiter;
  private String fname, name;
  private boolean addTimeStamp;
  // linked to preserve order
  private Set<String> sourceIds = new LinkedHashSet<String>();
  
  private static DateFormat format = new SimpleDateFormat("yyyy.MMM.dd.HH_mm_ss");
  

  public FileDataSinkBuilder(String name, String fileName, String delimiter, FormatType formatType, boolean addTimeStamp) {
    this.type = formatType;
    this.delimiter = delimiter;
    this.fname = fileName;
    this.addTimeStamp = addTimeStamp;
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
  
  private void moveOldFile(String filename) {
    File originalFile = new File(filename);
    if (!originalFile.exists()) {
      return;
    }

    File movedFileName = new File(originalFile.getAbsolutePath());
    
    long i = 0;
    while (movedFileName.exists()) {
      int index = filename.lastIndexOf(".");
      if (index != -1) {
        movedFileName = new File(filename.substring(0, index) + "." + i + filename.substring(index, filename.length()));
      } else {
        filename = filename + "." + i;
      }
      i++;
    }

    originalFile.renameTo(movedFileName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.builder.SinkBuilder#create()
   */
  @Override
  public DataSink create(Collection<? extends DataSource> sources) {
    String filename = fname;
    if (addTimeStamp) {
      String ts = format.format(new Date());
      int index = fname.lastIndexOf(".");
      if (index != -1) {
        filename = fname.substring(0, index) + "." + ts + fname.substring(index, fname.length());
      } else {
        filename = filename + "." + ts;
      }
    }
    
    if (filename.trim().startsWith("~")) {
      filename = filename.replace("~", System.getProperty("user.home"));
    }
    moveOldFile(filename);
    
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
