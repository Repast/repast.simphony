/**
 * 
 */
package repast.simphony.engine;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.xml.XMLSweeperProducer;

/**
 * @author Nick Collier
 */
public class InstanceRunner extends MapReduceBase implements Mapper<Text, Text, Text, Text> {
  
  private Parameters params;

  /* (non-Javadoc)
   * @see org.apache.hadoop.mapred.Mapper#map(java.lang.Object, java.lang.Object, org.apache.hadoop.mapred.OutputCollector, org.apache.hadoop.mapred.Reporter)
   */
  @Override
  public void map(Text key, Text value, OutputCollector<Text, Text> output, Reporter reporter)
      throws IOException {
    
    // set the parameters from the value arg which is a string like
    // pName\tpValue,....
    
    // split it up as a test 
    String[] vals = value.toString().split(",");
    for (String val : vals) {
      output.collect(key, new Text(val));
    }
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.mapred.MapReduceBase#configure(org.apache.hadoop.mapred.JobConf)
   */
  @Override
  public void configure(JobConf conf) {
    try {
      Path[] cacheFiles = DistributedCache.getLocalCacheArchives(conf);
      
      XMLSweeperProducer producer = new XMLSweeperProducer(new File(cacheFiles[0].toString()).toURI().toURL());
      params = producer.getParameters();
      
    } catch (IOException ex) {
      System.err.println("Exception reading distributed cache: " + ex);
    }
  }
  
  
}
