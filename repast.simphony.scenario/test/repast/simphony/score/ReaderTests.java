/**
 * 
 */
package repast.simphony.score;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import repast.simphony.filter.Filter;
import repast.simphony.filter.OrFilter;
import repast.simphony.parameter.StringConverterFactory;
import repast.simphony.scenario.data.Attribute;
import repast.simphony.scenario.data.Classpath;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.ContextFileReader;
import repast.simphony.scenario.data.ContextFileWriter;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.scenario.data.UserPathData;
import repast.simphony.scenario.data.UserPathFileReader;
import repast.simphony.util.ClassPathEntry;
import repast.simphony.util.ClassPathFilter;

/**
 * Unit tests for the score replacement components
 * 
 * @author Nick Collier
 */
public class ReaderTests {
  
  @Test
  public void pkgFilterTests() {
    ClassPathFilter filter = new ClassPathFilter("foo.bar.*");
    String path = makeClass("java", "lang", "String");
    assertFalse(path, filter.evaluate(path));
    
    path = makeClass("foo", "bar", "Util");
    assertTrue(path, filter.evaluate(path));
    
    filter = new ClassPathFilter("foo.bar.Util");
    path = makeClass("foo", "bar", "Util");
    assertTrue(path, filter.evaluate(path));
    
    filter = new ClassPathFilter("foo.bar.Util");
    path = "foo2bar2Util.class";
    assertFalse(path, filter.evaluate(path));
    
    filter = new ClassPathFilter("*SimpleAgent");
    path = "anl/gov/SimpleAgent.class";
    assertTrue(path, filter.evaluate(path));
    
    filter = new ClassPathFilter("*SimpleAgent");
    path = "anl/gov/Agent.class";
    assertFalse(path, filter.evaluate(path));
    
    filter = new ClassPathFilter("*SimpleAgent");
    path = "anl/gov/SimpleAgent2";
    assertFalse(path, filter.evaluate(path));
    
  }
  
  private String makeClass(String... parts) {
    StringBuilder buf = new StringBuilder();
    for (String val : parts) {
      buf.append(val);
      buf.append(File.separator);
    }
    return buf.toString().substring(0, buf.length() - 1) + ".class";
  }

  /*
   * 
   */
  @Test
  public void testModelFileReader() throws IOException, XMLStreamException {
    UserPathFileReader reader = new UserPathFileReader();
    File modelFile = new File("./test/data/model.xml");
    UserPathData data = reader.read(modelFile);
    assertEquals("Test Model", data.getName());

    List<String> expected = new ArrayList<String>();
    expected.add(new File(modelFile.getParentFile().getParentFile(), "bin").getCanonicalPath());
    expected.add("/model/groovy-bin");
    expected.add(new File(modelFile.getParentFile(), "lib/foo.jar").getCanonicalPath());
    expected.add(new File(modelFile.getParentFile().getParentFile(), "lib/bar.jar")
	.getCanonicalPath());
    expected.add("/foo.jar");
    expected.add("/lib/agent.jar");
    expected.add("/lib/more.jar");
    int i = 0;
    for (ClassPathEntry entry : data.classpathEntries()) {
      assertEquals(expected.get(i++), entry.getPath().getCanonicalPath());
    }

    expected.clear();
    expected.add(new File(modelFile.getParentFile(), "lib/foo.jar").getCanonicalPath());
    expected.add(new File(modelFile.getParentFile().getParentFile(), "lib/bar.jar").getCanonicalPath());
    expected.add("/lib/agent.jar");
    expected.add("/lib/more.jar");

    i = 0;
    for (ClassPathEntry entry : data.annotationCPEntries()) {
      assertEquals(expected.get(i++), entry.getPath().getCanonicalPath());
    }
    assertEquals(4, i);
    
    assertEquals(2, data.getAgentEntryCount());
    Iterator<ClassPathEntry> iter = data.agentEntries().iterator();
    ClassPathEntry agentPath = iter.next();
    assertEquals(new File("/lib/agent.jar").getCanonicalPath(), agentPath.getPath().getCanonicalPath());
    // no package or class specified so can filter always passes
    assertTrue(agentPath.filter("asdfa;lkjae;lkrja;sdflkj"));
    
    agentPath = iter.next();
    assertEquals(new File("/lib/more.jar").getCanonicalPath(), agentPath.getPath().getCanonicalPath());
    
    
    // filters foo.bar.*,baz.bar.*
    Iterator<Filter<String>> fiter = ((OrFilter<String>)agentPath.getFilter()).filters().iterator();
    ClassPathFilter cpFilter = (ClassPathFilter)fiter.next();
    assertEquals("foo\\.bar\\.\\w*\\.class", cpFilter.getPattern());
    
    cpFilter = (ClassPathFilter)fiter.next();
    assertEquals("baz\\.bar\\.\\w*\\.class", cpFilter.getPattern());
  }

