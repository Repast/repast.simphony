/*
 * Author: Michael J. North
 */

package repast.simphony.groovy.math;

import javax.measure.unit.Unit;

import org.jscience.physics.amount.Amount;

public class UnitsOperations {

	public static Amount addition(Amount amount1, Amount amount2) {
		return amount1.plus(amount2);
	}

	public static Amount addition(Amount amount, java.lang.Number number) {
		Amount numberAmount = Amount.valueOf(number.doubleValue(), Unit.ONE);
		return amount.plus(numberAmount);
	}

	public static Amount addition(java.lang.Number number, Amount amount) {
		Amount numberAmount = Amount.valueOf(number.doubleValue(), Unit.ONE);
		return amount.plus(numberAmount);
	}

	public static Amount subtraction(Amount amount1, Amount amount2) {
		return amount1.minus(amount2);
	}

	public static Amount subtraction(Amount amount, java.lang.Number number) {
		Amount numberAmount = Amount.valueOf(number.doubleValue(), Unit.ONE);
		return amount.minus(numberAmount);
	}

	public static Amount subtraction(java.lang.Number number, Amount amount) {
		Amount numberAmount = Amount.valueOf(number.doubleValue(), Unit.ONE);
		return amount.minus(numberAmount);
	}

	public static Amount multiplication(Amount amount1, Amount amount2) {
		return amount1.times(amount2);
	}

	public static Amount multiplication(Amount amount, Number number) {
		return amount.times(number.doubleValue());
	}

	public static Amount multiplication(Number number, Amount amount) {
		return amount.times(number.doubleValue());
	}

	public static Amount division(Amount amount1, Amount amount2) {
		return amount1.divide(amount2);
	}

	public static Amount division(Amount amount, Number number) {
		return amount.divide(number.doubleValue());
	}

	public static Amount division(Number number, Amount amount) {
		return amount.divide(number.doubleValue()).inverse();
	}

	public static Amount exponentiation(Amount amount1, Amount amount2) {
		return Amount.valueOf(Math.pow(amount1.getEstimatedValue(), amount2
				.getEstimatedValue()), Unit.ONE);
	}

	public static Amount exponentiation(Amount amount, Number number) {
		return Amount.valueOf(Math.pow(amount.getEstimatedValue(), number
				.doubleValue()), Unit.ONE);
	}

	public static Amount exponentiation(Number number, Amount amount) {
		return Amount.valueOf(Math.pow(number.doubleValue(), amount
				.getEstimatedValue()), Unit.ONE);
	}

}
