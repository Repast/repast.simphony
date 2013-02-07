/**
 * 
 */
package repast.simphony.engine.schedule;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.controller.ScheduledMethodControllerAction;
import repast.simphony.engine.environment.DefaultScheduleRegistry;
import repast.simphony.engine.environment.RunInfo;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.environment.ScheduleRegistry;

/**
 * Tests the ScheduleMethodController's detection of @ScheduledMethod annotations. 
 * 
 * @author Nick Collier
 */
public class ScheduledMethodControllerTest {
  
  private ISchedule schedule;
  private List<Result> results;
  
  @Before
  public void setup() {
    schedule = new Schedule();
    results = new ArrayList<Result>();
  }
  
  private void setupAction(Context<Object> context, Class<?>... classes) {
    RunInfo info = new RunInfo("test", 0, 0);
    ScheduleRegistry reg = new DefaultScheduleRegistry();
    reg.setModelSchedule(schedule);
    RunState rs = RunState.init(info, reg, null);
    rs.setMasterContext(context);
    
    ScheduledMethodControllerAction action = new ScheduledMethodControllerAction();
    List<Class<?>> list = new ArrayList<Class<?>>();
    for (Class<?> clazz : classes) {
      list.add(clazz);
    }
    action.processAnnotations(list);
    action.runInitialize(rs, null, null);
  }
  
  private Context<Object> setupAgentA(Context<Object> context) {
    for (int i = 0; i < 20; i++) {
      context.add(new AgentA(results, schedule));
    }
    
    return context;
  }
  
  private Context<Object> setupAgentB(Context<Object> context) {
    for (int i = 0; i < 20; i++) {
      context.add(new AgentB(results, schedule));
    }
    
    return context;
  }
  
  private Context<Object> setupAgentC(Context<Object> context) {
    for (int i = 0; i < 20; i++) {
      context.add(new AgentC(results, schedule));
    }
    
    return context;
  }
  
  @Test
  public void simpleTest() {
    Context<Object> context = new DefaultContext<Object>();
    setupAgentA(context);
    setupAction(context, AgentA.class);
    results.clear();
    schedule.execute();
    // each of the 20 add a result.
    assertEquals(20, results.size());
    for (Result result : results) {
      assertEquals(MethodName.M1, result.methodName);
      assertEquals(1.0, result.tick, 0);
    }
    
    results.clear();
    schedule.execute();
    // each of the picked 3 out of the 20 add a result.
    assertEquals(3, results.size());
    for (Result result : results) {
      assertEquals(MethodName.M2, result.methodName);
      assertEquals(1.5, result.tick, 0);
    }
    
    results.clear();
    schedule.execute();
    // each of the 20 add a result.
    assertEquals(20, results.size());
    for (Result result : results) {
      assertEquals(MethodName.M1, result.methodName);
      assertEquals(2.0, result.tick, 0);
    }
  }
  
  @Test
  public void overrideTest() {
    Context<Object> context = new DefaultContext<Object>();
    setupAgentB(context);
    setupAction(context, AgentB.class);
    results.clear();
    schedule.execute();
    // each of the 20 add a result.
    assertEquals(20, results.size());
    for (Result result : results) {
      assertEquals(MethodName.M1B, result.methodName);
      assertEquals(1.25, result.tick, 0);
    }
  }
  
  @Test
  public void overrideTest2() {
    Context<Object> context = new DefaultContext<Object>();
    setupAgentA(context);
    setupAgentB(context);
    setupAction(context, AgentA.class, AgentB.class);
    results.clear();
    schedule.execute();
    // each of the 20 add a result.
    // this makes sure only the 20 AgentAs get run
    // rather than everything that extends AgentA
    assertEquals(20, results.size());
    for (Result result : results) {
      assertEquals(MethodName.M1, result.methodName);
      assertEquals(1.0, result.tick, 0);
    }
    
    results.clear();
    schedule.execute();
    // each of the 20 add a result.
    assertEquals(20, results.size());
    for (Result result : results) {
      assertEquals(MethodName.M1B, result.methodName);
      assertEquals(1.25, result.tick, 0);
    }
    
    results.clear();
    schedule.execute();
    // each of the picked 3 out of the 20 add a result.
    assertEquals(3, results.size());
    for (Result result : results) {
      assertEquals(MethodName.M2, result.methodName);
      assertEquals(1.5, result.tick, 0);
    }
    
    results.clear();
    schedule.execute();
    // each of the 20 add a result.
    // this makes sure only the 20 AgentAs get run
    // rather than everything that extends AgentA
    assertEquals(20, results.size());
    for (Result result : results) {
      assertEquals(MethodName.M1, result.methodName);
      assertEquals(2.0, result.tick, 0);
    }
    
    results.clear();
    schedule.execute();
    // 40 because both A and B executed method3 at tick 2.5 via
    // A's ScheduleMethodAnnotation which B inherits and does not
    // override
    assertEquals(40, results.size());
    for (Result result : results) {
      assertEquals(MethodName.M3, result.methodName);
      assertEquals(2.5, result.tick, 0);
    }
  }
  
  @Test
  public void scheduledInterfaceTest() {
    Context<Object> context = new DefaultContext<Object>();
    setupAgentC(context);
    setupAction(context, AgentC.class);
    
    results.clear();
    schedule.execute();
    // ISMTest interface implementor's scheduled at tick 1
    // but C overrides it so no results even though C implements
    // it.
    assertEquals(0, results.size());
    assertEquals(1.0, schedule.getTickCount(), 0);
    
    results.clear();
    schedule.execute();
    // 20 AgentCs
    assertEquals(20, results.size());
    for (Result result : results) {
      assertEquals(MethodName.M1C, result.methodName);
      // overrides the start time given by the interface
      assertEquals(1.25, result.tick, 0);
    }
    
    results.clear();
    schedule.execute();
    // 20 AgentCs
    assertEquals(20, results.size());
    for (Result result : results) {
      assertEquals(MethodName.M2C, result.methodName);
      // scheduled via the interface for 1.5
      assertEquals(1.5, result.tick, 0);
    }
  }
}
