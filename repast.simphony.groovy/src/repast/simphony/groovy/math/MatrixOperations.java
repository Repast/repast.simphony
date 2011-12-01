/*
 * Author: Michael J. North
 */

package repast.simphony.groovy.math;

import java.util.ArrayList;

import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.Matrix;
import org.jscience.mathematics.vector.SparseMatrix;
import org.jscience.physics.amount.Amount;

public class MatrixOperations {

	public static DenseMatrix createDenseMatrix(ArrayList sourceArrayList) {
		
		ArrayList row;
		Amount element;
		Amount[][] sourceArray = new Amount[sourceArrayList.size()][((ArrayList) sourceArrayList.get(0)).size()];
		for (int rowIndex = 0; rowIndex < sourceArrayList.size(); rowIndex++) {
			row = (ArrayList) sourceArrayList.get(rowIndex);
			for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
				element = (Amount) row.get(columnIndex);
				sourceArray[rowIndex][columnIndex] = element;
			}
			
		}
		return DenseMatrix.valueOf(sourceArray);
		
	}
	
	public static DenseMatrix createDenseMatrix(Amount[][] sourceArray) {
		return DenseMatrix.valueOf(sourceArray);
		
	}

	public static SparseMatrix createSparseMatrix(ArrayList sourceArrayList, Amount zero) {
		return SparseMatrix.valueOf(MatrixOperations.createDenseMatrix(sourceArrayList), zero);
	}

	public static SparseMatrix createSparseMatrix(Amount[][] sourceArray, Amount zero) {
		return SparseMatrix.valueOf(MatrixOperations.createDenseMatrix(sourceArray), zero);
	}
	
	public static Matrix addition(Matrix m1, Matrix m2) {
		return m1.plus(m2);
	}

	public static Matrix subtraction(Matrix m1, Matrix m2) {
		return m1.minus(m2);
	}

	public static Matrix multiplication(Matrix m1, Matrix m2) {
		return m1.times(m2);
	}

	public static Matrix multiplication(Matrix m1, Amount amount) {
		return m1.times(amount);
	}
	
	public static Matrix division(Matrix m1, Amount amount) {
		return m1.times(amount.inverse());
	}

	public static Matrix multiplication(Amount amount, Matrix m1) {
		return m1.times(amount);
	}
	
	public static Matrix multiplication(Matrix m1, Number number) {
		return m1.times(Amount.valueOf(number.doubleValue(), Amount.ZERO.getUnit()));
	}
	
	public static Matrix division(Matrix m1, Number number) {
		return m1.times(Amount.valueOf(number.doubleValue(), Amount.ZERO.getUnit()).inverse());
	}

	public static Matrix multiplication(Number number, Matrix m1) {
		return m1.times(Amount.valueOf(number.doubleValue(), Amount.ZERO.getUnit()));
	}
	
	public static Matrix exponentiation(Matrix m1, int exponent) {
		return m1.pow(exponent);
	}
	
}
