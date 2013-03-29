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

  public void run(String bundleDir) throws IOException, ClassNotFoundException,
      IllegalArgumentException, SecurityException, IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {

   
    BundleFinder finder = new BundleFinder();
    BundleData bd = new BundleData("repast.simphony.distributed.batch.ui", new Version("2.0.1"));
    // find the distributed.batch.ui bundle
    finder.findBundle(new File(bundleDir), bd);
    System.setProperty("eclipse.home", bd.getLocation().getParentFile().getParent());
    System.setProperty("plugins.version", PLUGINS_VERSION);
    
    finder.findRequiredBundles(new File(bundleDir), bd);

    String propsFile = null;
    List<URL> urls = new ArrayList<URL>();
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

      for (String path : bundle.classPathEntries()) {
        urls.add(new File(bundle.getLocation(), path).toURI().toURL());
      }
    }
  
    URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[0]), this.getClass()
        .getClassLoader());
    Class<?> clazz = Class.forName("repast.simphony.batch.gui.Main", true, loader);
    clazz.getMethod("main", String[].class).invoke(null, new Object[] {new String[] { propsFile }});
    //System.out.println(method.getParameterTypes()[0]);
  }

  public static void main(String[] args) {
    try {
      new StandAloneMain().run(args[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
