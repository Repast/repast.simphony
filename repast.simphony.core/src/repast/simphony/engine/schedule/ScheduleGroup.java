package repast.simphony.engine.schedule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import repast.simphony.random.RandomHelper;
import cern.jet.random.Uniform;

/**
 * A collection of ISchedulableActions. This class assumes that the actions are
 * scheduled to execute at the same tick. A ScheduleGroup will order the
 * execution of its actions according to their priority and then execute them in
 * the proper order. This would be done by adding the appropriate actions to
 * this ScheduleGroup, calling sort() and then calling execute().
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 * @see repast.simphony.engine.schedule.ISchedulableAction
 */
// todo fix this -- its too easy to screw up the actions if sort is called again
// without
// a clear in between!!!
public class ScheduleGroup implements IAction {

  static final long serialVersionUID = 3192709155891784441L;

  /**
   * The number of actions contained by this ScheduleGroup
   */
  protected int count = 0;

  /**
   * List containing actions whose priority is a double _inside_ the range
   * NEG_INFINITY, POS_INFINITY.
   */
  protected ActionList actions = new ActionList();
  /**
   * List of actions whose priority is random.
   */
  protected ActionList randomActions = new ActionList();
  /**
   * List of actions that should be executed first.
   */
  protected ActionList firstActions = new ActionList();
  
  /**
   * List of actions that are executed as last actions but immediately
   * prior to those.
   */
  protected ActionList firstOfLastActions = new ActionList();
  
  /**
   * List of actions that should be executed last.
   */
  protected ActionList lastActions = new ActionList();

  /**
   * List of executed actions. These actions will be rescheduled.
   */
  protected List<ISchedulableAction> executedActions = new ArrayList<ISchedulableAction>();

  protected boolean finishing = false;

  // whether or not the group is currently executing. We need to know this
  // because execution may add actions to execute.
  private boolean executing = false;

  // whether or not to resort during execution -- will be true if an action is
  // added
  // while this group is executing.
  private boolean actionsAddedDuringExec = false;
  private List<ISchedulableAction> actionsAddedWhileExecuting = new ArrayList<ISchedulableAction>();

  /**
   * Compares ISchedulableActions according to their priority. Lower priority
   * later in order.
   */
  static class PriorityComparator implements Comparator<ISchedulableAction> {
    public int compare(ISchedulableAction o1, ISchedulableAction o2) {
      double index1 = o1.getPriority();
      double index2 = o2.getPriority();
      return index1 < index2 ? 1 : index1 == index2 ? 0 : -1;
    }
  }

  /**
   * Compares ISchedulableActions according to their orderIndex.
   */
  static class OrderComparator implements Comparator<ISchedulableAction> {
    public int compare(ISchedulableAction o1, ISchedulableAction o2) {
      double index1 = o1.getOrderIndex();
      double index2 = o2.getOrderIndex();
      return index1 < index2 ? -1 : index1 == index2 ? 0 : 1;
    }
  }

  private Comparator<ISchedulableAction> iComp = new PriorityComparator();
  private Comparator<ISchedulableAction> oComp = new OrderComparator();

  /**
   * Add an IScheduleAction to this ScheduleGroup.
   * 
   * @param action
   *          the action to add
   */
  public void addAction(ISchedulableAction action) {
    if (finishing) {
      return;
    }
    if (executing) {
      actionsAddedDuringExec = true;
      actionsAddedWhileExecuting.add(action);
    } else {
      doAddAction(action);
    }
  }

  /**
   * Removes the specified action from this ScheduleGroup.
   * 
   * @param action
   *          the action to remove
   * @return whether or not an action was removed from this group.
   */
  public boolean remove(ISchedulableAction action) {
    boolean retVal = false;
    if (randomActions.remove(action)) {
      retVal = true;
      count--;
    }
    if (firstActions.remove(action)) {
      count--;
      retVal = true;
    }
    if (lastActions.remove(action)) {
      count--;
      retVal = true;
    }
    
    if (firstOfLastActions.remove(action)) {
      count--;
      retVal = true;
    }
    
    if (actions.remove(action)) {
      count--;
      retVal = true;
    }

    return retVal;
  }

