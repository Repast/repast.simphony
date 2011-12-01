package repast.simphony.engine.watcher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class MyWatcher {

  Generator generator;
  int val = -1;
  double dval = -1;
  long lval = -1;
  float fval = -1;
  short sval = -1;
  byte byval = -1;
  char cval = 'a';
  boolean bval = false;
  String str = null;
  String name = "Fred";
  int plainTriggerVal = -1;
  int genTriggerVal = -1;
  int valTriggerVal = -1;
  int genValTriggerVal = -1;
  boolean triggered = false;
  boolean scheduleTest = false;
  Map<String, Boolean> queryResult = new HashMap<String, Boolean>();
  boolean queryTest = false;

  public void foo() {
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", triggerCondition = "", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void watchTrigger() {
    plainTriggerVal++;
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", triggerCondition = "", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void watchTrigger(int val) {
    this.val = val;
    valTriggerVal = val;
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "bVal, counter", triggerCondition = "", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void watchDoubleField() {
    queryResult.put("double_field", true);
  }

  public String getName() {
    return name;
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", triggerCondition = "", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void watchTrigger(Generator generator) {
    this.generator = generator;
    genTriggerVal++;
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", triggerCondition = "", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void watchTrigger(Generator generator, int val) {
    this.generator = generator;
    this.val = val;
    genValTriggerVal = val;
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", triggerCondition = "$watcher.getName().equals(\"Fred\") && $watchee.getCounter() == 2", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerConditionTest() {
    triggered = true;
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", triggerCondition = "$watcher.getName().equals(\"Fred\")", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 2, scheduleTriggerPriority = 0)
  public void triggerScheduleTest() {
    scheduleTest = true;
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query = "linked", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerLinkedQueryTest() {
    queryResult.put("linked", true);
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query = "linked_to", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerLinkedToQueryTest() {
    queryResult.put("linked_to", true);
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query = "linked_to 'family'", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerLinkedToNamedQueryTest() {
    queryResult.put("linked_to_named", true);
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query = "linked_from", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerLinkedFromQueryTest() {
    queryResult.put("linked_from", true);
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query = "colocated", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerColocatedQueryTest() {
    queryResult.put("colocated", true);
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query = "not colocated", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerNotColocatedQueryTest() {
    queryResult.put("not_colocated", true);
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query = "within 3", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerWithinQueryTest() {
    queryResult.put("within", true);
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query = "within 3 'family'", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerWithinNamedQueryTest() {
    queryResult.put("within_named", true);
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query = "within_vn 2", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerWithinVNQueryTest() {
    queryResult.put("within_vn", true);
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query = "within_moore 2 ", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerWithinMooreQueryTest() {
    queryResult.put("within_moore", true);
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query = "within_vn 2 'family'", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerWithinNamedVNQueryTest() {
    queryResult.put("within_vn_named", true);
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query = "within_moore 2 'family'", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 0, scheduleTriggerPriority = 0)
  public void triggerWithinNamedMooreQueryTest() {
    queryResult.put("within_moore_named", true);
  }

  public void setQueryTest(boolean val) {
    queryTest = val;
  }

  public void watchTrigger(Generator generator, long val) {
    this.generator = generator;
    this.lval = val;
  }

  public void watchTrigger(Generator generator, float val) {
    this.generator = generator;
    this.fval = val;
  }

  public void watchTrigger(Generator generator, short val) {
    this.generator = generator;
    this.sval = val;
  }

  public void watchTrigger(Generator generator, double val) {
    this.generator = generator;
    this.dval = val;
  }

  public void watchTrigger(Generator generator, byte val) {
    this.generator = generator;
    this.byval = val;
  }

  public void watchTrigger(Generator generator, char val) {
    this.generator = generator;
    this.cval = val;
  }

  public void watchTrigger(Generator generator, boolean val) {
    this.generator = generator;
    this.bval = val;
  }

  public boolean queryResult(String name) {
    Boolean res = queryResult.get(name);
    if (res == null)
      return false;
    return res;
  }
}
