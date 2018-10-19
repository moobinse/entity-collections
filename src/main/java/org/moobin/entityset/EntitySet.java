/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Magnus Lenti (moobin@moobin.org)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.moobin.entityset;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

import org.moobin.meta.EntityDescription;

/**
 * 
 * Entity set
 * 
 * @author Magnus Lenti
 *
 * @param <K> key type
 * @param <V> value type
 */
public interface EntitySet<K extends Comparable<K>, V> {
	
	/**
	 * Get the size of this set
	 * @return
	 */
	int size();
	
	/**
	 * Get a collection
	 * @return
	 */
	Collection<V> getValues();

	/**
	 * Get a subset
	 * 
	 * @param filter
	 * @return the filtered subset
	 */
	EntitySet<K, V> filter(Predicate<V> filter);

	/**
	 * Get a sorted set
	 *  
	 * @param comparator
	 * @return
	 */
	SortedEntitySet<K, V> sort(Comparator<V> comparator);

	/**
	 * Get a value by its key
	 * 
	 * @param key
	 * @return
	 */
	V getValue(K key);

	/**
	 * Get the value type of this set
	 * @return
	 */
	Class<V> getValueType();
	
	/**
	 * Get the key type of the values 
	 * @return
	 */
	Class<K> getKeyType();
	
	/**
	 * Get the key of a value
	 * 
	 * @param value
	 * @return
	 */
	K getKey(V value);

	/**
	 * Add a listener 
	 * 
	 * @param listener
	 * @return
	 */
	Collection<V> addListener(EntitySetListener<V> listener);

	/**
	 * Remove a listener
	 * @param listener
	 */
	void removeListener(EntitySetListener<V> listener);
	
	/**
	 * Get current state 
	 * @return
	 */
	EntitySetState getState();
	
}
