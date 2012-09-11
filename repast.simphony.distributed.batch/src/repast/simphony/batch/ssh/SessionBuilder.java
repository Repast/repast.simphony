/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds Session objects.
 * 
 * @author Nick Collier
 */
public class SessionBuilder {
  
  private Map<Integer, RemoteSession> remoteMap = new HashMap<Integer, RemoteSession>();
  private LocalSession localSession = null;
  
  private RemoteSession getRemote(int id) {
    RemoteSession session = remoteMap.get(id);
    if (session == null) {
      session = new RemoteSession();
      remoteMap.put(id, session);
    }
    return session;
  }
  
  public void addHost(int id, String host) throws IOException {
    RemoteSession session = getRemote(id);
    if (session.host != null) throw new IOException(String.format("Duplicate host property for remote %d", id));
    session.host = host;
  }
  
  public void addInstancesToRemote(int id, int instances) throws IOException {
    RemoteSession session = getRemote(id);
    if (session.instances != 0) throw new IOException(String.format("Duplicate instances property for remote %d", id));
    session.instances = instances;
  }
  
  public void addInstancesToLocal(int id, int instances) throws IOException {
    if (localSession == null) localSession = new LocalSession();
    if (localSession.instances != 0)  throw new IOException(String.format("Duplicate instances property for local %d", id));
    localSession.instances = instances;
  }
  
  public void addWorkingDirectory(int id, String workingDirectory) throws IOException {
    if (localSession == null) localSession = new LocalSession();
    if (localSession.workingDir != null)  throw new IOException(String.format("Duplicate working directory property for local %d", id));
    String wd = workingDirectory.contains("~") ? workingDirectory.replace("~", System.getProperty("user.home")) : workingDirectory;
    localSession.workingDir = wd;
  }

  public void addUser(int id, String user) throws IOException {
    RemoteSession session = getRemote(id);
    if (session.user != null) throw new IOException(String.format("Duplicate user property for remote %d", id));
    session.user = user;
  }
  
  public List<? extends Session> getSessions() throws IOException {
    for (Integer id : remoteMap.keySet()) {
      RemoteSession session = remoteMap.get(id);
      if (session.host == null || session.user == null || session.instances == 0) 
        throw new IOException(String.format("Remote %d is missing a required property", id));
    }
    
    if (localSession != null) {
      if (localSession.workingDir == null || localSession.instances == 0) 
        throw new IOException("local session is missing a required property");
    }
    
     List<Session> sessions = new ArrayList<Session>(remoteMap.values());
     if (localSession != null) sessions.add(localSession);
     return sessions;
  }
  
}
