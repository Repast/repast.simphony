package repast.simphony.scenario;

import java.io.File;
import java.io.IOException;

import repast.simphony.plugin.ExtendablePluginClassLoader;
import repast.simphony.scenario.data.Classpath;
import repast.simphony.scenario.data.UserPathData;
import repast.simphony.util.ClassPathEntry;
import simphony.util.messages.MessageCenter;

/**
 * Initializes the classpath for a scenario from the user patn file.
 * 
 * @author Nick Collier
 */
public class ScenarioCPInitializer {

  private static final MessageCenter msg = MessageCenter
      .getMessageCenter(ScenarioCPInitializer.class);

  public void run(UserPathData data) throws IOException {
    Classpath cpath = new Classpath();
    for (ClassPathEntry entry : data.classpathEntries()) {
      boolean added = cpath.addEntry(entry);
      if (!added) {
        msg.warn("Warning while parsing classpath: Empty or non-existent path '"
            + entry.getPath().getCanonicalPath() + "'.");
      }
    }

    ClassLoader loader = this.getClass().getClassLoader();
    if (loader instanceof ExtendablePluginClassLoader) {
      for (ClassPathEntry entry : cpath.entries()) {
        for (File path : entry.getClassPaths()) {
          ((ExtendablePluginClassLoader) loader).appendPaths(path.getCanonicalPath());
        }
      }
    }
  }
}
