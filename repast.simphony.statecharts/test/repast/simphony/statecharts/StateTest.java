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
		SimpleState<Object> four = new SimpleStateBuilder<Object>(
				"four").build();
		SimpleState<Object> five = new SimpleStateBuilder<Object>(
				"five").build();
		SimpleState<Object> six = new SimpleStateBuilder<Object>(
				"six").build();
		
		CompositeStateBuilder<Object> csb3 = new CompositeStateBuilder<Object>(
				"three",four);
		CompositeState<Object> three = csb3.build();
		
		CompositeStateBuilder<Object> csb2 = new CompositeStateBuilder<Object>(
				"two",three);
		CompositeState<Object> two = csb2.build();
		
		CompositeStateBuilder<Object> csb1 = new CompositeStateBuilder<Object>(
				"one",two);
		csb1.addChildState(five);
		CompositeState<Object> one = csb1.build();
		
		CompositeStateBuilder<Object> csb0 = new CompositeStateBuilder<Object>(
				"zero",one);
		CompositeState<Object> zero = csb0.build();
				
		assertEquals(one,four.calculateLowestCommonAncestor(five));
		assertEquals(one,five.calculateLowestCommonAncestor(four));
		assertEquals(three,four.calculateLowestCommonAncestor(three));
		assertEquals(three,three.calculateLowestCommonAncestor(four));
		assertEquals(zero,five.calculateLowestCommonAncestor(zero));
		assertNull(four.calculateLowestCommonAncestor(six));
		assertNull(six.calculateLowestCommonAncestor(four));
	}
	
	
	@Test
	public void getDefaultDestinationFromHistory(){
		SimpleState<Object> two = new SimpleStateBuilder<Object>(
				"two").build();
		
		CompositeStateBuilder<Object> csb1 = new CompositeStateBuilder<Object>(
				"one",two);
		CompositeState<Object> one = csb1.build();
		
		HistoryState<Object> hs1 = new HistoryStateBuilder<Object>("hs1").build();
		HistoryState<Object> hs2 = new HistoryStateBuilder<Object>("hs2",false).build();
		
		CompositeStateBuilder<Object> csb0 = new CompositeStateBuilder<Object>(
				"zero",one);
		csb0.addHistoryState(hs1);
		csb0.addHistoryState(hs2);
		@SuppressWarnings("unused")
		CompositeState<Object> zero = csb0.build();
		
		assertEquals(one,hs1.getDestination());
		assertEquals(one,hs2.getDestination());
		
	}

}
