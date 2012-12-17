/**
 * 
 */
package repast.simphony.statecharts.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Expands any short cuts in the code blocks to longer form 
 * valid code. 
 * 
 * @author Nick Collier
 */
public class CodeExpander {
  
  private Pattern pt = Pattern.compile("((?:(?:\"[^\"]*\")|[^;])*);");
  
  public String expand(String code, boolean addReturn) {
    BufferedReader reader = new BufferedReader(new StringReader(code));
    String line = null;
    List<String> results = new ArrayList<String>();
    try {
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.length() > 0) {
          if (!line.endsWith(";")) {
            line = line + ";";
          }
          Matcher m = pt.matcher(line.trim());
          while (m.find()) {
            String l = m.group(1).trim();
            if (l.length() > 0) {
              results.add(processLine(l));
            }
          }
        }
      }
    } catch (IOException e) {}
    
    if (addReturn) {
      String lastLine = results.get(results.size() - 1);
      if (!lastLine.startsWith("return")) {
        results.set(results.size() - 1, "return " + lastLine);
      }
    }
    
    StringBuilder buf = new StringBuilder();
    for (String l : results) {
      buf.append(l);
      buf.append("\n");
    }
    return buf.toString();
  }
  
  private String processLine(String line) {
    if (!(line.endsWith(";") || line.endsWith("{") || line.endsWith("}"))) {
      return line + ";";
    }
    return line;
  }

}
