package repast.simphony.visad;

import org.apache.commons.lang3.SystemUtils;
import repast.simphony.data.analysis.AnalysisPluginRunner;

/**
 * A wizard for executing VisAD on a file outputter's output.
 *
 * @author Eric Tatara
 * @author Jerry Vos
 */

public class RunVisadModel extends AnalysisPluginRunner {

  private static String path;

  static {
    if (SystemUtils.IS_OS_MAC) 
    	path = "/Applications/VisAD/visad.jar";
    else if (SystemUtils.IS_OS_WINDOWS)
    	path = "C:\\Program Files\\visad\\visad.jar";
    else
    	path = "/usr/local/bin/VisAD/visad.jar";
  }

  public RunVisadModel() {
    super("VisAD", path, "license.txt", new VisadWizard());
  }
}
