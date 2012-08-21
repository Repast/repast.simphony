package repast.simphony.batch;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
    } else if (type.equals("password")) {
      if (remote.password != null)
        throw new IOException(String.format("Duplicate property %s for %s", type, key));
      remote.password = val.trim();
    } else if (type.equals("file")) {
      if (remote.file != null)
        throw new IOException(String.format("Duplicate property %s for %s", type, key));
      if (!new File(val.trim()).exists())
        throw new IOException(val + " for " + key + " doesn't exist");
      remote.file = val.trim();
    }
  }

  private void checkVals(String key, String[] vals) throws IOException {
    if (vals.length != 3)
      throw new IOException("Invalid remote properties configuration for '" + key
          + "': expected remote.X.[host|user|password|file]");
    if (!vals[0].equals("remote"))
      throw new IOException("Invalid remote properties configuration:" + key);

    if (!(vals[2].equals("user") || vals[2].equals("host") || vals[2].equals("password") || vals[2]
        .equals("file"))) {
      throw new IOException("Invalid remote properties configuration:" + key);
    }
  }

  private static class PRemote implements Remote {

    String host, user, password, file;

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

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.batch.Remote#getPassword()
     */
    @Override
    public String getPassword() {
      return password;
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.batch.Remote#getModelArchive()
     */
    @Override
    public String getModelArchive() {
      return file;
    }
  }
}
