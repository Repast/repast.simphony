package repast.simphony.engine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test of HadoopDataSink.
 * 
 * @author Nick Collier
 */
public class HadoopDataSinkTest {
  
  @Test
  public void testFormatter() {
    List<TestDataSource> sources = new ArrayList<TestDataSource>();
    sources.add(new TestDataSource("A"));
    sources.add(new TestDataSource("B"));
    sources.add(new TestDataSource("C"));
    HadoopFormatter formatter = new HadoopFormatter(sources);
    
    for (int i = 0; i < 10; i++) {
      formatter.addData("A", sources.get(0).get(null));
      formatter.addData("B", sources.get(1).get(null));
      formatter.addData("C", sources.get(2).get(null));
      
      String row = formatter.formatData();
      assertEquals(sources.get(0).getLastVal(), parseRow("A", row));
      assertEquals(sources.get(1).getLastVal(), parseRow("B", row));
      assertEquals(sources.get(2).getLastVal(), parseRow("C", row));
    }
  }
  
  private Integer parseRow(String key, String row) {
    String[] vals = row.split(",");
    for (String item : vals) {
      if (item.startsWith(key)) {
        return new Integer(item.split("\t")[1].trim());
      }
    }
    
    return null;
  }

}
