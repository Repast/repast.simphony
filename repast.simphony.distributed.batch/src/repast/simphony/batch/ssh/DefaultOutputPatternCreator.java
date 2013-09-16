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
  
  public DefaultOutputPatternCreator(String basename) {
    int index = basename.lastIndexOf(".");
    if (index != -1) {
      name = basename.substring(0, index);
      extension = basename.substring(index + 1);
    } else {
      name = basename;
      extension = "";
    }
    this.timestamp = new SimpleDateFormat("yyyy.MMM.dd.HH_mm_ss").format(new Date());
  }
  
  public String getFinalFileName() {
    return name + "." + timestamp + (extension.length() > 0 ? "." + extension : "");
  }
  
  public String getParamMapPattern() {
    String fname = cleanMatchFile(name);
    String pattern = "{**/,}" + fname + "*" + ".batch_param_map";
    if (extension.length() > 0) {
      pattern += "." + cleanMatchFile(extension);
    }
    return pattern;
    
  }
  
  public String getFilePattern() {
    String fname = cleanMatchFile(name);
    String pattern = "{**/,}" + fname + "*";
    if (extension.length() > 0) {
      pattern += "." + cleanMatchFile(extension);
    }
    return pattern;
  }
  
  public String getFinalParamMapFileName() {
    return name + "." + timestamp + ".batch_param_map" + (extension.length() > 0 ? "." + extension : "");
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
