package repast.simphony.eclipse;

import java.io.File;
import java.net.URI;
import java.net.URL;

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
        URI uri = FileLocator.resolve(url).toURI();
        JavaCore.setClasspathVariable(var, new Path(new File(uri).getAbsolutePath()), null);
      } catch (Exception ex) {
        RepastSimphonyPlugin.getInstance().log(ex);
      }

    }
  }
}
