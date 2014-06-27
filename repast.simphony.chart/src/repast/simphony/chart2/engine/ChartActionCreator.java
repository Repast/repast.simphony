package repast.simphony.chart2.engine;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.plugin.CompositeControllerActionCreator;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:11:55 $
 */
public class ChartActionCreator implements CompositeControllerActionCreator {

  public String getID() {
    return ControllerActionConstants.CHART_ROOT;
  }

  public ControllerAction createControllerAction() {
    return new DefaultControllerAction();
  }

	@Override
	public boolean isMasterOnly() {
		return false;
	}
}
