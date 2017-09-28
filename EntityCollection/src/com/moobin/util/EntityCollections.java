package com.moobin.util;

public interface EntityCollections {

	<K, V> EntityCollection<K, V> getCollection(Class<V> clazz);

	/**
	 * 
	 * @param name
	 * @return
	 */
	<K, V> EntityCollection<K, V> getCollection(String name);

}
