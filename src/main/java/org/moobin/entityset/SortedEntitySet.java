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
import java.util.List;
import java.util.function.Predicate;

import org.moobin.meta.EntityDescription;

/**
 * 
 * @author Magnus Lenti
 *
 * @param <K>
 * @param <V>
 */
public interface SortedEntitySet<K extends Comparable<K>, V> extends EntitySet<K, V> {

	/**
	 * Get the source (unsorted) entity set
	 *  
	 * @return
	 */
	EntitySet<K, V> getSource();
	
	/**
	 * 
	 * Get value at indicated position
	 * 
	 * @param index
	 * @return
	 */
	V get(int index);
	
	/**
	 * 
	 * Get a list with values from indicated position
	 * 
	 * @param from
	 * @param count
	 * @return
	 */
	List<V> get(int from, int count);
	
	/**
	 * 
	 * Get position of value with key
	 * 
	 * @param key
	 * @return
	 */
	int indexByKey(K key);
	
	/**
	 * Get position of value
	 * 
	 * @param value
	 * @return
	 */
	default int indexByValue(V value) {
		return value == null ? -1 : indexByKey(getSource().getKey(value));
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	default EntityDescription<V, K> getEntityMeta() {
		return getSource().getEntityMeta();
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	default int size() {
		return getSource().size();
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	default Collection<V> getValues() {
		return getSource().getValues();
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	default EntitySet<K, V> filter(Predicate<V> filter) {
		return getSource().filter(filter);
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
	default SortedEntitySet<K, V> sort(Comparator<V> comparator) {
		return getSource().sort(comparator);
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override 
	default V getValue(K key) {
		return getSource().getValue(key);
	}

    /**
     * {@inheritDoc} 
     */
	@Override
	default Collection<V> addListener(EntitySetListener<V> listener) {
		return getSource().addListener(listener);
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
	default void removeListener(EntitySetListener<V> listener) {
		getSource().removeListener(listener);
	}
}
