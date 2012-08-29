/**
 * 
 */
package repast.simphony.batch;

/**
 * Constants used by distributed batch code.
 * 
 * @author Nick Collier
 */
public interface BatchConstants {
  
  String BATCH_RUN_PARAM_NAME = BatchConstants.class.getName() + "batch.name";
  
  // property names
  String BATCH_PARAM_FILE_PN = "batch.parameter.file";
  String UNROLLED_BATCH_PARAM_FILE_PN = "unrolled.batch.parameter.file";
  String SCENARIO_DIRECTORY_PN = "scenario.directory";
  String WORKING_DIRECTORY_PN = "working.directory";
  String REPAST_LIB_DIRECTORY_PN = "repast.lib.directory";

  String INSTANCE_COUNT_PN = "instance.count";
  
  String INSTANCE_DIR_PREFIX = "instance_";
  String DONE_FILE_NAME = "DONE";
  String STATUS_OUTPUT_FILE = "status_output.properties";
  
  String PARAM_MAP_SUFFIX = "batch_param_map";
  

}
