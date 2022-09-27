package repast.simphony.scenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

/**
 * Tests the Repast Simphony Xstream converter.
 * 
 * @author Eric Tatara
 */
public class ScenarioLoaderTests {

  @Test
  public void testLoadAbstractDescriptor() {

    XStream xstream = new XStream(new XppDriver(new XmlFriendlyNameCoder())) {
      protected boolean useXStream11XmlFriendlyMapper() {
        return true;
      }
    };

    xstream.allowTypesByRegExp(new String[] { "repast.simphony.*" });
//    xstream.addPermission(AnyTypePermission.ANY);
    
    BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("./test/data/TestDescriptor_1.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    TestDescriptor descriptor = (TestDescriptor)(xstream.fromXML(reader));
   
    int foo = descriptor.getFoo();
    String name = descriptor.getName();
    
    assertEquals(14, foo);
    assertEquals("Test Desc 1", name);
    assertNotNull(descriptor.getScs());  // Should be initialized on deserialization
   
  }
}
