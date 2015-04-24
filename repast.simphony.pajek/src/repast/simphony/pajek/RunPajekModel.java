package repast.simphony.pajek;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.SystemUtils;

import repast.simphony.data.analysis.AnalysisPluginRunner;

/**
 * A wizard for executing Pajek on a file outputter's output.
 *
 * @author Michael J. North
 * @author Eric Tatara
 * @author Jerry Vos
 */

public class RunPajekModel extends AnalysisPluginRunner {

  private static String path;

  static {
    if (SystemUtils.IS_OS_WINDOWS)
    	path = "C:\\Pajek\\Pajek.exe";
    else
    	path = "";
  }

  public RunPajekModel() {
    super("Pajek", path, "license.txt", new PajekWizard());
  }
  
  @Override
  public void actionPerformed(ActionEvent e){
  	if (!SystemUtils.IS_OS_WINDOWS) {
  		JOptionPane.showMessageDialog(null,
  		    "Pajek is only available for Windows.",
  		    "Pajek warning",
  		    JOptionPane.WARNING_MESSAGE);
  	}
  	else{
  		super.actionPerformed(e);
  	}
  }
}
