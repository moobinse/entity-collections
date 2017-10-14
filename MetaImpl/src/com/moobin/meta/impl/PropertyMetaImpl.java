package com.moobin.meta.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import com.moobin.ann.ReadOnly;
import com.moobin.ann.Required;
import com.moobin.meta.PropertyComposition;
import com.moobin.meta.PropertyMeta;
import com.moobin.meta.Meta;
import com.sun.javafx.collections.MappingChange.Map;

public class PropertyMetaImpl<V, P> implements PropertyMeta<V, P> {

	private Meta<V> owner;
	private String name;
	private PropertyComposition composition;
	private boolean required;
	private boolean readOnly;
	
	public PropertyMetaImpl(Field field) {
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
	public Meta<V> getOwner() {
		return owner;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Meta<P> getMeta() {
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

	
}
