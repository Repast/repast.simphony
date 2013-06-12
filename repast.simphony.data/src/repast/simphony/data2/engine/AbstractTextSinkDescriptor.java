/**
 * 
 */
package repast.simphony.data2.engine;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import repast.simphony.data2.FormatType;
import repast.simphony.scenario.AbstractDescriptor;

/**
 * Abstract base class for descriptors that define text output sink.
 * 
 * @author Nick Collier
 */
public abstract class AbstractTextSinkDescriptor extends AbstractDescriptor {

  protected String dataSet, delimiter;
  protected FormatType format = FormatType.TABULAR;
  // linked to preserve the order
  protected Set<String> sourceIds = new LinkedHashSet<String>();

  public AbstractTextSinkDescriptor(String name) {
    super(name);
    delimiter = ",";
  }

  /**
   * @return the dataSet
   */
  public String getDataSet() {
    return dataSet;
  }

  /**
   * @param dataSet
   *          the dataSet to set
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
   * @param delimiter
   *          the delimiter to set
   */
  public void setDelimiter(String delimiter) {
    if (!this.delimiter.equals(delimiter)) {
      this.delimiter = delimiter;
      scs.fireScenarioChanged(this, "delimiter");
    }
  }

  /**
   * @return the format
   */
  public FormatType getFormat() {
    return format;
  }

  /**
   * @param format
   *          the format to set
   */
  public void setFormat(FormatType format) {
    if (format != this.format) {
      this.format = format;
      scs.fireScenarioChanged(this, "format");
    }
  }
  
  public void removeSourceId(String sourceId) {
    if (sourceIds.contains(sourceId)) {
      sourceIds.remove(sourceId);
      scs.fireScenarioChanged(this, "sourceIds");
    }
  }

  public void addSourceId(String sourceId) {
    if (!sourceIds.contains(sourceId)) {
      sourceIds.add(sourceId);
      scs.fireScenarioChanged(this, "sourceIds");
    }
  }

  public void clearSourceIds() {
    if (sourceIds.size() > 0) {
      scs.fireScenarioChanged(this, "sourceIds");
      sourceIds.clear();
    }
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
