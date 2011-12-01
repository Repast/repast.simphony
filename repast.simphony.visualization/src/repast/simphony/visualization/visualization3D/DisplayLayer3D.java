package repast.simphony.visualization.visualization3D;

import java.util.Iterator;

import javax.media.j3d.Group;
import javax.media.j3d.Shape3D;

import repast.simphony.visualization.Layout;
import repast.simphony.visualization.visualization3D.style.DefaultStyle3D;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/06 22:53:54 $
 */
public class DisplayLayer3D extends AbstractDisplayLayer3D {

  public DisplayLayer3D(Style3D style, Group topGroup) {
    super(style, topGroup);
    if (style == null) this.style = new DefaultStyle3D();
  }
  
  protected VisualItem3D createVisualItem(Object obj) {
    TaggedBranchGroup rep = style.getBranchGroup(obj, null);
    VisualItem3D item = new NodeVisualItem(rep, obj, createLabel());
    for (Iterator<Shape3D> iter = item.shapes(); iter.hasNext();) {
      Shape3D shape = iter.next();
      TaggedAppearance attrib = style.getAppearance(obj, null, shape.getUserData());
      item.setShapeAppearance(shape, attrib);
    }

    return item;
  }

  protected void createItemsForAddedObjects(Layout layout, boolean doLayout) {
    if (doLayout) {
      for (Object obj : objsToAdd) {
        VisualItem3D item = createVisualItem(obj);
        visualItemMap.put(obj, item);
        // set the location
        float[] location = layout.getLocation(obj);
        item.setLocation(location);

        adder.addItemForAddition(item);
      }
    } else {
      for (Object obj : objsToAdd) {
        VisualItem3D item = createVisualItem(obj);
        visualItemMap.put(obj, item);
        adder.addItemForAddition(item);
      }
    }

    objsToAdd.clear();
  }

  protected Label createLabel() {
    return new AgentLabel();
	  //return new NullLabel();
  }

  public void doUpdate(Layout layout, boolean updateLocation) {
    if (updateLocation) {
      for (VisualItem3D item : visualItemMap.values()) {
        item.updateLocation(layout);
        item.updateLabel(style);
        item.updateScale(style);
        item.updateRotation(style);
        item.updateTaggedBranchGroup(style);
        item.updateAppearance(style);
      }
    } else {
      // no layout performed so don't set the location
      for (VisualItem3D item : visualItemMap.values()) {
        item.updateLabel(style);
        item.updateScale(style);
        item.updateRotation(style);
        item.updateTaggedBranchGroup(style);
        item.updateAppearance(style);
      }
    }
  }
}
