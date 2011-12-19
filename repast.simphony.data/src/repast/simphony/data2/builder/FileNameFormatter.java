/**
 * 
 */
package repast.simphony.data2.builder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that will format a file name by optionally adding a timestamp and
 * renaming any existing files with same name.
 * 
 * @author Nick Collier
 */
public class FileNameFormatter {

  private String filename;
  private boolean formatFile = false;

  private static DateFormat format = new SimpleDateFormat("yyyy.MMM.dd.HH_mm_ss");

  public FileNameFormatter(String file, boolean addTimeStamp) {
    filename = file;
    if (addTimeStamp)
      formatFile = true;
  }

  /**
   * Gets the formatted file name.
   * 
   * @return the formatted file name.
   */
  public String getFilename() {
    if (formatFile) {
      formatFile = false;
      formatFilename();
    }
    return filename;
  }
  
  /**
   * Gets the file name with the suffix append to it.
   * 
   * @param suffix
   * @return
   */
  public String getFilename(String suffix) {
    String name = getFilename();
    int index = name.lastIndexOf(".");
    if (index != -1) {
      name = name.substring(0, index) + "." + suffix + name.substring(index, name.length());
    } else {
      name = name + "." + suffix;
    }
    return name;
  }

  private void formatFilename() {

    String ts = format.format(new Date());
    int index = filename.lastIndexOf(".");
    if (index != -1) {
      filename = filename.substring(0, index) + "." + ts + filename.substring(index, filename.length());
    } else {
      filename = filename + "." + ts;
    }

    if (filename.trim().startsWith("~")) {
      filename = filename.replace("~", System.getProperty("user.home"));
    }
  }
}
