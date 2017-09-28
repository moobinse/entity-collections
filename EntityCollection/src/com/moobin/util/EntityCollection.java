package com.moobin.util;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

public interface EntityCollection<K, V> {

	Class<V> getValueType();
	
	Class<K> getKeyType();
	
	Function<V, K> getKeyFunction();

	V getValue(K key);
	
	K getKey(V value);
	
	V update(V value);
	
	V removeByKey(K key);
	
	void clear();
	
	default V remove(V value) {
		return removeByKey(getKey(value));
	}
	
	int getSize();
	
	Collection<V> getValues();

	EntityCollection<K, V> filter(Predicate<V> filter);

	
}
