package com.moobin.meta;

public interface EntityMeta<V, K> {

	K getKey(V value);
	
	Class<K> keyType();
	
	Class<V> entityType();
	
}
