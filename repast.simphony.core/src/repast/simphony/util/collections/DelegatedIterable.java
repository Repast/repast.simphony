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

import simphony.util.messages.MessageCenter;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * An iterable that retrieves the iterator when needed based on a method
 * call. You specify the method to call and the object to call it on and it
 * returns the result of the method call from iterator()
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class DelegatedIterable<AGENT_TYPE> implements Iterable<AGENT_TYPE> {
	private static final MessageCenter l4jLogger = MessageCenter.getMessageCenter(DelegatedIterable.class);
	
	private Object toCallOn;
	private Method methodToCall;
	
	
	/**
	 * Constructs this with the object to call the method on as toCallOn and the
	 * method to call as methodToCall [toCallOn.methodToCall()].
	 * 
	 * @param toCallOn
	 *            the object to call the specified method on
	 * @param methodToCall
	 *            the method to call on the object (must return Iterable<AGENT_TYPE>
	 *            and take no arguments)
	 */
	public DelegatedIterable(Object toCallOn, Method methodToCall) {
		this.toCallOn = toCallOn;
		this.methodToCall = methodToCall;
	}

	/**
	 * Fetches the result of calling the specified method on the specified object.
	 * 
	 * @return the return value of toCallOn.methodToCall()
	 */
	@SuppressWarnings("unchecked")
	public Iterator<AGENT_TYPE> iterator() {
		// TODO: clean up the error messages
		try {
			return (Iterator<AGENT_TYPE>) methodToCall
					.invoke(toCallOn);
		} catch (IllegalArgumentException e) {
			l4jLogger
					.error(
							"DelegatedIterator.getObjects: Error, the given method does not take 0 arguments",
							e);
			return null;
		} catch (ClassCastException e) {
			l4jLogger
					.error(
							"DelegatedIterator.getObjects: Error, the given method did not return a java.lang.Iterable",
							e);
			return null;
		} catch (Exception e) {
			l4jLogger
					.error(
							"DelegatedIterator.getObjects: Error calling the given method.",
							e);
			return null;
		}
	}
	
	/**
	 * Returns a hash code based on the object the method is to be called on and
	 * the method to call.
	 * 
	 * @return toCallOn.hashCode() ^ methodToCall.hashCode()
	 */
	@Override
	public int hashCode() {
		return toCallOn.hashCode() ^ methodToCall.hashCode();
	}

	/**
	 * Checks if the object is a DelegatedIterable and its method and object
	 * to call on are equal
	 * 
	 * @return if the objects share an equal method and method invocation target
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DelegatedIterable) {
			DelegatedIterable dosObj = (DelegatedIterable) obj;
			
			return dosObj.toCallOn.equals(this.toCallOn) 
				&& dosObj.methodToCall.equals(this.methodToCall);
		} else {
			return false;
		}
	}
}
