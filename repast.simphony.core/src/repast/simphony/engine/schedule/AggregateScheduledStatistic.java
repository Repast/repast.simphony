/*$$
 * Copyright (c) 2007, Argonne National Laboratory
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
 * Neither the name of the Repast project nor the names the names of its
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
package repast.simphony.engine.schedule;


/**
 * Types of statistics to compute on a given collection. Each
 * AggregateStatisticType implements a computeStastic method that takes in a set
 * of {@link java.lang.Number}s and returns the result of its computation on
 * them.
 * 
 * @see java.lang.Number
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public enum AggregateScheduledStatistic implements ScheduledStatistic {
	/**
	 * The sum of a set of values.
	 */
	SUM("Sum") {
		public double computeStatistic(Iterable<? extends Number> values) {
			double sum = 0;

			for (Number number : values) {
				sum += number.doubleValue();
			}

			return sum;
		}
	},
	/**
	 * The product of a set of values.
	 */
	PRODUCT("Product") {
		public double computeStatistic(Iterable<? extends Number> values) {
			double prod = 0;

			for (Number number : values) {
				prod *= number.doubleValue();
			}

			return prod;
		}
	},
	/**
	 * The max of a set of values.
	 */
	MAX("Max") {
		public double computeStatistic(Iterable<? extends Number> values) {
			double max = Double.NEGATIVE_INFINITY;

			for (Number number : values) {
				double doubleValue = number.doubleValue();

				if (max < doubleValue)
					max = doubleValue;
			}

			return max;
		}
	},
	/**
	 * The min of a set of values.
	 */
	MIN("Min") {
		public double computeStatistic(Iterable<? extends Number> values) {
			double min = Double.POSITIVE_INFINITY;

			for (Number number : values) {
				double doubleValue = number.doubleValue();

				if (min > doubleValue)
					min = doubleValue;
			}

			return min;
		}
	},
	/**
	 * The mean (average) of a set of values.
	 */
	MEAN("Mean") {
		public double computeStatistic(Iterable<? extends Number> values) {
			double max = Double.NEGATIVE_INFINITY;
			double min = Double.POSITIVE_INFINITY;

			for (Number number : values) {
				double doubleValue = number.doubleValue();

				if (max < doubleValue)
					max = doubleValue;

				if (min > doubleValue)
					min = doubleValue;

			}

			return (max - min) / 2.0;
		}
	};
	// /**
	// * The mode of a set of values.
	// */
	// MODE("Mode") {
	// public double computeStatistic(Iterable<Number> values) {
	// return 0;
	// }
	// };
	// TODO: add more of these, for example variance, ...

	public static final String STAT_COMPUTE_METH_NAME = "computeStatistic";

	private String description;

	private AggregateScheduledStatistic(String description) {
		this.description = description;
	}

	/**
	 * @return the description of this type
	 */
	public String toString() {
		return this.description;
	}

	/**
	 * Checks if a string matches this type's description (case-insensitive).
	 * 
	 * @param stringVal
	 *            a string to compare to this enumeration's description.
	 * @return if stringVal's the same (case-insensitive) as this type's
	 *         description
	 */
	public boolean equals(String stringVal) {
		return this.description.equalsIgnoreCase(stringVal);
	}
}
