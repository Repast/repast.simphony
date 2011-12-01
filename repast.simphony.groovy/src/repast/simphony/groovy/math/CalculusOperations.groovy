/*
 * Author: Michael J. North
 */

package repast.simphony.groovy.math

import org.jscience.physics.amount.Amount;
import javax.measure.unit.Unit


class CalculusOperations {
	
	public static defaultSecantLengthOrStepSize = 0.001
	public static defaultTolerance = 0.0000000001
	public static defaultIterations = 3.0

	public static final int EULER          = 0
	public static final int EULER_ADAPTIVE = 1
	public static final int RK4            = 2
	public static final int RK4_ADAPTIVE   = 3
	public static final int DEFAULT_TYPE   = EULER

	public static Amount derivative(Closure targetFunction, Amount point) {

		Amount secantLength = Amount.valueOf(CalculusOperations.defaultSecantLengthOrStepSize, point.getUnit())
		Amount tolerance = Amount.valueOf(CalculusOperations.defaultTolerance, point.getUnit())
		Amount iterations = Amount.valueOf(CalculusOperations.defaultIterations, point.getUnit())
		return CalculusOperations.derivative(targetFunction, point, secantLength, tolerance, iterations)
		
	}

	public static Amount derivative(Closure targetFunction, Amount point, Amount secantLength) {

		Amount results
		
		use (MathOperations.mathCategories()) {
			
			boolean debug = false;

			if (debug) println("10:")
			Amount deltaX = secantLength / 2.0
			if (debug) println("20:")
			if (secantLength.getEstimatedValue() == 0.0) {
				if (debug) println("30:")
				results = (Double.POSITIVE_INFINITY * targetFunction(point)) 
			} else {
				if (debug) println("40:")
				if (debug) println("   " + point)
				if (debug) println("   " + deltaX)
				if (debug) println("   " + secantLength)
				if (debug) println("   " + (point + deltaX))
				if (debug) println("   " + (point - deltaX))
				if (debug) println("   " + targetFunction(point + deltaX))
				if (debug) println("   " + targetFunction(point - deltaX))
				if (debug) println("   " + (targetFunction(point + deltaX) - targetFunction(point - deltaX)))
				results = ((targetFunction(point + deltaX) - targetFunction(point - deltaX)) /  secantLength)
				if (debug) println("   " + results)
			}
			
			if (debug) println("50:")
			if (debug) println("   " + results)
			
		}
		
		return results
		
	}

	public static Amount derivative(Closure targetFunction, Amount point, Amount secantLength, Amount tolerance) {

		Amount iterations = Amount.valueOf(CalculusOperations.defaultIterations, point.getUnit())
		return CalculusOperations.derivative(targetFunction, point, secantLength, tolerance, iterations)
		
	}
	
	public static Amount derivative(Closure targetFunction, Amount point, Amount secantLength, Amount tolerance, Amount iterations) {
		
		Amount result1
		Amount result2

		use (MathOperations.mathCategories()) {
			
			int counter = 0
			result1 = CalculusOperations.derivative(targetFunction, point, secantLength)
			secantLength = secantLength / 2
			result2 = CalculusOperations.derivative(targetFunction, point, secantLength)
			while ((counter < iterations.getEstimatedValue()) &&
					((((result1 - result2).getEstimatedValue()).abs()) <= tolerance.getEstimatedValue())) { 
				result1 = result2
				secantLength = secantLength / 2
				result2 = CalculusOperations.derivative(targetFunction, point, secantLength)
			}
			
		}
		
		return result2
		
	}

	protected static Amount integralCalculateEuler(Closure integrand, Amount lowerBound, Amount upperBound, Amount stepSize) {
		
		Amount result

		use (MathOperations.mathCategories()) {
			
			boolean debug = false;
			Amount bound = lowerBound 
			//print("unit: " + stepSize.getUnit())
			//print("fake result: " + integrand(2.0.pure) * stepSize)
			double halfPicket = stepSize.doubleValue(stepSize.getUnit()) / 2
			//int count = 0
			while (bound.getEstimatedValue() + halfPicket < upperBound.getEstimatedValue()) {
				//println("int bound : " + integrand(bound))
				//println("step: " + stepSize)
				if (result == null) result = integrand(bound) * stepSize
				else result = result + integrand(bound) * stepSize
				bound = bound + stepSize
				//print ("bound: " + bound)
				//print ("upper bound: " + upperBound)
				//count++
			}
			
			//print(count)
			
		}
		
		return result
	}

