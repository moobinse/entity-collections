package com.moobin.meta.impl;

import java.util.function.Function;

import com.moobin.meta.EntityMeta;
import com.moobin.meta.PropertyMeta;

public class EntityMetaImpl<V, K> implements EntityMeta<V, K> {

	private Function<V, K> keyFunction;
	private Class<K> keyType;
	private Class<V> valueType;

	public EntityMetaImpl(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction) {
		this.keyFunction = keyFunction;
		this.valueType = valueType;
		this.keyType = keyType;
	}

	@Override
	public K getKey(V value) {
		return keyFunction.apply(value);
	}

	@Override
	public Class<K> getKeyType() {
		return keyType;
	}

	@Override
	public Class<V> getItemType() {
		return valueType;
	}

	@Override
	public <T> PropertyMeta<K, ?> getPropretyMeta(String name) {
		return null;
	}

}
