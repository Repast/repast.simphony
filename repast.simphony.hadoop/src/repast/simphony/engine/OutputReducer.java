/**
 * 
 */
package repast.simphony.engine;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/**
 * @author Nick Collier
 */
public class OutputReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.hadoop.mapred.Reducer#reduce(java.lang.Object,
   * java.util.Iterator, org.apache.hadoop.mapred.OutputCollector,
   * org.apache.hadoop.mapred.Reporter)
   */
  @Override
  public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output,
      Reporter reporter) throws IOException {

    String out = "";
    while (values.hasNext()) {
      if (out.length() > 0) out += ",";
      out += values.next().toString();
    }
    output.collect(key, new Text(out));

  }
}
