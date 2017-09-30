package com.moobin.util;

public interface ModifyibleEntitySet<K, V> extends EntitySet<K, V> {

	V update(V value);
	
	V removeByKey(K key);
	
	void clear();
	
	default V remove(V value) {
		return removeByKey(getKey(value));
	}

}
