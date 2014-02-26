/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Parses output patterns from a configuration file to a list of OutputPatterns.
 * 
 * @author Nick Collier
 */
public class OutputPatternPropsParser {

  private static class OutputPatternBuilder {

    private Map<Integer, OutputPattern> patterns = new HashMap<>();

    private OutputPattern getPattern(int id) {
      OutputPattern pattern = patterns.get(id);
      if (pattern == null) {
        pattern = new OutputPattern();
        patterns.put(id, pattern);
      }
      return pattern;
    }

    public void setPattern(int id, String pattern) {
      OutputPattern oPattern = getPattern(id);
      oPattern.setPattern(pattern);
    }

    public void setPath(int id, String path) {
      OutputPattern oPattern = getPattern(id);
      oPattern.setPath(path);
    }

    public void setConcat(int id, boolean concat) {
      OutputPattern oPattern = getPattern(id);
      oPattern.setConcatenate(concat);
    }

    public void setHeader(int id, boolean header) {
      OutputPattern oPattern = getPattern(id);
      oPattern.setHeader(header);
    }

    public Collection<OutputPattern> getPatterns() throws IOException {
      for (Map.Entry<Integer, OutputPattern> entry : patterns.entrySet()) {
        OutputPattern pattern = entry.getValue();
        if (pattern.getPath() == null) {
          throw new IOException("Invalid output pattern: output pattern " + entry.getKey()
              + " is missing a path value");
        }

        if (pattern.getPattern() == null) {
          throw new IOException("Invalid output pattern: output pattern " + entry.getKey()
              + " is missing a pattern value");
        }
      }

      return patterns.values();
    }
  }

  /**
   * Parse the output patterns defined in the specified file into a List of
   * OutputPattern objects.
   * 
   * @param file
   * @return the parsed list.
   * 
   * @throws IOException
   */
  public List<OutputPattern> parse(String file) throws IOException {
    Properties props = new Properties();
    props.load(new FileReader(file));
    return run(props);
  }

  /**
   * Parse the output patterns defined in the specified properties into a List
   * of OutputPattern objects.
   * 
   * @param file
   * @return the parsed list.
   * 
   * @throws IOException
   */
  public List<OutputPattern> parse(Properties props) throws IOException {
    return run(props);
  }

  private List<OutputPattern> run(Properties props) throws IOException {
    OutputPatternBuilder builder = new OutputPatternBuilder();
    for (Object key : props.keySet()) {
      if (key.toString().startsWith(Configuration.PATTERN_PREFIX)) {
        String[] vals = key.toString().trim().split("\\.");
        checkVals(key.toString(), vals);
        int id = Integer.parseInt(vals[2].trim());
        setValue(builder, id, vals[3].trim(), props.getProperty(key.toString()));
      }
    }

    return new ArrayList<OutputPattern>(builder.getPatterns());
  }

  private void setValue(OutputPatternBuilder builder, int id, String type, String val)
      throws IOException {
    if (type.equals(Configuration.PATTERN)) {
      builder.setPattern(id, val.trim());
    } else if (type.equals(Configuration.PATH)) {
      builder.setPath(id, val.trim());
    } else if (type.equals(Configuration.HEADER)) {
      builder.setHeader(id, Boolean.parseBoolean(val.trim()));
    } else if (type.equals(Configuration.CONCATENATE)) {
      builder.setConcat(id, Boolean.parseBoolean(val.trim()));
    }
  }

  private void checkVals(String key, String[] vals) throws IOException {
    if (vals.length != 4)
      throw new IOException(
          "Invalid properties configuration for '"
              + key
              + "': expected output.pattern.X.[pattern|path|header|concatenate] where X is a numeric id");

    try {
      Integer.parseInt(vals[2].trim());
    } catch (NumberFormatException ex) {
      throw new IOException("Invalid output pattern configuration:" + key);
    }

    if (!(vals[3].equals(Configuration.PATTERN) || vals[3].equals(Configuration.PATH)
        || vals[3].equals(Configuration.HEADER) || vals[3].equals(Configuration.CONCATENATE))) {
      throw new IOException("Invalid output pattern configuration: " + key);
    }
  }
}