  /*
   * <context id="root">
   * 
   * <projection id="p_1" type="network" /> <projection id="p_2" type="grid" />
   * 
   * <context id="child"> <projection id="p_3" type="network" /> <projection
   * id="p_4" type="grid" />
   * 
   * <agent class="anl.gov.agent" /> <agent class="anl.gov.agent2" /> </context>
   * 
   * <context id="child2"> <projection id="p_5" type="network" /> <projection
   * id="p_6" type="grid" /> <projection id="p_7" type="continuous grid" />
   * <projection id="p_8" type="geography" />
   * 
   * <context id="grand_child"> <projection id="p_9" type="network" />
   * <projection id="p_10" type="grid" /> <projection id="p_11"
   * type="continuous grid" /> <projection id="p_12" type="geography" />
   * 
   * <agent class="anl.gov.agent" /> </context> </context>
   * 
   * <agent class="anl.gov.agent3" /> <agent class="anl.gov.agent4" />
   * </context>
   */

  private void testContext(ContextData data) {
    String[][] pExpected = { { "p_1", "NETWORK" }, { "p_2", "GRID" }, { "p_3", "NETWORK" },
	{ "p_4", "GRID" }, { "p_5", "NETWORK" }, { "p_6", "GRID" }, { "p_7", "CONTINUOUS_SPACE" },
	{ "p_8", "GEOGRAPHY" }, { "p_9", "NETWORK" }, { "p_10", "GRID" },
	{ "p_11", "CONTINUOUS_SPACE" }, { "p_12", "GEOGRAPHY" }, };

   
    assertEquals("root", data.getId());
    assertEquals(2, data.getProjectionCount());

    for (int i = 0; i < 2; i++) {
      ProjectionData pData = data.getProjection(i);
      assertEquals(pExpected[i][0], pData.getId());
      assertEquals(pExpected[i][1], pData.getType().toString());
      assertEquals(0, pData.getAttributeCount());
    }
    assertEquals(null, data.getContextClassName());
    
    assertEquals(2, data.getAttributeCount());
    Iterator<Attribute> aIter = data.attributes().iterator();
    Attribute a1 = aIter.next();
    assertEquals("a1", a1.getId());
    assertEquals("a1", a1.getDisplayName());
    assertEquals("Hello", a1.getValue());
    assertEquals(String.class, a1.getType());
    assertEquals(StringConverterFactory.instance().getConverter(String.class), a1.getConverter());
    
    Attribute a2 = aIter.next();
    assertEquals("a2", a2.getId());
    assertEquals("attribute 2", a2.getDisplayName());
    assertEquals("Foo", a2.getValue());
    assertEquals(TestAttributeType.class, a2.getType());
    assertEquals("repast.simphony.score.TestConverter", a2.getConverter().getClass().getName());


    assertEquals(2, data.getSubContextCount());
    ContextData child = data.getSubContext(0);
    assertNotNull(child);
    assertEquals("child", child.getId());
    assertEquals(2, child.getProjectionCount());
    assertEquals(0, child.getAttributeCount());

    for (int i = 0; i < 2; i++) {
      ProjectionData pData = child.getProjection(i);
      assertEquals(pExpected[i + 2][0], pData.getId());
      assertEquals(pExpected[i + 2][1], pData.getType().toString());
      
      if (pData.getId().equals("p_4")) {
        assertEquals(1, pData.getAttributeCount());
        Attribute pAttrib = pData.attributes().iterator().next();
        assertEquals("p_4_height", pAttrib.getId());
        assertEquals("p_4_height", pAttrib.getDisplayName());
        assertEquals("30", pAttrib.getValue());
        assertEquals(int.class, pAttrib.getType());
        assertEquals(StringConverterFactory.instance().getConverter(int.class), pAttrib.getConverter());
      } else {
        assertEquals(0, pData.getAttributeCount());
      }
    }
   

    assertEquals(0, child.getSubContextCount());

    child = data.getSubContext(1);
    assertNotNull(child);
    assertEquals("child2", child.getId());
    assertEquals("anl.gov.MyContext", child.getContextClassName());
    assertEquals(4, child.getProjectionCount());
    assertEquals(0, child.getAttributeCount());
    for (int i = 0; i < 4; i++) {
      ProjectionData pData = child.getProjection(i);
      assertEquals(0, pData.getAttributeCount());
      assertEquals(pExpected[i + 4][0], pData.getId());
      assertEquals(pExpected[i + 4][1], pData.getType().toString());
    }
    assertEquals(0, child.getAgentCount());
    assertEquals(1, child.getSubContextCount());

    child = child.getSubContext(0);
    assertNotNull(child);
    assertEquals("grand_child", child.getId());
    assertEquals(null, child.getContextClassName());
    assertEquals(4, child.getProjectionCount());

    for (int i = 0; i < 4; i++) {
      ProjectionData pData = child.getProjection(i);
      assertEquals(pExpected[i + 8][0], pData.getId());
      assertEquals(pExpected[i + 8][1], pData.getType().toString());
    }
    
    assertEquals(0, child.getSubContextCount());
  }

