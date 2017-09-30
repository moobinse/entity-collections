package com.moobin.util.impl;

import java.util.function.Function;

import com.moobin.meta.EntityMeta;
import com.moobin.util.EntitySet;
import com.moobin.util.EntitySetBuilder;

public class EntitySetBuilderImpl implements EntitySetBuilder {

	@Override
	public <K, V> EntitySet<K, V> create(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction) {

		EntityMeta<V, K> entityDef = new EntityMeta<V, K>() {

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

}
