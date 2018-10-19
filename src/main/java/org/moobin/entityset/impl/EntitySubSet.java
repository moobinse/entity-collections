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
package org.moobin.entityset.impl;

import java.util.function.Predicate;

/**
 * 
 * @author Magnus Lenti
 *
 * @param <K>
 * @param <V>
 */
class EntitySubSet<K extends Comparable<K>, V> extends EntitySetImpl<K, V> {

	private final Predicate<V> filter;

	/**
	 * 
	 * @param source
	 * @param filter
	 */
	public EntitySubSet(EntitySetImpl<K, V> source, Predicate<V> filter) {
		super(source);
		this.filter = filter;
		source.map.forEach((k, v) -> { if (filter.test(v)) map.put(k, v); });
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
	public V update(V value) {
		if (!filter.test(value)) {
			return removeByKey(getKey(value));
		}
		return super.update(value);
	}
	
}