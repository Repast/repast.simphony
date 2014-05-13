/**
 * 
 */
package repast.simphony.batch.ssh;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Creates the patterns for a default output file sink
 * file name. These patterns are used to identify the 
 * default simphony output.
 * 
 * @author Nick Collier
 */
public class DefaultOutputPatternCreator {
  
  private String name, extension;
  private String timestamp;
  
  /**
   * Creates a DefaultOutputPatternCreator that will create output patterns for
   * the specified basename. For example, a basename of ModelOutput would correspond
   * to instance output of ModelOutput.X.csv where X is a timestamp.
   */
  public DefaultOutputPatternCreator(String basename, boolean hasTimestamp) {
    int index = basename.lastIndexOf(".");
    if (index != -1) {
      name = basename.substring(0, index);
      extension = basename.substring(index + 1);
    } else {
      name = basename;
      extension = "";
    }
    if (hasTimestamp) 
      this.timestamp = "." + new SimpleDateFormat("yyyy.MMM.dd.HH_mm_ss").format(new Date());
    else 
      this.timestamp = "";
  }
  
  private String getFinalFileName() {
    return name + timestamp + (extension.length() > 0 ? "." + extension : "");
  }
  
  /**
   * Gets the OutputPattern for the parameter map file.
   * 
   * @return the OutputPattern for the parameter map file. 
   */
  public OutputPattern getParamMapPattern() {
    String fname = cleanMatchFile(name);
    String pattern = "{**/,}" + fname + "*" + ".batch_param_map";
    if (extension.length() > 0) {
      pattern += "." + cleanMatchFile(extension);
    }
    
    OutputPattern outPattern = new OutputPattern();
    outPattern.setPattern(pattern);
    outPattern.setPath(getFinalParamMapFileName());
    outPattern.setHeader(true);
    outPattern.setConcatenate(true);
    return outPattern;
  }
  
  /**
   * Gets the OutputPattern for the file sink output.
   * 
   * @return the OutputPattern for the file sink output.
   */
  public OutputPattern getFileSinkOutputPattern() {
    String fname = cleanMatchFile(name);
    String pattern = "{**/,}" + fname + "*";
    if (extension.length() > 0) {
      pattern += "." + cleanMatchFile(extension);
    }
    
    OutputPattern outPattern = new OutputPattern();
    outPattern.setPattern(pattern);
    outPattern.setPath(getFinalFileName());
    outPattern.setHeader(true);
    outPattern.setConcatenate(true);
    return outPattern;
  }
  
  private String getFinalParamMapFileName() {
    return name + timestamp + ".batch_param_map" + (extension.length() > 0 ? "." + extension : "");
  }
  
  private String cleanMatchFile(String filename) {
    String ret = filename.replace("*", "\\*");
    ret = ret.replace("?", "\\?");
    ret = ret.replace("{", "\\{");
    ret = ret.replace("}", "\\}");
    ret = ret.replace("\\", "\\\\");
    return ret;
  }
}
