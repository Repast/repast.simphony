package repast.simphony.statecharts.sheets;

import repast.simphony.statecharts.scmodel.TriggerTypes;

public enum TriggerTypeItem {
  
  ALWAYS("Always", TriggerTypes.ALWAYS),
  TIMED("Timed", TriggerTypes.TIMED),
  EXP_RATE("Exponential Decay Rate", TriggerTypes.EXPONENTIAL),
  PROBABILITY("Probability", TriggerTypes.PROBABILITY),
  CONDITION("Condition", TriggerTypes.CONDITION),
  MESSAGE("Message", TriggerTypes.MESSAGE);
  
  
  private TriggerTypes type;
  private String name;
  
  TriggerTypeItem(String name, TriggerTypes type) {
    this.type = type;
    this.name = name;
  }
  
  public TriggerTypes getType() {
    return this.type;
  }
  
  public String toString() {
    return name;
  }
  
  public static TriggerTypes getTriggerType(String name) {
    if (ALWAYS.toString().equals(name)) return ALWAYS.getType();
    if (TIMED.toString().equals(name)) return TIMED.getType();
    if (EXP_RATE.toString().equals(name)) return EXP_RATE.getType();
    if (PROBABILITY.toString().equals(name)) return PROBABILITY.getType();
    if (CONDITION.toString().equals(name)) return CONDITION.getType();
    if (MESSAGE.toString().equals(name)) return MESSAGE.getType();
    throw new IllegalArgumentException("Unknown trigger type name: " + name);
  }
  
  public static String getName(TriggerTypes type) {
    if (ALWAYS.getType().equals(type)) return ALWAYS.toString();
    if (TIMED.getType().equals(type)) return TIMED.toString();
    if (EXP_RATE.getType().equals(type)) return EXP_RATE.toString();
    if (PROBABILITY.getType().equals(type)) return PROBABILITY.toString();
    if (CONDITION.getType().equals(type)) return CONDITION.toString();
    if (MESSAGE.getType().equals(type)) return MESSAGE.toString();
    
    throw new IllegalArgumentException("Unknown trigger type: " + type);
  }

}
