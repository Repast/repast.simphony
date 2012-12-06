package repast.simphony.statecharts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayDeque;
import java.util.Queue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import simphony.util.messages.MessageCenter;
import simphony.util.messages.MessageEvent;
import simphony.util.messages.MessageEventListener;

public class TriggerTests {

	static IAction action;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		action = new IAction() {

			@Override
			public void execute() {

			}
		};
	}

	@Before
	public void setUp() throws Exception {
		RunEnvironment.init(new Schedule(), null, null, false);
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		// This is required to ensure that the schedule is at tick = 0 when each test begins.
		schedule.schedule(ScheduleParameters.createOneTime(0), action);
		schedule.execute();
	}

	@Test
	public void timedAndConditionTriggers() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), action);
		AbstractTrigger<Object> tTimed = new TimedTrigger<Object>(1);
		tTimed.setAgent(new Object());
		tTimed.initialize();
		AbstractTrigger<Object> tCondition = new ConditionTrigger<Object>(new ConditionTriggerCondition<Object>(){

			@Override
			public boolean condition(Object agent, Transition<Object> transition)
					throws Exception {
				return true;
			}
			
		});
		tCondition.setAgent(new Object());// For testing purposes only
		tCondition.initialize();
		assertEquals(false, tTimed.isTriggerConditionTrue());
		assertEquals(false, tTimed.isTriggered());
		assertEquals(true, tCondition.isTriggerConditionTrue()); // valid but not triggered
													// because the scheduled
													// polling time is at 1 not
													// 0
		assertEquals(false, tCondition.isTriggered());
		schedule.execute();
		assertEquals(true, tTimed.isTriggerConditionTrue());
		assertEquals(true, tTimed.isTriggered());
		assertEquals(true, tCondition.isTriggerConditionTrue());
		assertEquals(true, tCondition.isTriggered());
	}

	@Test
	public void timedAndConditionTriggers2() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), action);
		schedule.schedule(ScheduleParameters.createOneTime(2), action);
		AbstractTrigger<Object> tTimed = new TimedTrigger<Object>(2);
		tTimed.setAgent(new Object());
		tTimed.initialize();
		AbstractTrigger<Object> tCondition = new ConditionTrigger<Object>(new ConditionTriggerCondition<Object>(){

			@Override
			public boolean condition(Object agent, Transition<Object> transition)
					throws Exception {
				return true;
			}
			
		});
		tCondition.setAgent(new Object());// For testing purposes only
		tCondition.initialize();
		assertEquals(false, tTimed.isTriggerConditionTrue());
		assertEquals(false, tTimed.isTriggered());
		assertEquals(true, tCondition.isTriggerConditionTrue());
		assertEquals(false, tCondition.isTriggered());
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals(false, tTimed.isTriggerConditionTrue());
		assertEquals(false, tTimed.isTriggered());
		assertEquals(true, tCondition.isTriggerConditionTrue());
		assertEquals(true, tCondition.isTriggered());
		schedule.execute();
		assertEquals(true, tTimed.isTriggerConditionTrue());
		assertEquals(true, tTimed.isTriggered());
		assertEquals(true, tCondition.isTriggerConditionTrue());
		assertEquals(true, tCondition.isTriggered());
	}

	@Test
	public void probabilityTrigger() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), action);
		AbstractTrigger<Object> tProb = new ProbabilityTrigger<Object>(1);
		tProb.setAgent(new Object());
		tProb.initialize();
		assertEquals(true, tProb.isTriggerConditionTrue());
		assertEquals(false, tProb.isTriggered());
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals(true, tProb.isTriggerConditionTrue());
		assertEquals(true, tProb.isTriggered());
		AbstractTrigger<Object> tProb2 = new ProbabilityTrigger<Object>(0.5);
		tProb2.setAgent(new Object());
		int counter = 0;
		while (true) {
			counter++;
			if (counter > 10) {
				// Should trigger by attempt 10 with probability 0.999.
				System.err
						.println("Not triggered by attempt "
								+ counter
								+ ". Should have triggered by now with probability 0.999.");
				fail();
			}
			tProb2.initialize();
			schedule.schedule(ScheduleParameters.createOneTime(1 + counter),
					action);
			schedule.execute();
			if (tProb2.isTriggered()) {
				break;
			}
		}
		assertTrue(true);

	}
	
	@Test
	public void expDecayTrigger() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(6.9), action); // Should have triggered with 0.999 probability.
		AbstractTrigger<Object> tExpDecay = new ExponentialDecayRateTrigger<Object>(1);
		tExpDecay.setAgent(new Object());
		tExpDecay.initialize();
		assertEquals(false, tExpDecay.isTriggerConditionTrue());
		assertEquals(false, tExpDecay.isTriggered());
		schedule.execute();
		
		assertEquals(true, tExpDecay.isTriggerConditionTrue());
		assertEquals(true, tExpDecay.isTriggered());
		assertEquals(6.9, schedule.getTickCount(), 0.0001);
	}
	
	static class MyMessageEventListener implements MessageEventListener{

		public boolean messageReceived = false;
		public String message;
		
		@Override
		public void messageReceived(MessageEvent me) {
			messageReceived = true;
			message = (String) me.getMessage();
		}
		
	}
	
	@Test
	public void msgCenterConditionTrigger() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), action);
		
		Trigger tCondition = new ConditionTrigger<Object>(new ConditionTriggerCondition<Object>(){

			@Override
			public String toString() {
				return "TestConditionTrigger";
			}
			
			@Override
			public boolean condition(Object agent, Transition<Object> transition)
					throws Exception {
				return true;
			}
			
		});
		
		tCondition.initialize();
		MyMessageEventListener mel = new MyMessageEventListener();
		MessageCenter.addMessageListener(mel);
		assertEquals(false, mel.messageReceived);
		assertEquals(false, tCondition.isTriggerConditionTrue());
		assertEquals(true, mel.messageReceived);
		assertEquals("Error encountered when calling condition: TestConditionTrigger in ConditionTrigger with pollingTime: 1.0",mel.message);
		
	}
	
	@Test
	public void messageTriggers(){
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), action);
		Queue<Object> queue = new ArrayDeque<Object>();
		
		Trigger mt1 = new MessageTrigger<Object>(queue, new UnconditionalByClassMessageChecker(String.class));
		mt1.initialize();
		assertEquals(false, mt1.isTriggerConditionTrue()); 
		assertEquals(false, mt1.isTriggered());
		queue.add("Hello");
		assertEquals(true, mt1.isTriggerConditionTrue());// valid but not triggered because the scheduled polling time is at 1 not 0
		assertEquals(false, mt1.isTriggered());
		schedule.execute();
		assertEquals(true, mt1.isTriggerConditionTrue());
		assertEquals(true, mt1.isTriggered());
		
		schedule.schedule(ScheduleParameters.createOneTime(1.5), action);
		queue.poll();
		MessageEqualsMessageChecker<Object,String> memc = new MessageEqualsMessageChecker<Object,String>("hello",String.class);
		memc.setAgent(new Object());
		Trigger mt2 = new MessageTrigger<Object>(queue, memc ,0.5);
		SimpleState<Object> ss = new SimpleStateBuilder<Object>("ss").build();
		new Transition<Object>(mt2,ss,ss); // Just so the transition is set in the trigger
		
		mt2.initialize();
		assertEquals(false, mt2.isTriggerConditionTrue()); 
		assertEquals(false, mt2.isTriggered());
		queue.add("hello");
		assertEquals(true, mt2.isTriggerConditionTrue());// valid but not triggered because the scheduled polling time is at 1 not 0
		assertEquals(false, mt2.isTriggered());
		schedule.execute();
		assertEquals(true, mt2.isTriggerConditionTrue());
		assertEquals(true, mt2.isTriggered());
		assertEquals(1.5, schedule.getTickCount(), 0.0001);

	}

}