  private void doAddAction(ISchedulableAction action) {
    PriorityType pType = action.getPriorityType();
    
    if (pType == PriorityType.RANDOM)
      randomActions.add(action);
    else if (pType == PriorityType.LAST)
      lastActions.add(action);
    else if (pType == PriorityType.FIRST)
      firstActions.add(action);
    else if (pType == PriorityType.FIRST_OF_LAST) 
      firstOfLastActions.add(action);
    else
      actions.add(action);
    count++;
  }

  /**
   * Clears this ScheduleGroup of any ISchedulableActions it may contain
   */
  public void clear() {
    actions.clear();
    randomActions.clear();
    firstActions.clear();
    lastActions.clear();
    firstOfLastActions.clear();
    count = 0;
  }

  /**
   * Returns the number of ISchedulableActions in this ScheduleGroup.
   * 
   * @return the number of ISchedulableActions in this ScheduleGroup.
   */
  public int size() {
    return count;
  }

  public boolean hasMoreToExecute() {
    return executedActions.size() != count;
  }

  private boolean executeList(ActionList list) {
    int i = 0;
    boolean interrupted = false;
    for (Iterator<ISchedulableAction> iter = list.iterator(); iter.hasNext();) {
      if (actionsAddedDuringExec) {
        break;
      } else {
        ISchedulableAction action = iter.next();
        executedActions.add(action);
        action.execute();
        i++;
      }
    }
    list.delete(i);
    
    if (actionsAddedDuringExec) {
      interrupted = true;
      addExecAddedActions();
      actionsAddedDuringExec = false;
    }
    
    return interrupted;
  }

  /**
   * Executes all the ISchedulableAction in this ScheduleGroup.
   */
  public void execute() {
    executing = true;

    // execute the first actions
    boolean interrupted = executeList(firstActions);
    if (interrupted)
      return;

    interrupted = executeList(actions);
    if (interrupted)
      return;
    
    interrupted = executeList(firstOfLastActions);
    if (interrupted) return;

    interrupted = executeList(lastActions);
    if (interrupted)
      return;

    // we need to check again in case we don't iterate through
    // the lists once the new action has been added.
    if (actionsAddedDuringExec) {
      addExecAddedActions();
      actionsAddedDuringExec = false;
      return;
    }

    executing = false;
  }

  private void addExecAddedActions() {
    for (ISchedulableAction action : actionsAddedWhileExecuting) {
      doAddAction(action);
    }
    sort();
    actionsAddedWhileExecuting.clear();
  }

  /**
   * Reschedules all the ISchedulableActions contained by this ScheduleGroup.
   * 
   * @see repast.simphony.engine.schedule.ISchedulableAction#reschedule(ActionQueue)
   */
  public void reschedule(ActionQueue aQueue) {
    if (!finishing) {
      for (ISchedulableAction action : executedActions) {
        action.reschedule(aQueue);
      }
    }
    executedActions.clear();
  }

  /**
   * Sorts the DefaultActions in this ScheduleGroup according to their priority.
   * Those actions with a numeric priority are ordered w/r to each other. Those
   * actions with random priority are randomly ordered w/r to the numeric
   * actions. After sort(), a call to execute will execute the actions in the
   * appropriate order.
   */
  public void sort() {
    // sort the random actions according to their order indexes
    // we do this so they have a predicatable order before we randomize them.
    // Consequently, same seed will result in the same random order
    randomActions.sort(oComp, false);
    // we sort these to get a predictable order for actions scheduled with the
    // same priority
    firstActions.sort(oComp, false);
    firstOfLastActions.sort(oComp, false);
    lastActions.sort(oComp, false);
    actions.sort(oComp, false);

    if (actions.size() > 0) {
      // sort the indexed actions
      actions.sort(iComp, true);
      // merge the random actions randomly into actions
      int size = actions.size();
      Uniform defaultUniform = RandomHelper.getUniform();
      for (ISchedulableAction action : randomActions) {
        int index = defaultUniform.nextIntFromTo(0, size);
        if (index == size) {
          actions.addNoSort(action);
        } else {
          actions.addNoSort(index, action);
        }
        size++;
      }

    } else {
      randomActions.shuffle();
      actions.addAllNoSort(randomActions);
    }
    randomActions.clear();
  }

  public void setFinishing(boolean finishing) {
    this.finishing = finishing;
  }

  public boolean isFinishing() {
    return this.finishing;
  }
}
