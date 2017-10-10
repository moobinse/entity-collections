package com.moobin.util.impl;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import com.moobin.meta.EntityMeta;
import com.moobin.meta.PropertyMeta;
import com.moobin.util.EntitySet;
import com.moobin.util.EntitySetBuilder;

public class EntitySetBuilderImpl implements EntitySetBuilder {

	@Override
	public <K, V> EntitySet<K, V> create(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction) {
		EntityMeta<V, K> meta = new EntityMeta<V, K>() {

			@Override
			public Class<V> getType() {
				return valueType;
			}

			@Override
			public Map<String, PropertyMeta<V, ?>> getProperties() {
				return Collections.emptyMap();
			}

			@Override
			public Class<K> getKeyType() {
				return keyType;
			}

			@Override
			public K getKey(V value) {
				return keyFunction.apply(value);
			}

		};
		return new EntitySetImpl<>(meta);
	}

}
