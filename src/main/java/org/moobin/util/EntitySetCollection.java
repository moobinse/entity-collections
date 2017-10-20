package org.moobin.util;

public interface EntitySetCollection {

	<K extends Comparable<K>, V> EntitySet<K, V> getSet(Class<V> clazz);

	/**
	 * 
	 * @param name
	 * @return
	 */
	<K extends Comparable<K>, V> EntitySet<K, V> getSet(String name);

}
