package repast.simphony.ora;

import org.apache.commons.lang3.SystemUtils;
import repast.simphony.data.analysis.AnalysisPluginRunner;

/**
 * A wizard for executing ORA on a file outputter's output.
 *
 * @author Eric Tatara
 * @author Jerry Vos
 */

public class RunORAModel extends AnalysisPluginRunner {

  private static String path;

  static {
	    if (SystemUtils.IS_OS_MAC) 
	    	path = "/Applications/ORA/";
	    else if (SystemUtils.IS_OS_WINDOWS)
	    	path = "C:\\Program Files\\ORA-NetScenes-cst-iw-64\\ORA-NetScenes-cst-iw-64.exe";
	    else
	    	path = "/usr/local/bin/ORA/";
  }

  public RunORAModel() {
    super("*ORA", path, "license.txt", new ORAWizard());
  }
}
