package repast.simphony.visualization.visualization3D;

import java.util.BitSet;

/**
 * Tracks the whether or not behaviors are finished executing with respect
 * the current rendering iteration.<p>
 *
 * Behaviors register themselves with this class, and receive an id.
 * When a behavior finished executing, it should call behaviorCompleted
 * and passing in that id.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class BehaviorTracker {

  private boolean renderStarted = false;
  private boolean renderFinished = false;
  private int behaviorCount = 0;
	// need to do the tracking via something like a bitset
	// rather than simply decrementing an integer because
	// we may get several completed notifications from the same
	// behavior between resets.
	private BitSet bitSet = new BitSet();

	/**
	 * Registers a behavior for tracking.
	 *
	 * @return an id for that behavior to use to identify itself when
	 * it completes.
	 */
  public int registerBehavior() {
    return behaviorCount++;
  }

  public void deregisterBehavior(int index) {
    bitSet.clear(index);
  }

  public void renderStarted() {
    renderStarted = true;
  }

	/**
	 * Notifies this BehaviorTracker that the behavior
	 * associated with the specified id has completed executing.
	 *
	 * @param id the id of the behavior that has completed.
	 */
  public void behaviorCompleted(int id) {
    bitSet.clear(id);
  }

  public void reset() {
    renderStarted = false;
    renderFinished = false;
    if (behaviorCount > 0) bitSet.set(0, behaviorCount);
  }

  public boolean isRenderFinished() {
    return renderStarted && renderFinished && bitSet.isEmpty();
  }

  public void end() {
    renderFinished = true;
  }
}
