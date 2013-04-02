package sample1;

import sample1.chart.Statechart;

public class Agent {
  
  private int id;
  private int state = -1;

  public Agent(int id, int start) {
    Statechart.createStateChart(this, start);
    this.id = id;
  }

  public int getId() {
    return id;
  }
  
  public void setState(int val) {
    state = val;
  }

  public int getState() {
    return state;
  }

}
