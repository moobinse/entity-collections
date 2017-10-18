package com.moobin.util.impl;

import java.util.function.Function;

import com.moobin.meta.EntityMeta;
import com.moobin.meta.impl.EntityMetaImpl;
import com.moobin.util.EntitySet;
import com.moobin.util.EntitySetBuilder;

public class EntitySetBuilderImpl implements EntitySetBuilder {

	@Override
	public <K extends Comparable<K>, V> EntitySet<K, V> create(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction) {

		EntityMeta<V, K> entityDef = new EntityMetaImpl<V, K>(valueType, keyType, keyFunction);
		return new EntitySetImpl<>(entityDef);
	}

}
