package repast.simphony.relogo.styles;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Map;

import repast.simphony.relogo.image.NLImage;
import repast.simphony.relogo.image.NLImagePrimitive;
import saf.v3d.NamedShapeCreator;
import saf.v3d.ShapeFactory2D;

public class NLImageSpatialSource implements ReLogoSpatialSource {

  private NLImage image;
  private boolean rotate;
  private float offset;
  private String ID;
  private boolean simple;

  public NLImageSpatialSource(NLImage image) {
    this.image = image;
    this.rotate = image.isRotates();
    this.offset = 0f;
    this.ID = image.getName();
    this.simple = true;
  }

  public boolean doRotate() {
    return rotate;
  }

  public String getID() {
    return ID;
  }

  public float getOffset() {
    return offset;
  }

  public boolean isSimple() {
    return simple;
  }

  public void registerSource(ShapeFactory2D shapeFactory, Map<String, String> props)
      throws IOException {
    
    int width = 15, height = 15;
    if (props.containsKey(KEY_WIDTH)) {
      width = Integer.parseInt(props.get(KEY_WIDTH));
    }

    if (props.containsKey(KEY_HEIGHT)) {
      width = Integer.parseInt(props.get(KEY_HEIGHT));
    }

    NamedShapeCreator creator = shapeFactory.createNamedShape(image.getName());
    for (NLImagePrimitive prim : image.getPrimitives()) {
      
      
      java.awt.Shape shape = prim.renderingShape(new Rectangle(0, 0, width, height));
      // System.out.println("shape.getBounds2D() = " +
      // shape.getBounds2D());
      // this is necessary to flip the right way up.
      shape = AffineTransform.getScaleInstance(1, -1).createTransformedShape(shape);
      // System.out.println("shape.getBounds2D() = " +
      // shape.getBounds2D());
      // System.out.println("");
      creator.addShape(shape, prim.getColor(), prim.isChangingColor());
    }
    creator.registerShape();
  }
}
