package com.moobin.util.impl;

import java.util.function.Function;
import java.util.function.Predicate;

import com.moobin.util.Factory;
import com.moobin.util.EntityCollection;

public class FactoryImpl implements Factory {

	@Override
	public <K, V> EntityCollection<K, V> create(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction) {
		return new EntityCollectionImpl(keyType, valueType, keyFunction);
	}

	@Override
	public <K, V> EntityCollection<K, V> filter(EntityCollection<K, V> source, Predicate<V> filter) {
		return new SubCollection<>(source, filter);
	}
	
}
