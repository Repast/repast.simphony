package repast.simphony.engine.watcher;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class WatcheeDataNode {
  
  //private String className;
  private Set<WatcheeDataNode> children = new HashSet<WatcheeDataNode>();
  private WatcheeDataNode parent;
  WatcheeData data;
  
  public WatcheeDataNode(WatcheeData data) {
    this.data = data;
  }
  
  public WatcheeData getData() {
    return data;
  }
  
  public void addChild(WatcheeDataNode node) {
    node.parent = this;
    children.add(node);
  }
  
  public Collection<WatcheeDataNode> children() {
    return Collections.unmodifiableSet(children);
  }
  
  public boolean isRoot() {
    return parent == null;
  }
}