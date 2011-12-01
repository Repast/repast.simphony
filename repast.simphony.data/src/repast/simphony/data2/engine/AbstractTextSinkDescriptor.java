/**
 * 
 */
package repast.simphony.data2.engine;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import repast.simphony.data2.FormatType;
import repast.simphony.engine.schedule.Descriptor;

/**
 * Abstract base class for descriptors that define text output sink.
 * 
 * @author Nick Collier
 */
public abstract class AbstractTextSinkDescriptor implements Descriptor {

  protected String name, dataSet, delimiter;
  protected FormatType format = FormatType.TABULAR;
  // linked to preserve the order
  protected Set<String> sourceIds = new LinkedHashSet<String>();

  public AbstractTextSinkDescriptor(String name) {
    this.name = name;
    delimiter = ",";
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.engine.schedule.Descriptor#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.engine.schedule.Descriptor#setName(java.lang.String)
   */
  @Override
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the dataSet
   */
  public String getDataSet() {
    return dataSet;
  }

  /**
   * @param dataSet the dataSet to set
   */
  public void setDataSet(String dataSet) {
    this.dataSet = dataSet;
  }

  /**
   * @return the delimiter
   */
  public String getDelimiter() {
    return delimiter;
  }

  /**
   * @param delimiter the delimiter to set
   */
  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  /**
   * @return the format
   */
  public FormatType getFormat() {
    return format;
  }

  /**
   * @param format the format to set
   */
  public void setFormat(FormatType format) {
    this.format = format;
  }
  
  public void addSourceId(String sourceId) {
    sourceIds.add(sourceId);
  }
  
  public void clearSourceIds() {
    sourceIds.clear();
  }
  
  /**
   * Gets a list of the source ids this sink will record.
   * 
   * @return a list of the source ids this sink will record.
   */
  public List<String> getSourceIds() {
    List<String> list = new ArrayList<String>(sourceIds);
    return list;
  }
}
