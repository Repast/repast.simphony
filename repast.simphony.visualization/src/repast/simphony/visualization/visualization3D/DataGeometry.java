package repast.simphony.visualization.visualization3D;

import javax.media.j3d.Geometry;

import repast.simphony.visualization.visualization3D.style.ValueLayerStyle3D;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public interface DataGeometry {
  
  Geometry getGeometry(ValueLayerStyle3D style);
  void update(ValueLayerStyle3D style);
  void applyUpdate(Geometry geometry);
  
}
