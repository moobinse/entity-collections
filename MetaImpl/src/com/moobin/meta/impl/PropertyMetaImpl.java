package com.moobin.meta.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import com.moobin.ann.ReadOnly;
import com.moobin.ann.Required;
import com.moobin.meta.Composition;
import com.moobin.meta.PropertyMeta;
import com.moobin.meta.ValueMeta;
import com.sun.javafx.collections.MappingChange.Map;

public class PropertyMetaImpl<V, P> implements PropertyMeta<V, P> {

	private ValueMeta<V> owner;
	private String name;
	private Composition composition;
	private boolean required;
	private boolean readOnly;
	
	public PropertyMetaImpl(Field field) {
		name = field.getName();
		required = field.getType().isPrimitive() || field.getAnnotation(Required.class) != null;
		readOnly = field.getAnnotation(ReadOnly.class) != null;
		composition = getComposition(field.getType());
	}
	
	private static Composition getComposition(Class<?> clazz) {
		if (clazz.isArray()) {
			return Composition.list;
		}
		if (List.class.isAssignableFrom(clazz)) {
			return Composition.list;
		}
		if (Set.class.isAssignableFrom(clazz)) {
			return Composition.list;
		}
		if (Map.class.isAssignableFrom(clazz)) {
			return Composition.map;
		}
		return Composition.singleValue;
	}

	@Override
	public ValueMeta<V> getOwner() {
		return owner;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ValueMeta<P> getMeta() {
		return null;
	}

	@Override
	public Composition getComposition() {
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

	
}
