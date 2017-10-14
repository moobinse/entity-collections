package com.moobin.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

import com.moobin.meta.EntityMeta;

public interface EntitySet<K, V> {

	EntityMeta<V, K> getEntityMety();
	
	int getSize();
	
	Collection<V> getValues();

	EntitySet<K, V> filter(Predicate<V> filter);
	
	IndexedEntitySet<K, V> sort(Comparator<V> comparator);
	
	V getValue(K key);

	void addListener(EntitySetListener<V> sourceListener);
	
	default Class<V> getValueType() {
		return getEntityMety().getType();
	}
	
	default Class<K> getKeyType() {
		return getEntityMety().getKeyType();
	}
	
	default Function<V, K> getKeyFunction() {
		return getEntityMety()::getKey;
	}
	
	default K getKey(V value) {
		return getEntityMety().getKey(value);
	}
	
}
