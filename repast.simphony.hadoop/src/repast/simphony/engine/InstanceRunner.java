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

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.hadoop.mapred.Mapper#map(java.lang.Object,
   * java.lang.Object, org.apache.hadoop.mapred.OutputCollector,
   * org.apache.hadoop.mapred.Reporter)
   */
  @Override
  public void map(Text key, Text value, OutputCollector<Text, Text> output, Reporter reporter)
      throws IOException {

    setParameters(value.toString());

    String brKey = key.toString();
    int batchRun = Integer.parseInt(brKey.substring(brKey.indexOf("_") + 1, brKey.length()));
    System.out.println("BatchRun: " + batchRun);
    
    // TODO I think we can create this in configure
    // and close it (i.e. get it to call controller.batchCleanup())
    // in close. All this depends on how the initialize() calls work out.
    HadoopBatchRunner runner = new HadoopBatchRunner(batchRun);
    runner.run(params);

    // set the parameters from the value arg which is a string like
    // pName\tpValue,....

    // split it up as a test
    String[] vals = value.toString().split(",");
    for (String val : vals) {
      output.collect(key, new Text(val));
    }
  }

  private void setParameters(String strParams) {
    String[] vals = strParams.split(",");
    for (String val : vals) {
      String[] param = val.split("\t");
      params.setValue(param[0].trim(), param[1].trim());
    }
  }

  private File findFile(String fileName, Path[] cacheFiles) {
    for (Path path : cacheFiles) {
      File file = new File(path.toString());
      if (file.getName().equals(fileName))
        return file;
    }
    return null;
  }

  private void printFile(File file, int i) throws IOException {
    for (int t = 0; t < i; t++) {
      System.out.print("\t");
    }
    System.out.println(file.getCanonicalPath());
    if (file.isDirectory()) {
      i++;
      for (File child : file.listFiles()) {
        printFile(child, i);
      }
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.apache.hadoop.mapred.MapReduceBase#configure(org.apache.hadoop.mapred
   * .JobConf)
   */
  @Override
  public void configure(JobConf conf) {
    System.err.println("Classpath: " + System.getProperty("java.class.path"));
    try {

      for (Path path : DistributedCache.getLocalCacheArchives(conf)) {
        System.out.println(path);
      }

      Path[] paths = DistributedCache.getLocalCacheFiles(conf);
      File batchParams = findFile("batch_params.xml", paths);
      
      printFile(new File("."), 0);

      XMLSweeperProducer producer = new XMLSweeperProducer(batchParams.toURI().toURL());
      params = producer.getParameters();

    } catch (IOException ex) {
      System.err.println("Exception reading distributed cache: " + ex);
    }
  }

}
