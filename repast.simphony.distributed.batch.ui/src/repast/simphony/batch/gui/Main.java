/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author Nick Collier
 */
public class Main {
  
  private static final String PROPS_FILE = "-props_file";
  private static final String MODEL_DIR = "-model_dir";
  
  private static String[] getArgs(String[] args) {
    String[] ret = {"", ""};
    if (args.length == 1 || args.length == 3 || args.length > 4) {
      return null;
    }
    
    if (args.length == 2) {
      String val = args[0];
      if (val.equals(PROPS_FILE)) ret[0] = args[1];
      else if (val.equals(MODEL_DIR)) ret[1] = args[1];
      else return null;
    } else if (args.length == 4) {
      String val = args[0];
      if (val.equals(PROPS_FILE)) ret[0] = args[1];
      else if (val.equals(MODEL_DIR)) ret[1] = args[1];
      else {
        return null;
      }
      
      val = args[2];
      if (val.equals(PROPS_FILE)) ret[0] = args[3];
      else if (val.equals(MODEL_DIR)) ret[1] = args[3];
      else {
        return null;
      }
    }
    
    return ret;
  }
  
  private static void usage() {
    System.out.println("Usage: Main " + PROPS_FILE + " X " + MODEL_DIR + " Y \n\t" +
                "where X is the location of the log4j properties file and Y is the model project directory. Both arguments are optional");
    
  }

  public void run(String modelDir) {
    JFrame frame = new JFrame();
    frame.setLayout(new BorderLayout());
    if (modelDir.length() > 0) 
      frame.add(new MainPanel(new File(modelDir)), BorderLayout.CENTER);
    else
      frame.add(new MainPanel(), BorderLayout.CENTER);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(650, 650);
    frame.setMinimumSize(new Dimension(600, 430));
    frame.setTitle("Repast Simphony Batch");
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  public static void main(final String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          String[] ret = Main.getArgs(args);
          if (ret == null) {
            Main.usage();
            return;
          }
          
          String propsFile = ret[0];
          if (propsFile.length() == 0)
            propsFile = "../repast.simphony.distributed.batch/config/SSH.MessageCenter.log4j.properties";
          
          Properties orig = new Properties();
          File in = new File(propsFile);
          orig.load(new FileInputStream(in));
          Properties props = new Properties(orig);
          // replace any references to MessageCenterLayout with PatternLayout as
          // MessageCenterLayout is incompatible with log4j-2
          for (Entry<Object, Object> entry : orig.entrySet()) {
              if (entry.getValue().toString().trim().equals("simphony.util.messages.MessageCenterLayout")) {
                  // System.out.println("Replacing: " + entry.getKey());
                  props.put(entry.getKey(), "org.apache.log4j.PatternLayout");
              }
          }
          PropertyConfigurator.configure(props);
          
          new Main().run(ret[1]);
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    });
  }

}
