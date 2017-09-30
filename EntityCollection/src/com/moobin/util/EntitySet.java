package com.moobin.util;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

import com.moobin.meta.EntityMeta;

public interface EntitySet<K, V> {

	EntityMeta<V, K> getEntityMety();
	
	int getSize();
	
	Collection<V> getValues();

	EntitySet<K, V> filter(Predicate<V> filter);

	void addListener(EntitySetListener<V> sourceListener);
	
	default Class<V> getValueType() {
		return getEntityMety().entityType();
	}
	
	default Class<K> getKeyType() {
		return getEntityMety().getKeyType();
	}
	
	default Function<V, K> getKeyFunction() {
		return getEntityMety()::getKey;
	}
	
	V getValue(K key);
	
	default K getKey(V value) {
		return getEntityMety().getKey(value);
	}
	
}
