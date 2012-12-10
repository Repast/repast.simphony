/**
 * 
 */
package repast.simphony.batch.parameter;

import java.io.IOException;
import java.net.URI;

import repast.simphony.batch.BatchConstants;
import repast.simphony.parameter.DefaultParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.xml.XMLSweeperProducer;

/**
 * Parses a batch parameter line and into a Parameters object.
 * 
 * @author Nick Collier
 */
public class ParameterLineParser {
  
  private Parameters params;
  
  /**
   * Creates a ParameterLineParser for parsing parameter lines produced from the 
   * specified parameters file. The file is used as a schema for creating the parameters. 
   * 
   * @param batchParamsFile
   */
  public ParameterLineParser(URI batchParamsFile) throws IOException {
    XMLSweeperProducer producer = new XMLSweeperProducer(batchParamsFile.toURL());
    params = producer.getParameters();
  }
  
  /**
   * Parses the parameters line and returns a Parameters object initialized with those parameters.
   * The line has the format R\tP1\tV1,P2\tV2,P3\tV3,... R is the run number followed by a tab. P* and
   * V* is a parameter name and value pair which are separated from each other by a tab and from other PV
   * pairs by a comma delimeter. The run number will be a parameter with the name BatchConstants.BATCH_RUN_PARAM_NAME
   * and of the Integer type.
   *  
   * @param line
   * 
   * @return the initialiazed Parameters object
   */
  public Parameters parse(String line) {
    String runNum = line.substring(0, line.indexOf("\t")).trim();
    DefaultParameters params = new DefaultParameters(this.params);
    params.addParameter(BatchConstants.BATCH_RUN_PARAM_NAME, "Batch Run Number", Integer.class , 
        Integer.valueOf(runNum), true);
    
    line = line.substring(line.indexOf("\t") + 1, line.length());
    String[] vals = line.split(",");
    for (String val : vals) {
      String[] param = val.split("\t");
      params.setValue(param[0].trim(), param[1].trim());
    }
    
    return params;
  }
}
