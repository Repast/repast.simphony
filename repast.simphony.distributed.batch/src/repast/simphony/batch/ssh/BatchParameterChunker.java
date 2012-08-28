/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import repast.simphony.batch.parameter.ParametersToInput;

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

      int instances = 0;
      for (Remote remote : config.remotes()) {
        instances += remote.getInstances();
      }

      ParametersToInput toInput = new ParametersToInput(zipfile.getInputStream(entry));
      // each string is series of lines, consiting of the input for each
      // instances
      List<String> instanceInput = createInstanceInput(instances, toInput.formatForInput());
      // concatenate the input for each remote.
      int index = 0;
      for (Remote remote : config.remotes()) {
        StringBuilder builder = new StringBuilder();
        for (int i = index; i < index + remote.getInstances(); i++) {
          builder.append(instanceInput.get(i));
        }
        remote.setInput(builder.toString());
        index += remote.getInstances();
      }
      
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

  private List<String> createInstanceInput(int instances, List<String> lines) {

    List<StringBuilder> inputs = new ArrayList<StringBuilder>();
    for (int i = 0; i < instances; i++) {
      inputs.add(new StringBuilder());
    }

    int i = 0;
    for (String line : lines) {
      StringBuilder builder = inputs.get(i);
      builder.append(line);
      builder.append("\n");
      i++;
      if (i == instances)
        i = 0;
    }

    List<String> list = new ArrayList<String>();
    for (StringBuilder builder : inputs) {
      list.add(builder.toString());
    }

    return list;
  }
}
