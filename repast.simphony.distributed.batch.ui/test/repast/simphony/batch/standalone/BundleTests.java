/**
 * 
 */
package repast.simphony.batch.standalone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * @author Nick Collier
 */
public class BundleTests {
  
  @Test
  public void testVersion() {
    Version version = new Version("2.0.1");
    assertEquals(2, version.getMajor());
    assertEquals(0, version.getMinor());
    assertEquals(1, version.getService());
    
    version = new Version("2.0.6.xx-RELEASE-20121219-0800-e42");
    assertEquals(2, version.getMajor());
    assertEquals(0, version.getMinor());
    assertEquals(6, version.getService());
    
    Version a = new Version("3.0.1");
    Version b = new Version("4.0.1");
    
    assertTrue(a.lessEqual(b));
    assertTrue(!b.lessEqual(a));
    b = new Version("3.0.0");
    assertTrue(!a.lessEqual(b));
    assertTrue(b.lessEqual(a));
    b = new Version("3.0.1");
    assertTrue(a.lessEqual(b));
  }
  
  @Test
  public void testFindGroovyJars(){
	  StandAloneMain sam = new StandAloneMain();
	  File dir = Paths.get("test_data").toFile();
	  List<File> jars = sam.findGroovyJars(dir.getAbsolutePath());
	  Set<String> expected = new HashSet<>();
	  expected.add("groovy-3.0.8-indy.jar");
	  expected.add("groovy-swing-3.0.8.jar");
	  expected.add("groovy-templates-3.0.8.jar"); 
	  expected.add("groovy-xml-3.0.8.jar");

	  assertEquals(4, jars.size());
	  for (File jar : jars) {
		  String fname = jar.getName();
		  assertTrue(expected.contains(fname));
		  expected.remove(fname);
	  }
  }
  
  /*@Test
  public void testBundleDataReader() throws IOException {
    BundleDataReader reader = new BundleDataReader();
    BundleData bd = reader.reader(new FileInputStream("./test_data/MANIFEST.MF"));

    assertEquals("repast.simphony.distributed.batch", bd.getName());
    assertEquals("2.3.0", bd.getVersion());

    List<BundleData> data = new ArrayList<BundleData>();
    for (BundleData rbd : bd.requiredBundles()) {
      data.add(rbd);
    }
    assertEquals(6, data.size());

    assertEquals("repast.simphony.batch", data.get(0).getName());
    assertEquals("2.3.0", data.get(0).getVersion());

    assertEquals("repast.simphony.core", data.get(1).getName());
    assertEquals("2.3.0", data.get(1).getVersion());

    assertEquals("repast.simphony.data", data.get(2).getName());
    assertEquals("2.3.0", data.get(2).getVersion());

    assertEquals("repast.simphony.scenario", data.get(3).getName());
    assertEquals("2.3.0", data.get(3).getVersion());

    assertEquals("repast.simphony.runtime", data.get(4).getName());
    assertEquals("2.3.0", data.get(4).getVersion());

    assertEquals("org.junit", data.get(5).getName());
    assertEquals("", data.get(5).getVersion());

    List<String> cp = new ArrayList<String>();
    for (String path : bd.classPathEntries()) {
      cp.add(path);
    }

    assertEquals(3, cp.size());
    assertEquals(".", cp.get(0));
    assertEquals("lib/jsch-0.1.48.jar", cp.get(1));
    assertEquals("lib/ant.jar", cp.get(2));
  }*/

  /*@Test
  public void testBundleFinder() throws IOException {
    BundleDataReader reader = new BundleDataReader();
    BundleData bd = reader.reader(new FileInputStream("./test_data/MANIFEST.MF"));
    BundleFinder finder = new BundleFinder();
    finder.findBundles(new File("./test_data/"), bd);

    BundleData core = null;
    BundleData dataBundle = null;
    for (BundleData bundle : bd.requiredBundles()) {
      if (bundle.getName().equals("repast.simphony.core")) {
        core = bundle;
      } else if (bundle.getName().equals("repast.simphony.data")) {
        dataBundle = bundle;
      }
    }

    assertNotNull(core);
    assertEquals(new File("./test_data/repast.simphony.core_2.0.1.jar").getAbsoluteFile(), core.getLocation());
    
    String[] excp = { "lib/opencsv-1.5.jar", "lib/jsr108-0.01.jar", "lib/concurrent-1.3.4.jar",
        "lib/commons-lang-2.1.jar", "lib/commons-collections-3.2.jar", "lib/cglib-nodep-2.1_3.jar",
        "lib/jgap.jar", "lib/OpenForecast-0.4.0.jar", "lib/ProActive.jar",
        "lib/collections-generic-4.01.jar", "lib/jscience.jar", "lib/joone-editor.jar",
        "lib/joone-engine.jar", "lib/jbullet.jar", "lib/colt-1.2.0-no_hep.jar",
        "lib/poi-3.6-20091214.jar", "bin/", "lib/jung-algorithms-2.0.1.jar",
        "lib/jung-api-2.0.1.jar", "lib/jung-graph-impl-2.0.1.jar", "lib/jung-io-2.0.1.jar",
        "lib/velocity-1.4.jar", "lib/hsqldb-1.8.0.7.jar", "lib/gt-api-9.0.jar",
        "lib/gt-brewer-9.0.jar", "lib/gt-coverage-9.0.jar", "lib/gt-cql-9.0.jar",
        "lib/gt-data-9.0.jar", "lib/gt-epsg-hsql-9.0.jar", "lib/gt-main-9.0.jar",
        "lib/gt-metadata-9.0.jar", "lib/gt-opengis-9.0.jar", "lib/gt-referencing-9.0.jar",
        "lib/gt-render-9.0.jar", "lib/gt-shapefile-9.0.jar", "lib/jts-1.13.jar" };
    
    Set<String> cp = new HashSet<String>();
    for (String path : core.classPathEntries()) { 
      cp.add(path);
    }
    
    assertEquals(excp.length, cp.size());
    
    for (String path : excp) {
      assertTrue(cp.remove(path));
    }
    assertEquals(0, cp.size());
    
    assertNotNull(dataBundle);
    assertEquals(new File("./test_data/repast.simphony.data_2.0.1").getAbsoluteFile(), dataBundle.getLocation());
    
    cp = new HashSet<String>();
    for (String path : dataBundle.classPathEntries()) { 
      cp.add(path);
    }
    assertEquals(2, cp.size());
    assertTrue(cp.remove("bin/"));
    assertTrue(cp.remove("lib/commons-math-2.1.jar"));
    
  }*/
}
