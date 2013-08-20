/**
 * 
 */
package repast.simphony.visualization.editedStyle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import repast.simphony.gis.styleEditor.SimpleMarkFactory;
import repast.simphony.visualizationOGL2D.ImageSpatialSource;
import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.NamedShapeCreator;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;
import simphony.util.messages.MessageCenter;

/**
 * Loads style info from a serialized EditedStyleData class.
 * 
 * @author Nick Collier
 */
public class EditedStyleOGL2D implements StyleOGL2D<Object> {

  private static int counter = 0;
  private static final MessageCenter msg = MessageCenter.getMessageCenter(EditedStyleOGL2D.class);
  private static SimpleMarkFactory markFac = new SimpleMarkFactory();
  
  private EditedStyleData<Object> style;
  private String id = String.valueOf(++counter);
  private ShapeFactory2D factory;
  private float xOffset, yOffset;
  private Font font;
  private Position position;
  private Color labelColor;
  private float origSize;

  public EditedStyleOGL2D(String styleFile) {
    style = EditedStyleUtils.getStyle(styleFile);
    if (style == null)
      style = new DefaultEditedStyleData2D<Object>();

    font = new Font(style.getLabelFontFamily(), style.getLabelFontType(), style.getLabelFontSize());

    float colorRGB[] = style.getLabelColor();
    labelColor = new Color(colorRGB[0], colorRGB[1], colorRGB[2]);

    String pos = style.getLabelPosition();
    if (pos.equals("right")) {
      position = Position.EAST;
      xOffset = style.getLabelOffset();
      yOffset = 0;
    } else if (pos.equals("left")) {
      position = Position.WEST;
      xOffset = style.getLabelOffset();
      yOffset = 0;
    } else if (pos.equals("top")) {
      position = Position.NORTH;
      xOffset = 0;
      yOffset = style.getLabelOffset();
    } else {
      position = Position.SOUTH;
      xOffset = 0;
      yOffset = style.getLabelOffset();
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#init(saf.v3d.ShapeFactory2D)
   */
  @Override
  public void init(ShapeFactory2D factory) {
    origSize = style.getSize();
    Map<String, String> props = new HashMap<String, String>();
    this.factory = factory;
    if (style.getIconFile2D() != null) {
      try {
        ImageSpatialSource source = new ImageSpatialSource(id, style.getIconFile2D());
        // source.registerSource(factory, props);
        if (origSize != -1) {
          // -1 means just use the icons image size as is
          props.put(ImageSpatialSource.KEY_WIDTH, String.valueOf(origSize));
          props.put(ImageSpatialSource.KEY_HEIGHT, String.valueOf(origSize));
        }
        source.registerSource(factory, props);
      } catch (IOException ex) {
        msg.error("Error while initializing edited style", ex);
      }
    } else {
      // register the shape, scaled to the appropriate size
      String wkt = style.getShapeWkt();
      // these shapes are not closed, so we need to close them
      // to create filled polygons.
      // TODO using SimpleMarkFactory here is really an unnecceary dependency on GIS
      Shape shape = markFac.getMark(wkt);
      Rectangle2D bounds = shape.getBounds2D();
      // -1 means deafault size of 15.
      float size = origSize == -1 ? 15f : origSize;
      float scaleX = size / (float) bounds.getWidth();
      float scaleY = size / (float) bounds.getWidth();
      shape = AffineTransform.getScaleInstance(scaleX, scaleY).createTransformedShape(shape);
      GeneralPath path = new GeneralPath(shape);
      path.closePath();
      NamedShapeCreator creator = factory.createNamedShape(id);
      creator.addShape(path.createTransformedShape(new AffineTransform()), Color.BLACK, true);
      creator.registerShape();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getVSpatial(java.lang.Object,
   * saf.v3d.scene.VSpatial)
   */
  @Override
  public VSpatial getVSpatial(Object object, VSpatial spatial) {
    if (spatial == null) {
      return factory.getNamedSpatial(id);
    } else {
      return spatial;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getColor(java.lang.Object)
   */
  @Override
  public Color getColor(Object object) {
    return EditedStyleUtils.getColor(style, object);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getBorderSize(java.lang.Object
   * )
   */
  @Override
  public int getBorderSize(Object object) {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getBorderColor(java.lang.
   * Object)
   */
  @Override
  public Color getBorderColor(Object object) {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getRotation(java.lang.Object)
   */
  @Override
  public float getRotation(Object object) {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getScale(java.lang.Object)
   */
  @Override
  public float getScale(Object object) {
    float size = EditedStyleUtils.getSize(style, object);
    if (size == -1) return 1f;
    return size / origSize;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getLabel(java.lang.Object)
   */
  @Override
  public String getLabel(Object object) {
    return EditedStyleUtils.getLabel(style, object);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getLabelFont(java.lang.Object
   * )
   */
  @Override
  public Font getLabelFont(Object object) {
    return font;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getLabelXOffset(java.lang
   * .Object)
   */
  @Override
  public float getLabelXOffset(Object object) {
    return xOffset;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getLabelYOffset(java.lang
   * .Object)
   */
  @Override
  public float getLabelYOffset(Object object) {
    return yOffset;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getLabelPosition(java.lang
   * .Object)
   */
  @Override
  public Position getLabelPosition(Object object) {
    return position;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getLabelColor(java.lang.Object
   * )
   */
  @Override
  public Color getLabelColor(Object object) {
    return labelColor;
  }
}
