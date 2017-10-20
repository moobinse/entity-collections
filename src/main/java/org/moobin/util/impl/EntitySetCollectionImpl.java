package org.moobin.util.impl;

import java.util.Map;

import org.moobin.util.EntitySet;
import org.moobin.util.EntitySetCollection;

public class EntitySetCollectionImpl implements EntitySetCollection {

	Map<Object, EntitySet<?, ?>> map;
	
	@SuppressWarnings("unchecked")
	@Override
	public <K extends Comparable<K>, V> EntitySet<K, V> getSet(Class<V> clazz) {
		return (EntitySet<K, V>) map.get(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K extends Comparable<K>, V> EntitySet<K, V> getSet(String name) {
		return (EntitySet<K, V>) map.get(name);
	}

}
