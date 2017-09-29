package com.moobin.util;

public interface IndexedEntitySet<K extends Comparable<K>, V> {

	V get(int index);
	
	int size();
	
	V getValue(K key);
	
	K getKey(V value);
	
	int indexOf(K key);
	
	default int indexOf(V value) {
		return value == null ? -1 : indexOf(getKey(value));
	}
	
	V update(V value);
	
	V remove(K key);
	
	default V remove(V value) {
		return remove(getKey(value));
	}
}
