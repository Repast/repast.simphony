/**
 * 
 */
package repast.simphony.batch.parameter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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

  private static class Iter implements Iterator<String> {

    private int batchRun = 1;
    private List<String> paramNames = new ArrayList<>();
    private Parameters params;
    private ParameterTreeSweeper sweeper;
    private String next = null;

    public Iter(XMLSweeperProducer producer) throws IOException {
      params = producer.getParameters();

      for (String pName : params.getSchema().parameterNames()) {
        paramNames.add(pName);
      }

      sweeper = producer.getParameterSweeper();
      formatNext();
    }

    private void formatNext() {
      next = null;
      if (!sweeper.atEnd()) {
        sweeper.next(params);
        StringBuilder buf = new StringBuilder(String.valueOf(batchRun));
        buf.append("\t");

        boolean addComma = false;
        for (String pName : paramNames) {
          if (addComma)
            buf.append(",");
          buf.append(pName);
          buf.append("\t");
          buf.append(params.getValueAsString(pName));
          addComma = true;
        }

        batchRun++;
        next = buf.toString();
      }
    }

    @Override
    public boolean hasNext() {
      return next != null;
    }

    @Override
    public String next() {
      if (next == null) throw new NoSuchElementException();
      String tmp = next;
      formatNext();
      return tmp;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Gets an iterator over the formatted input. Each element returned
   * by the iterator is a batch parameter combination in "line" format.
   * 
   * @return an iterator over the formatted input. Each element returned
   * by the iterator is a batch parameter combination in "line" format.
   * @throws IOException
   */
  public Iterator<String> formatForInput() throws IOException {
    return new Iter(producer);
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
