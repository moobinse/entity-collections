package org.moobin.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.moobin.meta.EntityMeta;

public interface SortedEntitySet<K extends Comparable<K>, V> extends EntitySet<K, V> {

	EntitySet<K, V> getSource();
	
	V get(int index);
	
	List<V> get(int from, int count);
	
	int indexByKey(K key);
	
	default int indexByValue(V value) {
		return value == null ? -1 : indexByKey(getSource().getKey(value));
	}

	@Override
	default EntityMeta<V, K> getEntityMety() {
		return getSource().getEntityMety();
	}

	@Override
	default int getSize() {
		return getSource().getSize();
	}

	@Override
	default Collection<V> getValues() {
		return getSource().getValues();
	}

	@Override
	default EntitySet<K, V> filter(Predicate<V> filter) {
		return getSource().filter(filter);
	}
	
	@Override
	default SortedEntitySet<K, V> sort(Comparator<V> comparator) {
		return getSource().sort(comparator);
	}
	
	@Override 
	default V getValue(K key) {
		return getSource().getValue(key);
	}

	@Override
	default Collection<V> addListener(Listener<V> listener) {
		return getSource().addListener(listener);
	}
	
	@Override
	default void removeListener(Listener<V> listener) {
		getSource().removeListener(listener);
	}
}
