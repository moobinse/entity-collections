package com.moobin.util;

import java.util.function.Function;
import java.util.function.Predicate;

public interface EntitySetBuilder {

	<K, V> EntitySet<K, V> create(Class<V> valueType, Class<K> keyType, Function<V, K> keyFunction);
	
	<K, V> EntitySet<K, V> filter(EntitySet<K, V> source, Predicate<V> filter);
	
}
