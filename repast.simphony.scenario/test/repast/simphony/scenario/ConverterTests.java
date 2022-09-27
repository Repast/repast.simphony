package repast.simphony.scenario;

import static org.junit.Assert.*;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

/**
 * Tests the Repast Simphony Xstream converter.
 * 
 * @author Nick Collier
 */
public class ConverterTests {

  @Test
  public void testConverter() {

    XStream xstream = new XStream(new XppDriver(new XmlFriendlyNameCoder())) {
      protected boolean useXStream11XmlFriendlyMapper() {
        return true;
      }
    };

    xstream.addPermission(AnyTypePermission.ANY);
    
    xstream.registerConverter(new FastMethodConvertor(xstream));
    xstream.registerConverter(
        new DescriptorConverter(xstream.getMapper(), xstream.getReflectionProvider(), "1.0"));
    
    PersonDescriptor p = new PersonDescriptor();
    p.setName("nick");
   
    String xml = xstream.toXML(p);
    
    assertTrue(xml.startsWith("<repast.simphony.scenario.PersonDescriptor simphonyVersion=\"1.0\">"));
    assertTrue(xml.contains("<name>nick</name>"));

    PersonDescriptor p1 = (PersonDescriptor) xstream.fromXML(xml);
    assertEquals("nick", p1.getName());
  }
}
