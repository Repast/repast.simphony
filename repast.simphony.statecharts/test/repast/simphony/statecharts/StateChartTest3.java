/**
 * 
 */
package repast.simphony.statecharts;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import sample1.Agent;

/**
 * @author Nick Collier
 */
public class StateChartTest3 {
  
  private static final int START_TIME = 5;

  private Context<Agent> context;
  private ISchedule schedule;

  @Before
  public void setup() {
    schedule = new Schedule();
    RunEnvironment.init(schedule, null, null, false);
    StateChartScheduler.INSTANCE.initialize();

    context = new DefaultContext<Agent>();
    RunState.init().setMasterContext(context);
    
    int i = 0;
    for (i = 0; i < 10; ++i) {
      context.add(new Agent(i, 1));
    }
    
    for (; i < 20; ++i) {
      context.add(new Agent(i, START_TIME));
    }
  }

  private Agent getAgent(int id) {
    for (Agent agent : context) {
      if (agent.getId() == id)
        return agent;
    }
    return null;
  }

  @Test
  public void simpleTest() {
    Agent agent = getAgent(0);

    int j = 1;
    for (int i = 0; i < 20; ++i) {
      schedule.execute();
      assertEquals(j, agent.getState());
      ++j;
      if (j > 3) j = 1;
    }
  }
  
  @Test
  public void removeAgent() {
    for (int i = 0; i < 3; ++i) {
      schedule.execute();
    }
    
    Agent agent = getAgent(0);
    assertEquals(3, agent.getState());
    
    context.remove(agent);
    schedule.execute();
    // remains at 3 because the agent and thus 
    // the statechart are no longer in the sim.
    assertEquals(3, agent.getState());
  }
  
  @Test
  public void testDelayedBegin() {
    Agent agent = getAgent(12);
    int expState = agent.getState();
    for (int i = 0; i < START_TIME - 1; ++i) {
      schedule.execute();
      assertEquals(expState, agent.getState());
    }
    
    schedule.execute();
    assertEquals(1, agent.getState());
  }
  
  @Test
  public void testRemovedBeforeBegin() {
    Agent agent = getAgent(12);
    int expState = agent.getState();
    for (int i = 0; i < START_TIME - 1; ++i) {
      schedule.execute();
    }
    // make sure hasn't started yet
    assertEquals(expState, agent.getState());
    context.remove(agent);
    
    for (int i = 0; i < 3; ++i) {
      schedule.execute();
    }
    assertEquals(expState, agent.getState());
  }
  
  @Test
  public void testScaling() {
    context.clear();
    for (int i = 0; i < 200000; ++i) {
      context.add(new Agent(i, 1));
    }
    
    
    for (int i = 0; i < 2000; ++i) {
      System.out.println(i);
      schedule.execute();
    }
    
  }
}
