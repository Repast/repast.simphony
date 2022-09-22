/**
 * 
 */
package repast.simphony.eclipse;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Eclipse plugin activator for Repast Simphony.
 * 
 * @author Nick Collier, Michael North
 */
public class RepastSimphonyPlugin extends AbstractUIPlugin {

    public static final String REPAST_SIMPHONY_PLUGIN_ID = "repast.simphony.eclipse";
    public static final String REPAST_SIMPHONY_NATURE_ID = REPAST_SIMPHONY_PLUGIN_ID + ".repast_simphony_nature";

    public static final String REPAST_SIMPHONY_PLUGIN_VERSION = "2.10.0";

    public static final String JAR_FILE = "repast.simphony.bin_and_src.jar";
    public static final String JAR_PROJECT = "repast.simphony.bin_and_src_" + REPAST_SIMPHONY_PLUGIN_VERSION;
    public static final String JAR_PATH_RELATIVE = JAR_PROJECT + "/" + JAR_FILE;
    // no version number because running in dev environment
    public static final String DEBUG_JAR_PROJECT = "repast.simphony.bin_and_src";
    public static final String DEBUG_JAR_PATH_RELATIVE = DEBUG_JAR_PROJECT + "/" + JAR_FILE;

    private static String[] BASE_CLASSPATH_FOR_LAUNCHER = { "${workspace_loc:project_name}/bin",
            "ECLIPSE_HOME/plugins/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/bin",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/saf.core.runtime.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/commons-logging-1.1.2.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/javassist-3.17.1-GA.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jpf.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jpf-boot.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/log4j-1.2-api-2.17.2.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/log4j-api-2.17.2.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/log4j-core-2.17.2.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/xpp3_min-1.1.4c.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/xstream-1.4.7.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/xmlpull-1.1.3.1.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/commons-cli-1.2.jar", };

    private static String[] BASE_CLASSPATH_FOR_SERVER_LAUNCHER = { "${workspace_loc:project_name}/bin",
            "ECLIPSE_HOME/plugins/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/bin",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/saf.core.runtime.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/commons-logging-1.1.2.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/javassist-3.17.1-GA.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jpf.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jpf-boot.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/log4j-1.2-api-2.17.2.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/log4j-api-2.17.2.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/log4j-core-2.17.2.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/xpp3_min-1.1.4c.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/xstream-1.4.7.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/xmlpull-1.1.3.1.jar",
            "/repast.simphony.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/commons-cli-1.2.jar",
            
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/concurrent-1.3.4.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jung-api-2.0.1.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/opencsv-2.3.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/cglib-nodep-2.2.3.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jung-algorithms-2.0.1.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/colt-1.2.0-no_hep.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/poi-ooxml-3.9-20121203.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jscience-4.3.1-no_ogis.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/xmlbeans-2.3.0.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/velocity-1.4.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/commons-io-2.5.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/hsqldb-2.3.2.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/poi-3.9-20121203.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/commons-math3-3.6.1.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/commons-collections-3.2.2.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jung-graph-impl-2.0.1.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/dom4j-1.6.1.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/commons-lang3-3.1.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jung-io-2.0.1.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jbullet.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/collections-generic-4.01.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/ooxml-schemas-1.1.jar",
            "/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/bin",
            
            // For ProbeID which is imported in relogo BasePatch with is referenced in user_path.xml
            // and so read by RepastWS
            "/repast.simphony.gui_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/bin",
            
            
            "ECLIPSE_HOME/plugins/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/bin",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jackson-core-2.9.9.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jackson-databind-2.9.9.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/slf4j-simple-1.7.26.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/kotlin-stdlib-jdk7-1.3.31.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/slf4j-api-1.7.26.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/websocket-api-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/websocket-servlet-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jetty-webapp-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/kotlin-stdlib-common-1.3.31.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/websocket-client-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/javax.servlet-api-3.1.0.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jetty-io-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jetty-server-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jetty-util-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/kotlin-stdlib-1.3.31.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/websocket-common-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/annotations-13.0.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/javalin-3.0.0.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/kotlin-stdlib-jdk8-1.3.31.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jetty-servlet-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jeromq-0.5.1.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jackson-annotations-2.9.9.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jetty-client-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jetty-xml-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/saf.core.v3d.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jetty-http-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/websocket-server-9.4.17.v20190418.jar",
            "/repast.simphony.server_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/lib/jetty-security-9.4.17.v20190418.jar",

            "ECLIPSE_HOME/plugins/repast.simphony.scenario_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/bin",
            "ECLIPSE_HOME/plugins/repast.simphony.core_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/bin",
            "ECLIPSE_HOME/plugins/repast.simphony.relogo.runtime_" + REPAST_SIMPHONY_PLUGIN_VERSION + "/bin",

    };

    public static final String RS_EXTRA_JAR_CODE = "REPAST_EXTRA_JAR_";
    public static final ArrayList<String> AGENT_BUILDER_EXTRA_JARS = new ArrayList<String>();
    {
        IStringVariable[] variables = VariablesPlugin.getDefault().getStringVariableManager().getValueVariables();
        for (int i = 0; i < variables.length; i++) {
            if (variables[i].getName().startsWith(RS_EXTRA_JAR_CODE) && variables[i] instanceof IValueVariable) {
                AGENT_BUILDER_EXTRA_JARS.add(((IValueVariable) variables[i]).getValue());
            }
        }
    }
    private static final String ECLIPSE_PROJECT = "repast.simphony.eclipse";

    private static RepastSimphonyPlugin instance;
    private ResourceBundle resourceBundle;

    private String[] launcherClasspath, serverLauncherClasspath, compilerClasspath;
    private String installDirectory;

    /**
     * 
     */
    public RepastSimphonyPlugin() {
        instance = this;
        try {
            resourceBundle = ResourceBundle.getBundle("RepastSimphonyPluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
            x.printStackTrace();
        }
    }

    public static RepastSimphonyPlugin getInstance() {
        return instance;
    }

    private ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Gets the installation directory of this plugin.
     * 
     * @return the installation directory of this plugin.
     */
    public String getPluginInstallationDirectory() {
        if (installDirectory == null) {
            try {
                URL relativeURL = FileLocator.find(getBundle(), new Path(""), null);
                URL absoluteURL = FileLocator.resolve(relativeURL);
                String tempURLString = URLDecoder.decode(absoluteURL.getFile(), System.getProperty("file.encoding"));
                String mainDataSourcePluginFile = new File(tempURLString).getPath();
                IPath mainDataSourcePluginFilePath = new Path(mainDataSourcePluginFile);
                IPath mainDataSourcePluginDirectoryPath = mainDataSourcePluginFilePath.removeLastSegments(1);

                installDirectory = mainDataSourcePluginDirectoryPath.toFile().toString()
                        + System.getProperty("file.separator");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return installDirectory;
    }

    /**
     * Gets the classpath list to be used when launching a simulation.
     * 
     * @return
     */
    public String[] getClassPathListForLauncher() {
        return getClassPathList(BASE_CLASSPATH_FOR_LAUNCHER, launcherClasspath);
//        if (launcherClasspath == null) {
//            try {
//                // this looks for the bin_and_src.jar in the bin_and_src_VERSION
//                // directory
//                // if we are running in the development enviroment this won't exist so
//                // we can
//                // use its existence as a flag.
//                String pluginDirectory = getPluginInstallationDirectory();
//                File file;
//                if (pluginDirectory.trim().equals("")) {
//                    file = new Path(JAR_PATH_RELATIVE).toFile();
//                } else {
//                    file = new Path(pluginDirectory + JAR_PATH_RELATIVE).toFile();
//                }
//
//                boolean inDevEnv = !file.exists();
//
//                launcherClasspath = new String[BASE_CLASSPATH_FOR_LAUNCHER.length + AGENT_BUILDER_EXTRA_JARS.size()];
//                int i = 0;
//                for (String jarElement : BASE_CLASSPATH_FOR_LAUNCHER) {
//                    if ((jarElement != null) && (jarElement.toLowerCase().endsWith("jar"))) {
//                        launcherClasspath[i] = JavaCore.getClasspathVariable("ECLIPSE_HOME") + "/plugins" + jarElement;
//                    } else {
//                        launcherClasspath[i] = jarElement;
//                    }
//
//                    if (inDevEnv) {
//                        // rewrite the jar element
//                        String item = launcherClasspath[i];
//                        item = item.replace("_" + REPAST_SIMPHONY_PLUGIN_VERSION, "");
//                        item = item.replace(JavaCore.getClasspathVariable("ECLIPSE_HOME") + "/plugins/",
//                                pluginDirectory);
//                        item = item.replace("ECLIPSE_HOME/plugins/", pluginDirectory);
//                        launcherClasspath[i] = item;
//                    }
//
//                    i++;
//                }
//                for (int j = 0; j < AGENT_BUILDER_EXTRA_JARS.size(); j++) {
//                    String extraJar = AGENT_BUILDER_EXTRA_JARS.get(j);
//                    if ((extraJar != null) && (extraJar.toLowerCase().endsWith("jar"))) {
//                        launcherClasspath[i] = JavaCore.getClasspathVariable("ECLIPSE_HOME") + "/plugins" + extraJar;
//                    } else {
//                        launcherClasspath[i] = extraJar;
//                    }
//                    // System.out.println("Extra jar: " +
//                    // finalJarClassPathListForLauncher[i]);
//                    i++;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return launcherClasspath;
    }
    
    private String[] getClassPathList(String[] basePath, String[] classpath) {
        if (classpath == null) {
            try {
                // this looks for the bin_and_src.jar in the bin_and_src_VERSION
                // directory
                // if we are running in the development enviroment this won't exist so
                // we can
                // use its existence as a flag.
                String pluginDirectory = getPluginInstallationDirectory();
                File file;
                if (pluginDirectory.trim().equals("")) {
                    file = new Path(JAR_PATH_RELATIVE).toFile();
                } else {
                    file = new Path(pluginDirectory + JAR_PATH_RELATIVE).toFile();
                }

                boolean inDevEnv = !file.exists();

                classpath = new String[basePath.length + AGENT_BUILDER_EXTRA_JARS.size()];
                int i = 0;
                for (String jarElement : basePath) {
                    if ((jarElement != null) && (jarElement.toLowerCase().endsWith("jar"))) {
                        classpath[i] = JavaCore.getClasspathVariable("ECLIPSE_HOME") + "/plugins" + jarElement;
                    } else {
                        classpath[i] = jarElement;
                    }

                    if (inDevEnv) {
                        // rewrite the jar element
                        String item = classpath[i];
                        item = item.replace("_" + REPAST_SIMPHONY_PLUGIN_VERSION, "");
                        item = item.replace(JavaCore.getClasspathVariable("ECLIPSE_HOME") + "/plugins/",
                                pluginDirectory);
                        item = item.replace("ECLIPSE_HOME/plugins/", pluginDirectory);
                        classpath[i] = item;
                    }

                    i++;
                }
                for (int j = 0; j < AGENT_BUILDER_EXTRA_JARS.size(); j++) {
                    String extraJar = AGENT_BUILDER_EXTRA_JARS.get(j);
                    if ((extraJar != null) && (extraJar.toLowerCase().endsWith("jar"))) {
                        classpath[i] = JavaCore.getClasspathVariable("ECLIPSE_HOME") + "/plugins" + extraJar;
                    } else {
                        classpath[i] = extraJar;
                    }
                    // System.out.println("Extra jar: " +
                    // finalJarClassPathListForLauncher[i]);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return classpath;
        
    }
    
    /**
     * Gets the classpath list to be used when launching a simulation.
     * 
     * @return
     */
    public String[] getClassPathListForServerLauncher() {
        return getClassPathList(BASE_CLASSPATH_FOR_SERVER_LAUNCHER, serverLauncherClasspath);
    }

    /**
     * Gets whether or not the plugin has been launched as an Eclipse App from
     * within eclipse or is running as part of the regular repast install.
     * 
     * @return true if running in an Eclipse App from within eclipse, otherwise
     *         false.
     */
    public boolean isRunningInDevEnv() {
        String pluginDir = getPluginInstallationDirectory();
        File file;
        if (pluginDir.trim().equals("")) {
            file = new Path(JAR_PATH_RELATIVE).toFile();
        } else {
            file = new Path(pluginDir + JAR_PATH_RELATIVE).toFile();
        }

        return !file.exists();
    }

    /**
     * Returns the string from the plugin's resource bundle, or 'key' if not found.
     */
    public String getResourceString(String key) {
        ResourceBundle bundle = RepastSimphonyPlugin.getInstance().getResourceBundle();
        try {
            return (bundle != null ? bundle.getString(key) : key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public String[] getCompilerClasspath() {
        if (compilerClasspath == null) {

            File file = null;
            try {
                String installDir = getPluginInstallationDirectory();

                if (installDir.trim().equals("")) {
                    file = new Path(JAR_PATH_RELATIVE).toFile();
                } else {
                    file = new Path(installDir + JAR_PATH_RELATIVE).toFile();
                }

                // for running in dev environment
                boolean inDevEnv = false;
                if (!file.exists()) {
                    inDevEnv = true;
                    file = new Path(installDir + DEBUG_JAR_PATH_RELATIVE).toFile();
                }

                JarFile jarFile = new JarFile(file);

                Manifest manifest = jarFile.getManifest();

                Attributes attributes = manifest.getMainAttributes();

                String jarClassPath = attributes.getValue(Attributes.Name.CLASS_PATH).trim();

                jarClassPath = jarClassPath.replace("./", "/");
                jarClassPath = jarClassPath.replace(".\\", "\\");
                jarClassPath = jarClassPath.replace("\\", "/");

                String[] jarClassPathList = jarClassPath.split(" ");
                compilerClasspath = new String[jarClassPathList.length + AGENT_BUILDER_EXTRA_JARS.size()];
                int i = 0;

                String prefix = inDevEnv ? installDir : JavaCore.getClasspathVariable("ECLIPSE_HOME") + "/plugins";
                for (String jarElement : jarClassPathList) {
                    if (inDevEnv) {
                        jarElement = jarElement.replace("_" + REPAST_SIMPHONY_PLUGIN_VERSION, "");
                    }

                    compilerClasspath[i] = prefix + jarElement;
                    i++;
                }
                for (int j = 0; j < AGENT_BUILDER_EXTRA_JARS.size(); j++) {
                    compilerClasspath[i] = prefix + AGENT_BUILDER_EXTRA_JARS.get(j);
                    i++;
                }

                if (compilerClasspath == null) {
                    message("Error, Error: compilerClasspath == null");
                    message("install directory = " + installDir);
                    message("file = " + file.toString());
                    message("jarFile = " + jarFile.toString());
                    message("jarClassPath = " + jarClassPath);
                }
            } catch (Exception e) {
                message("Based on REPAST_SIMPHONY_PLUGIN_VERSION = " + REPAST_SIMPHONY_PLUGIN_VERSION
                        + ", could not find " + file);
                e.printStackTrace();
            }
        }

        return compilerClasspath;
    }

    /**
     * Opens a message Dialog window with the given messge.
     * 
     * @param message the message
     */
    public void message(String message) {
        MessageDialog.openInformation(new Shell(), "Repast Simphony Plug-in", message);
    }

    public void log(Throwable e) {
        getLog().log(
                new Status(IStatus.ERROR, getBundle().getSymbolicName(), IStatus.ERROR, "Error: " + e.getMessage(), e));
    }

    /**
     * Gets the directory name of the eclipse project.For example,
     * repast.simphony.eclipse_2.0.1 when running the release version or just
     * repast.simphony.eclipse when launched as an eclipse application.
     * 
     * @return the directory name of the eclipse project.
     */
    public String getPluginDirectoryName() {
        if (isRunningInDevEnv()) {
            return ECLIPSE_PROJECT;
        } else {
            return ECLIPSE_PROJECT + "_" + REPAST_SIMPHONY_PLUGIN_VERSION;
        }
    }

}
