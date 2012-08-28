/**
 * 
 */
package repast.simphony.batch.parameter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import repast.simphony.data2.BatchRunDataSource;
import repast.simphony.parameter.ParameterTreeSweeper;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.xml.XMLSweeperProducer;

/**
 * @author Nick Collier
 */
public class ParametersToInput {

  private XMLSweeperProducer producer;
  private int batchRun;
  private List<String> paramNames = new ArrayList<String>();

  public ParametersToInput(File paramsFile) throws MalformedURLException,
      ParserConfigurationException, SAXException, IOException {
    producer = new XMLSweeperProducer(paramsFile.toURI().toURL());
  }

  public ParametersToInput(InputStream in) throws MalformedURLException,
      ParserConfigurationException, SAXException, IOException {
    producer = new XMLSweeperProducer(in);
  }

  /**
   * Writes each batch parameter combination in line format and returns each
   * line in the list of strings.
   * 
   * @return a list of each string where each string a batch parameter
   *         combination in "line" format.
   * @throws IOException
   */
  public List<String> formatForInput() throws IOException {
    batchRun = 1;
    Parameters params = producer.getParameters();
    paramNames.clear();

    for (String pName : params.getSchema().parameterNames()) {
      paramNames.add(pName);
    }

    ParameterTreeSweeper sweeper = producer.getParameterSweeper();

    StringWriter writer = new StringWriter();
    while (!sweeper.atEnd()) {
      sweeper.next(params);
      write(writer, params);
      batchRun++;
    }
    
    return Arrays.asList(writer.toString().split("\n"));
  }

  /**
   * Writes each batch parameters combination as a line in two files. The first
   * will be in "line" format, and the second will be in the standard repast
   * batch parameter map format.
   * 
   * @param output
   * @param batchMapFile
   * @throws IOException
   */
  public void formatForInput(File output, File batchMapFile) throws IOException {
    batchRun = 1;
    Parameters params = producer.getParameters();
    paramNames.clear();

    for (String pName : params.getSchema().parameterNames()) {
      paramNames.add(pName);
    }

    ParameterTreeSweeper sweeper = producer.getParameterSweeper();

    BufferedWriter hWriter = null;
    BufferedWriter mWriter = null;
    try {
      if (!output.getParentFile().exists()) {
        output.getParentFile().mkdirs();
      }

      hWriter = new BufferedWriter(new FileWriter(output));
      mWriter = new BufferedWriter(new FileWriter(batchMapFile));
      writeHeader(mWriter);
      while (!sweeper.atEnd()) {
        sweeper.next(params);
        write(hWriter, params);
        writeMapFormat(mWriter, params);

        batchRun++;
      }

    } finally {
      if (hWriter != null) {
        hWriter.close();
      }

      if (mWriter != null)
        mWriter.close();
    }
  }

  private void writeHeader(BufferedWriter writer) throws IOException {
    writer.write(BatchRunDataSource.ID);
    for (String pName : paramNames) {
      writer.write(",");
      writer.write("\"");
      writer.write(pName);
      writer.write("\"");
    }
    writer.append("\n");
  }

  private void writeMapFormat(BufferedWriter writer, Parameters params) throws IOException {
    writer.write(String.valueOf(batchRun));
    for (String pName : paramNames) {
      writer.write(",");
      writer.write(params.getValueAsString(pName));
    }
    writer.append("\n");
  }

  private void write(Writer writer, Parameters params) throws IOException {
    writer.write(String.valueOf(batchRun));
    writer.write("\t");

    boolean addComma = false;
    for (String pName : paramNames) {
      if (addComma)
        writer.write(",");
      writer.write(pName);
      writer.append("\t");
      writer.append(params.getValueAsString(pName));
      addComma = true;
    }
    writer.append("\n");
  }

  public static void main(String[] args) {
    File params = new File(args[0]);
    File output = new File(args[1]);
    File mapFile = new File(args[2]);

    try {
      new ParametersToInput(params).formatForInput(output, mapFile);
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
    }

  }
}
