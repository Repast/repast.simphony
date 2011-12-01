package repast.simphony.engine.watcher;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WatcheeData {
  
  String className;
  Set<String> fields = new HashSet<String>();
  URL path;

  public WatcheeData(String className) {
    this.className = className;
  }

  public void addField(String fieldName) {
    fields.add(fieldName);
  }
  
  public void addFields(Collection<String> fields) {
    this.fields.addAll(fields);
  }
  
  public String toString() {
    return "[" + className + ": " + fields + "]";
  }
}