package repast.simphony.visualization.visualization3D;

import com.sun.j3d.utils.picking.PickTool;
import repast.simphony.space.Dimensions;
import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualization.visualization3D.style.DefaultValueLayerStyle3D;
import repast.simphony.visualization.visualization3D.style.ValueLayerStyle3D;

import javax.media.j3d.*;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class DefaultValueDisplayLayer3D implements ValueDisplayLayer3D {

  private ValueLayerStyle3D style;
  private int xDim, zDim;
  private int xOffset, zOffset;
  private BranchGroup branchGroup;
  private Display3D display;
  private DataGeometry dataGeom;
  private Map<String, ValueLayer> layerMap = new HashMap<String, ValueLayer>();

  public DefaultValueDisplayLayer3D(ValueLayerStyle3D style, Group topGroup, Display3D display) {
    this.display = display;
    if (style == null) style = new DefaultValueLayerStyle3D();
    this.style = style;
    branchGroup = new BranchGroup();
    branchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
    branchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
    topGroup.addChild(branchGroup);
  }

  public void registerGrid(Group parent) {
    //int[] dimensionVals = {xDim, zDim};
    float unitSize = style.getCellSize();
    Shape3D grid = ShapeFactory.createWireframeSquare(unitSize, xDim, zDim, new Color(0, 0, 0, 0));
    //Shape3D grid = ShapeFactory.createGrid(unitSize, zDim, new Color(0, 0, 0, 0));
    // translate the grid 1/2 cell size -x, -y, +z, so that objects
    // are in center of cells
    Transform3D gridTrans = new Transform3D();
    float offset = -unitSize / 2;
    gridTrans.setTranslation(new Vector3f(offset, offset, -offset));
    TransformGroup grp = new TransformGroup(gridTrans);
    grp.addChild(grid);
    parent.addChild(grp);
    //parent.addChild(grid);
    grid.setPickable(false);
  }

  public void addDataLayer(ValueLayer layer) {
    int[] dims = layer.getDimensions().toIntArray(null);
    int[] origin = layer.getDimensions().originToIntArray(null);
    if (dims.length > 3 || (dims.length == 3 && dims[2] != 0)) {
      // todo better error reporting
      throw new IllegalArgumentException("Data Layers with more than 2 dimensions are unsupported");
    }
    layerMap.put(layer.getName(), layer);
    xDim = Math.max(dims[0], xDim);
    zDim = Math.max(dims[1], zDim);
    xOffset = origin[0];
    zOffset = origin[1];
  }

  public void setStyle(ValueLayerStyle3D style) {
    this.style = style;
  }

  public void init(Behavior masterBehavior) {
    // create the shape
    dataGeom = new MeshGridGeometry(xDim, zDim, xOffset, zOffset);
    Shape3D shape = new Shape3D(dataGeom.getGeometry(style));
    PickTool.setCapabilities(shape, PickTool.INTERSECT_FULL);
    shape.setAppearance(getAppearance());

    // translate to center and scale so have appropriate cell width and height
    Transform3D trans = new Transform3D();
    trans.setTranslation(new Vector3f(-(xDim / 2f), 0, zDim / 2f));
    Transform3D scale = new Transform3D();
    float size = style.getCellSize();
    scale.setScale(new Vector3d(size, 1, size));
    scale.mul(trans);
    TransformGroup transGroup = new TransformGroup(scale);
    transGroup.addChild(shape);
    branchGroup.addChild(transGroup);
    branchGroup.compile();

    // set up the update behavior for updating the display based on the style
    DataGeometryUpdater updater = new DataGeometryUpdater(dataGeom);
    DataUpdateBehavior behavior = new DataUpdateBehavior(dataGeom.getGeometry(style), updater, masterBehavior);
    behavior.setSchedulingBounds(new BoundingSphere(new Point3d(0, 0, 0), Float.POSITIVE_INFINITY));
    display.getSceneRoot().addChild(behavior);
  }

  private Appearance getAppearance() {
    // todo should use to be stuff from the style for this
    Appearance appearance = new Appearance();
    PolygonAttributes polyAttrib = new PolygonAttributes();
    polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
    polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_FILL);
    // Setting back face normal flip to true allows backfacing
    // polygons to be lit (normal is facing wrong way) but only
    // if the normal is flipped.
    polyAttrib.setBackFaceNormalFlip(true);
    appearance.setPolygonAttributes(polyAttrib);
    Material material = new Material();
    material.setAmbientColor(.15f, .15f, .15f);
    material.setSpecularColor(0f, 0f, 0f);
    appearance.setMaterial(material);

    return appearance;
  }

  public void update() {
    dataGeom.update(style);
  }

  public void applyUpdates() {
    // not needed because because the behavior that controls will be triggered by the "main" behavior
  }

  public Object findObjForShape(Shape3D shape, Point3d intersectPoint) {
    if (shape.getGeometry().equals(dataGeom.getGeometry(style))) {
      int x = (int) intersectPoint.x;
      int y = (int) -intersectPoint.z;
      for (Map.Entry<String, ValueLayer> entry : layerMap.entrySet()) {
        ValueLayer layer = entry.getValue();
        Dimensions dims = layer.getDimensions();
        if (x < dims.getWidth() && y < dims.getHeight()) {
          // for now just return a String
          return entry.getKey() + ": " + layer.get(x, y);
        }
      }
    }

    return null;
  }
}
