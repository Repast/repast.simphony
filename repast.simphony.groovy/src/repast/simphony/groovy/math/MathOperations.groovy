/*
 * Author: Michael J. North
 */

package repast.simphony.groovy.math


public class MathOperations {
	
	public static def mathCategories() {
		def x = EmptyCategory
		try {
			def y = 1.0.kilograms			
		} catch (Exception e) {
			x = [UnitsCategory, MatrixCategory, CalculusCategory]
		}
		return x
	}

}

class EmptyCategory {
	
}
