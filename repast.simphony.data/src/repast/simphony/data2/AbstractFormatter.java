/**
 * 
 */
package repast.simphony.data2;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract base class for Formatters.
 * 
 * @author Nick Collier
 */
public abstract class AbstractFormatter implements Formatter {

  protected static class ItemFormatter {

    int index;

    public ItemFormatter(int index) {
      this.index = index;
    }

    public void format(Object obj, String[] data) {
      data[index] = obj.toString();
    }
  }
  
  protected static class RuntimeTestFormatter extends ItemFormatter {
    
    Map<String, ItemFormatter> keyMap;
    String id;
    
    public RuntimeTestFormatter(Map<String, ItemFormatter> keyMap, String id, int index) {
      super(index);
      this.keyMap = keyMap;
      this.id = id;
    }
    
    public void format(Object obj, String[] data) {
      ItemFormatter formatter;
      if (Number.class.isAssignableFrom(obj.getClass())) {
        formatter = new ItemFormatter(index);
      } else {
        formatter = new StringFormatter(index);
      }
      
      formatter.format(obj, data);
      keyMap.put(id, formatter);
    }
  }

  protected static class StringFormatter extends ItemFormatter {

    public StringFormatter(int index) {
      super(index);
    }

    public void format(Object obj, String[] data) {
      StringBuilder builder = new StringBuilder("\"");
      builder.append(obj.toString());
      builder.append("\"");
      data[index] = builder.toString();
    }
  }

  protected Map<String, ItemFormatter> keyMap = new LinkedHashMap<String, ItemFormatter>();
  protected String[] data;
  protected String delimiter;

  /**
   * Creates a Formatter that will format data form the specified sources. The
   * ids of the sources will become the header info.
   * 
   * @param sources
   */
  public AbstractFormatter(Collection<? extends DataSource> sources, String delimiter) {
    this.delimiter = delimiter;
    int i = 0;
    for (DataSource source : sources) {

      if (source.getDataType().equals(Object.class)) {
        // ReLogo data types can be plain objects.
        keyMap.put(source.getId(), new RuntimeTestFormatter(keyMap, source.getId(), i));
      } else if (Number.class.isAssignableFrom(source.getDataType())) {
        keyMap.put(source.getId(), new ItemFormatter(i));
      } else {
        keyMap.put(source.getId(), new StringFormatter(i));
      }
      i++;
    }

    data = new String[keyMap.size()];
    Arrays.fill(data, "");
  }

  /**
   * Clears this formatter of any data that has been added for formatting.
   */
  public void clear() {
    Arrays.fill(data, "");
  }

  /**
   * Adds the specified object to the data to be formatted.
   * 
   * @param id
   *          the id of the data to add
   * @param obj
   *          the object to add
   */
  public void addData(String id, Object obj) {
    ItemFormatter formatter = keyMap.get(id);
    if (formatter != null)
      formatter.format(obj, data);
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.Formatter#getDelimiter()
   */
  @Override
  public String getDelimiter() {
    return delimiter;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.Formatter#setDelimiter(java.lang.String)
   */
  @Override
  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }
  
  
}
