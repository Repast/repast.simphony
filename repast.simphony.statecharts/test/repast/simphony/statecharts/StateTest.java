package repast.simphony.statecharts;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StateTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void lowestCommonAncestor() {
		CompositeState zero = new CompositeState("zero");
		CompositeState one = new CompositeState("one");
		CompositeState two = new CompositeState("two");
		CompositeState three = new CompositeState("three");
		AbstractState four = new SimpleState("four");
		AbstractState five = new SimpleState("five");
		AbstractState six = new SimpleState("six");
		zero.add(one);
		one.add(two);
		one.add(five);
		two.add(three);
		three.add(four);
		assertEquals(one,four.calculateLowestCommonAncestor(five));
		assertEquals(one,five.calculateLowestCommonAncestor(four));
		assertEquals(three,four.calculateLowestCommonAncestor(three));
		assertEquals(three,three.calculateLowestCommonAncestor(four));
		assertEquals(zero,five.calculateLowestCommonAncestor(zero));
		assertNull(four.calculateLowestCommonAncestor(six));
		assertNull(six.calculateLowestCommonAncestor(four));
	}

}
