/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.io.IOException;
import java.util.Map;

import saf.v3d.NamedShapeCreator;
import saf.v3d.ShapeFactory2D;

/**
 * Abstract base class for creating named shape spatials. Child classes
 * need only implement createShape(NamedShapeCreator creator).
 * 
 * @author Nick Collier
 */
public abstract class NamedSpatialSoure implements SpatialSource {
  
  protected String id;

  public NamedSpatialSoure(String id) {
    this.id = id;
  }

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.SpatialSource#getID()
   */
  public String getID() {
    return id;
  }

  protected abstract void createShape(NamedShapeCreator creator);

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.SpatialSource#registerSource(saf.v3d.ShapeFactory2D, java.util.Map)
   */
  public void registerSource(ShapeFactory2D shapeFactory, Map<String, String> props)
      throws IOException {
  
    NamedShapeCreator creator = shapeFactory.createNamedShape(id);
    createShape(creator);
    creator.registerShape();

  }

}
