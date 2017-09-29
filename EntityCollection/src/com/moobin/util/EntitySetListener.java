package com.moobin.util;

public interface EntitySetListener<V> {

	void onAdd(V value);
	
	void onUpdate(V value, V oldValue);

	void onRemove(V value);
	
	void onClear();
	
}
