package com.moobin.util;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

public interface EntitySet<K, V> {

	Entity<V, K> getEntity();
	
	default Class<V> getValueType() {
		return getEntity().entityType();
	}
	
	default Class<K> getKeyType() {
		return getEntity().keyType();
	}
	
	default Function<V, K> getKeyFunction() {
		return getEntity()::getKey;
	}

	V getValue(K key);
	
	default K getKey(V value) {
		return getEntity().getKey(value);
	}
	
	V update(V value);
	
	V removeByKey(K key);
	
	void clear();
	
	default V remove(V value) {
		return removeByKey(getKey(value));
	}
	
	int getSize();
	
	Collection<V> getValues();

	EntitySet<K, V> filter(Predicate<V> filter);
	
}
