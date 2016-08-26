package repast.simphony.eclipse;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

/**
 * Initializes the StandAloneBatch classpath variable. This is used to launch
 * the stand alone batch GUI from eclipse itself.
 * 
 * @author Nick Collier
 */
public class StandAloneBatchCPInit extends ClasspathVariableInitializer {
  
  public static final String CP_VARIABLE_NAME = "STAND_ALONE_BATCH_CLASSPATH";

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.jdt.core.ClasspathVariableInitializer#initialize(java.lang.
   * String)
   */
  @Override
  public void initialize(String var) {
    Bundle bundle = Platform.getBundle("repast.simphony.distributed.batch.ui");
    if (bundle != null) {
      URL url = FileLocator.find(bundle, new Path("bin-standalone"), null);
      try {
        url = FileLocator.resolve(url);
        File f = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
        JavaCore.setClasspathVariable(var, new Path(f.getAbsolutePath()), null);
        // TODO: add jar urls for repast.simphony.runtime_${rs.version}/lib/commons-cli-1.3.1.jar and
        // repast.simphony.core_${rs.version}/lib/commons-lang3-3.1.jar
        // Look at repast.simphony.eclipse.RepastSimphonyPlugin for how to properly include these
        
//        String pluginDirectory = RepastSimphonyPlugin.getInstance().getPluginInstallationDirectory();
//        File file;
//        if (pluginDirectory.trim().equals("")) {
//          file = new Path(RepastSimphonyPlugin.JAR_PATH_RELATIVE).toFile();
//        } else {
//          file = new Path(pluginDirectory + RepastSimphonyPlugin.JAR_PATH_RELATIVE).toFile();
//        }
//        boolean inDevEnv = !file.exists();
//        
//        String[] jars = {
//        	      "/repast.simphony.runtime_" + RepastSimphonyPlugin.REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/commons-cli-1.3.1.jar",
//        	      "/repast.simphony.core_" + RepastSimphonyPlugin.REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/commons-lang3-3.1.jar"};
//        
        
      } catch (Exception ex) {
        RepastSimphonyPlugin.getInstance().log(ex);
      }

    }
  }
}
