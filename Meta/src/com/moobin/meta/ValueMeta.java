package com.moobin.meta;

import java.util.Map;

public interface ValueMeta<V> {

	Class<V> getType();

	Map<String, PropertyMeta<V, ?>> getProperties();
	
	@SuppressWarnings("unchecked")
	default <T> PropertyMeta<V, T> getProperty(String name) {
		return (PropertyMeta<V, T>) getProperties().get(name);
	}
	
}
