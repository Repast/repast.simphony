/**
 * 
 */
package repast.simphony.conversion;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Collects user_path data extracted from a score file.
 * @author Nick Collier
 */
public class ScorePathData {
  
  static class Pair {
    String first, second;
    
    public Pair(String first, String second) {
      this.first = first;
      this.second = second;
    }
    
    public String getFirst() {
      return first;
    }
    
    public String getSecond() {
      return second;
    }
  }
  
  private String name;
  private Deque<Pair> stack = new LinkedList<Pair>();
  private List<String> agents = new ArrayList<String>();
  private List<String> entries = new ArrayList<String>();
  
  public ScorePathData(String name) {
    this.name = name;
  }
  
  public String getCurrentPath() {
    return stack.getFirst().getFirst();
  }
  
  public String getCurrentPkg() {
    return stack.getFirst().getSecond();
  }
  
  /**
   * Duplicates the path on the top of stack,
   * pushing the duplicate on to the top.
   */
  public void dupStack() {
    stack.addFirst(stack.getFirst());
  }

  
  public void pushStack(String path, String pkg) {
    if (!pkg.endsWith(".")) pkg += ".";
    stack.addFirst(new Pair(path, pkg));
  }
  
  public void popStack() {
    stack.removeFirst();
  }
  
  public void addEntry(String path) {
    entries.add(String.format("<entry path=\"%s\" annotations=\"false\"/>%n", path));
  }
  
  public void addAgent(String className) {
    Pair data = stack.getFirst();
    String path = data.getFirst() + "/bin," + data.getFirst() + "/bin-groovy";
    String agent = String.format("<agents path=\"%s\" filter=\"%s\"/>%n", path, data.getSecond() + className);
    agents.add(agent);
  }
  
  public void write(Writer out) throws IOException {
    String sep = System.getProperty("line.separator");
    out.write(String.format("<model name=\"%s\">%n", name));
    out.write("<classpath>");
    out.write(sep);
    for (String entry : agents) {
      out.write(entry);
    }
    for (String entry : entries) {
      out.write(entry);
    }
    out.write("</classpath>");
    out.write(sep);
    out.write("</model>");
    out.write(sep);
    out.flush();
  }

}
