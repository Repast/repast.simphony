/**
 * 
 */
package repast.simphony.data2;

import java.util.Collection;
import java.util.Map;

/**
 * Formats data in single line in the following format:
 * 
 * id: data, id: data, ...
 * 
 * @author Nick Collier
 */
public class LineFormatter extends AbstractFormatter {

  /**
   * Creates a Formatter that will format data from the specified sources. T
   * 
   * @param sources
   */
  public LineFormatter(Collection<? extends DataSource> sources, String delimiter) {
    super(sources, delimiter + " ");
  }

  /**
   * @return an empty string as this formatter has no header data.
   */
  public String getHeader() {
    return "";
  }

  /**
   * Formats the data that has been added to this Formatter in
   * 
   * id: data , id: data, ...
   * 
   * @return the formatted data.
   */
  public String formatData() {
    StringBuilder builder = new StringBuilder();
    boolean first = true;
    for (Map.Entry<String, ItemFormatter> entry : keyMap.entrySet()) {
      if (!first) {
        builder.append(delimiter);
      }
      builder.append(entry.getKey());
      builder.append(": ");
      builder.append(data[entry.getValue().index]);
      first = false;
    }
    return builder.toString();
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.Formatter#getFormatType()
   */
  @Override
  public FormatType getFormatType() {
    return FormatType.LINE;
  }

  
}
