package repast.simphony.engine.watcher;

import repast.simphony.engine.schedule.CallBackAction;
import repast.simphony.util.ClassUtilities;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ArgMatcher {

  protected int watcheeIndex = -1;
  protected int dataIndex = -1;
  protected int fieldIndex = -1;

  protected WatchData watchData;

  public ArgMatcher(WatchData watch) {
    this.watchData = watch;
  }

  static abstract class AbstractTrigger implements NotifierTrigger {
    protected CallBackAction action;
  }

  static abstract class AbstractSetDataTrigger extends AbstractTrigger {

    WatchData data;
    int watcheeIndex, dataIndex, fieldIndex;

    protected AbstractSetDataTrigger(WatchData watch, int dataIndex, int fieldIndex, int watcheeIndex) {
      data = watch;
      this.dataIndex = dataIndex;
      this.fieldIndex = fieldIndex;
      this.watcheeIndex = watcheeIndex;
    }
  }

  static class SetWatcheeTrigger extends AbstractTrigger {
    public void execute(Object watcher, Object watchee, Object value) {
      action.setTarget(watcher);
      action.setArgs(0, watchee);
      action.execute();
    }
  }

  static class SetDataTrigger extends AbstractTrigger {

    private WatchData data;

    public SetDataTrigger(WatchData watch) {
      this.data = watch;
    }

    public void execute(Object watcher, Object watchee, Object value) {
      action.setTarget(watcher);
      action.setArgs(0, data);
      action.execute();
    }
  }

  static class SetWatcheeDataTrigger extends AbstractSetDataTrigger {

    public SetWatcheeDataTrigger(WatchData watch, int dataIndex, int fieldIndex, int watcheeIndex) {
      super(watch, dataIndex, fieldIndex, watcheeIndex);
    }

    public void execute(Object watcher, Object watchee, Object value) {
      action.setTarget(watcher);
      action.setArgs(watcheeIndex, watchee);
      action.setArgs(dataIndex, data);
      action.execute();
    }
  }

  static class SetWatcheeValueTrigger extends AbstractSetDataTrigger {

    public SetWatcheeValueTrigger(WatchData watch, int dataIndex, int fieldIndex, int watcheeIndex) {
      super(watch, dataIndex, fieldIndex, watcheeIndex);
    }

    public void execute(Object watcher, Object watchee, Object value) {
      action.setTarget(watcher);
      action.setArgs(watcheeIndex, watchee);
      action.setArgs(fieldIndex, value);
      action.execute();
    }
  }

  static class SetWatcheeDataValueTrigger extends AbstractSetDataTrigger {

    public SetWatcheeDataValueTrigger(WatchData watch, int dataIndex, int fieldIndex, int watcheeIndex) {
      super(watch, dataIndex, fieldIndex, watcheeIndex);
    }

    public void execute(Object watcher, Object watchee, Object value) {
      action.setTarget(watcher);
      action.setArgs(watcheeIndex, watchee);
      action.setArgs(dataIndex, data);
      action.setArgs(fieldIndex, value);
      action.execute();
    }
  }

  static class SetDataValueTrigger extends AbstractSetDataTrigger {

    public SetDataValueTrigger(WatchData watch, int dataIndex, int fieldIndex, int watcheeIndex) {
      super(watch, dataIndex, fieldIndex, watcheeIndex);
    }

    public void execute(Object watcher, Object watchee, Object value) {
      action.setTarget(watcher);
      action.setArgs(fieldIndex, value);
      action.setArgs(dataIndex, data);
      action.execute();
    }
  }

  static class SetWatcheeValTrigger extends AbstractSetDataTrigger {

    public SetWatcheeValTrigger(WatchData watch, int dataIndex, int fieldIndex, int watcheeIndex) {
      super(watch, dataIndex, fieldIndex, watcheeIndex);
    }

    public void execute(Object watcher, Object watchee, Object value) {
      action.setTarget(watcher);
      action.setArgs(watcheeIndex, watchee);
      action.setArgs(fieldIndex, value);
      action.execute();
    }
  }

  static class SetValueTrigger extends AbstractTrigger {
    public void execute(Object watcher, Object watchee, Object value) {
      action.setTarget(watcher);
      action.setArgs(0, value);
      action.execute();
    }
  }

  static class PlainTrigger extends AbstractTrigger {
    public void execute(Object watcher, Object watchee, Object value) {
      action.setTarget(watcher);
      action.execute();
    }
  }

  public NotifierTrigger createTrigger(CallBackAction action) {
    AbstractTrigger trigger = null;
    if (watcheeIndex == -1 && dataIndex == -1 && fieldIndex == -1) {
      trigger = new PlainTrigger();
    } else if (watcheeIndex != -1) {
      if (dataIndex == -1 && fieldIndex == -1) {
        // do watchee only
        trigger = new SetWatcheeTrigger();
      } else if (dataIndex != -1 && fieldIndex == -1) {
        // do watchee and data
        trigger = new SetWatcheeDataTrigger(watchData, dataIndex, fieldIndex, watcheeIndex);
      } else if (dataIndex == -1 && fieldIndex != -1) {
        // do watchee and field
        trigger = new SetWatcheeValueTrigger(watchData, dataIndex, fieldIndex, watcheeIndex);
      } else if (dataIndex != -1 && fieldIndex != -1) {
        // do watchee, data and field
        trigger = new SetWatcheeDataValueTrigger(watchData, dataIndex, fieldIndex, watcheeIndex);
      }
    } else if (dataIndex != -1) {
      // we know the watcheeIndex == -1
      if (fieldIndex == -1) {
        trigger = new SetDataTrigger(watchData);
      } else {
        trigger = new SetDataValueTrigger(watchData, dataIndex, fieldIndex, watcheeIndex);
      }
    } else {
      trigger = new SetValueTrigger();
    }
    trigger.action = action;
    return trigger;
  }

  public boolean match(Method method, String watcheeName, String fieldNames) throws ClassNotFoundException, NoSuchFieldException {
    Class watchee = Class.forName(watcheeName);
    Class dataClazz = WatchData.class;
    Set<Class> types = new HashSet<Class>();
    types.add(watchee);
    types.add(dataClazz);
    Class[] paramTypes = method.getParameterTypes();
    if (fieldNames.contains(",")) {
      // mult field names so fields name cannot be passed
      // so we check on watchee and watchData
      for (int i = 0; i < paramTypes.length; i++) {
        Class paramType = paramTypes[i];
        boolean res = types.remove(paramType);
        if (!res) return false;
        if (paramType.equals(watchee)) watcheeIndex = i;
        else if (paramType.equals(dataClazz)) dataIndex = i;
      }
    } else {
      Class fieldType = ClassUtilities.deepFindField(watchee, fieldNames).getType();
      types.add(fieldType);
      for (int i = 0; i < paramTypes.length; i++) {
        Class paramType = paramTypes[i];
        boolean res = types.remove(paramType);
        if (!res) return false;
        if (paramType.equals(watchee)) watcheeIndex = i;
        else if (paramType.equals(dataClazz)) dataIndex = i;
        else if (paramType.equals(fieldType)) fieldIndex = i;
      }
    }

    return true;
  }

  public int getArgCount() {
    return (watcheeIndex != -1 ? 1 : 0) + (dataIndex != -1 ? 1 : 0) + (fieldIndex != -1 ? 1 : 0);
  }
}
