/**
 * 
 */
package repast.simphony.systemdynamics.handlers;

import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.SystemModel;

/**
 * Runs the EditSubscriptsDialog.
 * 
 * @author Nick Collier
 */
public class DialogTest {

  public void run() {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.pack();
    shell.open();

    SystemModel model = SDModelFactory.eINSTANCE.createSystemModel();
    Subscript sub = SDModelFactory.eINSTANCE.createSubscript();
    sub.setName("cities");
    sub.getElements().add("boston");
    sub.getElements().add("new york");
    model.getSubscripts().add(sub);
    EditSubscriptsDialog dialog = new EditSubscriptsDialog(shell, model);
    dialog.open();
    
    List<Subscript> subs = dialog.getSubscripts();
    for (Subscript sb : subs) {
      System.out.println(sb.getName() + ": " + sb.getElements());
    }

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    
    
    display.dispose();
  }

  public static void main(String[] args) {
    new DialogTest().run();
  }

}
