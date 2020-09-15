package repast.simphony.visualization.visualization3D;

import org.jogamp.java3d.BranchGroup;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the adding and removing of VisualItem3Ds.
 *
 * @author Nick Collier
 */
public class VisualItem3DAdderRemover {
  
  private Set<VisualItem3D> itemsToAdd = new HashSet<VisualItem3D>();
  private Set<VisualItem3D> itemsToRemove = new HashSet<VisualItem3D>();
  
  public void clear() {
    itemsToAdd.clear();
    itemsToRemove.clear();
  }
  
  public void addItemForAddition(VisualItem3D item) {
    itemsToAdd.add(item);
  }
  
  public void addItemForRemoval(VisualItem3D item) {
    // don't add the item for reomval if we can just remove
    // it from the items to add and thus we never add it,
    // so don't need to remove it.
    if (!itemsToAdd.remove(item)) itemsToRemove.add(item);
  }
  
  public boolean isNew(VisualItem3D item) {
    return itemsToAdd.contains(item);
  }
  
  public void execute(BranchGroup group) {
    for (VisualItem3D item: itemsToAdd) {
      item.addTo(group);
    }
    
    for (VisualItem3D item: itemsToRemove) {
      item.removeFrom(group);
    }
    
    clear();
  }
}
