package repast.simphony.statecharts;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StateChartTest {

	@Test
	public void removeTime() {
		List<Double> times = new ArrayList<Double>();
		double a = 1.2, b=2.3;
		times.add(a);
		times.add(b);
		assertEquals(2,times.size());
		times.remove(2.3);
		assertEquals(1,times.size());
		// Within double precision
		assertEquals(false,times.remove(1.200000000000001));
		assertEquals(1,times.size());
		// Beyond double precision
		assertEquals(true,times.remove(1.20000000000000001));
		assertEquals(0,times.size());
	}

}
