/**
 * 
 */
package repast.simphony.runtime;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.log4j.BasicConfigurator;
import org.java.plugin.Plugin;
import org.java.plugin.PluginManager;

import saf.core.runtime.Boot;
import simphony.util.messages.MessageCenter;

/**
 * Main class used to start batch runs. This will autoload all the repast
 * simphony classes and required libraries via the jpf plugins
 * 
 * @author Nick Collier
 */
public class RepastBatchMain {

  static MessageCenter msgCenter = MessageCenter.getMessageCenter(RepastBatchMain.class);
  private Options options;

  @SuppressWarnings("static-access")
  public RepastBatchMain() {
    options = new Options();
    Option help = new Option("help", "print this message");

    Option paramfile = OptionBuilder.withArgName("file").hasArg()
        .withDescription("use given parameter sweep file").create("params");

    Option opt = OptionBuilder.withArgName("file").hasOptionalArg()
        .withDescription("use optimizable parameter sweeper with given optimizing properties file")
        .create("opt");

    Option mode = OptionBuilder.withDescription("specifies if the batch mode is interactive")
        .create("interactive");

    Option bootProps = OptionBuilder.withArgName("file").hasArg()
        .withDescription("location of boot.properties file (optional)").create("bootprops");

    options.addOption(help);
    options.addOption(paramfile);
    options.addOption(opt);
    options.addOption(mode);
    options.addOption(bootProps);
  }

  public void run(String[] args) {
    try {
      CommandLineParser parser = new GnuParser();

      // parse the command line arguments
      CommandLine line = parser.parse(options, args);
      if (line.hasOption("help") || args.length < 1) {
        showHelp();
        System.exit(0);
      }

      String[] bootArgs = new String[] { "", "" };
      if (line.hasOption("bootprops")) {
        bootArgs[1] = line.getOptionValue("bootprops");
      } else {
        URL runtimeSource = repast.simphony.runtime.RepastMain.class.getProtectionDomain()
            .getCodeSource().getLocation();
        String path = runtimeSource.getFile().replaceAll("%20", " ");
        bootArgs[1] = new File(path).getParent() + File.separator;
      }

      Boot boot = new Boot();
      
      PluginManager manager = boot.init(bootArgs);
  
      if (manager == null) {
        // probably a bad boot propertie so do the basic log config
        // so that msgCenter errors will show up.
        BasicConfigurator.configure();
        throw new IllegalArgumentException(
            "Unable to load the repast symphony libraries. Is the boot.properties path '"
                + bootArgs[1] + "' correct?");
      }

      Plugin plugin = manager.getPlugin("repast.simphony.core");
      ClassLoader loader = manager.getPluginClassLoader(plugin.getDescriptor());
      // avoid referring to any of the class and method except as Strings
      // so we don't have any compile time dependencies. Boot should make sure
      // that
      // the batch code is on the classpath.
      Class<?> clazz = Class.forName("repast.simphony.batch.BatchInit", true, loader);
      Object init = clazz.newInstance();
      clazz.getMethod("run", CommandLine.class, String[].class).invoke(init, line, (Object) args);
  
    } catch (Exception ex) {
      showHelp();
      msgCenter.fatal("Fatal error starting repast.simphony simphony.", ex);
      ex.printStackTrace();
    }
  }

  private void showHelp() {
    HelpFormatter formatter = new HelpFormatter();
    String header = "Where target is the path to a scenario file or the fully qualified name of a class"
        + " that implements repast.simphony.batch.BatchScenarioCreator.\n";
    formatter.printHelp(RepastMain.class.getName() + " [options] target", header, options, "");
  }

  public static void main(String[] args) {
    new RepastBatchMain().run(args);
  }
}
