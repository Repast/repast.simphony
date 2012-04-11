/**
 * 
 */
package repast.simphony.batch;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick Collier
 */
public class LocalDriver {
  
  private List<String> createParameterStrings(String parameterLineFile) throws IOException {
    List<String> list = new ArrayList<String>();
    BufferedReader reader = new BufferedReader(new FileReader(parameterLineFile));
    String line = null;
    while ((line = reader.readLine()) != null) {
      if (line.trim().length() > 0) list.add(line);
    }
    
    return list;
  }
  
  private String concatenateLines(List<String> lines, int start, int end) {
    StringBuilder buf = new StringBuilder();
    for (int i = start; i < end; i++) {
      buf.append(lines.get(i));
      buf.append("\n");
    }
    return buf.toString();
  }
  
  // TODO create N number of working directories as children of the working directory
  // Run an InstanceRunner in each one of those. Probably need a lib directory argument as well.
  public void run(String parameterLineFile, String scenarioFile, String workingDirectory) throws IOException {
    List<String> lines = createParameterStrings(parameterLineFile);
    // concatenate these lines with chunks and send them to different processes as arguments
    String arg = concatenateLines(lines, 0, lines.size());
    
    ProcessBuilder builder = new ProcessBuilder();
    builder.directory(new File(workingDirectory));
    builder.command("java", "-cp", "lib/*", "repast.simphony.batch.InstanceRunner", scenarioFile, arg);
    builder.redirectErrorStream(true);
    try {
      Process process = builder.start();
      writeProcessOutput(process);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  private void writeProcessOutput(Process process) throws IOException {
    InputStreamReader tempReader = new InputStreamReader(
        new BufferedInputStream(process.getInputStream()));
    BufferedReader reader = new BufferedReader(tempReader);
    while (true){
        String line = reader.readLine();
        if (line == null)
            break;
        System.out.println(line);
    }           
    
  }
  
  public static void main(String[] args) {
    LocalDriver driver = new LocalDriver();
    try {
      // test_data will be relative to test_working when run so ".."
      driver.run("./test_out/parameters_exploded.txt", "../test_data/JZombies/JZombies.rs", "./test_working");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
