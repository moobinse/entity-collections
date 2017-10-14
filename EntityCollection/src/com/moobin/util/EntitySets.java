package com.moobin.util;

public interface EntitySets {

	<K, V> EntitySet<K, V> getSet(Class<V> clazz);

	/**
	 * 
	 * @param name
	 * @return
	 */
	<K, V> EntitySet<K, V> getSet(String name);

}
