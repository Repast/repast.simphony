package repast.simphony.batch.ssh;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Parses a Sessions properties file into individual Session instances.
 * 
 * @author Nick Collier
 */
public class SessionPropsParser {

  /**
   * Parse the Sessions defined in the specified file into a List of Session
   * objects.
   * 
   * @param file
   * @return the parsed list.
   * 
   * @throws IOException
   */
  public List<? extends Session> parse(String file) throws IOException {
    Properties props = new Properties();
    props.load(new FileReader(file));
    return init(props);
  }

  public List<? extends Session> parse(Properties props) throws IOException {
    return init(props);
  }

  private List<? extends Session> init(Properties props) throws IOException {
    SessionBuilder builder = new SessionBuilder();
    for (Object key : props.keySet()) {
      String[] vals = key.toString().trim().split("\\.");
      checkVals(key.toString(), vals);
      String type = vals[0].trim();
      int id = Integer.parseInt(vals[1].trim());
      if (type.equals("remote"))
        setRemote(builder, id, vals[2], props.get(key).toString());
      else if (type.equals("local"))
        setLocal(builder, id, vals[2], props.get(key).toString());
    }

    return new ArrayList<Session>(builder.getSessions());
  }

  private void setRemote(SessionBuilder builder, int id, String type, String val)
      throws IOException {
    if (type.equals("user")) {
      builder.addUser(id, val.trim());
    } else if (type.equals("host")) {
      builder.addHost(id, val.trim());
    } else if (type.equals("ssh_key_file")) {
      builder.addKeyFile(id, val.trim());
    } else if (type.equals("instances")) {
      try {
        int instances = Integer.parseInt(val.trim());
        if (instances < 1) {
          throw new IOException(String.format("Invalid number of instances for %d", id));
        }
        builder.addInstancesToRemote(id, instances);
      } catch (NumberFormatException ex) {
        throw new IOException(String.format("Invalid number format for property for %d", id));
      }
    }
  }

  private void setLocal(SessionBuilder builder, int id, String type, String val) throws IOException {
    if (type.equals("working_directory")) {
      builder.addWorkingDirectory(id, val.trim());
    } else if (type.equals("instances")) {
      try {
        int instances = Integer.parseInt(val.trim());
        if (instances < 1) {
          throw new IOException(String.format("Invalid number of instances for %d", id));
        }
        builder.addInstancesToLocal(id, instances);
      } catch (NumberFormatException ex) {
        throw new IOException(String.format("Invalid number format for property for %d", id));
      }
    }
  }

  private void checkVals(String key, String[] vals) throws IOException {
    if (vals.length != 3)
      throw new IOException("Invalid properties configuration for '" + key
          + "': expected remote.X.[host|user|instances|ssh_key_file] or local.X.[working_directory | instances]");
    if (!(vals[0].equals("remote") || vals[0].equals("local")))
      throw new IOException("Invalid  properties configuration:" + key);

    try {
      Integer.parseInt(vals[1].trim());
    } catch (NumberFormatException ex) {
      throw new IOException("Invalid remote properties configuration:" + key);
    }

    if (vals[0].equals("remote")) {
      if (!(vals[2].equals("user") || vals[2].equals("host") || vals[2].equals("instances") || vals[2].equals("ssh_key_file"))) {
        throw new IOException("Invalid remote properties configuration:" + key);
      } else if (vals[0].equals("local")) {
        if (!(vals[2].equals("working_directory") || vals[2].equals("instances"))) {
          throw new IOException("Invalid local properties configuration:" + key);
        } else {
          throw new IOException("Invalid properties configuration:" + key);
        }
      }
    }
  }
}
