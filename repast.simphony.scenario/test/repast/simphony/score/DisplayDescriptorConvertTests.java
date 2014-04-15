package repast.simphony.score;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import repast.simphony.scenario.data.DisplayDescriptorXMLConverter;

public class DisplayDescriptorConvertTests {
  
  private static final String PROJ_DATA_EXPECTED = "<repast.simphony.scenario.data.ProjectionData><id>A Network</id><attributes/>" +
  		"<type>NETWORK</type></repast.simphony.scenario.data.ProjectionData><repast.simphony.scenario.data.ProjectionData>" +
  		"<id>Continuous Space</id><attributes/><type>CONTINUOUS_SPACE</type></repast.simphony.scenario.data.ProjectionData>";
  
  private static final String PROJ_1 = "<proj reference=\"../../../../projections/repast.simphony.scenario.data.ProjectionData\">";
  private static final String PROJ_2 = "<proj reference=\"../../../../projections/repast.simphony.scenario.data.ProjectionData[2]\">";
  
  @Test
  public void testConversion() throws XMLStreamException, IOException {
    DisplayDescriptorXMLConverter converter = new DisplayDescriptorXMLConverter();
    StringWriter writer = new StringWriter();
    
    converter.convert(new File("./test/data/repast.simphony.action.display_4.xml"), writer);
    
    String converted = writer.getBuffer().toString();
    
    converted = converted.replace("\n", "");
    converted = converted.replace("\r\n", "");
    converted = converted.replace("\r", "");
    
    assertTrue(converted.contains(PROJ_DATA_EXPECTED));
    assertTrue(converted.contains(PROJ_1));
    assertTrue(converted.contains(PROJ_2));
  }
}
