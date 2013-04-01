/**
 * 
 */
package repast.simphony.batch.standalone;

/**
 * @author Nick Collier
 */
public class Version {
  
  private int major, minor, service;
  
  public Version(String version) {
    String[] parts = version.split("\\.");
    if (parts.length == 1) {
      if (parts[0].length() > 0) 
        major = Integer.parseInt(parts[0]);
    } else if (parts.length == 2) {
      major = Integer.parseInt(parts[0]);
      minor = Integer.parseInt(parts[1]);
    } else if (parts.length >= 3) {
      major = Integer.parseInt(parts[0]);
      minor = Integer.parseInt(parts[1]);
      service = Integer.parseInt(parts[2]);
    }
  }
  
  /**
   * Gets whether this version less than or equal to the
   * specified version.
   * 
   * @param other
   * @return
   */
  public boolean lessEqual(Version other) {
    if (major > other.major) return false;
    if (major < other.major) return true;
    // majors are equal
    if (minor > other.minor) return false;
    if (minor < other.minor) return true;
    // minors are equal
    if (service > other.service) return false;
    return true;
  }

  /**
   * @return the major
   */
  public int getMajor() {
    return major;
  }

  /**
   * @return the minor
   */
  public int getMinor() {
    return minor;
  }

  /**
   * @return the service
   */
  public int getService() {
    return service;
  }
  
  

}
