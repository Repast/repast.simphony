package repast.simphony.visualization.visualization3D;

import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryUpdater;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class DataGeometryUpdater implements GeometryUpdater {
  
  private DataGeometry dataGeometry;

  public DataGeometryUpdater(DataGeometry dataGeometry) {
    this.dataGeometry = dataGeometry;
  }

  public void updateData(Geometry geometry) {
    dataGeometry.applyUpdate(geometry);
  }
}
