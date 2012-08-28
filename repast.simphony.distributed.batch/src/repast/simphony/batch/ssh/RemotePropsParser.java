package repast.simphony.batch.ssh;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import repast.simphony.batch.RunningStatus;

/**
 * Parses a remote format properties file into a Remotes object.
 * 
 * @author Nick Collier
 */
public class RemotePropsParser {

  /**
   * Parse the remote hosts defined in the specified file into a List
   * of Remote objects. 
   * 
   * @param file
   * @return the parsed list.
   * 
   * @throws IOException
   */
  public List<Remote> parse(String file) throws IOException {
    Properties props = new Properties();
    props.load(new FileReader(file));
    return init(props);
  }
  
  public List<Remote> parse(Properties props) throws IOException {
    return init(props);
  }
  
  private List<Remote> init(Properties props) throws IOException {

    Map<String, PRemote> remoteMap = new HashMap<String, PRemote>();
    for (Object key : props.keySet()) {
      String[] vals = key.toString().trim().split("\\.");
      checkVals(key.toString(), vals);
      PRemote remote = remoteMap.get(vals[1]);
      if (remote == null) {
        remote = new PRemote();
        remoteMap.put(vals[1], remote);
      }
      setRemote(remote, key.toString(), vals[2], props.get(key).toString());
    }

    return new ArrayList<Remote>(remoteMap.values());
  }

  private void setRemote(PRemote remote, String key, String type, String val) throws IOException {
    if (type.equals("user")) {
      if (remote.user != null)
        throw new IOException(String.format("Duplicate property %s for %s", type, key));
      remote.user = val.trim();
    } else if (type.equals("host")) {
      if (remote.host != null)
        throw new IOException(String.format("Duplicate property %s for %s", type, key));
      remote.host = val.trim();
    } else if (type.equals("instances")) {
      if (remote.instances != -99)
        throw new IOException(String.format("Duplicate property %s for %s", type, key));
      try {
        remote.instances = Integer.parseInt(val.trim());
        if (remote.instances < -1) {
          throw new IOException(String.format("Invalid number of instances for property %s for %s", type, key));
        }
      } catch (NumberFormatException ex) {
        throw new IOException(String.format("Invalid number format for property %s for %s", type, key));
      }
    }
  }

  private void checkVals(String key, String[] vals) throws IOException {
    if (vals.length != 3)
      throw new IOException("Invalid remote properties configuration for '" + key
          + "': expected remote.X.[host|user|instances]");
    if (!vals[0].equals("remote"))
      throw new IOException("Invalid remote properties configuration:" + key);

    if (!(vals[2].equals("user") || vals[2].equals("host") || vals[2].equals("instances"))) {
      throw new IOException("Invalid remote properties configuration:" + key);
    }
  }

  private static class PRemote implements Remote {

    String host, user, input = "";
    int instances = -99;
    
    Map<Integer, RunningStatus> stati = new HashMap<Integer, RunningStatus>();

    public int getInstances() {
      return instances;
    }
    
    /* (non-Javadoc)
     * @see repast.simphony.batch.Remote#getId()
     */
    @Override
    public String getId() {
      return user + "@" + host;
    }
   
    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.batch.Remote#getHost()
     */
    @Override
    public String getHost() {
      return host;
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.batch.Remote#getUser()
     */
    @Override
    public String getUser() {
      return user;
    }

    /* (non-Javadoc)
     * @see repast.simphony.batch.ssh.Remote#getInput()
     */
    @Override
    public String getInput() {
      return input;
    }

    /* (non-Javadoc)
     * @see repast.simphony.batch.ssh.Remote#setInput(java.lang.String)
     */
    @Override
    public void setInput(String input) {
      this.input = input;
    }

    /* (non-Javadoc)
     * @see repast.simphony.batch.ssh.Remote#setRunStatus(int, repast.simphony.batch.RunningStatus)
     */
    @Override
    public void setRunStatus(int instance, RunningStatus status) {
      stati.put(instance, status);
    }

    /* (non-Javadoc)
     * @see repast.simphony.batch.ssh.Remote#getStatus(int)
     */
    @Override
    public RunningStatus getStatus(int instance) {
      return stati.get(instance);
    }
  }
}
