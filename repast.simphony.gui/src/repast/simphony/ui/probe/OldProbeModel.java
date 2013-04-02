package repast.simphony.ui.probe;

import com.jgoodies.binding.beans.Model;

/**
 * Abstract base class for probeable beans.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class OldProbeModel extends Model {

  /**
   * Fires a property change event for all properties so that the viz
   * representation will update to show the new values.
   */
  public void update() {
    fireMultiplePropertiesChanged();
  }
}
