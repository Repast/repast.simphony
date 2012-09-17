/**
 * 
 */
package repast.simphony.batch.ssh;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.jcraft.jsch.UserInfo;

/**
 * @author Nick Collier
 */
public class GUIUserInfo implements UserInfo {

  private String passphrase;
  private JTextField passphraseField = new JPasswordField(20);

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
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.jcraft.jsch.UserInfo#promptPassphrase(java.lang.String)
   */
  @Override
  public synchronized boolean promptPassphrase(final String message) {
    if (SwingUtilities.isEventDispatchThread()) {
      Object[] ob = { passphraseField };
      int result = JOptionPane.showConfirmDialog(null, ob, message, JOptionPane.OK_OPTION);
      if (result == JOptionPane.OK_OPTION) {
        passphrase = passphraseField.getText();
        return true;
      } else {
        return false;
      }
    } else {
      try {
        SwingUtilities.invokeAndWait(new Runnable() {
          public void run() {
            Object[] ob = { passphraseField };
            int result = JOptionPane.showConfirmDialog(null, ob, message, JOptionPane.OK_OPTION);
            if (result == JOptionPane.OK_OPTION) {
              passphrase = passphraseField.getText();
            }
      
          }
        });
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      return true;
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.jcraft.jsch.UserInfo#promptPassword(java.lang.String)
   */
  @Override
  public boolean promptPassword(String arg0) {
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

  }

}
