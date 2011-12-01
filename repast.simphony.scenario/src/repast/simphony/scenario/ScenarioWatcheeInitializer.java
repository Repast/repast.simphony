package repast.simphony.scenario;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import repast.simphony.engine.watcher.WatcheeInstrumentor;
import repast.simphony.engine.watcher.WatcherPathFilter;
import repast.simphony.engine.watcher.WatcherTrigger;
import repast.simphony.filter.OrFilter;
import repast.simphony.scenario.data.Classpath;
import repast.simphony.scenario.data.UserPathData;
import repast.simphony.util.ClassPathEntry;
import simphony.util.messages.MessageCenter;

/**
 * Initializes watchers using scenario model file data.
 * 
 * @author Nick Collier
 */
public class ScenarioWatcheeInitializer {
  
  private static final MessageCenter msg = MessageCenter.getMessageCenter(ScenarioWatcheeInitializer.class);
  
  public void run(UserPathData data) throws IOException, ClassNotFoundException {
    Classpath cpath = new Classpath();
    for (ClassPathEntry entry : data.annotationCPEntries()) {
      boolean added = cpath.addEntry(entry);
      if (!added) {
	msg.warn("While initializing watchers: Empty or non-existent path '" + entry.getPath().getCanonicalPath() + "'.");
      }
    }
    initWatchees(data, cpath);
  }
 
  // instruments the watchees
  private void initWatchees(UserPathData data, Classpath cpath) throws IOException {
    WatcheeInstrumentor instrumentor = new WatcheeInstrumentor();
    WatcherTrigger.initInstance(instrumentor);
    
    for (ClassPathEntry entry : cpath.entries()) {
      for (File path : entry.getClassPaths()) {
        instrumentor.addFieldToWatchFromWatcherPath(path.getAbsolutePath());
      }
    }
    
    cpath = new Classpath();
    for (ClassPathEntry entry : data.classpathEntries()) {
      cpath.addEntry(entry);
    }
    
    OrFilter<String> orFilter = new OrFilter<String>();
    Set<File> agentPaths = new HashSet<File>();
    for (ClassPathEntry entry : data.agentEntries()) {
      orFilter.addFilter(entry.getFilter());
      agentPaths.add(entry.getPath());
    }
    
    for (ClassPathEntry entry : data.annotationCPEntries()) {
      if (!agentPaths.contains(entry.getPath())) {
        orFilter.addFilter(new WatcherPathFilter(entry.getPath().getCanonicalPath()));
      }
    }
    
    StringBuilder buf = new StringBuilder();
    for (File path : cpath.classPaths()) {
      buf.append(path.getAbsolutePath());
      buf.append(File.pathSeparator);
    }
    String cp = buf.toString();
    // strip off last path separator
    if (cp.length() > 0) instrumentor.instrument(cp.substring(0, cp.length() - 1), orFilter);
    else instrumentor.instrument(cp, orFilter);
  }

}
