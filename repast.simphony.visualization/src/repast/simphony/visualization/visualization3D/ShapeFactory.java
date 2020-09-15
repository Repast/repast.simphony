package repast.simphony.visualization.visualization3D;

import org.jogamp.java3d.utils.geometry.*;
import org.jogamp.java3d.utils.picking.PickTool;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Color4f;
import org.jogamp.vecmath.Point3f;
import java.awt.*;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class ShapeFactory {

  /**
   * Creates a sphere with the specified radius and number of divisions.
   *
   * @param radius
   * @param divisions the number of divisions in the sphere. More look better
   *                  but less is faster.
   * @param id        the id of the object.
   * @return the created Sphere as a Shape3D.
   */
  public static Shape3D createSphere(float radius, int divisions, Object id,
                                     int primFlags, Appearance appearance) {

    Sphere sphere = new Sphere(radius, primFlags, divisions, null);
    Shape3D shape = new Shape3D(sphere.getShape().getGeometry(), appearance);

    shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);

    Geometry geom = shape.getGeometry();
    if (!(geom.isLive() || geom.isCompiled())) {
      PickTool.setCapabilities(shape, PickTool.INTERSECT_FULL);
    }

    shape.setUserData(id);
    return shape;
  }

  public static Shape3D createSphere(float radius, Object id) {
    return createSphere(radius, 15, id, Sphere.GENERATE_NORMALS, null);
  }

  public static Shape3D createCube(float edgeLength, Object id) {
    return ShapeFactory.createBox(edgeLength, edgeLength, edgeLength, id, Primitive.GENERATE_NORMALS, null);
  }

  public static Shape3D createArrowHead(float height, Object id) {
    LineArray la = new LineArray(4, LineArray.COORDINATES);
    float halfHeight = height / 2;
    la.setCoordinate(0, new Point3f(-halfHeight / 2, -halfHeight, 0));
    la.setCoordinate(1, new Point3f(0, halfHeight, 0));
    la.setCoordinate(2, new Point3f(0, halfHeight, 0));
    la.setCoordinate(3, new Point3f(halfHeight / 2, -halfHeight, 0));
    Shape3D shape = new Shape3D(la);
    if (!(la.isLive() || la.isCompiled())) {
      PickTool.setCapabilities(shape, PickTool.INTERSECT_FULL);
    }
    shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    shape.setUserData(id);
    return shape;
  }

  public static Shape3D createLine(float length, Object id) {
    LineArray la = new LineArray(2, LineArray.COORDINATES);
    la.setCapability(LineArray.ALLOW_COORDINATE_WRITE);
    la.setCapability(LineArray.ALLOW_COORDINATE_READ);
    la.setCoordinate(0, new Point3f(0, -(length / 2), 0));
    la.setCoordinate(1, new Point3f(0, length / 2, 0));
    Shape3D shape = new Shape3D(la);
    if (!(la.isLive() || la.isCompiled())) {
      PickTool.setCapabilities(shape, PickTool.INTERSECT_FULL);
    }
    shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
    shape.setUserData(id);
    return shape;
  }

  public static Shape3D createCylinder(float radius, float height, Object id) {
    return ShapeFactory.createCylinder(radius, height, id, Primitive.GENERATE_NORMALS, null);
  }

  public static Shape3D createCylinder(float radius, float height, Object id,
                                       int primFlags, Appearance appearance) {
    Cylinder cyl = new Cylinder(radius, height, primFlags, null);
    // make shape because primitives are pia to deal with
    Shape3D shape = new Shape3D();
    // cylinder has a top, bottom, and body shape
    for (int i = 0; i < 3; i++) {
      // make geometry pickable
      Geometry geometry = cyl.getShape(i).getGeometry();
      shape.addGeometry(geometry);
      if (!(geometry.isLive() || geometry.isCompiled()))
        PickTool.setCapabilities(cyl.getShape(i), PickTool.INTERSECT_FULL);

    }
    if (appearance != null)
      shape.setAppearance(appearance);

    shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    shape.setUserData(id);
    return shape;
  }

  public static Shape3D createBox(float xdim, float ydim, float zdim, Object id, int primFlags, Appearance appearance) {
    Box box = new Box(xdim, ydim, zdim, primFlags, null);
    Shape3D shape = new Shape3D();
    for (int i = 0; i < 6; i++) {
      Geometry geom = box.getShape(i).getGeometry();
      shape.addGeometry(geom);
      if (!(geom.isLive() || geom.isCompiled())) {
        PickTool.setCapabilities(shape, PickTool.INTERSECT_FULL);
      }
    }
    if (appearance != null)
      shape.setAppearance(appearance);

    shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    shape.setUserData(id);
    return shape;
  }

  public static Shape3D createCone(float radius, float height, Object id) {
    return ShapeFactory.createCone(radius, height, id, Primitive.GENERATE_NORMALS, null);
  }

  public static Shape3D createCone(float radius, float height, Object id, int primFlags, Appearance appearance) {
    Cone cone = new Cone(radius, height, primFlags, null);
    Shape3D shape = new Shape3D();

    for (int i = 0; i < 2; i++) {
      // make geometry pickable
      Geometry geometry = cone.getShape(i).getGeometry();
      shape.addGeometry(geometry);
      if (!(geometry.isLive() || geometry.isCompiled()))
        PickTool.setCapabilities(cone.getShape(i), PickTool.INTERSECT_FULL);
    }
    if (appearance != null)
      shape.setAppearance(appearance);

    shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    shape.setUserData(id);
    return shape;
  }

  public static Shape3D createWireframeSquare(float unitSize, float xdim, float zdim, Color color) {
    LineArray lineArray = new LineArray(8, GeometryArray.COORDINATES | GeometryArray.COLOR_4);
    int i = 0;

    float xExtent = xdim * unitSize;
    float zExtent = zdim * unitSize;

    lineArray.setCoordinate(i++, new Point3f(0, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(xExtent, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(0, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(0, 0, zExtent));

    lineArray.setCoordinate(i++, new Point3f(0, 0, zExtent));
    lineArray.setCoordinate(i++, new Point3f(xExtent, 0, zExtent));
    lineArray.setCoordinate(i++, new Point3f(xExtent, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(xExtent, 0, zExtent));


    Color4f color4f = new Color4f(color.getRGBComponents(null));

    for (int j = 0; j < i; j++) {
      lineArray.setColor(j, color4f);
    }

    
    Shape3D shape3d = new Shape3D(lineArray);
    
    Appearance appearance = new Appearance();
    shape3d.setAppearanceOverrideEnable(true);
    appearance.setTransparencyAttributes(
    		new TransparencyAttributes(TransparencyAttributes.FASTEST, 1.0f));
    shape3d.setAppearance(appearance);
    
    return shape3d;
  }

  public static Shape3D createWireframeBox(float xdim, float ydim, float zdim, Color color, Object id) {
    LineArray lineArray = new LineArray(24, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
    int i = 0;
    // these are the axes
    lineArray.setCoordinate(i++, new Point3f(0, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(xdim, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(0, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(0, ydim, 0));
    lineArray.setCoordinate(i++, new Point3f(0, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(0, 0, zdim));

    lineArray.setCoordinate(i++, new Point3f(xdim, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(xdim, ydim, 0));
    lineArray.setCoordinate(i++, new Point3f(xdim, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(xdim, 0, zdim));

    lineArray.setCoordinate(i++, new Point3f(xdim, ydim, 0));
    lineArray.setCoordinate(i++, new Point3f(0, ydim, 0));
    lineArray.setCoordinate(i++, new Point3f(xdim, ydim, 0));
    lineArray.setCoordinate(i++, new Point3f(xdim, ydim, zdim));

    lineArray.setCoordinate(i++, new Point3f(0, ydim, 0));
    lineArray.setCoordinate(i++, new Point3f(0, ydim, zdim));

    lineArray.setCoordinate(i++, new Point3f(0, 0, zdim));
    lineArray.setCoordinate(i++, new Point3f(xdim, 0, zdim));
    lineArray.setCoordinate(i++, new Point3f(0, 0, zdim));
    lineArray.setCoordinate(i++, new Point3f(0, ydim, zdim));

    lineArray.setCoordinate(i++, new Point3f(xdim, 0, zdim));
    lineArray.setCoordinate(i++, new Point3f(xdim, ydim, zdim));

    lineArray.setCoordinate(i++, new Point3f(xdim, ydim, zdim));
    lineArray.setCoordinate(i++, new Point3f(0, ydim, zdim));

    Color3f colors = new Color3f(color.getRGBColorComponents(null));

    for (int j = 0; j < i; j++) {
      lineArray.setColor(j, colors);
    }

    Shape3D shape = new Shape3D(lineArray);

    shape.setUserData(id);

    return shape;
  }

  public static Shape3D createGrid(float cellSize, Color color, int... dimensions) {
    if (dimensions.length == 2) {
      int xDim = dimensions[0];
      int yDim = dimensions[1];
      LineArray lineArray = new LineArray(xDim * 2 + yDim * 2 + 4, GeometryArray.COORDINATES
              | GeometryArray.COLOR_3);

      // vert lines
      float z = cellSize * yDim;
      int index = 0;
      // todo update to indexed line array so we don't need a color for each vertex.
      Color3f color3f = new Color3f(color.getRGBColorComponents(null));
      for (int i = 0; i <= xDim; i++) {
        float x = i * cellSize;
        lineArray.setCoordinate(index, new Point3f(x, 0, 0));
        lineArray.setColor(index, color3f);
        index++;
        lineArray.setCoordinate(index, new Point3f(x, 0, z));
        lineArray.setColor(index, color3f);
        index++;
      }

      // horiz lines
      float x = cellSize * xDim;
      for (int i = 0; i <= yDim; i++) {
        z = i * cellSize;
        lineArray.setCoordinate(index, new Point3f(0, 0, z));
        lineArray.setColor(index, color3f);
        index++;
        lineArray.setCoordinate(index, new Point3f(x, 0, z));
        lineArray.setColor(index, color3f);
        index++;
      }

      return new Shape3D(lineArray);
    } else {
      int xDim = dimensions[0];
      int yDim = dimensions[1];
      int zDim = dimensions[2];
      int verts = ((xDim + 1) * 2 + (zDim + 1)) * 2 * (yDim + 1) +
              (xDim + 1) * (zDim + 1) * 2;
      LineArray lineArray = new LineArray(verts, GeometryArray.COORDINATES
              | GeometryArray.COLOR_3);

      Color3f color3f = new Color3f(color.getRGBColorComponents(null));
      float zMax = cellSize * zDim;
      float xMax = cellSize * xDim;
      int index = 0;

      // creates a stack of 2D grids
      for (int i = 0; i <= yDim; i++) {
        float y = i * cellSize;
        // do the z (front-to-back) lines
        for (int j = 0; j <= xDim; j++) {
          float x = j * cellSize;
          lineArray.setCoordinate(index, new Point3f(x, y, 0));
          lineArray.setColor(index, color3f);
          index++;
          lineArray.setCoordinate(index, new Point3f(x, y, zMax));
          lineArray.setColor(index, color3f);
          index++;
        }

        // do the x lines (side-to-side)
        for (int j = 0; j <= zDim; j++) {
          float z = j * cellSize;
          lineArray.setCoordinate(index, new Point3f(0, y, z));
          lineArray.setColor(index, color3f);
          index++;
          lineArray.setCoordinate(index, new Point3f(xMax, y, z));
          lineArray.setColor(index, color3f);
          index++;
        }
      }

      // need to run vertical lines through the stack
      float yMax = cellSize * yDim;
      for (int i = 0; i <= xDim; i++) {
        float x = i * cellSize;
        for (int j = 0; j <= zDim; j++) {
          float z = j * cellSize;
          lineArray.setCoordinate(index, new Point3f(x, 0, z));
          lineArray.setColor(index, color3f);
          index++;
          lineArray.setCoordinate(index, new Point3f(x, yMax, z));
          lineArray.setColor(index, color3f);
          index++;
        }
      }

      return new Shape3D(lineArray);
    }


  }

  public static Shape3D createAxes(float width, float height, float depth, float maxAxisLength,
                                   Color color, Object id) {
    LineArray lineArray = new LineArray(6, GeometryArray.COORDINATES | GeometryArray.COLOR_3);

    width = (width < maxAxisLength) ? width : maxAxisLength;
    height = (height < maxAxisLength) ? height : maxAxisLength;
    depth = (depth < maxAxisLength) ? depth : maxAxisLength;

    int i = 0;
    // x axis
    lineArray.setCoordinate(i++, new Point3f(0, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(width, 0, 0));

    // y axis
    lineArray.setCoordinate(i++, new Point3f(0, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(0, height, 0));

    // z axis
    lineArray.setCoordinate(i++, new Point3f(0, 0, 0));
    lineArray.setCoordinate(i++, new Point3f(0, 0, depth));

    Color3f colors = new Color3f(color.getRGBColorComponents(null));

    for (int j = 0; j < i; j++) {
      lineArray.setColor(j, colors);
    }

    Shape3D shape = new Shape3D(lineArray);

    shape.setUserData(id);

    return shape;
  }
}
