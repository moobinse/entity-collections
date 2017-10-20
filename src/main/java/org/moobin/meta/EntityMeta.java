package org.moobin.meta;

public interface EntityMeta<V, K> extends Meta<V> {

	Class<K> getKeyType();
	
	K getKey(V value);
	
}
