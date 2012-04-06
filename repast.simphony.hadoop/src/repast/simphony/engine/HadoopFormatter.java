/**
 * 
 */
package repast.simphony.engine;

import java.util.Collection;
import java.util.Iterator;

import repast.simphony.data2.AbstractFormatter;
import repast.simphony.data2.DataSource;
import repast.simphony.data2.FormatType;

/**
 * @author Nick Collier
 */
public class HadoopFormatter extends AbstractFormatter {
  
  /**
   * Creates a Formatter that will format data form the specified sources. The
   * ids of the sources will become the header info.
   * 
   * @param sources
   */
  public HadoopFormatter(Collection<? extends DataSource> sources) {
    super(sources, "\t");
  }

  /**
   * Gets the ids of the formatted data sources, quoted and separated by a 
   * tab character.
   * 
   * @return the ids of the formatted data sources, quoted and separated by a
   * tab character.
   *         
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

  /* (non-Javadoc)
   * @see repast.simphony.data2.Formatter#formatData()
   */
  @Override
  public String formatData() {
    StringBuilder builder = new StringBuilder();
    for (String id : keyMap.keySet()) {
      if (builder.length() > 0) builder.append(",");
      builder.append(id);
      builder.append("\t");
      builder.append(data[keyMap.get(id).index]);
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
