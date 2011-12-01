package repast.simphony.visualization.visualization3D;

import javax.media.j3d.Behavior;

import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualization.visualization3D.style.ValueLayerStyle3D;


/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public interface ValueDisplayLayer3D {

  void setStyle(ValueLayerStyle3D style);

  void init(Behavior masterBehavior);

  void update();

  void applyUpdates();

  void addDataLayer(ValueLayer layer);
}
