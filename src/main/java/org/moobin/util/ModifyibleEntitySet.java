package org.moobin.util;

public interface ModifyibleEntitySet<K extends Comparable<K>, V> extends EntitySet<K, V> {

	V update(V value);
	
	V removeByKey(K key);
	
	default V remove(V value) {
		return removeByKey(getKey(value));
	}
	
	void clear();

	void destroy();

}
