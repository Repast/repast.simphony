/**
 * 
 */
package repast.simphony.parameter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import repast.simphony.parameter.xml.XMLSweeperProducer;

/**
 * @author Nick Collier
 */
public class ParametersToInput {

  private XMLSweeperProducer producer;
  private int batchRun;

  public ParametersToInput(File paramsFile) throws MalformedURLException,
      ParserConfigurationException, SAXException, IOException {
    producer = new XMLSweeperProducer(paramsFile.toURI().toURL());

  }

  public void formatForInput(File output) throws IOException {
    batchRun = 1;
    Parameters params = producer.getParameters();
    ParameterTreeSweeper sweeper = producer.getParameterSweeper();

    BufferedWriter writer = null;
    try {
      if (!output.getParentFile().exists()) {
        output.getParentFile().mkdirs();
      }
      
      writer = new BufferedWriter(new FileWriter(output));
      while (!sweeper.atEnd()) {
        sweeper.next(params);
        write(writer, params);
      }
      
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }
  
  private void write(BufferedWriter writer, Parameters params) throws IOException {
    writer.write("batchRun_");
    writer.write(String.valueOf(batchRun));
    writer.write("\t");
    
    boolean addComma = false;
    for (String pName : params.getSchema().parameterNames()) {
      if (addComma) writer.write(",");
      writer.write(pName);
      writer.append("\t");
      writer.append(params.getValueAsString(pName));
      addComma = true;
    }
    writer.append("\n");
    batchRun++;
  }
  
  public static void main(String[] args) {
    File params = new File(args[0]);
    File output = new File(args[1]);
    
    try {
      new ParametersToInput(params).formatForInput(output);
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
    }
    
  }
}
