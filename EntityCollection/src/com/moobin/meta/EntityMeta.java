package com.moobin.meta;

public interface EntityMeta<V, K> extends ItemMeta<V> {

	Class<K> getKeyType();
	
	default Class<V> entityType() {
		return getItemType();
	}

	K getKey(V value);
	
	<T> PropertyMeta<K, ?> getPropretyMeta(String name);
	
}
