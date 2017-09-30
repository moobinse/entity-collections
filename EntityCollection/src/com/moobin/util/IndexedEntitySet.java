package com.moobin.util;

public interface IndexedEntitySet<K, V> extends EntitySet<K, V> {

	V get(int index);
	
	int indexByKey(K key);
	
	default int indexByValue(V value) {
		return value == null ? -1 : indexByKey(getKey(value));
	}

}
