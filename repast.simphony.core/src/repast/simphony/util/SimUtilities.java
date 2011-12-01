/*$$
 * Copyright (c) 1999, Trustees of the University of Chicago
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with 
 * or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *	 Redistributions of source code must retain the above copyright notice,
 *	 this list of conditions and the following disclaimer.
 *
 *	 Redistributions in binary form must reproduce the above copyright notice,
 *	 this list of conditions and the following disclaimer in the documentation
 *	 and/or other materials provided with the distribution.
 *
 * Neither the name of the University of Chicago nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *$$*/
package repast.simphony.util;

import cern.jet.random.Uniform;
import simphony.util.messages.MessageCenter;

import java.math.BigDecimal;
import java.util.List;

/**
 * A collection of utility methods.
 * 
 * @author Nick Collier
 */
public class SimUtilities {

	private static final MessageCenter msgCenter = MessageCenter
			.getMessageCenter(SimUtilities.class);

	private static double[] sinArray = new double[360];

	private static double[] cosArray = new double[360];

	static {
		// System.out.println("setting up trig arrays");
		for (int i = 0; i < 360; i++) {
			sinArray[i] = Math.sin(i * (Math.PI / 180));
			cosArray[i] = Math.cos(i * (Math.PI / 180));
		}
	}


	/**
	 * Captializes the specified String.
	 * 
	 * @param str
	 *            the String to capitalize.
	 * @return the capitalized String.
	 */
	public static String capitalize(String str) {
		char[] chars = str.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}

	/**
	 * Shuffles the specified list using the specifid Uniform rng. This list is shuffled by
	 * iterating backwards through the list and swapping the current item with a randomly chosen
	 * item. This randomly chosen item will occur before the current item in the list.
	 * 
	 * @param list
	 *            the List to shuffle
	 * @param rng
	 *            the random number generator to use for the shuffle
	 */
	public static void shuffle(List list, Uniform rng) {
		for (int i = list.size(); i > 1; i--)
			swap(list, i - 1, rng.nextIntFromTo(0, i - 1));
	}

	/**
	 * Shuffles the specified double[] using the specifid Uniform rng. This list is shuffled by
	 * iterating backwards through the list and swapping the current item with a randomly chosen
	 * item. This randomly chosen item will occur before the current item in the list.
	 * 
	 * @param array
	 *            the double[] to shuffle
	 * @param rng
	 *            the random number generator to use for the shuffle
	 */
	public static void shuffle(double[] array, Uniform rng) {
		for (int i = array.length; i > 1; i--)
			swap(array, i - 1, rng.nextIntFromTo(0, i - 1));
	}

	/**
	 * Swaps the two specified elements in the specified list.
	 */
	@SuppressWarnings("unchecked")
	private static void swap(List list, int i, int j) {
		Object tmp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, tmp);
	}

	private static void swap(double[] array, int i, int j) {
		double tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}

	/**
	 * Gets an x, y coordinate as a double[] of length 2, given a heading (0-359) and a distance.
	 * The point of origin is 0, 0 and the returned coordinate is relative to this distance. (An
	 * agent can calculate a new coordinate by adding the returned coordinates to its own x, y
	 * values.) This assumes that north = 0 degrees, east = 90 and so on.
	 * 
	 * @param heading
	 *            the heading in degrees
	 * @param distance
	 *            the distance to travel along the heading
	 * @return a double[] with the calculated coordinate
	 */
	public static double[] getPointFromHeadingAndDistance(int heading, int distance) {
		double y = sinArray[heading] * distance;
		double x = cosArray[heading] * distance;

		double[] retVal = new double[2];
		retVal[0] = x;
		retVal[1] = y;
		return retVal;
	}

	/**
	 * Normalize the specified value to the specified size
	 * 
	 * @param val
	 *            the value to normalize
	 * @param size
	 *            the size to normalize the value to
	 * @return the normalized value
	 */
	public static int norm(int val, int size) {
		if (val < 0 || val > size - 1) {
			while (val < 0)
				val += size;
			return val % size;
		}

		return val;
	}

	/**
	 * Scales a decimal value to the specified number of decimal places. This uses the BigDecimal
	 * class to perform the calculation to attempt to avoid overflows from other scaling methods. A
	 * side effect of this is the creation of BigDecimals which slows this down.
	 * 
	 * @param value
	 *            the value to convert
	 * @param decimalPlaces
	 *            the number of decimal places to keep
	 * @return a rounded value
	 */
	public static double scale(double value, int decimalPlaces) {
		return new BigDecimal(value).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

}
