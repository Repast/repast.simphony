package repast.simphony.engine.watcher;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import repast.simphony.util.ClassPathFilter;

/**
 * Note that these two tests need to be run separately otherwise a class will be
 * instrumented twice which will cause errors.
 * 
 * @author Nick Collier
 */
public class InstrumentorTest {

  @Test
  public void testWatcherFromJarPath() {
    WatcheeInstrumentor inst = new WatcheeInstrumentor();
    inst.addFieldToWatchFromWatcherPath("./test/watcher_test.jar");
    // MyWatcher watches the Generator so if we filter on that then we just get
    // Generator instrumented
    inst.instrument("./test/watcher_test.jar", new ClassPathFilter(
        "repast.simphony.engine.watcher.MyWatcher"));

    assertEquals(1, WatcheeInstrumentor.getInstrumented().size());
    assertEquals("repast.simphony.engine.watcher.Generator", WatcheeInstrumentor.getInstrumented()
        .iterator().next());
  }

  @Test
  public void testWatcherFromDir() {
    WatcheeInstrumentor inst = new WatcheeInstrumentor();
    // static so we need to clear it.
    WatcheeInstrumentor.getInstrumented().clear();
    inst.addFieldToWatchFromWatcherPath("./test_bin");
    // MyWatcher watches the Generator so if we filter on that then we just get
    // Generator instrumented
    inst.instrument("./test_bin", new ClassPathFilter("repast.simphony.engine.watcher.MyWatcher"));

    assertEquals(1, WatcheeInstrumentor.getInstrumented().size());
    assertEquals("repast.simphony.engine.watcher.Generator", WatcheeInstrumentor.getInstrumented()
        .iterator().next());

  }
}
