package com.moobin.util.impl;

import java.util.function.Function;
import java.util.function.Predicate;

import com.moobin.util.EntitySetBuilder;
import com.moobin.util.EntitySet;

public class FactoryImpl implements EntitySetBuilder {

	@Override
	public <K, V> EntitySet<K, V> create(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction) {
		return new EntityCollectionImpl(keyType, valueType, keyFunction);
	}

	@Override
	public <K, V> EntitySet<K, V> filter(EntitySet<K, V> source, Predicate<V> filter) {
		return new SubCollection<>(source, filter);
	}
	
}