  @Test
  public void testContextFileReader() throws IOException, XMLStreamException {

    ContextFileReader reader = new ContextFileReader();
    File contextFile = new File("./test/data/context.xml");
    ContextData data = reader.read(contextFile, new Classpath());
    assertNotNull(data);
    testContext(data);

  }
  
  @Test
  public void testContextFileWriter() throws IOException, XMLStreamException {

    ContextFileReader reader = new ContextFileReader();
    File contextFile = new File("./test/data/context.xml");
    ContextData data = reader.read(contextFile, new Classpath());
    assertNotNull(data);
    ContextFileWriter writer = new ContextFileWriter();
    File copy = new File("./test/data/context_copy.xml");
    writer.write(copy, data);
    
    data = reader.read(copy, new Classpath());
    assertNotNull(data);
    testContext(data);
  }

  private Set<String> toSet(String[] vals) {
    Set<String> set = new HashSet<String>();
    for (String val : vals) {
      set.add(val);
    }

    return set;

  }

  @Test
  public void testClasspath() {
    // this list may change as the contents of saf.core.ui.lib chanes
    String[] expected1 = { "dockingFrames-1.0.8p2.jar", "dockingFrames-1.0.8p3a-saf.jar", "dockingFrames-1.0.6.jar", "forms-1.0.5.jar", "jh.jar",
	"l2fprod-common-all.jar", "osx.jar", "TableLayout.jar", "wizard-0.1.12.jar", "bin",
	"colt-1.2.0.jar" };

    Classpath cp = new Classpath();
    assertTrue(cp.addEntry("../saf.core.ui/lib"));
    assertTrue(cp.addEntry("../repast.simphony.core/bin/"));
    assertTrue(cp.addEntry("../repast.simphony.core/lib/colt-1.2.0.jar"));

    Set<String> expected = toSet(expected1);

    for (File path : cp.classPaths()) {
      // System.out.println("file = " + file);
      assertTrue(path.getName(), expected.remove(path.getName()));
    }
    assertEquals(0, expected.size());

    assertTrue(!cp.addEntry("../foo"));
  }

  @Test
  public void testFindClasses() throws IOException, ClassNotFoundException {
    String[] expectedArray = { "saf.core.runtime.Boot$1", "saf.core.runtime.Boot$Restriction",
	"saf.core.runtime.Boot", "saf.core.runtime.CorePlugin",
	"saf.core.runtime.IApplicationRunnable", "saf.core.runtime.PluginAttributes",
	"saf.core.runtime.PluginDefinitionException", "saf.core.runtime.PluginReader",
	"saf.core.runtime.SimpleAttribute", "simphony.settings.SettingsIO",
	"simphony.settings.SettingsRegistry", "simphony.util.Main",
	"simphony.util.ThreadUtilities$1", "simphony.util.ThreadUtilities$Runner",
	"simphony.util.ThreadUtilities", "simphony.util.error.ErrorCenter",
	"simphony.util.error.ErrorEvent", "simphony.util.error.ErrorHandler",
	"simphony.util.messages.AbstractMessageListener",
	"simphony.util.messages.Log4jMessageListener", "simphony.util.messages.MessageCenter",
	"simphony.util.messages.MessageCenterLayout", "simphony.util.messages.MessageEvent",
	"simphony.util.messages.MessageEventListener", "simphony.util.messages.NullTaskMessage",
	"simphony.util.messages.StackTaskMessageListener",
	"simphony.util.messages.TaskMessage$TaskStatus", "simphony.util.messages.TaskMessage",
	"simphony.util.messages.TaskMessageCallback", "simphony.util.messages.TaskMessageListener",
	"repast.simphony.plugin.ExtendablePluginClassLoader",
	"repast.simphony.plugin.PluginLifecycleHandler", "repast.simphony.runtime.RepastMain",
	"repast.simphony.ant.BuildPluginClasspath$1", "repast.simphony.ant.BuildPluginClasspath",
	"repast.simphony.ant.BuildPluginClasspath$NameExtractor",
	"repast.simphony.ant.DummyEntityResolver", "repast.simphony.ant.PluginDescriptor",
	"repast.simphony.ant.PluginHandler", "repast.simphony.ant.PluginLibHandler",
	"repast.simphony.ant.PluginList",

    };

    Set<String> expected = toSet(expectedArray);

    Classpath cp = new Classpath();
    assertTrue(cp.addEntry("../repast.simphony.runtime/lib/saf.core.runtime.jar"));
    assertTrue(cp.addEntry("../repast.simphony.runtime/bin"));
    List<Class<?>> classes = cp.getClasses();
    for (Class<?> clazz : classes) {
      assertTrue(clazz.getName(), expected.remove(clazz.getName()));
    }
    // System.out.println("expected = " + expected);
    assertEquals(0, expected.size());

  }
}
