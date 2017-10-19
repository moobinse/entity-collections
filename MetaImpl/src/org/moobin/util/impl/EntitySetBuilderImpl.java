package org.moobin.util.impl;

import java.util.function.Function;

import org.moobin.meta.EntityMeta;
import org.moobin.meta.impl.EntityMetaImpl;
import org.moobin.util.EntitySet;
import org.moobin.util.EntitySetBuilder;
import org.moobin.util.impl.EntitySetImpl;

public class EntitySetBuilderImpl implements EntitySetBuilder {

	@Override
	public <K extends Comparable<K>, V> EntitySet<K, V> create(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction) {

		EntityMeta<V, K> entityDef = new EntityMetaImpl<V, K>(valueType, keyType, keyFunction);
		return new EntitySetImpl<>(entityDef);
	}

}
