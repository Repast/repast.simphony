package repast.simphony.batch.standalone;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Nick Collier
 */
public class StandAloneMain {

  public static Options getOptions() {

    Options options = new Options();
    options.addOption("h", "help", false, "show help");
    options.addOption("hl", "headless", false, "headless batch");
    options.addOption(PLUGIN_DIR, true, "location of the eclipse plugin directory");
    options.addOption(MODEL_DIR, true,
        "location of the model project directory\n(needs to be specified if model \ndefined parameter types are used)");
    // Batch configuration file
    options.addOption("c", "batch_config", true,
        "location of the batch configuration file\n(only with -hl option)");
    // Parameters file
    options.addOption("p", "params", true,
        "location of the parameters file\n(only with -hl option)");
    // Batch parameters file
    options.addOption("b", "batch_params", true,
        "location of the batch parameters file\n(only with -hl option)");
    // Unrolled parameter file
    options.addOption("u", "upf", true,
        "location of a custom unrolled parameter file\n(only with -hl and without -r option)");
    // Scenario directory
    options.addOption("s", "scenario_dir", true,
        "location of the scenario directory\n(only with -hl option)");
    // Output directory
    options.addOption("o", "output_dir", true,
        "location of the output directory\n(only with -hl option)");
    // Run (vs. archive)
    options.addOption("r", "run", false,
        "run distributed batch\n(archive if not specified,\nonly with -hl option)");

    return options;
  }

  private static final String PLUGINS_VERSION = "2.8.0";
  private static final String PLUGIN_DIR = "plugin_dir";
  public static final String MODEL_DIR = "model_dir";
  public static final String PROPS_FILE = "props_file";

  protected File findGroovyJar(String bundleDir) {
    File latestGJar = null;
    File dir = new File(bundleDir);
    List<File> candidateGroovyAllJars = new ArrayList<>();

    for (File file : dir.listFiles()) {
      if (file.isDirectory() && file.getName().startsWith("org.codehaus.groovy")) {
        File gJar = findGroovyAllJar(file);
        if (gJar != null) {
          if (gJar.exists()) {
            candidateGroovyAllJars.add(gJar);
          }
        }
      }
    }
    if (!candidateGroovyAllJars.isEmpty()) {
      Version latest = new Version("0.0.0");
      for (File gJar : candidateGroovyAllJars) {
      	
      	// Groovy all jar format is usually: groovy-all-x.x.x-suffix.jar so need
      	//  to extract the number version x.x.x
      	
      	// remove the prefix
        String ver = gJar.getName().substring("groovy-all-".length());
        
        // remove the suffix
        int suffixIndex = ver.indexOf('-');
        
        if (suffixIndex > 0)
        	ver = ver.substring(0, suffixIndex);
        
        Version current = new Version(ver);
        if (latest.lessEqual(current)) {
          latest = current;
          latestGJar = gJar;
        }
      }
    }

    return latestGJar;
  }

  protected File findGroovyAllJar(File dir) {
    if (dir != null && dir.exists()) {
      File libDir = new File(dir, "lib");
      if (libDir.exists()) {
        File[] files = libDir.listFiles();
        for (File file : files) {
          String fileName = file.getName();
          if (fileName != null) {
            if (fileName.startsWith("groovy-all-") && 
            		!fileName.contains("sources") &&
            		!fileName.contains("javadoc")) {
              return file;
            }
          }
        }
      }
    }
    return null;
  }

  public void run(String bundleDir, String modelDir, boolean headless, String[] args)
      throws IOException, ClassNotFoundException, IllegalArgumentException, SecurityException,
      IllegalAccessException, InvocationTargetException, NoSuchMethodException {

    BundleFinder finder = new BundleFinder();
    BundleData bd = new BundleData("repast.simphony.distributed.batch.ui",
        new Version(PLUGINS_VERSION));
    // find the distributed.batch.ui bundle
    finder.findBundle(new File(bundleDir), bd);

    System.setProperty("eclipse.home", bd.getLocation().getParentFile().getParent());
    System.setProperty("plugins.version", PLUGINS_VERSION);

    finder.findRequiredBundles(new File(bundleDir), bd);

    String propsFile = null;
    List<URL> urls = new ArrayList<URL>();

    // add groovy to the classpath if its not already there
    try {
      Class<?> clazz = groovy.lang.GroovyObject.class;
    } catch (NoClassDefFoundError ex) {
      File f = findGroovyJar(bundleDir);
      if (f != null) {
        urls.add(f.toURI().toURL());
      }
    }

    // urls.add(new File(bd.getLocation(), "bin").toURI().toURL());
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
        if (path.equals("."))
          path = "bin";
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

    String[] baseArgs = new String[] { "-".concat(PROPS_FILE), propsFile };
    if (!modelDir.isEmpty()) {
      File modelDirFile = new File(modelDir);
      if (modelDirFile != null && modelDirFile.exists()) {
        urls.add(new File(modelDirFile, "bin").toURI().toURL());
        urls.add(new File(modelDirFile, "lib").toURI().toURL());
      }
    }

    URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[0]),
        this.getClass().getClassLoader());
    if (headless) {
      Class<?> clazz = Class.forName("repast.simphony.batch.gui.HeadlessMain", true, loader);
      String[] full_args = ArrayUtils.addAll(baseArgs, args);
      clazz.getMethod("main", String[].class).invoke(null, new Object[] { full_args });
    } else {
      Class<?> clazz = Class.forName("repast.simphony.batch.gui.Main", true, loader);
      if (modelDir.isEmpty()){
    	  clazz.getMethod("main", String[].class).invoke(null, new Object[] { baseArgs });
      }
      else {
    	  String[] modelArg = new String[] { "-".concat(MODEL_DIR), modelDir };
    	  String[] guiFullArgs = ArrayUtils.addAll(baseArgs, modelArg);
    	  clazz.getMethod("main", String[].class).invoke(null, new Object[] { guiFullArgs });
      }
    }

  }

  public static void main(String[] args) {
    CommandLineParser parser = new DefaultParser();
    Options options = getOptions();

    boolean headless = false;

    try {
      CommandLine line = parser.parse(options, args);
      if (line.hasOption("h")) {
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("batch_runner", options);
        return;
      }
      if (line.hasOption("hl")) {
        headless = true;
      }

      if (!headless) {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException ex) {
        }
      }

      String bundleDir = "";
      String modelDir = "";
      if (line.hasOption(PLUGIN_DIR)) {
        bundleDir = line.getOptionValue(PLUGIN_DIR);
      }
      if (line.hasOption(MODEL_DIR)) {
        modelDir = line.getOptionValue(MODEL_DIR);
      }

      if (bundleDir.length() == 0) {
        URL runtimeSource = StandAloneMain.class.getProtectionDomain().getCodeSource()
            .getLocation();
        File location = new File(runtimeSource.toURI());
        if (location.getName().endsWith("batch_runner.jar")) {
          // assume this is being run from executable jar just
          // below
          // the eclipse dir
       
          if (new File(location.getParent(), "eclipse").exists()) {
            bundleDir = Paths.get(location.getParent(), "eclipse", "plugins").toString();
          } else if (new File(location.getParent(), "Eclipse.app").exists()) {
            bundleDir = Paths.get(location.getParent(), "Eclipse.app", "Contents", "Eclipse","plugins").toString();
          } else {
            throw new IOException("Unable to find path to eclipse plugins directory");
          }
          
        } else {
          File dir = location.getParentFile().getParentFile();
          bundleDir = dir.getAbsolutePath();
        }
      }
      new StandAloneMain().run(bundleDir, modelDir, headless, args);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
