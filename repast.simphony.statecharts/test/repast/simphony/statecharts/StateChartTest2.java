package repast.simphony.statecharts;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;

public class StateChartTest2 {

	@Before
	public void setUp() throws Exception {
		RunEnvironment.init(new Schedule(), null, null, false);
		StateChartResolveActionScheduler.INSTANCE.initialize();
	}
	
	/**
	 * Statechart for composite state transition not being affected by internal transition.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart1 extends DefaultStateChart {

		public MyStateChart1(final MyAgent1 a) {
			CompositeState cs = new CompositeState("cs");
			this.registerEntryState(cs);
			AbstractState one = new SimpleState("one");
			AbstractState two = new SimpleState("two");
			AbstractState three = new SimpleState("three");
			cs.registerEntryState(one);
			cs.add(two);
			addRegularTransition(new Transition(new TimedTrigger(1), one, two));
			addRegularTransition(new Transition(new TimedTrigger(2), cs, three));
		}
	}

	private static class MyAgent1 {

		public StateChart st;

		public void setup() {
			st = new MyStateChart1(this);
			st.begin();
		}
	}

	@Test
	public void myStateChart1Scenario1() {
		MyAgent1 a = new MyAgent1();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentState().getId());
		schedule.execute();
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentState().getId());
	}

	


}
