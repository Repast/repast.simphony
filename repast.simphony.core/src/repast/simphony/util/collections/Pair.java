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
package repast.simphony.util.collections;

/**
 * A simple pair class that represents a tuple with two elements.<p/>
 * 
 * This class is not implemented to handle null pair values, however it will
 * accept them.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class Pair<X, Y> {

	private X first;
	private Y second;

	/**
	 * Constructs this class with the specified first and second values
	 * 
	 * @param first
	 *            the first value
	 * @param second
	 *            the second value
	 */
	public Pair(X first, Y second) {
		this.first = first;
		this.second = second;
		
	}

	/**
	 * Compares this pair to another object.
	 * 
	 * @return if the other object is a pair with the same first and second
	 *         members
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof Pair) {
			Pair objPair = (Pair) obj;
			return (objPair.first.equals(first) && objPair.second
					.equals(second));
		}

		return false;
	}

	/**
	 * Returns a hashCode of this objects first and second elements xor'd.
	 * 
	 * @return <code>first.hashCode() ^ second.hashCode
	 */
	public int hashCode() {
          int hashcode = 17;
          hashcode = 31 * hashcode + first.hashCode();
          hashcode = 31 * hashcode + second.hashCode();
          return hashcode;
	}

	/**
	 * Retrieves the first element in the tuple (first, second).
	 * 
	 * @return the first element
	 */
	public X getFirst() {
		return first;
	}

	/**
	 * Retrieves the second element in the tuple (first, second).
	 * 
	 * @return the second element
	 */
	public Y getSecond() {
		return second;
	}

	/**
	 * Sets the first element in the tuple (first, second).
	 * 
	 * @param first
	 *            the first element
	 */
	public void setFirst(X first) {
		this.first = first;
	}

	/**
	 * Sets the second element in the tuple (first, second).
	 * 
	 * @param second
	 *            the second element
	 */
	public void setSecond(Y second) {
		this.second = second;
	}
}
