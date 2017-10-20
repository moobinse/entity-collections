package org.moobin.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

import org.moobin.meta.EntityMeta;

public interface EntitySet<K extends Comparable<K>, V> {
	
	interface Listener<V> {

		void onAdd(V value);
		
		void onUpdate(V value, V oldValue);

		void onRemove(V value);
		
		void onClear();

		void onDestroy();
		
	}

	EntityMeta<V, K> getEntityMety();
	
	int getSize();
	
	Collection<V> getValues();

	EntitySet<K, V> filter(Predicate<V> filter);
	
	SortedEntitySet<K, V> sort(Comparator<V> comparator);
	
	V getValue(K key);

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
	
	Collection<V> addListener(Listener<V> listener);
	
	void removeListener(Listener<V> listener);
	
}
