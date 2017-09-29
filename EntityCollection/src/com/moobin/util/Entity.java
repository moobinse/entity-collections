package com.moobin.util;

public interface Entity<V, K> {

	K getKey(V value);
	
	Class<K> keyType();
	
	Class<V> entityType();
	
}
