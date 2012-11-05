/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

import com.jcraft.jsch.UserInfo;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 */
public class GUIUserInfo implements UserInfo {

  private String passphrase;
  private Window window;
  private JPasswordField fld;
  private boolean canceled;

  public GUIUserInfo(Window window) {
    this.window = window;
  }
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
  public synchronized boolean promptPassphrase(String prompt) {
    ShowDialog sd = new ShowDialog(createDialog(prompt));
    if (SwingUtilities.isEventDispatchThread()) {
      sd.run();
    } else {
      try {
        SwingUtilities.invokeAndWait(sd);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    
    if (!canceled) {
      passphrase = new String(fld.getPassword());
    }
    
    return !canceled;
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
  public boolean promptYesNo(String msg) {
    int ret = JOptionPane.showConfirmDialog(window, msg, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
    return ret == JOptionPane.YES_OPTION;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.jcraft.jsch.UserInfo#showMessage(java.lang.String)
   */
  @Override
  public void showMessage(String msg) {
    

  }
  
  private JDialog createDialog(String prompt) {
    final JDialog dialog = new JDialog(window);
    dialog.setLayout(new BorderLayout());
    dialog.setModal(true);
    
    FormLayout layout = new FormLayout("5dlu, pref:grow, 3dlu, center:pref, 3dlu, center:pref", 
        "");
    DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
    formBuilder.setDefaultDialogBorder();
    formBuilder.append(new JLabel(prompt + ":"), 6);
    formBuilder.nextLine();
    formBuilder.setLeadingColumnOffset(1);
    fld = new JPasswordField();
    formBuilder.append(fld, 5);
    formBuilder.nextLine();
    
    @SuppressWarnings("serial")
    Action okAction = new AbstractAction("OK") {
      public void actionPerformed(ActionEvent evt) {
        canceled = false;
        dialog.dispose();
      }
    };
    
    JButton ok = new JButton("OK");
    ok.setAction(okAction);
    fld.setAction(okAction);
    
    JButton cancel = new JButton("Cancel");
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        canceled = true;
        dialog.dispose();
      }
    });
    
    canceled = true;
    formBuilder.setLeadingColumnOffset(3);
    formBuilder.append(ok, cancel);
    dialog.add(formBuilder.getPanel(), BorderLayout.CENTER);
    
    dialog.pack();
    java.awt.Dimension d = dialog.getPreferredSize();
    dialog.setPreferredSize(new java.awt.Dimension(d.width + 60, d.height));
    dialog.setLocationRelativeTo(window);
    
    return dialog;
  }
  
  private static class ShowDialog implements Runnable {
    JDialog dialog;
    
    public ShowDialog(JDialog dialog) {
      this.dialog = dialog;
    }
    
    public void run() {
      dialog.setVisible(true);
    }
  }

}
