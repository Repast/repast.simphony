/**
 * 
 */
package repast.simphony.data2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Formats data in a tabular format with a user specified delimiter.
 * 
 * @author Nick Collier
 */
public class TabularFormatter extends AbstractFormatter {

  /**
   * Creates a Formatter that will format data form the specified sources. The
   * ids of the sources will become the header info.
   * 
   * @param sources
   */
  public TabularFormatter(Collection<? extends DataSource> sources, String delimiter) {
    super(sources, delimiter);
  }
  
  /**
   * Gets a list of the column names.
   * 
   * @return
   */
  public List<String> getColumnNames() {
    return new ArrayList<String>(keyMap.keySet());
  }

  /**
   * Gets the ids of the formatted data sources, quoted and separated by the
   * delimiter.
   * 
   * @return the ids of the formatted data sources, quoted and separated by the
   *         delimiter.
   */
  @Override
  public String getHeader() {
    Iterator<String> iter = keyMap.keySet().iterator();
    StringBuilder builder = new StringBuilder("\"");
    builder.append(iter.next());
    builder.append("\"");

    while (iter.hasNext()) {
      builder.append(delimiter);
      builder.append("\"");
      builder.append(iter.next());
      builder.append("\"");
    }

    return builder.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.Formatter#formatData()
   */
  @Override
  public String formatData() {
    StringBuilder builder = new StringBuilder();
    builder.append(data[0]);
    for (int i = 1, n = data.length; i < n; i++) {
      builder.append(delimiter);
      builder.append(data[i]);
    }
    return builder.toString();
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.Formatter#getFormatType()
   */
  @Override
  public FormatType getFormatType() {
    return FormatType.TABULAR;
  }
}
