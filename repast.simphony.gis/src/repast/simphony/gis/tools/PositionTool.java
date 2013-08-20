package repast.simphony.gis.tools;

import com.vividsolutions.jts.geom.Coordinate;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import repast.simphony.gis.display.PiccoloMapPanel;
import simphony.util.messages.MessageCenter;

import java.awt.*;

public class PositionTool extends PBasicInputEventHandler implements MapTool {

  MessageCenter center = MessageCenter.getMessageCenter(getClass());

  protected boolean active = false;

  protected LocationSetter setter;

  protected CoordinateReferenceSystem crs;

  protected CoordinateReferenceSystem wgs84;

  protected MathTransform transform;

  public PositionTool(CoordinateReferenceSystem crs, LocationSetter setter) {
    this.setter = setter;
    this.crs = crs;
    try {
      wgs84 = CRS.decode("EPSG:4326");
      transform = CRS.findMathTransform(crs, wgs84, true);
    } catch (NoSuchAuthorityCodeException e) {
      wgs84 = DefaultGeographicCRS.WGS84;
    } catch (FactoryException e) {
      center.warn("Unable to create transform", e);
    }
  }

  public void cleanUp() {
    this.setter = null;
    this.crs = null;
  }

  @Override
  public void mouseEntered(PInputEvent event) {
    active = true;
  }

  @Override
  public void mouseExited(PInputEvent event) {
    active = false;
    setter.unsetLocation();
  }

  @Override
  public void mouseMoved(PInputEvent event) {
    if (active) {
      if (transform == null || crs.equals(wgs84)) {
        setter.setLocation(event.getPosition().getX(), event
                .getPosition().getY());
      } else {
        Coordinate coord = new Coordinate(event.getPosition().getX(),
                event.getPosition().getY());
        try {
          JTS.transform(coord, coord, transform);
          setter.setLocation(coord.x, coord.y);
        } catch (TransformException e) {
          setter.unsetLocation();
        }
      }
    }
  }

  public void activate(PiccoloMapPanel panel) {
  }

  public void deactivate() {
  }

  public Cursor getCursor() {
    return Cursor.getDefaultCursor();
  }

}
