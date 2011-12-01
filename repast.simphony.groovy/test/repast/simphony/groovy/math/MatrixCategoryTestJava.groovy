/*
 * Author: Michael J. North
 */

package repast.simphony.groovy.math;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import junit.framework.TestCase;

import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.SparseMatrix;
import org.jscience.physics.amount.Amount;

public class MatrixCategoryTestJava extends TestCase {
	
	public MatrixCategoryTestJava() {
		super();
	}

	public void testCreation() {
		
		Amount[][] c = [
			[
				Amount.valueOf(2.5, SI.KILOGRAM),
				Amount.valueOf(4.3, SI.KILOGRAM),
			],
			[
				Amount.valueOf(9.6, SI.KILOGRAM), 
				Amount.valueOf(5.7, SI.KILOGRAM)
			]
		];
		DenseMatrix d = MatrixOperations.createDenseMatrix(c);
		System.out.println("Creation Dense Matrix of Kilograms from Array: " + d.getClass() + " " + d.toString());
		
		Amount element;
		ArrayList arrayList = new ArrayList();
		ArrayList row;
		int rowIndex = 0;
		while (rowIndex < c.length) {
			row = new ArrayList();
			arrayList.add(row);
			int columnIndex = 0;
			while (columnIndex < c[rowIndex].length) {
				row.add(c[rowIndex][columnIndex]);
				columnIndex++;
			}
			rowIndex++;
		}
		DenseMatrix e = MatrixOperations.createDenseMatrix(arrayList);
		System.out.println("Creation Dense Matrix of Kilograms from ArrayList: " + e.getClass() + " " + e.toString());
			
		SparseMatrix f = MatrixOperations.createSparseMatrix(c, Amount.valueOf(0.0, SI.KILOGRAM));
		System.out.println("Creation Sparse Matrix of Kilograms from Array: " + f.getClass() + " " + f.toString());

		SparseMatrix g = MatrixOperations.createSparseMatrix(arrayList, Amount.valueOf(0.0, SI.KILOGRAM));
		System.out.println("Creation Sparse Matrix of Kilograms from ArrayList: " + g.getClass() + " " + g.toString());
		
	}
		
}