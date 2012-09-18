package repast.simphony.batch.ssh;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * Factory for creating SSHSession objects.
 * 
 * @author Nick Collier
 */
public class SSHSessionFactory {

  private static Logger logger = Logger.getLogger(SSHSessionFactory.class);
  
  static {
    JSch.setLogger(new com.jcraft.jsch.Logger() {
      public boolean isEnabled(int priority) {
        return true;
      }
      
      public void log(int priority, String msg) {
        if (priority == com.jcraft.jsch.Logger.DEBUG) logger.debug(msg);
        else if (priority == com.jcraft.jsch.Logger.ERROR) logger.error(msg);
        else if (priority == com.jcraft.jsch.Logger.FATAL) logger.fatal(msg);
        else if (priority == com.jcraft.jsch.Logger.INFO) logger.info(msg);
        else if (priority == com.jcraft.jsch.Logger.WARN) logger.warn(msg);
      }
    });
  }
  
  private static SSHSessionFactory instance;
  
  public static void init(String sshKeyDir) {
    instance = new SSHSessionFactory(sshKeyDir);
  }
  
  public static SSHSessionFactory getInstance() {
    if (instance == null) throw new RuntimeException("SSHSessionFactory must be initialized before use.");
    return instance;
  }
  
  private String sshKeyDir;
  private Map<String, byte[]> passphrases = new HashMap<String, byte[]>();
  
  private SSHSessionFactory(String sshKeyDir) {
    this.sshKeyDir = sshKeyDir;
  }
  

  public SSHSession create(RemoteSession remote) throws JSchException {
    JSch jsch = new JSch();
    
    byte[] passphrase = passphrases.get(remote.getKeyFile());
    if (passphrase != null) jsch.addIdentity(sshKeyDir + "/" + remote.getKeyFile(), passphrase);
    else jsch.addIdentity(sshKeyDir + "/" + remote.getKeyFile());
    jsch.setKnownHosts(sshKeyDir + "/known_hosts");
    Session session = jsch.getSession(remote.getUser(), remote.getHost());
    //UserInfo userInfo = null;
    //if (Boolean.getBoolean("use.gui"))  userInfo = new GUIUserInfo();
    //else 
    UserInfo userInfo = new ConsoleUserInfo();
    session.setUserInfo(userInfo);
    session.connect();
    
    if (passphrase == null) {
      passphrase = userInfo.getPassphrase().getBytes();
      passphrases.put(remote.getKeyFile(), passphrase);
    }
    
    return new SSHSession(session);

  }
 
}
