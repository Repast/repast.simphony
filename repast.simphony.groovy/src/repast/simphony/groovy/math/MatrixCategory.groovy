/*
 * Author: Michael J. North
 */

package repast.simphony.groovy.math

import java.util.*
import javax.measure.unit.*
import org.jscience.physics.amount.*
import org.jscience.mathematics.number.*
import org.jscience.mathematics.vector.*

// Amount: http://jscience.org/api/org/jscience/physics/amount/Amount.html
//
// SI units: http://jscience.org/api/javax/measure/unit/SI.html
// NonSI Units: http://jscience.org/api/javax/measure/unit/NonSI.html

class MatrixCategory {

	static DenseMatrix getDenseMatrix(ArrayList sourceArrayList) {
        return MatrixOperations.createDenseMatrix(sourceArrayList);
    }

	static DenseMatrix getMatrix(ArrayList sourceArrayList) {
        return MatrixOperations.createDenseMatrix(sourceArrayList);
    }
	
	static SparseMatrix sparseMatrix(ArrayList sourceArrayList, Amount zero) {
        return MatrixOperations.createSparseMatrix(sourceArrayList, zero);
    }

	static Matrix plus(Matrix m1, Matrix m2) {
        return MatrixOperations.addition(m1, m2);
    }

	static Matrix minus(Matrix m1, Matrix m2) {
        return MatrixOperations.subtraction(m1, m2);
    }
	
	static Matrix multiply(Matrix m1, Matrix m2) {
        return MatrixOperations.multiplication(m1, m2);
    }

	static Matrix multiply(Matrix m1, Amount amount) {
        return MatrixOperations.multiplication(m1, amount);
    }

	static Matrix multiply(Amount amount, Matrix m1) {
        return MatrixOperations.multiplication(m1, amount);
    }
	
	static Matrix multiply(Matrix m1, java.lang.Number number) {
        return MatrixOperations.multiplication(m1, number);
    }

	static Matrix multiply(java.lang.Number number, Matrix m1) {
        return MatrixOperations.multiplication(m1, number);
    }
	
	static Matrix power(Matrix m1, int number) {
        return MatrixOperations.exponentiation(m1, number);
    }
	
}