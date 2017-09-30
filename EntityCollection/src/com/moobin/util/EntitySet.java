package com.moobin.util;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

import com.moobin.meta.EntityMeta;

public interface EntitySet<K, V> {

	EntityMeta<V, K> getEntity();
	
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
	
	int getSize();
	
	Collection<V> getValues();

	EntitySet<K, V> filter(Predicate<V> filter);

	void addListener(EntitySetListener<V> sourceListener);
	
}
