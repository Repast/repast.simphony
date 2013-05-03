package repast.simphony.R;

import org.apache.commons.lang3.SystemUtils;
import repast.simphony.data.analysis.AnalysisPluginRunner;

/**
 * A wizard for executing R on a file outputter's output.
 * 
 * @author Eric Tatara test
 * @author Jerry Vos
 */

public class RunRModel extends AnalysisPluginRunner  {

  private static String path;

  static {
    if (SystemUtils.IS_OS_MAC)
    	path = "/Applications/R.app";
    else if (SystemUtils.IS_OS_WINDOWS)
    	path = "C:\\R-3.0.0\\bin\\x64\\RGui.exe"; 
    else
    	path = "/usr/bin/R";
  }

  public RunRModel(){
		super("R", path, "license.txt",new RWizard());
		
	}
}
