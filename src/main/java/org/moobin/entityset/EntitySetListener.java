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

/**
 * 
 * Listener for changes in this set
 * 
 * @author Magnus Lenti
 *
 * @param <V> value type
 */
public interface EntitySetListener<V> {

	/**
	 * Notification when value is added to set
	 * 
	 * @param value
	 */
	void onAdd(V value);

	/**
	 * Notification when value is updated
	 * 
	 * @param value
	 * @param oldValue
	 */
	void onUpdate(V value, V oldValue);

	/**
	 * Notification when value is removed from set
	 * 
	 * @param value
	 */
	void onRemove(V value);
	
	/**
	 * Notification when set is cleared
	 */
	void onClear();

	/**
	 * Notification when value is destroyed
	 */
	void onDestroy();
	
}
