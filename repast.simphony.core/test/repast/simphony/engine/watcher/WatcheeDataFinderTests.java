package repast.simphony.engine.watcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

import repast.simphony.filter.Filter;
import repast.simphony.util.ClassPathFilter;

public class WatcheeDataFinderTests {
  
  static {
    BasicConfigurator.configure();
  }
  
  // Classpath filter with pattern like: repast\.simphony\.demo\.heatbugs\.\w*\.class
  // for user_path data like filter="repast.simphony.demo.heatbugs.*"
  
  private File classpath = new File("./test_bin/");
  private WatcheeDataFinder finder;
  
  @Before
  public void setup() {
    finder = new WatcheeDataFinder(new WatcheeInstrumentor());
  }
  
  private List<WatcheeData> dataToList() {
    List<WatcheeData> data = new ArrayList<WatcheeData>();
    for (WatcheeData wd : finder.data()) {
      data.add(wd);
    }
    return data;
  }
  
  @Test
  public void simpleFinderTest() {
    try {
      Filter<String> filter = new ClassPathFilter("watcherOOP.test.SimpleWatcher");
      finder.addPathToSearch(classpath.getCanonicalPath());
      finder.run(classpath.getCanonicalPath(), filter);
      List<WatcheeData> data = dataToList();
      
      assertEquals(1, data.size());
      assertEquals(2, data.get(0).fields.size());
      
    } catch (IOException e) {
      fail();
    } catch (ClassNotFoundException e) {
      fail();
    }
  }
  
  private WatcheeData findData(String name, List<WatcheeData> list) {
    for (WatcheeData data : list) {
      if (data.className.equals(name)) return data;
    }
    return null;
  }
  
  private void watcheeTest(String className, List<WatcheeData> list, String... fields) {
    WatcheeData data = findData(className, list);
    assertNotNull(className, data);
    assertEquals(fields.length, data.fields.size());
    for (String field : fields) {
      assertTrue(className + ": " + field, data.fields.contains(field));
    }
  }
  
  @Test
  public void test2Level() {
    // @Watch(watcheeClassName="watcherOOP.test.WatcheeChildA", watcheeFieldNames="val"
    // @Watch(watcheeClassName="watcherOOP.test.SimpleWatchee", watcheeFieldNames="val, str", 
    try {
      finder.addPathToSearch(classpath.getCanonicalPath());
      Filter<String> filter = new ClassPathFilter("watcherOOP.test.OOPWatcher");
      finder.run(classpath.getCanonicalPath(), filter);
      List<WatcheeData> data = dataToList();
      assertEquals(4, data.size());
      
      watcheeTest("watcherOOP.test.SimpleWatchee", data, "val", "str");
      watcheeTest("watcherOOP.test.WatcheeRoot", data, "val");
      watcheeTest("watcherOOP.test.WatcheeChildA", data, "val");
      watcheeTest("watcherOOP.test.WatcheeChildC", data, "val");
      
    } catch (IOException e) {
      fail();
    } catch (ClassNotFoundException e) {
      fail();
    }
  }
  
  private int getDataOrderIndex(List<WatcheeData> list, String name) {
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).className.equals(name)) return i;
    }
    return -1;
  }
  
  @Test
  public void test3Level() {
    //@Watch(watcheeClassName="watcherOOP.test.SimpleWatchee", watcheeFieldNames="val, str"
    //@Watch(watcheeClassName="watcherOOP.test.WatcheeChildC", watcheeFieldNames="val, cStr"
    
    try {
      finder.addPathToSearch(classpath.getCanonicalPath());
      Filter<String> filter = new ClassPathFilter("watcherOOP.test.OOPWatcherOnC");
      finder.run(classpath.getCanonicalPath(), filter);
      List<WatcheeData> data = dataToList();
      assertEquals(4, data.size());
      
      watcheeTest("watcherOOP.test.SimpleWatchee", data, "val", "str");
      watcheeTest("watcherOOP.test.WatcheeRoot", data, "val");
      watcheeTest("watcherOOP.test.WatcheeChildA", data, "val");
      watcheeTest("watcherOOP.test.WatcheeChildC", data, "val", "cStr");
      
      int rootIndex = getDataOrderIndex(data, "watcherOOP.test.WatcheeRoot");
      int aIndex = getDataOrderIndex(data, "watcherOOP.test.WatcheeChildA");
      int cIndex = getDataOrderIndex(data, "watcherOOP.test.WatcheeChildC");
      
      assertTrue(rootIndex < aIndex);
      assertTrue(aIndex < cIndex);
      
    } catch (IOException e) {
      fail();
    } catch (ClassNotFoundException e) {
      fail();
    }
  }
  
  @Test
  public void testMultChildren() {
    // OnB is watching: 
    // @Watch(watcheeClassName="watcherOOP.test.SimpleWatchee", watcheeFieldNames="val", 
    // @Watch(watcheeClassName="watcherOOP.test.WatcheeChildB", watcheeFieldNames="ival, dVal",
    
    // OnC is watching
    //@Watch(watcheeClassName="watcherOOP.test.SimpleWatchee", watcheeFieldNames="str"
    //@Watch(watcheeClassName="watcherOOP.test.WatcheeChildC", watcheeFieldNames="val, cStr"
    
    try {
      finder.addPathToSearch(classpath.getCanonicalPath());
      Filter<String> filter = new ClassPathFilter("watcherOOP.test.OOPWatcherOn*");
      finder.run(classpath.getCanonicalPath(), filter);
      List<WatcheeData> data = dataToList();
      assertEquals(5, data.size());
      
      // val + str melded from both watchers
      watcheeTest("watcherOOP.test.SimpleWatchee", data, "val", "str");
      // val + dVal melded from both A and B
      watcheeTest("watcherOOP.test.WatcheeRoot", data, "val", "dVal");
      // iVal from B and dVal from root
      watcheeTest("watcherOOP.test.WatcheeChildB", data, "iVal", "dVal");
      watcheeTest("watcherOOP.test.WatcheeChildC", data, "val", "cStr");
      // A is included as the parent of C
      watcheeTest("watcherOOP.test.WatcheeChildA", data, "val");
      
      int rootIndex = getDataOrderIndex(data, "watcherOOP.test.WatcheeRoot");
      int aIndex = getDataOrderIndex(data, "watcherOOP.test.WatcheeChildA");
      int cIndex = getDataOrderIndex(data, "watcherOOP.test.WatcheeChildC");
      int bIndex = getDataOrderIndex(data, "watcherOOP.test.WatcheeChildB");
      
      assertTrue(rootIndex < aIndex);
      assertTrue(rootIndex < bIndex);
      assertTrue(aIndex + ", " + cIndex, aIndex < cIndex);
      
    } catch (IOException e) {
      fail();
    } catch (ClassNotFoundException e) {
      fail();
    }
  }
  
  // TODO test class being instrumented is also a field in an instrumented class.
  

}
