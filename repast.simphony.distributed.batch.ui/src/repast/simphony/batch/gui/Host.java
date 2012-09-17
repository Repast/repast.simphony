/**
 * 
 */
package repast.simphony.batch.gui;

/**
 * Encapsulates host data.
 * 
 * @author Nick Collier
 */
public class Host {
  
  public enum Type {LOCAL, REMOTE}
  
  private Type type = Type.LOCAL;
  private String user, host, sshKeyFile = "id_rsa";
  private int instances = 1;
  
  public Host(Type type) {
    this.type = type;
    if (type == Type.LOCAL) {
      user = System.getProperty("user.name");
      host = "localhost";
    }
  }

  /**
   * @return the type
   */
  public Type getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(Type type) {
    this.type = type;
  }

  /**
   * @return the user
   */
  public String getUser() {
    return user;
  }

  /**
   * @param user the user to set
   */
  public void setUser(String user) {
    this.user = user;
  }

  /**
   * @return the host
   */
  public String getHost() {
    return host;
  }

  /**
   * @param host the host to set
   */
  public void setHost(String host) {
    this.host = host;
  }

  /**
   * @return the instances
   */
  public int getInstances() {
    return instances;
  }

  /**
   * @param instances the instances to set
   */
  public void setInstances(int instances) {
    this.instances = instances;
  }

  /**
   * @return the sshKeyFile
   */
  public String getSSHKeyFile() {
    return sshKeyFile;
  }

  /**
   * @param sshKeyFile the sshKeyFile to set
   */
  public void setSSHKeyFile(String sshKeyFile) {
    this.sshKeyFile = sshKeyFile;
  }
}
