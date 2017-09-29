package com.moobin.util.impl;

import java.util.Map;

import com.moobin.util.EntitySets;
import com.moobin.util.EntitySet;

public class EntityCacheImpl implements EntitySets {

	Map<Object, EntitySet<?, ?>> map;
	
	@SuppressWarnings("unchecked")
	@Override
	public <K, V> EntitySet<K, V> getCollection(Class<V> clazz) {
		return (EntitySet<K, V>) map.get(clazz);
	}

	@Override
	public <K, V> EntitySet<K, V> getCollection(String name) {
		return (EntitySet<K, V>) map.get(name);
	}

}
