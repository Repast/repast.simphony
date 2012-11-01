package repast.simphony.statecharts;

import static org.junit.Assert.*;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;

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
		schedule.schedule(ScheduleParameters.createOneTime(0), action);
		schedule.execute();
	}

	@Test
	public void timedAndConditionTriggers() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), action);
		Trigger tTimed = new TimedTrigger(1);
		tTimed.initialize();
		Trigger tCondition = new ConditionTrigger(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				return true;
			}
		});
		tCondition.initialize();
		assertEquals(false, tTimed.isValid());
		assertEquals(false, tTimed.isTriggered());
		assertEquals(true, tCondition.isValid()); // valid but not triggered
													// because the scheduled
													// polling time is at 1 not
													// 0
		assertEquals(false, tCondition.isTriggered());
		schedule.execute();
		assertEquals(true, tTimed.isValid());
		assertEquals(true, tTimed.isTriggered());
		assertEquals(true, tCondition.isValid());
		assertEquals(true, tCondition.isTriggered());
	}

	@Test
	public void timedAndConditionTriggers2() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), action);
		schedule.schedule(ScheduleParameters.createOneTime(2), action);
		Trigger tTimed = new TimedTrigger(2);
		tTimed.initialize();
		Trigger tCondition = new ConditionTrigger(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				return true;
			}
		});
		tCondition.initialize();
		assertEquals(false, tTimed.isValid());
		assertEquals(false, tTimed.isTriggered());
		assertEquals(true, tCondition.isValid());
		assertEquals(false, tCondition.isTriggered());
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals(false, tTimed.isValid());
		assertEquals(false, tTimed.isTriggered());
		assertEquals(true, tCondition.isValid());
		assertEquals(true, tCondition.isTriggered());
		schedule.execute();
		assertEquals(true, tTimed.isValid());
		assertEquals(true, tTimed.isTriggered());
		assertEquals(true, tCondition.isValid());
		assertEquals(true, tCondition.isTriggered());
	}

	@Test
	public void probabilityTrigger() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), action);
		Trigger tProb = new ProbabilityTrigger(1);
		tProb.initialize();
		assertEquals(true, tProb.isValid());
		assertEquals(false, tProb.isTriggered());
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals(true, tProb.isValid());
		assertEquals(true, tProb.isTriggered());
		Trigger tProb2 = new ProbabilityTrigger(0.5);
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
		Trigger tExpDecay = new ExponentialDecayRateTrigger(1);
		tExpDecay.initialize();
		assertEquals(false, tExpDecay.isValid());
		assertEquals(false, tExpDecay.isTriggered());
		schedule.execute();
		
		assertEquals(true, tExpDecay.isValid());
		assertEquals(true, tExpDecay.isTriggered());
		assertEquals(6.9, schedule.getTickCount(), 0.0001);

	}

}
