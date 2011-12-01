package repast.simphony.engine.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Simple container used to gather the objects in one place for
 * serialization. Intended to mimic a sim model in that it contains
 * a schedule and other bits.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class SerializedContainer implements Serializable {

  public List list = new ArrayList();
  public ISchedule schedule;
}
