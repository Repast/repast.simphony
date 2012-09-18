/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author Nick Collier
 */
public class Main {
  
  public void run() {
    JFrame frame = new JFrame();
    frame.setLayout(new BorderLayout());
    frame.add(new MainPanel().getPanel(), BorderLayout.CENTER);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(650, 500);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
        Properties props = new Properties();
        File in = new File("../repast.simphony.distributed.batch/config/SSH.MessageCenter.log4j.properties");
        props.load(new FileInputStream(in));
        PropertyConfigurator.configure(props);
        
        new Main().run();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      }
    });
  }

}
