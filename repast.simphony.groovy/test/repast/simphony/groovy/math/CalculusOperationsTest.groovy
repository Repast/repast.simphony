/*
 * Author: Michael J. North
 */
 

package repast.simphony.groovy.math

import javax.measure.unit.SI;


// http://en.wikipedia.org/wiki/Derivative_(examples)

import junit.framework.TestCase

class CalculusOperationsTest extends TestCase {

	public CalculusOperationsTest(){
		RepastMathEMC.initAll();
	}
	
	void testDerivatives() {
		
    use (MathOperations.mathCategories()) {
			 
			assertEquals(0.0.kilograms, derivative({ 5.kilograms }, 3.pure,  0.0001.pure))
			
			def result = derivative({ 2.kilograms * it - 3.kilograms }, 4.1.pure, 0.0001.pure);
			assertEquals(SI.KILOGRAM, result.getUnit())
			assertEquals(2.kilograms.doubleValue(SI.KILOGRAM), result.doubleValue(SI.KILOGRAM), 0.0000001)

			
			result = derivative({ 2.meters / 1.0.seconds * it - 3.meters }, 4.1.seconds,  0.0001.seconds)
			assertEquals(SI.METERS_PER_SECOND, result.getUnit())
			assertEquals((2.0.meters / 1.0.seconds).doubleValue(SI.METERS_PER_SECOND), result.doubleValue(SI.METERS_PER_SECOND), 0.0000001)

			assertEquals(SI.METERS_PER_SECOND, result.getUnit())
			result = derivative({ 2.meters / 1.0.seconds * it - 3.meters }, 4.1.seconds)
			assertEquals(SI.METERS_PER_SECOND, result.getUnit())
			assertEquals((2.0.meters / 1.0.seconds).doubleValue(SI.METERS_PER_SECOND), result.doubleValue(SI.METERS_PER_SECOND), 0.0000001)
		}

	}

	void testIntegrals() {
		
		use (MathOperations.mathCategories()) {
			
			
			println(Math.PI / 4.0)
			println("integral 0 = " + integral({(1.pure - it ** 2).sqrt()}, 0.pure, 1.pure))

			def result = integral({ 5.meters }, 2.0.seconds,  3.0.seconds)
			println("integral 1 = " + result)
			
			
			
			
			println("integral 2 = " + integral({ it / 1.1.seconds }, 2.0.seconds,  3.0.seconds))
			println("integral 3 = " + integral({ 5.meters / 1.1.seconds * it }, 2.0.seconds,  3.0.seconds))
			println("integral 4 = " + integral({ 5.meters / 1.1.seconds * (it ** 2) }, 2.0.seconds,  3.0.seconds))
			println("derivative = " + derivative({5.meters * it}, 1.1.seconds))
			
			result = integral({it ** 3.pure}, 2.0.pure, 4.0.pure)
			print result
			double val = 1.0 / 4 * (4 ** 4 - 2 ** 4)
			print val
			
			
			//println("ODE      1 = "+
			//		integral({
			//			derivative({5.meters * it}, 1.1.seconds) +
			//			integral({ 5.meters / 1.1.seconds * (it ** 2) }, 2.0.seconds,  3.0.seconds)
			//		}, 2.0.seconds, 2.1.seconds))
		}
	}
}