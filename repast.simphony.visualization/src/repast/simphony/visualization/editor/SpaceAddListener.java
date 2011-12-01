package repast.simphony.visualization.editor;

import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.visualization.editor.space.Projected3DSpace;

/**
 * Listens for add events and prepares
 * the space so that the agent will be added
 * at the appropriate location.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class SpaceAddListener implements AddListener {

  class LocationAdder<Object> implements ContinuousAdder<Object> {

    private double[] location;

    public void initLocation(double[] location) {
      this.location = location;
    }

    public void add(ContinuousSpace<Object> space, Object object) {
      space.moveTo(object, location);
    }
  }

  private ContinuousSpace space;
  private ContinuousAdder adder;
  private LocationAdder locAdder;
  private int constantIndex = -1, dimIndex1, dimIndex2;
  private double[] location;
  private SpaceAddHandler handler;

  public SpaceAddListener(ContinuousSpace space, SpaceAddHandler handler) {
    this.space = space;
    this.adder = space.getAdder();
    if (space instanceof Projected3DSpace) {
      location = new double[3];
      Projected3DSpace pSpace = (Projected3DSpace) space;
      constantIndex = pSpace.getConstantIndex();
      dimIndex1 = pSpace.getIndex1();
      dimIndex2 = pSpace.getIndex2();
    } else {
      location = new double[space.getDimensions().size()];
    }
    locAdder = new LocationAdder();
    this.handler = handler;
  }

  /**
   * Called immediately prior to an agent being added.
   *
   * @param obj
   *@param point the location at which the agent should
   *              be added. @return true if an agent can be added to the specified location,
   *         otherwise false.
   */
  public boolean preAdd(Object obj, double... point) {
    if (constantIndex == -1) {
      for (int i = 0; i < this.location.length; i++) {
        this.location[i] = point[i];
      }
    } else {
      this.location[constantIndex] = 0;
      this.location[dimIndex1] = point[0];
      this.location[dimIndex2] = point[1];
    }

    locAdder.initLocation(location);
    space.setAdder(locAdder);
    return true;

  }

  /**
   * Called immediately after the agent has been added.
   */
  public void postAdd() {
    space.setAdder(adder);
  }

  /**
   * Gets the PInputEvent handler that will handle
   * the gui part of the adding.
   *
   * @return the PInputEvent handler that will handle
   *         the gui part of the adding.
   */
  public PEditorEventListener getAddHandler() {
    return handler;
  }
}