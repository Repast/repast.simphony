/**
 * 
 */
package repast.simphony.engine;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 
 * @author Nick Collier
 */
public class BatchJob extends Configured implements Tool {

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.hadoop.util.Tool#run(java.lang.String[])
   */
  @Override
  public int run(String[] args) throws Exception {
    Configuration conf = getConf();
    JobConf job = new JobConf(conf, getClass());

    Path in = new Path(args[0]);
    Path out = new Path(args[1]);
    FileInputFormat.setInputPaths(job, in);
    FileOutputFormat.setOutputPath(job, out);
    
    DistributedCache.addCacheFile(new Path("./test_data/batch_params.xml").toUri(), conf);
    
    //DistributedCache.addFileToClassPath(new Path("./tmp_jars/repast.simphony.batch.jar"), conf); //, FileSystem.getLocal(conf));

    job.setJobName("RS Batch Job");
    job.setMapperClass(InstanceRunner.class);
    job.setReducerClass(OutputReducer.class);
    job.setInputFormat(KeyValueTextInputFormat.class);

    job.setOutputFormat(TextOutputFormat.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    JobClient.runJob(job);

    return 0;
  }

  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new Configuration(), new BatchJob(), args);

    System.exit(res);
  }

}
