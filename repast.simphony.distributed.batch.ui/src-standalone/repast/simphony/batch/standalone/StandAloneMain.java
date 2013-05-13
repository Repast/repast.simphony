/**
 * 
 */
package repast.simphony.batch.standalone;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick Collier
 */
public class StandAloneMain {
  
  private static final String PLUGINS_VERSION = "2.0.1";
  private static final String PLUGIN_DIR = "-plugin_dir";
  private static final String MODEL_DIR = "-model_dir";
  
  
  private static String[] getArgs(String[] args) {
    String[] ret = {"", ""};
    if (args.length == 1 || args.length == 3 || args.length > 4) {
      return null;
    }
    
    if (args.length == 2) {
      String val = args[0];
      if (val.equals(PLUGIN_DIR)) ret[0] = args[1];
      else if (val.equals(MODEL_DIR)) ret[1] = args[1];
      else return null;
    } else if (args.length == 4) {
      String val = args[0];
      if (val.equals(PLUGIN_DIR)) ret[0] = args[1];
      else if (val.equals(MODEL_DIR)) ret[1] = args[1];
      else {
        return null;
      }
      
      val = args[2];
      if (val.equals(PLUGIN_DIR)) ret[0] = args[3];
      else if (val.equals(MODEL_DIR)) ret[1] = args[3];
      else {
        return null;
      }
    }
    
    return ret;
  }
  
  private static void usage() {
    System.out.println("Usage: StandAloneMain " + PLUGIN_DIR + " X " + MODEL_DIR + " Y \n\t" +
    		"where X is the location of the eclipse plugin directory and Y is the model project directory. Both arguments are optional");
    
  }

  public void run(String bundleDir, String modelDir) throws IOException, ClassNotFoundException,
      IllegalArgumentException, SecurityException, IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {

   
    BundleFinder finder = new BundleFinder();
    BundleData bd = new BundleData("repast.simphony.distributed.batch.ui", new Version(PLUGINS_VERSION));
    // find the distributed.batch.ui bundle
    finder.findBundle(new File(bundleDir), bd);
    
    System.setProperty("eclipse.home", bd.getLocation().getParentFile().getParent());
    System.setProperty("plugins.version", PLUGINS_VERSION);
    
    finder.findRequiredBundles(new File(bundleDir), bd);

    String propsFile = null;
    List<URL> urls = new ArrayList<URL>();
    
    urls.add(new File(bd.getLocation(), "bin").toURI().toURL());
    for (String path : bd.classPathEntries()) {
      urls.add(new File(bd.getLocation(), path).toURI().toURL());
    }

    for (BundleData bundle : bd.requiredBundles()) {
      if (bundle.getName().equals("repast.simphony.distributed.batch")) {
        propsFile = new File(bundle.getLocation(), "config/SSH.MessageCenter.log4j.properties")
            .getAbsolutePath();
      }
      if (bundle.getLocation().getName().endsWith("jar")) {
        urls.add(bundle.getLocation().toURI().toURL());
      }

      // this is the classpath part of the manifest.mf
      for (String path : bundle.classPathEntries()) {
        if (path.equals(".")) path = "bin";
        urls.add(new File(bundle.getLocation(), path).toURI().toURL());
      }
    }
    
    BundleData rsRuntime = new BundleData("repast.simphony.runtime", new Version(PLUGINS_VERSION));
    finder.findBundle(new File(bundleDir), rsRuntime);
    for (String path : rsRuntime.classPathEntries()) {
      if (path.contains("saf.core.runtime"))
        urls.add(new File(rsRuntime.getLocation(), path).toURI().toURL());
    }
    
    BundleData batch = new BundleData("repast.simphony.batch", new Version(PLUGINS_VERSION));
    finder.findBundle(new File(bundleDir), batch);
    urls.add(new File(batch.getLocation(), "bin").toURI().toURL());
   
    URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[0]), this.getClass()
        .getClassLoader());
    Class<?> clazz = Class.forName("repast.simphony.batch.gui.Main", true, loader);
    clazz.getMethod("main", String[].class).invoke(null, new Object[] {new String[] { "-props_file",
        propsFile, "-model_dir",  modelDir }});
    //System.out.println(method.getParameterTypes()[0]);
  }

  public static void main(String[] args) {
    try {
      String[] vals = StandAloneMain.getArgs(args);
      if (vals == null) {
        StandAloneMain.usage();
        return;
      }
      
      String bundleDir = vals[0];
      String modelDir = vals[1];
      if (bundleDir.length() == 0) {
        URL runtimeSource = StandAloneMain.class.getProtectionDomain().getCodeSource().getLocation();
        File location = new File(runtimeSource.toURI());
        if (location.getName().endsWith("batch_runner.jar")) {
          // assume this is being run from executable jar just below the eclipse dir
          bundleDir = location.getParent() + "/eclipse/plugins";
        } else {
          File dir = location.getParentFile().getParentFile();
          bundleDir = dir.getAbsolutePath();
        }
      }
      new StandAloneMain().run(bundleDir, modelDir);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