	protected static Amount integralCalculateRK4(Closure integrand, Amount lowerBound, Amount upperBound, Amount stepSize) {
		
		Amount result = Amount.valueOf(0.0, lowerBound.getUnit())
		
		use (MathOperations.mathCategories()) {

			boolean useFeedback = false
			if (integrand.parameterTypes.length >= 2) {
				useFeedback = true
			}
			Amount bound = lowerBound
			while (bound < upperBound) {
				
				// k1 = integrand (evaluated using stepsize' and result')
				// stepsize' = stepsize
				// result' = result
				Amount k1;
				if (useFeedback) k1 = integrand(bound + stepSize, result)
				else             k1 = integrand(bound + stepSize)
				
				// k2 = integrand (evaluated using stepsize' and result')
				// stepsize' = stepsize * 1/2
				// result' = result + 1/2 dt * k1
				Amount k2;
				if (useFeedback) k2 = integrand(integrand(bound + 0.5 * stepSize), result + 0.5 * stepSize + k1)
				else             k2 = integrand(integrand(bound + 0.5 * stepSize))
				
				// k3 = integrand (evaluated using stepsize' and result')
				// stepsize' = stepsize * 1/2
				// result' = result + 1/2 dt * k2
				Amount k3;
				if (useFeedback) k3 = integrand(integrand(bound + 0.5 * stepSize), result + 0.5 * stepSize + k2)
				else             k3 = integrand(integrand(bound + 0.5 * stepSize))
				
				// k4 = integrand (evaluated using stepsize' and result')
				// stepsize' = stepsize
				// result' = result + stepsize * k3
				Amount k4;
				if (useFeedback) k4 = integrand(bound + stepSize, result + stepSize + k3)
				else             k4 = integrand(bound + stepSize)

				result = (result + 
						stepSize * 1.0/6.0 * k1 +
						stepSize * 1.0/3.0 * k2 +
						stepSize * 1.0/3.0 * k3 +
						stepSize * 1.0/6.0 * k4)

				bound = bound + stepSize
				
			}
			
		}
		
		return result
		
	}
	
	public static Amount integral(Closure integrand, Amount lowerBound, Amount upperBound) {
		
		return CalculusOperations.integral(integrand, lowerBound, upperBound, CalculusOperations.DEFAULT_TYPE)
		
	}
	
	public static Amount integral(Closure integrand, Amount lowerBound, Amount upperBound, int type) {
		Amount stepSize = Amount.valueOf(CalculusOperations.defaultSecantLengthOrStepSize, lowerBound.getUnit())
		Amount tolerance = Amount.valueOf(CalculusOperations.defaultTolerance, lowerBound.getUnit())
		Amount iterations = Amount.valueOf(CalculusOperations.defaultIterations, lowerBound.getUnit())
		return CalculusOperations.integral(integrand, lowerBound, upperBound, type, stepSize, tolerance, iterations)
		
	}
	
	public static Amount integral(Closure integrand, Amount lowerBound, Amount upperBound, int type, Amount stepSize) {	
		Amount tolerance = Amount.valueOf(CalculusOperations.defaultTolerance, lowerBound.getUnit())
		Amount iterations = Amount.valueOf(CalculusOperations.defaultIterations, lowerBound.getUnit())
		return CalculusOperations.integral(integrand, lowerBound, upperBound, type, stepSize, tolerance, iterations)
	}
	
	protected static Amount integralCalculate(Closure integrand, Amount lowerBound, Amount upperBound, int type, Amount stepSize) {
		
		if (type == CalculusOperations.EULER) {
			return CalculusOperations.integralCalculateEuler(integrand, lowerBound, upperBound, stepSize)
		} else if (type == CalculusOperations.EULER_ADAPTIVE) {
			return CalculusOperations.integralCalculateEuler(integrand, lowerBound, upperBound, stepSize)
		} else if (type == CalculusOperations.RK4) {
			return CalculusOperations.integralCalculateRK4(integrand, lowerBound, upperBound, stepSize)
		} else if (type == CalculusOperations.RK4_ADAPTIVE) {
			return CalculusOperations.integralCalculateRK4(integrand, lowerBound, upperBound, stepSize)
		}
		
	}
	
	public static Amount integral(Closure integrand, Amount lowerBound, Amount upperBound, int type, Amount stepSize, Amount tolerance, Amount iterations) {
		if ((type == CalculusOperations.EULER) || (type == CalculusOperations.RK4)) {
			
			return CalculusOperations.integralCalculate(integrand, lowerBound, upperBound, type, stepSize)
			
		} else if ((type == CalculusOperations.EULER_ADAPTIVE) || (type == CalculusOperations.RK4_ADAPTIVE)) {
			Amount result1
			Amount result2
			double iters = iterations.doubleValue(iterations.getUnit())
			use (MathOperations.mathCategories()) {
				int counter = 0
				Amount currentStepSize = Amount.valueOf(CalculusOperations.defaultSecantLengthOrStepSize, lowerBound.getUnit())
				result1 = CalculusOperations.integralCalculate(integrand, lowerBound, upperBound, type, currentStepSize)
				currentStepSize = currentStepSize / 2.0
				result2 = CalculusOperations.integralCalculate(integrand, lowerBound, upperBound, type, currentStepSize)
				while (counter < iters && (((result1 - result2).getEstimatedValue()).abs()) <= tolerance.getEstimatedValue()) {
					result1 = result2
					result2 = CalculusOperations.integralCalculate(integrand, lowerBound, upperBound, type, currentStepSize)
					currentStepSize = currentStepSize / 2.0
					counter++
				}

			}
			return result2
			
		}
		
	}

}