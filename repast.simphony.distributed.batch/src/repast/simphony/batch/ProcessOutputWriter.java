/**
 * 
 */
package repast.simphony.batch;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Writes the output of a Process to a log file.
 * 
 * @author Nick Collier
 */
public class ProcessOutputWriter {

  private File file;
  private boolean showInConsole = false;

  public ProcessOutputWriter(File file, boolean showInConsole) {
    this.file = file;
    this.showInConsole = showInConsole;
  }
  
  public ProcessOutputWriter(File file) {
    this(file, false);
  }

  public void captureOutput(Process process) throws IOException {
    InputStreamReader tempReader = new InputStreamReader(new BufferedInputStream(
        process.getInputStream()));
    BufferedReader reader = new BufferedReader(tempReader);
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(file));
      String line = "";
      while ((line = reader.readLine()) != null) {
        writer.write(line);
        writer.write("\n");
        if (showInConsole) System.out.println(line);
      }
    } finally {
      if (writer != null)
        writer.close();
    }
  }
}
