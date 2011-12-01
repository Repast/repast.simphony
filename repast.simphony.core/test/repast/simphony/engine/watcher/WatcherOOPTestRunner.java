/**
 * 
 */
package repast.simphony.engine.watcher;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.junit.runner.JUnitCore;

import repast.simphony.filter.Filter;
import repast.simphony.util.ClassPathFilter;

/**
 * Runs the JUnit tests for WatcherOOPTests. This is necessary
 * because we need to do the instrumentation before the actual
 * test class is loaded. 
 * 
 * @author Nick Collier
 */
public class WatcherOOPTestRunner {
  
  private static final File classpath = new File("./test_bin/");
  
  public static void main(String[] args) {
    BasicConfigurator.configure();
    
    
    WatcheeInstrumentor instrumentor = new WatcheeInstrumentor();
    WatcherTrigger.initInstance(instrumentor);
    
    instrumentor.addFieldToWatchFromWatcherPath(classpath.getAbsolutePath());
    Filter<String> filter = new ClassPathFilter("watcherOOP.test.OOPWatcherOn*");
    instrumentor.instrument(classpath.getAbsolutePath(), filter);
    
    JUnitCore.main(new String[]{"repast.simphony.engine.watcher.WatcherOOPTests"});
    
  }

}
