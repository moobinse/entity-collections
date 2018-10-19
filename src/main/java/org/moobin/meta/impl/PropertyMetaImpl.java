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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.moobin.meta.PropertyComposition;
import org.moobin.meta.PropertyDescription;
import org.moobin.meta.ValueDescription;
import org.moobin.meta.annotation.ReadOnly;
import org.moobin.meta.annotation.Required;

public class PropertyMetaImpl<V, P> implements PropertyDescription<V, P> {

	private ValueDescription<V> owner;
	private String name;
	private PropertyComposition composition;
	private boolean required;
	private boolean readOnly;
	private Field field;
	
	public PropertyMetaImpl(Field field) {
		this.field = field;
		field.setAccessible(true);
		name = field.getName();
		required = field.getType().isPrimitive() || field.getAnnotation(Required.class) != null;
		readOnly = field.getAnnotation(ReadOnly.class) != null;
		composition = getComposition(field.getType());
	}
	
	private static PropertyComposition getComposition(Class<?> clazz) {
		if (clazz.isArray()) {
			return PropertyComposition.list;
		}
		if (List.class.isAssignableFrom(clazz)) {
			return PropertyComposition.list;
		}
		if (Set.class.isAssignableFrom(clazz)) {
			return PropertyComposition.list;
		}
		if (Map.class.isAssignableFrom(clazz)) {
			return PropertyComposition.map;
		}
		return PropertyComposition.singleValue;
	}

	@Override
	public ValueDescription<V> getOwner() {
		return owner;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ValueDescription<P> getValueDescription() {
		return null;
	}

	@Override
	public PropertyComposition getComposition() {
		return composition;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public Object getProperty(V value) {
		try {
			return field.get(value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
}
