/**
 * 
 */
package repast.simphony.data2.engine;

/**
 * Descriptor for File type sinks.
 * 
 * @author Nick Collier
 */
public class FileSinkDescriptor extends AbstractTextSinkDescriptor {

  private String fileName = "ModelOutput.txt";
  private boolean addTimeStamp = true;

  /**
   * Creates a {@link FileSinkDescriptor}
   * 
   * @param name
   */
  public FileSinkDescriptor(String name) {
    super(name);
  }

  /**
   * @return the fileName
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @param fileName
   *          the fileName to set
   */
  public void setFileName(String fileName) {
    if (!this.fileName.equals(fileName)) {
      this.fileName = fileName;
      scs.fireScenarioChanged(this, "filename");
    }
  }

  /**
   * @return the addTimeStamp
   */
  public boolean isAddTimeStamp() {
    return addTimeStamp;
  }

  /**
   * @param addTimeStamp
   *          the addTimeStamp to set
   */
  public void setAddTimeStamp(boolean addTimeStamp) {
    if (addTimeStamp != this.addTimeStamp) {
      this.addTimeStamp = addTimeStamp;
      scs.fireScenarioChanged(this, "timestamp");
    }
  }
}
