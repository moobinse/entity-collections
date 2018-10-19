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
package org.moobin.meta.impl;

import java.lang.reflect.Field;
import java.util.function.Function;

import org.moobin.meta.EntityDescription;

public class EntityMetaImpl<V, K> extends MetaImpl<V> implements EntityDescription<V, K> {

	private Function<V, K> keyFunction;
	private Class<K> keyType;

	public EntityMetaImpl(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction) {
		super(valueType);
		this.keyFunction = keyFunction;
		this.keyType = keyType;
	}
	
	@SuppressWarnings("unchecked")
	public EntityMetaImpl(Class<V> valueType, String keyField) throws NoSuchFieldException, SecurityException {
		super(valueType);
		Field field = valueType.getField(keyField);
		this.keyType = (Class<K>) field.getType();
		this.keyFunction = v -> {
			try {
				return (K) field.get(v);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException();
			}
		};
	}

	@Override
	public K getKey(V value) {
		return keyFunction.apply(value);
	}

	@Override
	public Class<K> getKeyType() {
		return keyType;
	}

}
