/*
 * Author: Michael J. North
 */

package repast.simphony.groovy.math

import java.util.*
import javax.measure.unit.*
import org.jscience.physics.amount.*
import org.jscience.mathematics.number.*
import org.jscience.mathematics.vector.*

class CalculusCategory {

	static Amount derivative(Closure owningClosure, Closure targetFunction, Amount point) {
		return CalculusOperations.derivative(targetFunction, point)
	}

	static Amount derivative(Closure owningClosure, Closure targetFunction, Amount point, Amount secantLength) {
		return CalculusOperations.derivative(targetFunction, point, secantLength)
	}

	static Amount derivative(Closure owningClosure, Closure targetFunction, Amount point, Amount secantLength, Amount tolerance) {
		return CalculusOperations.derivative(targetFunction, point, secantLength, tolerance)
	}
	
	static Amount derivative(Closure owningClosure, Closure targetFunction, Amount point, Amount secantLength, Amount tolerance, Amount iterations) {
		return CalculusOperations.derivative(targetFunction, point, secantLength, tolerance, iterations)
	}

	static Amount integral(Closure owningClosure, Closure integrand, Amount lowerBound, Amount upperBound) {
		return CalculusOperations.integral(integrand, lowerBound, upperBound)
	}
	
	static Amount integral(Closure owningClosure, Closure integrand, Amount lowerBound, Amount upperBound, int type) {
		return CalculusOperations.integral(integrand, lowerBound, upperBound, type)
	}
	
	static Amount integral(Closure owningClosure, Closure integrand, Amount lowerBound, Amount upperBound, int type, Amount stepSize) {
		return CalculusOperations.integral(integrand, lowerBound, upperBound, type, stepSize)
	}
	
	static Amount integral(Closure owningClosure, Closure integrand, Amount lowerBound, Amount upperBound, int type, Amount stepSize, Amount tolerance, Amount iterations) {
		return CalculusOperations.integral(integrand, lowerBound, upperBound, type, stepSize, tolerance, iterations)
	}	
	
}