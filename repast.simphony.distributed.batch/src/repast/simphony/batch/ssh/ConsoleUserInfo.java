/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jcraft.jsch.UserInfo;

/**
 * Console based user info.
 * 
 * @author Nick Collier
 */
public class ConsoleUserInfo implements UserInfo {

  private String passphrase;
  
  /*
   * (non-Javadoc)
   * 
   * @see com.jcraft.jsch.UserInfo#getPassphrase()
   */
  @Override
  public String getPassphrase() {
    return passphrase;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.jcraft.jsch.UserInfo#getPassword()
   */
  @Override
  public String getPassword() {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.jcraft.jsch.UserInfo#promptPassphrase(java.lang.String)
   */
  @Override
  public boolean promptPassphrase(String prompt) {
    if (System.console() != null) {
      char[] pp = System.console().readPassword("%s: ", prompt);
      passphrase = new String(pp);
    } else {
      System.out.print(prompt + ": ");
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new InputStreamReader(System.in));
        passphrase = reader.readLine();
      } catch (IOException ex) {
        if (reader != null) 
          try {
            reader.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
      }
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.jcraft.jsch.UserInfo#promptPassword(java.lang.String)
   */
  @Override
  public boolean promptPassword(String prompt) {
    System.out.print(prompt + ": ");
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(System.in));
      String res = reader.readLine();
      return res.trim().equalsIgnoreCase("yes");
    } catch (IOException ex) {
      if (reader != null) 
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.jcraft.jsch.UserInfo#promptYesNo(java.lang.String)
   */
  @Override
  public boolean promptYesNo(String arg0) {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.jcraft.jsch.UserInfo#showMessage(java.lang.String)
   */
  @Override
  public void showMessage(String msg) {
    System.console().printf("%s", msg);
  }
}
