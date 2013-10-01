/**
 * 
 */
package repast.simphony.batch.ssh;

/**
 * Encapsulates the configuration data for an output pattern.
 * 
 * @author Nick Collier
 */
public class OutputPattern {
  
  private String pattern = "", path = "";
  private boolean concatenate, header;
  
  /**
   * Gets file matching pattern.
   * 
   * @return the pattern
   */
  public String getPattern() {
    return pattern;
  }
  /**
   * Sets the glob style pattern for file matching. 
   * <ul>
   * <li>The * character matches zero or more characters of a name component
   * without crossing directory boundaries.
   * 
   * <li>The ** characters matches zero or more characters crossing directory
   * boundaries.
   * 
   * <li>The ? character matches exactly one character of a name component.
   * 
   * <li>The backslash character (\) is used to escape characters that would
   * otherwise be interpreted as special characters. The expression \\ matches a
   * single backslash and "\{" matches a left brace for example.
   * </ul>
   * 
   * For example **&#47;output/my_output.txt will match any file called
   * my_output.txt in an "output" directory where the parent of that "output"
   * directory can be anything.
   * @param pattern the pattern to set
   */
  public void setPattern(String pattern) {
    this.pattern = pattern;
  }
  /**
   * Gets the path where the found output will be written to.
   * If concatenate is true, then those files will be concatenated
   * into a file with the specified path name. Otherwise, all
   * the found files will be copied into a directory with the
   * specified path name.
   * 
   * @return the path
   */
  public String getPath() {
    return path;
  }
  /**
   * Sets the path for the found output to be written to. If
   * concatenate is true, then those files will be concatenated
   * into a file with the specified path name. Otherwise, all
   * the found files will be copied into a directory with the
   * specified path name.
   * 
   * @param path the path to set
   */
  public void setPath(String path) {
    this.path = path;
  }
  /**
   * @return true if the files that match the pattern should be
   * concatenated into a single output, otherwise false.
   */
  public boolean isConcatenate() {
    return concatenate;
  }
  /**
   * Sets whether or not to concatenate the files that match
   * pattern.
   * 
   * @param concatenate the concatenate to set
   */
  public void setConcatenate(boolean concatenate) {
    this.concatenate = concatenate;
  }
  /**
   * @return true if the files matching the pattern will have a
   * header otherwise false.
   */
  public boolean isHeader() {
    return header;
  }
  /**
   * Sets whether or not the files that match the pattern will
   * have a header. 
   * 
   * @param header the header to set
   */
  public void setHeader(boolean header) {
    this.header = header;
  }
  
  public int hashCode() {
    int result = 17;
    result = 31 * result + pattern.hashCode();
    result = 31 * result + path.hashCode();
    result = 31 * result + (concatenate ? 1 : 0);
    result = 31 * result + (header ? 1 : 0);
    
    return result;
  }
  
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (obj instanceof OutputPattern) {
      OutputPattern other = (OutputPattern)obj;
      return other.pattern.equals(this.pattern) && other.path.equals(this.path) 
          && other.concatenate == this.concatenate && other.header == this.header;
    }
    
    return false;
  }
}
