package com.moobin.util.impl;

import java.util.Map;

import com.moobin.util.EntityCollections;
import com.moobin.util.EntityCollection;

public class EntityCacheImpl implements EntityCollections {

	Map<Object, EntityCollection<?, ?>> map;
	
	@SuppressWarnings("unchecked")
	@Override
	public <K, V> EntityCollection<K, V> getCollection(Class<V> clazz) {
		return (EntityCollection<K, V>) map.get(clazz);
	}

	@Override
	public <K, V> EntityCollection<K, V> getCollection(String name) {
		return (EntityCollection<K, V>) map.get(name);
	}

}
