package com.moobin.meta;

public interface EntityMeta<V, K> {

	Class<K> getKeyType();
	
	Class<V> entityType();

	K getKey(V value);
	
	<T> PropertyMeta<K, ?> getPropretyMeta(String name);
	
}
