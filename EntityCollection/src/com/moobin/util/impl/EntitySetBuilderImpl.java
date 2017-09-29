package com.moobin.util.impl;

import java.util.function.Function;
import java.util.function.Predicate;

import com.moobin.util.Entity;
import com.moobin.util.EntitySet;
import com.moobin.util.EntitySetBuilder;

public class EntitySetBuilderImpl implements EntitySetBuilder {

	@Override
	public <K, V> EntitySet<K, V> create(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction) {

		Entity<V, K> entityDef = new Entity<V, K>() {

			@Override
			public K getKey(V value) {
				return keyFunction.apply(value);
			}

			@Override
			public Class<K> keyType() {
				return keyType;
			}

			@Override
			public Class<V> entityType() {
				return valueType;
			}
			
		};
		return new EntitySetImpl<>(entityDef);
	}

	@Override
	public <K, V> EntitySet<K, V> filter(EntitySet<K, V> source, Predicate<V> filter) {
		return new EntitySubSet<>(source, filter);
	}
	
}
