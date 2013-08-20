/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif’s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North’s Modifications are Copyright 2007 Under
 * the Repast Simphony License, All Rights Reserved)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. * Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. * Neither the name of the
 * Flow4J-Eclipse project nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package repast.simphony.agents.flows.tasks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author alex
 * 
 * TODO
 * 
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class AgentBuilderDictionary extends HashMap {
	public static final long serialVersionUID = 0;

	/**
	 * Constructs an empty AgentBuilderDictionary with the default initial
	 * capacity and the default load factor.
	 */
	public AgentBuilderDictionary() {
		super();
	}

	/**
	 * Constructs an empty AgentBuilderDictionary with the specified initial
	 * capacity and the default load factor.
	 * 
	 * @param initialCapacity
	 */
	public AgentBuilderDictionary(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Constructs an empty AgentBuilderDictionary with the specified initial
	 * capacity and load factor.
	 * 
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public AgentBuilderDictionary(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * Constructs a new AgentBuilderDictionary with the same mappings as the
	 * specified Map.
	 * 
	 * @param m
	 */
	public AgentBuilderDictionary(Map m) {
		super(m);
	}

	/**
	 * Returns true if the map contains the specified key. If extended is true
	 * then other containers than the underlying map can also be searched for
	 * the key.
	 * 
	 * @param key
	 *            key whose presence in this map is to be tested.
	 * @return true if this map contains a mapping for the specified key.
	 */
	public boolean extContainsKey(String key) {
		return containsKey(key);
	}

	/**
	 * Returns the value to which this map maps the specified key. Returns null
	 * if the map contains no mapping for this key.
	 * 
	 * @param key
	 *            key whose associated value is to be returned.
	 * @param isExtended
	 *            if true then other sources than the underlying map can be
	 *            searched
	 * @return the value to which this map maps the specified key, or null if
	 *         the map contains no mapping for this key.
	 * @throws ClassCastException
	 *             if the key is of an inappropriate type for this map
	 *             (optional).
	 * @throws NullPointerException
	 *             key is null and this map does not not permit null keys
	 *             (optional).
	 */
	public Object extGet(String key) throws ClassCastException,
			NullPointerException {
		return get(key);
	}

	/**
	 * Returns the <code>Integer</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Integer</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Integer getInteger(String key) throws AgentBuilderRuntimeException {
		if (!extContainsKey(key))
			throw new AgentBuilderRuntimeException(
					"Dictionary does not contain key \"" + key + "\"");
		return (Integer) extGet(key);
	}

	/**
	 * Returns the <code>Integer</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Integer</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Integer I(String key) throws AgentBuilderRuntimeException {
		return getInteger(key);
	}

	/**
	 * Returns the <code>int</code> value to which the specified key is
	 * mapped, or a AgentBuilderRuntimeException if no such key exists in the
	 * dictionary.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>int</code> value to which this map maps the specified
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public int getInt(String key) throws AgentBuilderRuntimeException {
		return getInteger(key).intValue();
	}

	/**
	 * Returns the <code>int</code> value to which the specified key is
	 * mapped, or a AgentBuilderRuntimeException if no such key exists in the
	 * dictionary.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>int</code> value to which this map maps the specified
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public int i(String key) throws AgentBuilderRuntimeException {
		return getInt(key);
	}

	/**
	 * Returns the <code>String</code> representation of the integer value
	 * that is stored in the dictionary under the given key.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>int</code> value to which this map maps the specified
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public String i2s(String key) throws AgentBuilderRuntimeException {
		return I(key).toString();
	}

	/**
	 * Returns the <code>String</code> representation of the integer value
	 * that is stored in the dictionary under the given key.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>int</code> value to which this map maps the specified
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key or the string
	 *             could not be converted to a number.
	 */
	public int s2i(String key) throws AgentBuilderRuntimeException {
		int result = 0;
		try {
			result = Integer.parseInt(str(key));
		} catch (NumberFormatException e) {
			throw new AgentBuilderRuntimeException(e);
		} catch (AgentBuilderRuntimeException e) {
			throw e;
		}
		return result;
	}

	/**
	 * Returns the <code>String</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>String</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public String getString(String key) throws AgentBuilderRuntimeException {
		if (!extContainsKey(key))
			throw new AgentBuilderRuntimeException(
					"Dictionary does not contain key \"" + key + "\"");
		return (String) extGet(key);
	}

	/**
	 * Returns the <code>String</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>String</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public String Str(String key) throws AgentBuilderRuntimeException {
		return getString(key);
	}

	/**
	 * Returns the <code>String</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>String</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public String str(String key) throws AgentBuilderRuntimeException {
		return getString(key);
	}

	/**
	 * Returns the <code>Float</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Float</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Float getFloat(String key) throws AgentBuilderRuntimeException {
		if (!extContainsKey(key))
			throw new AgentBuilderRuntimeException(
					"Dictionary does not contain key \"" + key + "\"");
		return (Float) extGet(key);
	}

	/**
	 * Returns the <code>Float</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Float</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Float F(String key) throws AgentBuilderRuntimeException {
		return getFloat(key);
	}

	/**
	 * Returns the <code>float</code> value to which the specified key is
	 * mapped, or a AgentBuilderRuntimeException if no such key exists in the
	 * dictionary.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>float</code> value to which this map maps the
	 *         specified key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public float f(String key) throws AgentBuilderRuntimeException {
		return getFloat(key).floatValue();
	}

	/**
	 * Returns the <code>Double</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Double</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Double getDouble(String key) throws AgentBuilderRuntimeException {
		if (!extContainsKey(key))
			throw new AgentBuilderRuntimeException(
					"Dictionary does not contain key \"" + key + "\"");
		return (Double) extGet(key);
	}

	/**
	 * Returns the <code>Double</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Double</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Double D(String key) throws AgentBuilderRuntimeException {
		return getDouble(key);
	}

	/**
	 * Returns the <code>double</code> value to which the specified key is
	 * mapped, or a AgentBuilderRuntimeException if no such key exists in the
	 * dictionary.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>double</code> value to which this map maps the
	 *         specified key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public double d(String key) throws AgentBuilderRuntimeException {
		return getDouble(key).doubleValue();
	}

	/**
	 * Returns the <code>Short</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Short</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Short getShort(String key) throws AgentBuilderRuntimeException {
		if (!extContainsKey(key))
			throw new AgentBuilderRuntimeException(
					"Dictionary does not contain key \"" + key + "\"");
		return (Short) extGet(key);
	}

	/**
	 * Returns the <code>Short</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Short</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Short S(String key) throws AgentBuilderRuntimeException {
		return getShort(key);
	}

	/**
	 * Returns the <code>short</code> value to which the specified key is
	 * mapped, or a AgentBuilderRuntimeException if no such key exists in the
	 * dictionary.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>short</code> value to which this map maps the
	 *         specified key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public short s(String key) throws AgentBuilderRuntimeException {
		return getShort(key).shortValue();
	}

	/**
	 * Returns the <code>Long</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Long</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Long getLong(String key) throws AgentBuilderRuntimeException {
		if (!extContainsKey(key))
			throw new AgentBuilderRuntimeException(
					"Dictionary does not contain key \"" + key + "\"");
		return (Long) extGet(key);
	}

	/**
	 * Returns the <code>Long</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Long</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Long L(String key) throws AgentBuilderRuntimeException {
		return getLong(key);
	}

	/**
	 * Returns the <code>long</code> value to which the specified key is
	 * mapped, or a AgentBuilderRuntimeException if no such key exists in the
	 * dictionary.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>long</code> value to which this map maps the
	 *         specified key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public long l(String key) throws AgentBuilderRuntimeException {
		return getLong(key).longValue();
	}

	/**
	 * Returns the <code>Boolean</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Boolean</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Boolean getBoolean(String key) throws AgentBuilderRuntimeException {
		if (!extContainsKey(key))
			throw new AgentBuilderRuntimeException(
					"Dictionary does not contain key \"" + key + "\"");
		return (Boolean) extGet(key);
	}

	/**
	 * Returns the <code>Boolean</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Boolean</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Boolean Bool(String key) throws AgentBuilderRuntimeException {
		if (!extContainsKey(key))
			throw new AgentBuilderRuntimeException(
					"Dictionary does not contain key \"" + key + "\"");
		return (Boolean) extGet(key);
	}

	/**
	 * Returns the <code>Boolean</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Boolean</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Boolean B(String key) throws AgentBuilderRuntimeException {
		if (!extContainsKey(key))
			throw new AgentBuilderRuntimeException(
					"Dictionary does not contain key \"" + key + "\"");
		return (Boolean) extGet(key);
	}

	/**
	 * Returns the <code>boolean</code> value to which the specified key is
	 * mapped, or a AgentBuilderRuntimeException if no such key exists in the
	 * dictionary.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>boolean</code> value to which this map maps the
	 *         specified key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public boolean bool(String key) throws AgentBuilderRuntimeException {
		return getBoolean(key).booleanValue();
	}

	/**
	 * Returns the <code>boolean</code> value to which the specified key is
	 * mapped, or a AgentBuilderRuntimeException if no such key exists in the
	 * dictionary.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>boolean</code> value to which this map maps the
	 *         specified key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public boolean b(String key) throws AgentBuilderRuntimeException {
		return getBoolean(key).booleanValue();
	}

	/**
	 * Returns the <code>Object</code> value to which the specified key is
	 * mapped, or null if the map contains no mapping for this key
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the <code>Object</code> value to which this map maps the
	 *         specified key, or null if the map contains no mapping for this
	 *         key
	 * @throws AgentBuilderRuntimeException
	 *             if the dictionary does not contain such a key
	 */
	public Object obj(String key) throws AgentBuilderRuntimeException {
		if (!extContainsKey(key))
			throw new AgentBuilderRuntimeException(
					"Dictionary does not contain key \"" + key + "\"");
		return extGet(key);
	}

	/**
	 * Returns the Dictionary Contents.
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("Dictionary contents:\n");
		for (Iterator iter = entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			buf.append("  ").append(entry.getKey()).append(" = ").append(
					entry.getValue()).append("\n");
		}
		return buf.toString();
	}

}
