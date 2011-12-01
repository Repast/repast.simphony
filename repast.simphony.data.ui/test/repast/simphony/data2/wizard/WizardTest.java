package repast.simphony.data2.wizard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class WizardTest {
  
  public void run() {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.toFront();
    frame.requestFocus();
    List<Class<?>> agentClasses = new ArrayList<Class<?>>();
    agentClasses.add(Agent1.class);
    agentClasses.add(Agent2.class);
    
    DataSetEditorWizard wizard = new DataSetEditorWizard(agentClasses);
    wizard.showDialog(frame, "Data Set Editor");
    
    System.out.println(wizard.getModel().getDescriptor().getScheduleParameters());
    frame.dispose();
  }
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
       new WizardTest().run();
      }
    });
  }
}
