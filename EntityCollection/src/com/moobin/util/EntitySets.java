package com.moobin.util;

public interface EntitySets {

	<K, V> EntitySet<K, V> getCollection(Class<V> clazz);

	/**
	 * 
	 * @param name
	 * @return
	 */
	<K, V> EntitySet<K, V> getCollection(String name);

}
