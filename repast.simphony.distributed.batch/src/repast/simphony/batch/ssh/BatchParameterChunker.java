/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import repast.simphony.batch.parameter.ParametersToInput;
import repast.simphony.parameter.ParameterTreeSweeper;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.xml.XMLSweeperProducer;

/**
 * Takes an ssh run configuration object and reads the batch parameter xml out
 * of the archive, generating in each parameter combination in a line format.
 * This then groups all those lines into chunks suitable to send to each remote.
 * 
 * @author Nick Collier
 */
public class BatchParameterChunker {

  private Configuration config;

  public BatchParameterChunker(Configuration config) {
    this.config = config;
  }

  private int getRunCount(XMLSweeperProducer producer) throws IOException {
    int count = 0;
    ParameterTreeSweeper sweeper = producer.getParameterSweeper();
    Parameters params = producer.getParameters();
    while (!sweeper.atEnd()) {
      sweeper.next(params);
      count++;
    }

    return count;
  }

  public void run() throws BatchParameterChunkerException {
    ZipFile zipfile = null;
    ZipEntry entry = null;

    try {
      zipfile = new ZipFile(config.getModelArchive());
      entry = zipfile.getEntry(config.getBatchParamsFile());
      if (entry == null)
        throw new BatchParameterChunkerException(String.format(
            "Unable to find batch param file '%s' in model archive '%s'",
            config.getBatchParamsFile(), config.getModelArchive()));

      int runCount = getRunCount(new XMLSweeperProducer(zipfile.getInputStream(entry)));
      ParametersToInput toInput = new ParametersToInput(zipfile.getInputStream(entry));

      // create a file for each session that contains all the run parameter
      // combos
      // to run for that session
      createInstanceInput(toInput, runCount);

    } catch (IOException ex) {
      throw new BatchParameterChunkerException(ex);

    } catch (ParserConfigurationException e) {
      throw new BatchParameterChunkerException(e);

    } catch (SAXException e) {
      throw new BatchParameterChunkerException(e);
    } finally {
      if (zipfile != null)
        try {
          zipfile.close();
        } catch (IOException e) {
        }
    }

  }

  // Creates a file for session. Each file contains the parameter combos
  // to run in that session.
  private void createInstanceInput(ParametersToInput toInput, int runCount) throws IOException {

    System.out.println(runCount);

    int instances = 0;
    for (Session session : config.sessions()) {
      instances += session.getInstances();
    }

    // calculate how many runs per instance
    long[] counts = new long[instances];
    int val = runCount / instances;
    for (int i = 0; i < counts.length; ++i) {
      counts[i] = val;
    }

    long diff = runCount - (val * instances);
    if (diff > 0) {
      // add in the remainder
      for (int i = 0; i < counts.length; ++i) {
        counts[i] = counts[i] + 1;
        diff--;
        if (diff == 0)
          break;
      }
    }

    Iterator<String> lines = toInput.formatForInput();

    int start = 0;
    int index = 1;

    // for each session calculate the number of
    // runs based on how many instances that session
    // should run
    for (Session session : config.sessions()) {
      int end = start + session.getInstances();
      int numRuns = 0;
      for (int i = start; i < end; ++i) {
        numRuns += counts[i];
      }

      System.out.println(numRuns);
      start = end;
      File file = new File(System.getProperty("java.io.tmpdir"), new Date().getTime()
          + "unrolled_params_" + index + ".txt");
      session.setInput(file.getCanonicalPath());
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        for (int i = 0; i < numRuns; ++i) {
          writer.append(lines.next());
          writer.append("\n");
        }
      } catch (NoSuchElementException ex) {
        ex.printStackTrace();
      }
      ++index;
    }
  }
}
