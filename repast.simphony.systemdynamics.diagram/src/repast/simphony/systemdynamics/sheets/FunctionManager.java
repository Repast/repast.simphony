/**
 * 
 */
package repast.simphony.systemdynamics.sheets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the list of functions in the equation editor. 
 * 
 * @author Nick Collier
 */
public class FunctionManager {
  
  private static final String FUNC_FILE = "functions.txt";
  private static final String LOOKUP = "Lookup";
  private static final String[] FUNC_SETS = {"Common", LOOKUP};
  
  private static FunctionManager instance;
  
  public static FunctionManager getInstance() {
    if (instance == null) instance = new FunctionManager();
    return instance;
  }
  
  private Map<String, String> funcMap = new HashMap<String, String>();
  private Map<String, List<String>> setMap = new HashMap<String, List<String>>();
  
  private FunctionManager() {
    for (String key : FUNC_SETS) {
      setMap.put(key.toLowerCase(), new ArrayList<String>());
    }
    BufferedReader reader = null;
    try {
     reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(FUNC_FILE)));
     String line = null;
     while ((line = reader.readLine()) != null) {
       line = line.trim();
       if (!line.startsWith("#") && line.length() > 0) {
         String[] vals = line.split(":");
         funcMap.put(vals[0].trim(), vals[1].trim());
         String[] sets = vals[2].trim().split(",");
         for (String set : sets) {
           setMap.get(set.trim()).add(vals[0].trim());
         }
       }
     }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    } finally {
      try {
      if (reader != null) reader.close();
      } catch (IOException ex) {}
    }
  }
  
  /**
   * Gets the function set names.
   *
   * @return the function set names.
   */
  public String[] getFunctionSetNames() {
    return FUNC_SETS;
  }
  
  public void addLookup(String name) {
    setMap.get(LOOKUP.toLowerCase()).add(name);
  }
  
  public void clearLookups() {
    setMap.get(LOOKUP.toLowerCase()).clear();
  }
  
  /**
   * Gets an array of the function names.
   * 
   * @return an array of the function names.
   */
  public String[] getFunctionNames(String setName) {
    List<String> names = new ArrayList<String>(setMap.get(setName.toLowerCase()));
    Collections.sort(names);
    return names.toArray(new String[names.size()]);
  }
  
  /**
   * Gets the function pattern for the specified function name.
   * 
   * @param name
   * @return the function pattern for the specified name.
   */
  public String getFunctionPattern(String name) {
    String pattern = funcMap.get(name);
    if (pattern == null) {
      // assume its a lookup and add "(X)"
      pattern = "(X)";
    }
    return pattern;
  }

}
